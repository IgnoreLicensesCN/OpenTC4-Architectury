package thaumcraft.api.listeners.warp.consts;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.listeners.warp.PickWarpEventContext;
import thaumcraft.api.listeners.warp.WarpConditionChecker;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

public class WarpConditions {

    public static final WarpConditionChecker WARP_AND_COUNTER = new WarpConditionChecker(0) {
        @Override
        public boolean check(PickWarpEventContext context, LivingEntity living) {
            return context.warpCounter > 0 && context.warp > 0 && living.getRandom().nextInt(100)
                    <= Math.sqrt(context.warpCounter);
        }
    };
    public static final WarpConditionChecker NO_WARP_WARD = new WarpConditionChecker(1) {
        @Override
        public boolean check(PickWarpEventContext context, LivingEntity living) {
            return living.hasEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.WARP_WARD());
        }
    };
}
