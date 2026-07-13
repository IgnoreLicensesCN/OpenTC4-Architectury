package thaumcraft.common.blocks.crafted.visdevice;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.abstracts.AbstractExtendedMenuProviderContainerBlock;
import thaumcraft.api.research.ThaumcraftResearches;
import thaumcraft.common.tiles.crafted.vis.FocalManipulatorBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

public class FocalManipulatorBlock extends AbstractExtendedMenuProviderContainerBlock implements EntityBlock {
    public FocalManipulatorBlock(Properties properties) {
        super(properties);
    }
    public FocalManipulatorBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FocalManipulatorBlockEntity(blockPos,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof FocalManipulatorBlockEntity focalManipulator){
                    focalManipulator.serverTick();
                }
            });
        }
        return null;
    }
    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof ExtendedMenuProvider menuProvider) {
                if (ThaumcraftResearches.FOCAL_MANIPULATION.isLivingEntityCompletedResearch(serverPlayer)) {
                    openExtendedMenu(serverPlayer,menuProvider);
                }else {
                    Component.translatable("tc.researchmissing").withStyle(ChatFormatting.RED);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide){
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof Container container) {
                Containers.dropContents(level, pos, container);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (!level.isClientSide){
            return;
        }
        if ((level instanceof ClientLevel clientLevel && LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof FocalManipulatorBlockEntity be)) {
            if (!be.centiVisListView.isEmpty()) {
                var pos = be.getBlockPos().getCenter();
                ClientFXUtils.drawGenericParticles(
                        clientLevel,
                        pos.x + ((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F),
                        pos.y + 0.75 +  ((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F),
                        pos.z +  ((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F),
                        0.0F, 0.0F, 0.0F,
                        0.5F + clientLevel.random.nextFloat() * 0.4F,
                        1.0F - clientLevel.random.nextFloat() * 0.4F,
                        1.0F - clientLevel.random.nextFloat() * 0.4F,
                        0.8F,
                        false,
                        112,
                        9,
                        1,
                        6 + clientLevel.random.nextInt(5),
                        0,
                        0.7F + clientLevel.random.nextFloat() * 0.4F
                );
            }
        }
    }
}
