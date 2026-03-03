package thaumcraft.common.tiles.crafted.visnet;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.*;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.visnet.VisNetNodeBlockEntity;
import thaumcraft.common.lib.NodeInfo;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.EnergizedAuraNodeBlockEntityTagAccessors.*;

public class EnergizedAuraNodeBlockEntity extends VisNetNodeBlockEntity {
    protected @NotNull AspectList<Aspect> auraBase = (new AspectList<>())
            .addAll(Aspects.AIR, 20)
            .addAll(Aspects.FIRE, 20)
            .addAll(Aspects.EARTH, 20)
            .addAll(Aspects.WATER, 20)
            .addAll(Aspects.ORDER, 20)
            .addAll(Aspects.ENTROPY, 20);
    protected @NotNull CentiVisList<Aspect> centiVisBase = new CentiVisList<>();
    protected @NotNull CentiVisList<Aspect> currentOwningCentiVis = new CentiVisList<>();
    protected @NotNull NodeType nodeType = NodeType.NORMAL;
    protected @NotNull NodeModifier nodeModifier = NodeModifier.EMPTY;
    protected @NotNull String id = "blank";

    public @NotNull AspectList<Aspect> getAuraBase() {
        return auraBase;
    }

    public void setAuraBase(@NotNull AspectList<Aspect> auraBase) {
        this.auraBase = auraBase;
    }

    public @NotNull CentiVisList<Aspect> getCentiVisBase() {
        return centiVisBase;
    }

    public void setCentiVisBase(@NotNull CentiVisList<Aspect> centiVisBase) {
        this.centiVisBase = centiVisBase;
    }

    public @NotNull CentiVisList<Aspect> getCurrentOwningCentiVis() {
        return currentOwningCentiVis;
    }

    public void setCurrentOwningCentiVis(@NotNull CentiVisList<Aspect> currentOwningCentiVis) {
        this.currentOwningCentiVis = currentOwningCentiVis;
    }

    public EnergizedAuraNodeBlockEntity(BlockEntityType<? extends EnergizedAuraNodeBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EnergizedAuraNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ENERGIZED_NODE,blockPos,blockState);
    }

    @Override
    public int getRange() {
        return 8;
    }

    @Override
    public boolean isSource() {
        return true;
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        var nodeInfo = NODE_INFO.readFromCompoundTag(compoundTag);
        this.id = nodeInfo.nodeId;
        this.nodeType = nodeInfo.nodeType;
        this.nodeModifier = nodeInfo.nodeModifier;
        this.centiVisBase = NODE_CENTIVIS_BASE_ACCESSOR.readFromCompoundTag(compoundTag);
        this.auraBase = nodeInfo.nodeAspectsBase;
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        NODE_INFO.writeToCompoundTag(compoundTag,new NodeInfo(id,nodeType,nodeModifier, UnmodifiableAspectList.EMPTY,auraBase));
        NODE_CENTIVIS_BASE_ACCESSOR.writeToCompoundTag(compoundTag,centiVisBase);
    }
    public void tick(){
        super.tick();
        if (this.level == null) {
            return;
        }
        if (!this.level.isClientSide){
            //TODO:[maybe wont finished]better api
            nodeType.nodeTypeTickEnergized(this);
        }
    }

    //TODO:[maybe wont finished]better api
    public void setupNode(){
        this.centiVisBase = new CentiVisList<>();
        var temp = ResearchManager.reduceToPrimals(auraBase, true);

        for(var aspectEntry : temp.getAspectView().entrySet()) {
            var aspect = aspectEntry.getKey();
            int amt = nodeModifier.onSetupEnergizedNodeAspectAmount(this,aspect,aspectEntry.getValue());
            amt = nodeType.onSetupEnergizedNodeAspectAmount(this,aspect,amt);

            if (amt >= 1) {
                this.centiVisBase.mergeWithHighest(aspect, amt);
            }
        }

        this.markDirtyAndUpdateSelf();
    }
    public int consumeCentiVis(Aspect aspect, int amount) {
        int drain = Math.min(this.currentOwningCentiVis.getAmount(aspect), amount);
        if (drain > 0) {
            this.currentOwningCentiVis.tryReduce(aspect, drain);
        }

        return drain;
    }

}
