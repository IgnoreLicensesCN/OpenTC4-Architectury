package thaumcraft.common.entities.projectile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.damagesource.DamageSourceIndirectThaumcraftEntity;

public class EntityPrimalArrow extends EntityArrow implements IProjectile, IEntityAdditionalSpawnData {
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;
    private boolean inGround;
    public int ticksInGround;
    private int ticksInAir;
    private double damage;
    public int shootingEntityId;
    private int knockbackStrength;
    public int type;

    public void writeSpawnData(ByteBuf data) {
        data.writeDouble(this.motionX);
        data.writeDouble(this.motionY);
        data.writeDouble(this.motionZ);
        data.writeFloat(this.rotationYaw);
        data.writeFloat(this.rotationPitch);
        data.writeByte(this.type);
        data.writeInt(this.shootingEntityId);
    }

    public void readSpawnData(ByteBuf data) {
        this.motionX = data.readDouble();
        this.motionY = data.readDouble();
        this.motionZ = data.readDouble();
        this.rotationYaw = data.readFloat();
        this.rotationPitch = data.readFloat();
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.type = data.readByte();
        this.shootingEntityId = data.readInt();
    }

    public EntityPrimalArrow(World par1World) {
        super(par1World);
        this.inTile = Blocks.air;
        this.inData = 0;
        this.inGround = false;
        this.ticksInAir = 0;
        this.damage = 2.1;
        this.type = 0;
        this.renderDistanceWeight = 10.0F;
        this.setSize(0.5F, 0.5F);
    }

    public EntityPrimalArrow(World par1World, double par2, double par4, double par6) {
        super(par1World);
        this.inTile = Blocks.air;
        this.inData = 0;
        this.inGround = false;
        this.ticksInAir = 0;
        this.damage = 2.1;
        this.type = 0;
        this.renderDistanceWeight = 10.0F;
        this.setSize(0.25F, 0.25F);
        this.setPosition(par2, par4, par6);
        this.yOffset = 0.0F;
    }

