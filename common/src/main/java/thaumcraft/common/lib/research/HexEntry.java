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
        if (!(o instanceof HexEntry(Aspect aspect1, HexType type1))) return false;

        return type == type1 && Objects.equals(aspect, aspect1);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(aspect);
        result = 31 * result + Objects.hashCode(type);
        return result;
    }
}
