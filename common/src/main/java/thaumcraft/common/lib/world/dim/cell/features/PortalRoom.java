package thaumcraft.common.lib.world.dim.cell.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import thaumcraft.common.lib.world.dim.cell.CellState;

import static net.minecraft.world.level.block.StairBlock.FACING;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.*;
import static thaumcraft.common.blocks.worldgenerated.eldritch.EldritchAltarBlock.ELDRITCH_ALTAR_EYES;

public class PortalRoom extends CellFeature {
    public static final PortalRoom INSTANCE = new PortalRoom(true);
    protected PortalRoom(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos();
        generateRoomOuter(world, random, x, y, z, cellState);

        for (int xOffset = 3; xOffset <= 13; ++xOffset) {
            for (int zOffset = 3; zOffset <= 13; ++zOffset) {
                for (int yOffset = 2; yOffset < 11; ++yOffset) {
                    if ((xOffset == 3 || xOffset == 13 || zOffset == 3 || zOffset == 13) && (xOffset > 4 || zOffset > 4) && (xOffset > 4 || zOffset < 12) && (xOffset < 12 || zOffset > 4) && (xOffset < 12 || zOffset < 12)) {
                        world.setBlock(mutable.set(x + xOffset, y + yOffset, z + zOffset), S, 3);
                    }
                }
            }
        }
        for (int xOffset = 2; xOffset <= 14; ++xOffset) {
            for (int zOffset = 2; zOffset <= 14; ++zOffset) {

                world.setBlock(mutable.set(x + xOffset, y - 1, z + zOffset), B, 3);
                world.setBlock(mutable.set(x + xOffset, y, z + zOffset), V, 3);

                if (!world.getBlockState(mutable.set(x + xOffset, y + 1, z + zOffset)).is(V.getBlock())) {
                    world.setBlock(mutable,S,3);
                }

                world.setBlock(mutable.set(x + xOffset, y + 13, z + zOffset), B, 3);
                world.setBlock(mutable.set(x + xOffset, y + 12, z + zOffset), V, 3);
                world.setBlock(mutable.set(x + xOffset, y + 11, z + zOffset), S, 3);
                {
                    int q = Math.min(Math.abs(8 - xOffset), Math.abs(8 - zOffset));

                    for (int yOffset = 0; yOffset < q - 1; ++yOffset) {
                        if (!world.getBlockState(mutable.set(x + xOffset, y + 1 + yOffset, z + zOffset)).is(V.getBlock())) {
                            world.setBlock(mutable,S,3);
                        }
                    }
                }

                if (xOffset > 3 && xOffset < 13 && zOffset > 3 && zOffset < 13) {
                    int yOffset = Math.min(Math.abs(8 - xOffset), Math.abs(8 - zOffset));

                    for (int g = 0; g < yOffset; ++g) {
                        if (!world.getBlockState(mutable.set(x + xOffset, y + 11 - g, z + zOffset)).is(V.getBlock())) {
                            world.setBlock(mutable,S,3);
                        }
                    }
                }
            }
        }
        for (int g = 0; g < 5; ++g) {
            world.setBlock(mutable.set(x + 6 + g, y + 2, z + 4),STAIR_LD.setValue(FACING,Direction.NORTH),3);
            world.setBlock(mutable.set(x + 6 + g, y + 2, z + 12),STAIR_LD.setValue(FACING,Direction.SOUTH),3);
            world.setBlock(mutable.set(x + 12, y + 2, z + 6 + g),STAIR_LD.setValue(FACING,Direction.EAST),3);
            world.setBlock(mutable.set(x + 4, y + 2, z + 6 + g),STAIR_LD.setValue(FACING,Direction.WEST),3);
        }
        generateConnections(world,random,cx, cz, y, cellState, 3, true);
    }

    @Override
    public void placeFeatureAfter(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos();
        for (int xOffset = 3; xOffset <= 13; ++xOffset) {
            for (int zOffset = 3; zOffset <= 13; ++zOffset) {
                for (int yOffset = 1; yOffset < 12; ++yOffset) {
                    if (xOffset <= 4 && zOffset <= 4 || xOffset <= 4 && zOffset >= 12 || xOffset >= 12 && zOffset <= 4 || xOffset >= 12 && zOffset >= 12) {
                        world.setBlock(mutable.set(x + xOffset, y + yOffset, z + zOffset),Blocks.AIR.defaultBlockState(),3);
                        world.blockUpdated(mutable.set(x + xOffset, y + yOffset, z + zOffset), Blocks.AIR);
                    }
                }
            }
        }
        world.setBlock(mutable.set(x + 5, y + 3, z + 5),STAIR_LD.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 4, y + 3, z + 5),STAIR_LD.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 5, y + 3, z + 4),STAIR_LD.setValue(FACING,Direction.WEST),3);
        world.setBlock(mutable.set(x + 12, y + 3, z + 5),STAIR_LD.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 11, y + 3, z + 5),STAIR_LD.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 11, y + 3, z + 4),STAIR_LD.setValue(FACING,Direction.EAST),3);
        world.setBlock(mutable.set(x + 5, y + 3, z + 11),STAIR_LD.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 4, y + 3, z + 11),STAIR_LD.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 5, y + 3, z + 12),STAIR_LD.setValue(FACING,Direction.WEST),3);
        world.setBlock(mutable.set(x + 12, y + 3, z + 11),STAIR_LD.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 11, y + 3, z + 11),STAIR_LD.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 11, y + 3, z + 12),STAIR_LD.setValue(FACING,Direction.EAST),3);

        world.setBlock(mutable.set(x + 5, y + 8, z + 5),STAIR_LU.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 4, y + 8, z + 5),STAIR_LU.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 5, y + 8, z + 4),STAIR_LU.setValue(FACING,Direction.WEST),3);
        world.setBlock(mutable.set(x + 12, y + 8, z + 5),STAIR_LU.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 11, y + 8, z + 5),STAIR_LU.setValue(FACING,Direction.NORTH),3);
        world.setBlock(mutable.set(x + 11, y + 8, z + 4),STAIR_LU.setValue(FACING,Direction.EAST),3);
        world.setBlock(mutable.set(x + 5, y + 8, z + 11),STAIR_LU.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 4, y + 8, z + 11),STAIR_LU.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 5, y + 8, z + 12),STAIR_LU.setValue(FACING,Direction.WEST),3);
        world.setBlock(mutable.set(x + 12, y + 8, z + 11),STAIR_LU.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 11, y + 8, z + 11),STAIR_LU.setValue(FACING,Direction.SOUTH),3);
        world.setBlock(mutable.set(x + 11, y + 8, z + 12),STAIR_LU.setValue(FACING,Direction.EAST),3);

        world.setBlock(mutable.set(x + 8, y + 2, z + 8), ELDRITCH_ALTAR().defaultBlockState().setValue(ELDRITCH_ALTAR_EYES,4), 3);//just dont waste eyes here
        world.setBlock(mutable.set(x + 8, y + 3, z + 8), ELDRITCH_PORTAL().defaultBlockState(), 3);
        genObelisk(world, x + 8, y + 4, z + 8);
    }
}
