package thaumcraft.api.visnet;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.lib.resourcelocations.VisNetNodeTypeResourceLocation;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.linearity.opentc4.Consts.TileVisNodeCompoundTagAccessors.PARENT_POS_ACCESSOR;
import static thaumcraft.api.visnet.VisNetHandler.visNetNodeLookups;

public abstract class VisNetNodeBlockEntity extends TileThaumcraft {
    public static final BlockPos NULL_POS_TO_WRITE = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final int DEFAULT_RANGE = 8;
    public static final Map<Level, Map<VisNetNodeTypeResourceLocation,CubeChunkedWeakLookups<VisNetNodeBlockEntity>>> DEFAULT_VIS_NET_NODE_LOOKUPS = new MapMaker().weakKeys().makeMap();
    static {
        visNetNodeLookups.add(DEFAULT_VIS_NET_NODE_LOOKUPS);
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
        if (readPos.equals(NULL_POS_TO_WRITE)) {
            parentPos = null;
        }
        else {
            parentPos = readPos;
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        if (parentPos == null){
            PARENT_POS_ACCESSOR.writeToCompoundTag(compoundTag,NULL_POS_TO_WRITE);
        }
        else{
            PARENT_POS_ACCESSOR.writeToCompoundTag(compoundTag, parentPos);
        }
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
     * This method should never be called directly. Use {@link VisNetHandler#drainVis} instead
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
    public void triggerConsumeEffect(Aspect aspect) {	}
    
    @Nullable
    public VisNetNodeBlockEntity getParent() {
        if (this.level == null) return null;
        if (this.parentPos == null) return null;
        if (!(this.level.getBlockEntity(this.parentPos) instanceof VisNetNodeBlockEntity visNetNodeBlockEntity)) return null;
        return visNetNodeBlockEntity;
    }

    public VisNetNodeBlockEntity getRootSource() {
        var parent = getParent();
        return parent != null ?
                parent.getRootSource() : this.isSource() ?
                this : null;
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



            if (!Objects.equals(parentPosBefore, this.parentPos)) {
                parentChanged();
            }
        }

    }

    @FunctionalInterface
    public interface VisNetNodeComparator{
        int compare(VisNetNodeBlockEntity parent1, VisNetNodeBlockEntity parent2,VisNetNodeBlockEntity base);
    }
    public static final VisNetNodeComparator DEFAULT_NODE_PARENT_COMPARING = ((parent1, parent2, base) -> {
        var basePos = base.getBlockPos();
        return Integer.compare(parent1.getBlockPos().distManhattan(basePos),parent2.getBlockPos().distManhattan(basePos));
    });
    public void findNewParent() {
        AtomicReference<VisNetNodeBlockEntity> probablyBestParent = new AtomicReference<>(null);
        final Consumer<VisNetNodeBlockEntity> chooseNodeInLoop = (anotherNode) -> {
            if (anotherNode.canConnect(VisNetNodeBlockEntity.this)
                    && VisNetNodeBlockEntity.this.canConnect(anotherNode)) {
                var currentParent = probablyBestParent.get();
                if (currentParent == null) {
                    probablyBestParent.set(anotherNode);
                }else {
                    var compareResult = DEFAULT_NODE_PARENT_COMPARING.compare(
                            currentParent,anotherNode,VisNetNodeBlockEntity.this
                    );
                    if (compareResult > 0){
                        probablyBestParent.set(anotherNode);
                    }
                }
            }
        };
        var selfPos = getBlockPos();
        for (var lookupLevelledMap : visNetNodeLookups) {
            if (lookupLevelledMap == null) continue;
            var lookupTypedMap = lookupLevelledMap.get(level);
            if (lookupTypedMap == null) continue;
            var sourceLookup = lookupTypedMap.get(SOURCE);
            if (sourceLookup != null) {
                sourceLookup.forItemsNearPos(selfPos,chooseNodeInLoop);
            }
            if (probablyBestParent.get() == null){
                var relayLookup = lookupTypedMap.get(RELAY);
                if (relayLookup != null) {
                    relayLookup.forItemsNearPos(selfPos,chooseNodeInLoop);
                }
            }
        }
        this.parentPos = probablyBestParent.get().parentPos;
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


    protected void storeToLookup(Level level){
        if (level != this.level){
            var pos = getBlockPos();
            if (this.level != null) {
                var lookupWithType = DEFAULT_VIS_NET_NODE_LOOKUPS.get(this.level);
                if (lookupWithType != null) {
                    var lookup = lookupWithType.get(getVisNetNodeType());
                    if (lookup != null){
                        lookup.remove(pos,this);
                    }
                }
            }
            if (level != null){
                DEFAULT_VIS_NET_NODE_LOOKUPS.computeIfAbsent(
                        level,
                        _ignored -> new HashMap<>()
                ).computeIfAbsent(getVisNetNodeType(),_ignored -> new CubeChunkedWeakLookups<>((byte)3))
                        .store(pos,this);
            }
        }
    }

    @Override
    public void setLevel(Level level) {
        storeToLookup(level);
        super.setLevel(level);
    }
}
