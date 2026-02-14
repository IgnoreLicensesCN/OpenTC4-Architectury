package thaumcraft.common.tiles.crafted;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.visnet.VisNetNodeBlockEntity;
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

    public EnergizedAuraNodeBlockEntity(BlockEntityType<EnergizedAuraNodeBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
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
        this.id = NODE_ID_ACCESSOR.readFromCompoundTag(compoundTag);
        this.nodeType = NodeType.valueOf(NODE_TYPE_ACCESSOR.readFromCompoundTag(compoundTag));
        this.nodeModifier = NodeModifier.valueOf(NODE_MODIFIER_ACCESSOR.readFromCompoundTag(compoundTag));
        this.centiVisBase = NODE_CENTIVIS_BASE_ACCESSOR.readFromCompoundTag(compoundTag);
        this.auraBase = NODE_ASPECT_BASE_ACCESSOR.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        NODE_ID_ACCESSOR.writeToCompoundTag(compoundTag, id);
        NODE_TYPE_ACCESSOR.writeToCompoundTag(compoundTag,nodeType.name());
        NODE_MODIFIER_ACCESSOR.writeToCompoundTag(compoundTag,nodeModifier.name());
        NODE_CENTIVIS_BASE_ACCESSOR.writeToCompoundTag(compoundTag,centiVisBase);
        NODE_ASPECT_BASE_ACCESSOR.writeToCompoundTag(compoundTag,auraBase);
    }
    public void tick(){
        super.tick();
        if (Platform.getEnvironment() != Env.CLIENT){
            nodeType.nodeTypeTickEnergized(this);
        }
    }
    public void setupNode(){
        this.centiVisBase = new CentiVisList<>();
        var temp = ResearchManager.reduceToPrimals(auraBase, true);

        for(var aspectEntry : temp.aspectView.entrySet()) {
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
