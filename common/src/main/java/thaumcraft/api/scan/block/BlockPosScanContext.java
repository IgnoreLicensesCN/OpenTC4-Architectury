package thaumcraft.api.scan.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPosScanContext {
    public final ServerPlayer playerScanning;
    public final BlockState bState;
    public final BlockPos pos;
    public boolean shouldBreak = false;

    protected BlockPosScanContext(ServerPlayer playerScanning, BlockState bState, BlockPos pos) {
        this.playerScanning = playerScanning;
        this.bState = bState;
        this.pos = pos;
    }

}
