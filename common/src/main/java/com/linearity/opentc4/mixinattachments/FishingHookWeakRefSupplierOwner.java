package com.linearity.opentc4.mixinattachments;

import com.linearity.annotations.InterfaceShouldAttachTo;
import net.minecraft.world.entity.projectile.FishingHook;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.function.Supplier;

@InterfaceShouldAttachTo(FishingHook.class)
public interface FishingHookWeakRefSupplierOwner {
    FishingHookWeakRefSupplier opentc4$getFishingHookWeakRefSupplier();

    class FishingHookWeakRefSupplier implements Supplier<@Nullable FishingHook> {
        public final WeakReference<FishingHook> fishingHookRef;
        public FishingHookWeakRefSupplier(FishingHook fishingHook) {
            this.fishingHookRef = new WeakReference<>(fishingHook);
        }
        public @Nullable FishingHook get(){
            var result = fishingHookRef.get();
            if (result != null){
                if (result.isRemoved()){
                    fishingHookRef.clear();
                    return null;
                }
            }
            return result;
        }
    }
}
