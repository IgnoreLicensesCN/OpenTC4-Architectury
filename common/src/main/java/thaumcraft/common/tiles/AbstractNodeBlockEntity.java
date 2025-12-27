package thaumcraft.common.tiles;

import com.linearity.opentc4.utils.BlockPosWithDim;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.*;
import thaumcraft.api.research.ScanResult;
import thaumcraft.api.wands.INodeHarmfulComponent;
import thaumcraft.api.wands.IVisContainer;
import thaumcraft.api.wands.IWandComponentsOwner;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.items.misc.ItemCompassStone;
import thaumcraft.common.lib.network.fx.PacketFXBlockZapS2C;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linearity.opentc4.Consts.NodeBlockEntityCompoundTagAccessors.*;
import static com.linearity.opentc4.utils.BlockPosWithDim.UNKNOWN_DIM;
import static thaumcraft.api.nodes.NodeModifier.*;
import static thaumcraft.api.wands.IVisContainer.CENTIVIS_MULTIPLIER;

//i think it would be suitable to abstract this since we have 3 types.
public abstract class AbstractNodeBlockEntity extends TileThaumcraft
        implements
        INodeBlockEntity
        , IWandInteractableBlock {
    long lastActiveMillis = 0L;
    AspectList aspects = new AspectList();
    AspectList aspectsBase = new AspectList();
    public static HashMap<String, BlockPosWithDim> nodeIdToLocations = new HashMap<>();
    private NodeType nodeType;
    private NodeModifier nodeModifier;
    int tickCount;
    public int regenerationTickPeriod;
    int wait;
    String id;
    ResourceLocation nodeLockId;
    boolean catchUp;
    public int drainColor;
    public Color targetColor;
    public Color color;
    //for renderers
    public Entity drainEntity;
    public HitResult drainCollision;

    public AbstractNodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.nodeType = NodeType.NORMAL;
        this.nodeModifier = null;
        this.tickCount = 0;
        this.regenerationTickPeriod = -1;
        this.wait = 0;
        this.id = null;
        this.nodeLockId = null;
        this.catchUp = false;
        this.drainEntity = null;
        this.drainCollision = null;
        this.drainColor = 0XFFFFFF;
        this.targetColor = new Color(0XFFFFFF);
        this.color = new Color(0XFFFFFF);
    }

    public String getId() {
        if (this.id == null) {
            this.id = this.generateId();
        }

        return this.id;
    }

    public String generateId() {
        var pos = this.getBlockPos();
        var level = this.level;
        var posWithDim = new BlockPosWithDim(
                level == null ? UNKNOWN_DIM:level.dimension().location(), pos
        );
        this.id = posWithDim.toString();
        if (level != null && nodeIdToLocations != null) {
            nodeIdToLocations.put(this.id, posWithDim);
        }

        return this.id;
    }

    @Override
    public void setRemoved() {
        if (nodeIdToLocations != null) {
            nodeIdToLocations.remove(this.id);
        }

        super.setRemoved();
    }


    private void bothSidedTickPart() {

    }


    public static void serverTick(AbstractNodeBlockEntity thiz) {
        if (thiz.id == null) {
            thiz.generateId();
        }

        boolean change = false;
        change = thiz.handleHungryNodeFirst(change);
        ++thiz.tickCount;
        thiz.checkLock();

        change |= INodeLock.getNodeLock(thiz.getLockId())
                .nodeLockTick(thiz);

        change |= thiz.handleAttackAnotherNode();
        change |= thiz.handleRecharge();
        change = thiz.handleNodeStability(change);
        //TODO:Listeners for NodeTypes
        change = thiz.handleTaintNode(change);
        change = thiz.handleDarkNode(change);
        change = thiz.handlePureNode(change);
        change = thiz.handleHungryNodeSecond(change);
        if (change) {
            thiz.markDirtyAndUpdateSelf();
        }
    }

    @Override
    public void clientTickByBlockHandle() {
        if (this.id == null) {
            this.generateId();
        }

        boolean change = false;
        change = this.handleHungryNodeFirst(change);
        ++this.tickCount;
        this.checkLock();

        if (this.getNodeType() == NodeType.DARK && this.tickCount % 50 == 0) {
            ItemCompassStone.sinisterNodes.put(new WorldCoordinates(this), System.currentTimeMillis());
        }
    }


    public void nodeChange() {
        this.regenerationTickPeriod = -1;
        markDirtyAndUpdateSelf();
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var usingStack = useOnContext.getItemInHand();
        var usingItem = usingStack.getItem();
        var player = useOnContext.getPlayer();
        if (usingItem instanceof IVisContainer container && player != null) {
            player.startUsingItem(useOnContext.getHand());
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void interactOnWandInteractable(Level level, LivingEntity livingEntity, ItemStack usingWand, int useCount) {
        boolean mfu = false;
        if (!(livingEntity instanceof Player player)) {
            return;
        }
        if (this.level == null) {
            player.stopUsingItem();
            return;
        }
        if (!(usingWand.getItem() instanceof IVisContainer visContainer) || !(usingWand.getItem() instanceof IWandComponentsOwner componentsOwner)) {
            return;
        }
        HitResult hitResult = EntityUtils.getHitResultFromPlayer(this.level, player, true);
        if (hitResult.getType() != Type.BLOCK) {
            player.stopUsingItem();
            return;
        }
        var hitPos = hitResult.getLocation();
        var hitBlockPos = new BlockPos((int) hitPos.x, (int) hitPos.y, (int) hitPos.z);

        var pos = this.getBlockPos();

        int i = hitBlockPos.getX();
        int j = hitBlockPos.getY();
        int k = hitBlockPos.getZ();
        if (i != pos.getX() || j != pos.getY() || k != pos.getZ()) {
            player.stopUsingItem();
            return;
        }

        if (tickCount % 5 == 0) {
            int tap = 1;
            if (ResearchManager.isResearchComplete(
                    player.getName()
                            .getString(), "NODETAPPER1"
            )) {
                ++tap;
            }

            if (ResearchManager.isResearchComplete(
                    player.getName()
                            .getString(), "NODETAPPER2"
            )) {
                ++tap;
            }

            //TODO:Some listeners
            boolean hasNodeHarmfulComponentsFlag = false;
            for (var component : componentsOwner.getWandComponents(usingWand)) {
                if (component instanceof INodeHarmfulComponent) {
                    hasNodeHarmfulComponentsFlag = true;
                    break;
                }
            }
            boolean preserve = !player.isCrouching()
                    && ResearchManager.isResearchComplete(
                    player.getName()
                            .getString(), "NODEPRESERVE"
            )
                    && !hasNodeHarmfulComponentsFlag;
            boolean success = false;
            Aspect aspect = null;
            if ((aspect = this.chooseRandomFilteredFromSource(
                    visContainer.getAspectsWithRoom(usingWand), preserve)) != null) {
                int amt = this.getAspects()
                        .getAmount(aspect);
                if (tap > amt) {
                    tap = amt;
                }

                if (preserve && tap == amt) {
                    --tap;
                }

                if (tap > 0) {
                    var serverFlag = Platform.getEnvironment() == Env.SERVER;
                    int remainingCentiVis = visContainer.addCentiVis(
                            usingWand, aspect, tap * CENTIVIS_MULTIPLIER, serverFlag);
                    if (remainingCentiVis < tap) {
                        this.drainColor = aspect.getColor();
                        if (serverFlag) {
                            this.takeFromContainer(aspect, tap - remainingCentiVis);
                            mfu = true;
                        }

                        success = true;
                    }
                }
            }

            if (success) {
                this.drainEntity = player;
                this.drainCollision = hitResult;
                this.targetColor = new Color(this.drainColor);
            } else {
                this.drainEntity = null;
                this.drainCollision = null;
            }

            if (mfu) {
                markDirtyAndUpdateSelf();
            }
        }

        if ((Platform.getEnvironment() == Env.CLIENT)) {
            int r = this.targetColor.getRed();
            int g = this.targetColor.getGreen();
            int b = this.targetColor.getBlue();
            int r2 = this.color.getRed() * 4;
            int g2 = this.color.getGreen() * 4;
            int b2 = this.color.getBlue() * 4;
            this.color = new Color((r + r2) / 5, (g + g2) / 5, (b + b2) / 5);
        }
    }

//   public int onWandRightClick(Level world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
//      return -1;
//   }
//
//   public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
//      player.setItemInUse(wandstack, Integer.MAX_VALUE);
//      WandCastingItem wand = (WandCastingItem)wandstack.getItem();
//      wand.setObjectInUse(wandstack, pos.getX(), pos.getY(), pos.getZ());
//      return wandstack;
//   }

    public AspectList getAspects() {
        return this.aspects;
    }

    public AspectList getAspectsBase() {
        return this.aspectsBase;
    }

    public void setAspects(AspectList aspects) {
        this.aspects = aspects;
        this.aspectsBase = aspects.copy();
    }

    public int addToContainer(Aspect aspect, int amount) {
        int left = amount + this.aspects.getAmount(aspect) - this.aspectsBase.getAmount(aspect);
        left = Math.max(left, 0);
        this.aspects.addAll(aspect, amount - left);
        return left;
    }

    public boolean takeFromContainer(Aspect aspect, int amount) {
        return this.aspects.reduce(aspect, amount);
    }

    public Aspect takeRandomPrimalFromSource() {
        Aspect[] primals = this.aspects.getPrimalAspects();
        Aspect asp = primals[this.level.random.nextInt(primals.length)];
        return asp != null && this.aspects.reduce(asp, 1) ? asp : null;
    }

    public Aspect chooseRandomFilteredFromSource(Map<Aspect, Integer> aspectIntegerMap, boolean preserve) {
        return chooseRandomFilteredFromSource(AspectList.of(aspectIntegerMap), preserve);
    }

    public Aspect chooseRandomFilteredFromSource(AspectList filter, boolean preserve) {
        int min = preserve ? 1 : 0;
        ArrayList<Aspect> validaspects = new ArrayList<>();

        for (var entry : this.aspects.getAspects()
                .entrySet()) {
            var prim = entry.getKey();
            var amount = entry.getValue();
            if (filter.getAmount(prim) > 0 && amount > min) {
                validaspects.add(prim);
            }
        }

        if (this.level != null && !validaspects.isEmpty()) {
            Aspect asp = validaspects.get(this.level.random.nextInt(validaspects.size()));
            if (asp != null && this.aspects.getAmount(asp) > min) {
                return asp;
            }
        }
        return null;
    }

    public @NotNull NodeType getNodeType() {
        return this.nodeType != null?this.nodeType:NodeType.EMPTY;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setNodeModifier(NodeModifier nodeModifier) {
        this.nodeModifier = nodeModifier;
    }

    public @NotNull NodeModifier getNodeModifier() {
        return this.nodeModifier != null?this.nodeModifier:NodeModifier.EMPTY;
    }

    public int getNodeVisBase(Aspect aspect) {
        return this.aspectsBase.getAmount(aspect);
    }

    public void setNodeVisBase(Aspect aspect, short nodeVisBase) {
        if (this.aspectsBase.getAmount(aspect) < nodeVisBase) {
            this.aspectsBase.mergeWithHighest(aspect, nodeVisBase);
        } else {
            this.aspectsBase.reduce(aspect, this.aspectsBase.getAmount(aspect) - nodeVisBase);
        }

    }

    @Override
    public void readCustomNBT(CompoundTag tag) {

        this.id = NODE_ID_ACCESSOR.readFromCompoundTag(tag);
        addNodeToCache();

        var nodeType = NodeType.valueOf(NODE_TYPE_ACCESSOR.readFromCompoundTag(tag));
        this.setNodeType(nodeType);
        String mod = NODE_MODIFIER_ACCESSOR.readFromCompoundTag(tag);
        if (mod != null) {
            this.setNodeModifier(NodeModifier.valueOf(mod));
        } else {
            this.setNodeModifier(null);
        }

        this.aspects.loadFrom(tag, NODE_ASPECTS_ACCESSOR);

//      String de = nbttagcompound.getString("drainer");
//      if (de != null && !de.isEmpty() && this.getlevel != null) {
//         this.drainEntity = this.getlevel.getPlayerEntityByName(de);
//         if (this.drainEntity != null) {
//            this.drainCollision = new HitResult(
//                    pos.getX(), pos.getY(), pos.getZ(),
//                    0, new Vec3(this.drainEntity.posX, this.drainEntity.posY, this.drainEntity.posZ));
//         }
//      }
//
//      this.drainColor = nbttagcompound.getInteger("draincolor");

        this.lastActiveMillis = NODE_LAST_ACTIVE_ACCESSOR.readFromCompoundTag(tag);
        AspectList al = new AspectList();
        al.loadFrom(tag, NODE_ASPECTS_BASE_ACCESSOR);
        this.aspectsBase = al;

        var modifier = this.getNodeModifier();
        int regen = modifier.getRegenValue(this);

        long ct = System.currentTimeMillis();
        int inc = regen * 75;
        if (regen > 0 && this.lastActiveMillis > 0L && ct > this.lastActiveMillis + (long) inc) {
            this.catchUp = true;
        }
    }


    public void addNodeToCache() {
        if (this.level != null && nodeIdToLocations != null) {
            nodeIdToLocations.put(
                    this.id, new BlockPosWithDim(
                            this.level.dimension()
                                    .location(), this.getBlockPos()
                    )
            );
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag tag) {
        NODE_LAST_ACTIVE_ACCESSOR.writeToCompoundTag(tag, this.lastActiveMillis);
        this.aspectsBase.saveTo(tag, NODE_ASPECTS_BASE_ACCESSOR);


        if (this.id == null) {
            this.id = this.generateId();
        }

        addNodeToCache();

        NODE_ID_ACCESSOR.writeToCompoundTag(tag, this.id);
        NODE_TYPE_ACCESSOR.writeToCompoundTag(tag, this.getNodeType().name());
        NODE_MODIFIER_ACCESSOR.writeToCompoundTag(tag, this.getNodeModifier().name());

        this.aspects.saveTo(tag, NODE_ASPECTS_ACCESSOR);
    }

//   public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
//
//
//   }
//
//   public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
//      this.drainEntity = null;
//      this.drainCollision = null;
//   }

    public boolean takeFromContainer(AspectList ot) {
        return false;
    }

    public boolean doesContainerContainAmount(Aspect tag, int amount) {
        return false;
    }

    public boolean doesContainerContain(AspectList ot) {
        return false;
    }

    public int containerContains(Aspect tag) {
        return 0;
    }

    public boolean doesContainerAccept(Aspect tag) {
        return true;
    }

    private boolean handleHungryNodeFirst(boolean change) {
        var pos = this.getBlockPos();
        if (this.getNodeType() == NodeType.HUNGRY) {
            if (this.level instanceof ClientLevel clientLevel) {
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
                            clientLevel, v1, v2, true/*, false, false (always these two flags)*/);

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
                                    AspectList al = ScanManager.getScanAspects(scan, this.level);
                                    if (al != null && al.size() > 0) {
                                        al = ResearchManager.reduceToPrimals(al.copy());
                                        if (al.size() > 0) {
                                            Aspect a = al.getAspects()
                                                    .keySet()
                                                    .toArray(new Aspect[0])[this.level.random.nextInt(al.size())];
                                            if (this.getAspects()
                                                    .getAmount(a) < this.getNodeVisBase(a)) {
                                                this.addToContainer(a, 1);
                                                change = true;
                                            } else if (this.level.random.nextInt(
                                                    1 + this.getNodeVisBase(a) * 2) < al.getAmount(a)) {
                                                this.aspectsBase.addAll(a, 1);
                                                change = true;
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
        }

        return change;
    }

    //attack another node(zap~),take vis from there.
    private boolean handleAttackAnotherNode() {
        if (level == null) return false;
        var nodeLock = INodeLock.getNodeLock(this.getLockId());
        var pos = this.getBlockPos();
        var nodeType = this.getNodeType();
        var nodeModifier = this.getNodeModifier();
        if (nodeLock != null && !nodeLock.allowToAttackAnotherNode()) {
            return false;
        }
        if (nodeModifier.allowToAttackAnotherNode(this)) {
            return false;
        }

        float nodeTypeAttackBiggerNodeChangeModifier = nodeType.getAttackBiggerNodeChangeModifier();
        float nodeModifierAttackBiggerNodeChangeModifier = nodeModifier.getAttackBiggerNodeChangeModifier();
        float attackBiggerNodeChangeModifier = Math.max(nodeTypeAttackBiggerNodeChangeModifier,nodeModifierAttackBiggerNodeChangeModifier);
        int attackPeriod;
        attackPeriod = Math.min(nodeType.getAttackAnotherNodePeriod(this), nodeModifier.getAttackAnotherNodePeriod(this));

        if (this.tickCount % attackPeriod != 0) {
            return false;
        }
        int xOffset = this.level.random.nextInt(5) - this.level.random.nextInt(5);
        int yOffset = this.level.random.nextInt(5) - this.level.random.nextInt(5);
        int zOffset = this.level.random.nextInt(5) - this.level.random.nextInt(5);
        if (xOffset == 0 && yOffset == 0 && zOffset == 0) {
            return false;
        }

        var pos2 = new BlockPos(pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset);
        BlockEntity probablyAnotherNode = this.level.getBlockEntity(pos2);
        if (probablyAnotherNode instanceof AbstractNodeBlockEntity anotherNode
                && this.level.getBlockState(pos2).getBlock() instanceof INodeBlock nodeBlock
                && !nodeBlock.preventAttackFromAnotherNode()
        ) {
            if (anotherNode.getLockId() != null) {
                return false;
            }

            int visSizeAvgOfAnotherNode = (anotherNode.getAspects().visSize() + anotherNode.getAspectsBase().visSize())
                    / 2;
            int visSizeAvgOfThisNode = (this.getAspects().visSize() + this.getAspectsBase().visSize())
                    / 2;
            boolean anotherNodeHaveVis = anotherNode.getAspects().size() > 0;
            if (visSizeAvgOfAnotherNode < visSizeAvgOfThisNode
                    && anotherNodeHaveVis
            ) {
                Aspect aspectToTake = anotherNode.getAspects()
                        .randomAspect(this.level.getRandom());
                int canTakeAmountOfAnother = this.getAspects().getAmount(aspectToTake);
                int aspectCapacityAmountOfThis = this.getNodeVisBase(aspectToTake);

                boolean didAttackNode = false;
                boolean canTakeVisFromNode = anotherNode.takeFromContainer(aspectToTake, 1);
                if (canTakeAmountOfAnother < aspectCapacityAmountOfThis
                        && canTakeVisFromNode
                ) {
                    this.addToContainer(aspectToTake, 1);
                    didAttackNode = true;
                } else if (canTakeVisFromNode) {
                    if (this.level.random.nextInt(1 + (int) ((double) aspectCapacityAmountOfThis / attackBiggerNodeChangeModifier)) == 0) {
                        this.aspectsBase.addAll(aspectToTake, 1);
                        nodeModifier.onAttackAnotherNode(this, anotherNode, aspectToTake);
                    }
                    didAttackNode = true;
                }

                if (didAttackNode) {
                    anotherNode.wait = anotherNode.regenerationTickPeriod / 2;
                    probablyAnotherNode.setChanged();
                    if (probablyAnotherNode.hasLevel() && probablyAnotherNode.getLevel() != null) {
                        probablyAnotherNode.getLevel()
                                .sendBlockUpdated(
                                        probablyAnotherNode.getBlockPos(), probablyAnotherNode.getBlockState(),
                                        probablyAnotherNode.getBlockState(), Block.UPDATE_ALL
                                );
                    }
                    if (level instanceof ServerLevel serverLevel) {
                        float cx = pos.getX() + 0.5f;
                        float cy = pos.getY() + 0.5f;
                        float cz = pos.getZ() + 0.5f;
                        float nodeBeingAttackedX = cx + xOffset;
                        float nodeBeingAttackedY = cy + yOffset;
                        float nodeBeingAttackedZ = cz + zOffset;
                        double rangeSq = 32.0 * 32.0;

                        PacketFXBlockZapS2C packet = new PacketFXBlockZapS2C(
                                nodeBeingAttackedX,
                                nodeBeingAttackedY,
                                nodeBeingAttackedZ,
                                cx,
                                cy,
                                cz
                        );
                        packet.sendToAllAround(serverLevel, pos, rangeSq);
                    }
                    return true;
                }
            }
        }


        return false;
    }

    private boolean handleRecharge() {
        if (level == null) return false;
        var modifier = this.getNodeModifier();
        var nodeLock = INodeLock.getNodeLock(this.getLockId());
        if (this.regenerationTickPeriod < 0) {

            this.regenerationTickPeriod = modifier.getRegenValue(this);
            if (nodeLock != null) {
                this.regenerationTickPeriod *= nodeLock.nodeRegenerationDelayMultiplier();
            }
        }

        if (this.catchUp) {
            this.catchUp = false;
            long currentMillis = System.currentTimeMillis();
            int regenPeriod = this.regenerationTickPeriod * 75;
            int regenCounts = regenPeriod > 0 ? (int) ((currentMillis - this.lastActiveMillis) / (long) regenPeriod) : 0;
            if (regenCounts > 0) {
                for (int a = 0; a < Math.min(regenCounts, this.aspectsBase.visSize()); ++a) {
                    AspectList al = new AspectList();

                    for (var aspectEntry : this.getAspects()
                            .getAspects()
                            .entrySet()) {
                        var aspect = aspectEntry.getKey();
                        var amount = aspectEntry.getValue();
                        if (amount < this.getNodeVisBase(aspect)) {
                            al.addAll(aspect, 1);
                        }
                    }

                    if (al.size() > 0) {
                        this.addToContainer(al.randomAspect(this.level.random), 1);
                    }
                }
            }
        }

        if (this.tickCount % 1200 == 0) {
            for (var aspectEntry : this.getAspects()
                    .getAspects()
                    .entrySet()) {
                var aspect = aspectEntry.getKey();
                var amount = aspectEntry.getValue();
                if (amount <= 0) {
                    //so if we're unlucky to meet this period,aspect size will -1!
                    this.setNodeVisBase(aspect, (short) (this.getNodeVisBase(aspect) - 1));
                    if (this.level.random.nextInt(20) == 0 || this.getNodeVisBase(aspect) <= 0) {
                        this.getAspects().reduceAndRemoveIfNegative(aspect);
                        this.getNodeModifier().onPeriodicReduceSize(this);
                        this.nodeChange();
                        break;
                    }
                    this.nodeChange();
                }
            }

            if (this.getAspects()
                    .size() <= 0) {
                removeNode();
                this.setRemoved();
            }
        }

        if (this.wait > 0) {
            --this.wait;
        }

        //regen all vis
        if (this.regenerationTickPeriod > 0
                && this.wait == 0
                && this.tickCount % this.regenerationTickPeriod == 0) {
            this.lastActiveMillis = System.currentTimeMillis();
            AspectList al = new AspectList();
            for (var aspectEntry : this.getAspects()
                    .getAspects()
                    .entrySet()) {
                var aspect = aspectEntry.getKey();
                var amount = aspectEntry.getValue();
                if (amount < this.getNodeVisBase(aspect)) {
                    al.addAll(aspect, 1);
                }
            }
            if (al.size() > 0) {
                this.addToContainer(al.randomAspect(this.level.random), 1);
                return true;
            }
        }

        return false;
    }

    //TODO:If you are silverTree log or something contains a node,override it
    public abstract void removeNode();

    private boolean handleTaintNode(boolean change) {
        if (!(this.level instanceof ServerLevel serverLevel)) return false;
        var pos = this.getBlockPos();
        if (this.getNodeType() == NodeType.TAINTED && this.tickCount % 50 == 0) {
            int x = 0;
            int z = 0;
            int y = 0;
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
        } else if (this.getNodeType() != NodeType.PURE && this.getNodeType() != NodeType.TAINTED && this.tickCount % 100 == 0) {
            BiomeGenBase bg = serverLevel.getBiomeGenForCoords(pos.getX(), pos.getZ());
            if (bg.biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID && serverLevel.random.nextInt(500) == 0) {
                this.setNodeType(NodeType.TAINTED);
                this.nodeChange();
            }
        }

        return change;
    }

    private boolean handleNodeStability(boolean change) {
        if (!(this.level instanceof ServerLevel serverLevel)) return false;
        var pos = this.getBlockPos();
        if (this.tickCount % 100 == 0) {
            if (this.getNodeType() == NodeType.UNSTABLE && this.level.random.nextBoolean()) {
                if (this.getLockId() == 0) {
                    Aspect aspect = null;
                    if ((aspect = this.takeRandomPrimalFromSource()) != null) {
                        EntityAspectOrb orb = new EntityAspectOrb(
                                this.level, (double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F,
                                (double) pos.getZ() + (double) 0.5F, aspect, 1
                        );
                        this.level.addFreshEntity(orb);

                        change = true;
                    }
                } else if (
                        this.level.random.nextInt(10000 / this.getLockId()) == 42) {
                    this.setNodeType(NodeType.NORMAL);
                    change = true;
                }
            }

            if (this.getNodeModifier() == FADING
                    && this.getLockId() > 0
                    && this.level.random.nextInt(
                    12500 / this.getLockId()) == 69
            ) {
                this.setNodeModifier(PALE);
                change = true;
            }
        }

        return change;
    }

    private boolean handlePureNode(boolean change) {
        if (!(this.level instanceof ServerLevel serverLevel)) return false;
        var pos = this.getBlockPos();
        int dimbl = ThaumcraftWorldGenerator.getDimBlacklist(this.level.dimension());
        if (dimbl != 0 && dimbl != 2 && this.getNodeType() == NodeType.PURE && this.tickCount % 50 == 0) {
            int x = pos.getX() + this.level.random.nextInt(8) - this.level.random.nextInt(8);
            int z = pos.getZ() + this.level.random.nextInt(8) - this.level.random.nextInt(8);
            BiomeGenBase bg = this.level.getBiomeGenForCoords(x, z);
            int biobl = ThaumcraftWorldGenerator.getBiomeBlacklist(bg.biomeID);
            if (biobl != 0 && biobl != 2 && bg.biomeID != ThaumcraftWorldGenerator.biomeMagicalForest.biomeID) {
                if (bg.biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID) {
                    Utils.setBiomeAt(this.level, x, z, ThaumcraftWorldGenerator.biomeMagicalForest);
                } else if (this.level.getBlock(pos.getX(), pos.getY(), pos.getZ()) == ConfigBlocks.blockMagicalLog) {
                    Utils.setBiomeAt(this.level, x, z, ThaumcraftWorldGenerator.biomeMagicalForest);
                }
            }
        }

        return change;
    }

    private boolean handleDarkNode(boolean change) {
        if (!(this.level instanceof ServerLevel serverLevel)) return false;
        var pos = this.getBlockPos();
        int dimbl = ThaumcraftWorldGenerator.getDimBlacklist(this.level.dimension());
        int biobl = ThaumcraftWorldGenerator.getBiomeBlacklist(
                this.level.getBiomeGenForCoords(pos.getX(), pos.getZ()).biomeID);
        if (biobl != 0 && biobl != 2 && this.level.dimension() != -1 && this.level.dimension() != 1 && dimbl != 0 && dimbl != 2 && this.getNodeType() == NodeType.DARK && this.tickCount % 50 == 0) {
            int x = pos.getX() + this.level.random.nextInt(12) - this.level.random.nextInt(12);
            int z = pos.getZ() + this.level.random.nextInt(12) - this.level.random.nextInt(12);
            BiomeGenBase bg = this.level.getBiomeGenForCoords(x, z);
            if (bg.biomeID != ThaumcraftWorldGenerator.biomeEerie.biomeID) {
                Utils.setBiomeAt(this.level, x, z, ThaumcraftWorldGenerator.biomeEerie);
            }

            if (Config.hardNode && this.level.random.nextBoolean() && this.level.getClosestPlayer(
                    (double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F,
                    (double) pos.getZ() + (double) 0.5F, 24.0F
            ) != null) {
                EntityGiantBrainyZombie entity = new EntityGiantBrainyZombie(this.level);
                if (entity != null) {
                    int j = this.level.getEntitiesWithinAABB(
                                    entity.getClass(),
                                    AxisAlignedBB.getBoundingBox(
                                                    pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1,
                                                    pos.getY() + 1, pos.getZ() + 1
                                            )
                                            .expand(10.0F, 6.0F, 10.0F)
                            )
                            .size();
                    if (j <= 3) {
                        double d0 = (double) pos.getX() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double) 5.0F;
                        double d3 = pos.getY() + this.level.random.nextInt(3) - 1;
                        double d4 = (double) pos.getZ() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double) 5.0F;
                        LivingEntity entityliving = entity;
                        entity.setLocationAndAngles(d0, d3, d4, this.level.random.nextFloat() * 360.0F, 0.0F);
                        if (entityliving == null || entityliving.getCanSpawnHere()) {
                            this.level.spawnEntityInWorld(entityliving);
                            this.level.playAuxSFX(2004, pos.getX(), pos.getY(), pos.getZ(), 0);
                            if (entityliving != null) {
                                entityliving.spawnExplosionParticle();
                            }
                        }
                    }
                }
            }
        }

        return change;
    }

    private boolean handleHungryNodeSecond(boolean change) {
        if (this.getNodeType() == NodeType.HUNGRY && this.tickCount % 50 == 0) {
            var pos = this.getBlockPos();
            int tx = pos.getX() + this.level.random.nextInt(16) - this.level.random.nextInt(16);
            int ty = pos.getY() + this.level.random.nextInt(16) - this.level.random.nextInt(16);
            int tz = pos.getZ() + this.level.random.nextInt(16) - this.level.random.nextInt(16);
            if (ty > this.level.getHeightValue(tx, tz)) {
                ty = this.level.getHeightValue(tx, tz);
            }

            Vec3 v1 = new Vec3(
                    (double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F,
                    (double) pos.getZ() + (double) 0.5F
            );
            Vec3 v2 = new Vec3((double) tx + (double) 0.5F, (double) ty + (double) 0.5F, (double) tz + (double) 0.5F);
            HitResult mop = ThaumcraftApiHelper.rayTraceIgnoringSource(this.level, v1, v2, true, false, false);
            if (mop != null && this.getDistanceFrom(mop.blockX, mop.blockY, mop.blockZ) < (double) 256.0F) {
                tx = mop.blockX;
                ty = mop.blockY;
                tz = mop.blockZ;
                Block bi = this.level.getBlock(tx, ty, tz);
                this.level.getBlockMetadata(tx, ty, tz);
                if (!bi.isAir(this.level, tx, ty, tz)) {
                    float h = bi.getBlockHardness(this.level, tx, ty, tz);
                    if (h >= 0.0F && h < 5.0F) {
                        this.level.func_147480_a(tx, ty, tz, true);
                    }
                }
            }
        }

        return change;
    }

    public ResourceLocation getLockId() {
        return this.nodeLockId;
    }

    public abstract boolean nodeLockApplicable();

    public void checkLock() {
        var pos = this.getBlockPos();
        if ((this.tickCount <= 1 || this.tickCount % 50 == 0) && pos.getY() > 0
                && nodeLockApplicable()
        ) {
            var oldLock = this.nodeLockId;
            this.nodeLockId = null;
            if (this.level != null) {
                if (!this.level.hasNeighborSignal(
                        pos.below())) {//i have to say if it has activated it shouldn't be charged.
                    var block = this.level.getBlockState(pos.below())
                            .getBlock();
                    if (block instanceof INodeLock nodeLockCurrent) {
                        this.nodeLockId = nodeLockCurrent.getNodeLockId();
                    }
                }
            }

            if (oldLock != this.nodeLockId) {
                this.regenerationTickPeriod = -1;
            }
        }

    }
//
//    @Override
//    public void tick() {
//
//    }
//
//    @Override
//    public BlockPos getPos() {
//        return this.getBlockPos();
//    }


}
