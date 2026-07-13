package thaumcraft.common.blocks.crafted.ownedblock;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.abstracts.wandabstraction.wandinteractable.IWandInteractableOwnedBlock;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.abstracts.owned.KeyableOwnedBlockEntity;

public class ArcaneDoorBlock extends DoorBlock
        implements
        IWandInteractableOwnedBlock {
    public ArcaneDoorBlock(Properties properties, BlockSetType blockSetType) {
        super(properties, blockSetType);
    }
    public ArcaneDoorBlock(){
        this(
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
    public @NotNull InteractionResult use(BlockState arg, Level level, BlockPos pos, Player player, InteractionHand arg5, BlockHitResult arg6) {
        if (arg.getValue(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.below();
            arg = arg.setValue(HALF, DoubleBlockHalf.LOWER);
        }
        var ownedBlockEntity = LevelBlockEntityAccessing.getExistingBlockEntity(level, pos);
        if (ownedBlockEntity instanceof KeyableOwnedBlockEntity owned){

            if (owned.playerCanUseThis(player)){
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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER){
            return null;
        }
        return new KeyableOwnedBlockEntity(blockPos, blockState);
    }
}
