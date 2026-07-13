package thaumcraft.common.items.wands;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

//set to a living entity
public class WandCooldownManager {
    //as a mixin point?
    public static @Nullable WandCooldownManager getFromLiving(LivingEntity livingEntity) {
        if (livingEntity instanceof IWandCooldownManagerOwnerLivingEntity owner) {
            return owner.getWandCooldownManager();
        }
        return null;
    }

    @ApiStatus.Internal
    public int cooldownUntilNextTick = 0;

    public boolean isOnCooldown(LivingEntity living){
        return living.tickCount < cooldownUntilNextTick;
    }
    public void setCooldown(LivingEntity living, int ticks) {
        cooldownUntilNextTick = ticks + living.tickCount;
    }

    public int getRemainingCooldown(LivingEntity living) {
        return Math.max(0,cooldownUntilNextTick - living.tickCount);
    }
}
