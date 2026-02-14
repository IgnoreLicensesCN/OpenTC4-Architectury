package thaumcraft.common.blocks.crafted.ownedblock;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.crafted.OwnedBlockEntity;

public class ArcaneDoorBlock extends DoorBlock implements IWandInteractableBlock,EntityBlock {
    public ArcaneDoorBlock(Properties properties, BlockSetType blockSetType) {
        super(properties, blockSetType);
    }
    public ArcaneDoorBlock(){
        super(
                Properties.copy(Blocks.IRON_DOOR)
                        .strength(-1/*Config.wardedStone ? -1.0F : 15.0F*/,999.F)
                ,BlockSetType.IRON);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OwnedBlockEntity(blockPos, blockState);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, pos, blockState, livingEntity, itemStack);
        if (Platform.getEnvironment() == Env.SERVER) {
            var player = (livingEntity instanceof Player player0) ? player0:null;
            if (player != null) {
                var ownedBlockEntity = level.getBlockEntity(pos);
                if (ownedBlockEntity instanceof OwnedBlockEntity owned){
                    owned.owners.add(player.getGameProfile().getName());
                }else if (ownedBlockEntity == null){
                    var owned = new OwnedBlockEntity(pos,this.defaultBlockState());
                    owned.owners.add(player.getGameProfile().getName());
                    level.setBlockEntity(owned);
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var clickedPos = useOnContext.getClickedPos();
        var blockState = level.getBlockState(clickedPos);
        var blockEntity = level.getBlockEntity(clickedPos);
        if (blockEntity instanceof OwnedBlockEntity owned){
            var player = useOnContext.getPlayer();
            if (player == null) return InteractionResult.PASS;
            var playerName = player.getGameProfile().getName();
            if (owned.owners.contains(playerName)){
                level.destroyBlock(clickedPos, true, player);
                level.playSound(player,clickedPos, SoundType.STONE.getBreakSound(), SoundSource.BLOCKS);
                if (level instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(
                            new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                            clickedPos.getX() + 0.5,
                            clickedPos.getY() + 0.5,
                            clickedPos.getZ() + 0.5,
                            20,     // 数量
                            0.3, 0.3, 0.3, // 扩散
                            0.1     // 速度
                    );
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(BlockState arg, Level level, BlockPos pos, Player player, InteractionHand arg5, BlockHitResult arg6) {
        var ownedBlockEntity = level.getBlockEntity(pos);
        if (ownedBlockEntity instanceof OwnedBlockEntity owned){

            if (owned.owners.contains(player.getGameProfile().getName())){
                arg = arg.cycle(OPEN);
                level.setBlock(pos, arg, 10);
                this.playSound(player, level, pos, arg.getValue(OPEN));
                level.gameEvent(player, this.isOpen(arg) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }else if (Platform.getEnvironment() == Env.SERVER){
                player.sendSystemMessage(Component.translatable("thaumcraft.open_arcane_door_failed"));
                level.playSound(player,pos, ThaumcraftSounds.DOOR_FAIL, SoundSource.BLOCKS, 0.66F, 1.0F);
            }
        }
        return InteractionResult.PASS;
    }
    private void playSound(@Nullable Entity entity, Level level, BlockPos blockPos, boolean bl) {
        level.playSound(entity, blockPos, bl ? BlockSetType.IRON.doorOpen() : BlockSetType.IRON.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }
}
