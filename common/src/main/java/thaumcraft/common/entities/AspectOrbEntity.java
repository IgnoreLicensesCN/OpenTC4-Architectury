package thaumcraft.common.entities;

import com.linearity.opentc4.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.common.lib.utils.InventoryUtils;

import static com.linearity.opentc4.Consts.AspectOrbEntityTagAccessors.*;

//changed:multiple aspect in a orb(performance could be better if anyone wants)
public class AspectOrbEntity extends Entity {
    public int orbAge = 0;
    public int orbMaxAge = 150;
    public int orbCooldown;
    protected int orbHealth = 5;
    protected Player closestPlayer;
    protected static final EntityDataAccessor<Integer> DATA_ID_COLOR = SynchedEntityData.defineId(AspectOrbEntity.class, EntityDataSerializers.INT);
    protected final AspectList<Aspect> owningAspect = new HashAspectList<>();

    public AspectOrbEntity(Level worldIn) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ASPECT_ORB(),worldIn);
    }
    public AspectOrbEntity(Level par1World, double par2, double par4, double par6, Aspect aspect, int par8) {
        this(par1World);
        this.setPos(par2, par4, par6);
        this.setYRot(random.nextFloat() * 360.0F);
        this.setDeltaMovement(
                random.nextFloat()*0.4F-0.2F,
                random.nextFloat()*0.4F,
                random.nextFloat()*0.4F-0.2F
        );
        this.addOwningAspect(aspect, par8);
    }
    public AspectOrbEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_COLOR, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        OWNING_ASPECTS.readFromCompoundTagInto(compoundTag, this.owningAspect);
        orbHealth = HEALTH.readIntFromCompoundTag(compoundTag);
        orbAge = AGE.readIntFromCompoundTag(compoundTag);
        refreshColor();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        OWNING_ASPECTS.writeToCompoundTag(compoundTag, this.owningAspect);
        HEALTH.writeIntToCompoundTag(compoundTag, this.orbHealth);
        AGE.writeIntToCompoundTag(compoundTag, this.orbAge);
    }

    public void addOwningAspect(Aspect aspectToAdd,int amountToAdd) {
        if (amountToAdd == 0){
            return;
        }
        int amountPrev = this.owningAspect.visSize();
        this.owningAspect.addAll(aspectToAdd, amountToAdd);

        var colorForNewAspect = new Color(aspectToAdd.color);
        var colorOld = new Color(aspectToAdd.color);
        var colorAverage = amountPrev==0?new Color(
                colorForNewAspect.r(),colorForNewAspect.g(),colorForNewAspect.b()
                ):new Color(
                        (colorForNewAspect.r() * amountToAdd + colorOld.r())/(amountPrev+amountToAdd),
                (colorForNewAspect.g() * amountToAdd + colorOld.g())/(amountPrev+amountToAdd),
                (colorForNewAspect.b() * amountToAdd + colorOld.b())/(amountPrev+amountToAdd)
        );
        this.entityData.set(DATA_ID_COLOR, colorAverage.getRGB());
    }
    public void takeOwningAspect(Aspect aspectToTake,int amountToTake) {
        if (amountToTake == 0){
            return;
        }
        this.owningAspect.reduceAndRemoveIfNotPositive(aspectToTake, amountToTake);
        refreshColor();
    }

    protected void refreshColor() {
        int[] rgb = {0,0,0};
        this.owningAspect.forEach((aspect,amount) -> {
            var color = new Color(aspect.color);
            rgb[0] += color.r() *amount;
            rgb[1] += color.g() *amount;
            rgb[2] += color.b() *amount;
        });
        rgb[0] /= owningAspect.visSize();
        rgb[1] /= owningAspect.visSize();
        rgb[2] /= owningAspect.visSize();

        this.entityData.set(DATA_ID_COLOR,new Color(rgb[0],rgb[1],rgb[2]).getRGB());
    }

    public AspectList<Aspect> getOwningAspect() {
        return this.owningAspect;
    }
    public int getColor() {
        return this.entityData.get(DATA_ID_COLOR);
    }

    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    private static final Vec3 FALL_VEC = new Vec3(0.0F, -0.03F, 0.0F);
    @Override
    public void tick() {
        if (this.owningAspect.isEmpty()){
            discard();
        }
        super.tick();
        if (this.orbCooldown > 0){
            this.orbCooldown--;
        }
        this.addDeltaMovement(FALL_VEC);
        var level = this.level();
        if (level.getBlockState(blockPosition()).getFluidState().is(FluidTags.LAVA)){
            this.setDeltaMovement(random.nextFloat()*0.4F-0.2F,0.2F,random.nextFloat()*0.4F-0.2F);
            this.playSound(SoundEvents.LAVA_EXTINGUISH, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }
        this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
        double d0 = 8.0F;
        var pos = position();
        if (this.tickCount % 5 == 0 && this.closestPlayer == null) {
            this.closestPlayer = this.level().getNearestPlayer(
                    pos.x,pos.y,pos.z,d0,p -> {
                        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p)){
                            return false;
                        }
                        if (!(p instanceof Player player)){
                            return false;
                        }

                        return owningAspect.forEachWithBreak((aspect,amount) -> {
                            var findingStack = InventoryUtils.isOwningCentiVisContainerWithRoom(aspect,amount,player);
                            return findingStack != null && !findingStack.isEmpty();
                        });
                    }
            );
        }

        if (this.closestPlayer != null) {
            var vecToPlayer  = this.closestPlayer.position().subtract(pos);
            double d4 = vecToPlayer.length();
            double d5 = (double)1.0F - d4;
            if (d5 > (double)0.0F) {
                d5 *= d5;
                this.addDeltaMovement(vecToPlayer.scale(d5*0.1/d4));
            }
        }

        this.move(MoverType.SELF,getDeltaMovement());
