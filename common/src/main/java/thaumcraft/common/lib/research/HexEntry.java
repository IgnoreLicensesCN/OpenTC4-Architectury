package thaumcraft.common.lib.research;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;

import java.util.Objects;

public record HexEntry(Aspect aspect, HexType type) {
    public static final HexEntry EMPTY = new HexEntry(Aspects.EMPTY, HexType.NONE);

    @Override
    public String toString() {
        return "HexEntry{" +
                "aspect=" + aspect +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HexEntry hexEntry)) return false;
        return type == hexEntry.type && Objects.equals(aspect, hexEntry.aspect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspect, type);
    }
}
