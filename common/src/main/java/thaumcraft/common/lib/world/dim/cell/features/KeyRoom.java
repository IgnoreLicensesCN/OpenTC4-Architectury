package thaumcraft.common.lib.world.dim.cell.features;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.phys.Vec3;
import thaumcraft.common.entities.SpecialItemEntity;
import thaumcraft.common.entities.monster.eldritch.EldritchGuardianEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.dim.cell.CellState;

import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CAPSTONE;

public class KeyRoom extends CellFeature {
    public static final KeyRoom INSTANCE = new KeyRoom(true);

    protected KeyRoom(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos(x, y, z);

        generateRoomOuter(world, random, x, y, z, cellState);

        for (int a = 3; a <= 13; ++a) {
            for (int b = 3; b <= 13; ++b) {
                for (int c = 2; c < 11; ++c) {
                    if (!world.getBlockState(mutable.set(x + a, y + c, z + b)).is(V.getBlock())){
                        if (a == 3 || a == 13 || b == 3 || b == 13) {
                            if (c > 3 && c < 9 && (a == 8 || b == 8) || c > 4 && c < 8 && (a == 7 || b == 7 || a == 9 || b == 9)) {
                                if (a != 8 && b != 8 || c != 6) {
                                    world.setBlock(mutable.set(x + a, y + c, z + b), S, 3);
                                }
                            } else {
                                world.setBlock(mutable.set(x + a, y + c, z + b), R, 3);
                            }
                        }
                    }
                }
            }
        }

        for (int a = 2; a <= 14; ++a) {
            for (int b = 2; b <= 14; ++b) {
                world.setBlock(mutable.set(x + a, y - 1, z + b), B, 3);
                world.setBlock(mutable.set(x + a, y, z + b), V, 3);
                placeDecorationAt(world, random, mutable.set(x + a, y + 1, z + b), cellState);
                world.setBlock(mutable.set(x + a, y + 13, z + b), B, 3);
                world.setBlock(mutable.set(x + a, y + 12, z + b), V, 3);
                placeDecorationAt(world, random, mutable.set(x + a, y + 11, z + b), cellState);

                int q = Math.min(Math.abs(8 - a), Math.abs(8 - b));

                for (int yOffset = 0; yOffset < q - 1; ++yOffset) {
                    placeDecorationAt(world, random, mutable.set(x + a, y + 1 + yOffset, z + b), cellState);
                }


                if (a > 3 && a < 13 && b > 3 && b < 13) {
                    for (int yOffset = 0; yOffset < q; ++yOffset) {
                        placeDecorationAt(world, random, mutable.set(x + a, y + 11 - yOffset, z + b), cellState);
                    }
                }
            }
        }

        placeLowerEdge(world,random,x,y,z,cellState);

        generateConnections(world, random, cx, cz, y, cellState, 3, true);
        world.blockUpdated(mutable.set(x + 8, y + 2, z + 8), ELDRITCH_CAPSTONE());
        ServerLevel serverLevel = world.getLevel();
        var entityitem = new SpecialItemEntity(
                serverLevel,
                (double) x + (double) 8.5F, (double) y + (double) 3.5F, (double) z + (double) 8.5F,
                new ItemStack(ThaumcraftItemInstances.RUNED_TABLET())
        );
        entityitem.setUnlimitedLifetime();
        entityitem.setDeltaMovement(Vec3.ZERO);
        world.addFreshEntity(entityitem);
        int zz = 2 + (world.getDifficulty() == Difficulty.HARD ? 2 : (world.getDifficulty() == Difficulty.NORMAL ? 1 : 0));

        for (int qq = 0; qq < zz; ++qq) {
            var eg = new EldritchGuardianEntity(serverLevel);
            double i1 = (double) x + (double) 8.5F + (double) (MathHelper.getRandomIntegerInRange(world.getRandom(), 1, 3)
                    * MathHelper.getRandomIntegerInRange(world.getRandom(), -1, 1));
            double j1 = y + 2;
            double k1 = (double) z + (double) 8.5F + (double) (MathHelper.getRandomIntegerInRange(world.getRandom(), 1, 3)
                    * MathHelper.getRandomIntegerInRange(world.getRandom(), -1, 1));
            eg.setPos(i1, j1, k1);
            eg.finalizeSpawn(serverLevel,serverLevel.getCurrentDifficultyAt(eg.blockPosition()), MobSpawnType.CHUNK_GENERATION,null,null);
            eg.restrictTo(new BlockPos(x + 8, y + 2, z + 8), 16);
            if (qq == 0 && zz >= 4) {
                EntityUtils.makeChampion(eg, true);
            }

            world.addFreshEntity(eg);
        }

    }
}
