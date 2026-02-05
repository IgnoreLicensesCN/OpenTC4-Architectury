package thaumcraft.api.expands.listeners.worldgen.node.consts;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.api.expands.listeners.worldgen.node.listeners.NodeModifierPicker;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.common.config.Config;

import org.jetbrains.annotations.Nullable;

public class NodeModifierPickers {

    public static final NodeModifierPicker DEFAULT_NODE_MODIFIER_PICKER = new NodeModifierPicker(0) {
        @Override
        
        public NodeModifier onPickingNodeModifier(WorldGenLevel world, BlockPos pos, RandomSource random, boolean silverwood, boolean eerie, boolean small, @Nullable NodeModifier previous) {
            if (random.nextInt(Config.specialNodeRarity / 2) == 0) {
                switch (random.nextInt(3)) {
                    case 0:
                        return NodeModifier.BRIGHT;
                    case 1:
                        return NodeModifier.PALE;
                    case 2:
                        return NodeModifier.FADING;
                }
            }
            return null;
        }
    };
}
