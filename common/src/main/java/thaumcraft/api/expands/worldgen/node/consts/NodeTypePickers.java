package thaumcraft.api.expands.worldgen.node.consts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.api.expands.worldgen.node.listeners.NodeTypePicker;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.Config;


import java.util.Random;

import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeTaint;


public class NodeTypePickers {
    public static final NodeTypePicker DEFAULT_NODE_TYPE_PICKER = new NodeTypePicker(0) {
        
        @Override
        public NodeType onPickingNodeType(Level world, int x, int y, int z, Random random, boolean silverwood, boolean eerie, boolean small,NodeType previous) {
            NodeType type = previous;
            if (silverwood) {
                type = NodeType.PURE;
            } else if (eerie) {
                type = NodeType.DARK;
            } else if (random.nextInt(Config.specialNodeRarity) == 0) {
                switch (random.nextInt(10)) {
                    case 0:
                    case 1:
                    case 2:
                        type = NodeType.DARK;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        type = NodeType.UNSTABLE;
                        break;
                    case 6:
                    case 7:
                    case 8:
                        type = NodeType.PURE;
                        break;
                    case 9:
                        type = NodeType.HUNGRY;
                }
            }
//            BiomeGenBase bg = world.getBiomeGenForCoords(x, z);
            Holder<Biome> biome = world.getBiome(new BlockPos(x, y, z));
            if (type != NodeType.PURE && biome.is(biomeTaint)) {
                if (random.nextBoolean()) {
                    type = NodeType.TAINTED;
                }
            }
            return type;
        }
    };
}
