package thaumcraft.common.lib.world.dim;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CellFeature {
    public static final CellFeature NONE = new CellFeature(false);//0
    public static final CellFeature PORTAL = new CellFeature(true);
    public static final CellFeature BOSS_ROOM = new CellFeature(true);
    public static final CellFeature BOSS_ROOM_1 = new CellFeature(true);
    public static final CellFeature BOSS_ROOM_2 = new CellFeature(true);
    public static final CellFeature BOSS_ROOM_3 = new CellFeature(true);//5
    public static final CellFeature KEY_ROOM = new CellFeature(true);
    public static final CellFeature NEST_ROOM = new CellFeature(false);
    public static final CellFeature LIBRARY_ROOM = new CellFeature(false);//8
    public static final CellFeature PASSAGE = new CellFeature(false);
    public static final CellFeature PASSAGE_1 = new CellFeature(false);//10
    public static final CellFeature PASSAGE_2 = new CellFeature(false);
    public static final CellFeature PASSAGE_3 = new CellFeature(false);//12
    public static final CellFeature PASSAGE_4 = new CellFeature(false);
    public static final CellFeature PASSAGE_5 = new CellFeature(false);
    public static final CellFeature INVALID = new CellFeature(false);//99

    public final boolean preventInitCorePathConnection;
    public static final CellFeature[] FEATURE_SET_A = {
            NEST_ROOM,
            LIBRARY_ROOM,
            PASSAGE
    };
    public static final CellFeature[] FEATURE_SET_B = {
            LIBRARY_ROOM,
            PASSAGE_1,
            PASSAGE_2,
            PASSAGE_2,
            PASSAGE_3,
            PASSAGE_3,
            PASSAGE_4,
            PASSAGE_5,
    };
    public static final CellFeature[] FEATURE_SET_C = {
            NONE,
            PORTAL,
            BOSS_ROOM,
    };
}
