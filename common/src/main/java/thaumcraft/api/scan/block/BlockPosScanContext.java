package thaumcraft.api.scan.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPosScanContext {
    public final LivingEntity livingScanning;
    public final BlockState bState;
    public final BlockPos pos;
    public boolean shouldBreak = false;

    protected BlockPosScanContext(LivingEntity livingScanning, BlockState bState, BlockPos pos) {
        this.livingScanning = livingScanning;
        this.bState = bState;
        this.pos = pos;
    }

}
