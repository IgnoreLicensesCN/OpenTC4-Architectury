package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public record CompoundAspectComponent(Aspect aspectA, Aspect aspectB,int hash)
        implements Iterable<Aspect> {
    public static CompoundAspectComponent of(Aspect aspectA, Aspect aspectB){
        var aspA = aspectA;
        var aspB = aspectB;
        if (aspectA.hashCode() <= aspectB.hashCode()) {
            aspA = aspectB;
            aspB = aspectA;
        }
        var hash = (Objects.hash(aspA) * (1<<15)) + Objects.hash(aspB);
        return new CompoundAspectComponent(aspA, aspB, hash);
    }

    public boolean isCombinedFrom(Aspect a, Aspect b) {
        return (Objects.equals(aspectA, a) && Objects.equals(aspectB, b))
                || (Objects.equals(aspectA, b) && Objects.equals(aspectB, a));
    }

    @Override
    public @NotNull String toString() {
        return "CompoundAspectComponent{" +
                "aspectA=" + aspectA +
                ", aspectB=" + aspectB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CompoundAspectComponent(Aspect a, Aspect b, int hash1))) return false;
        return(Objects.equals(aspectA, a) && Objects.equals(aspectB, b))
                || (Objects.equals(aspectA, b) && Objects.equals(aspectB, a));
    }

    @Override
    public int hashCode() {
        return hash;
    }
    @Override
    public @NotNull Iterator<Aspect> iterator() {
        return List.of(aspectA, aspectB).iterator();
    }

    public boolean contains(Aspect aspect) {
        return aspectA == aspect || aspectB == aspect;
    }
}
