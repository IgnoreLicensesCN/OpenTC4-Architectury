package thaumcraft.common.blocks.crafted.ownedblock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableOwnedBlock;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.crafted.OwnedBlockEntity;

public class ArcaneDoorBlock extends DoorBlock
        implements
        IWandInteractableOwnedBlock {
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
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level,blockPos,blockState,livingEntity,itemStack);
        setPlacedOwnedBlockBy(level, blockPos, blockState, livingEntity, itemStack);
    }

    @Override
    public InteractionResult use(BlockState arg, Level level, BlockPos pos, Player player, InteractionHand arg5, BlockHitResult arg6) {
        var ownedBlockEntity = level.getBlockEntity(pos);
        if (ownedBlockEntity instanceof OwnedBlockEntity owned){

            if (owned.playerOwnThis(player)){
                arg = arg.cycle(OPEN);
                level.setBlock(pos, arg, 10);
                this.playSound(player, level, pos, arg.getValue(OPEN));
                level.gameEvent(player, this.isOpen(arg) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }else if (!level.isClientSide){
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
