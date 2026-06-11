package com.linearity.opentc4.chatcomponent;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.Optional;


@UtilityLikeAbstraction(reason = "the string pattern matters")
public record AspectChatComponent(AspectResourceLocation aspectResourceLocation) implements ComponentContents {
    @Override
    public <T> @NotNull Optional<T> visit(FormattedText.ContentConsumer<T> contentConsumer) {
        return contentConsumer.accept(ComponentRendering.AspectComponentElementRenderer.wrapAspectResourceLocation(aspectResourceLocation));
    }

    @Override
    public <T> @NotNull Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style) {
        return styledContentConsumer.accept(style, ComponentRendering.AspectComponentElementRenderer.wrapAspectResourceLocation(aspectResourceLocation));
    }

    public @NotNull String toString() {
        return ComponentRendering.AspectComponentElementRenderer.wrapAspectResourceLocation(aspectResourceLocation);
    }

}
