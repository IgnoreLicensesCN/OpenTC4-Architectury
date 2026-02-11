package thaumcraft.client.renderers.item;

import com.linearity.opentc4.mixinaccessors.ItemRendererAccessor;
import com.linearity.opentc4.OpenTC4CommonProxy;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ThaumometerItemRenderer implements IThaumcraftItemRenderer {

    public static final ThaumometerItemRenderer INSTANCE = new ThaumometerItemRenderer();

//    private static final ResourceLocation SCANNER_OBJ = new ResourceLocation("thaumcraft", "textures/models/scanner.obj");//loaded
    private static final ResourceLocation SCAN_SCREEN = new ResourceLocation("thaumcraft", "textures/item/scanscreen.png");


    //TODO:Render aspects.
    @Override
    public boolean render(ItemStack stack, ItemDisplayContext context, boolean bl, PoseStack poseStack,
                       MultiBufferSource bufferSource, int light, int overlay, BakedModel bakedModel) {
        if (Platform.getEnv() != EnvType.CLIENT) {
            return false;
        }
        var player = OpenTC4CommonProxy.INSTANCE.getLocalPlayer();
        switch (context){
            case FIRST_PERSON_RIGHT_HAND,FIRST_PERSON_LEFT_HAND -> {
                if (player != null) {
                    if (player.isUsingItem()) {
                        var mainStack = player.getMainHandItem();
                        var offStack = player.getOffhandItem();
                        var usingItem = player.getUseItem();
                        if ((usingItem == offStack && stack == mainStack)
                                || (usingItem == mainStack && stack == offStack)
                        ) {
                            return false;
                        }
                    }
                }
            }
            default -> {}
        }


        final float totalMultiplier = 1f;
        final float glassScaleMultiplier = 1f;
        final float glassYOffset = -0.07f;
        final float frameScaleMultiplier = 0.45f/1.2f;

        final float closingOffset = .12f;
        final float verticalOffset = -.72f;
        final float horizonalOffset = -.22f;

        final float leftHandClosingOffset = -closingOffset;
        final float leftHandHorizonalOffset = -verticalOffset;
        final float leftHandVerticalOffset = -horizonalOffset;

        final float leftHandFixClosingOffset = .3f;
        final float leftHandFixHorizonalOffset = 0.00f;
        final float leftHandFixVerticalOffset = -.37f;


        poseStack.pushPose();

        // 基础平移/缩放
        switch (context) {
            case FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND,
                 GUI -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.mulPose(Axis.YP.rotationDegrees(30));
            }
            default -> {}
        }
        if (player.isUsingItem() && stack == player.getUseItem()){
            switch (context){
                case FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND -> {
                    poseStack.translate(horizonalOffset, closingOffset, verticalOffset);
                }
                case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> {
                    poseStack.translate(
                            leftHandHorizonalOffset + leftHandFixHorizonalOffset,
                            leftHandClosingOffset + leftHandFixClosingOffset,
                            leftHandVerticalOffset + leftHandFixVerticalOffset);
                }
                default -> {}
            }
        }

        poseStack.scale(totalMultiplier, totalMultiplier, totalMultiplier);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(SCAN_SCREEN));

        poseStack.scale(glassScaleMultiplier, glassScaleMultiplier, glassScaleMultiplier);
        poseStack.translate(0, glassYOffset, 0);
        renderScanScreen(poseStack, consumer,light,overlay);
        poseStack.translate(0, -glassYOffset, 0);

        poseStack.scale(frameScaleMultiplier/glassScaleMultiplier, frameScaleMultiplier/glassScaleMultiplier, frameScaleMultiplier/glassScaleMultiplier);
        consumer = bufferSource.getBuffer(RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
        ((ItemRendererAccessor) Minecraft.getInstance().getItemRenderer()).opentc4$renderModelLists(
                bakedModel, stack, light, overlay, poseStack, consumer
        );//dont forget to render obj part :)
        poseStack.popPose();
        return true;
    }
    private void renderScanScreen(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        poseStack.pushPose();

        // 移到 scanner 屏幕中心
        poseStack.translate(0.0, 0.11, 0.0);

        // 固定旋转：X 轴旋转 90 度，Z 轴旋转 90 度（你可以调整）
        poseStack.mulPose(axisAngle(new Vector3f(1, 0, 0), 90f));
        poseStack.mulPose(axisAngle(new Vector3f(0, 0, 1), 90f));

        // 保持纵横比
        float width = 1.f;
        float height = 1.f;

        // 半透明 alpha
        float alpha = (float)(190 + Math.sin(Minecraft.getInstance().player.tickCount * 0.1) * 10 + 10) / 255f;

        ResourceLocation scanscreen = new ResourceLocation("thaumcraft", "textures/item/scanscreen.png");
        RenderSystem.setShaderTexture(0, scanscreen);


        // 绘制 quad
        consumer.vertex(poseStack.last().pose(), -width/2, -height/2, 0f).color(1f,1f,1f, alpha).uv(0f,0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0) // 或传入 render() 的 light
                .normal(0f, 0f, 1f) // 面向正
                .endVertex();
        consumer.vertex(poseStack.last().pose(), width/2, -height/2, 0f).color(1f,1f,1f, alpha).uv(1f,0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0) // 或传入 render() 的 light
                .normal(0f, 0f, 1f) // 面向正
                .endVertex();
        consumer.vertex(poseStack.last().pose(), width/2, height/2, 0f).color(1f,1f,1f, alpha)
                .uv(1f,1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0) // 或传入 render() 的 light
                .normal(0f, 0f, 1f) // 面向正
                .endVertex();
        consumer.vertex(poseStack.last().pose(), -width/2, height/2, 0f).color(1f,1f,1f, alpha).uv(0f,1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0) // 或传入 render() 的 light
                .normal(0f, 0f, 1f) // 面向正
                .endVertex();

        poseStack.popPose();
    }

    /**
     * 辅助方法：创建旋转 Quaternionf
     */
    private static Quaternionf axisAngle(Vector3f axis, float degrees) {
        float rad = (float) Math.toRadians(degrees / 2);
        float sin = (float) Math.sin(rad);
        float cos = (float) Math.cos(rad);
        return new Quaternionf(axis.x() * sin, axis.y() * sin, axis.z() * sin, cos);
    }



}
