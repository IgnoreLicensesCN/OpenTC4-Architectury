package thaumcraft.api.aspects;

import java.util.Objects;

public record AspectComponent(Aspect aspectA, Aspect aspectB) {

    public boolean isCombinedFrom(Aspect a, Aspect b) {
        return (Objects.equals(aspectA, a) && Objects.equals(aspectB, b))
                || (Objects.equals(aspectA, b) && Objects.equals(aspectB, a));
    }
}
