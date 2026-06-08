package com.linearity.opentc4.chatcomponent;

import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.Optional;
import java.util.regex.Pattern;


public record AspectChatComponent(AspectResourceLocation aspectResourceLocation) implements ComponentContents {
    @Override
    public <T> @NotNull Optional<T> visit(FormattedText.ContentConsumer<T> contentConsumer) {
        return contentConsumer.accept(wrapAspectResourceLocation(aspectResourceLocation));
    }

    @Override
    public <T> @NotNull Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style) {
        return styledContentConsumer.accept(style, aspectResourceLocation.toString());
    }

    public @NotNull String toString() {
        return "aspect{" + this.aspectResourceLocation + "}";
    }

    public static final String ASPECT_RESOURCE_LOCATION_HEAD = "[AspectResourceLocation]{";
    public static String wrapAspectResourceLocation(@NotNull AspectResourceLocation aspectResourceLocation) {
        return "[AspectResourceLocation]{" + aspectResourceLocation + "}";
    }
    public static AspectResourceLocation unwrapAspectResourceLocation(String wrapped) {
        int start = wrapped.indexOf('{') + 1;
        int end = wrapped.lastIndexOf('}');
        return AspectResourceLocation.of(wrapped.substring(start, end));
    }
    public static final Pattern ASPECT_RES_LOC_PATTERN = Pattern.compile("(\\[AspectResourceLocation]\\{[a-z0-9_.\\-]+:[a-z0-9_.\\-/]+})");

    //null if cant split into 3
    public static @Nullable String[] findFirstWrappedAspectResourceLocation(String input) {
        var matcher = ASPECT_RES_LOC_PATTERN.matcher(input);

        if (matcher.find()) {
            String part1 = input.substring(0, matcher.start()); // Everything before
            String part2 = matcher.group(1);                   // The matched pattern
            String part3 = input.substring(matcher.end());     // Everything after
            return new String[]{part1, part2, part3};
        }
        return null;
    }
}
