package thaumcraft.common.tiles.crafted.jars;

import com.google.common.collect.MapMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.linearity.opentc4.Consts.BrainJarTagAccessors.EXP;

public class BrainJarBlockEntity extends TileThaumcraft implements IValueContainerBasedComparatorSignalProviderBlockEntity {
    public int xp = 0;
    public static final int EXP_CAPACITY = 2000;
    public int getExpCapacity(){
        return EXP_CAPACITY;
    }
    public int eatDelay = 0;
    public BrainJarBlockEntity(BlockEntityType<? extends BrainJarBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public BrainJarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BRAIN_JAR, blockPos, blockState);
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.xp = EXP.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        EXP.writeToCompoundTag(compoundTag, this.xp);

    }

    public static final double PLAYER_AND_EXP_DETECT_RANGE = 6.;
    public static final double EXP_CONSUME_RANGE = 6.;
    public double getPlayerAndExpDetectRange(){
        return PLAYER_AND_EXP_DETECT_RANGE;
    }
    public double getExpConsumeRange(){
        return EXP_CONSUME_RANGE;
    }

    @Override
    public int currentComparatorSignalValue() {
        return xp;
    }

    @Override
    public int comparatorSignalCapacity() {
        return getExpCapacity();
    }

    public static class ClientTickContext {
        private float wobble = 0;
        private float animationTicker = 0;
        private float targetYaw = 0;

        private float rota = 0;
        private float rotb = 0;
        public float getRota() {
            return rota;
        }
        public float getRotb() {
            return rotb;
        }
        long lastsigh = System.currentTimeMillis() + 1500L;
        private static final Map<BrainJarBlockEntity, ClientTickContext> contexts =
                new MapMaker().weakKeys().makeMap();
        public static void tickBE(BrainJarBlockEntity be){
            var ctx = contexts.computeIfAbsent(be,jarBE -> new ClientTickContext());

            ctx.rotb = ctx.rota;
            var center = be.getBlockPos().getCenter();
            Entity entity = be.getClosestXPOrb(be.getPlayerAndExpDetectRange());
            if (entity == null && be.level != null) {
                entity = be.level.getNearestPlayer(center.x,center.y,center.z, be.getPlayerAndExpDetectRange(),null);
                if (entity != null && ctx.lastsigh < System.currentTimeMillis()) {
                    be.level.playLocalSound(center.x,center.y,center.z, ThaumcraftSounds.BRAIN, SoundSource.BLOCKS, 0.15F, 0.8F + be.level.random.nextFloat() * 0.4F, false);
                    ctx.lastsigh = System.currentTimeMillis() + 5000L + be.level.random.nextInt(25000);
                }
            }

            if (entity != null) {
                double d = entity.getX() - center.x;
                double d1 = entity.getZ() - center.z;
                ctx.targetYaw = (float)Math.atan2(d1, d);
                ctx.animationTicker += 0.1F;
                var rand = be.level != null?be.level.random: RandomSource.createNewThreadLocalInstance();
                if (ctx.animationTicker < 0.5F || rand.nextInt(40) == 0) {
                    float f3 = ctx.wobble;

                    do {
                        ctx.wobble += (float)(rand.nextInt(4) - rand.nextInt(4));
                    } while(f3 == ctx.wobble);
                }
            } else {
                ctx.targetYaw += 0.01F;
            }

            ctx.rota = wrapToPi(ctx.rota);
            ctx.targetYaw = wrapToPi(ctx.targetYaw);
            float delta = wrapToPi(ctx.targetYaw - ctx.rota);

            ctx.rota += delta * 0.04F;
        }
        private static float wrapToPi(float angle) {
            angle = (float)(angle % (2. * Math.PI));
            if (angle >= Math.PI) {
                angle -= (float) (2. * Math.PI);
            }
            if (angle < -Math.PI) {
                angle += (float) (2. * Math.PI);
            }
            return angle;
        }
    }
    public void clientTick() {
        ClientTickContext.tickBE(this);
    }
    public void serverTick() {
        if (this.level == null) {
            return;
        }
        var capacity = getExpCapacity();
        if (this.xp > capacity) {
            this.xp = capacity;
        }

        if (this.xp < capacity) {
            var orb = this.getClosestXPOrb(getPlayerAndExpDetectRange());
            var center = getBlockPos().getCenter();
            if (orb != null && this.eatDelay == 0) {
                double var3 = (center.x - orb.getX()) / (double)7.0F;
                double var5 = (center.y - orb.getY()) / (double)7.0F;
                double var7 = (center.z - orb.getZ()) / (double)7.0F;
                double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
                double var11 = (double)1.0F - var9;
                if (var11 > (double)0.0F) {
                    var11 *= var11;
                    orb.addDeltaMovement(new Vec3(
                            var3 / var9 * var11 * 0.15,
                            var5 / var9 * var11 * 0.33,
                            var7 / var9 * var11 * 0.15
                            )
                    );
                }
            }
        }
        if (this.eatDelay > 0) {
            --this.eatDelay;
        } else if (this.xp < getExpCapacity()) {
            List<ExperienceOrb> list = new ArrayList<>(200);
            this.level.getEntities(
                    EntityTypeTest.forClass(ExperienceOrb.class),
                    new AABB(getBlockPos()).inflate(getExpConsumeRange()),
                    e -> true,
                    list,
                    200//dont get too much
            );
            list.forEach(orb -> {
                this.xp += orb.getValue();
                orb.discard();
                this.level.playSound(null,
                        getBlockPos(),
                        SoundEvents.GENERIC_EAT,
                        SoundSource.BLOCKS,
                        0.1F,
                        (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F + 1.0F);
            });
        }
    }


    public ExperienceOrb getClosestXPOrb(double range) {
        if (this.level == null){return null;}
        List<ExperienceOrb> list = new ArrayList<>(5);
        this.level.getEntities(
                EntityTypeTest.forClass(ExperienceOrb.class),
                new AABB(getBlockPos()).inflate(range),
                e -> true,
                list,
                5//dont get too much
        );
        list.sort(
                (o1,o2)
                        ->
                        Double.compare(
                                o2.distanceToSqr(getBlockPos().getCenter()),
                                o1.distanceToSqr(getBlockPos().getCenter())
                        )
        );
        if (list.isEmpty()) {
            return null;
        }
        return list.getFirst();
    }
}
