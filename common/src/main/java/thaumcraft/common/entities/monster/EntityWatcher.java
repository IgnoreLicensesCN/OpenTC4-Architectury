package thaumcraft.common.entities.monster;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.common.entities.ai.misc.AIWander;

public class EntityWatcher extends EntityMob {
    private float field_175482_b;
    private float field_175484_c;
    private float field_175483_bk;
    private float field_175485_bl;
    private float field_175486_bm;
    private EntityLivingBase field_175478_bn;
    private int field_175479_bo;
    private boolean field_175480_bp;
    private AIWander wander;
    private EntityMoveHelper moveHelper;
    private GuardianLookHelper lookHelper;
    IEntitySelector mobSelector = ent -> true;

    public EntityWatcher(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.85F, 0.85F);
        this.tasks.addTask(4, new AIGuardianAttack());
        EntityAIMoveTowardsRestriction entityaimovetowardsrestriction;
        this.tasks.addTask(5, entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0F));
        this.tasks.addTask(7, this.wander = new AIWander(this, 1.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityWatcher.class, 12.0F, 0.01F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.wander.setMutexBits(3);
        entityaimovetowardsrestriction.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false));
        this.lookHelper = new GuardianLookHelper(this);
        this.moveHelper = new GuardianMoveHelper();
        this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0F);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5F);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0F);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0F);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
    }

    @Override
    public GuardianLookHelper getLookHelper() {
        return this.lookHelper;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(17, 0);
    }

    private boolean getFlags(int p_175468_1_) {
        return (this.dataWatcher.getWatchableObjectInt(16) & p_175468_1_) != 0;
    }

    private void setFlags(int p_175473_1_, boolean p_175473_2_) {
        int j = this.dataWatcher.getWatchableObjectInt(16);
        if (p_175473_2_) {
            this.dataWatcher.updateObject(16, j | p_175473_1_);
        } else {
            this.dataWatcher.updateObject(16, j & ~p_175473_1_);
        }

    }

    public boolean isGazing() {
        return this.getFlags(2);
    }

    private void setGazing(boolean p_175476_1_) {
        this.setFlags(2, p_175476_1_);
    }

    public int func_175464_ck() {
        return this.func_175461_cl() ? 60 : 80;
    }

    public boolean func_175461_cl() {
        return this.getFlags(4);
    }

    private void func_175463_b(int p_175463_1_) {
        this.dataWatcher.updateObject(17, p_175463_1_);
    }

    public boolean func_175474_cn() {
        return this.dataWatcher.getWatchableObjectInt(17) != 0;
    }

    public EntityLivingBase getTargetedEntity() {
        if (!this.func_175474_cn()) {
            return null;
        } else if (this.worldObj.isRemote) {
            if (this.field_175478_bn != null) {
                return this.field_175478_bn;
            } else {
                Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
                if (entity instanceof EntityLivingBase) {
                    this.field_175478_bn = (EntityLivingBase) entity;
                    return this.field_175478_bn;
                } else {
                    return null;
                }
            }
        } else {
            return this.getAttackTarget();
        }
    }

    @Override
    public void func_145781_i(int p_145781_1_) {
        super.func_145781_i(p_145781_1_);
        if (p_145781_1_ == 16) {
            if (this.func_175461_cl() && this.width < 1.0F) {
                this.setSize(1.9975F, 1.9975F);
            }
        } else if (p_145781_1_ == 17) {
            this.field_175479_bo = 0;
            this.field_175478_bn = null;
        }

    }

    @Override
    public int getTalkInterval() {
        return 160;
    }

    @Override
    protected String getLivingSound() {
        return !this.isInWater() ? "mob.guardian.land.idle" : (this.func_175461_cl() ? "mob.guardian.elder.idle" : "mob.guardian.idle");
    }

    @Override
    protected String getHurtSound() {
        return !this.isInWater() ? "mob.guardian.land.hit" : (this.func_175461_cl() ? "mob.guardian.elder.hit" : "mob.guardian.hit");
    }

    @Override
    protected String getDeathSound() {
        return !this.isInWater() ? "mob.guardian.land.death" : (this.func_175461_cl() ? "mob.guardian.elder.death" : "mob.guardian.death");
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5F;
    }

    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        return this.worldObj.isAirBlock(x, y, z) ? 10.0F : super.getBlockPathWeight(x, y, z);
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.field_175484_c = this.field_175482_b;
            if (this.isGazing()) {
                if (this.field_175483_bk < 0.5F) {
                    this.field_175483_bk = 4.0F;
                } else {
                    this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;
                }
            } else {
                this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
            }

            this.field_175482_b += this.field_175483_bk;
            this.field_175486_bm = this.field_175485_bl;
            if (this.isGazing()) {
                this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
            } else {
                this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
            }

            if (this.isGazing()) {
                Vec3 vec3 = this.getLook(0.0F);

                for (int i = 0; i < 2; ++i) {
                    this.worldObj.spawnParticle("bubble", this.posX + (this.rand.nextDouble() - (double) 0.5F) * (double) this.width - vec3.xCoord * (double) 1.5F, this.posY + this.rand.nextDouble() * (double) this.height - vec3.yCoord * (double) 1.5F, this.posZ + (this.rand.nextDouble() - (double) 0.5F) * (double) this.width - vec3.zCoord * (double) 1.5F, 0.0F, 0.0F, 0.0F);
                }
            }

            if (this.func_175474_cn()) {
                if (this.field_175479_bo < this.func_175464_ck()) {
                    ++this.field_175479_bo;
                }

                EntityLivingBase entitylivingbase = this.getTargetedEntity();
                if (entitylivingbase != null) {
                    this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
                    this.getLookHelper().onUpdateLook();
                    double d5 = this.func_175477_p(0.0F);
                    double d0 = entitylivingbase.posX - this.posX;
                    double d1 = entitylivingbase.posY + (double) (entitylivingbase.height * 0.5F) - (this.posY + (double) this.getEyeHeight());
                    double d2 = entitylivingbase.posZ - this.posZ;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    double d4 = this.rand.nextDouble();

                    while (d4 < d3) {
                        d4 += 1.8 - d5 + this.rand.nextDouble() * (1.7 - d5);
                        this.worldObj.spawnParticle("bubble", this.posX + d0 * d4, this.posY + d1 * d4 + (double) this.getEyeHeight(), this.posZ + d2 * d4, 0.0F, 0.0F, 0.0F);
                    }
                }
            }
        }

        if (this.func_175474_cn()) {
            this.rotationYaw = this.rotationYawHead;
        }

        super.onLivingUpdate();
    }

    @SideOnly(Side.CLIENT)
    public float func_175471_a(float p_175471_1_) {
        return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
    }

    @SideOnly(Side.CLIENT)
    public float func_175469_o(float p_175469_1_) {
        return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
    }

    public float func_175477_p(float p_175477_1_) {
        return ((float) this.field_175479_bo + p_175477_1_) / (float) this.func_175464_ck();
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.func_175461_cl() && !this.hasHome()) {
            this.setHomeArea((int) this.posX, (int) this.posY, (int) this.posZ, 16);
        }

    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.isGazing() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) source.getSourceOfDamage();
            if (!source.isExplosion()) {
                entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
                entitylivingbase.playSound("damage.thorns", 0.5F, 1.0F);
            }
        }

        this.wander.setWander();
        return super.attackEntityFrom(source, amount);
    }

    public int getVerticalFaceSpeed() {
        return 180;
    }

    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        this.moveFlying(p_70612_1_, p_70612_2_, 0.1F);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9F;
        this.motionY *= 0.9F;
        this.motionZ *= 0.9F;
    }

    class AIGuardianAttack extends EntityAIBase {
        private EntityWatcher field_179456_a = EntityWatcher.this;
        private int field_179455_b;
        private static final String __OBFID = "CL_00002211";

        public AIGuardianAttack() {
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.field_179456_a.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        @Override
        public boolean continueExecuting() {
            return super.continueExecuting() && (this.field_179456_a.func_175461_cl() || this.field_179456_a.getDistanceSqToEntity(this.field_179456_a.getAttackTarget()) > (double) 9.0F);
        }

        @Override
        public void startExecuting() {
            this.field_179455_b = -10;
            this.field_179456_a.getNavigator().clearPathEntity();
            this.field_179456_a.getLookHelper().setLookPositionWithEntity(this.field_179456_a.getAttackTarget(), 90.0F, 90.0F);
            this.field_179456_a.isAirBorne = true;
        }

        @Override
        public void resetTask() {
            this.field_179456_a.func_175463_b(0);
            this.field_179456_a.setAttackTarget(null);
            this.field_179456_a.wander.setWander();
        }

        @Override
        public void updateTask() {
            EntityLivingBase entitylivingbase = this.field_179456_a.getAttackTarget();
            this.field_179456_a.getNavigator().clearPathEntity();
            this.field_179456_a.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
            if (!this.field_179456_a.canEntityBeSeen(entitylivingbase)) {
                this.field_179456_a.setAttackTarget(null);
            } else {
                ++this.field_179455_b;
                if (this.field_179455_b == 0) {
                    this.field_179456_a.func_175463_b(this.field_179456_a.getAttackTarget().getEntityId());
                    this.field_179456_a.worldObj.setEntityState(this.field_179456_a, (byte) 21);
                } else if (this.field_179455_b >= this.field_179456_a.func_175464_ck()) {
                    float f = 1.0F;
                    if (this.field_179456_a.worldObj.difficultySetting == EnumDifficulty.HARD) {
                        f += 2.0F;
                    }

                    if (this.field_179456_a.func_175461_cl()) {
                        f += 2.0F;
                    }

                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.field_179456_a, this.field_179456_a), f);
                    entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.field_179456_a), (float) this.field_179456_a.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
                    this.field_179456_a.setAttackTarget(null);
                }
//                else if (this.field_179455_b >= 60 && this.field_179455_b % 20 == 0) {
//                }

                super.updateTask();
            }

        }
    }

    public static class GuardianLookHelper extends EntityLookHelper {
        public GuardianLookHelper(EntityLiving p_i1613_1_) {
            super(p_i1613_1_);
        }

        double getX() {
            try {
                return ReflectionHelper.getPrivateValue(GuardianLookHelper.class, this, new String[]{"posX", "field_75656_e"});
            } catch (Exception var2) {
                return 0.0F;
            }
        }

        double getY() {
            try {
                return ReflectionHelper.getPrivateValue(GuardianLookHelper.class, this, new String[]{"posY", "field_75653_f"});
            } catch (Exception var2) {
                return 0.0F;
            }
        }

        double getZ() {
            try {
                return ReflectionHelper.getPrivateValue(GuardianLookHelper.class, this, new String[]{"posZ", "field_75654_g"});
            } catch (Exception var2) {
                return 0.0F;
            }
        }

        boolean getLooking() {
            try {
                return ReflectionHelper.getPrivateValue(GuardianLookHelper.class, this, new String[]{"isLooking", "field_75655_d"});
            } catch (Exception var2) {
                return false;
            }
        }
    }

    class GuardianMoveHelper extends EntityMoveHelper {
        private EntityWatcher field_179930_g = EntityWatcher.this;

        public GuardianMoveHelper() {
            super(EntityWatcher.this);
        }

        double getX() {
            try {
                return ReflectionHelper.getPrivateValue(GuardianMoveHelper.class, this, new String[]{"posX", "field_75646_b"});
            } catch (Exception var2) {
                return 0.0F;
            }
        }

        double getY() {
            try {
                return ReflectionHelper.getPrivateValue(GuardianMoveHelper.class, this, new String[]{"posY", "field_75647_c"});
            } catch (Exception var2) {
                return 0.0F;
            }
        }

        double getZ() {
            try {
                return ReflectionHelper.getPrivateValue(GuardianMoveHelper.class, this, new String[]{"posZ", "field_75644_d"});
            } catch (Exception var2) {
                return 0.0F;
            }
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.isUpdating() && !this.field_179930_g.getNavigator().noPath()) {
                double d0 = this.getX() - this.field_179930_g.posX;
                double d1 = this.getY() - this.field_179930_g.posY;
                double d2 = this.getZ() - this.field_179930_g.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt_double(d3);
                d1 /= d3;
                float f = (float) (Math.atan2(d2, d0) * (double) 180.0F / Math.PI) - 90.0F;
                this.field_179930_g.rotationYaw = this.limitAngle(this.field_179930_g.rotationYaw, f, 30.0F);
                this.field_179930_g.renderYawOffset = this.field_179930_g.rotationYaw;
                float f1 = (float) (this.getSpeed() * this.field_179930_g.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                this.field_179930_g.setAIMoveSpeed(this.field_179930_g.getAIMoveSpeed() + (f1 - this.field_179930_g.getAIMoveSpeed()) * 0.125F);
                double d4 = Math.sin((double) (this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * (double) 0.5F) * 0.05;
                double d5 = Math.cos(this.field_179930_g.rotationYaw * (float) Math.PI / 180.0F);
                double d6 = Math.sin(this.field_179930_g.rotationYaw * (float) Math.PI / 180.0F);
                EntityWatcher watcher = this.field_179930_g;
                watcher.motionX += d4 * d5;
                watcher.motionZ += d4 * d6;
                d4 = Math.sin((double) (this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * (double) 0.75F) * 0.05;
                watcher = this.field_179930_g;
                watcher.motionY += d4 * (d6 + d5) * (double) 0.25F;
                watcher.motionY += (double) this.field_179930_g.getAIMoveSpeed() * d1 * 0.1;

                GuardianLookHelper entitylookhelper = this.field_179930_g.getLookHelper();
                double d7 = this.field_179930_g.posX + d0 / d3 * (double) 2.0F;
                double d8 = (double) this.field_179930_g.getEyeHeight() + this.field_179930_g.posY + d1 / d3;
                double d9 = this.field_179930_g.posZ + d2 / d3 * (double) 2.0F;
                double d10 = entitylookhelper.getX();
                double d11 = entitylookhelper.getY();
                double d12 = entitylookhelper.getZ();
                if (!entitylookhelper.getLooking()) {
                    d10 = d7;
                    d11 = d8;
                    d12 = d9;
                }

                this.field_179930_g.getLookHelper().setLookPosition(d10 + (d7 - d10) * (double) 0.125F, d11 + (d8 - d11) * (double) 0.125F, d12 + (d9 - d12) * (double) 0.125F, 10.0F, 40.0F);
                this.field_179930_g.setGazing(true);
            } else {
                this.field_179930_g.setAIMoveSpeed(0.0F);
                this.field_179930_g.setGazing(false);
            }

        }

        private float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
            float f3 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
            if (f3 > p_75639_3_) {
                f3 = p_75639_3_;
            }

            if (f3 < -p_75639_3_) {
                f3 = -p_75639_3_;
            }

            return p_75639_1_ + f3;
        }
    }
}
