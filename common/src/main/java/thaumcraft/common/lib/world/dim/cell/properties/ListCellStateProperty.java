package thaumcraft.common.lib.world.dim.cell.properties;

import java.util.List;

public class ListCellStateProperty<T> extends CellStateProperty<T> {
    public final List<T> values;
    public ListCellStateProperty(Class<T> clazz, String name, List<T> values) {
        super(clazz, name);
        this.values = values;
    }

    @Override
    public List<T> getPossibleValues() {
        return values;
    }

    @Override
    public String getName(T value) {
        return value.toString();
    }

    @Override
    public boolean isDefaultValue(Object value) {
        return !values.isEmpty() && value == values.getFirst();
    }
}
