package thaumcraft.api.entityrender;

import net.minecraft.resources.ResourceLocation;

public interface ShieldRunesFXGetter {
    default ResourceLocation getShieldRunesFXTexture(int frame) {
        return new ResourceLocation(
                "thaumcraft",
                "textures/models/ripple" + frame + ".png"
        );
    }
}
