package thaumcraft.client.renderers.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class TileBlockInfoGetter {

    @Nullable
    public static Block getBlockTypeSafely(TileEntity tile) {
        if (tile == null) {
            return null;
        }
        if (tile.blockType != null) {
            return tile.blockType;
        }
        if (tile.hasWorldObj()) {
            if (tile.getWorldObj() != null) {
                return tile.getBlockType();
            }
        }
        return null;
    }

    public static int getBlockMetaSafely(TileEntity tile) {
        if (tile == null) {
            return -1;
        }
        if (tile.blockMetadata != -1) {
            return tile.blockMetadata;
        }
        if  (tile.hasWorldObj()) {
            if (tile.getWorldObj() != null) {
                return tile.getBlockMetadata();
            }
        }
        return -1;
    }
}
