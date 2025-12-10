package thaumcraft.client.renderers.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public interface ThaumcraftItemRenderer {
    public void render(
            ItemStack itemStack,
            ItemDisplayContext itemDisplayContext,
            boolean bl,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            int j,
            BakedModel bakedModel
    );
}
