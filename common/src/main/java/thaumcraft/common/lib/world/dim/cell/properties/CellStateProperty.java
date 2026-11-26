package thaumcraft.common.lib.world.dim.cell.properties;

import com.linearity.annotations.RequiresParameterizedTypeEqualForEquals;
import com.linearity.annotations.StoleFrom;
import lombok.AllArgsConstructor;

import java.util.List;

@StoleFrom("net.minecraft.world.level.block.state.properties.Property")
@AllArgsConstructor
@RequiresParameterizedTypeEqualForEquals
public abstract class CellStateProperty<T> {//Singleton
    public final Class<T> clazz;
    public final String name;

    public abstract List<T> getPossibleValues();
    public CellStateProperty.Value<T> value(T value) {
        return new CellStateProperty.Value<>(this, value);
    }
    public abstract String getName(T value);

    public abstract boolean isDefaultValue(Object value);

    public record Value<T/* extends Comparable<T>*/>(CellStateProperty<T> property, T value) {
        public Value(CellStateProperty<T> property, T value) {
            if (!property.getPossibleValues().contains(value)) {
                throw new IllegalArgumentException("Value " + value + " does not belong to property " + property);
            } else {
                this.property = property;
                this.value = value;
            }
        }

        public String toString() {
            return this.property.name + "=" + this.property.getName(this.value);
        }
    }
}
