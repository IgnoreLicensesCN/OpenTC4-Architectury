package thaumcraft.api.expands.worldgen.node.consts;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.expands.worldgen.node.CreateNodeContext;
import thaumcraft.api.expands.worldgen.node.listeners.CreateNodeListener;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileNode;

public class CreateNodeListeners {
    public static CreateNodeListener DEFAULT_NODE_CREATOR = new CreateNodeListener(0) {
        @Override
        public boolean onCreateNode(World world, CreateNodeContext context) {
            if (world.isAirBlock(context.x, context.y, context.z)) {
                world.setBlock(context.x, context.y, context.z, ConfigBlocks.blockAiry, 0, 0);
            }

            TileEntity te = world.getTileEntity(context.x, context.y, context.z);
            if (te instanceof TileNode) {
                TileNode tileNode = (TileNode) te;
                tileNode.setNodeType(context.nodeType);
                tileNode.setNodeModifier(context.nodeModifier);
                tileNode.setAspects(context.aspects);
            }

            world.markBlockForUpdate(context.x, context.y, context.z);
            return false;
        }
    };
}