//        float f = 0.98F;
//        if (this.onGround()) {
//            f = 0.58800006F;
//            Block i = this.level().getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
//            if (!i.isAir(this.level(), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ))) {
//                f = i.slipperiness * 0.98F;
//            }
//        }
//        @StoleFrom("net.minecraft.world.entity.item.ItemEntity#tick()")
        if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            float g = 0.98F;
            if (this.onGround()) {
                g = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
            }

            this.setDeltaMovement(this.getDeltaMovement().multiply(g, 0.98, g));
            if (this.onGround()) {
                Vec3 vec32 = this.getDeltaMovement();
                if (vec32.y < 0.0) {
                    this.setDeltaMovement(vec32.multiply(1.0, -0.9, 1.0));
                }
            }
        }

        ++this.orbAge;
        if (this.orbAge >= this.orbMaxAge) {
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            this.markHurt();
            this.orbHealth = (int)((float)this.orbHealth - f);
            if (this.orbHealth <= 0) {
                this.discard();
            }

            return false;
        }
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);

        var level = level();
        if (!level.isClientSide){
            AspectList<Aspect> tookAspect = new HashAspectList<>();
            owningAspect.forEach((aspect,amount) -> {
                var stack = InventoryUtils.isOwningCentiVisContainerWithRoom(aspect, amount, player);
                if (this.orbCooldown == 0 && player.takeXpDelay == 0 && stack != null && stack.getItem() instanceof ICentiVisContainerItem<? extends Aspect> centiVisContainerNotCasted) {
                    var centiVisContainerCasted = (ICentiVisContainerItem<Aspect>) stack.getItem();
                    int remaining = centiVisContainerCasted.addCentiVis(stack, aspect, amount, true);
                    tookAspect.put(aspect,remaining);
                }
            });
            if (!tookAspect.isEmpty()){
                tookAspect.forEach((aspect,amount) -> {
                    if (amount == 0){
                        owningAspect.reduceAndRemoveIfNotPositive(aspect,amount);
                    }
                });
                if (owningAspect.isEmpty()){
                    this.discard();
                }
                this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.1F, 0.5F * ((this.random.nextFloat()*2-1) * 0.7F + 1.8F));
                player.takeXpDelay = 2;
            }
        }
    }
}
