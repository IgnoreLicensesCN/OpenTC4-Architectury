package thaumcraft.common.tiles.abstracts.owned;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.HashSet;
import java.util.Set;

import static com.linearity.opentc4.Consts.ArcaneDoorBlockEntityTagAccessors.USERS;

public class KeyableOwnedBlockEntity extends OwnedBlockEntity implements IKeyAccessibleOwnedBlockEntity{

    public KeyableOwnedBlockEntity(BlockEntityType<? extends KeyableOwnedBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public KeyableOwnedBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.KEYABLE_OWNED(),blockPos,blockState);
    }

    public Set<String> users = new HashSet<>();

    @Override
    public boolean playerCanUseThis(Player player){
        return users.contains(player.getGameProfile().getName()) || playerOwnThis(player);
    }
    @Override
    public void addUser(Player player){
        users.add(player.getGameProfile().getName());
    }
    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        users.clear();
        users.addAll(USERS.readFromCompoundTag(compoundTag));
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        USERS.writeToCompoundTag(compoundTag, users);
    }

    @Override
    public String getKeyableName() {
        return getBlockState().getBlock().getName().getString();
    }
}
