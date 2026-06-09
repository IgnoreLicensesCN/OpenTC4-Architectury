package com.linearity.opentc4.mixin.client;

import com.linearity.opentc4.chatcomponent.ComponentElementRenderer;
import com.linearity.opentc4.chatcomponent.ComponentElementSplitParts;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static com.linearity.opentc4.chatcomponent.ComponentRendering.COMPONENT_ELEMENT_RENDERERS;

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
        ComponentElementRenderer renderer = null;
        ComponentElementSplitParts splitParts = null;
        for (var rendererMayUse:COMPONENT_ELEMENT_RENDERERS.getListeners()){
            var parts = rendererMayUse.getPartsIfExistsAndCanRender(string);
            if (parts != null){
                renderer = rendererMayUse;
                splitParts = parts;
                break;
            }
        }
        if (renderer == null){return;}

        float currentX = f;

        if (!splitParts.beforeElement().isEmpty()){
            currentX = this.drawInternal(splitParts.beforeElement(), currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k, bl2);
        }
        currentX += renderer.renderPartForElement(
                splitParts.elementContent(),
                currentX,
                g,
                i,
                bl,
                matrix4f,
                multiBufferSource,
                displayMode,
                j,
                k,
                bl2,
                this.lineHeight
        );
        if (!splitParts.afterElement().isEmpty()){
            currentX = this.drawInternal(splitParts.afterElement(), currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k, false);
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
            Matrix4f matrix4f,
            MultiBufferSource multiBufferSource, Font.DisplayMode displayMode,
            int j, int k,
            CallbackInfoReturnable<Integer> cir){

        StringBuilder textDetector = new StringBuilder();
        formattedCharSequence.accept((idx, style, cp) -> {
            textDetector.appendCodePoint(cp);
            return true;
        });
        String fullText = textDetector.toString();

        ComponentElementRenderer renderer = null;
        ComponentElementSplitParts splitParts = null;
        for (var rendererMayUse:COMPONENT_ELEMENT_RENDERERS.getListeners()){
            var parts = rendererMayUse.getPartsIfExistsAndCanRender(fullText);
            if (parts != null){
                renderer = rendererMayUse;
                splitParts = parts;
                break;
            }
        }
        if (renderer == null){return;}
        String textBefore = splitParts.beforeElement();
        String matchedContent = splitParts.elementContent();
        String textAfter = splitParts.afterElement();

        int startTagIndex = textBefore.length();
        int endTagIndex = startTagIndex + matchedContent.length() - 1;

        List<Object[]> beforeList = new ArrayList<>(textBefore.length());
        List<Object[]> afterList = new ArrayList<>(textAfter.length());

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
        currentX += renderer.renderPartForElement(
                matchedContent,
                currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k,false,this.lineHeight
        );
        if (!textAfter.isEmpty()){
            currentX = this.drawInternal(afterSeq, currentX, g, i, bl, matrix4f, multiBufferSource, displayMode, j, k);
        }
        cir.setReturnValue((int) currentX + (bl ? 1 : 0));
    }
}
