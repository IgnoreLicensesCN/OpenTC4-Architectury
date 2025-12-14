package thaumcraft.api.expands.warp.consts;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpConditionChecker;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

public class WarpConditions {

    public static final WarpConditionChecker WARP_AND_COUNTER = new WarpConditionChecker(0) {
        @Override
        public boolean check(PickWarpEventContext context, Player player) {
            return context.warpCounter > 0 && context.warp > 0 && player.getRandom().nextInt(100)
                    <= Math.sqrt(context.warpCounter);
        }
    };
    public static final WarpConditionChecker NO_WARP_WARD = new WarpConditionChecker(1) {
        @Override
        public boolean check(PickWarpEventContext context, Player player) {
            return player.hasEffect(ThaumcraftEffects.WARP_WARD);
        }
    };
}
