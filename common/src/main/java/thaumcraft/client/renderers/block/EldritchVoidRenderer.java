package thaumcraft.client.renderers.block;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.EldritchVoidBlockEntity;

import static net.minecraft.client.renderer.RenderStateShard.RENDERTYPE_END_GATEWAY_SHADER;

public class EldritchVoidRenderer implements BlockEntityRenderer<EldritchVoidBlockEntity> {
    public EldritchVoidRenderer(BlockEntityRendererProvider.Context context) {
//        super(context);
    }
    public void render(EldritchVoidBlockEntity theEndPortalBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        Matrix4f matrix4f = poseStack.last().pose();
        this.renderCube(theEndPortalBlockEntity, matrix4f, multiBufferSource.getBuffer(this.renderType()));
    }

    private void renderCube(EldritchVoidBlockEntity theEndPortalBlockEntity, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        float f = this.getOffsetDown();
        float g = this.getOffsetUp();
        this.renderFace(theEndPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(theEndPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(theEndPortalBlockEntity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(theEndPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(theEndPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(theEndPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, g, g, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(
            EldritchVoidBlockEntity theEndPortalBlockEntity,
            Matrix4f matrix4f,
            VertexConsumer vertexConsumer,
            float f,
            float g,
            float h,
            float i,
            float j,
            float k,
            float l,
            float m,
            Direction direction
    ) {
        vertexConsumer.vertex(matrix4f, f, h, j).endVertex();
        vertexConsumer.vertex(matrix4f, g, h, k).endVertex();
        vertexConsumer.vertex(matrix4f, g, i, l).endVertex();
        vertexConsumer.vertex(matrix4f, f, i, m).endVertex();
    }

    protected float getOffsetUp() {
        return 0.75F;
    }

    protected float getOffsetDown() {
        return 0.375F;
    }

    public static final ResourceLocation END_SKY_LOCATION = new ResourceLocation(Thaumcraft.MOD_ID,"textures/misc/tunnel.png");
    public static final ResourceLocation END_PORTAL_LOCATION = new ResourceLocation(Thaumcraft.MOD_ID,"textures/misc/particlefield.png");
    public static final ResourceLocation END_PORTAL_LOCATION_2 = new ResourceLocation(Thaumcraft.MOD_ID,"textures/misc/particlefield32.png");
    private static final RenderType END_GATEWAY = RenderType.create(
            "eldritch_void",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_END_GATEWAY_SHADER)
                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(END_SKY_LOCATION, false, false)
                                    .add(END_PORTAL_LOCATION, false, false)
                                    .add(END_PORTAL_LOCATION_2, false, false)
                                    .build()
                    )
                    .createCompositeState(false)
    );
    protected RenderType renderType() {
        return END_GATEWAY;
    }
}
