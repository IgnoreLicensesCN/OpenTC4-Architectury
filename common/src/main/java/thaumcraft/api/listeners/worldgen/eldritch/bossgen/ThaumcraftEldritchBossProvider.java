package thaumcraft.api.listeners.worldgen.eldritch.bossgen;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.monster.boss.CultistPortalEntity;
import thaumcraft.common.entities.monster.boss.EldritchGolemEntity;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;
import thaumcraft.common.entities.monster.boss.EntityTaintacleGiant;
import thaumcraft.common.entities.monster.tainted.TaintacleEntity;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeLookups;
import thaumcraft.common.lib.world.dim.GenCommon;

import static net.minecraft.world.level.block.SlabBlock.TYPE;
import static thaumcraft.api.listeners.worldgen.eldritch.bossgen.ThaumcraftEldritchBossProvider.Impl.Util.genStairPedestal;
import static thaumcraft.api.listeners.worldgen.eldritch.bossgen.ThaumcraftEldritchBossProvider.Impl.Util.notifyNearPlayers;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.*;
import static thaumcraft.common.blocks.crafted.loot.DefaultAbstractLootBlock.DEFAULT_RARITY_PROPERTY;
import static thaumcraft.common.lib.world.biomes.BiomeUtils.setPosTaint;

public class ThaumcraftEldritchBossProvider {
    public static void init() {
        BOSS_ROOM_GENERATOR.add(Impl::spawnGolemBossRoom,1);
        BOSS_ROOM_GENERATOR.add(Impl::spawnCultistBossRoom,1);
        BOSS_ROOM_GENERATOR.add(Impl::spawnWardenBossRoom,1);
        BOSS_ROOM_GENERATOR.add(Impl::spawnTaintBossRoom,1);
    }
    public static final WeightedRandomCollection<IEldritchBossRoomGenerator> BOSS_ROOM_GENERATOR = new WeightedRandomCollection<>();
    public static void generateEldritchBossRoom(Level level, BlockPos pos,int centerX, int centerZ, int exit) {
        var random = level.random;//maybe should be changed
        BOSS_ROOM_GENERATOR.getRandom(random).generateEldritchBossRoom(level, pos, centerX, centerZ, exit);
    }
    public interface IEldritchBossRoomGenerator {
        void generateEldritchBossRoom(Level level, BlockPos pos,int centerX, int centerZ, int exit);
    }
    
    //TODO:[maybe wont finished]reduce "new BlockPos" here
    public static class Impl {
        public static class Util {
            public static final BlockState STAIR_DEFAULT_STATE = ANCIENT_STONE_STAIRS().defaultBlockState();
            public static final BlockState[][] ped =
                    new BlockState[][]{
                            {
                                    STAIR_DEFAULT_STATE.setValue(StairBlock.FACING, Direction.SOUTH).setValue(StairBlock.HALF, Half.BOTTOM),
                                    STAIR_DEFAULT_STATE.setValue(StairBlock.FACING, Direction.SOUTH).setValue(StairBlock.HALF, Half.BOTTOM),
                                    STAIR_DEFAULT_STATE.setValue(StairBlock.FACING, Direction.SOUTH).setValue(StairBlock.HALF, Half.BOTTOM)
                            },
                            {
                                    STAIR_DEFAULT_STATE.setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.BOTTOM),
                                    null,
                                    null,
                            },
                            {
                                    STAIR_DEFAULT_STATE.setValue(StairBlock.FACING, Direction.NORTH).setValue(StairBlock.HALF, Half.BOTTOM),
                                    STAIR_DEFAULT_STATE.setValue(StairBlock.FACING, Direction.NORTH).setValue(StairBlock.HALF, Half.BOTTOM),
                                    STAIR_DEFAULT_STATE.setValue(StairBlock.FACING, Direction.NORTH).setValue(StairBlock.HALF, Half.BOTTOM)
                            },
                    };//new int[][]{{2, 2, 2}, {0, -1, 1}, {3, 3, 3}};//wtf


            public static void genStairPedestal(Level level, int x, int y, int z) {
                for(int a = 0; a < 3; ++a) {
                    for(int b = 0; b < 3; ++b) {
                        if (ped[a][b] == null) {
                            level.setBlockAndUpdate(new BlockPos(x - 1 + b, y + 2, z - 1 + a), GLOWING_CRUSTED_STONE().defaultBlockState());
                        } else {
                            level.setBlockAndUpdate(new BlockPos(x - 1 + b, y + 2, z - 1 + a), ped[a][b]);
                        }
                    }
                }
            }

