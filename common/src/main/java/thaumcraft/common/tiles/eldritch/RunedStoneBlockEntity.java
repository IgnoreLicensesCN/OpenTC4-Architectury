package thaumcraft.common.tiles.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.fx.PacketFXBlockZapS2C;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class RunedStoneBlockEntity extends BlockEntity {
    public RunedStoneBlockEntity(BlockEntityType<RunedStoneBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public RunedStoneBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.RUNED_STONE, blockPos, blockState);
    }

    int count = 20;
    public void serverTick(){
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        this.count -= 1;
        if (this.count > 0){
            return;
        }
        this.count = 10 + this.level.random.nextInt(25);
        var pos = getBlockPos();
        Player p = this.level.getNearestPlayer(pos.getX() + 0.5F, pos.getY() + 0.5F,pos.getZ() + 0.5F, 3.0F, true);
        if (p != null) {
            p.hurt(this.level.damageSources().magic(),2.F);
            if (this.level.random.nextBoolean()) {
                Thaumcraft.addWarpToPlayer(p, 1 + this.level.random.nextInt(2), true);
            }

            new PacketFXBlockZapS2C(
                    pos.getX() + 0.5F,
                    pos.getY() + 0.5F,
                    pos.getZ() + 0.5F,
                    (float)p.position().x,
                    (float)p.getBoundingBox().minY + p.getEyeHeight(),
                    (float) p.position().z).sendToAllAround(serverLevel,pos,32.F*32);
        }
    }
}
