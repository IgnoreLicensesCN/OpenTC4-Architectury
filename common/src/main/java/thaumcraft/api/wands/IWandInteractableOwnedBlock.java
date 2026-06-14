package thaumcraft.api.wands;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.abstracts.owned.OwnedBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

@UtilityLikeAbstraction(reason = "it's harmful to write the same logic in every place but it's also not suitable to make it static")
public interface IWandInteractableOwnedBlock extends IWandInteractableBlockOrBlockEntity, EntityBlock {
    @Override
    default @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        if (!level.isClientSide()) {
            var clickedPos = useOnContext.getClickedPos();
            var blockEntity = LevelBlockEntityAccessing.getExistingBlockEntity(level, clickedPos);
            if (blockEntity instanceof OwnedBlockEntity owned){
                var player = useOnContext.getPlayer();
                if (player == null) return InteractionResult.PASS;
                if (owned.playerOwnThis(player)){
                    onOwnerClicked(useOnContext);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    default BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OwnedBlockEntity(blockPos, blockState);
    }

    default void onOwnerClicked(UseOnContext useOnContext) {
        var clickedPos = useOnContext.getClickedPos();
        var level = useOnContext.getLevel();
        var blockState = level.getBlockState(clickedPos);
        var player = useOnContext.getPlayer();
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
    }

    //usually should be called when "setPlacedBy"
    default void setPlacedOwnedBlockBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
//        super.setPlacedBy(level, pos, blockState, livingEntity, itemStack);
        if (!level.isClientSide()) {
            var player = (livingEntity instanceof Player player0) ? player0 : null;
            if (player != null) {
                var ownedBlockEntity = LevelBlockEntityAccessing.getExistingBlockEntity(level, pos);
                if (ownedBlockEntity instanceof OwnedBlockEntity owned){
                    owned.addOwner(player);
                }else if (ownedBlockEntity == null){
                    var owned = new OwnedBlockEntity(pos,blockState);
                    owned.addOwner(player);
                    level.setBlockEntity(owned);
                }
            }
        }
    }
}
