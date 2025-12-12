package thaumcraft.common.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.particles.FXSparkle;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.items.ThaumcraftItems;

public class NitorBlock extends Block {

    public NitorBlock() {
        super(Properties.of()
                .strength(0.2F)
                .lightLevel(s -> 15)
                .noOcclusion()
        );
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE; // 像老TC一样不可见，只是光源
    }
    @Deprecated
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    private static final VoxelShape SHAPE = Block.box(
            0.3 * 16, 0.3 * 16, 0.3 * 16,
            0.7 * 16, 0.7 * 16, 0.7 * 16
    );
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(ThaumcraftItems.NITOR);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        if (!(world instanceof ClientLevel clientLevel)) return;
        FXSparkle ef2 = new FXSparkle(
                clientLevel, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5,
                (float) pos.getX() + 0.5F + (rand.nextFloat() - rand.nextFloat()) / 3.0F,
                (float) pos.getY() + 0.5F + (rand.nextFloat() - rand.nextFloat()) / 3.0F,
                (float) pos.getZ() + 0.5F + (rand.nextFloat() - rand.nextFloat()) / 3.0F, 1.0F, 6, 3
        );
        ef2.setGravity(0.05F);
        ef2.setNoClip(true);
        Minecraft.getInstance().particleEngine.add(ef2);
        if (rand.nextInt(9 - ClientFXUtils.particleCount(2)) == 0) {
            ClientFXUtils.wispFX3(
                    clientLevel, (float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F,
                    (float) pos.getX() + 0.3F + rand.nextFloat() * 0.4F, (float) pos.getY() + 0.5F,
                    (float) pos.getZ() + 0.3F + rand.nextFloat() * 0.4F, 0.5F, 4, true, -0.025F
            );
        }

        if (rand.nextInt(15 - ClientFXUtils.particleCount(4)) == 0) {
            ClientFXUtils.wispFX3(
                    clientLevel, (float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F,
                    (float) pos.getX() + 0.4F + rand.nextFloat() * 0.2F, (float) pos.getY() + 0.5F,
                    (float) pos.getZ() + 0.4F + rand.nextFloat() * 0.2F, 0.25F, 1, true, -0.02F
            );
        }
    }
}
