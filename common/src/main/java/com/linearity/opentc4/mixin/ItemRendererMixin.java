package com.linearity.fabrictests.mixin.client;

import com.linearity.fabrictests.mixinaccessor.ItemRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import renders.ThaumometerItemRenderer;

import static com.linearity.fabrictests.Fabrictests.THAUMOMETER_ITEM;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin implements ItemRendererAccessor {

    @Inject(method = "render",
            at = @At("HEAD"),
            cancellable = true)
    private void render(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel, CallbackInfo ci) {
        if (itemStack != null){
            if (itemStack.getItem() == THAUMOMETER_ITEM){
                ThaumometerItemRenderer.INSTANCE.render(itemStack,itemDisplayContext,bl,poseStack,multiBufferSource,i,j,bakedModel);
                ci.cancel();
            }
        }
    }

    @Shadow private void renderModelLists(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer){
        throw new RuntimeException("should not reach here");
    }

    @Override
    public void opentc4$renderModelLists(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer) {
        renderModelLists(bakedModel,itemStack,i,j,poseStack,vertexConsumer);
    }
}