    public EntityPrimalArrow(World par1World, EntityLivingBase par2EntityLivingBase, float par3, int type) {
        super(par1World);
        this.inTile = Blocks.air;
        this.inData = 0;
        this.inGround = false;
        this.ticksInAir = 0;
        this.damage = 2.1;
        this.type = 0;
        this.renderDistanceWeight = 10.0F;
        this.shootingEntity = par2EntityLivingBase;
        this.type = type;
        this.canBePickedUp = 0;
        this.shootingEntityId = this.shootingEntity.getEntityId();
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(par2EntityLivingBase.posX, par2EntityLivingBase.posY + (double) par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.posZ, par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.posY -= 0.10000000014901161;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        Vec3 vec3d = par2EntityLivingBase.getLook(1.0F);
        this.posX += vec3d.xCoord;
        this.posY += vec3d.yCoord;
        this.posZ += vec3d.zCoord;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * (double) 180.0F / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f) * (double) 180.0F / Math.PI);
        }

        Block i = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
        if (!i.isAir(this.worldObj, this.xTile, this.yTile, this.zTile)) {
            i.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = i.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            Block j = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
            int k = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
            if (j == this.inTile && k == this.inData) {
                ++this.ticksInGround;
                if (this.ticksInGround == 100) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec3, vec31, false, true, false);
            vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0F, 1.0F, 1.0F));
            double d0 = 0.0F;

            for (Object o : list) {
                Entity entity1 = (Entity) o;
                if (entity1.canBeCollidedWith() && (entity1.getEntityId() != this.shootingEntityId || this.ticksInAir >= 5)) {
                    float f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f1, f1, f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);
                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);
                        if (d1 < d0 || d0 == (double) 0.0F) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                    movingobjectposition = null;
                }
            }

            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    if (this.inflictDamage(movingobjectposition)) {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;
                            if (this.knockbackStrength > 0) {
                                float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                                if (f3 > 0.0F) {
                                    movingobjectposition.entityHit.addVelocity(this.motionX * (double) this.knockbackStrength * (double) 0.6F / (double) f3, 0.1, this.motionZ * (double) this.knockbackStrength * (double) 0.6F / (double) f3);
                                }
                            }

                            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, entitylivingbase);
                            }

                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                            }
                        }

                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    } else {
                        this.motionX *= -0.1F;
                        this.motionY *= -0.1F;
                        this.motionZ *= -0.1F;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                } else {
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (float) (movingobjectposition.hitVec.xCoord - this.posX);
                    this.motionY = (float) (movingobjectposition.hitVec.yCoord - this.posY);
                    this.motionZ = (float) (movingobjectposition.hitVec.zCoord - this.posZ);
                    float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) f2 * (double) 0.05F;
                    this.posY -= this.motionY / (double) f2 * (double) 0.05F;
                    this.posZ -= this.motionZ / (double) f2 * (double) 0.05F;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);
                    if (this.inTile.isAir(this.worldObj, this.xTile, this.yTile, this.zTile)) {
                        this.inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                    }
                }
            }

            if (this.getIsCritical()) {
                for (int var22 = 0; var22 < 4; ++var22) {
                    this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double) var22 / (double) 4.0F, this.posY + this.motionY * (double) var22 / (double) 4.0F, this.posZ + this.motionZ * (double) var22 / (double) 4.0F, -this.motionX, -this.motionY + 0.2, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * (double) 180.0F / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, f2) * (double) 180.0F / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f4 = 0.99F;
            float f1 = 0.05F;
            if (this.isInWater()) {
                for (int j1 = 0; j1 < 4; ++j1) {
                    float f3 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f3, this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3, this.motionX, this.motionY, this.motionZ);
                }

                f4 = 0.8F;
            }

            this.motionX *= f4;
            this.motionY *= f4;
            this.motionZ *= f4;
            this.motionY -= f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();
        }

    }

    public boolean inflictDamage(MovingObjectPosition movingobjectposition) {
        float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        int i1 = MathHelper.ceiling_double_int((double) f2 * this.getDamage());
        int fire = this.isBurning() && this.type != 2 ? 5 : 0;
        if (this.getIsCritical()) {
            i1 += this.rand.nextInt(i1 / 2 + 2);
        }

        DamageSource damagesource = null;
        switch (this.type) {
            case 0:
                if (this.shootingEntity == null) {
                    damagesource = (new DamageSourceIndirectThaumcraftEntity("airarrow", this, this)).setDamageBypassesArmor().setMagicDamage().setProjectile();
                } else {
                    damagesource = (new DamageSourceIndirectThaumcraftEntity("airarrow", this, this.shootingEntity)).setDamageBypassesArmor().setMagicDamage().setProjectile();
                }
                break;
            case 1:
                fire += 5;
                if (this.shootingEntity == null) {
                    damagesource = (new DamageSourceIndirectThaumcraftEntity("firearrow", this, this)).setFireDamage().setProjectile();
                } else {
                    damagesource = (new DamageSourceIndirectThaumcraftEntity("firearrow", this, this.shootingEntity)).setFireDamage().setProjectile();
                }
                break;
            case 2:
                if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                    ((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 4));
                }
            case 5:
                if (this.type == 5 && movingobjectposition.entityHit instanceof EntityLivingBase) {
                    ((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 100));
                }
            case 3:
            default:
                if (this.shootingEntity == null) {
                    damagesource = (new EntityDamageSourceIndirect("arrow", this, this)).setProjectile();
                } else {
                    damagesource = (new EntityDamageSourceIndirect("arrow", this, this.shootingEntity)).setProjectile();
                }
                break;
            case 4:
                if (this.shootingEntity == null) {
                    damagesource = (new DamageSourceIndirectThaumcraftEntity("orderarrow", this, this)).setDamageBypassesArmor().setMagicDamage().setProjectile();
                } else {
                    damagesource = (new DamageSourceIndirectThaumcraftEntity("orderarrow", this, this.shootingEntity)).setDamageBypassesArmor().setMagicDamage().setProjectile();
                }

                if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                    ((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 4));
                }
        }

        if (fire > 0 && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
            movingobjectposition.entityHit.setFire(fire);
        }

        return movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) i1);
    }

    public double getDamage() {
        switch (this.type) {
            case 3:
                return this.damage * (double) 1.5F;
            case 4:
                return this.damage * 0.8;
            case 5:
                return this.damage * 0.8;
            default:
                return this.damage;
        }
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setByte("type", (byte) this.type);
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.type = par1NBTTagCompound.getByte("type");
    }
}
