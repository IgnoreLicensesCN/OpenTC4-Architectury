package thaumcraft.common.lib.world.dim.cell.properties;

import java.util.List;

public class BooleanCellStateProperty extends CellStateProperty<Boolean> {
    public static final List<Boolean> POSSIBLE_VALUES = List.of(Boolean.FALSE,Boolean.TRUE);
    public BooleanCellStateProperty(String name) {
        super(Boolean.class, name);
    }

    @Override
    public boolean isDefaultValue(Object value) {
        return value == Boolean.FALSE;
    }

    @Override
    public List<Boolean> getPossibleValues() {
        return POSSIBLE_VALUES;
    }

    @Override
    public String getName(Boolean b) {
        return b.toString();
    }
}
