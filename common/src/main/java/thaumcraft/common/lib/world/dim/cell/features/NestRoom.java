package thaumcraft.common.lib.world.dim.cell.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.world.dim.cell.CellState;

import static net.minecraft.world.level.block.StairBlock.FACING;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.*;
import static thaumcraft.common.blocks.crafted.loot.DefaultAbstractLootBlock.DEFAULT_RARITY_PROPERTY;
import static thaumcraft.common.lib.world.dim.cell.CellState.cellConnectedTo;

//7
public class NestRoom extends CellFeature{
    public static final NestRoom INSTANCE = new NestRoom(false);
    protected NestRoom(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    public void generateRoomOuter(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos();
        for (int a = 1; a <= 15; ++a) {
            for (int b = 1; b <= 15; ++b) {
                for (int c = 0; c < 11; ++c) {
                    if (a == 1 || a == 15 || b == 1 || b == 15) {
                        world.setBlock(mutable.set(x + a, y + c, z + b), B, 3);
                    }
                }
            }
        }
        for (int a = 2; a <= 14; ++a) {
            for (int b = 2; b <= 14; ++b) {
                for (int c = 1; c < 10; ++c) {
                    if ((a == 2 || a == 14 || b == 2 || b == 14)
                            && (a != 2 || b <= 3 || b >= 12 || !cellConnectedTo(cellState,Direction.WEST))
                            && (a != 14 || b <= 3 || b >= 12 || !cellConnectedTo(cellState,Direction.EAST))
                            && (b != 2 || a <= 3 || a >= 12 || !cellConnectedTo(cellState,Direction.NORTH))
                            && (b != 14 || a <= 3 || a >= 12 || !cellConnectedTo(cellState,Direction.SOUTH))) {
                        world.setBlock(mutable.set(x + a, y + c, z + b), V, 3);
                    }
                }
            }
        }
    }
    
    protected void crabSpawnerForceGen(WorldGenLevel world, RandomSource random, BlockPos pos, CellState cellState){
        if (!world.getBlockState(pos).is(V.getBlock())){

            BlockState currentState = ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_STONE().defaultBlockState();
            if (random.nextInt(25) == 0){
                currentState =  ThaumcraftBlocks.ThaumcraftBlockInstances.GLOWING_CRUSTED_STONE().defaultBlockState();
            } else if (random.nextInt(25) == 0){
                var newState = getCrabSpawnerForPlacement(world, random, pos, cellState);
                if (newState != null){
                    currentState = newState;
                }
            }
            world.setBlock(pos, currentState, 3);
        }
    }

    @Override
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos();
        generateRoomOuter(world, random, x, y, z, cellState);

        for(int a = 3; a <= 13; ++a) {
            for(int b = 3; b <= 13; ++b) {
                for(int c = 2; c < 9; ++c) {
                    if (a == 3 || a == 13 || b == 3 || b == 13) {
                        crabSpawnerForceGen(world, random, mutable.set(x + a, y + c, z + b), cellState);
                    }

                    if ((a == 4 && !cellConnectedTo(cellState,Direction.WEST) 
                            || a == 12 && !cellConnectedTo(cellState,Direction.EAST) 
                            || b == 4 && !cellConnectedTo(cellState,Direction.NORTH) 
                            || b == 12 && !cellConnectedTo(cellState,Direction.SOUTH)) 
                            && random.nextBoolean()
                    ) {
                        crabSpawnerForceGen(world, random, mutable.set(x + a, y + c, z + b), cellState);
                    }
                }
            }
        }

        for(int a = 2; a <= 14; ++a) {
            for(int b = 2; b <= 14; ++b) {
                world.setBlock(mutable.set(x + a, y - 1, z + b), B, 3);
                world.setBlock(mutable.set(x + a, y, z + b), V, 3);
                crabSpawnerForceGen(world, random, mutable.set(x + a, y + 1, z + b), cellState);
                world.setBlock(mutable.set(x + a, y + 11, z + b), B, 3);
                world.setBlock(mutable.set(x + a, y + 10, z + b), V, 3);
                crabSpawnerForceGen(world, random, mutable.set(x + a, y + 9, z + b), cellState);
                mutable.set(x + a, y + 8, z + b);
                if (random.nextBoolean()) {
                    crabSpawnerForceGen(world, random, mutable, cellState);
                } else if (random.nextBoolean() && world.getBlockState(mutable).isAir()) {
                    world.setBlock(mutable, STRANGE_CRYSTALS().defaultBlockState().setValue(FACING,Direction.DOWN), 7, 3);
                }
            }
        }

        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 2, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 3, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 4, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 7, y + 2, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 2, z + 7), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 9, y + 2, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 2, z + 9), cellState);
        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 7, y + 3, z + 8), cellState);
        }

        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 3, z + 7), cellState);
        }

        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 9, y + 3, z + 8), cellState);
        }

        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 3, z + 9), cellState);
        }

        if (random.nextBoolean()) {
            world.setBlock(mutable.set(x + 8, y + 5, z + 8), G, 3);
        }

        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 8, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 7, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 6, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 7, y + 8, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 8, z + 7), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 9, y + 8, z + 8), cellState);
        crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 8, z + 9), cellState);
        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 7, y + 7, z + 8), cellState);
        }

        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 7, z + 7), cellState);
        }

        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 9, y + 7, z + 8), cellState);
        }

        if (random.nextBoolean()) {
            crabSpawnerForceGen(world, random, mutable.set(x + 8, y + 7, z + 9), cellState);
        }

        generateConnections(world, random, cx, cz, y, cellState, 3, true);

        for(int a = -5; a <= 5; ++a) {
            for(int b = -5; b <= 5; ++b) {
                if (random.nextFloat() < 0.15F && world.getBlockState(mutable.set(x + 8 + a, y + 2, z + 8 + b)).isAir()) {
                    float rr = random.nextFloat();
                    int md = rr < 0.15F ? 2 : (rr < 0.4F ? 1 : 0);
                    world.setBlock(mutable, (random.nextFloat() < 0.2F ? CRATE_LOOT() : URN_LOOT()).defaultBlockState().setValue(DEFAULT_RARITY_PROPERTY,md), 3);
                }
            }
        }
    }

    @Override
    protected void placeDecorationAt(WorldGenLevel world, RandomSource random, BlockPos pos, CellState cellState) {
        if (!world.getBlockState(pos).is(V.getBlock())) {
            if (random.nextInt(25) == 0){
                var newState = getCrabSpawnerForPlacement(world, random, pos, cellState);
                if (newState != null){
                    world.setBlock(pos,newState,3);
                }
            }else {
                world.setBlock(pos,S,3);
            }
        }
    }
}
