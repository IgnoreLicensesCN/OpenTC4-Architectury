package thaumcraft.api.visnet;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.collectionlike.CubeChunkedWeakLookups;
import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.lib.resourcelocations.VisNetNodeTypeResourceLocation;
import thaumcraft.common.tiles.abstracts.ICubeChunkBasedWeakLookupOwner;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.linearity.opentc4.Consts.TileVisNodeCompoundTagAccessors.PARENT_POS_ACCESSOR;
import static thaumcraft.api.visnet.VisNetHandler.visNetNodeLookups;

public abstract class VisNetNodeBlockEntity extends TileThaumcraft implements ICubeChunkBasedWeakLookupOwner<VisNetNodeBlockEntity> {
    public static final int DEFAULT_RANGE = 8;
    public static final Map<VisNetNodeTypeResourceLocation, Map<Level,CubeChunkedWeakLookups<VisNetNodeBlockEntity>>> DEFAULT_VIS_NET_NODE_LOOKUPS = new MapMaker().weakKeys().makeMap();
    static {
        visNetNodeLookups.add(DEFAULT_VIS_NET_NODE_LOOKUPS);
    }
    protected Map<VisNetNodeTypeResourceLocation, Map<Level,CubeChunkedWeakLookups<VisNetNodeBlockEntity>>> getVisNetNodeLookups() {
        return DEFAULT_VIS_NET_NODE_LOOKUPS;
    }
    public VisNetNodeBlockEntity(BlockEntityType<? extends VisNetNodeBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    protected @Nullable BlockPos parentPos;
    protected int nodeCounter = 0;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        var readPos = PARENT_POS_ACCESSOR.readFromCompoundTag(compoundTag);
        if (readPos.equals(BlockPosAccessor.NULL_POS_TO_WRITE)) {
            parentPos = null;
        }
        else {
            parentPos = readPos;
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        PARENT_POS_ACCESSOR.writeToCompoundTag(compoundTag, Objects.requireNonNullElse(parentPos, BlockPosAccessor.NULL_POS_TO_WRITE));
    }

    /**
     * @return the number from blocks away this node will check for parent nodes to connect to.
     */
    public int getRange() {
        return DEFAULT_RANGE;
    }

    /**
     * @return true if this is the source or root node from the vis network.
     */
    public abstract boolean isSource();

    /**
     * This method should never be called directly. Use {@link VisNetHandler#drainCentiVis} instead
     * <p>IgnoreLicensesCN:maybe you can call it in some ways?if you really want to drain from someone directly.</p>
     * @param aspect what aspect to drain
     * @param vis how much to drain
     * @return how much was actually drained
     *
     *
     */
    public int consumeCentiVis(Aspect aspect, int vis) {
        var parent = getParent();
        if (parent != null) {
            int out = parent.consumeCentiVis(aspect, vis);
            if (out>0) {
                triggerConsumeEffect(aspect);
            }
            return out;
        }
        return 0;
    }
    public void triggerConsumeEffect(Aspect aspect) {

    }
    
    @Nullable
    public VisNetNodeBlockEntity getParent() {
        if (this.level == null) return null;
        if (this.parentPos == null) return null;
        if (!(this.level.getBlockEntity(this.parentPos) instanceof VisNetNodeBlockEntity visNetNodeBlockEntity)) return null;
        return visNetNodeBlockEntity;
    }

    public VisNetNodeBlockEntity getRootSource() {
        var parent = this;
        while (true){
            if (parent == null) return null;
            if (parent.isSource()) return parent;
            parent = parent.getParent();
        }
    }
    public void setParent(VisNetNodeBlockEntity parent) {
        this.parentPos = parent.getBlockPos();
    }
    public void removeParent(){
        this.parentPos = null;
    }
    public void tick() {
//        if (processSavedLink()){return;}
        if (level == null) {return;}
        if (!level.isClientSide && ((nodeCounter++) % 40==0)) {
            var level = getLevel();
            if (level == null) {return;}
            var parentPosBefore = this.parentPos;
            //check for changes
            if (parentPos != null) {
                if (level.getBlockEntity(parentPos) instanceof VisNetNodeBlockEntity parentNodeBE) {
                    if (!VisNetHandler.canNodeBeSeen(this, parentNodeBE)) {
                        this.parentPos = null;
                    }
                }else {
                    this.parentPos = null;
                }
            }
            if (parentPos == null) {
                findNewParent();
            }
            if (!Objects.equals(parentPosBefore, this.parentPos)) {
                parentChanged();
            }
        }

    }

    @FunctionalInterface
    public interface VisNetNodeComparator{
        int compare(ObjectIntPair<VisNetNodeBlockEntity> parent1, ObjectIntPair<VisNetNodeBlockEntity> parent2,VisNetNodeBlockEntity base);
    }
    public static final VisNetNodeComparator DEFAULT_NODE_PARENT_COMPARING = ((parent1Pair, parent2Pair, base) -> {
        var basePos = base.getBlockPos();
        var compareNodeDistance = Integer.compare(parent1Pair.rightInt(), parent2Pair.rightInt());
        if (compareNodeDistance != 0) {
            return compareNodeDistance;
        }
        return Integer.compare(
                parent1Pair.left().getBlockPos().distManhattan(basePos),
                parent2Pair.left().getBlockPos().distManhattan(basePos)
        );
    });
    //-1 if connect to self
    protected final int getNodesToFinalParent(VisNetNodeBlockEntity another){
        int result = 0;
        while (true){
            var parentForAnother = another.getParent();
            if (parentForAnother == null) return result;
            if (parentForAnother == this) return -1;
            result += 1;
            another = parentForAnother;
        }
    }
    public void findNewParent() {
        AtomicReference<ObjectIntPair<VisNetNodeBlockEntity>> probablyBestParent = new AtomicReference<>(null);
        final Consumer<VisNetNodeBlockEntity> chooseNodeInLoop = (anotherNode) -> {
            int toFinalParentNodes = getNodesToFinalParent(anotherNode);
            if (anotherNode.canConnect(VisNetNodeBlockEntity.this)
                    && VisNetNodeBlockEntity.this.canConnect(anotherNode)
                    && toFinalParentNodes >= 0
            ) {
                var anotherNodeAndDistance = new ObjectIntPair<>(anotherNode,toFinalParentNodes);
                var currentParent = probablyBestParent.get();
                if (currentParent == null) {
                    probablyBestParent.set(anotherNodeAndDistance);
                }else {
                    var compareResult = DEFAULT_NODE_PARENT_COMPARING.compare(
                            currentParent,anotherNodeAndDistance,VisNetNodeBlockEntity.this
                    );
                    if (compareResult > 0){
                        probablyBestParent.set(anotherNodeAndDistance);
                    }
                }
            }
        };
        var selfPos = getBlockPos();
        for (var lookupLevelledMap : visNetNodeLookups) {
            if (lookupLevelledMap == null) continue;
            var lookupTypedMap = getVisNetNodeLookups().get(SOURCE);
            if (lookupTypedMap == null) continue;
            var sourceLookup = lookupTypedMap.get(level);
            if (sourceLookup != null) {
                sourceLookup.forItemsNearPosWithRange(selfPos,chooseNodeInLoop,getRange());
            }
            if (probablyBestParent.get() == null){
                var relayLookupLevelled = getVisNetNodeLookups().get(RELAY);
                if (relayLookupLevelled != null) {
                    var relayLookup = relayLookupLevelled.get(level);
                    if (relayLookup != null) {
                        relayLookup.forItemsNearPosWithRange(selfPos, chooseNodeInLoop, getRange());
                    }
                }
            }
        }
        this.parentPos = probablyBestParent.get().left().parentPos;
    }
    
    public void parentChanged() { }
    /**
     * only affects VisNetNode connection and color(not really for aspect)
     * like channel.but put 0 for any "channel".
     * --IgnoreLicensesCN
     * @return the type from shard this is attuned to:
     * none 0, <s>air 1, fire 2, water 3, earth 4, order 5, entropy 6
     * Should return -1 for most implementations</s>
     * (wont make sure which aspect it shows anymore.depends on BE behavior)
     */
    public int getAttunement() {
        return 0;
    }

    protected boolean canConnect(VisNetNodeBlockEntity anotherNode) {
        if (VisNetHandler.canNodeBeSeen(this, anotherNode)) return true;
        return this.getAttunement() == -1
                || anotherNode.getAttunement() == -1
                || anotherNode.getAttunement() == this.getAttunement();
    }

    public abstract VisNetNodeTypeResourceLocation getVisNetNodeType();

    public static final VisNetNodeTypeResourceLocation SOURCE =
            VisNetNodeTypeResourceLocation.of("thaumcraft","source");
    public static final VisNetNodeTypeResourceLocation RELAY =
            VisNetNodeTypeResourceLocation.of("thaumcraft","relay");

    @Override
    public void setRemoved() {
        unregisterFromWeakLookup(this.level);
        super.setRemoved();
    }

    @Override
    public void setLevel(Level level) {

        registerToCubeLookup(
                level,
                this.level

        );
        super.setLevel(level);
    }

    @Override
    public @Nullable Map<Level, CubeChunkedWeakLookups<VisNetNodeBlockEntity>> getSelfLookupMap() {
        return getVisNetNodeLookups()
                .computeIfAbsent(
                        getVisNetNodeType(),
                        _ignored -> new MapMaker().weakKeys().makeMap()
                );
    }

    @Override
    public @NotNull VisNetNodeBlockEntity getStoreItemForLookup() {
        return this;
    }
}
