package thaumcraft.common.lib.effects.effectshaderhandlers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

public class SunScornedShaderHandler extends AbstractShaderHandler {
    public static final SunScornedShaderHandler INSTANCE = new SunScornedShaderHandler();
    public SunScornedShaderHandler() {
        super(new ResourceLocation(Thaumcraft.MOD_ID,"shaders/post/sunscorned.json"), ThaumcraftEffects.SUN_SCORNED);
    }
}
