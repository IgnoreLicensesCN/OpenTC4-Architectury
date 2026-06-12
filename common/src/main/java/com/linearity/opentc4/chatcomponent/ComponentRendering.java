package com.linearity.opentc4.chatcomponent;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static thaumcraft.api.aspects.Aspects.ALL_ASPECTS;

public class ComponentRendering {
    public static final ListenerManager<ComponentElementRenderer> COMPONENT_ELEMENT_RENDERERS = new ListenerManager<>();

    public static void init(){
        COMPONENT_ELEMENT_RENDERERS.registerListener(new AspectComponentElementRenderer(0));
    }

    public static class AspectComponentElementRenderer extends ComponentElementRenderer {

        public static final Pattern ASPECT_RES_LOC_PATTERN = Pattern.compile("(\\[AspectResourceLocation]\\{[a-z0-9_.\\-]+:[a-z0-9_.\\-/]+})");

        public AspectComponentElementRenderer(int weight) {
            super(weight);
        }

        public static String wrapAspectResourceLocation(@NotNull AspectResourceLocation aspectResourceLocation) {
            return "[AspectResourceLocation]{" + aspectResourceLocation + "}";
        }

        public static AspectResourceLocation unwrapAspectResourceLocation(String wrapped) {
            int start = wrapped.indexOf('{') + 1;
            int end = wrapped.lastIndexOf('}');
            return AspectResourceLocation.of(wrapped.substring(start, end));
        }

        @Override
        public @Nullable ComponentElementSplitParts getPartsIfExistsAndCanRender(String toSplitIntoParts) {
            var matcher = ASPECT_RES_LOC_PATTERN.matcher(toSplitIntoParts);
            if (matcher.find()) {
                String part1 = toSplitIntoParts.substring(0, matcher.start()); // Everything before
                String part2 = matcher.group(1);                   // The matched pattern
                String part3 = toSplitIntoParts.substring(matcher.end());     // Everything after
                Aspect aspect = ALL_ASPECTS.get(unwrapAspectResourceLocation(part2));
                if (aspect == null) {
                    return null;
                }
                return new ComponentElementSplitParts(part1, part2, part3);
            }
            return null;
        }
        
        private final Map<AspectResourceLocation,ResourceLocation> aspectResLocToTexture = new ConcurrentHashMap<>();
        private static final ResourceLocation UNDISCOVERED_TEXTURE = new ResourceLocation(Thaumcraft.MOD_ID,"textures/gui/aspects/_unknown.png");
        @Override
        public int renderPartForElement(
                String elementContent,
                float xCoordCurrent,
                float yCoord,
                int textColor,
                boolean dropShadow,
                Matrix4f transformationMatrix,
                MultiBufferSource multiBufferSource,
                Font.DisplayMode displayMode,
                int backgroundColor,
                int lightMapCoord,
                boolean enableBidirectional,
                int lineHeight
        ) {
            var aspectResourceLocation = unwrapAspectResourceLocation(elementContent);
            ResourceLocation myTexture = aspectResLocToTexture.computeIfAbsent(
                    aspectResourceLocation,
                    aspResLoc -> 
                            new ResourceLocation(aspResLoc.getNamespace(), "textures/gui/aspects_colored/" + aspResLoc.getPath() + ".png")
            );
            var aspect = ALL_ASPECTS.get(aspectResourceLocation);
            assert aspect != null;
            var player = Minecraft.getInstance().player;
            if (player != null){
                var info = ResearchAndScannedInfo.getFromPlayer(player);
                if (!info.hasResearchAspect(aspect)){
                    myTexture = UNDISCOVERED_TEXTURE;
                }
            }

            VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.text(myTexture));

            float u0 = 0.0F, u1 = 1.0F;
            float v0 = 0.0F, v1 = 1.0F;

            consumer.vertex(transformationMatrix, xCoordCurrent, yCoord, 0.0F)
                    .color(1.0F, 1.0F, 1.0F, 1.0F)
                    .uv(u0, v0)
                    .uv2(0xf000f0);

            consumer.vertex(transformationMatrix, xCoordCurrent, yCoord + lineHeight, 0.0F)
                    .color(1.0F, 1.0F, 1.0F, 1.0F)
                    .uv(u0, v1)
                    .uv2(0xf000f0);

            consumer.vertex(transformationMatrix, xCoordCurrent + lineHeight, yCoord + lineHeight, 0.0F)
                    .color(1.0F, 1.0F, 1.0F, 1.0F)
                    .uv(u1, v1)
                    .uv2(0xf000f0);

            consumer.vertex(transformationMatrix, xCoordCurrent + lineHeight, yCoord, 0.0F)
                    .color(1.0F, 1.0F, 1.0F, 1.0F)
                    .uv(u1, v0)
                    .uv2(15728880);
            return 9;
        }
    }
}
