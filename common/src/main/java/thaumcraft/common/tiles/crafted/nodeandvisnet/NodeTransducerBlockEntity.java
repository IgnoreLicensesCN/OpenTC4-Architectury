package thaumcraft.common.tiles.crafted.nodeandvisnet;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.crafted.noderelated.visnet.EnergizedAuraNodeBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;

import static com.linearity.opentc4.Consts.NodeTransducerBlockEntityTagAccessors.TRANSDUCER_STATUS_CODE;
import static com.linearity.opentc4.Consts.NodeTransducerBlockEntityTagAccessors.TRANSDUCER_TICK_COUNT;

public class NodeTransducerBlockEntity extends TileThaumcraft {
    public NodeTransducerBlockEntity(BlockEntityType<? extends NodeTransducerBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public NodeTransducerBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.NODE_TRANSDUCER,blockPos,blockState);
    }
    public static final int STATUS_CODE_OFF = 0;
    public static final int STATUS_CODE_SETTING_UP = 1;
    public static final int STATUS_CODE_ON = 2;
    public int tickCount = -1;
    public int statusCode = STATUS_CODE_OFF;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        tickCount = TRANSDUCER_TICK_COUNT.readFromCompoundTag(compoundTag);
        statusCode = TRANSDUCER_STATUS_CODE.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        TRANSDUCER_TICK_COUNT.writeToCompoundTag(compoundTag,tickCount);
        TRANSDUCER_STATUS_CODE.writeToCompoundTag(compoundTag,statusCode);
    }

    public void tick(){
        if (this.level == null){return;}
        if (tickCount < 0){
            checkStatus();
        }

        if (this.statusCode == STATUS_CODE_SETTING_UP 
                && Platform.getEnvironment() != Env.CLIENT 
                && this.tickCount >= 1000) {
            var probablyNodeBE = level.getBlockEntity(getNodePos());
            if (probablyNodeBE instanceof INodeBlockEntity auraNode) {
                AspectList<Aspect> base = auraNode.getAspectsBase();
                NodeType type = auraNode.getNodeType();
                NodeModifier mod = auraNode.getNodeModifier();
                var id = auraNode.getId();
                this.level.setBlockAndUpdate(getNodePos(), ThaumcraftBlocks.ENERGIZED_NODE.defaultBlockState());
                var be = this.level.getBlockEntity(getNodePos());
                if (be instanceof EnergizedAuraNodeBlockEntity energizedNode) {
                    energizedNode.setAuraBase(base.copy());
                    energizedNode.nodeType = type;
                    energizedNode.nodeModifier = mod;
                    energizedNode.id = id;
                    energizedNode.setupNode();
                }
                this.checkStatus();
                this.level.blockEvent(getBlockPos(),ThaumcraftBlocks.NODE_TRANSDUCER,10,10);
                this.markDirtyAndUpdateSelf();
            }
        }

        if (this.statusCode == STATUS_CODE_ON
                && Platform.getEnvironment() != Env.CLIENT 
                && this.tickCount <= 50) {
            var be = this.level.getBlockEntity(getNodePos());
            if (be instanceof EnergizedAuraNodeBlockEntity energizedNode) {
                var base = energizedNode.auraBase;
                var type = energizedNode.nodeType;
                var modifier = energizedNode.nodeModifier;
                this.level.setBlockAndUpdate(getNodePos(),ThaumcraftBlocks.AURA_NODE.defaultBlockState());
                var nodeBE = this.level.getBlockEntity(getNodePos());
                if (nodeBE instanceof AbstractNodeBlockEntity nodeBlockEntity){
                    nodeBlockEntity.setNodeType(type);
                    nodeBlockEntity.setNodeModifier(modifier);
                    nodeBlockEntity.setAspectsBase(base.copy());
                }
                this.statusCode = STATUS_CODE_OFF;
                this.level.blockEvent(getBlockPos(),ThaumcraftBlocks.NODE_TRANSDUCER,10,10);
                this.markDirtyAndUpdateSelf();
            }
        }

        if (this.statusCode != STATUS_CODE_OFF 
                && this.level.hasNeighborSignal(getBlockPos())) {
            if (this.tickCount < 1000) {
                ++this.tickCount;
                if (Platform.getEnvironment() != Env.CLIENT) {
                    var nodePos = getNodePos();
                    var probablyNodeBE = level.getBlockEntity(nodePos);
                    if (probablyNodeBE instanceof INodeBlockEntity auraNode) {
                        var aspectsCurrent = auraNode.getAspects();
                        if (!aspectsCurrent.isEmpty()) {
                            auraNode.takeFromContainer(aspectsCurrent.randomAspect(level.random),1);
                            if (this.tickCount % 5 == 0 || aspectsCurrent.visSize() == 0) {
                                var currentNodeState = level.getBlockState(nodePos);
                                this.level.sendBlockUpdated(nodePos,currentNodeState,currentNodeState,3);
                            }
                        }
                    }
                }

                if (this.tickCount > 50 && (Platform.getEnvironment() == Env.CLIENT)) {
                    var xCoord = getBlockPos().getX();
                    var yCoord = getBlockPos().getY();
                    var zCoord = getBlockPos().getZ();
                    if (this.level.random.nextBoolean() && this.level instanceof ClientLevel clientLevel) {
                        ClientFXUtils.nodeBolt(
                                clientLevel,
                                (float)xCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)yCoord + 0.5F,
                                (float)zCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)xCoord + 0.5F,
                                (float)yCoord - 0.5F,
                                (float)zCoord + 0.5F
                        );
                    }

                    if (this.level.random.nextBoolean() && this.hasValidStabilizer() && this.level instanceof ClientLevel clientLevel) {
                        ClientFXUtils.nodeBolt(
                                clientLevel,
                                (float)xCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)yCoord - 1.5F,
                                (float)zCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)xCoord + 0.5F,
                                (float)yCoord - 0.5F,
                                (float)zCoord + 0.5F
                        );
                    }
                }
            }
        } else if (this.tickCount > 0) {
            --this.tickCount;
            if (this.tickCount > 50 && (Platform.getEnvironment() == Env.CLIENT)) {
                var xCoord = getBlockPos().getX();
                var yCoord = getBlockPos().getY();
                var zCoord = getBlockPos().getZ();
                if (this.level instanceof ClientLevel clientLevel) {
                    if (this.level.random.nextBoolean()) {
                        ClientFXUtils.nodeBolt(
                                clientLevel,
                                (float)xCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)yCoord + 0.5F,
                                (float)zCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)xCoord + 0.5F,
                                (float)yCoord - 0.5F,
                                (float)zCoord + 0.5F
                        );
                    }

                    if (this.level.random.nextBoolean() && this.hasValidStabilizer()) {
                        ClientFXUtils.nodeBolt(
                                clientLevel,
                                (float)xCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)yCoord - 1.5F,
                                (float)zCoord + 0.25F + this.level.random.nextFloat() * 0.5F,
                                (float)xCoord + 0.5F,
                                (float)yCoord - 0.5F,
                                (float)zCoord + 0.5F
                        );
                    }
                }
            }
        }

        if (this.tickCount > 1000) {
            this.tickCount = 1000;
        }
    }
    public BlockPos getNodePos(){
        return getBlockPos().below();
    }
    public BlockPos getStabilizerPos(){
        return getNodePos().below();
    }
    public boolean hasValidStabilizer(){
        if (level == null) return false;
        var stabilizerPos = getStabilizerPos();
        return !level.hasNeighborSignal(stabilizerPos) && level.getBlockState(stabilizerPos).getBlock() instanceof INodeLockBlock;
    }
    public boolean hasEnergizedNode(){
        if (level == null){return false;}
        return level.getBlockState(getNodePos()).getBlock() instanceof EnergizedAuraNodeBlock;
    }
    public boolean hasValidAuraNode(){
        if (level == null){return false;}
        var nodePos = getNodePos();
        return level.getBlockState(nodePos).getBlock() instanceof INodeBlock nodeBlock 
                && nodeBlock.canBeConvertedToEnergizedNode(level,nodePos);
    }
    public boolean hasSignal(){
        if (level == null) return false;
        return level.hasNeighborSignal(getBlockPos());
    }
    public void checkStatus() {
        if (this.level == null){return;}
        if (this.tickCount == -1) {
            this.tickCount = 0;
        }

        if (this.statusCode != STATUS_CODE_ON 
                || this.tickCount <= 50 || (this.hasValidStabilizer() && hasEnergizedNode())) 
        {
            if (this.hasSignal() && this.hasValidAuraNode()) {
                this.statusCode = STATUS_CODE_SETTING_UP;
                this.markDirtyAndUpdateSelf();
            } else if (this.hasEnergizedNode()) {
                this.statusCode = STATUS_CODE_ON;
                this.tickCount = 1000;
                this.markDirtyAndUpdateSelf();
            } else {
                this.statusCode = STATUS_CODE_OFF;
            }
        } else if (level instanceof ServerLevel serverLevel){
            EnergizedAuraNodeBlock.explode(serverLevel, getNodePos());
            this.statusCode = STATUS_CODE_OFF;
            this.tickCount = 50;
            this.markDirtyAndUpdateSelf();
        }

    }
}
