package thaumcraft.common.lib.world.dim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.dim.cell.CellState;

import java.util.Random;

import static thaumcraft.common.lib.world.dim.cell.CellState.cellConnectedTo;

public class GenPortal extends GenCommon {
    static void generatePortal(Level world, Random random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos();

        for (int xOffset = 1; xOffset <= 15; ++xOffset) {
            for (int zOffset = 1; zOffset <= 15; ++zOffset) {
                for (int yOffset = 0; yOffset < 13; ++yOffset) {
                    if (xOffset == 1 || xOffset == 15 || zOffset == 1 || zOffset == 15) {
                        placeBlock(world, x + xOffset, y + yOffset, z + zOffset, B, cellState);
                    }
                }
            }
        }

        for (int xOffset = 2; xOffset <= 14; ++xOffset) {
            for (int zOffset = 2; zOffset <= 14; ++zOffset) {
                for (int yOffset = 1; yOffset < 12; ++yOffset) {
                    if ((xOffset == 2 || xOffset == 14 || zOffset == 2 || zOffset == 14)
                            && (xOffset != 2 || zOffset <= 3 || zOffset >= 12 || !cellConnectedTo(cellState, Direction.WEST) || yOffset >= 10)
                            && (xOffset != 14 || zOffset <= 3 || zOffset >= 12 || !cellConnectedTo(cellState, Direction.EAST) || yOffset >= 10)
                            && (zOffset != 2 || xOffset <= 3 || xOffset >= 12 || !cellConnectedTo(cellState, Direction.NORTH) || yOffset >= 10)
                            && (zOffset != 14 || xOffset <= 3 || xOffset >= 12 || !cellConnectedTo(cellState, Direction.SOUTH) || yOffset >= 10)
                    ) {
                        placeBlock(world, x + xOffset, y + yOffset, z + zOffset, V, cellState);
                    }
                }
            }
        }

        for (int xOffset = 3; xOffset <= 13; ++xOffset) {
            for (int zOffset = 3; zOffset <= 13; ++zOffset) {
                for (int yOffset = 2; yOffset < 11; ++yOffset) {
                    if ((xOffset == 3 || xOffset == 13 || zOffset == 3 || zOffset == 13) && (xOffset > 4 || zOffset > 4) && (xOffset > 4 || zOffset < 12) && (xOffset < 12 || zOffset > 4) && (xOffset < 12 || zOffset < 12)) {
                        placeBlock(world, x + xOffset, y + yOffset, z + zOffset, S, cellState);
                    }
                }
            }
        }

        for (int a = 2; a <= 14; ++a) {
            for (int b = 2; b <= 14; ++b) {
                placeBlock(world, x + a, y - 1, z + b, B, cellState);
                placeBlock(world, x + a, y, z + b, V, cellState);
                placeBlock(world, x + a, y + 1, z + b, SN, cellState);
                placeBlock(world, x + a, y + 13, z + b, B, cellState);
                placeBlock(world, x + a, y + 12, z + b, V, cellState);
                placeBlock(world, x + a, y + 11, z + b, S, cellState);
                {
                    int q = Math.min(Math.abs(8 - a), Math.abs(8 - b));

                    for (int g = 0; g < q - 1; ++g) {
                        placeBlock(world, x + a, y + 1 + g, z + b, 19, cellState);
                    }
                }

                if (a > 3 && a < 13 && b > 3 && b < 13) {
                    int q = Math.min(Math.abs(8 - a), Math.abs(8 - b));

                    for (int g = 0; g < q; ++g) {
                        placeBlock(world, x + a, y + 11 - g, z + b, 19, cellState);
                    }
                }
            }
        }

        for (int g = 0; g < 5; ++g) {
            placeBlock(world, x + 6 + g, y + 2, z + 4, STD, Direction.NORTH, cellState);
            placeBlock(world, x + 6 + g, y + 2, z + 12, STD, Direction.SOUTH, cellState);
            placeBlock(world, x + 12, y + 2, z + 6 + g, STD, Direction.EAST, cellState);
            placeBlock(world, x + 4, y + 2, z + 6 + g, STD, Direction.WEST, cellState);
        }

        GenCommon.generateConnections(world, random, cx, cz, y, cellState, 3, true);

        for (int a = 3; a <= 13; ++a) {
            for (int b = 3; b <= 13; ++b) {
                for (int c = 1; c < 12; ++c) {
                    if (a <= 4 && b <= 4 || a <= 4 && b >= 12 || a >= 12 && b <= 4 || a >= 12 && b >= 12) {
                        placeBlock(world, x + a, y + c, z + b, 9, cellState);
                        world.blockUpdated(mutable.set(x + a, y + c, z + b),Blocks.AIR);
                    }
                }
            }
        }

        placeBlock(world, x + 5, y + 3, z + 5, 10, Direction.NORTH, cellState);
        placeBlock(world, x + 4, y + 3, z + 5, 10, Direction.NORTH, cellState);
        placeBlock(world, x + 5, y + 3, z + 4, 10, Direction.WEST, cellState);
        placeBlock(world, x + 5, y + 8, z + 5, 11, Direction.NORTH, cellState);
        placeBlock(world, x + 4, y + 8, z + 5, 11, Direction.NORTH, cellState);
        placeBlock(world, x + 5, y + 8, z + 4, 11, Direction.WEST, cellState);
        placeBlock(world, x + 12, y + 3, z + 5, 10, Direction.NORTH, cellState);
        placeBlock(world, x + 11, y + 3, z + 5, 10, Direction.NORTH, cellState);
        placeBlock(world, x + 11, y + 3, z + 4, 10, Direction.EAST, cellState);
        placeBlock(world, x + 12, y + 8, z + 5, 11, Direction.NORTH, cellState);
        placeBlock(world, x + 11, y + 8, z + 5, 11, Direction.NORTH, cellState);
        placeBlock(world, x + 11, y + 8, z + 4, 11, Direction.EAST, cellState);
        placeBlock(world, x + 5, y + 3, z + 11, 10, Direction.SOUTH, cellState);
        placeBlock(world, x + 4, y + 3, z + 11, 10, Direction.SOUTH, cellState);
        placeBlock(world, x + 5, y + 3, z + 12, 10, Direction.WEST, cellState);
        placeBlock(world, x + 5, y + 8, z + 11, 11, Direction.SOUTH, cellState);
        placeBlock(world, x + 4, y + 8, z + 11, 11, Direction.SOUTH, cellState);
        placeBlock(world, x + 5, y + 8, z + 12, 11, Direction.WEST, cellState);
        placeBlock(world, x + 12, y + 3, z + 11, 10, Direction.SOUTH, cellState);
        placeBlock(world, x + 11, y + 3, z + 11, 10, Direction.SOUTH, cellState);
        placeBlock(world, x + 11, y + 3, z + 12, 10, Direction.EAST, cellState);
        placeBlock(world, x + 12, y + 8, z + 11, 11, Direction.SOUTH, cellState);
        placeBlock(world, x + 11, y + 8, z + 11, 11, Direction.SOUTH, cellState);
        placeBlock(world, x + 11, y + 8, z + 12, 11, Direction.EAST, cellState);

        //generated text with java

        {
            world.blockUpdated(mutable.set(x + 3, y + 1, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 2, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 3, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 4, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 5, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 6, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 7, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 8, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 9, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 10, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 11, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 1, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 2, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 3, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 4, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 5, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 6, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 7, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 8, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 9, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 10, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 11, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 1, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 2, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 3, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 4, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 5, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 6, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 7, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 8, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 9, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 10, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 11, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 1, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 2, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 3, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 4, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 5, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 6, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 7, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 8, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 9, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 10, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 3, y + 11, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 1, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 2, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 3, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 4, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 5, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 6, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 7, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 8, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 9, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 10, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 11, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 1, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 2, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 3, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 4, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 5, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 6, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 7, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 8, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 9, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 10, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 11, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 1, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 2, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 3, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 4, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 5, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 6, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 7, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 8, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 9, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 10, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 11, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 1, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 2, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 3, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 4, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 5, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 6, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 7, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 8, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 9, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 10, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 4, y + 11, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 1, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 2, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 3, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 4, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 5, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 6, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 7, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 8, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 9, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 10, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 11, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 1, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 2, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 3, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 4, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 5, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 6, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 7, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 8, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 9, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 10, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 11, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 1, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 2, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 3, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 4, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 5, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 6, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 7, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 8, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 9, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 10, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 11, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 1, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 2, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 3, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 4, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 5, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 6, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 7, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 8, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 9, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 10, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 12, y + 11, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 1, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 2, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 3, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 4, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 5, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 6, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 7, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 8, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 9, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 10, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 11, z + 3), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 1, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 2, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 3, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 4, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 5, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 6, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 7, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 8, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 9, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 10, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 11, z + 4), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 1, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 2, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 3, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 4, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 5, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 6, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 7, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 8, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 9, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 10, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 11, z + 12), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 1, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 2, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 3, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 4, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 5, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 6, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 7, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 8, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 9, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 10, z + 13), Blocks.AIR);
            world.blockUpdated(mutable.set(x + 13, y + 11, z + 13), Blocks.AIR);
        }
        world.setBlock(x + 8, y + 2, z + 8, ConfigBlocks.blockEldritch, 3, 3);
        world.setBlock(x + 8, y + 3, z + 8, ConfigBlocks.blockEldritchPortal);
        genObelisk(world, x + 8, y + 4, z + 8);
    }
}
