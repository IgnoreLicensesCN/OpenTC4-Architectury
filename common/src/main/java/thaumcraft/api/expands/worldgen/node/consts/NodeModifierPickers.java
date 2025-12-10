package thaumcraft.api.expands.worldgen.node.consts;

import net.minecraft.world.World;
import thaumcraft.api.expands.worldgen.node.listeners.NodeModifierPicker;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.common.config.Config;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class NodeModifierPickers {

    public static final NodeModifierPicker DEFAULT_NODE_MODIFIER_PICKER = new NodeModifierPicker(0) {
        @Override
        @ParametersAreNonnullByDefault
        public NodeModifier onPickingNodeModifier(World world, int x, int y, int z, Random random, boolean silverwood, boolean eerie, boolean small,@Nullable NodeModifier previous) {
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
