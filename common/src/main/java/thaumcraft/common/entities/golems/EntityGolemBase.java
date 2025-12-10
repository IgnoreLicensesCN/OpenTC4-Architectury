package thaumcraft.common.entities.golems;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fromhodgepodge.mixins.hooks.ThaumcraftMixinMethods;
import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.InventoryMob;
import thaumcraft.common.entities.ai.combat.*;
import thaumcraft.common.entities.ai.fluid.*;
import thaumcraft.common.entities.ai.interact.AIFish;
import thaumcraft.common.entities.ai.interact.AIHarvestCrops;
import thaumcraft.common.entities.ai.interact.AIHarvestLogs;
import thaumcraft.common.entities.ai.interact.AIUseItem;
import thaumcraft.common.entities.ai.inventory.*;
import thaumcraft.common.entities.ai.misc.AIOpenDoor;
import thaumcraft.common.entities.ai.misc.AIReturnHome;
import thaumcraft.common.entities.projectile.EntityDart;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityGolemBase extends EntityGolem implements IEntityAdditionalSpawnData {
   public InventoryMob inventory;
   public ItemStack itemCarried;
   public FluidStack fluidCarried;
   public ItemStack itemWatched;
   public Aspect essentia;
   public int essentiaAmount;
   public boolean advanced;
   public int homeFacing;
   public boolean paused;
   public boolean inactive;
   public boolean flag;
   public byte[] colors;
   public byte[] upgrades;
   public String decoration;
   public float bootup;
   public EnumGolemType golemType;
   public int regenTimer;
   protected ArrayList<Marker> markers;
   boolean pdw;
   public int action;
   public int leftArm;
   public int rightArm;
   public int healing;

   public EntityGolemBase(World par1World) {
      super(par1World);
      this.inventory = new InventoryMob(this, 1);
      this.itemWatched = null;
      this.advanced = false;
      this.homeFacing = 0;
      this.paused = false;
      this.inactive = false;
      this.flag = false;
      this.colors = null;
      this.upgrades = null;
      this.decoration = "";
      this.bootup = -1.0F;
      this.golemType = EnumGolemType.WOOD;
      this.regenTimer = 0;
      this.markers = new ArrayList<>();
      this.pdw = false;
      this.action = 0;
      this.leftArm = 0;
      this.rightArm = 0;
      this.healing = 0;
      this.dataWatcher.addObject(30, (byte)((int)this.getMaxHealth()));
      this.stepHeight = 1.0F;
      this.colors = new byte[]{-1};
      this.upgrades = new byte[]{-1};
      this.setSize(0.4F, 0.95F);
      this.getNavigator().setBreakDoors(true);
      this.getNavigator().setEnterDoors(true);
      this.getNavigator().setCanSwim(true);
      this.func_110163_bv();
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0F);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(32.0F);
   }

   public EntityGolemBase(World par0World, EnumGolemType type, boolean adv) {
      this(par0World);
      this.golemType = type;
      this.advanced = adv;
      this.upgrades = new byte[this.golemType.upgrades + (this.advanced ? 1 : 0)];

       Arrays.fill(this.upgrades, (byte) -1);

   }

   public boolean setupGolemInventory() {
      ItemStack core = null;
      if (!ItemGolemCore.hasInventory(this.getCore())) {
         return false;
      } else {
         if (this.getCore() > -1) {
            int invSize = 0;
            switch (this.getCore()) {
               case 3:
               case 4:
               case 6:
                  break;
               case 5:
                  invSize = 1;
                  invSize += this.getUpgradeAmount(2);
                  break;
               default:
                  invSize = 6;
                  invSize += this.getUpgradeAmount(2) * 6;
            }

            InventoryMob inventory2 = new InventoryMob(this, invSize);

             System.arraycopy(this.inventory.inventory, 0, inventory2.inventory, 0, this.inventory.inventory.length);

            this.inventory = inventory2;
         }

         byte[] oldcolors = this.colors;
         this.colors = new byte[this.inventory.slotCount];

         for(int a = 0; a < this.inventory.slotCount; ++a) {
            this.colors[a] = -1;
            if (a < oldcolors.length) {
               this.colors[a] = oldcolors[a];
            }
         }

         return true;
      }
   }

   public boolean setupGolem() {
      if (!this.worldObj.isRemote) {
         this.dataWatcher.updateObject(19, (byte)this.golemType.ordinal());
      }

       this.getNavigator().setAvoidsWater(this.getGolemType() != EnumGolemType.STONE && this.getGolemType() != EnumGolemType.IRON && this.getGolemType() != EnumGolemType.THAUMIUM);

      if (this.getGolemType().fireResist) {
         this.isImmuneToFire = true;
      }

      int bonus = 0;

      try {
         bonus = this.getGolemDecoration().contains("H") ? 5 : 0;
      } catch (Exception ignored) {
      }

      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getGolemType().health + bonus);
      int damage = 2 + this.getGolemStrength() + this.getUpgradeAmount(1);

      try {
         if (this.getGolemDecoration().contains("M")) {
            damage += 2;
         }
      } catch (Exception ignored) {
      }

      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(damage);
      this.tasks.taskEntries.clear();
      if (this.getCore() > -1) {
         this.tasks.addTask(0, new AIAvoidCreeperSwell(this));
      }

      switch (this.getCore()) {
         case 0:
            this.tasks.addTask(0, new AIHomeReplace(this));
            this.tasks.addTask(1, new AIHomePlace(this));
            this.tasks.addTask(2, new AIHomeDrop(this));
            this.tasks.addTask(3, new AIFillTake(this));
            this.tasks.addTask(4, new AIFillGoto(this));
            break;
         case 1:
            this.tasks.addTask(0, new AIHomeReplace(this));
            this.tasks.addTask(1, new AIEmptyPlace(this));
            this.tasks.addTask(2, new AIEmptyDrop(this));
            this.tasks.addTask(3, new AIEmptyGoto(this));
            this.tasks.addTask(4, new AIHomeTake(this));
            break;
         case 2:
            this.tasks.addTask(0, new AIHomeReplace(this));
            this.tasks.addTask(1, new AIHomePlace(this));
            this.tasks.addTask(2, new AIItemPickup(this));
            break;
         case 3:
            this.tasks.addTask(2, new AIHarvestCrops(this));
            break;
         case 4:
            if (this.decoration.contains("R")) {
               this.tasks.addTask(2, new AIDartAttack(this));
            }

            this.tasks.addTask(3, new AIGolemAttackOnCollide(this));
            this.targetTasks.addTask(1, new AIHurtByTarget(this, false));
            this.targetTasks.addTask(2, new AINearestAttackableTarget(this, 0, true));
            break;
         case 5:
            this.tasks.addTask(1, new AILiquidEmpty(this));
            this.tasks.addTask(2, new AILiquidGather(this));
            this.tasks.addTask(3, new AILiquidGoto(this));
            break;
         case 6:
            this.tasks.addTask(1, new AIEssentiaEmpty(this));
            this.tasks.addTask(2, new AIEssentiaGather(this));
            this.tasks.addTask(3, new AIEssentiaGoto(this));
            break;
         case 7:
            this.tasks.addTask(2, new AIHarvestLogs(this));
            break;
         case 8:
            this.tasks.addTask(0, new AIHomeReplace(this));
            this.tasks.addTask(0, new AIUseItem(this));
            this.tasks.addTask(4, new AIHomeTake(this));
            break;
         case 9:
            if (this.decoration.contains("R")) {
               this.tasks.addTask(2, new AIDartAttack(this));
            }

            this.tasks.addTask(3, new AIGolemAttackOnCollide(this));
            this.targetTasks.addTask(1, new AINearestButcherTarget(this, 0, true));
            break;
         case 10:
            this.tasks.addTask(0, new AIHomeReplace(this));
            this.tasks.addTask(1, new AISortingPlace(this));
            this.tasks.addTask(3, new AISortingGoto(this));
            this.tasks.addTask(4, new AIHomeTakeSorting(this));
            break;
         case 11:
            this.tasks.addTask(2, new AIFish(this));
      }

      if (this.getCore() > -1) {
         this.tasks.addTask(5, new AIOpenDoor(this, true));
         this.tasks.addTask(6, new AIReturnHome(this));
         this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
         this.tasks.addTask(8, new EntityAILookIdle(this));
      }

      return true;
   }

   public int getCarryLimit() {
      int base = this.golemType.carry;
      if (this.worldObj.isRemote) {
         base = this.getGolemType().carry;
      }

      base += Math.min(16, Math.max(4, base)) * this.getUpgradeAmount(1);
      return base;
   }

   public int getFluidCarryLimit() {
      return MathHelper.floor_double(Math.sqrt(this.getCarryLimit())) * 1000;
   }

   public float getAIMoveSpeed() {
      if (!this.paused && !this.inactive) {
         float speed = this.golemType.speed * (this.decoration.contains("B") ? 1.1F : 1.0F);
         if (this.decoration.contains("P")) {
            speed *= 0.88F;
         }

         speed *= 1.0F + (float)this.getUpgradeAmount(0) * 0.15F;
         if (this.advanced) {
            speed *= 1.1F;
         }

         if (this.isInWater() && (this.getGolemType() == EnumGolemType.STONE || this.getGolemType() == EnumGolemType.IRON || this.getGolemType() == EnumGolemType.THAUMIUM)) {
            speed *= 2.0F;
         }

         return speed;
      } else {
         return 0.0F;
      }
   }

   public void setup(int side) {
      this.homeFacing = side;
      this.setupGolem();
      this.setupGolemInventory();
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObjectByDataType(16, 5);
      this.dataWatcher.addObject(17, "");
      this.dataWatcher.addObject(18, (byte)0);
      this.dataWatcher.addObject(19, (byte)0);
      this.dataWatcher.addObject(20, "");
      this.dataWatcher.addObject(21, -1);
      this.dataWatcher.addObject(22, "");
      this.dataWatcher.addObject(23, "");
   }

   public boolean isAIEnabled() {
      return !this.paused && !this.inactive;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.action > 0) {
         --this.action;
      }

      if (this.leftArm > 0) {
         --this.leftArm;
      }

      if (this.rightArm > 0) {
         --this.rightArm;
      }

      if (this.healing > 0) {
         --this.healing;
      }

      int xx = MathHelper.floor_double(this.posX);
      int yy = MathHelper.floor_double(this.posY);
      int zz = MathHelper.floor_double(this.posZ);
       this.inactive = yy > 0 && this.worldObj.getBlock(xx, yy - 1, zz) == ConfigBlocks.blockCosmeticSolid && this.worldObj.getBlockMetadata(xx, yy - 1, zz) == 10;

      if (!this.worldObj.isRemote) {
         if (this.regenTimer > 0) {
            --this.regenTimer;
         } else {
            this.regenTimer = this.golemType.regenDelay;
            if (this.decoration.contains("F")) {
               this.regenTimer = (int)((float)this.regenTimer * 0.66F);
            }

            if (!this.worldObj.isRemote && this.getHealth() < this.getMaxHealth()) {
               this.worldObj.setEntityState(this, (byte)5);
               this.heal(1.0F);
            }
         }

         if (this.getDistanceSq(this.getHomePosition().posX, this.getHomePosition().posY, this.getHomePosition().posZ) >= (double)2304.0F || this.isEntityInsideOpaqueBlock()) {
            int var1 = MathHelper.floor_double(this.getHomePosition().posX);
            int var2 = MathHelper.floor_double(this.getHomePosition().posZ);
            int var3 = MathHelper.floor_double(this.getHomePosition().posY);

            for(int var0 = 1; var0 >= -1; --var0) {
               for(int var4 = -1; var4 <= 1; ++var4) {
                  for(int var5 = -1; var5 <= 1; ++var5) {
                     World var10000 = this.worldObj;
                     if (World.doesBlockHaveSolidTopSurface(this.worldObj, var1 + var4, var3 - 1 + var0, var2 + var5) && !this.worldObj.isBlockNormalCubeDefault(var1 + var4, var3 + var0, var2 + var5, false)) {
                        this.setLocationAndAngles((float)(var1 + var4) + 0.5F, (double)var3 + (double)var0, (float)(var2 + var5) + 0.5F, this.rotationYaw, this.rotationPitch);
                        this.getNavigator().clearPathEntity();
                        return;
                     }
                  }
               }
            }
         }
      } else if (this.bootup > 0.0F && this.getCore() > -1) {
         this.bootup *= this.bootup / 33.1F;
         this.worldObj.playSound(this.posX, this.posY, this.posZ, "thaumcraft:cameraticks", this.bootup * 0.2F, this.bootup, false);
      }

   }

   public float getRange() {
      float dmod = (float)(16 + this.getUpgradeAmount(3) * 4);
      if (this.decoration.contains("G")) {
         dmod += Math.max(dmod * 0.1F, 1.0F);
      }

      if (this.advanced) {
         dmod += Math.max(dmod * 0.2F, 2.0F);
      }

      return dmod;
   }

   public boolean isWithinHomeDistance(int par1, int par2, int par3) {
      float dmod = this.getRange();
      return this.getHomePosition().getDistanceSquared(par1, par2, par3) < dmod * dmod;
   }

   protected void updateEntityActionState() {
      ++this.entityAge;
      this.despawnEntity();
      boolean vara = this.isInWater();
      boolean varb = this.handleLavaMovement();
      if (vara || varb) {
         this.isJumping = this.rand.nextFloat() < 0.8F;
      }

   }

   public void onDeath(DamageSource ds) {
      if (!this.worldObj.isRemote) {
         FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[Thaumcraft] {} was killed by {} ( {} )",this,ds.getSourceOfDamage(),ds.getDamageType());
      }

      super.onDeath(ds);
   }

   public void setFire(int par1) {
      if (!this.golemType.fireResist) {
         super.setFire(par1);
      }

   }

   protected boolean canDespawn() {
       return super.canDespawn();
   }

   protected void despawnEntity() {
   }

   public int decreaseAirSupply(int par1) {
      return par1;
   }

   public short getColors(int slot) {
      char[] chars = this.dataWatcher.getWatchableObjectString(22).toCharArray();
      if (slot < chars.length) {
         return ("" + chars[slot]).equals("h") ? -1 : Short.parseShort("" + chars[slot], 16);
      } else {
         return -1;
      }
   }

   public void setColors(int slot, int color) {
      this.colors[slot] = (byte)color;
      StringBuilder s = new StringBuilder();

      for(byte c : this.colors) {
         if (c == -1) {
            s.append("h");
         } else {
            s.append(Integer.toHexString(c));
         }
      }

      this.dataWatcher.updateObject(22, s.toString());
   }

   public byte getUpgrade(int slot) {
      char[] chars = this.dataWatcher.getWatchableObjectString(23).toCharArray();
      if (slot < chars.length) {
         byte t = Byte.parseByte("" + chars[slot], 16);
         return t == 15 ? -1 : t;
      } else {
         return -1;
      }
   }

   public int getUpgradeAmount(int type) {
      int a = 0;

      for(byte b : this.upgrades) {
         if (type == b) {
            ++a;
         }
      }

      return a;
   }

   public void setUpgrade(int slot, byte upgrade) {
      this.upgrades[slot] = upgrade;
      StringBuilder s = new StringBuilder();

      for(byte c : this.upgrades) {
         s.append(Integer.toHexString(c));
      }

      this.dataWatcher.updateObject(23, s.toString());
   }

   public ArrayList<Byte> getColorsMatching(ItemStack match) {
      ArrayList<Byte> l = new ArrayList<>();
      if (this.inventory.inventory != null && this.inventory.inventory.length > 0) {
         boolean allNull = true;

         for(int a = 0; a < this.inventory.inventory.length; ++a) {
            if (this.inventory.getStackInSlot(a) != null) {
               allNull = false;
            }

            if (InventoryUtils.areItemStacksEqual(this.inventory.getStackInSlot(a), match, this.checkOreDict(), this.ignoreDamage(), this.ignoreNBT())) {
               l.add(this.colors[a]);
            }
         }

         if (allNull) {
            for(int a = 0; a < this.inventory.inventory.length; ++a) {
               l.add(this.colors[a]);
            }
         }
      }

      return l;
   }

   public void writeEntityToNBT(NBTTagCompound nbt) {
      super.writeEntityToNBT(nbt);
      nbt.setInteger("HomeX", this.getHomePosition().posX);
      nbt.setInteger("HomeY", this.getHomePosition().posY);
      nbt.setInteger("HomeZ", this.getHomePosition().posZ);
      nbt.setByte("HomeFacing", (byte)this.homeFacing);
      nbt.setByte("GolemType", (byte)this.golemType.ordinal());
      nbt.setByte("Core", this.getCore());
      nbt.setString("Decoration", this.decoration);
      nbt.setByte("toggles", this.getTogglesValue());
      nbt.setBoolean("advanced", this.advanced);
      nbt.setByteArray("colors", this.colors);
      nbt.setByteArray("upgrades", this.upgrades);
      if (this.getCore() == 5 && this.fluidCarried != null) {
         this.fluidCarried.writeToNBT(nbt);
      }

      if (this.getCore() == 6 && this.essentia != null && this.essentiaAmount > 0) {
         nbt.setString("essentia", this.essentia.getTag());
         nbt.setByte("essentiaAmount", (byte)this.essentiaAmount);
      }

      NBTTagCompound var4 = new NBTTagCompound();
      if (this.itemCarried != null) {
         this.itemCarried.writeToNBT(var4);
      }

      nbt.setTag("ItemCarried", var4);
      if (this.getOwnerName() == null) {
         nbt.setString("Owner", "");
      } else {
         nbt.setString("Owner", this.getOwnerName());
      }

      NBTTagList tl = new NBTTagList();

      for(Marker l : this.markers) {
         NBTTagCompound nbtc = new NBTTagCompound();
         nbtc.setInteger("x", l.x);
         nbtc.setInteger("y", l.y);
         nbtc.setInteger("z", l.z);
         nbtc.setInteger("dim", l.dim);
         nbtc.setByte("side", l.side);
         nbtc.setByte("color", l.color);
         tl.appendTag(nbtc);
      }

      nbt.setTag("Markers", tl);
      nbt.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
   }

   public void readEntityFromNBT(NBTTagCompound nbt) {
      super.readEntityFromNBT(nbt);
      int hx = nbt.getInteger("HomeX");
      int hy = nbt.getInteger("HomeY");
      int hz = nbt.getInteger("HomeZ");
      this.homeFacing = nbt.getByte("HomeFacing");
      this.setHomeArea(hx, hy, hz, 32);
      this.advanced = nbt.getBoolean("advanced");
      this.golemType = EnumGolemType.getType(nbt.getByte("GolemType"));
      this.setCore(nbt.getByte("Core"));
      if (this.getCore() == 5) {
         this.fluidCarried = FluidStack.loadFluidStackFromNBT(nbt);
      }

      if (this.getCore() == 6) {
         String s = nbt.getString("essentia");
         if (s != null) {
            this.essentia = Aspect.getAspect(s);
            if (this.essentia != null) {
               this.essentiaAmount = nbt.getByte("essentiaAmount");
            }
         }
      }

      this.setTogglesValue(nbt.getByte("toggles"));
      NBTTagCompound var4 = nbt.getCompoundTag("ItemCarried");
      this.itemCarried = ItemStack.loadItemStackFromNBT(var4);
      this.updateCarried();
      this.decoration = nbt.getString("Decoration");
      this.setGolemDecoration(this.decoration);
      String var2 = nbt.getString("Owner");
      if (!var2.isEmpty()) {
         this.setOwner(var2);
      }

      this.dataWatcher.updateObject(30, (byte)((int)this.getHealth()));
      NBTTagList nbttaglist = nbt.getTagList("Markers", 10);

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
         int x = nbttagcompound1.getInteger("x");
         int y = nbttagcompound1.getInteger("y");
         int z = nbttagcompound1.getInteger("z");
         int dim = nbttagcompound1.getInteger("dim");
         byte s = nbttagcompound1.getByte("side");
         byte c = nbttagcompound1.getByte("color");
         this.markers.add(new Marker(x, y, z, (byte)dim, s, c));
      }

      this.upgrades = new byte[this.golemType.upgrades + (this.advanced ? 1 : 0)];
      int ul = this.upgrades.length;
      this.upgrades = nbt.getByteArray("upgrades");
      if (ul != this.upgrades.length) {
         byte[] tt = new byte[ul];

          Arrays.fill(tt, (byte) -1);

         for(int a = 0; a < this.upgrades.length; ++a) {
            if (a < ul) {
               tt[a] = this.upgrades[a];
            }
         }

         this.upgrades = tt;
      }

      StringBuilder st = new StringBuilder();

      for(byte c : this.upgrades) {
         st.append(Integer.toHexString(c));
      }

      this.dataWatcher.updateObject(23, st.toString());
      this.setupGolem();
      this.setupGolemInventory();
      NBTTagList nbttaglist2 = nbt.getTagList("Inventory", 10);
      this.inventory.readFromNBT(nbttaglist2);
      this.colors = nbt.getByteArray("colors");
      byte[] oldcolors = this.colors;
      this.colors = new byte[this.inventory.slotCount];

      for(int a = 0; a < this.inventory.slotCount; ++a) {
         this.colors[a] = -1;
         if (a < oldcolors.length) {
            this.colors[a] = oldcolors[a];
         }
      }

      st = new StringBuilder();

      for(byte c : this.colors) {
         if (c == -1) {
            st.append("h");
         } else {
            st.append(Integer.toHexString(c));
         }
      }

      this.dataWatcher.updateObject(22, st.toString());

      NBTTagList nbtTagList = nbt.getTagList("markers", 10);
      ThaumcraftMixinMethods.overwriteMarkersDimID(nbtTagList, this.markers);
   }

   public String getOwnerName() {
      return this.dataWatcher.getWatchableObjectString(17);
   }

   public void setOwner(String par1Str) {
      this.dataWatcher.updateObject(17, par1Str);
   }

   public void setMarkers(ArrayList markers) {
      this.markers = markers;
   }

   public ArrayList<Marker> getMarkers() {
      this.validateMarkers();
      return this.markers;
   }

   protected void validateMarkers() {
      ArrayList<Marker> newMarkers = new ArrayList<>();

      for(Marker marker : this.markers) {
         if (marker.dim == this.worldObj.provider.dimensionId) {
            newMarkers.add(marker);
         }
      }

      this.markers = newMarkers;
   }

   public EntityLivingBase getOwner() {
      return this.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   protected void damageEntity(DamageSource ds, float par2) {
      if (!ds.isFireDamage() || !this.golemType.fireResist) {
         if (ds == DamageSource.inWall || ds == DamageSource.outOfWorld) {
            this.setLocationAndAngles((double)this.getHomePosition().posX + (double)0.5F, (double)this.getHomePosition().posY + (double)0.5F, (double)this.getHomePosition().posZ + (double)0.5F, 0.0F, 0.0F);
         }

         super.damageEntity(ds, par2);
         if (!this.worldObj.isRemote) {
            this.dataWatcher.updateObject(30, (byte)((int)this.getHealth()));
         }

      }
   }

   public void heal(float par1) {
      super.heal(par1);

      try {
         if (!this.worldObj.isRemote) {
            this.dataWatcher.updateObject(30, (byte)((int)this.getHealth()));
         }
      } catch (Exception ignored) {
      }

   }

   public void setHealth(float par1) {
      super.setHealth(par1);

      try {
         if (!this.worldObj.isRemote) {
            this.dataWatcher.updateObject(30, (byte)((int)this.getHealth()));
         }
      } catch (Exception ignored) {
      }

   }

   public float getHealthPercentage() {
      return (float)this.dataWatcher.getWatchableObjectByte(30) / this.getMaxHealth();
   }

   public void setCarried(ItemStack stack) {
      this.itemCarried = stack;
      this.updateCarried();
   }

   public boolean hasSomething() {
      return this.inventory.hasSomething();
   }

   public ItemStack getCarried() {
      if (this.itemCarried != null && this.itemCarried.stackSize <= 0) {
         this.setCarried(null);
      }

      return this.itemCarried;
   }

   public int getCarrySpace() {
      return this.itemCarried == null ? this.getCarryLimit() : Math.min(this.getCarryLimit() - this.itemCarried.stackSize, this.itemCarried.getMaxStackSize() - this.itemCarried.stackSize);
   }

   public boolean[] getToggles() {
      return Utils.unpack(this.dataWatcher.getWatchableObjectByte(18));
   }

   public byte getTogglesValue() {
      return this.dataWatcher.getWatchableObjectByte(18);
   }

   public void setToggle(int index, boolean tog) {
      boolean[] fz = this.getToggles();
      fz[index] = tog;
      this.dataWatcher.updateObject(18, Utils.pack(fz));
   }

   public void setTogglesValue(byte tog) {
      this.dataWatcher.updateObject(18, tog);
   }

   public boolean canAttackHostiles() {
      return !this.getToggles()[1];
   }

   public boolean canAttackAnimals() {
      return !this.getToggles()[2];
   }

   public boolean canAttackPlayers() {
      return !this.getToggles()[3];
   }

   public boolean canAttackCreepers() {
      return !this.getToggles()[4];
   }

   public boolean checkOreDict() {
      return this.getToggles()[5];
   }

   public boolean ignoreDamage() {
      return this.getToggles()[6];
   }

   public boolean ignoreNBT() {
      return this.getToggles()[7];
   }

   public EnumGolemType getGolemType() {
      return EnumGolemType.getType(this.dataWatcher.getWatchableObjectByte(19));
   }

   public int getGolemStrength() {
      return this.getGolemType().strength + this.getUpgradeAmount(1);
   }

   public void setCore(byte core) {
      this.dataWatcher.updateObject(21, core);
   }

   public byte getCore() {
      try {
         return this.dataWatcher.getWatchableObjectByte(21);
      }catch (ClassCastException cce) {//idk why
         return (byte) this.dataWatcher.getWatchableObjectInt(21);
      }
   }

   public String getGolemDecoration() {
      return this.dataWatcher.getWatchableObjectString(20);
   }

   public void setGolemDecoration(String string) {
      this.dataWatcher.updateObject(20, String.valueOf(this.decoration));
   }

   public ItemStack getCarriedForDisplay() {
      return this.dataWatcher.getWatchableObjectItemStack(16) != null ? this.dataWatcher.getWatchableObjectItemStack(16) : null;
   }

   public void updateCarried() {
      if (this.itemCarried != null) {
         this.getDataWatcher().updateObject(16, this.itemCarried.copy());
         this.getDataWatcher().setObjectWatched(16);
      } else if (this.getCore() == 5 && this.fluidCarried != null) {
         this.getDataWatcher().updateObject(16, new ItemStack(Item.getItemById(this.fluidCarried.getFluidID()), 1, this.fluidCarried.amount));
         this.getDataWatcher().setObjectWatched(16);
      } else if (this.getCore() == 6) {
         ItemStack disp = new ItemStack(ConfigItems.itemJarFilled);
         int amt = (int)(64.0F * ((float)this.essentiaAmount / (float)this.getCarryLimit()));
         if (this.essentia != null && this.essentiaAmount > 0) {
            ((IEssentiaContainerItem)disp.getItem()).setAspects(disp, (new AspectList()).add(this.essentia, amt));
         }

         this.getDataWatcher().updateObject(16, disp);
         this.getDataWatcher().setObjectWatched(16);
      } else {
         this.getDataWatcher().addObjectByDataType(16, 5);
         this.getDataWatcher().setObjectWatched(16);
      }

   }

   protected float getSoundVolume() {
      return 0.1F;
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.dropStuff();
   }

   public void dropStuff() {
      if (!this.worldObj.isRemote && this.itemCarried != null) {
         this.entityDropItem(this.itemCarried, 0.5F);
      }

   }

   protected boolean addDecoration(String type, ItemStack itemStack) {
      if (this.decoration.contains(type)) {
         return false;
      } else if (!type.equals("F") && !type.equals("H") || !this.decoration.contains("F") && !this.decoration.contains("H")) {
         if (!type.equals("G") && !type.equals("V") || !this.decoration.contains("G") && !this.decoration.contains("V")) {
            if (!type.equals("B") && !type.equals("P") || !this.decoration.contains("P") && !this.decoration.contains("B")) {
               this.decoration = this.decoration + type;
               if (!this.worldObj.isRemote) {
                  this.setGolemDecoration(this.decoration);
                  --itemStack.stackSize;
                  this.worldObj.playSoundAtEntity(this, "thaumcraft:cameraclack", 1.0F, 1.0F);
               }

               this.setupGolem();
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean customInteraction(EntityPlayer player) {
      if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() == ConfigItems.itemGolemBell) {
         return false;
      } else if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() == ConfigItems.itemGolemDecoration) {
         this.addDecoration(ItemGolemDecoration.getDecoChar(player.inventory.getCurrentItem().getItemDamage()), player.inventory.getCurrentItem());
         player.swingItem();
         return false;
      } else if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() == Items.cookie) {
         player.inventory.consumeInventoryItem(Items.cookie);
         player.swingItem();

         for(int var3 = 0; var3 < 3; ++var3) {
            double var4 = this.rand.nextGaussian() * 0.02;
            double var6 = this.rand.nextGaussian() * 0.02;
            double var8 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle("heart", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)0.5F + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8);
            this.worldObj.playSoundAtEntity(this, "random.eat", 0.3F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            int duration = 600;
            if (this.worldObj.isRemote) {
               if (this.getActivePotionEffect(Potion.moveSpeed) != null && this.getActivePotionEffect(Potion.moveSpeed).getDuration() < 2400) {
                  duration += this.getActivePotionEffect(Potion.moveSpeed).getDuration();
               }

               this.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, duration, 0));
            }
         }

         this.heal(5.0F);
         return false;
      } else if (this.getCore() > -1 && ItemGolemCore.hasGUI(this.getCore()) && (player.inventory.getCurrentItem() == null || !(player.inventory.getCurrentItem().getItem() instanceof ItemWandCasting)) && !this.worldObj.isRemote) {
         player.openGui(Thaumcraft.instance, 0, this.worldObj, this.getEntityId(), 0, 0);
         return false;
      } else {
         return false;
      }
   }

   public boolean interact(EntityPlayer player) {
      if (player.isSneaking()) {
         return false;
      } else {
         ItemStack itemstack = player.inventory.getCurrentItem();
         if (this.getCore() > -1 && itemstack != null && itemstack.getItem() == ConfigItems.itemGolemBell) {
            return false;
         } else if (this.getCore() == -1 && itemstack != null && itemstack.getItem() == ConfigItems.itemGolemCore && itemstack.getItemDamage() != 100) {
            this.setCore((byte)itemstack.getItemDamage());
            this.setupGolem();
            this.setupGolemInventory();
            --itemstack.stackSize;
            if (itemstack.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }

            this.worldObj.playSoundAtEntity(this, "thaumcraft:upgrade", 0.5F, 1.0F);
            player.swingItem();
            this.worldObj.setEntityState(this, (byte)7);
            return true;
         } else if (itemstack != null && itemstack.getItem() == ConfigItems.itemGolemUpgrade) {
            for(int a = 0; a < this.upgrades.length; ++a) {
               if (this.getUpgrade(a) == -1 && this.getUpgradeAmount(itemstack.getItemDamage()) < 2) {
                  this.setUpgrade(a, (byte)itemstack.getItemDamage());
                  this.setupGolem();
                  this.setupGolemInventory();
                  --itemstack.stackSize;
                  if (itemstack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                  }

                  this.worldObj.playSoundAtEntity(this, "thaumcraft:upgrade", 0.5F, 1.0F);
                  player.swingItem();
                  return true;
               }
            }

            return false;
         } else {
            return this.customInteraction(player);
         }
      }
   }

   public int getActionTimer() {
      return 3 - Math.abs(this.action - 3);
   }

   public void startActionTimer() {
      if (this.action == 0) {
         this.action = 6;
         this.worldObj.setEntityState(this, (byte)4);
      }

   }

   public void startLeftArmTimer() {
      if (this.leftArm == 0) {
         this.leftArm = 5;
         this.worldObj.setEntityState(this, (byte)6);
      }

   }

   public void startRightArmTimer() {
      if (this.rightArm == 0) {
         this.rightArm = 5;
         this.worldObj.setEntityState(this, (byte)8);
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if (par1 == 4) {
         this.action = 6;
      } else if (par1 == 5) {
         this.healing = 5;
         int bonus = 0;

         try {
            bonus = this.getGolemDecoration().contains("H") ? 5 : 0;
         } catch (Exception ignored) {
         }

         this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getGolemType().health + bonus);
      } else if (par1 == 6) {
         this.leftArm = 5;
      } else if (par1 == 8) {
         this.rightArm = 5;
      } else if (par1 == 7) {
         this.bootup = 33.0F;
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   protected void updateFallState(double par1, boolean par3) {
      if (par3 && this.fallDistance > 0.0F) {
         int var4 = MathHelper.floor_double(this.posX);
         int var5 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
         int var6 = MathHelper.floor_double(this.posZ);
         this.worldObj.getBlock(var4, var5, var6);
         if (this.worldObj.isAirBlock(var4, var5, var6) && this.worldObj.getBlock(var4, var5 - 1, var6) == Blocks.fence) {
            this.worldObj.getBlock(var4, var5 - 1, var6);
         }
      }

      if (par3) {
         if (this.fallDistance > 0.0F) {
            this.fall(this.fallDistance);
            this.fallDistance = 0.0F;
         }
      } else if (par1 < (double)0.0F) {
         this.fallDistance = (float)((double)this.fallDistance - par1);
      }

   }

   public EntityLivingBase getAttackTarget() {
      EntityLivingBase e = super.getAttackTarget();
      if (e != null && !e.isEntityAlive()) {
         e = null;
      }

      return e;
   }

   public int getTotalArmorValue() {
      int var1 = super.getTotalArmorValue() + this.golemType.armor;
      if (this.decoration.contains("V")) {
         ++var1;
      }

      if (this.decoration.contains("P")) {
         var1 += 4;
      }

      if (var1 > 20) {
         var1 = 20;
      }

      return var1;
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      int i = 0;
      if (par1Entity instanceof EntityLivingBase) {
         f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)par1Entity);
         i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)par1Entity);
      }

      boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
      if (flag) {
         if (this.decoration.contains("V")) {
            EntityUtils.setRecentlyHit((EntityLivingBase)par1Entity, 100);
         }

         if (i > 0) {
            par1Entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F);
            this.motionX *= 0.6;
            this.motionZ *= 0.6;
         }

         int j = EnchantmentHelper.getFireAspectModifier(this) + this.getUpgradeAmount(2);
         if (j > 0) {
            par1Entity.setFire(j * 4);
         }

         if (par1Entity instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase)par1Entity, this);
         }

         EnchantmentHelper.func_151385_b(this, par1Entity);
      }

      return flag;
   }

   public boolean attackEntityFrom(DamageSource ds, float par2) {
      this.paused = false;
      if (ds == DamageSource.cactus) {
         return false;
      } else {
         if (this.getGolemType() == EnumGolemType.THAUMIUM && ds == DamageSource.magic) {
            par2 *= 0.5F;
         }

         if (ds.getSourceOfDamage() != null && this.getUpgradeAmount(5) > 0 && ds.getSourceOfDamage().getEntityId() != this.getEntityId()) {
            ds.getSourceOfDamage().attackEntityFrom(DamageSource.causeThornsDamage(this), (float)(this.getUpgradeAmount(5) * 2 + this.rand.nextInt(2 * this.getUpgradeAmount(5))));
            ds.getSourceOfDamage().playSound("damage.thorns", 0.5F, 1.0F);
         }

         return super.attackEntityFrom(ds, par2);
      }
   }

   public boolean canAttackClass(Class par1Class) {
      return EntityVillager.class != par1Class && EntityGolemBase.class != par1Class && EntityBat.class != par1Class;
   }

   public boolean isValidTarget(Entity target) {
      if (!target.isEntityAlive()) {
         return false;
      } else if (target instanceof EntityPlayer && target.getCommandSenderName().equals(this.getOwnerName())) {
         return false;
      } else if (!this.isWithinHomeDistance(MathHelper.floor_double(target.posX), MathHelper.floor_double(target.posY), MathHelper.floor_double(target.posZ))) {
         return false;
      } else {
         if (this.getCore() == 9) {
            if ((target instanceof IAnimals) && !(target instanceof IMob) && (!(target instanceof EntityTameable) || !((EntityTameable)target).isTamed()) && !(target instanceof EntityGolem)) {
                return !(target instanceof EntityAnimal) || !((EntityAnimal) target).isChild();
            }
         } else {
            if (this.canAttackCreepers() && this.getUpgradeAmount(4) > 0 && target instanceof EntityCreeper) {
               return true;
            }

            if (this.canAttackHostiles() && (target instanceof IMob) && !(target instanceof EntityCreeper)) {
               return true;
            }

            if (this.canAttackAnimals() && this.getUpgradeAmount(4) > 0 && (target instanceof IAnimals) && !(target instanceof IMob) && (!(target instanceof EntityTameable) || !((EntityTameable)target).isTamed()) && !(target instanceof EntityGolem)) {
               return true;
            }

             return this.canAttackPlayers() && this.getUpgradeAmount(4) > 0 && target instanceof EntityPlayer;
         }

         return false;
      }
   }

   public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLiving) {
      EntityDart var2 = new EntityDart(this.worldObj, this, par1EntityLiving, 1.6F, 7.0F - (float)this.getUpgradeAmount(3) * 1.75F);
      float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      var2.setDamage(f * 0.4F);
      this.playSound("thaumcraft:golemironshoot", 0.5F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.6F));
      this.worldObj.spawnEntityInWorld(var2);
      this.startLeftArmTimer();
   }

   public int getAttackSpeed() {
      return 20 - (this.advanced ? 2 : 0);
   }

   protected String getLivingSound() {
      return "thaumcraft:cameraclack";
   }

   protected String getHurtSound() {
      return "thaumcraft:cameraclack";
   }

   protected String getDeathSound() {
      return "thaumcraft:cameraclack";
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeByte(this.getCore());
      data.writeBoolean(this.advanced);
      data.writeByte(this.inventory.slotCount);
      data.writeByte(this.upgrades.length);

      for(byte b : this.upgrades) {
         data.writeByte(b);
      }

   }

   public void readSpawnData(ByteBuf data) {
      try {
         this.setCore(data.readByte());
         this.advanced = data.readBoolean();
         if (this.getCore() >= 0) {
            this.bootup = 0.0F;
         }

         this.inventory = new InventoryMob(this, data.readByte());
         this.colors = new byte[this.inventory.slotCount];

         for(int a = 0; a < this.inventory.slotCount; ++a) {
            this.colors[a] = -1;
         }

         this.upgrades = new byte[data.readByte()];

         for(int a = 0; a < this.upgrades.length; ++a) {
            this.upgrades[a] = data.readByte();
         }

         int bonus = 0;

         try {
            bonus = this.getGolemDecoration().contains("H") ? 5 : 0;
         } catch (Exception ignored) {
         }

         this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getGolemType().health + bonus);
      } catch (Exception ignored) {
      }

   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag() ? this.getCustomNameTag() : StatCollector.translateToLocal("item.ItemGolemPlacer." + this.getGolemType().ordinal() + ".name");
   }
}
