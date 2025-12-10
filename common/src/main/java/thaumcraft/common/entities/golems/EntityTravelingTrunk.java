package thaumcraft.common.entities.golems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFood;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldServer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.events.EventHandlerEntity;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class EntityTravelingTrunk extends EntityLiving implements IEntityOwnable {
   public int slotCount = 27;
   public InventoryTrunk inventory;
   public float lidrot;
   public float field_768_a;
   public float field_767_b;
   private int jumpDelay;
   private int eatDelay;

   public EntityTravelingTrunk(World world) {
      super(world);
      this.inventory = new InventoryTrunk(this, this.slotCount);
      this.eatDelay = 0;
      this.jumpDelay = 0;
      this.preventEntitySpawning = true;
      this.jumpDelay = this.rand.nextInt(20) + 10;
      this.isImmuneToFire = true;
      this.fireResistance = 10;
      this.lidrot = 0.0F;
      this.func_110163_bv();
      this.setSize(0.8F, 0.8F);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(75.0F);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(15, (byte) 0);
      this.dataWatcher.addObject(16, (byte) 0);
      this.dataWatcher.addObject(17, "");
      this.dataWatcher.addObject(18, -1);
      this.dataWatcher.addObject(19, (byte) 0);
      this.dataWatcher.addObject(20, (short) 0);
   }

   public boolean attackEntityFrom(DamageSource ds, float par2) {
      if (ds == DamageSource.cactus) {
         return false;
      } else {
         return this.getUpgrade() != 3 && super.attackEntityFrom(ds, par2);
      }
   }

   public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      super.writeEntityToNBT(nbttagcompound);
      nbttagcompound.setBoolean("Stay", this.getStay());
      nbttagcompound.setByte("upgrade", (byte)this.getUpgrade());
      if (this.func_152113_b() == null) {
         nbttagcompound.setString("Owner", "");
      } else {
         nbttagcompound.setString("Owner", this.func_152113_b());
      }

      nbttagcompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
   }

   public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      super.readEntityFromNBT(nbttagcompound);
      this.setStay(nbttagcompound.getBoolean("Stay"));
      this.setUpgrade(nbttagcompound.getByte("upgrade"));
      String s = nbttagcompound.getString("Owner");
      if (!s.isEmpty()) {
         this.setOwner(s);
      }

      NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory", 10);
      this.inventory.readFromNBT(nbttaglist);
      this.setInvSize();
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.getUpgrade() == 5) {
         this.pullItems();
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (this.inWater) {
         this.motionY += 0.033F;
      }

      if ((Platform.getEnvironment() == Env.CLIENT)) {
         if (!this.onGround && this.motionY < (double)0.0F && !this.inWater) {
            this.lidrot += 0.015F;
         }

         if ((this.onGround || this.inWater) && !this.isOpen()) {
            this.lidrot -= 0.1F;
            if (this.lidrot < 0.0F) {
               this.lidrot = 0.0F;
            }
         }

         if (this.isOpen()) {
            this.lidrot += 0.035F;
         }

         if (this.lidrot > (this.isOpen() ? 0.5F : 0.2F)) {
            this.lidrot = this.isOpen() ? 0.5F : 0.2F;
         }
      } else if (this.getHealth() < this.getMaxHealth() && (this.getUpgrade() == 3 || this.ticksExisted % 50 == 0)) {
         this.heal(1.0F);
      }

   }

   protected void updateEntityActionState() {
      if (this.getAnger() > 0) {
         this.setAnger(this.getAnger() - 1);
      }

      if (this.eatDelay > 0) {
         --this.eatDelay;
      }

      this.fallDistance = 0.0F;
      if (this.getOwner() != null) {
         if (Platform.getEnvironment() != Env.CLIENT) {
            ArrayList<WeakReference<Entity>> ll = EventHandlerEntity.linkedEntities.get(this.getOwner().getCommandSenderName());
            if (ll == null) {
               ll = new ArrayList<>();
            }

            boolean add = true;

            for(WeakReference trunk : ll) {
               if (trunk.get() != null && ((Entity)trunk.get()).getEntityId() == this.getEntityId()) {
                  add = false;
                  break;
               }
            }

            if (add) {
               ll.add(new WeakReference(this));
               EventHandlerEntity.linkedEntities.put(this.getOwner().getCommandSenderName(), ll);
            }
         }

         if (!this.getStay() && this.getOwner() != null && this.getDistanceToEntity(this.getOwner()) > 20.0F) {
            int i = MathHelper.floor_double(this.getOwner().posX) - 2;
            int j = MathHelper.floor_double(this.getOwner().posZ) - 2;
            int k = MathHelper.floor_double(this.getOwner().boundingBox.minY);

            for(int l = 0; l <= 4; ++l) {
               for(int i1 = 0; i1 <= 4; ++i1) {
                  if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && (this.level().isBlockNormalCubeDefault(i + l, k - 1, j + i1, false) || this.level().getBlock(i + l, k - 1, j + i1).getMaterial() == Material.water) && !this.level().isBlockNormalCubeDefault(i + l, k, j + i1, false) && !this.level().isBlockNormalCubeDefault(i + l, k + 1, j + i1, false)) {
                     this.level().playSoundEffect((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, "mob.endermen.portal", 0.5F, 1.0F);
                     this.setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, this.rotationYaw, this.rotationPitch);
                     this.setAttackTarget(null);
                     return;
                  }
               }
            }
         }

         if (this.getAttackTarget() != null && this.getAttackTarget().isDead) {
            this.setAttackTarget(null);
            this.setAnger(5);
         }

         if (!this.getStay() && this.getUpgrade() == 2 && this.getAnger() == 0 && this.getAttackTarget() == null && this.getOwner() != null && this.getOwnerEntity().getAITarget() != null && !this.getOwnerEntity().getAITarget().isDead && this.getOwnerEntity().getAITarget() instanceof EntityLivingBase && this.canEntityBeSeen(this.getOwnerEntity().getAITarget())) {
            this.setAnger(600);
            this.setAttackTarget(this.getOwnerEntity().getAITarget());
         }

         boolean move = false;
         if (this.getAnger() > 0 && this.getAttackTarget() != null && !this.getAttackTarget().isDead && this.getAttackTarget() != this.getOwnerEntity()) {
            this.faceEntity(this.getAttackTarget(), 10.0F, 20.0F);
            move = true;
            if (this.attackTime <= 0 && (double)this.getDistanceToEntity(this.getAttackTarget()) < (double)1.5F && this.getAttackTarget().boundingBox.maxY > this.boundingBox.minY && this.getAttackTarget().boundingBox.minY < this.boundingBox.maxY) {
               this.attackTime = 10 + this.level().rand.nextInt(5);
               this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), 4.0F);
               this.level().setEntityState(this, (byte)17);
               this.level().playSoundAtEntity(this, "mob.blaze.hit", 0.5F, this.level().rand.nextFloat() * 0.1F + 0.9F);
            }
         }

         if (this.getOwner() != null && this.getDistanceToEntity(this.getOwner()) > 5.0F && this.getAnger() == 0 && !this.getStay()) {
            this.faceEntity(this.getOwner(), 10.0F, 20.0F);
            move = true;
         }

         if ((this.onGround || this.inWater) && this.jumpDelay-- <= 0 && move) {
            boolean fast = this.getUpgrade() == 0;
            this.jumpDelay = this.rand.nextInt(10) + 5;
            this.jumpDelay /= 3;
            this.isJumping = true;
            this.field_768_a = 1.0F;
            this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
            this.moveForward = fast ? 8.0F : 6.0F;
            if (this.inWater) {
               this.moveForward *= 0.75F;
            }

            this.jumpMovementFactor = fast ? 0.04F : 0.03F;
            this.level().playSoundAtEntity(this, "random.chestclosed", 0.1F, this.level().rand.nextFloat() * 0.1F + 0.9F);
         } else {
            this.isJumping = false;
            if (this.onGround) {
               this.moveStrafing = this.moveForward = 0.0F;
            }
         }
      }

   }

   protected boolean canDespawn() {
      return false;
   }

   public void onCollideWithPlayer(Player Player) {
   }

   protected String getHurtSound() {
      return Blocks.log.stepSound.getStepResourcePath();
   }

   protected String getDeathSound() {
      return "random.break";
   }

   protected Item getDropItem() {
       return super.getDropItem();
   }

   protected float getSoundVolume() {
      return 0.5F;
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      this.setInvSize();
      return super.onSpawnWithEgg(par1EntityLivingData);
   }

   public void setInvSize() {
      this.setRows(this.getUpgrade() == 1 ? 4 : 3);
      this.slotCount = this.getRows() * 9;
   }

   public boolean interact(Player player) {
      if (player.isSneaking()) {
         return false;
      } else {
         ItemStack itemstack = player.inventory.getCurrentItem();
         if (itemstack != null && itemstack.getItem() == ConfigItems.itemGolemBell) {
            return this.getUpgrade() == 3 && !this.func_152113_b().equals(player.getCommandSenderName());
         } else if (this.getUpgrade() == -1 && itemstack != null && itemstack.getItem() == ConfigItems.itemGolemUpgrade) {
            this.setUpgrade(itemstack.getItemDamage());
            this.setInvSize();
            --itemstack.stackSize;
            if (itemstack.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }

            this.level().playSoundAtEntity(this, "thaumcraft:upgrade", 0.5F, 1.0F);
            player.swingItem();
            return true;
         } else if (itemstack != null && itemstack.getItem() instanceof ItemFood && this.getHealth() < this.getMaxHealth()) {
            ItemFood itemfood = (ItemFood)itemstack.getItem();
            --itemstack.stackSize;
            this.heal((float)itemfood.func_150905_g(itemstack));
            if (this.getHealth() == this.getMaxHealth()) {
               this.level().playSoundAtEntity(this, "random.burp", 0.5F, this.level().rand.nextFloat() * 0.5F + 0.5F);
            } else {
               this.level().playSoundAtEntity(this, "random.eat", 0.5F, this.level().rand.nextFloat() * 0.5F + 0.5F);
            }

            this.level().setEntityState(this, (byte)18);
            this.lidrot = 0.15F;
            if (itemstack.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }

            return true;
         } else if (Platform.getEnvironment() != Env.CLIENT) {
            if (this.getUpgrade() == 3 && !this.func_152113_b().equals(player.getCommandSenderName())) {
               return true;
            } else {
               player.openGui(Thaumcraft.instance, 2, this.level(), this.getEntityId(), 0, 0);
               return false;
            }
         } else {
            return true;
         }
      }
   }

   void showHeartsOrSmokeFX(boolean flag) {
      String s = "heart";
      int amount = 1;
      if (!flag) {
         s = "explode";
         amount = 7;
      }

      for(int i = 0; i < amount; ++i) {
         double d = this.rand.nextGaussian() * 0.02;
         double d1 = this.rand.nextGaussian() * 0.02;
         double d2 = this.rand.nextGaussian() * 0.02;
         this.level().spawnParticle(s, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)0.5F + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d, d1, d2);
      }

   }

   private void pullItems() {
      if (!this.isDead && !(this.getHealth() <= 0.0F)) {
         List list = null;
         if (Platform.getEnvironment() != Env.CLIENT) {
            list = this.level().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(this.posX - (double)0.5F, this.posY - (double)0.5F, this.posZ - (double)0.5F, this.posX + (double)0.5F, this.posY + (double)0.5F, this.posZ + (double)0.5F));

             for (Object o : list) {
                 Entity entity = (Entity) o;
                 if (entity instanceof EntityItem) {
                     ItemStack stack = ((EntityItem) entity).getEntityItem().copy();
                     ItemStack outstack = InventoryUtils.placeItemStackIntoInventory(stack, this.inventory, 0, true);
                     if (outstack == null || outstack.stackSize != stack.stackSize) {
                         this.level().playSoundAtEntity(this, "random.eat", 0.5F, this.level().rand.nextFloat() * 0.5F + 0.5F);
                         this.level().setEntityState(this, (byte) 17);
                         if (outstack != null && outstack.stackSize >= 0) {
                             ((EntityItem) entity).setEntityItemStack(outstack);
                         } else {
                             entity.setDead();
                         }
                     }
                 }
             }
         }

         list = this.level().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(this.posX - (double)3.0F, this.posY - (double)3.0F, this.posZ - (double)3.0F, this.posX + (double)3.0F, this.posY + (double)3.0F, this.posZ + (double)3.0F));

          for (Object o : list) {
              Entity entity = (Entity) o;
              if (entity instanceof EntityItem) {
                  double d6 = entity.posX - this.posX;
                  double d8 = entity.posY - this.posY + (double) (this.height * 0.8F);
                  double d10 = entity.posZ - this.posZ;
                  double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                  d6 /= d11;
                  d8 /= d11;
                  d10 /= d11;
                  double d13 = 0.075;
                  entity.motionX -= d6 * d13;
                  entity.motionY -= d8 * d13;
                  entity.motionZ -= d10 * d13;
              }
          }

      }
   }

   public void onDeath(DamageSource par1DamageSource) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         this.inventory.dropAllItems();
      }

      super.onDeath(par1DamageSource);
   }

   public boolean canBreatheUnderwater() {
      return true;
   }

   public int getUpgrade() {
      return this.dataWatcher.getWatchableObjectByte(18);
   }

   public void setUpgrade(int upgrade) {
      this.dataWatcher.updateObject(18, (byte)upgrade);
   }

   public int getRows() {
      return this.dataWatcher.getWatchableObjectByte(19);
   }

   public void setRows(int rows) {
      this.dataWatcher.updateObject(19, (byte)rows);
   }

   public int getAnger() {
      return this.dataWatcher.getWatchableObjectShort(20);
   }

   public void setAnger(int anger) {
      this.dataWatcher.updateObject(20, (short)anger);
   }

   public boolean isOpen() {
      return this.dataWatcher.getWatchableObjectByte(15) == 1;
   }

   public void setOpen(boolean par1) {
      this.dataWatcher.updateObject(15, (byte) (par1 ? 1 : 0));
   }

   public boolean getStay() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   public void setStay(boolean par1) {
      this.dataWatcher.updateObject(16, (byte) (par1 ? 1 : 0));
   }

   public String func_152113_b() {
      return this.dataWatcher.getWatchableObjectString(17);
   }

   public void setOwner(String par1Str) {
      this.dataWatcher.updateObject(17, par1Str);
   }

   public Entity getOwner() {
      return this.getOwnerEntity();
   }

   public EntityLivingBase getOwnerEntity() {
      return this.level().getPlayerEntityByName(this.func_152113_b());
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if (par1 == 17) {
         this.lidrot = 0.15F;
      } else if (par1 == 18) {
         this.lidrot = 0.15F;
         this.showHeartsOrSmokeFX(true);
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public void travelToDimension(int par1) {
      if (!this.getStay() && !this.isDead && this.dimension != par1) {
         if (this.getOwner() == null) {
            try {
               MinecraftServer minecraftserver = MinecraftServer.getServer();
               WorldServer worldserver1 = minecraftserver.worldServerForDimension(par1);
               if (worldserver1 == null) {
                  return;
               }

               Entity target = worldserver1.getPlayerEntityByName(this.func_152113_b());
               if (target == null) {
                  return;
               }

               this.level().removeEntity(this);
               this.isDead = false;
               if (this.isEntityAlive()) {
                  this.level().updateEntityWithOptionalForce(this, false);
               }

               Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);
               if (entity != null) {
                  entity.copyDataFrom(this, true);
                  entity.setLocationAndAngles(target.posX, target.posY + (double)0.25F, target.posZ, entity.rotationYaw, entity.rotationPitch);
                  entity.dimension = par1;
                  worldserver1.spawnEntityInWorld(entity);
               }

               this.dimension = par1;
               this.isDead = true;
            } catch (Exception e) {
                Thaumcraft.log.error("Error while teleporting traveling trunk to dimension {}", par1);
               e.printStackTrace();
            }
         } else {
            super.travelToDimension(par1);
         }

      }
   }
}
