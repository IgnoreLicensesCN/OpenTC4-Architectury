package com.linearity.opentc4.mixin.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.chatcomponent.AspectChatComponent.findFirstWrappedAspectResourceLocation;
import static com.linearity.opentc4.chatcomponent.AspectChatComponent.unwrapAspectResourceLocation;
import static thaumcraft.api.aspects.Aspects.ALL_ASPECTS;

//powered by gemini i may have to fix this part(supports itemStack rendering?)
//TODO:Test it
@Mixin(Font.class)
public abstract class FontRendererMixin {
    @Shadow @Final public int lineHeight;
    @Shadow
    private int drawInternal(
            String string,
            float f,
            float g,
            int i,
            boolean bl,
            Matrix4f matrix4f,
            MultiBufferSource multiBufferSource,
            Font.DisplayMode displayMode,
            int j,
            int k,
            boolean bl2){
        throw new RuntimeException("Should not reach here");
    }
    @Shadow
    private int drawInternal(
            FormattedCharSequence formattedCharSequence,
            float f,
            float g,
            int i,
            boolean bl,
            Matrix4f matrix4f,
            MultiBufferSource multiBufferSource,
            Font.DisplayMode displayMode,
            int j,
            int k
    ){
        throw new RuntimeException("Should not reach here");
    }

    @Inject(
            method = "drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I",
            at = @At("HEAD"),
            cancellable = true
    )
    public void opentc4$drawInternalString(
            String string,
            float f,
            float g,
            int i,
            boolean bl,
            Matrix4f matrix4f,
            MultiBufferSource multiBufferSource,
            Font.DisplayMode displayMode,
            int j,
            int k,
            boolean bl2,
            CallbackInfoReturnable<Integer> cir){
        if (string.isEmpty()){return;}
        var splitted = findFirstWrappedAspectResourceLocation(string);
        if (splitted == null){
            return;
        }
        var resLoc = unwrapAspectResourceLocation(splitted[1]);
        if (!ALL_ASPECTS.containsKey(AspectResourceLocation.of(resLoc))) {
            return;
        }

        float currentX = f;

        if (!splitted[0].isEmpty()){
            currentX = this.drawInternal(splitted[0], currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k, bl2);
        }
        if (!bl) {
            opentc4$renderAspect(resLoc,currentX, g, matrix4f, multiBufferSource);
        }
        currentX += this.lineHeight;
        if (!splitted[2].isEmpty()){
            currentX = this.drawInternal(splitted[2], currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k, false);
        }
        cir.setReturnValue((int) currentX + (bl ? 1 : 0));
    }
    @Inject(
            method = "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
            at = @At("HEAD"),
            cancellable = true
    )
    public void opentc4$drawInternalCharSequence(
            FormattedCharSequence formattedCharSequence,
            float f, float g, int i, boolean bl,
            Matrix4f matrix4f, MultiBufferSource multiBufferSource, Font.DisplayMode displayMode, int j, int k,
            CallbackInfoReturnable<Integer> cir){

        StringBuilder textDetector = new StringBuilder();
        formattedCharSequence.accept((idx, style, cp) -> {
            textDetector.appendCodePoint(cp);
            return true;
        });
        String fullText = textDetector.toString();

        String[] splitResult = findFirstWrappedAspectResourceLocation(fullText);
        if (splitResult == null) {
            return;
        }
        String textBefore = splitResult[0];
        String matchedTag = splitResult[1];
        String textAfter = splitResult[2];

        var aspectResourceLocation = unwrapAspectResourceLocation(matchedTag);
        if (!ALL_ASPECTS.containsKey(aspectResourceLocation)) {
            return;
        }

        int startTagIndex = textBefore.length();
        int endTagIndex = startTagIndex + matchedTag.length() - 1;

        java.util.List<Object[]> beforeList = new java.util.ArrayList<>();
        java.util.List<Object[]> afterList = new java.util.ArrayList<>();

        int[] charCounter = {0}; // 计数器

        formattedCharSequence.accept((idx, style, cp) -> {
            int currentPos = charCounter[0];
            if (currentPos < startTagIndex) {
                beforeList.add(new Object[]{style, cp});
            } else if (currentPos > endTagIndex) {
                afterList.add(new Object[]{style, cp});
            }
            charCounter[0]++;
            return true;
        });

        // 4. 用无敌的 Lambda 重新捏造前半段和后半段的 Sequence
        FormattedCharSequence beforeSeq = (sink) -> {
            for (int m = 0; m < beforeList.size(); m++) {
                Object[] data = beforeList.get(m);
                if (!sink.accept(m, (Style)data[0], (Integer)data[1])) return false;
            }
            return true;
        };

        FormattedCharSequence afterSeq = (sink) -> {
            for (int m = 0; m < afterList.size(); m++) {
                Object[] data = afterList.get(m);
                if (!sink.accept(m, (Style)data[0], (Integer)data[1])) return false;
            }
            return true;
        };

        float currentX = f;
        if (!textBefore.isEmpty()){
            currentX = this.drawInternal(beforeSeq, currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k);
        }
        if (!bl) {
            opentc4$renderAspect(aspectResourceLocation,currentX,g,matrix4f,multiBufferSource);
        }
        currentX += lineHeight;
        if (!textAfter.isEmpty()){
            currentX = this.drawInternal(afterSeq, currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k);
        }
        cir.setReturnValue((int) currentX + (bl ? 1 : 0));
    }

    @Unique
    private final Map<AspectResourceLocation,ResourceLocation> opentc4$aspectResLocToTexture = new ConcurrentHashMap<>();
    @Unique
    private static final ResourceLocation UNDISCOVERED_TEXTURE = new ResourceLocation(Thaumcraft.MOD_ID,"textures/gui/aspects/_unknown.png");
    @Unique
    private void opentc4$renderAspect(AspectResourceLocation aspectResourceLocation, float currentX, float g, Matrix4f matrix4f, MultiBufferSource multiBufferSource) {
        ResourceLocation myTexture = opentc4$aspectResLocToTexture.computeIfAbsent(
                aspectResourceLocation,
                aspResLoc -> new ResourceLocation(aspResLoc.getNamespace(), "textures/gui/aspects_colored/" + aspResLoc.getPath() + ".png")
        );
        var aspect = ALL_ASPECTS.get(aspectResourceLocation);
        if (aspect == null) {
            return;
        }
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

        consumer.vertex(matrix4f, currentX, g, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F)
                .uv(u0, v0)
                .uv2(0xf000f0);

        consumer.vertex(matrix4f, currentX, g + lineHeight, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F)
                .uv(u0, v1)
                .uv2(0xf000f0);

        consumer.vertex(matrix4f, currentX + lineHeight, g + lineHeight, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F)
                .uv(u1, v1)
                .uv2(0xf000f0);

        consumer.vertex(matrix4f, currentX + lineHeight, g, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F)
                .uv(u1, v0)
                .uv2(15728880);
    }
}
