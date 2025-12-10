package thaumcraft.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import thaumcraft.api.IRepairable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.lib.utils.Utils;

import java.util.List;
import java.util.Objects;

public class ItemElementalSword extends ItemSword implements IRepairable {
    public IIcon icon;

    public ItemElementalSword(Item.ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(Thaumcraft.tabTC);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("thaumcraft:elementalsword");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icon;
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.rare;
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 2)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        super.onUsingTick(stack, player, count);
        int ticks = this.getMaxItemUseDuration(stack) - count;
        if (player.motionY < (double) 0.0F) {
            player.motionY /= 1.2F;
            player.fallDistance /= 1.2F;
        }

        player.motionY += 0.08F;
        if (player.motionY > (double) 0.5F) {
            player.motionY = 0.2F;
        }

        if (player instanceof EntityPlayerMP) {
            Utils.resetFloatCounter((EntityPlayerMP) player);
        }

        List targets = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(2.5F, 2.5F, 2.5F));
        if (!targets.isEmpty()) {
            for (Object target : targets) {
                Entity entity = (Entity) target;
                if (!(entity instanceof EntityPlayer) && !entity.isDead && (player.ridingEntity == null || player.ridingEntity != entity)) {
                    Vec3 p = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
                    Vec3 t = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
                    double distance = p.distanceTo(t) + 0.1;
                    Vec3 r = Vec3.createVectorHelper(t.xCoord - p.xCoord, t.yCoord - p.yCoord, t.zCoord - p.zCoord);
                    entity.motionX += r.xCoord / (double) 2.5F / distance;
                    entity.motionY += r.yCoord / (double) 2.5F / distance;
                    entity.motionZ += r.zCoord / (double) 2.5F / distance;
                }
            }
        }

        if (player.worldObj.isRemote) {
            int miny = (int) (player.boundingBox.minY - (double) 2.0F);
            if (player.onGround) {
                miny = MathHelper.floor_double(player.boundingBox.minY);
            }

            for (int a = 0; a < 5; ++a) {
                Thaumcraft.proxy.smokeSpiral(player.worldObj, player.posX, player.boundingBox.minY + (double) (player.height / 2.0F), player.posZ, 1.5F, player.worldObj.rand.nextInt(360), miny, 14540253);
            }

            if (player.onGround) {
                float r1 = player.worldObj.rand.nextFloat() * 360.0F;
                float mx = -MathHelper.sin(r1 / 180.0F * (float) Math.PI) / 5.0F;
                float mz = MathHelper.cos(r1 / 180.0F * (float) Math.PI) / 5.0F;
                player.worldObj.spawnParticle("smoke", player.posX, player.boundingBox.minY + (double) 0.1F, player.posZ, mx, 0.0F, mz);
            }
        } else if (ticks % 20 == 0) {
            player.worldObj.playSoundAtEntity(player, "thaumcraft:wind", 0.5F, 0.9F + player.worldObj.rand.nextFloat() * 0.2F);
        }

        if (ticks % 20 == 0) {
            stack.damageItem(1, player);
        }

    }

    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (entity.isEntityAlive()) {
            List<Entity> targets = (List<Entity>) player.worldObj.getEntitiesWithinAABBExcludingEntity(player, entity.boundingBox.expand(1.2, 1.1, 1.2));
            int count = 0;
            if (targets.size() > 1) {
                for (Entity target : targets) {
                    if (!target.isDead
                            && (!(target instanceof EntityGolemBase) || !Objects.equals(((EntityGolemBase) target).getOwnerName(), player.getCommandSenderName()))
                            && (!(target instanceof EntityTameable) || !Objects.equals(((EntityTameable) target).func_152113_b(), player.getCommandSenderName()))
                            && target instanceof EntityLiving
                            && target.getEntityId() != entity.getEntityId()) {
                        if (target.isEntityAlive()) {
                            this.attackTargetEntityWithCurrentItem(target, player);
                            ++count;
                        }
                    }
                }

                if (count > 0 && !player.worldObj.isRemote) {
                    player.worldObj.playSoundAtEntity(entity, "thaumcraft:swing", 1.0F, 0.9F + player.worldObj.rand.nextFloat() * 0.2F);
                }
            }
        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    public void attackTargetEntityWithCurrentItem(Entity par1Entity, EntityPlayer player) {
        if (!MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player, par1Entity))) {
            if (par1Entity.canAttackWithItem() && !par1Entity.hitByEntity(player)) {
                float f = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                int i = 0;
                float f1 = 0.0F;
                if (par1Entity instanceof EntityLivingBase) {
                    f1 = EnchantmentHelper.getEnchantmentModifierLiving(player, (EntityLivingBase) par1Entity);
                    i += EnchantmentHelper.getKnockbackModifier(player, (EntityLivingBase) par1Entity);
                }

                if (player.isSprinting()) {
                    ++i;
                }

                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null && par1Entity instanceof EntityLivingBase;
                    if (flag && f > 0.0F) {
                        f *= 1.5F;
                    }

                    f += f1;
                    boolean flag1 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(player);
                    if (par1Entity instanceof EntityLivingBase && j > 0 && !par1Entity.isBurning()) {
                        flag1 = true;
                        par1Entity.setFire(1);
                    }

                    boolean flag2 = par1Entity.attackEntityFrom(DamageSource.causePlayerDamage(player), f);
                    if (flag2) {
                        if (i > 0) {
                            par1Entity.addVelocity(-MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F, 0.1, MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F);
                            player.motionX *= 0.6;
                            player.motionZ *= 0.6;
                            player.setSprinting(false);
                        }

                        if (flag) {
                            player.onCriticalHit(par1Entity);
                        }

                        if (f1 > 0.0F) {
                            player.onEnchantmentCritical(par1Entity);
                        }

                        if (f >= 18.0F) {
                            player.triggerAchievement(AchievementList.overkill);
                        }

                        player.setLastAttacker(par1Entity);
                        if (par1Entity instanceof EntityLivingBase) {
                            EnchantmentHelper.func_151384_a((EntityLivingBase) par1Entity, player);
                        }
                    }

                    ItemStack itemstack = player.getCurrentEquippedItem();
                    Object object = par1Entity;
                    if (par1Entity instanceof EntityDragonPart) {
                        IEntityMultiPart ientitymultipart = ((EntityDragonPart) par1Entity).entityDragonObj;
                        if (ientitymultipart instanceof EntityLivingBase) {
                            object = ientitymultipart;
                        }
                    }

                    if (itemstack != null && object instanceof EntityLivingBase) {
                        itemstack.hitEntity((EntityLivingBase) object, player);
                        if (itemstack.stackSize <= 0) {
                            player.destroyCurrentEquippedItem();
                        }
                    }

                    if (par1Entity instanceof EntityLivingBase) {
                        player.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
                        if (j > 0 && flag2) {
                            par1Entity.setFire(j * 4);
                        } else if (flag1) {
                            par1Entity.extinguish();
                        }
                    }

                    player.addExhaustion(0.3F);
                }
            }

        }
    }
}
