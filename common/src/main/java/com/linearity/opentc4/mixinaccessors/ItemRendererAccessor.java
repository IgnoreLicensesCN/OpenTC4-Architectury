package com.linearity.fabrictests.mixinaccessor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;


public interface ItemRendererAccessor {

    void opentc4$renderModelLists(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer);
}
