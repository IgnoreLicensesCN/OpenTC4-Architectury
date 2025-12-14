package thaumcraft.common.lib.effects;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.api.effects.PreventMilkRemoveEffect;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.FiniteLiquidBlock;
import thaumcraft.common.config.ConfigBlocks;

public class ThaumarhiaEffect extends MobEffect implements PreventMilkRemoveEffect {
    protected ThaumarhiaEffect() {
        super(MobEffectCategory.HARMFUL, 0x664477);
    }

    @Override
    public void applyEffectTick(LivingEntity target, int i) {

        if (target.getRandom().nextInt(15) == 0) {
            var pos = target.blockPosition();
            var level = target.level();
            if (level.getBlockState(target.blockPosition()).isAir()) {
                var gooState = ThaumcraftBlocks.FLUX_GOO.defaultBlockState();
                var stateOne = gooState.setValue(FiniteLiquidBlock.LEVEL,1);
                level.setBlock(pos, stateOne, FiniteLiquidBlock.UPDATE_ALL);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return i%20 == 0;
    }
}
