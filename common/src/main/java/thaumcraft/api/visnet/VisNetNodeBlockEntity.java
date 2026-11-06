package thaumcraft.api.visnet;

import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import com.linearity.opentc4.utils.collectionlike.coordlookups.CoordLookup;
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
import thaumcraft.common.tiles.abstracts.ICoordLookupOwnerBlockEntity;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.linearity.opentc4.Consts.TileVisNodeCompoundTagAccessors.PARENT_POS_ACCESSOR;

public abstract class VisNetNodeBlockEntity extends TileThaumcraft implements ICoordLookupOwnerBlockEntity<VisNetNodeBlockEntity> {
    public static final int DEFAULT_RANGE = 8;

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
        if (!(LevelBlockEntityAccessing.getExistingBlockEntity(this.level, this.parentPos) instanceof VisNetNodeBlockEntity visNetNodeBlockEntity)) return null;
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
                if (LevelBlockEntityAccessing.getExistingBlockEntity(level, parentPos) instanceof VisNetNodeBlockEntity parentNodeBE) {
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
        assert level != null;
        for(var sourceLookup:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(SOURCE)){
            sourceLookup.forItemsNearPosWithRange(selfPos,chooseNodeInLoop,getRange());
            break;
        }
        if (probablyBestParent.get() == null){
            for (var relayLookup:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(RELAY)){
                relayLookup.forItemsNearPosWithRange(selfPos, chooseNodeInLoop, getRange());
                break;
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

    public abstract VisNetNodeType getVisNetNodeType();

    public static class VisNetNodeType extends CoordLookupOwnerLevel.CoordLookupTypeKey<VisNetNodeBlockEntity /*with VisNetNodeType*/ >{
        public final VisNetNodeTypeResourceLocation id;
        public VisNetNodeType(VisNetNodeTypeResourceLocation id) {
            this.id = id;
            ALL_VIS_NET_NODE_TYPES.add(this);
        }
        public static final Collection<VisNetNodeType> ALL_VIS_NET_NODE_TYPES = new CopyOnWriteArrayList<>();
    }
    public static final VisNetNodeType SOURCE =
            new VisNetNodeType(VisNetNodeTypeResourceLocation.of("thaumcraft","source"));
    public static final VisNetNodeType RELAY =
            new VisNetNodeType(VisNetNodeTypeResourceLocation.of("thaumcraft","relay"));

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
    public @NotNull CoordLookup<VisNetNodeBlockEntity> getSelfLookupInLevel(Level level) {
        for (var result:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(getVisNetNodeType())){
            return result;
        }
        throw new IllegalStateException("Cannot get self lookup for type " + getVisNetNodeType().id + " in level " + level + " ,maybe you need to register a default one");
    }

    @Override
    public @NotNull VisNetNodeBlockEntity getStoreItemForLookup() {
        return this;
    }
}
