package com.linearity.opentc4.chatcomponent;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public abstract class ComponentElementRenderer implements Comparable<ComponentElementRenderer>{
    public final int weight;

    public ComponentElementRenderer(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull ComponentElementRenderer o) {
        return Integer.compare(weight, o.weight);
    }
    //null if cant render for that.
    public abstract @Nullable ComponentElementSplitParts getPartsIfExistsAndCanRender(String toSplitIntoParts);
    //return width **added**
    //don't cause performance issue here
    public abstract int renderPartForElement(
            String elementContent,
            float xCoordCurrent,
            float yCoord,
            @RGBColor int textColor,
            boolean dropShadow,
            Matrix4f transformationMatrix,
            MultiBufferSource multiBufferSource,
            Font.DisplayMode displayMode,
            int backgroundColor,
            int lightMapCoord,
            boolean enableBidirectional,
            int lineHeight
    );
}
