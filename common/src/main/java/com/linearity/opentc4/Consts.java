package com.linearity.opentc4;

import com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.mc.ItemStackTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect.AspectAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect.AspectListAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect.CentiVisListAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.*;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.researches.HexGridAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.*;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.ModifiableListAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.JsonObjectTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.ModifiableStringSetTagAccessor;
import net.minecraft.core.BlockPos;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class Consts {
    public static final int TAINT_SPREAD_UP_DISTANCE = 64;
    public static final int PURE_NODE_Y_RANGE = 8;


    public static class AspectCompoundTagAccessors {
        private static final String ASPECT_KEY = "key";//String
        public static final ResourceLocationTagAccessor ASPECT_KEY_ACCESSOR = new ResourceLocationTagAccessor(ASPECT_KEY);
        private static final String ASPECT_AMOUNT = "amount";//int
        public static final IntTagAccessor ASPECT_AMOUNT_ACCESSOR = new IntTagAccessor(ASPECT_AMOUNT);
        private static final String ASPECT_ASPECTS = "Aspects";//List<CompoundTag>
        public static final ListTagAccessor ASPECT_ASPECTS_ACCESSOR = new ListTagAccessor(ASPECT_ASPECTS);
    }

    public static class ThaumcraftPlayerCompoundTagAccessors {
        private static final String WARP_PERM = "Thaumcraft.eldritch";
        public static final IntTagAccessor THAUMCRAFT_PLAYER_WARP_PERM_ACCESSOR =
                new IntTagAccessor(WARP_PERM);

        private static final String WARP_TEMP = "Thaumcraft.eldritch.temp";
        public static final IntTagAccessor THAUMCRAFT_PLAYER_WARP_TEMP_ACCESSOR =
                new IntTagAccessor(WARP_TEMP);

        private static final String WARP_STICKY = "Thaumcraft.eldritch.sticky";
        public static final IntTagAccessor THAUMCRAFT_PLAYER_WARP_STICKY_ACCESSOR =
                new IntTagAccessor(WARP_STICKY);

        private static final String WARP_COUNTER = "Thaumcraft.eldritch.counter";
        public static final IntTagAccessor THAUMCRAFT_PLAYER_WARP_COUNTER_ACCESSOR =
                new IntTagAccessor(WARP_COUNTER);

        private static final String SHIELDING = "Thaumcraft.shielding";
        public static final IntTagAccessor THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR =
                new IntTagAccessor(SHIELDING);
        private static final String RESEARCH = "THAUMCRAFT.RESEARCH";
        public static final ListTagAccessor THAUMCRAFT_PLAYER_RESEARCH_ACCESSOR =
                new ListTagAccessor(RESEARCH);
        public static final ResearchItemResourceLocationTagAccessor LIST_TAG_RESEARCH_ACCESSOR =
                new ResearchItemResourceLocationTagAccessor("research");

        private static final String CLUE = "THAUMCRAFT.CLUE";
        public static final ListTagAccessor THAUMCRAFT_PLAYER_CLUE_ACCESSOR =
                new ListTagAccessor(CLUE);
        public static final ClueResourceLocationTagAccessor LIST_TAG_CLUE_ACCESSOR =
                new ClueResourceLocationTagAccessor("clue");

        private static final String ASPECTS = "THAUMCRAFT.ASPECTS";
        public static final ListTagAccessor THAUMCRAFT_PLAYER_ASPECTS_ACCESSOR =
                new ListTagAccessor(ASPECTS);
        public static final AspectResourceLocationTagAccessor LIST_TAG_ASPECT_ACCESSOR =
                new AspectResourceLocationTagAccessor("aspect");
        public static final IntTagAccessor LIST_TAG_ASPECT_INT_ACCESSOR =
                new IntTagAccessor("aspect_amount");

        private static final String SCAN_OBJECTS = "THAUMCRAFT.SCAN.OBJECTS";
        public static final ListTagAccessor THAUMCRAFT_PLAYER_SCAN_OBJECTS_ACCESSOR =
                new ListTagAccessor(SCAN_OBJECTS);

        private static final String SCAN_ENTITIES = "THAUMCRAFT.SCAN.ENTITIES";
        public static final ListTagAccessor THAUMCRAFT_PLAYER_SCAN_ENTITIES_ACCESSOR =
                new ListTagAccessor(SCAN_ENTITIES);

        private static final String SCAN_PHENOMENA = "THAUMCRAFT.SCAN.PHENOMENA";
        public static final ListTagAccessor THAUMCRAFT_PLAYER_SCAN_PHENOMENA_ACCESSOR =
                new ListTagAccessor(SCAN_PHENOMENA);


        private static final String SCAN_OBJECT_ITEM = "scannedObj";
        public static final StringTagAccessor LIST_TAG_SCANNED_OBJECT_ACCESSOR =
                new StringTagAccessor(SCAN_OBJECT_ITEM);

        private static final String SCAN_ENTITY_ITEM = "scannedEntity";
        public static final StringTagAccessor LIST_TAG_SCANNED_ENTITY_ACCESSOR =
                new StringTagAccessor(SCAN_ENTITY_ITEM);
        private static final String SCAN_PHENOMENA_ITEM = "scannedPhenomena";
        public static final StringTagAccessor LIST_TAG_SCANNED_PHENOMENA_ACCESSOR =
                new StringTagAccessor(SCAN_PHENOMENA_ITEM);
    }

    public static class WorldCoordsCompoundTagAccessors {
        private static final String WORLD_X = "w_x"; // int
        public static final IntTagAccessor WORLD_X_ACCESSOR = new IntTagAccessor(WORLD_X);

        private static final String WORLD_Y = "w_y"; // int
        public static final IntTagAccessor WORLD_Y_ACCESSOR = new IntTagAccessor(WORLD_Y);

        private static final String WORLD_Z = "w_z"; // int
        public static final IntTagAccessor WORLD_Z_ACCESSOR = new IntTagAccessor(WORLD_Z);

        private static final String WORLD_DIM = "w_d"; // String
        public static final StringTagAccessor WORLD_DIM_ACCESSOR = new StringTagAccessor(WORLD_DIM);
    }

    public static class BlockPosCompoundTagKeys {
        private static final String BLOCK_X = "b_x"; // int
        public static final IntTagAccessor BLOCK_X_ACCESSOR = new IntTagAccessor(BLOCK_X);

        private static final String BLOCK_Y = "b_y"; // int
        public static final IntTagAccessor BLOCK_Y_ACCESSOR = new IntTagAccessor(BLOCK_Y);

        private static final String BLOCK_Z = "b_z"; // int
        public static final IntTagAccessor BLOCK_Z_ACCESSOR = new IntTagAccessor(BLOCK_Z);
    }

    public static class EnchantmentConsts{

    }

    public static class ResearchNoteCompoundTagAccessors {

        private static final String RESEARCH_NOTE_COLOR = "color";
        public static final IntTagAccessor RESEARCH_NOTE_COLOR_ACCESSOR =
                new IntTagAccessor(RESEARCH_NOTE_COLOR);

        private static final String RESEARCH_NOTE_COMPLETE = "complete";
        public static final BooleanTagAccessor RESEARCH_NOTE_COMPLETE_ACCESSOR =
                new BooleanTagAccessor(RESEARCH_NOTE_COMPLETE);

        private static final String RESEARCH_NOTE_COPIES = "copies";
        public static final IntTagAccessor RESEARCH_NOTE_COPIES_ACCESSOR =
                new IntTagAccessor(RESEARCH_NOTE_COPIES);

        private static final String RESEARCH_NOTE_HEXGRID = "hexgrid";
        public static final HexGridAccessor RESEARCH_NOTE_HEXGRID_ACCESSOR =
                new HexGridAccessor(RESEARCH_NOTE_HEXGRID);
        public static final ResearchItemResourceLocationTagAccessor RESEARCH_NOTE_RESEARCH_ACCESSOR =
                new ResearchItemResourceLocationTagAccessor("research");
    }

    public static class TileVisNodeCompoundTagAccessors {
        private static final String TILE_VIS_NODE_LINKS = "Link";
        public static final ModifiableListAccessor<BlockPos> TILE_VIS_NODE_LINKS_ACCESSOR =
                new ModifiableListAccessor<>(TILE_VIS_NODE_LINKS,new BlockPosAccessor(TILE_VIS_NODE_LINKS + "_block_pos"));
    }
    public static class NodeBlockEntityCompoundTagAccessors {
        private static final String NODE_ID = "nodeId";
        public static final StringTagAccessor NODE_ID_ACCESSOR = new StringTagAccessor(NODE_ID);
        private static final String NODE_TYPE = "nodeType";
        public static final NodeTypeResourceLocationTagAccessor NODE_TYPE_ACCESSOR = new NodeTypeResourceLocationTagAccessor(NODE_TYPE);
        private static final String NODE_MODIFIER = "nodeModifier";
        public static final NodeModifierResourceLocationTagAccessor NODE_MODIFIER_ACCESSOR = new NodeModifierResourceLocationTagAccessor(NODE_MODIFIER);
        public static final String NODE_LAST_ACTIVE = "lastActive";
        public static final LongTagAccessor NODE_LAST_ACTIVE_ACCESSOR = new LongTagAccessor(NODE_LAST_ACTIVE);
        public static final String NODE_ASPECTS = "nodeAspects";
        public static final AspectListAccessor NODE_ASPECTS_ACCESSOR = new AspectListAccessor(NODE_ASPECTS);
        public static final String NODE_ASPECTS_BASE = "nodeAspectsBase";
        public static final AspectListAccessor NODE_ASPECTS_BASE_ACCESSOR = new AspectListAccessor(NODE_ASPECTS_BASE);
    }


    public static class MazeHandlerCompoundTagAccessors {
        private static final String MAZE_HANDLER_CELLS = "cells";
        public static final ListTagAccessor MAZE_HANDLER_CELLS_ACCESSOR = new ListTagAccessor(MAZE_HANDLER_CELLS);
        private static final String MAZE_HANDLER_CELL_LOC_X = "x";
        private static final String MAZE_HANDLER_CELL_LOC_Z = "z";
        private static final String MAZE_HANDLER_CELL_ID = "cell";
        public static final IntTagAccessor MAZE_HANDLER_CELL_LOC_X_ACCESSOR = new IntTagAccessor(MAZE_HANDLER_CELL_LOC_X);
        public static final IntTagAccessor MAZE_HANDLER_CELL_LOC_Z_ACCESSOR = new IntTagAccessor(MAZE_HANDLER_CELL_LOC_Z);
        public static final ShortTagAccessor MAZE_HANDLER_CELL_INFO_ACCESSOR = new ShortTagAccessor(MAZE_HANDLER_CELL_ID);
    }

    public static class WandCastingCompoundTagAccessors {
        private static final String WAND_CAP = "cap";
        private static final String WAND_ROD = "rod";
        private static final String WAND_FOCUS = "focus";
        private static final String WAND_OWING_VIS = "owningVis";
        public static final ItemStackTagAccessor WAND_CAP_ACCESSOR = new ItemStackTagAccessor(WAND_CAP);
        public static final ItemStackTagAccessor WAND_ROD_ACCESSOR = new ItemStackTagAccessor(WAND_ROD);
        public static final ItemStackTagAccessor WAND_FOCUS_ACCESSOR = new ItemStackTagAccessor(WAND_FOCUS);
        public static final CentiVisListAccessor WAND_OWING_VIS_ACCESSOR = new CentiVisListAccessor(WAND_OWING_VIS);
    }

    public static class FocusUpgradeCompoundTagAccessors {
        private static final String WAND_UPGRADE = "upgrade";
        public static final ModifiableListAccessor<FocusUpgradeTypeResourceLocation> FOCUS_UPGRADE_ACCESSOR =
                new ModifiableListAccessor<>(WAND_UPGRADE,new FocusUpgradeTypeResourceLocationTagAccessor(WAND_UPGRADE));
        public static final JsonObjectTagAccessor FOCUS_UPGRADE_JSON_ACCESSOR = new JsonObjectTagAccessor(WAND_UPGRADE);
    }

    public static class EldritchRingMapToMazeSize{
        private static final String WIDTH = "width";
        public static final IntTagAccessor WIDTH_ACCESSOR = new IntTagAccessor(WIDTH);
        private static final String HEIGHT = "height";
        public static final IntTagAccessor HEIGHT_ACCESSOR = new IntTagAccessor(HEIGHT);
    }

    public static class OwnedBlockEntityTagAccessors {
        private static final String OWNERS = "owners";
        public static final ModifiableStringSetTagAccessor OWNERS_ACCESSOR = new ModifiableStringSetTagAccessor(OWNERS);
    }

    public static class InfernalFurnaceBlockEntityTagAccessors {
        private static final String PROCESSED_TICKS = "processed_ticks";
        public static final IntTagAccessor PROCESSED_TICKS_ACCESSOR = new IntTagAccessor(PROCESSED_TICKS);
        private static final String PROCESSING_ITEM_STACK = "processing_item_stack";
        public static final ItemStackTagAccessor PROCESSING_ITEM_STACK_ACCESSOR = new ItemStackTagAccessor(PROCESSING_ITEM_STACK);
    }

    public static class DeconstructionTableBlockEntityTagAccessors {
        private static final String STORING_ASPECT = "storing_aspect";
        public static final AspectAccessor STORING_ASPECT_ACCESSOR = new AspectAccessor(STORING_ASPECT);
        private static final String BREAK_TIME_REMAINING = "break_time_remaining";
        public static final IntTagAccessor BREAK_TIME_ACCESSOR = new IntTagAccessor(BREAK_TIME_REMAINING);
    }
    public static class ResearchTableBlockEntityTagAccessors {
        private static final String TICK_COUNT = "tick_count";
        public static final IntTagAccessor TICK_COUNT_ACCESSOR = new IntTagAccessor(TICK_COUNT);
        private static final String BONUS_ASPECT = "bonus_aspect";
        public static final AspectListAccessor BONUS_ASPECT_ACCESSOR = new AspectListAccessor(BONUS_ASPECT);
    }
    public static class EnergizedAuraNodeBlockEntityTagAccessors {
        public static final StringTagAccessor NODE_ID_ACCESSOR = NodeBlockEntityCompoundTagAccessors.NODE_ID_ACCESSOR;
        public static final NodeTypeResourceLocationTagAccessor NODE_TYPE_ACCESSOR = NodeBlockEntityCompoundTagAccessors.NODE_TYPE_ACCESSOR;
        public static final NodeModifierResourceLocationTagAccessor NODE_MODIFIER_ACCESSOR = NodeBlockEntityCompoundTagAccessors.NODE_MODIFIER_ACCESSOR;
        public static final CentiVisListAccessor NODE_CENTIVIS_BASE_ACCESSOR = new CentiVisListAccessor("centivis_base");
        public static final AspectListAccessor NODE_ASPECT_BASE_ACCESSOR = NodeBlockEntityCompoundTagAccessors.NODE_ASPECTS_BASE_ACCESSOR;
    }
    public static class NodeTransducerBlockEntityTagAccessors {
        public static final IntTagAccessor TRANSDUCER_TICK_COUNT = new IntTagAccessor("tick_count");
        public static final IntTagAccessor TRANSDUCER_STATUS_CODE = new IntTagAccessor("status_code");
    }
    public static class AlchemicalFurnaceBlockEntityTagAccessors {
        public static final AspectListAccessor ASPECTS_OWNING = new AspectListAccessor("aspects_owning");
        public static final IntTagAccessor COOKED_TIME = new IntTagAccessor("cooked_time");
        public static final IntTagAccessor REQUIRED_COOK_TIME = new IntTagAccessor("required_cook_time");
        public static final IntTagAccessor FUEL_REMAINING_TIME = new IntTagAccessor("fuel_remaining_time");
        public static final IntTagAccessor FUEL_TOTAL_TIME = new IntTagAccessor("fuel_total_time");
    }

}
