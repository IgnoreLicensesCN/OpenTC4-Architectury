package thaumcraft.api.scan.itemstack;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ItemStackScanContext{
    public final LivingEntity livingScanning;
    public final ItemStack stack;
    public boolean shouldBreak = false;

    protected ItemStackScanContext(LivingEntity livingScanning, ItemStack stack) {
        this.livingScanning = livingScanning;
        this.stack = stack;
    }

}
