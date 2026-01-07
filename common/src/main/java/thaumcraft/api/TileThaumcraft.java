package thaumcraft.api;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * @author azanor
 * <p>
 * Custom tile entity class I use for most of my tile entities. Setup in such a way that only
 * the nbt data within readCustomNBT / writeCustomNBT will be sent to the client when the tile
 * updates. Apart from all the normal TE data that gets sent that is.
 */
public class TileThaumcraft extends BlockEntity {
    protected final BlockEntityType<?> type;
    public TileThaumcraft(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.type = blockEntityType;
    }

    //NBT stuff

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        readCustomNBT(compoundTag);
    }

    public void readCustomNBT(CompoundTag compoundTag) {
        //TODO
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        writeCustomNBT(compoundTag);
    }

    public void writeCustomNBT(CompoundTag compoundTag) {
        //TODO
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        writeCustomNBT(tag);  // 只同步自定义 NBT
        return tag;
    }

    //Client Packet stuff
//    @Override
//    public Packet getDescriptionPacket() {
//        CompoundTag CompoundTag = new CompoundTag();
//        this.writeCustomNBT(CompoundTag);
//        return new S35PacketUpdateBlockEntity(this.xCoord, this.yCoord, this.zCoord, -999, CompoundTag);
//    }
//
//    @Override
//    public void onDataPacket(NetworkManager net, S35PacketUpdateBlockEntity pkt) {
//        super.onDataPacket(net, pkt);
//        this.readCustomNBT(pkt.func_148857_g());
//    }

    public void markDirtyAndUpdateSelf(){
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(),
                    this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void serverRandomTickByBlockHandle(){}
    public void clientTickByBlockHandle(){}

}
