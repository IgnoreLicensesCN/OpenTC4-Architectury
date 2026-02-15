package thaumcraft.common.tiles.eldritch;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityTaintacle;
import thaumcraft.common.entities.monster.boss.EntityCultistPortal;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;
import thaumcraft.common.entities.monster.boss.EntityTaintacleGiant;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.dim.*;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class AncientLockInsertedBlockEntity extends BlockEntity {
    public int tickCount = 0;

    public AncientLockInsertedBlockEntity(BlockEntityType<? extends AncientLockInsertedBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public AncientLockInsertedBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ThaumcraftBlockEntities.ANCIENT_LOCK_INSERTED, blockPos, blockState);
    }
    
    public void tick() {
        tickCount += 1;
        if (this.tickCount % 5 == 0) {
            this.level.playSound(null,getBlockPos(), ThaumcraftSounds.PUMP, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        if (tickCount >= 100 && Platform.getEnvironment() != Env.CLIENT){
            doBossSpawn();
        }
    }
    
    //TODO:below
    private void doBossSpawn() {
        this.level.playSound(null,getBlockPos(), ThaumcraftSounds.ICE, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (Platform.getEnvironment() != Env.CLIENT) {
            final var pos = this.getBlockPos();
            final var posX = pos.getX();
            final var posY = pos.getY();
            final var posZ = pos.getZ();
            int cx = posX >> 4;
            int cz = posZ >> 4;
            int centerx = posX >> 4;
            int centerz = posZ >> 4;
            int exit = 0;

            for(int a = -2; a <= 2; ++a) {
                for(int b = -2; b <= 2; ++b) {
                    Cell c = MazeHandler.getFromHashMap(new CellLoc(cx + a, cz + b));
                    if (c != null && c.feature == 2) {
                        centerx = cx + a;
                        centerz = cz + b;
                    }

                    if (c != null && c.feature >= 2 && c.feature <= 5 && (c.north || c.south || c.east || c.west)) {
                        exit = c.feature;
                    }
                }
            }

            MapBossData mbd = (MapBossData)this.level.loadItemData(MapBossData.class, "BossMapData");
            if (mbd == null) {
                mbd = new MapBossData("BossMapData");
                mbd.bossCount = 0;
                mbd.markDirty();
                this.level.setItemData("BossMapData", mbd);
            }

            ++mbd.bossCount;
            if (this.level.random.nextFloat() < 0.25F) {
                ++mbd.bossCount;
            }

            mbd.markDirty();
            //TODO:API (maybe i have mental illness for api)
            switch (mbd.bossCount % 4) {
                case 0:
                    this.spawnGolemBossRoom(centerx, centerz, exit);
                    break;
                case 1:
                    this.spawnWardenBossRoom(centerx, centerz, exit);
                    break;
                case 2:
                    this.spawnCultistBossRoom(centerx, centerz, exit);
                    break;
                case 3:
                    this.spawnTaintBossRoom(centerx, centerz, exit);
            }

            for(int a = -2; a <= 2; ++a) {
                for(int b = -2; b <= 2; ++b) {
                    for(int c = -2; c <= 2; ++c) {
                        if (this.level.getBlock(posX + a, posY + b, posZ + c) == ConfigBlocks.blockAiry) {
                            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkleS2C(posX + a, posY + b, posZ + c, 4194368), new NetworkRegistry.TargetPoint(this.level.dimension(), posX + a, posY + b, posZ + c, 32.0F));
                            this.level.setBlockToAir(posX + a, posY + b, posZ + c);
                        }
                    }
                }
            }

            this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }

    }

    private void spawnWardenBossRoom(int cx, int cz, int exit) {
        final var pos = this.getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        for(int i = 0; i < this.level.playerEntities.size(); ++i) {
            Player ep = (Player)this.level.playerEntities.get(i);
            if (ep.getDistanceSq(posX, posY, posZ) < (double)300.0F) {
                ep.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tc.boss.warden")));
            }
        }

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

        GenCommon.genObelisk(this.level, x2, y + 4, z);
        GenCommon.genObelisk(this.level, x, y + 4, z2);
        this.level.setBlock(x2, y + 2, z, ConfigBlocks.blockEldritch, 3, 3);
        this.level.setBlock(x, y + 2, z2, ConfigBlocks.blockEldritch, 3, 3);

        for(int a = -1; a <= 1; ++a) {
            for(int b = -1; b <= 1; ++b) {
                if (a != 0 && b != 0 && this.level.random.nextFloat() < 0.9F) {
                    float rr = this.level.random.nextFloat();
                    int md = rr < 0.1F ? 2 : (rr < 0.3F ? 1 : 0);
                    this.level.setBlock(x2 + a, y + 2, z + b, ConfigBlocks.blockLootUrn, md, 3);
                }

                if (a != 0 && b != 0 && this.level.random.nextFloat() < 0.9F) {
                    float rr = this.level.random.nextFloat();
                    int md = rr < 0.1F ? 2 : (rr < 0.3F ? 1 : 0);
                    this.level.setBlock(x + a, y + 2, z2 + b, ConfigBlocks.blockLootUrn, md, 3);
                }
            }
        }

        this.level.setBlock(x - 2, y + 3, z - 2, ConfigBlocks.blockEldritch, 10, 3);
        this.level.setBlock(x - 2, y + 3, z + 2, ConfigBlocks.blockEldritch, 10, 3);
        this.level.setBlock(x + 2, y + 3, z + 2, ConfigBlocks.blockEldritch, 10, 3);
        this.level.setBlock(x + 2, y + 3, z - 2, ConfigBlocks.blockEldritch, 10, 3);
        this.level.setBlock(x - 2, y + 2, z - 2, ConfigBlocks.blockCosmeticSolid, 15, 3);
        this.level.setBlock(x - 2, y + 2, z + 2, ConfigBlocks.blockCosmeticSolid, 15, 3);
        this.level.setBlock(x + 2, y + 2, z + 2, ConfigBlocks.blockCosmeticSolid, 15, 3);
        this.level.setBlock(x + 2, y + 2, z - 2, ConfigBlocks.blockCosmeticSolid, 15, 3);

        for(int a = 0; a < 3; ++a) {
            for(int b = 0; b < 3; ++b) {
                if (this.ped[a][b] < 0) {
                    this.level.setBlock(x2 - 1 + b, y + 2, z2 - 1 + a, ConfigBlocks.blockEldritch, 4, 3);
                } else {
                    this.level.setBlock(x2 - 1 + b, y + 2, z2 - 1 + a, ConfigBlocks.blockStairsEldritch, this.ped[a][b], 3);
                }
            }
        }

        EntityEldritchWarden boss = new EntityEldritchWarden(this.level);
        double d0 = (double)posX - ((double)x2 + (double)0.5F);
        double d1 = (float)posY - ((float)(y + 3) + boss.getEyeHeight());
        double d2 = (double)posZ - ((double)z2 + (double)0.5F);
        double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f = (float)(Math.atan2(d2, d0) * (double)180.0F / Math.PI) - 90.0F;
        float f1 = (float)(-(Math.atan2(d1, d3) * (double)180.0F / Math.PI));
        boss.setLocationAndAngles((double)x2 + (double)0.5F, y + 3, (double)z2 + (double)0.5F, f, f1);
        boss.onSpawnWithEgg(null);
        boss.setHomeArea(x, y + 2, z, 32);
        this.level.spawnEntityInWorld(boss);
    }

    private void spawnGolemBossRoom(int cx, int cz, int exit) {
        final var pos = this.getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        for(int i = 0; i < this.level.playerEntities.size(); ++i) {
            Player ep = (Player)this.level.playerEntities.get(i);
            if (ep.getDistanceSq(posX, posY, posZ) < (double)300.0F) {
                ep.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tc.boss.golem")));
            }
        }

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

        GenCommon.genObelisk(this.level, x + x2, y + 4, z + z2);
        GenCommon.genObelisk(this.level, x - x2, y + 4, z + z2);
        GenCommon.genObelisk(this.level, x + x2, y + 4, z - z2);
        this.level.setBlock(x + x2, y + 2, z + z2, ConfigBlocks.blockEldritch, 3, 3);
        this.level.setBlock(x - x2, y + 2, z + z2, ConfigBlocks.blockEldritch, 3, 3);
        this.level.setBlock(x + x2, y + 2, z - z2, ConfigBlocks.blockEldritch, 3, 3);

        for(int a = 0; a < 3; ++a) {
            for(int b = 0; b < 3; ++b) {
                if (this.ped[a][b] < 0) {
                    this.level.setBlock(x - 1 + b, y + 2, z - 1 + a, ConfigBlocks.blockEldritch, 4, 3);
                } else {
                    this.level.setBlock(x - 1 + b, y + 2, z - 1 + a, ConfigBlocks.blockStairsEldritch, this.ped[a][b], 3);
                }
            }
        }

        for(int a = -10; a <= 10; ++a) {
            for(int b = -10; b <= 10; ++b) {
                if ((a < -2 && b < -2 || a > 2 && b > 2 || a < -2 && b > 2 || a > 2 && b < -2) && this.level.random.nextFloat() < 0.15F && this.level.isAirBlock(x + a, y + 2, z + b)) {
                    float rr = this.level.random.nextFloat();
                    int md = rr < 0.05F ? 2 : (rr < 0.2F ? 1 : 0);
                    this.level.setBlock(x + a, y + 2, z + b, this.level.random.nextFloat() < 0.3F ? ConfigBlocks.blockLootCrate : ConfigBlocks.blockLootUrn, md, 3);
                }
            }
        }

        EntityEldritchGolem boss = new EntityEldritchGolem(this.level);
        double d0 = (double)posX - ((double)x + (double)0.5F);
        double d1 = (float)posY - ((float)(y + 3) + boss.getEyeHeight());
        double d2 = (double)posZ - ((double)z + (double)0.5F);
        double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f = (float)(Math.atan2(d2, d0) * (double)180.0F / Math.PI) - 90.0F;
        float f1 = (float)(-(Math.atan2(d1, d3) * (double)180.0F / Math.PI));
        boss.setLocationAndAngles((double)x + (double)0.5F, y + 3, (double)z + (double)0.5F, f, f1);
        boss.onSpawnWithEgg(null);
        this.level.spawnEntityInWorld(boss);
    }

    private void spawnCultistBossRoom(int cx, int cz, int exit) {
        final var pos = this.getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        for(int i = 0; i < this.level.playerEntities.size(); ++i) {
            Player ep = (Player)this.level.playerEntities.get(i);
            if (ep.getDistanceSq(posX, posY, posZ) < (double)300.0F) {
                ep.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tc.boss.crimson")));
            }
        }

        int x = cx * 16 + 16;
        int y = 50;
        int z = cz * 16 + 16;

        for(int a = -4; a <= 4; ++a) {
            for(int b = -4; b <= 4; ++b) {
                if ((Math.abs(a) != 2 && Math.abs(b) != 2 || !this.level.random.nextBoolean()) && (Math.abs(a) != 3 && Math.abs(b) != 3 || !(this.level.random.nextFloat() > 0.33F)) && (Math.abs(a) != 4 && Math.abs(b) != 4 || !(this.level.random.nextFloat() > 0.25F))) {
                    this.level.setBlock(x + b, y + 1, z + a, ConfigBlocks.blockEldritch, 7, 3);
                }
            }
        }

        for(int a = 0; a < 5; ++a) {
            for(int b = 0; b < 5; ++b) {
                if (a == 0 || a == 4 || b == 0 || b == 4) {
                    this.level.setBlock(x - 8 + b * 4, y + 2, z - 8 + a * 4, ConfigBlocks.blockCosmeticSolid, 11, 3);
                    this.level.setBlock(x - 8 + b * 4, y + 3, z - 8 + a * 4, ConfigBlocks.blockEldritch, 5, 3);
                    this.level.setBlock(x - 8 + b * 4, y + 4, z - 8 + a * 4, ConfigBlocks.blockSlabStone, 1, 3);
                    this.level.setBlock(x - 8 + b * 4, y + 10, z - 8 + a * 4, ConfigBlocks.blockCosmeticSolid, 11, 3);
                    this.level.setBlock(x - 8 + b * 4, y + 9, z - 8 + a * 4, ConfigBlocks.blockEldritch, 5, 3);
                    this.level.setBlock(x - 8 + b * 4, y + 8, z - 8 + a * 4, ConfigBlocks.blockSlabStone, 9, 3);
                }
            }
        }

        EntityCultistPortal boss = new EntityCultistPortal(this.level);
        boss.setLocationAndAngles((double)x + (double)0.5F, y + 2, (double)z + (double)0.5F, 0.0F, 0.0F);
        this.level.spawnEntityInWorld(boss);
    }

    private void spawnTaintBossRoom(int cx, int cz, int exit) {
        final var pos = this.getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        for(int i = 0; i < this.level.playerEntities.size(); ++i) {
            Player ep = (Player)this.level.playerEntities.get(i);
            if (ep.getDistanceSq(posX, posY, posZ) < (double)300.0F) {
                ep.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tc.boss.taint")));
            }
        }

        int x = cx * 16 + 16;
        int y = 50;
        int z = cz * 16 + 16;

        for(int a = -12; a <= 12; ++a) {
            for(int b = -12; b <= 12; ++b) {
                Utils.setBiomeAt(this.level, x + b, z + a, ThaumcraftWorldGenerator.biomeTaint);

                for(int c = 0; c < 9; ++c) {
                    if (this.level.isAirBlock(x + b, y + 2 + c, z + a) && BlockUtils.isAdjacentToSolidBlock(this.level, x + b, y + 2 + c, z + a) && this.level.random.nextInt(3) != 0) {
                        this.level.setBlock(x + b, y + 2 + c, z + a, ConfigBlocks.blockTaintFibres, this.level.random.nextInt(4) == 0 ? 1 : 0, 3);
                    }
                }

                if ((double)this.level.random.nextFloat() < 0.15) {
                    this.level.setBlock(x + b, y + 2, z + a, ConfigBlocks.blockTaint, 0, 3);
                    if ((double)this.level.random.nextFloat() < 0.2) {
                        this.level.setBlock(x + b, y + 3, z + a, ConfigBlocks.blockTaint, 0, 3);
                    }
                }

                if ((Math.abs(a) != 4 && Math.abs(b) != 4 || !this.level.random.nextBoolean()) && (Math.abs(a) < 5 && Math.abs(b) < 5 || !(this.level.random.nextFloat() > 0.33F)) && (Math.abs(a) < 7 && Math.abs(b) < 7 || !(this.level.random.nextFloat() > 0.25F))) {
                    this.level.setBlock(x + b, y + 1, z + a, ConfigBlocks.blockTaint, 1, 3);
                }
            }
        }

        EntityTaintacle boss1 = this.level.difficultySetting != Difficulty.HARD ? new EntityTaintacle(this.level) : new EntityTaintacleGiant(this.level);
        boss1.setLocationAndAngles((double)x + (double)0.5F, y + 3, (double)z + (double)0.5F, 0.0F, 0.0F);
        EntityUtils.makeChampion(boss1, true);
        this.level.spawnEntityInWorld(boss1);
        EntityTaintacle boss2 = this.level.random.nextBoolean() ? new EntityTaintacle(this.level) : new EntityTaintacleGiant(this.level);
        boss2.setLocationAndAngles((double)x + (double)3.5F, y + 3, (double)z + (double)3.5F, 0.0F, 0.0F);
        EntityUtils.makeChampion(boss2, true);
        this.level.spawnEntityInWorld(boss2);
        EntityTaintacle boss3 = boss2 instanceof EntityTaintacleGiant ? new EntityTaintacle(this.level) : new EntityTaintacleGiant(this.level);
        boss3.setLocationAndAngles((double)x - (double)2.5F, y + 3, (double)z + (double)3.5F, 0.0F, 0.0F);
        EntityUtils.makeChampion(boss3, true);
        this.level.spawnEntityInWorld(boss3);
        EntityTaintacle boss4 = this.level.random.nextBoolean() ? new EntityTaintacle(this.level) : new EntityTaintacleGiant(this.level);
        boss4.setLocationAndAngles((double)x + (double)3.5F, y + 3, (double)z - (double)2.5F, 0.0F, 0.0F);
        EntityUtils.makeChampion(boss4, true);
        this.level.spawnEntityInWorld(boss4);
        EntityTaintacle boss5 = boss4 instanceof EntityTaintacleGiant ? new EntityTaintacle(this.level) : new EntityTaintacleGiant(this.level);
        boss5.setLocationAndAngles((double)x - (double)2.5F, y + 3, (double)z - (double)2.5F, 0.0F, 0.0F);
        EntityUtils.makeChampion(boss5, true);
        this.level.spawnEntityInWorld(boss5);
    }
}
