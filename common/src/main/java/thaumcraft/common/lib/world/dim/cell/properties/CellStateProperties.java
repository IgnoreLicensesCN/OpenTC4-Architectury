package thaumcraft.common.lib.world.dim.cell.properties;

import net.minecraft.core.Direction;
import thaumcraft.common.lib.world.dim.CellFeature;

import java.util.*;

public class CellStateProperties {
    private static final Set<CellStateProperty<?>> ALL_CELL_STATE_PROPERTIES = new HashSet<>();
    public static final Set<CellStateProperty<?>> ALL_CELL_STATE_PROPERTIES_VIEW = Collections.unmodifiableSet(ALL_CELL_STATE_PROPERTIES);
    
    private static final List<CellFeature> CELL_FEATURES = new ArrayList<>();
    public static final List<CellFeature> CELL_FEATURES_VIEW = Collections.unmodifiableList(CELL_FEATURES);

    public static final BooleanCellStateProperty CONNECT_NORTH = new BooleanCellStateProperty("connect_north");
    public static final BooleanCellStateProperty CONNECT_SOUTH = new BooleanCellStateProperty("connect_south");
    public static final BooleanCellStateProperty CONNECT_EAST = new BooleanCellStateProperty("connect_east");
    public static final BooleanCellStateProperty CONNECT_WEST = new BooleanCellStateProperty("connect_west");
    public static final ListCellStateProperty<CellFeature> CELL_FEATURE = new ListCellStateProperty<>(CellFeature.class,"cell_feature",CELL_FEATURES_VIEW);


    public static final Map<Direction, BooleanCellStateProperty> CONNECTED_DIRECTION_PROPERTIES = new HashMap<>();
    public static void registerCellStateProperty(CellStateProperty<?> property) {
        ALL_CELL_STATE_PROPERTIES.add(property);
    }
    public static void registerCellFeature(CellFeature feature) {
        CELL_FEATURES.add(feature);
    }
    public static void init(){
        registerCellStateProperty(CONNECT_NORTH);
        registerCellStateProperty(CONNECT_SOUTH);
        registerCellStateProperty(CONNECT_EAST);
        registerCellStateProperty(CONNECT_WEST);
        CONNECTED_DIRECTION_PROPERTIES.put(Direction.NORTH, CONNECT_NORTH);
        CONNECTED_DIRECTION_PROPERTIES.put(Direction.SOUTH, CONNECT_SOUTH);
        CONNECTED_DIRECTION_PROPERTIES.put(Direction.EAST, CONNECT_EAST);
        CONNECTED_DIRECTION_PROPERTIES.put(Direction.WEST, CONNECT_WEST);
        registerCellStateProperty(CELL_FEATURE);
        registerCellFeature(CellFeature.NONE);
        registerCellFeature(CellFeature.PORTAL);
        registerCellFeature(CellFeature.BOSS_ROOM);
        registerCellFeature(CellFeature.BOSS_ROOM_1);
        registerCellFeature(CellFeature.BOSS_ROOM_2);
        registerCellFeature(CellFeature.BOSS_ROOM_3);
        registerCellFeature(CellFeature.KEY_ROOM);
        registerCellFeature(CellFeature.NEST_ROOM);
        registerCellFeature(CellFeature.LIBRARY_ROOM);//8
        registerCellFeature(CellFeature.PASSAGE);
        registerCellFeature(CellFeature.PASSAGE_1);//10
        registerCellFeature(CellFeature.PASSAGE_2);
        registerCellFeature(CellFeature.PASSAGE_3);//12
        registerCellFeature(CellFeature.PASSAGE_4);
        registerCellFeature(CellFeature.PASSAGE_5);
        registerCellFeature(CellFeature.INVALID);//99
    }

    public static void onDatapackReloaded() {
    }
}
