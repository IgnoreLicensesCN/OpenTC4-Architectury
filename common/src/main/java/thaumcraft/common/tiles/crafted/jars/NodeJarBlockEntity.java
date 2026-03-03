package thaumcraft.common.tiles.crafted.jars;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectDisplayBlockEntity;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.lib.NodeInfo;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.NodeJarTagAccessors.NODE_INFO;

public class NodeJarBlockEntity extends TileThaumcraft
        implements IAspectDisplayBlockEntity<Aspect>,
        IBonusAspectOwnerItem<Aspect>
{
    public @NotNull NodeInfo nodeInfo = NodeInfo.EMPTY;

    public NodeJarBlockEntity(BlockEntityType<? extends NodeJarBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public NodeJarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.NODE_JAR, blockPos, blockState);
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.nodeInfo = NODE_INFO.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        NODE_INFO.writeToCompoundTag(compoundTag, nodeInfo);
    }

    @Override
    public AspectList<Aspect> getAspectsToDisplay() {
        return nodeInfo.nodeAspects;
    }

    @Override
    public AspectList<Aspect> getOwningBonusAspects(ItemStack stack) {
        return nodeInfo.nodeAspects;
    }
}
