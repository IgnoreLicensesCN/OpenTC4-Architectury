package thaumcraft.client.renderers.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class ItemRenderListener implements Comparable<ItemRenderListener> {
    public final int weight;
    public ItemRenderListener(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull ItemRenderListener o) {
        return Integer.compare(weight, o.weight);
    }

    //true if cancel further renderers and vanilla #render
    public abstract boolean render(
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
