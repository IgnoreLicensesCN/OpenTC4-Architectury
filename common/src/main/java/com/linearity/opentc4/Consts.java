package com.linearity.opentc4;

import com.linearity.opentc4.utils.CompoundTagHelper;

public class Consts {
    public static final int TAINT_SPREAD_UP_DISTANCE = 64;


    public static class AspectCompoundTagAccessors {
        private static final String ASPECT_KEY = "key";//String
        public static final CompoundTagHelper.StringTagAccessor ASPECT_KEY_ACCESSOR = new CompoundTagHelper.StringTagAccessor(ASPECT_KEY);
        private static final String ASPECT_AMOUNT = "amount";//int
        public static final CompoundTagHelper.IntTagAccessor ASPECT_AMOUNT_ACCESSOR = new CompoundTagHelper.IntTagAccessor(ASPECT_AMOUNT);
        private static final String ASPECT_ASPECTS = "Aspects";//List<CompoundTag>
        public static final CompoundTagHelper.ListTagAccessor ASPECT_ASPECTS_ACCESSOR = new CompoundTagHelper.ListTagAccessor(ASPECT_ASPECTS);
    }

    public static class ThaumcraftPlayerCompoundTagAccessors {
        private static final String WARP_PERM = "Thaumcraft.eldritch";
        public static final CompoundTagHelper.IntTagAccessor THAUMCRAFT_PLAYER_WARP_PERM_ACCESSOR =
                new CompoundTagHelper.IntTagAccessor(WARP_PERM);

        private static final String WARP_TEMP = "Thaumcraft.eldritch.temp";
        public static final CompoundTagHelper.IntTagAccessor THAUMCRAFT_PLAYER_WARP_TEMP_ACCESSOR =
                new CompoundTagHelper.IntTagAccessor(WARP_TEMP);

        private static final String WARP_STICKY = "Thaumcraft.eldritch.sticky";
        public static final CompoundTagHelper.IntTagAccessor THAUMCRAFT_PLAYER_WARP_STICKY_ACCESSOR =
                new CompoundTagHelper.IntTagAccessor(WARP_STICKY);

        private static final String WARP_COUNTER = "Thaumcraft.eldritch.counter";
        public static final CompoundTagHelper.IntTagAccessor THAUMCRAFT_PLAYER_WARP_COUNTER_ACCESSOR =
                new CompoundTagHelper.IntTagAccessor(WARP_COUNTER);

        private static final String SHIELDING = "Thaumcraft.shielding";
        public static final CompoundTagHelper.IntTagAccessor THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR =
                new CompoundTagHelper.IntTagAccessor(SHIELDING);
        private static final String RESEARCH = "THAUMCRAFT.RESEARCH";
        public static final CompoundTagHelper.ListTagAccessor THAUMCRAFT_PLAYER_RESEARCH_ACCESSOR =
                new CompoundTagHelper.ListTagAccessor(RESEARCH);
        private static final String ASPECTS = "THAUMCRAFT.ASPECTS";
        public static final CompoundTagHelper.ListTagAccessor THAUMCRAFT_PLAYER_ASPECTS_ACCESSOR =
                new CompoundTagHelper.ListTagAccessor(ASPECTS);

        private static final String SCAN_OBJECTS = "THAUMCRAFT.SCAN.OBJECTS";
        public static final CompoundTagHelper.ListTagAccessor THAUMCRAFT_PLAYER_SCAN_OBJECTS_ACCESSOR =
                new CompoundTagHelper.ListTagAccessor(SCAN_OBJECTS);

        private static final String SCAN_ENTITIES = "THAUMCRAFT.SCAN.ENTITIES";
        public static final CompoundTagHelper.ListTagAccessor THAUMCRAFT_PLAYER_SCAN_ENTITIES_ACCESSOR =
                new CompoundTagHelper.ListTagAccessor(SCAN_ENTITIES);

        private static final String SCAN_PHENOMENA = "THAUMCRAFT.SCAN.PHENOMENA";
        public static final CompoundTagHelper.ListTagAccessor THAUMCRAFT_PLAYER_SCAN_PHENOMENA_ACCESSOR =
                new CompoundTagHelper.ListTagAccessor(SCAN_PHENOMENA);
    }

    public static class WorldCoordsCompoundTagAccessors {
        private static final String WORLD_X = "w_x"; // int
        public static final CompoundTagHelper.IntTagAccessor WORLD_X_ACCESSOR = new CompoundTagHelper.IntTagAccessor(WORLD_X);

        private static final String WORLD_Y = "w_y"; // int
        public static final CompoundTagHelper.IntTagAccessor WORLD_Y_ACCESSOR = new CompoundTagHelper.IntTagAccessor(WORLD_Y);

