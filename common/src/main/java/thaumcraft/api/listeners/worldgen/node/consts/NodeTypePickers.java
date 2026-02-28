package thaumcraft.api.listeners.worldgen.node.consts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.api.listeners.worldgen.node.listeners.NodeTypePicker;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;


public class NodeTypePickers {
    public static final NodeTypePicker DEFAULT_NODE_TYPE_PICKER = new NodeTypePicker(0) {
        
        @Override
        public NodeType onPickingNodeType(LevelAccessor world, BlockPos pos, RandomSource random, boolean silverwood, boolean eerie, boolean small, NodeType previous) {
            NodeType type = previous;
            if (silverwood) {
                type = NodeType.PURE;
            } else if (eerie) {
                type = NodeType.DARK;
            } else if (random.nextInt(Config.specialNodeRarity) == 0) {
                type = switch (random.nextInt(10)) {
                    case 0, 1, 2 -> NodeType.DARK;
                    case 3, 4, 5 -> NodeType.UNSTABLE;
                    case 6, 7, 8 -> NodeType.PURE;
                    case 9 -> NodeType.HUNGRY;
                    default -> type;
                };
            }
//            BiomeGenBase bg = world.getBiomeGenForCoords(x, z);
            Holder<Biome> biome = world.getBiome(pos);
            if (type != NodeType.PURE && biome.is(ThaumcraftBiomeIDs.TAINT_ID)) {
                if (random.nextBoolean()) {
                    type = NodeType.TAINTED;
                }
            }
            return type;
        }
    };
}
