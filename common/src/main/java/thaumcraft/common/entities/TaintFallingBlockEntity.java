package thaumcraft.common.entities;

import com.linearity.opentc4.mixin.FallingBlockEntityAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import thaumcraft.common.ClientFXUtils;

public class TaintFallingBlockEntity extends FallingBlockEntity {
    public TaintFallingBlockEntity(Level world) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINT_FALLING_BLOCK(), world);
    }
    public TaintFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
        this.dropItem = false;
        disableDrop();
    }
    public TaintFallingBlockEntity(Level level, double d, double e, double f, BlockState blockState){
        this(level);
        ((FallingBlockEntityAccessor)this).setBlockState(blockState);
        this.blocksBuilding = true;
        this.setPos(d, e, f);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = d;
        this.yo = e;
        this.zo = f;
        this.setStartPos(this.blockPosition());
    }
    public TaintFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, Level level, double d, double e, double f, BlockState blockState){
        this(level, d, e, f, blockState);
    }

    @Override
    public void tick() {
        super.tick();
        var level = level();
        if (!level.isClientSide) {
            if (this.time == 1){
                if (this.getBlockState() != level.getBlockState(this.getStartPos())) {
                    discardAndDrop();//TODO:[maybe wont finished]Find out if this old design is reasonable.
                }
            }
        }else if (this.onGround() || this.time == 1) {
            for(int j = 0; j < 10; ++j) {
                ClientFXUtils.taintLandFX(this);
            }
        }
    }

    //actually we wont drop in tc4(i say there should be a same method for FallingBlockEntity)
    protected void discardAndDrop(){
        this.discard();
        if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            var block = this.getBlockState().getBlock();
            var blockPos = this.blockPosition();
            this.callOnBrokenAfterFall(block, blockPos);
            this.spawnAtLocation(block);
        }
    }
}
