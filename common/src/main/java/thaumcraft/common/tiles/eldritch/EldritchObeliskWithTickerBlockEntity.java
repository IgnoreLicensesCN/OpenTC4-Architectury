package thaumcraft.common.tiles.eldritch;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;
//TODO:Render
public class EldritchObeliskWithTickerBlockEntity extends TileThaumcraft {
    public EldritchObeliskWithTickerBlockEntity(BlockEntityType<EldritchObeliskWithTickerBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchObeliskWithTickerBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ELDRITCH_OBELISK_WITH_TICKER, blockPos, blockState);
    }
    public void serverTick() {
        if (this.level == null) {
            return;
        }
        var pos = this.getBlockPos();
        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();
        List<LivingEntity> list = EntityUtils.getEntitiesInRange(this.level,
                x + 0.5, y, z + 0.5, null, LivingEntity.class, 6.0F);
        for(LivingEntity living : list) {
            if (living instanceof IEldritchMob && !living.hasEffect(MobEffects.REGENERATION)) {
                living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40,0));
                living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40,0));
            }
        }
    }

    public void clientTick() {
        if (!(this.level instanceof ClientLevel clientLevel)) {
            return;
        }
        var pos = this.getBlockPos();
        var x = pos.getX() + .5;
        var y = pos.getY() + this.level.random.nextDouble() * 3;
        var z = pos.getZ() + .5;
        List<LivingEntity> list = EntityUtils.getEntitiesInRange(this.level,
                x + 0.5, y, z + 0.5, null, LivingEntity.class, 6.0F);
        for(LivingEntity living : list) {
            if (living instanceof IEldritchMob) {
                ClientFXUtils.wispFX4(
                        clientLevel,
                        x,y,z,
                        living,
                        5,
                        true,
                        1.f
                        );
            }
        }
    }
}
