package thaumcraft.common.lib.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.effects.PreventMilkRemoveEffect;
import thaumcraft.api.effects.VisCostAddEffectWithCategory;
import thaumcraft.common.Thaumcraft;

public class VisExhaustEffect extends MobEffect implements VisCostAddEffectWithCategory, PreventMilkRemoveEffect {
    public static final String VIS_ADD_CATEGORY = Thaumcraft.MOD_ID + ":vis_exhaust";
    protected VisExhaustEffect() {
        super(MobEffectCategory.HARMFUL, 0x664477);
    }
    protected VisExhaustEffect(MobEffectCategory effectCategory,int color) {
        super(effectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {}

    @Override
    public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity2, LivingEntity livingEntity, int i, double d) {}

    @Override
    public String getVisCostAddCategory() {
        return VIS_ADD_CATEGORY;
    }

    @Override
    public int getVisCostAddPercentage(int amplifier) {
        return 10;
    }
}
