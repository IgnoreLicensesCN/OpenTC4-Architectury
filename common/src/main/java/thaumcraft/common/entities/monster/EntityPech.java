package thaumcraft.common.entities.monster;

import com.linearity.opentc4.simpleutils.SimplePair;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemNameTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.pech.AIPechItemEntityGoto;
import thaumcraft.common.entities.ai.pech.AIPechTradePlayer;
import thaumcraft.common.entities.projectile.EntityPechBlast;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.misc.ItemManaBean;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.*;

//TODO:3 classes for 3 types
public class EntityPech extends EntityMob implements IRangedAttackMob {
   public ItemStack[] loot = new ItemStack[9];
   public boolean trading = false;
   public boolean updateAINextTick = false;
   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 0.6, 20, 50, 15.0F);
   private EntityAIArrowAttack aiBlastAttack = new EntityAIArrowAttack(this, 0.6, 20, 30, 15.0F);
   private AIAttackOnCollide aiMeleeAttack = new AIAttackOnCollide(this, EntityLivingBase.class, 0.6, false);
   private EntityAIAvoidEntity aiAvoidPlayer = new EntityAIAvoidEntity(this, Player.class, 8.0F, 0.5F, 0.6);
   public float mumble = 0.0F;
   int chargecount = 0;
   static Map<ResourceLocation,Integer> valuedItems = new HashMap<>();
   public static Map<Integer,List<SimplePair<Integer,ItemStack>>> tradeInventory = new HashMap<>();

   public String getCommandSenderName() {
      if (this.hasCustomNameTag()) {
         return this.getCustomNameTag();
      } else {
         switch (this.getPechType()) {
             case 1:
               return StatCollector.translateToLocal("entity.Thaumcraft.Pech.1.name");
            case 2:
               return StatCollector.translateToLocal("entity.Thaumcraft.Pech.2.name");
            default:
               return StatCollector.translateToLocal("entity.Thaumcraft.Pech.name");
         }
      }
   }

   public EntityPech(World world) {
      super(world);
      this.setSize(0.6F, 1.8F);
      this.getNavigator().setBreakDoors(false);
      this.getNavigator().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new AIPechTradePlayer(this));
      this.tasks.addTask(3, new AIPechItemEntityGoto(this));
      this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
      this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 0.5F));
      this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0F, false));
      this.tasks.addTask(9, new EntityAIWander(this, 0.6));
      this.tasks.addTask(9, new EntityAIWatchClosest2(this, Player.class, 3.0F, 1.0F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.tasks.addTask(11, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      if (world != null && Platform.getEnvironment() != Env.CLIENT) {
         this.setCombatTask();
      }

      this.equipmentDropChances[0] = 0.2F;
   }

   public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
      super.setCurrentItemOrArmor(par1, par2ItemStack);
      if (Platform.getEnvironment() != Env.CLIENT && par1 == 0) {
         this.updateAINextTick = true;
      }

   }

   protected void addRandomArmor() {
      super.addRandomArmor();
      switch (this.rand.nextInt(20)) {
         case 0:
         case 12:
            ItemStack wand = new ItemStack(ConfigItems.WandCastingItem);
            ItemStack focus = new ItemStack(ConfigItems.itemFocusPech);
            ((WandCastingItem)wand.getItem()).setFocus(wand, focus);
            ((WandCastingItem)wand.getItem()).addCentiVis(wand, Aspects.EARTH, 2 + this.rand.nextInt(6), true);
            ((WandCastingItem)wand.getItem()).addCentiVis(wand, Aspects.ENTROPY, 2 + this.rand.nextInt(6), true);
            ((WandCastingItem)wand.getItem()).addCentiVis(wand, Aspects.WATER, 2 + this.rand.nextInt(6), true);
            ((WandCastingItem)wand.getItem()).addCentiVis(wand, Aspects.AIR, this.rand.nextInt(4), true);
            ((WandCastingItem)wand.getItem()).addCentiVis(wand, Aspects.FIRE, this.rand.nextInt(4), true);
            ((WandCastingItem)wand.getItem()).addCentiVis(wand, Aspects.ORDER, this.rand.nextInt(4), true);
            this.setCurrentItemOrArmor(0, wand);
            break;
         case 1:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
            break;
         case 2:
         case 4:
         case 10:
         case 11:
         case 13:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
            break;
         case 3:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_axe));
            break;
         case 5:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
            break;
         case 6:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_axe));
            break;
         case 7:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.fishing_rod));
            break;
         case 8:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_pickaxe));
            break;
         case 9:
            this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_pickaxe));
      }

   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
      this.addRandomArmor();
      ItemStack itemstack = this.getHeldItem();
      if (itemstack != null && itemstack.getItem() == ConfigItems.WandCastingItem) {
         this.setPechType(1);
         this.equipmentDropChances[0] = 0.1F;
      } else if (itemstack != null) {
         if (itemstack.getItem() == Items.bow) {
            this.setPechType(2);
         }

         this.enchantEquipment();
      }

      this.setCanPickUpLoot(this.rand.nextFloat() < 0.75F * this.level().func_147462_b(this.posX, this.posY, this.posZ));
      return super.onSpawnWithEgg(par1EntityLivingData);
   }

   public boolean getCanSpawnHere() {
      BiomeGenBase biome = this.level().getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
      boolean magicBiome = false;
      if (biome != null) {
         magicBiome = BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL) && biome.biomeID != Config.biomeTaintID;
      }

      int count = 0;

      try {
         List l = this.level().getEntitiesWithinAABB(EntityPech.class, this.boundingBox.expand(16.0F, 16.0F, 16.0F));
         if (l != null) {
            count = l.size();
         }
      } catch (Exception ignored) {
      }

      if (this.level().dimension() != 0 && biome.biomeID != Config.biomeMagicalForestID && biome.biomeID != Config.biomeEerieID) {
         magicBiome = false;
      }

      return count < 4 && magicBiome && super.getCanSpawnHere();
   }

   public float getEyeHeight() {
      return this.height * 0.66F;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(13, (byte) 0);
      this.dataWatcher.addObject(14, (short) 0);
      this.dataWatcher.addObject(16, (byte) 0);
   }

   public int getPechType() {
      return this.dataWatcher.getWatchableObjectByte(13);
   }

   public int getAnger() {
      return this.dataWatcher.getWatchableObjectShort(14);
   }

   public boolean isTamed() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   public void setPechType(int par1) {
      this.dataWatcher.updateObject(13, (byte)par1);
   }

   public void setAnger(int par1) {
      this.dataWatcher.updateObject(14, (short)par1);
   }

   public void setTamed(boolean par1) {
      this.dataWatcher.updateObject(16, (byte) (par1 ? 1 : 0));
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5F);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("PechType", (byte)this.getPechType());
      par1NBTTagCompound.setShort("Anger", (short)this.getAnger());
      par1NBTTagCompound.setBoolean("Tamed", this.isTamed());
      NBTTagList nbttaglist = new NBTTagList();

       for (ItemStack itemStack : this.loot) {
           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
           if (itemStack != null) {
               itemStack.writeToNBT(nbttagcompound1);
           }

           nbttaglist.appendTag(nbttagcompound1);
       }

      par1NBTTagCompound.setTag("Loot", nbttaglist);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      if (par1NBTTagCompound.hasKey("PechType")) {
         byte b0 = par1NBTTagCompound.getByte("PechType");
         this.setPechType(b0);
      }

      this.setAnger(par1NBTTagCompound.getShort("Anger"));
      this.setTamed(par1NBTTagCompound.getBoolean("Tamed"));
      if (par1NBTTagCompound.hasKey("Loot")) {
         NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Loot", 10);

         for(int i = 0; i < this.loot.length; ++i) {
            this.loot[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
         }
      }

      this.updateAINextTick = true;
   }

   protected boolean canDespawn() {
      try {
         if (this.loot == null) {
            return true;
         } else {
            int q = 0;

            for(ItemStack is : this.loot) {
               if (is != null && is.stackSize > 0) {
                  ++q;
               }
            }

            return q < 5;
         }
      } catch (Exception var6) {
         return true;
      }
   }

   public boolean allowLeashing() {
      return false;
   }

   protected void dropFewItems(boolean flag, int i) {
       for (ItemStack itemStack : this.loot) {
           if (itemStack != null && this.level().rand.nextFloat() < 0.88F) {
               this.entityDropItem(itemStack.copy(), 1.5F);
           }
       }

      Aspect[] aspects = Aspects.getPrimalAspects().toArray(new Aspect[0]);

      for(int a = 0; a < 1 + i; ++a) {
         if (this.rand.nextBoolean()) {
            ItemStack is = new ItemStack(ConfigItems.itemManaBean);
            ((ItemManaBean)is.getItem()).setAspects(is, (new AspectList<>()).addAll(aspects[this.rand.nextInt(aspects.length)], 1));
            this.entityDropItem(is, 1.5F);
         }
      }

      if (this.level().rand.nextInt(10) < 1 + i) {
         this.entityDropItem(new ItemStack(ThaumcraftItems.GOLD_COIN), 1.5F);
      }

      super.dropFewItems(flag, i);
   }

   protected void dropRareDrop(int par1) {
      this.entityDropItem(new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT), 1.5F);
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if (par1 == 16) {
         this.mumble = (float)Math.PI;
      } else if (par1 == 17) {
         this.mumble = ((float)Math.PI * 2F);
      } else if (par1 == 18) {
         for(int i = 0; i < 5; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02;
            double d1 = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            this.level().spawnParticle("happyVillager", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)0.5F + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
         }
      }

      if (par1 == 19) {
         for(int i = 0; i < 5; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02;
            double d1 = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            this.level().spawnParticle("angryVillager", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)0.5F + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
         }

         this.mumble = ((float)Math.PI * 2F);
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public void playLivingSound() {
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (this.rand.nextInt(3) == 0) {
            List list = this.level().getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(4.0F, 2.0F, 4.0F));

             for (Object o : list) {
                 Entity entity1 = (Entity) o;
                 if (entity1 instanceof EntityPech) {
                     this.level().setEntityState(this, (byte) 17);
                     this.playSound("thaumcraft:pech_trade", this.getSoundVolume(), this.getSoundPitch());
                     return;
                 }
             }
         }

         this.level().setEntityState(this, (byte)16);
      }

      super.playLivingSound();
   }

   public int getTalkInterval() {
      return 120;
   }

   protected float getSoundVolume() {
      return 0.4F;
   }

   protected String getLivingSound() {
      return "thaumcraft:pech_idle";
   }

   protected String getHurtSound() {
      return "thaumcraft:pech_hit";
   }

   protected String getDeathSound() {
      return "thaumcraft:pech_death";
   }

   protected Entity findPlayerToAttack() {
      return this.getAnger() == 0 ? null : super.findPlayerToAttack();
   }

   public void setCombatTask() {
      this.tasks.removeTask(this.aiMeleeAttack);
      this.tasks.removeTask(this.aiArrowAttack);
      this.tasks.removeTask(this.aiBlastAttack);
      ItemStack itemstack = this.getHeldItem();
      if (itemstack != null && itemstack.getItem() == Items.bow) {
         this.tasks.addTask(2, this.aiArrowAttack);
      } else if (itemstack != null && itemstack.getItem() == ConfigItems.WandCastingItem) {
         this.tasks.addTask(2, this.aiBlastAttack);
      } else {
         this.tasks.addTask(2, this.aiMeleeAttack);
      }

      if (this.isTamed()) {
         this.tasks.removeTask(this.aiAvoidPlayer);
      } else {
         this.tasks.addTask(4, this.aiAvoidPlayer);
      }

   }

   public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
      if (this.getPechType() == 2) {
         EntityArrow entityarrow = new EntityArrow(this.level(), this, entitylivingbase, 1.6F, (float)(14 - this.level().difficultySetting.getDifficultyId() * 4));
         int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
         int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
         entityarrow.setDamage((double)(f * 2.0F) + this.rand.nextGaussian() * (double)0.25F + (double)((float)this.level().difficultySetting.getDifficultyId() * 0.11F));
         if (i > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + (double)i * (double)0.5F + (double)0.5F);
         }

         if (j > 0) {
            entityarrow.setKnockbackStrength(j);
         }

         this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
         this.level().spawnEntityInWorld(entityarrow);
      } else if (this.getPechType() == 1) {
         EntityPechBlast blast = new EntityPechBlast(this.level(), this, 1, 0, this.rand.nextFloat() < 0.1F);
         double d0 = entitylivingbase.posX + entitylivingbase.motionX - this.posX;
         double d1 = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - 1.500000023841858 - this.posY;
         double d2 = entitylivingbase.posZ + entitylivingbase.motionZ - this.posZ;
         float f1 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
         blast.setThrowableHeading(d0, d1 + (double)(f1 * 0.1F), d2, 1.5F, 4.0F);
         this.playSound("thaumcraft:ice", 0.4F, 1.0F + this.rand.nextFloat() * 0.1F);
         this.level().spawnEntityInWorld(blast);
      }

      this.swingItem();
   }

   private void becomeAngryAt(Entity par1Entity) {
      this.entityToAttack = par1Entity;
      if (this.getAnger() <= 0) {
         this.level().setEntityState(this, (byte)19);
         this.playSound("thaumcraft:pech_charge", this.getSoundVolume(), this.getSoundPitch());
      }

      this.setAttackTarget((EntityLivingBase)par1Entity);
      this.setAnger(400 + this.rand.nextInt(400));
      this.setTamed(false);
      this.updateAINextTick = true;
   }

   public int getTotalArmorValue() {
      int i = super.getTotalArmorValue() + 2;
      if (i > 20) {
         i = 20;
      }

      return i;
   }

   public boolean attackEntityFrom(DamageSource damSource, float par2) {
      if (this.isEntityInvulnerable()) {
         return false;
      } else {
         Entity entity = damSource.getEntity();
         if (entity instanceof Player) {
            List list = this.level().getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0F, 16.0F, 32.0F));

             for (Object o : list) {
                 Entity entity1 = (Entity) o;
                 if (entity1 instanceof EntityPech) {
                     EntityPech entitypech = (EntityPech) entity1;
                     entitypech.becomeAngryAt(entity);
                 }
             }

            this.becomeAngryAt(entity);
         }

         return super.attackEntityFrom(damSource, par2);
      }
   }

   public void onUpdate() {
      if (this.mumble > 0.0F) {
         this.mumble *= 0.75F;
      }

      if (this.getAnger() > 0) {
         this.setAnger(this.getAnger() - 1);
      }

      if (this.getAnger() > 0 && (this.entityToAttack == null || this.getAttackTarget() == null)) {
         this.findPlayerToAttack();
         this.setAttackTarget((EntityLivingBase)this.entityToAttack);
         if (this.entityToAttack != null) {
            if (this.chargecount > 0) {
               --this.chargecount;
            }

            if (this.chargecount == 0) {
               this.chargecount = 100;
               this.playSound("thaumcraft:pech_charge", this.getSoundVolume(), this.getSoundPitch());
            }

            this.level().setEntityState(this, (byte)17);
         }
      }

      if ((Platform.getEnvironment() == Env.CLIENT) && this.rand.nextInt(15) == 0 && this.getAnger() > 0) {
         double d0 = this.rand.nextGaussian() * 0.02;
         double d1 = this.rand.nextGaussian() * 0.02;
         double d2 = this.rand.nextGaussian() * 0.02;
         this.level().spawnParticle("angryVillager", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)0.5F + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
      }

      if ((Platform.getEnvironment() == Env.CLIENT) && this.rand.nextInt(25) == 0 && this.isTamed()) {
         double d0 = this.rand.nextGaussian() * 0.02;
         double d1 = this.rand.nextGaussian() * 0.02;
         double d2 = this.rand.nextGaussian() * 0.02;
         this.level().spawnParticle("happyVillager", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)0.5F + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
      }

      super.onUpdate();
   }

   public void updateAITasks() {
      if (this.updateAINextTick) {
         this.updateAINextTick = false;
         this.setCombatTask();
      }

      super.updateAITasks();
      if (this.ticksExisted % 40 == 0) {
         this.heal(1.0F);
      }

   }

   public boolean canPickup(ItemStack entityItem) {
      if (entityItem == null) {
         return false;
      } else if (!this.isTamed()
              && valuedItems.containsKey(BuiltInRegistries.ITEM.getKey(entityItem.getItem()))
      ) {
         return true;
      } else {
         for(int a = 0; a < this.loot.length; ++a) {
            if (this.loot[a] != null && this.loot[a].getCount() <= 0) {
               this.loot[a] = null;
            }

            if (this.loot[a] == null) {
               return true;
            }

            if (ItemStack.matches(entityItem, this.loot[a]) && entityItem.getCount() + this.loot[a].getCount() <= this.loot[a].getMaxStackSize()) {
               return true;
            }
         }

         return false;
      }
   }

   public @Nullable ItemStack pickupItem(ItemStack entityItem) {
      if (entityItem == null) {
         return entityItem;
      } else if (!this.isTamed() && this.isValued(entityItem)) {
         if (this.rand.nextInt(10) < this.getValue(entityItem)) {
            this.setTamed(true);
            this.updateAINextTick = true;
            this.level().setEntityState(this, (byte)18);
         }

         entityItem.shrink(1);
         return entityItem.getCount() <= 0 ? null : entityItem;
      } else {
         for(int a = 0; a < this.loot.length; ++a) {
            if (this.loot[a] != null && this.loot[a].stackSize <= 0) {
               this.loot[a] = null;
            }

            if (entityItem != null && entityItem.stackSize > 0 && this.loot[a] != null && this.loot[a].stackSize < this.loot[a].getMaxStackSize() && InventoryUtils.areItemStacksEqualStrict(entityItem, this.loot[a])) {
               if (entityItem.stackSize + this.loot[a].stackSize <= this.loot[a].getMaxStackSize()) {
                  ItemStack var5 = this.loot[a];
                  var5.stackSize += entityItem.stackSize;
                  return null;
               }

               int sz = Math.min(entityItem.stackSize, this.loot[a].getMaxStackSize() - this.loot[a].stackSize);
               ItemStack var10000 = this.loot[a];
               var10000.stackSize += sz;
               entityItem.stackSize -= sz;
            }

            if (entityItem != null && entityItem.stackSize <= 0) {
               entityItem = null;
            }
         }

         for(int a = 0; a < this.loot.length; ++a) {
            if (this.loot[a] != null && this.loot[a].stackSize <= 0) {
               this.loot[a] = null;
            }

            if (entityItem != null && entityItem.stackSize > 0 && this.loot[a] == null) {
               this.loot[a] = entityItem.copy();
               return null;
            }
         }

         if (entityItem != null && entityItem.stackSize <= 0) {
            entityItem = null;
         }

         return entityItem;
      }
   }

   public boolean interact(Player player) {
      if (!player.isSneaking() && (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemNameTag))) {
         if (Platform.getEnvironment() != Env.CLIENT && this.isTamed()) {
            player.openGui(Thaumcraft.instance, 1, this.level(), this.getEntityId(), 0, 0);
            return true;
         } else {
            return super.interact(player);
         }
      } else {
         return false;
      }
   }

   public boolean isValued(ItemStack item) {
      if (item == null) {
         return false;
      } else {
         boolean value = valuedItems.containsKey(BuiltInRegistries.ITEM.getKey(item.getItem()));
         if (!value) {
            AspectList<Aspect>al = ThaumcraftCraftingManager.getObjectTags(item);
            al = ThaumcraftCraftingManager.getBonusAspects(item, al);
            if (al.getAmount(Aspects.GREED) > 0) {
               value = true;
            }
         }

         return value;
      }
   }

   public int getValue(ItemStack item) {
      if (item == null) {
         return 0;
      } else {
         int value = valuedItems.containsKey(BuiltInRegistries.ITEM.getKey(item.getItem())) ? (Integer)valuedItems.get(BuiltInRegistries.ITEM.getKey(item.getItem())) : 0;
         if (value == 0) {
            AspectList<Aspect>al = ThaumcraftCraftingManager.getObjectTags(item);
            al = ThaumcraftCraftingManager.getBonusAspects(item, al);
            value = Math.min(32, al.getAmount(Aspects.GREED));
         }

         return value;
      }
   }

   static {
      valuedItems.put(BuiltInRegistries.ITEM.getKey(ConfigItems.itemManaBean), 1);
      valuedItems.put(BuiltInRegistries.ITEM.getKey(Items.GOLD_INGOT), 2);
      valuedItems.put(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_APPLE), 2);
      valuedItems.put(BuiltInRegistries.ITEM.getKey(Items.ENDER_PEARL), 3);
      valuedItems.put(BuiltInRegistries.ITEM.getKey(Items.DIAMOND), 4);
      valuedItems.put(BuiltInRegistries.ITEM.getKey(Items.EMERALD), 5);
      valuedItems.put(BuiltInRegistries.ITEM.getKey(Items.ENCHANTED_GOLDEN_APPLE), 10);//just addition--IgnoreLicensesCN

      ItemStack potionStack;
      ItemStack book;

      {
         List<SimplePair<Integer, ItemStack>> forInv = new ArrayList<>();
         forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemManaBean)));
         forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemNugget, 1, 16)));
         forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemNugget, 1, 31)));
         forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemNugget, 1, 21)));
         if (Config.foundCopperIngot) {
            forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemNugget, 1, 17)));
         }

         if (Config.foundTinIngot) {
            forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemNugget, 1, 18)));
         }

         if (Config.foundSilverIngot) {
            forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemNugget, 1, 19)));
         }

         if (Config.foundLeadIngot) {
            forInv.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemNugget, 1, 20)));
         }

         forInv.add(new SimplePair<>(2, new ItemStack(Items.BLAZE_ROD)));
         forInv.add(new SimplePair<>(2, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 0)));

         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH);
         forInv.add(new SimplePair<>(2, potionStack));


         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS);
         forInv.add(new SimplePair<>(2, potionStack));

         forInv.add(new SimplePair<>(3, new ItemStack(Items.EXPERIENCE_BOTTLE)));
         forInv.add(new SimplePair<>(3, new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT)));
         forInv.add(new SimplePair<>(3, new ItemStack(Items.GOLDEN_APPLE)));

         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_STRENGTH);
         forInv.add(new SimplePair<>(3, potionStack));
         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION);
         forInv.add(new SimplePair<>(3, potionStack));

         forInv.add(new SimplePair<>(5, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE)));
         forInv.add(new SimplePair<>(4, new ItemStack(ConfigItems.itemPickThaumium)));
         forInv.add(new SimplePair<>(5, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 1)));
         forInv.add(new SimplePair<>(5, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 1)));
         tradeInventory.put(0, forInv);
      }
      {
         List<SimplePair<Integer, ItemStack>> forMag = new ArrayList<>();
         forMag.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemManaBean)));

         for (int a = 0; a < 6; ++a) {
            forMag.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemShard, 1, a)));
         }

         forMag.add(new SimplePair<>(1, new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT)));
         forMag.add(new SimplePair<>(2, new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT)));
         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION);
         forMag.add(new SimplePair<>(2, potionStack));
         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING);
         forMag.add(new SimplePair<>(2, potionStack));

         book = new ItemStack(Items.ENCHANTED_BOOK);
         EnchantedBookItem.addEnchantment(
                 book,
                 new EnchantmentInstance(ThaumcraftEnchantments.HASTE, 1)
         );
         forMag.add(new SimplePair<>(3, book));

         forMag.add(new SimplePair<>(3, new ItemStack(Items.GOLDEN_APPLE)));
         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION);
         forMag.add(new SimplePair<>(3, potionStack));
         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING);
         forMag.add(new SimplePair<>(3, potionStack));

         for (int a = 0; a < 7; ++a) {
            forMag.add(new SimplePair<>(4, new ItemStack(ConfigBlocks.blockCrystal, 1, a)));
         }

         forMag.add(new SimplePair<>(5, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE)));


         book = new ItemStack(Items.ENCHANTED_BOOK);
         EnchantedBookItem.addEnchantment(
                 book,
                 new EnchantmentInstance(ThaumcraftEnchantments.REPAIR, 1)
         );
         forMag.add(new SimplePair<>(5, book));

         forMag.add(new SimplePair<>(5, new ItemStack(ConfigItems.itemFocusPouch)));
         forMag.add(new SimplePair<>(5, new ItemStack(ConfigItems.itemFocusPech)));
         forMag.add(new SimplePair<>(5, new ItemStack(ConfigItems.itemAmuletVis, 1, 0)));
         tradeInventory.put(1, forMag);
      }

      {
         List<SimplePair<Integer, ItemStack>> forArc = new ArrayList<>();
         forArc.add(new SimplePair<>(1, new ItemStack(ConfigItems.itemManaBean)));

         for (int a = 0; a < 15; ++a) {
            forArc.add(new SimplePair<>(1, new ItemStack(ConfigBlocks.blockCandle, 1, a)));
         }

         forArc.add(new SimplePair<>(2, new ItemStack(Items.GHAST_TEAR)));
         forArc.add(new SimplePair<>(2, new ItemStack(Items.potionitem, 1, 8194)));
         forArc.add(new SimplePair<>(2, new ItemStack(Items.potionitem, 1, 8201)));


         book = new ItemStack(Items.ENCHANTED_BOOK);
         EnchantedBookItem.addEnchantment(
                 book,
                 new EnchantmentInstance(Enchantments.POWER_ARROWS, 1)
         );
         forArc.add(new SimplePair<>(2, book));

         forArc.add(new SimplePair<>(3, new ItemStack(Items.EXPERIENCE_BOTTLE)));
         forArc.add(new SimplePair<>(3, new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT)));
         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_INVISIBILITY);
         forArc.add(new SimplePair<>(3, potionStack));

         potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION);
         forArc.add(new SimplePair<>(3, potionStack));

         forArc.add(new SimplePair<>(3, new ItemStack(Items.GOLDEN_APPLE)));
         forArc.add(new SimplePair<>(5, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE)));
         forArc.add(new SimplePair<>(4, new ItemStack(ConfigItems.itemBootsThaumium)));
         forArc.add(new SimplePair<>(5, new ItemStack(ConfigItems.itemRingRunic, 1, 0)));


         book = new ItemStack(Items.ENCHANTED_BOOK);
         EnchantedBookItem.addEnchantment(
                 book,
                 new EnchantmentInstance(Enchantments.FLAMING_ARROWS, 1)
         );
         forArc.add(new SimplePair<>(5, book));

         book = new ItemStack(Items.ENCHANTED_BOOK);
         EnchantedBookItem.addEnchantment(
                 book,
                 new EnchantmentInstance(Enchantments.INFINITY_ARROWS, 1)
         );
         forArc.add(new SimplePair<>(5, book));
         tradeInventory.put(2, forArc);
      }
   }
}
