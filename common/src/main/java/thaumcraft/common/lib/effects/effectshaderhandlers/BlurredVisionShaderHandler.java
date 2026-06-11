package thaumcraft.common.lib.effects.effectshaderhandlers;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;

import static thaumcraft.common.lib.effects.ThaumcraftEffects.ThaumcraftEffectTypeInstances.BLURRED_VISION;

public class BlurredVisionShaderHandler extends AbstractShaderHandler {
    public static final BlurredVisionShaderHandler INSTANCE = new BlurredVisionShaderHandler();
    public BlurredVisionShaderHandler() {
        super(new ResourceLocation(Thaumcraft.MOD_ID,"shaders/post/blurtc.json"), BLURRED_VISION);
    }
}
