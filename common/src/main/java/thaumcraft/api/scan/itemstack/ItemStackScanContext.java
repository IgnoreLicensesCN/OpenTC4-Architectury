package thaumcraft.api.scan.itemstack;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemStackScanContext{
    public final ServerPlayer playerScanning;
    public final ItemStack stack;
    public boolean shouldBreak = false;

    protected ItemStackScanContext(ServerPlayer playerScanning, ItemStack stack) {
        this.playerScanning = playerScanning;
        this.stack = stack;
    }

}
