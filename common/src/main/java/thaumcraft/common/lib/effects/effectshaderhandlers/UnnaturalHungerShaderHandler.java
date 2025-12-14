package thaumcraft.common.lib.effects.effectshaderhandlers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.effects.UnnaturalHungerEffect;

public class UnnaturalHungerShaderHandler extends AbstractShaderHandler{
    public static final UnnaturalHungerShaderHandler INSTANCE = new UnnaturalHungerShaderHandler();

    public UnnaturalHungerShaderHandler() {
        super(new ResourceLocation(Thaumcraft.MOD_ID,"shaders/post/hunger.json"), ThaumcraftEffects.UNNATURAL_HUNGER);
    }
}