        private static final String WORLD_Z = "w_z"; // int
        public static final CompoundTagHelper.IntTagAccessor WORLD_Z_ACCESSOR = new CompoundTagHelper.IntTagAccessor(WORLD_Z);

        private static final String WORLD_DIM = "w_d"; // String
        public static final CompoundTagHelper.StringTagAccessor WORLD_DIM_ACCESSOR = new CompoundTagHelper.StringTagAccessor(WORLD_DIM);
    }

    public static class BlockPosCompoundTagKeys {
        private static final String BLOCK_X = "b_x"; // int
        public static final CompoundTagHelper.IntTagAccessor BLOCK_X_ACCESSOR = new CompoundTagHelper.IntTagAccessor(BLOCK_X);

        private static final String BLOCK_Y = "b_y"; // int
        public static final CompoundTagHelper.IntTagAccessor BLOCK_Y_ACCESSOR = new CompoundTagHelper.IntTagAccessor(BLOCK_Y);

        private static final String BLOCK_Z = "b_z"; // int
        public static final CompoundTagHelper.IntTagAccessor BLOCK_Z_ACCESSOR = new CompoundTagHelper.IntTagAccessor(BLOCK_Z);
    }

    public static class EnchantmentConsts{

    }

    public static class ResearchNoteCompoundTagAccessors {
        private static final String RESEARCH_NOTE_KEY = "key";
        public static final CompoundTagHelper.StringTagAccessor RESEARCH_NOTE_KEY_ACCESSOR =
                new CompoundTagHelper.StringTagAccessor(RESEARCH_NOTE_KEY);

        private static final String RESEARCH_NOTE_COLOR = "color";
        public static final CompoundTagHelper.IntTagAccessor RESEARCH_NOTE_COLOR_ACCESSOR =
                new CompoundTagHelper.IntTagAccessor(RESEARCH_NOTE_COLOR);

        private static final String RESEARCH_NOTE_COMPLETE = "complete";
        public static final CompoundTagHelper.BooleanTagAccessor RESEARCH_NOTE_COMPLETE_ACCESSOR =
                new CompoundTagHelper.BooleanTagAccessor(RESEARCH_NOTE_COMPLETE);

        private static final String RESEARCH_NOTE_COPIES = "copies";
        public static final CompoundTagHelper.IntTagAccessor RESEARCH_NOTE_COPIES_ACCESSOR =
                new CompoundTagHelper.IntTagAccessor(RESEARCH_NOTE_COPIES);

        private static final String RESEARCH_NOTE_HEXGRID = "hexgrid";
        public static final CompoundTagHelper.ListTagAccessor RESEARCH_NOTE_HEXGRID_ACCESSOR =
                new CompoundTagHelper.ListTagAccessor(RESEARCH_NOTE_HEXGRID);

        // HexEntry 子项
        public static final CompoundTagHelper.ByteTagAccessor RESEARCH_NOTE_HEX_Q_ACCESSOR =
                new CompoundTagHelper.ByteTagAccessor("hexq");
        public static final CompoundTagHelper.ByteTagAccessor RESEARCH_NOTE_HEX_R_ACCESSOR =
                new CompoundTagHelper.ByteTagAccessor("hexr");
        public static final CompoundTagHelper.ByteTagAccessor RESEARCH_NOTE_HEX_TYPE_ACCESSOR =
                new CompoundTagHelper.ByteTagAccessor("type");
        public static final CompoundTagHelper.StringTagAccessor RESEARCH_NOTE_HEX_ASPECT_ACCESSOR =
                new CompoundTagHelper.StringTagAccessor("aspect");
    }

