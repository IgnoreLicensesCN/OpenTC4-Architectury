package thaumcraft.common.lib.world.dim.cell;

import com.linearity.annotations.UtilityLikeAbstraction;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.world.dim.CellFeature;
import thaumcraft.common.lib.world.dim.cell.properties.CellStateProperty;

import java.util.HashMap;
import java.util.Map;

import static thaumcraft.common.lib.world.dim.cell.properties.CellStateProperties.CELL_FEATURE;
import static thaumcraft.common.lib.world.dim.cell.properties.CellStateProperties.CONNECTED_DIRECTION_PROPERTIES;

public class CellState {
    private final Map<CellStateProperty<?>, Object> properties = new HashMap<>();
    public boolean isEmpty() {
        if (properties.isEmpty()){
            return true;
        }
        for (var entry: properties.entrySet()) {
            if (!entry.getKey().isDefaultValue(entry.getValue())){
                return false;
            }
        }
        return true;
    }
    public CellState(){}
    public CellState(Direction... dirs){
        for (var dir: dirs){
            var prop = CONNECTED_DIRECTION_PROPERTIES.get(dir);
            if (prop != null){
                properties.put(prop,Boolean.TRUE);
            }
        }
    }
    public CellState(CellFeature feature,Direction... dirs){
        this(dirs);
        properties.put(CELL_FEATURE,feature);
    }
    public <T> @Nullable T getPropertyValue(CellStateProperty<T> prop){
        return (T)properties.get(prop);
    }
    public <T> @Nullable T setPropertyValue(CellStateProperty<T> prop, T value){
        return (T) properties.put(prop,value);
    }
    public <T> @Nullable T removePropertyValue(CellStateProperty<T> prop){
        return (T) properties.remove(prop);
    }

    @UtilityLikeAbstraction
    public static void addCellConnectedDirection(CellState state,Direction dir){
        state. setPropertyValue(CONNECTED_DIRECTION_PROPERTIES.get(dir),Boolean.TRUE);
    }

    @UtilityLikeAbstraction
    public static boolean cellConnectedTo(CellState state,Direction dir){
        return state.getPropertyValue(CONNECTED_DIRECTION_PROPERTIES.get(dir))==Boolean.TRUE;
    }

    @UtilityLikeAbstraction
    public static CellFeature getCellFeature(CellState state){
        return state.getPropertyValue(CELL_FEATURE);
    }

    public static @Nullable CellFeature setCellFeature(CellState state,CellFeature feature){
        return state.setPropertyValue(CELL_FEATURE,feature);
    }

    public static boolean cellHasConnectedDirection(CellState state){
        for (var dir:Direction.Plane.HORIZONTAL){
            if (cellConnectedTo(state,dir)){
                return true;
            }
        }
        return false;
    }

//    public final EnumSet<Direction> availableDirs = EnumSet.noneOf(Direction.class);
//    public CellFeature feature = CellFeature.NONE;
//
//    public boolean isEmpty(){
//        return availableDirs.isEmpty() && feature == CellFeature.NONE;
//    }
//    public CellState() {
//    }
//
//    public CellState(Direction... dirs){
//        availableDirs.addAll(Arrays.asList(dirs));
//    }
//    public CellState(CellFeature feature, Direction... dirs) {
//        this.feature = feature;
//        availableDirs.addAll(Arrays.asList(dirs));
//    }
//
//    @Override
//    public final boolean equals(Object o) {
//        if (!(o instanceof CellState cellState)) return false;
//        return feature == cellState.feature && availableDirs.equals(cellState.availableDirs);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = availableDirs.hashCode();
//        result = 31 * result + feature.hashCode();
//        return result;
//    }

}
