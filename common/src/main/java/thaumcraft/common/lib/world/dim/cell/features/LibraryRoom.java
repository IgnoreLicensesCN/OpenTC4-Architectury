package thaumcraft.common.lib.world.dim.cell.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import thaumcraft.common.lib.world.dim.cell.CellState;

import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.*;

public class LibraryRoom extends CellFeature {
    public static final LibraryRoom INSTANCE = new LibraryRoom(false);
    protected LibraryRoom(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    public void generateRoomOuter(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        super.generateRoomOuter(world, random, cx, cz, y, cellState);
    }

    @Override
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos();

        generateRoomOuter(world, random, cx, cz, y, cellState);


        for(int a = 3; a <= 13; ++a) {
            for(int b = 3; b <= 13; ++b) {
                for(int c = 2; c < 11; ++c) {
                    if (a == 3 || a == 13 || b == 3 || b == 13) {
                        placeDecorationAt(world,random,mutable.set(x + a, y + c, z + b),cellState);
                    }
                }
            }
        }

        for(int a = 2; a <= 14; ++a) {
            for(int b = 2; b <= 14; ++b) {
                world.setBlock(mutable.set(x + a, y - 1, z + b),B,3);
                world.setBlock(mutable.set(x + a, y, z + b),V,3);
                placeDecorationAt(world,random,mutable.set(x + a, y + 1, z + b),cellState);
                world.setBlock(mutable.set(x + a, y + 12, z + b),B,3);
                world.setBlock(mutable.set(x + a, y + 11, z + b),V,3);
                placeDecorationAt(world,random,mutable.set(x + a, y + 10, z + b),cellState);
                if (a > 3 && a < 13 && b > 3 && b < 13) {
                    if (a <= 5 && b <= 5 || a <= 5 && b >= 11 || a >= 11 && b <= 5 || a >= 11 && b >= 11) {
                        placeDecorationAt(world,random,mutable.set(x + a, y + 2, z + b),cellState);
                        placeDecorationAt(world,random,mutable.set(x + a, y + 9, z + b),cellState);
                    }

                    if (a == 5 && b == 5 || a == 5 && b == 11 || a == 11 && b == 5 || a == 11 && b == 11) {
                        world.setBlock(mutable.set(x + a, y + 3, z + b),ANCIENT_STONE_PEDESTAL().defaultBlockState(),3);
                        world.setBlock(mutable.set(x + a, y + 8, z + b),ANCIENT_STONE_PEDESTAL().defaultBlockState(),3);
                    }
                }
            }
        }

        placeLowerEdge(world,random,x,y,z,cellState);
        for(int g = 0; g < 5; ++g) {
            world.setBlock(mutable.set( x + 6 + g, y + 9, z + 4),STAIR_LU.rotate(rotationFromNorth(Direction.NORTH)), 3);
            world.setBlock(mutable.set( x + 6 + g, y + 9, z + 12),STAIR_LU.rotate(rotationFromNorth(Direction.SOUTH)), 3);
            world.setBlock(mutable.set( x + 12, y + 9, z + 6 + g),STAIR_LU.rotate(rotationFromNorth(Direction.EAST)), 3);
            world.setBlock(mutable.set( x + 4, y + 9, z + 6 + g),STAIR_LU.rotate(rotationFromNorth(Direction.WEST)), 3);
        }

        world.setBlock(mutable.set(x + 5,y + 4, z + 11),GLYPHED_STONE().defaultBlockState(),3);
        world.setBlock(mutable.set(x + 5,y + 5, z + 11),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM),3);
        world.setBlock(mutable.set(x + 5,y + 6, z + 11),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP),3);
        world.setBlock(mutable.set(x + 5,y + 7, z + 11),GLYPHED_STONE().defaultBlockState(),3);

        world.setBlock(mutable.set(x + 5,y + 4, z + 5),GLYPHED_STONE().defaultBlockState(),3);
        world.setBlock(mutable.set(x + 5,y + 5, z + 5),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM),3);
        world.setBlock(mutable.set(x + 5,y + 6, z + 5),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP),3);
        world.setBlock(mutable.set(x + 5,y + 7, z + 5),GLYPHED_STONE().defaultBlockState(),3);

        world.setBlock(mutable.set(x + 11,y + 4, z + 5),GLYPHED_STONE().defaultBlockState(),3);
        world.setBlock(mutable.set(x + 11,y + 5, z + 5),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM),3);
        world.setBlock(mutable.set(x + 11,y + 6, z + 5),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP),3);
        world.setBlock(mutable.set(x + 11,y + 7, z + 5),GLYPHED_STONE().defaultBlockState(),3);

        world.setBlock(mutable.set(x + 11,y + 4, z + 11),GLYPHED_STONE().defaultBlockState(),3);
        world.setBlock(mutable.set(x + 11,y + 5, z + 11),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM),3);
        world.setBlock(mutable.set(x + 11,y + 6, z + 11),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP),3);
        world.setBlock(mutable.set(x + 11,y + 7, z + 11),GLYPHED_STONE().defaultBlockState(),3);


        world.setBlock(mutable.set(x + 8, y + 2, z + 8),ANCIENT_STONE_PEDESTAL().defaultBlockState(),3);
        world.setBlock(mutable.set(x + 8,y + 3, z + 8),GLYPHED_STONE().defaultBlockState(),3);
        world.setBlock(mutable.set(x + 8,y + 4, z + 8),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM),3);

        world.setBlock(mutable.set(x + 8,y + 7, z + 8),ANCIENT_STONE_SLAB().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP),3);
        world.setBlock(mutable.set(x + 8, y + 9, z + 8),ANCIENT_STONE_PEDESTAL().defaultBlockState(),3);
        world.setBlock(mutable.set(x + 8,y + 8, z + 8),GLYPHED_STONE().defaultBlockState(),3);

        generateConnections(world, random, cx, cz, y, cellState, 3, true);
    }
}
