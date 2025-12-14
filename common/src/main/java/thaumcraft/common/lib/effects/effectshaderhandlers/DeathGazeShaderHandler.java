package thaumcraft.common.lib.effects.effectshaderhandlers;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

public class DeathGazeShaderHandler extends AbstractShaderHandler {
    public static final DeathGazeShaderHandler INSTANCE = new DeathGazeShaderHandler();
    public DeathGazeShaderHandler() {
        super(new ResourceLocation(Thaumcraft.MOD_ID, "shaders/post/desaturatetc.json"),ThaumcraftEffects.DEATH_GAZE);
    }
}
