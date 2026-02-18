package thaumcraft.api.aspects;

import java.util.Objects;

public record CompoundAspectComponent(Aspect aspectA, Aspect aspectB) {

    public boolean isCombinedFrom(Aspect a, Aspect b) {
        return (Objects.equals(aspectA, a) && Objects.equals(aspectB, b))
                || (Objects.equals(aspectA, b) && Objects.equals(aspectB, a));
    }

    @Override
    public String toString() {
        return "CompoundAspectComponent{" +
                "aspectA=" + aspectA +
                ", aspectB=" + aspectB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CompoundAspectComponent that)) return false;
        return Objects.equals(aspectA, that.aspectA) && Objects.equals(aspectB, that.aspectB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspectA, aspectB);
    }
}
