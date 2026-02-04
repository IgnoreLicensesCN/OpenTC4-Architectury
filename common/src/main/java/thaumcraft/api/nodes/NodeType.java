package thaumcraft.api.nodes;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;

import java.util.*;

public class NodeType {
    private static final Map<String, NodeType> BY_NAME = new LinkedHashMap<>();
    private static final List<NodeType> VALUES = new ArrayList<>();

    public static final NodeType NORMAL = new NodeType("thaumcraft:normal",1.f){

        @Override
        public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {
            var result = super.nodeTypeTick(thisNode);
            
            
            
            return result;
        }
    };
    public static final NodeType UNSTABLE = new NodeType("thaumcraft:unstable",1.f){
        @Override
        public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {
            var level = thisNode.getLevel();
            var pos = thisNode.getBlockPos();

            if (!(level instanceof ServerLevel serverLevel)) return false;
            var result = super.nodeTypeTick(thisNode);
            
            
            if (level.random.nextBoolean()) {
                var nodeLock = INodeLock.getNodeLock(thisNode.getLockId());
                if (nodeLock == null) {
                    Aspect aspect = null;
                    if ((aspect = thisNode.takeRandomPrimalFromSource()) != null) {
                        EntityAspectOrb orb = new EntityAspectOrb(
                                level, (double) pos.getX() + (double) 0.5F,
                                (double) pos.getY() + (double) 0.5F,
                                (double) pos.getZ() + (double) 0.5F, aspect, 1
                        );
                        level.addFreshEntity(orb);

                        result = true;
                    }
                }
            }
            return result;
        }
    };
    public static final NodeType DARK = new NodeType("thaumcraft:dark",1.f){
        @Override
        public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {
            var result = super.nodeTypeTick(thisNode);

            if (!(thisNode.getLevel() instanceof ServerLevel serverLevel)) return false;
            var pos = thisNode.getBlockPos();
//            int dimbl = ThaumcraftWorldGenerator.getDimBlacklist(serverLevel.dimension());
//            int biobl = ThaumcraftWorldGenerator.getBiomeBlacklist(
//                    serverLevel.getBiomeGenForCoords(pos.getX(), pos.getZ()).biomeID);
            if (
//                    biobl != 0 
//                    && biobl != 2 
//                    && serverLevel.dimension() != -1 
//                    && serverLevel.dimension() != 1 
//                    && dimbl != 0 
//                    && dimbl != 2 
//                    && thisNode.getNodeType() == NodeType.DARK && 
                    thisNode.getTickCount() % 50 == 0
            ) {
                int x = pos.getX() + serverLevel.random.nextInt(12) - serverLevel.random.nextInt(12);
                int z = pos.getZ() + serverLevel.random.nextInt(12) - serverLevel.random.nextInt(12);
                BiomeGenBase bg = serverLevel.getBiomeGenForCoords(x, z);
                if (bg.biomeID != ThaumcraftWorldGenerator.biomeEerie.biomeID) {
                    Utils.setBiomeAt(serverLevel, x, z, ThaumcraftWorldGenerator.biomeEerie);
                }

                if (Config.hardNode && serverLevel.random.nextBoolean() && serverLevel.hasNearbyAlivePlayer(
                        (double) pos.getX() + (double) 0.5F,
                        (double) pos.getY() + (double) 0.5F,
                        (double) pos.getZ() + (double) 0.5F,
                        24.0F
                )) {
                    EntityGiantBrainyZombie entity = new EntityGiantBrainyZombie(serverLevel);
                    AABB box = new AABB(pos).inflate(10.0D, 6.0D, 10.0D);
                    int j = serverLevel.getEntitiesOfClass(EntityGiantBrainyZombie.class, box).size();
//                    int j = serverLevel.getEntitiesWithinAABB(
//                                    entity.getClass(),
//                                    AxisAlignedBB.getBoundingBox(
//                                                    pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1,
//                                                    pos.getY() + 1, pos.getZ() + 1
//                                            )
//                                            .expand(10.0F, 6.0F, 10.0F)
//                            )
//                            .size();
                    if (j <= 3) {
                        double d0 = (double) pos.getX() + (serverLevel.random.nextDouble() - serverLevel.random.nextDouble()) * (double) 5.0F;
                        double d3 = pos.getY() + serverLevel.random.nextInt(3) - 1;
                        double d4 = (double) pos.getZ() + (serverLevel.random.nextDouble() - serverLevel.random.nextDouble()) * (double) 5.0F;
                        LivingEntity entityliving = entity;
                        entity.moveTo(d0, d3, d4, serverLevel.random.nextFloat() * 360.0F, 0.0F);
                        if (entity.checkSpawnRules(serverLevel, MobSpawnType.EVENT)
                                && entity.checkSpawnObstruction(serverLevel)) {
                            serverLevel.addFreshEntity(entityliving);
                            serverLevel.levelEvent(LevelEvent.PARTICLES_MOBBLOCK_SPAWN, pos, 0);
//                            serverLevel.playAuxSFX(2004, pos.getX(), pos.getY(), pos.getZ(), 0);
                            entity.spawnAnim();
                        }
                    }
                }
            }
            
            return result;
        }
    };
    public static final NodeType TAINTED = new NodeType("thaumcraft:tainted",1.f){
        @Override
        public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {
            var level = thisNode.getLevel();
            if (!(level instanceof ServerLevel serverLevel)) return false;
            var pos = thisNode.getBlockPos();
            if (thisNode.getTickCount() % 50 == 0){
                int x ;
                int z ;
                int y ;
                x = pos.getX() + serverLevel.random.nextInt(8) - serverLevel.random.nextInt(8);
                z = pos.getZ() + serverLevel.random.nextInt(8) - serverLevel.random.nextInt(8);
                y = pos.getY() + serverLevel.random.nextInt(8) - serverLevel.random.nextInt(8);
                BiomeGenBase bg = serverLevel.getBiomeGenForCoords(x, z);
                if (bg.biomeID != ThaumcraftWorldGenerator.biomeTaint.biomeID) {
                    Utils.setBiomeAt(serverLevel, x, y, z, ThaumcraftWorldGenerator.biomeTaint);
                }

                if (Config.hardNode && serverLevel.random.nextBoolean()) {
                    x = pos.getX() + serverLevel.random.nextInt(5) - serverLevel.random.nextInt(5);
                    z = pos.getZ() + serverLevel.random.nextInt(5) - serverLevel.random.nextInt(5);
                    y = pos.getY() + serverLevel.random.nextInt(5) - serverLevel.random.nextInt(5);
                    if (BlockTaintFibres.spreadFibres(serverLevel, x, y, z)) {
                    }
                }
            }
            
            return false;
        }
    };
    public static final NodeType HUNGRY = new NodeType("thaumcraft:hungry",1.5f){
        @Override
        public int getAttackAnotherNodePeriod(AbstractNodeBlockEntity thisNode) {
            return 1;
        }

        @Override
        public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {
            var result = super.nodeTypeTick(thisNode);

            var pos = thisNode.getBlockPos();
            var level = thisNode.getLevel();
            if (level instanceof ClientLevel clientLevel) {
                for (int a = 0; a < ClientFXUtils.particleCount(1); ++a) {
                    int tx = pos.getX() + clientLevel.random.nextInt(16) - clientLevel.random.nextInt(16);
                    int ty = pos.getY() + clientLevel.random.nextInt(16) - clientLevel.random.nextInt(16);
                    int tz = pos.getZ() + clientLevel.random.nextInt(16) - clientLevel.random.nextInt(16);
                    var heightAt = clientLevel.getHeight(Heightmap.Types.WORLD_SURFACE, tx, tz);
                    if (ty > heightAt) {
                        ty = heightAt;
                    }

                    Vec3 v1 = new Vec3(
                            (double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F,
                            (double) pos.getZ() + (double) 0.5F
                    );
                    Vec3 v2 = new Vec3(
                            (double) tx + (double) 0.5F, (double) ty + (double) 0.5F, (double) tz + (double) 0.5F);
                    HitResult mop = ThaumcraftApiHelper.rayTraceIgnoringSource(
                            clientLevel, v1, v2, true/*, false, false (always these two flags)*/
                    ,null);

                    if (mop != null && mop.getLocation()
                            .distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < (double) 256.0F) {
                        var rayTraceLoc = mop.getLocation();
                        tx = (int) rayTraceLoc.x;
                        ty = (int) rayTraceLoc.y;
                        tz = (int) rayTraceLoc.z;
                        var bi = clientLevel.getBlockState(new BlockPos(tx, ty, tz));
                        if (!bi.isAir()) {
                            ClientFXUtils.hungryNodeFX(
                                    clientLevel, tx, ty, tz, pos.getX(), pos.getY(), pos.getZ(), bi.getBlock());
                        }
                    }
                }
            }

            if (Config.hardNode) {
                AABB box = new AABB(
                        pos.getX(), pos.getY(), pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1
                ).inflate(15.0D);
                if (level instanceof ServerLevel serverLevel) {

                    List<Entity> ents = level.getEntities(null, box);

                    if (!ents.isEmpty()) {
                        for (Entity entity : ents) {
                            if (entity instanceof Player player && player.getAbilities().invulnerable) {
                                continue;
                            }

                            if (!entity.isAlive() || entity.isInvulnerable()) continue;

                            double distSq = entity.distanceToSqr(
                                    pos.getX() + 0.5,
                                    pos.getY() + 0.5,
                                    pos.getZ() + 0.5
                            );
                            if (distSq < (double) 2.0F) {
                                entity.hurt(
                                        level.damageSources()
                                                .fellOutOfWorld(), 1.0F
                                );
                                if (!entity.isAlive()) {
                                    ScanResult scan = new ScanResult((byte) 2, (Item) null, entity, "");
                                    AspectList al = ScanManager.getScanAspects(scan, serverLevel);
                                    if (al != null && al.size() > 0) {
                                        al = ResearchManager.reduceToPrimals(al.copy());
                                        if (al.size() > 0) {
                                            Aspect a = al.getAspects()
                                                    .keySet()
                                                    .toArray(new Aspect[0])[serverLevel.random.nextInt(al.size())];
                                            if (thisNode.getAspects()
                                                    .getAmount(a) < thisNode.getNodeVisBase(a)) {
                                                thisNode.addToContainer(a, 1);
                                                result = true;
                                            } else if (serverLevel.random.nextInt(
                                                    1 + thisNode.getNodeVisBase(a) * 2) < al.getAmount(a)) {
                                                thisNode.getAspectsBase().addAll(a, 1);
                                                result = true;
                                            }
                                        }
                                    }
                                }
                            }

                            double dx = (pos.getX() + 0.5 - entity.getX()) / 15.0;
                            double dy = (pos.getY() + 0.5 - entity.getY()) / 15.0;
                            double dz = (pos.getZ() + 0.5 - entity.getZ()) / 15.0;

                            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                            double strength = 1.0 - dist;

                            if (strength > 0.0) {
                                strength *= strength;

                                Vec3 motion = entity.getDeltaMovement();
                                entity.setDeltaMovement(
                                        motion.x + dx / dist * strength * 0.15,
                                        motion.y + dy / dist * strength * 0.25,
                                        motion.z + dz / dist * strength * 0.15
                                );
                                entity.hurtMarked = true; // Forge/vanilla 同步用
                            }
                        }
                    }
                }
            }
            if (thisNode.getTickCount() % 50 == 0 && level instanceof ServerLevel serverLevel) {
                int tx = pos.getX() + level.random.nextInt(16) - serverLevel.random.nextInt(16);
                int tz = pos.getZ() + serverLevel.random.nextInt(16) - serverLevel.random.nextInt(16);
                int ty = Math.min(serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE,tx, tz),pos.getY() + serverLevel.random.nextInt(16) - serverLevel.random.nextInt(16));


                Vec3 v1 = new Vec3(
                        (double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F,
                        (double) pos.getZ() + (double) 0.5F
                );
                Vec3 v2 = new Vec3(
                        (double) tx + (double) 0.5F, (double) ty + (double) 0.5F, (double) tz + (double) 0.5F);
                HitResult mop = ThaumcraftApiHelper.rayTraceIgnoringSource(serverLevel, v1, v2, true,null);
                if (mop != null) {
                    var mopPos = mop.getLocation();
                    if (mopPos.distanceToSqr(pos.getCenter()) <  256.0F * 256.0F){
                        tx = (int) mopPos.x;
                        ty = (int) mopPos.y;
                        tz = (int) mopPos.z;
                        var posToBreak = new BlockPos(tx, ty, tz);

                        BlockState bState = serverLevel.getBlockState(posToBreak);

                        if (!bState.isAir()) {
                            float strength = bState.getBlock().defaultDestroyTime();
                            if (strength >= 0.0F && strength < 5.0F) {
                                serverLevel.destroyBlock(posToBreak, true);
                            }
                        }
                    }
                }
            }
            
            return result;
        }
    };
    public static final NodeType PURE = new NodeType("thaumcraft:pure",1.f){
        @Override
        public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {

            if (!(thisNode.getLevel() instanceof ServerLevel serverLevel)) return false;
            var pos = thisNode.getBlockPos();
            if (thisNode.getTickCount() % 50 == 0) {
                int x = pos.getX() + serverLevel.random.nextInt(8) - serverLevel.random.nextInt(8);
                int z = pos.getZ() + serverLevel.random.nextInt(8) - serverLevel.random.nextInt(8);
                BiomeGenBase bg = serverLevel.getBiomeGenForCoords(x, z);
                int biobl = ThaumcraftWorldGenerator.getBiomeBlacklist(bg.biomeID);
                if (biobl != 0 && biobl != 2 && bg.biomeID != ThaumcraftWorldGenerator.biomeMagicalForest.biomeID) {
                    if (bg.biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID) {
                        Utils.setBiomeAt(serverLevel, x, z, ThaumcraftWorldGenerator.biomeMagicalForest);
                    } else if (serverLevel.getBlockState(pos).getBlock() == ThaumcraftBlocks.SILVERWOOD_KNOT) {
                        Utils.setBiomeAt(serverLevel, x, z, ThaumcraftWorldGenerator.biomeMagicalForest);
                    }
                }
            }
            
            return false;
        }
    };
    public static final NodeType EMPTY = new NodeType("thaumcraft:empty",1.f){
        @Override
        public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {
            var result = super.nodeTypeTick(thisNode);
            
            
            return result;
        }
    };

    private final String name;
    //when doing a node attack:
    // if another node has aspectAmount more than this node's aspect capacity(for same aspect)
    // we will have a chance of 1/(1 + (int) ((double) aspectCapacityAmountOfThis / attackBiggerNodeChangeModifier))
    // to attack that 'bigger' node
    // this value will pick Math.max(NodeModifier's[default:1],NodeType's[default:1]) for a node
    private final float attackBiggerNodeChangeModifier;

    public NodeType(String name,float attackBiggerNodeChangeModifier) {
        this.name = name;
        this.attackBiggerNodeChangeModifier = attackBiggerNodeChangeModifier;
        register(this);
    }

//    /** 注册新的 NodeType，可用于动态扩展 */
//    public static NodeType register(String name,float attackBiggerNodeChangeModifier) {
//        if (BY_NAME.containsKey(name)) {
//            throw new IllegalArgumentException("NodeType already exists: " + name);
//        }
//        return new NodeType(name,attackBiggerNodeChangeModifier);
//    }

    /** 内部注册方法 */
    private static void register(NodeType type) {
        if (BY_NAME.containsKey(type.name)) {
            throw new RuntimeException("Node type " + type.name + " is already registered");
        }
        BY_NAME.put(type.name, type);
        VALUES.add(type);
    }

    /** 返回所有 NodeType（顺序和定义顺序一致） */
    public static List<NodeType> values() {
        return Collections.unmodifiableList(VALUES);
    }

    /** 类似 enum 的 valueOf 方法 */
    public static NodeType valueOf(String name) {
        NodeType type = BY_NAME.get(name);
        if (type == null) {
            throw new IllegalArgumentException("No enum constant NodeType." + name);
        }
        return type;
    }

    /** 返回名称 */
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NodeType type)) return false;
        return Objects.equals(name, type.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public float getAttackBiggerNodeChangeModifier() {
        return attackBiggerNodeChangeModifier;
    }

    public boolean allowToAttackAnotherNode(AbstractNodeBlockEntity thisNode) {
        return true;
    }
    public int getAttackAnotherNodePeriod(AbstractNodeBlockEntity thisNode){
        return 2;
    }

    public boolean nodeTypeTick(AbstractNodeBlockEntity thisNode) {
        var level = thisNode.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return false;
        var pos = thisNode.getBlockPos();
        BiomeGenBase bg = serverLevel.getBiomeGenForCoords(pos.getX(), pos.getZ());
        if (bg.biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID && serverLevel.random.nextInt(500) == 0) {
            thisNode.setNodeType(NodeType.TAINTED);
            thisNode.nodeChange();
            return true;
        }
        return false;
    }
}