    public static class TileVisNodeCompoundTagAccessors {
        private static final String TILE_VIS_NODE_LINKS = "Link";
        public static final CompoundTagHelper.CompoundListAccessor TILE_VIS_NODE_LINKS_ACCESSOR =
                new CompoundTagHelper.CompoundListAccessor(TILE_VIS_NODE_LINKS);
        private static final String TILE_VIS_NODE_PATH_X = "x";
        private static final String TILE_VIS_NODE_PATH_Y = "y";
        private static final String TILE_VIS_NODE_PATH_Z = "z";
        public static final CompoundTagHelper.IntTagAccessor TILE_VIS_NODE_PATH_X_ACCESSOR
                = new CompoundTagHelper.IntTagAccessor(TILE_VIS_NODE_PATH_X);
        public static final CompoundTagHelper.IntTagAccessor TILE_VIS_NODE_PATH_Y_ACCESSOR
                = new CompoundTagHelper.IntTagAccessor(TILE_VIS_NODE_PATH_Y);
        public static final CompoundTagHelper.IntTagAccessor TILE_VIS_NODE_PATH_Z_ACCESSOR
                = new CompoundTagHelper.IntTagAccessor(TILE_VIS_NODE_PATH_Z);

    }
    public static class NodeBlockEntityCompoundTagAccessors {
        private static final String NODE_ID = "nodeId";
        public static final CompoundTagHelper.StringTagAccessor NODE_ID_ACCESSOR = new CompoundTagHelper.StringTagAccessor(NODE_ID);
        private static final String NODE_TYPE = "nodeType";
        public static final CompoundTagHelper.StringTagAccessor NODE_TYPE_ACCESSOR = new CompoundTagHelper.StringTagAccessor(NODE_TYPE);
        private static final String NODE_MODIFIER = "nodeModifier";
        public static final CompoundTagHelper.StringTagAccessor NODE_MODIFIER_ACCESSOR = new CompoundTagHelper.StringTagAccessor(NODE_MODIFIER);
        public static final String NODE_LAST_ACTIVE = "lastActive";
        public static final CompoundTagHelper.LongTagAccessor NODE_LAST_ACTIVE_ACCESSOR = new CompoundTagHelper.LongTagAccessor(NODE_LAST_ACTIVE);
        public static final String NODE_ASPECTS = "nodeAspects";
        public static final CompoundTagHelper.ListTagAccessor NODE_ASPECTS_ACCESSOR = new CompoundTagHelper.ListTagAccessor(NODE_ASPECTS);
        public static final String NODE_ASPECTS_BASE = "nodeAspectsBase";
        public static final CompoundTagHelper.ListTagAccessor NODE_ASPECTS_BASE_ACCESSOR = new CompoundTagHelper.ListTagAccessor(NODE_ASPECTS_BASE);
    }


    public static class MazeHandlerCompoundTagAccessors {
        private static final String MAZE_HANDLER_CELLS = "cells";
        public static final CompoundTagHelper.ListTagAccessor MAZE_HANDLER_CELLS_ACCESSOR = new CompoundTagHelper.ListTagAccessor(MAZE_HANDLER_CELLS);
        private static final String MAZE_HANDLER_CELL_LOC_X = "x";
        private static final String MAZE_HANDLER_CELL_LOC_Z = "z";
        private static final String MAZE_HANDLER_CELL_ID = "cell";
        public static final CompoundTagHelper.IntTagAccessor MAZE_HANDLER_CELL_LOC_X_ACCESSOR = new CompoundTagHelper.IntTagAccessor(MAZE_HANDLER_CELL_LOC_X);
        public static final CompoundTagHelper.IntTagAccessor MAZE_HANDLER_CELL_LOC_Z_ACCESSOR = new CompoundTagHelper.IntTagAccessor(MAZE_HANDLER_CELL_LOC_Z);
        public static final CompoundTagHelper.ShortTagAccessor MAZE_HANDLER_CELL_INFO_ACCESSOR = new CompoundTagHelper.ShortTagAccessor(MAZE_HANDLER_CELL_ID);
    }

    public static class WandCastingCompoundTagAccessors {
        private static final String WAND_CAP = "cap";
        private static final String WAND_ROD = "rod";
        private static final String WAND_FOCUS = "focus";
        private static final String WAND_OWING_VIS = "owningVis";
        public static final CompoundTagHelper.StringTagAccessor WAND_CAP_ACCESSOR = new CompoundTagHelper.StringTagAccessor(WAND_CAP);
        public static final CompoundTagHelper.StringTagAccessor WAND_ROD_ACCESSOR = new CompoundTagHelper.StringTagAccessor(WAND_ROD);
        public static final CompoundTagHelper.ItemStackTagAccessor WAND_FOCUS_ACCESSOR = new CompoundTagHelper.ItemStackTagAccessor(WAND_FOCUS);
        public static final CompoundTagHelper.VisOwningTagAccessor WAND_OWING_VIS_ACCESSOR = new CompoundTagHelper.VisOwningTagAccessor(WAND_OWING_VIS);
    }

    public static class FocusUpgradeCompoundTagAccessors {
        private static final String WAND_UPGRADE = "upgrade";
        public static final CompoundTagHelper.JsonObjectTagAccessor FOCUS_UPGRADE_JSON_ACCESSOR = new CompoundTagHelper.JsonObjectTagAccessor(WAND_UPGRADE);
    }

    public static class EldritchRingMapToMazeSize{
        private static final String WIDTH = "width";
        public static final CompoundTagHelper.IntTagAccessor WIDTH_ACCESSOR = new CompoundTagHelper.IntTagAccessor(WIDTH);
        private static final String HEIGHT = "height";
        public static final CompoundTagHelper.IntTagAccessor HEIGHT_ACCESSOR = new CompoundTagHelper.IntTagAccessor(HEIGHT);
    }

    public static class OwnedBlockEntityTagAccessors {
        private static final String OWNERS = "owners";
        public static final CompoundTagHelper.StringSetTagAccessor OWNERS_ACCESSOR = new CompoundTagHelper.StringSetTagAccessor(OWNERS);
    }
}
