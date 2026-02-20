package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public record CompoundAspectComponent(Aspect aspectA, Aspect aspectB)
        implements Iterable<Aspect> {

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
        return(Objects.equals(aspectA, that.aspectA) && Objects.equals(aspectB, that.aspectB))
                || (Objects.equals(aspectA, that.aspectB) && Objects.equals(aspectB, that.aspectA));
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspectA, aspectB);
    }
    @Override
    public Iterator<Aspect> iterator() {
        return List.of(aspectA, aspectB).iterator();
    }
}
