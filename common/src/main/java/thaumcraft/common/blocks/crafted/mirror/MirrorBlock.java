package thaumcraft.common.blocks.crafted.mirror;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.tiles.crafted.mirror.MirrorBlockEntity;

import java.util.List;

public class MirrorBlock extends AbstractMirrorBlock implements EntityBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MirrorBlockEntity(blockPos,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()) {
            return (level1, blockPos, blockState1, be) -> {
                if (be instanceof MirrorBlockEntity mirror) {
                    mirror.serverTick();
                }
            };
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof MirrorBlockEntity mirror) {
            mirror.blockOnRemoved();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        return List.of();//we will drop in BE
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (i != 1) {
            return super.triggerEvent(blockState, level, blockPos, i, j);
        } else {
            if (level.isClientSide() ) {
                if (level instanceof ClientLevel clientLevel) {
                    Direction facing = blockState.getValue(FACING);
                    var x = blockPos.getX();
                    var y = blockPos.getY();
                    var z = blockPos.getZ();
                    for(int q = 0; q < ClientFXUtils.particleCount(1); ++q) {
                        double xx = x + 0.33 + (double)(level.random.nextFloat() * 0.33F) - (double)facing.getStepX() / (double)2.0F;
                        double yy = y + 0.33 + (double)(level.random.nextFloat() * 0.33F) - (double)facing.getStepY() / (double)2.0F;
                        double zz = z + 0.33 + (double)(level.random.nextFloat() * 0.33F) - (double)facing.getStepZ() / (double)2.0F;
                        var particleEngine = Minecraft.getInstance().particleEngine;
                        var particle = particleEngine.createParticle(
                                ParticleTypes.WITCH,
                                xx,yy,zz,
                                facing.getStepX() * 0.05,facing.getStepY() * 0.05,facing.getStepZ() * 0.05
                        );
                        if (particle != null) {
                            particle.setColor(0,0,0);
                            particleEngine.add(particle);
                        }
                    }
                }
            }
            return true;
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);
        if (!level.isClientSide && entity instanceof ItemEntity itemEntity && level.getBlockEntity(blockPos) instanceof MirrorBlockEntity mirror) {
            mirror.transportItemEntity(itemEntity);
        }
    }

}
