package thaumcraft.common.tiles.abstracts.owned;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.Consts.OwnedBlockEntityTagAccessors.OWNERS_ACCESSOR;

public class OwnedBlockEntity extends TileThaumcraft implements IOwnedBlockEntity {
    public OwnedBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public OwnedBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.OWNED(), blockPos, blockState);
    }
    private final Set<String> owners = ConcurrentHashMap.newKeySet();

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        owners.addAll(OWNERS_ACCESSOR.readFromCompoundTag(compoundTag));
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        OWNERS_ACCESSOR.writeToCompoundTag(compoundTag, owners);
    }

    @Override
    public boolean playerOwnThis(Player player){
        if (player == null) {
            return false;
        }
        if (owners.isEmpty()){
            OpenTC4.LOGGER.warn(
                    "OwnedBE owner is empty:"
                            + getBlockPos() + " "
                            + (level == null ? "level:null" : level.dimension().registry().toString())
            );
            return true;
        }
        return owners.contains(player.getUUID().toString());
    }

    @Override
    public void addOwner(Player player){
        owners.add(player.getUUID().toString());
    }
}
