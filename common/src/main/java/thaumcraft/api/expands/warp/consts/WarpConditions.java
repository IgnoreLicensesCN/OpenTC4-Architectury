package thaumcraft.api.expands.warp.consts;

import net.minecraft.entity.player.Player;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpConditionChecker;
import thaumcraft.common.config.Config;

public class WarpConditions {
    public static final WarpConditionChecker WARP_AND_COUNTER = new WarpConditionChecker(0) {
        @Override
        public boolean check(PickWarpEventContext context, Player player) {
            if (player.worldObj == null) {
                return false;
            }
            return context.warpCounter > 0 && context.warp > 0 && (double)player.worldObj.rand.nextInt(100)
                    <= Math.sqrt(context.warpCounter);
        }
    };
    public static final WarpConditionChecker NO_WARP_WARD = new WarpConditionChecker(1) {
        @Override
        public boolean check(PickWarpEventContext context, Player player) {
            return !player.isPotionActive(Config.potionWarpWardID);
        }
    };
}