            public static void notifyNearPlayers(Level level,BlockPos center,Component toNotify) {
                if (level == null) {
                    return;
                }
                var pos = center;
                for(int i = 0; i < level.players().size(); ++i) {
                    Player ep = level.players().get(i);
                    if (ep.distanceToSqr(pos.getX(),pos.getY(),pos.getZ()) < (double)300.0F) {
                        ep.sendSystemMessage(toNotify);
                    }
                }
            }
        }
        
        public static void spawnGolemBossRoom(Level level,BlockPos lockPos,int cx, int cz, int exit) {
            final var posX = lockPos.getX();
            final var posY = lockPos.getY();
            final var posZ = lockPos.getZ();
            notifyNearPlayers(level,lockPos, Component.translatable("tc.boss.golem"));

            int x = cx * 16 + 16;
            int y = 50;
            int z = cz * 16 + 16;
            int x2 = 0;
            int z2 = 0;
            switch (exit) {
                case 2:
                    x2 = 8;
                    z2 = 8;
                    break;
                case 3:
                    x2 = -8;
                    z2 = 8;
                    break;
                case 4:
                    x2 = 8;
                    z2 = -8;
                    break;
                case 5:
                    x2 = -8;
                    z2 = -8;
            }

            GenCommon.genObelisk(level, x + x2, y + 4, z + z2);
            GenCommon.genObelisk(level, x - x2, y + 4, z + z2);
            GenCommon.genObelisk(level, x + x2, y + 4, z - z2);
            level.setBlockAndUpdate(new BlockPos(x + x2, y + 2, z + z2), ELDRITCH_CAPSTONE().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x - x2, y + 2, z + z2), ELDRITCH_CAPSTONE().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x + x2, y + 2, z - z2), ELDRITCH_CAPSTONE().defaultBlockState());

            genStairPedestal(level, x, y, z);

            for(int a = -10; a <= 10; ++a) {
                for(int b = -10; b <= 10; ++b) {
                    if ((a < -2 && b < -2 || a > 2 && b > 2 || a < -2 && b > 2 || a > 2 && b < -2) && level.random.nextFloat() < 0.15F 
                            && level.getBlockState(new BlockPos(x + a, y + 2, z + b)).isAir()) {
                        float rr = level.random.nextFloat();
                        int md = rr < 0.05F ? 2 : (rr < 0.2F ? 1 : 0);
                        level.setBlock(new BlockPos(x + a, y + 2, z + b),
                                (level.random.nextFloat() < 0.3F ? CRATE_LOOT().defaultBlockState() : URN_LOOT().defaultBlockState()).setValue(DEFAULT_RARITY_PROPERTY,md), md);
                    }
                }
            }

            var boss = new EldritchGolemEntity(level);
            double d0 = (double)posX - ((double)x + (double)0.5F);
            double d1 = (float)posY - ((float)(y + 3) + boss.getEyeHeight());
            double d2 = (double)posZ - ((double)z + (double)0.5F);
            double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float f = (float)(Math.atan2(d2, d0) * (double)180.0F / Math.PI) - 90.0F;
            float f1 = (float)(-(Math.atan2(d1, d3) * (double)180.0F / Math.PI));
            boss.setPos(x + 0.5F, y + 3, z + 0.5F);
            boss.setXRot(f);
            boss.setYRot(f1);
            level.addFreshEntity(boss);
        }

        public static void spawnCultistBossRoom(Level level,BlockPos lockPos,int cx, int cz, int exit) {
            if (level == null){
                return;
            }
            notifyNearPlayers(level,lockPos,Component.translatable("tc.boss.crimson"));

            int x = cx * 16 + 16;
            int y = 50;
            int z = cz * 16 + 16;

            for(int a = -4; a <= 4; ++a) {
                for(int b = -4; b <= 4; ++b) {
                    if ((Math.abs(a) != 2 && Math.abs(b) != 2 || !level.random.nextBoolean()) && (Math.abs(a) != 3 && Math.abs(b) != 3 || !(level.random.nextFloat() > 0.33F)) && (Math.abs(a) != 4 && Math.abs(b) != 4 || !(level.random.nextFloat() > 0.25F))) {
                        level.setBlockAndUpdate(new BlockPos(x + b, y + 1, z + a), ANCIENT_GATEWAY().defaultBlockState());
                    }
                }
            }

            for(int a = 0; a < 5; ++a) {
                for(int b = 0; b < 5; ++b) {
                    if (a == 0 || a == 4 || b == 0 || b == 4) {
                        level.setBlock(new BlockPos(x - 8 + b * 4, y + 2, z - 8 + a * 4), ANCIENT_STONE().defaultBlockState(), 3);
                        level.setBlock(new BlockPos(x - 8 + b * 4, y + 3, z - 8 + a * 4), GLYPHED_STONE().defaultBlockState(), 3);
                        level.setBlock(new BlockPos(x - 8 + b * 4, y + 4, z - 8 + a * 4), ANCIENT_STONE_SLAB().defaultBlockState().setValue(TYPE, SlabType.BOTTOM), 1, 3);
                        level.setBlock(new BlockPos(x - 8 + b * 4, y + 10, z - 8 + a * 4), ANCIENT_STONE().defaultBlockState(), 11, 3);
                        level.setBlock(new BlockPos(x - 8 + b * 4, y + 9, z - 8 + a * 4), GLYPHED_STONE().defaultBlockState(), 3);
                        level.setBlock(new BlockPos(x - 8 + b * 4, y + 8, z - 8 + a * 4), ANCIENT_STONE_SLAB().defaultBlockState().setValue(TYPE, SlabType.TOP), 3);
                    }
                }
            }

            var boss = new CultistPortalEntity(level);
            boss.setPos(x + 0.5F, y + 2, z + 0.5F);
            level.addFreshEntity(boss);
        }

        public static void spawnWardenBossRoom(Level level,BlockPos lockPos,int cx, int cz, int exit) {
            if (level == null) {
                return;
            }
            final var pos = lockPos;
            final var posX = pos.getX();
            final var posY = pos.getY();
            final var posZ = pos.getZ();
            notifyNearPlayers(level,lockPos,Component.translatable("tc.boss.warden"));

            int x = cx * 16 + 16;
            int y = 50;
            int z = cz * 16 + 16;
            int x2 = x;
            int z2 = z;
            switch (exit) {
                case 2:
                    x2 = x + 8;
                    z2 = z + 8;
                    break;
                case 3:
                    x2 = x - 8;
                    z2 = z + 8;
                    break;
                case 4:
                    x2 = x + 8;
                    z2 = z - 8;
                    break;
                case 5:
                    x2 = x - 8;
                    z2 = z - 8;
            }

            GenCommon.genObelisk(level, x2, y + 4, z);
            GenCommon.genObelisk(level, x, y + 4, z2);
            level.setBlockAndUpdate(new BlockPos(x2, y + 2, z), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CAPSTONE().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x, y + 2, z2), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CAPSTONE().defaultBlockState());

            for(int a = -1; a <= 1; ++a) {
                for(int b = -1; b <= 1; ++b) {
                    if (a != 0 && b != 0 && level.random.nextFloat() < 0.9F) {
                        float rr = level.random.nextFloat();
                        int md = rr < 0.1F ? 2 : (rr < 0.3F ? 1 : 0);
                        level.setBlockAndUpdate(new BlockPos(x2 + a, y + 2, z + b), ThaumcraftBlocks.ThaumcraftBlockInstances.URN_LOOT().defaultBlockState().setValue(DEFAULT_RARITY_PROPERTY,md));
                    }

                    if (a != 0 && b != 0 && level.random.nextFloat() < 0.9F) {
                        float rr = level.random.nextFloat();
                        int md = rr < 0.1F ? 2 : (rr < 0.3F ? 1 : 0);
                        level.setBlockAndUpdate(new BlockPos(x + a, y + 2, z2 + b), ThaumcraftBlocks.ThaumcraftBlockInstances.URN_LOOT().defaultBlockState().setValue(DEFAULT_RARITY_PROPERTY,md));
                    }
                }
            }

            level.setBlockAndUpdate(new BlockPos(x - 2, y + 3, z - 2), ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x - 2, y + 3, z + 2), ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x + 2, y + 3, z - 2), ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x + 2, y + 3, z + 2), ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x - 2, y + 2, z - 2), ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_PEDESTAL().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x - 2, y + 2, z + 2), ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_PEDESTAL().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x + 2, y + 2, z - 2), ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_PEDESTAL().defaultBlockState());
            level.setBlockAndUpdate(new BlockPos(x + 2, y + 2, z + 2), ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_PEDESTAL().defaultBlockState());


            genStairPedestal(level,x2, y, z2);

            var boss = new EntityEldritchWarden(level);
            double d0 = (double)posX - ((double)x2 + (double)0.5F);
            double d1 = (float)posY - ((float)(y + 3) + boss.getEyeHeight());
            double d2 = (double)posZ - ((double)z2 + (double)0.5F);
            double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float f = (float)(Math.atan2(d2, d0) * (double)180.0F / Math.PI) - 90.0F;
            float f1 = (float)(-(Math.atan2(d1, d3) * (double)180.0F / Math.PI));
            boss.setPos(x2 + 0.5F, y + 3, z2 + 0.5F);
            boss.setXRot(f);
            boss.setYRot(f1);
            boss.restrictTo(new BlockPos(x, y + 2, z), 32);
            level.addFreshEntity(boss);
        }


        public static void spawnTaintBossRoom(Level level,BlockPos lockPos,int cx, int cz, int exit) {
            notifyNearPlayers(level,lockPos,Component.translatable("tc.boss.taint"));

            int x = cx * 16 + 16;
            int y = 50;
            int z = cz * 16 + 16;
            if (!(level instanceof ServerLevel serverLevel)){return;}
            var holderTaint = ThaumcraftBiomeLookups.biomeHolderForLevel(serverLevel, ThaumcraftBiomeIDs.TAINT_KEY);
            for(int a = -12; a <= 12; ++a) {
                for(int b = -12; b <= 12; ++b) {
                    var pickPos = new BlockPos(x+b,y,z+a);
                    setPosTaint(serverLevel, pickPos,holderTaint);


                    var fibreLocation = pickPos.below();
                    if (!serverLevel.getBlockState(fibreLocation).isAir()){
                        serverLevel.setBlockAndUpdate(fibreLocation,FIBROUS_TAINT().defaultBlockState());
                    }

                    var pickTaintSourcePos = pickPos.mutable();
                    int baseY = pickPos.getY() + 2;
                    for(int c = 0; c < 9; ++c) {
                        pickTaintSourcePos = pickTaintSourcePos.setY(baseY + c);
                        if (level.getBlockState(pickPos).isAir() && BlockUtils.isAdjacentToSolidBlock(level, pickPos) && level.random.nextInt(3) != 0) {
                            if (level.random.nextInt(4) == 0){
                                serverLevel.setBlockAndUpdate(fibreLocation,FIBROUS_TAINT().defaultBlockState());
                            }else {
                                serverLevel.setBlockAndUpdate(fibreLocation,TAINTED_GRASS().defaultBlockState());
                            }
                        }
                    }
                    pickTaintSourcePos = pickTaintSourcePos.setY(baseY);

                    if (level.random.nextFloat() < 0.15F) {
                        level.setBlockAndUpdate(pickTaintSourcePos, CRUSTED_TAINT().defaultBlockState());
                        if (level.random.nextFloat() < 0.2F) {
                            level.setBlockAndUpdate(pickTaintSourcePos, CRUSTED_TAINT().defaultBlockState());
                        }
                    }

                    pickTaintSourcePos = pickTaintSourcePos.setY(baseY-1);
                    if ((Math.abs(a) != 4 && Math.abs(b) != 4 || !level.random.nextBoolean()) && (Math.abs(a) < 5 && Math.abs(b) < 5 || !(level.random.nextFloat() > 0.33F)) && (Math.abs(a) < 7 && Math.abs(b) < 7 || !(level.random.nextFloat() > 0.25F))) {
                        level.setBlockAndUpdate(pickTaintSourcePos, TAINTED_SOIL().defaultBlockState());
                    }
                }
            }

            var boss1 = level.getDifficulty() != Difficulty.HARD ? new TaintacleEntity(level) : new EntityTaintacleGiant(level);
            boss1.setPos(x + 0.5F, y + 3, z + 0.5F);
            EntityUtils.makeChampion(boss1, true);
            level.addFreshEntity(boss1);
            var boss2 = level.random.nextBoolean() ? new TaintacleEntity(level) : new EntityTaintacleGiant(level);
            boss2.setPos(x + 3.5F, y + 3, z + 3.5F);
            EntityUtils.makeChampion(boss2, true);
            level.addFreshEntity(boss2);
            var boss3 = boss2 instanceof EntityTaintacleGiant ? new TaintacleEntity(level) : new EntityTaintacleGiant(level);
            boss3.setPos(x - 2.5F, y + 3, z + 3.5F);
            EntityUtils.makeChampion(boss3, true);
            level.addFreshEntity(boss3);
            var boss4 = level.random.nextBoolean() ? new TaintacleEntity(level) : new EntityTaintacleGiant(level);
            boss4.setPos(x + 3.5F, y + 3, z - 2.5F);
            EntityUtils.makeChampion(boss4, true);
            level.addFreshEntity(boss4);
            var boss5 = boss4 instanceof EntityTaintacleGiant ? new TaintacleEntity(level) : new EntityTaintacleGiant(level);
            boss5.setPos(x - 2.5F, y + 3, z - 2.5F);
            EntityUtils.makeChampion(boss5, true);
            level.addFreshEntity(boss5);
        }
    }
}
