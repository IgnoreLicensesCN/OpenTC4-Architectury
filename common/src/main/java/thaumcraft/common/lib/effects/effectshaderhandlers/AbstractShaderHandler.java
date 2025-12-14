package thaumcraft.common.lib.effects.effectshaderhandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractShaderHandler {
    public final ResourceLocation shaderLocation;
    public final MobEffect effect;
    public AbstractShaderHandler(ResourceLocation shaderLocation, MobEffect effect) {
        this.shaderLocation = shaderLocation;
        this.effect = effect;
    }

    protected PostChain effectPostChain;

    public void tick(@NotNull LocalPlayer player) {
        boolean has = player.hasEffect(effect);

        if (has) {
            enable();
        } else {
            disable();
        }
    }

    private void enable() {
        var mc = Minecraft.getInstance();
        if (effectPostChain != null) return;

        try {
            effectPostChain = new PostChain(
                    mc.getTextureManager(),
                    mc.getResourceManager(),
                    mc.getMainRenderTarget(),
                    shaderLocation
            );
            effectPostChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        } catch (Exception e) {
            effectPostChain = null;
        }
    }

    private void disable() {
        if (effectPostChain != null) {
            effectPostChain.close();
            effectPostChain = null;
        }
    }
}
