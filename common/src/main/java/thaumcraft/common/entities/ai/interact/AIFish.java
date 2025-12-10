package thaumcraft.common.entities.ai.interact;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.item.ItemFishFood.FishType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.FurnaceRecipes;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EntityGolemBobber;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AIFish extends EntityAIBase {
   private EntityGolemBase theGolem;
   private float quality;
   private float distance;
   private World theWorld;
   private int maxDelay = 1;
   private int mod = 1;
   private int count = 0;
   private Vec3 target = null;
   private EntityGolemBobber bobber = null;
   private static final List LOOTCRAP;
   private static final List LOOTRARE;
   private static final List LOOTFISH;

   public AIFish(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.level();
      this.setMutexBits(3);
      this.distance = (float)MathHelper.ceiling_float_int(this.theGolem.getRange() / 2.0F);
   }

   public boolean shouldExecute() {
      if (this.target == null && this.count <= 0 && this.theGolem.ticksExisted % Config.golemDelay <= 0 && this.theGolem.getNavigator().noPath()) {
         if (this.bobber != null) {
            this.bobber.setDead();
         }

         Vec3 vv = this.findWater();
         if (vv == null) {
            return false;
         } else {
            this.target = Vec3.createVectorHelper(vv.xCoord, vv.yCoord, vv.zCoord);
            this.quality = 0.0F;
            int x = (int)this.target.xCoord;
            int y = (int)this.target.yCoord;
            int z = (int)this.target.zCoord;

            for(int a = 2; a <= 5; ++a) {
               ForgeDirection dir = ForgeDirection.getOrientation(a);
               if (this.theWorld.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).getMaterial() == Material.water && this.theWorld.isAirBlock(x + dir.offsetX, y + 1 + dir.offsetY, z + dir.offsetZ)) {
                  this.quality += 3.0E-5F;
                  if (this.theWorld.canBlockSeeTheSky(x + dir.offsetX, y + 1 + dir.offsetY, z + dir.offsetZ)) {
                     this.quality += 3.0E-5F;
                  }

                  for(int depth = 1; depth <= 3; ++depth) {
                     if (this.theWorld.getBlock(x + dir.offsetX, y - depth + dir.offsetY, z + dir.offsetZ).getMaterial() == Material.water) {
                        this.quality += 1.5E-5F;
                     }
                  }
               }
            }

            this.theWorld.playSoundAtEntity(this.theGolem, "random.bow", 0.5F, 0.4F / (this.theworld.getRandom().nextFloat() * 0.4F + 0.8F));
            this.bobber = new EntityGolemBobber(this.theWorld, this.theGolem, x, y, z);
            return this.theWorld.spawnEntityInWorld(this.bobber);
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.bobber != null && !this.bobber.isDead && this.target != null && this.count-- > 0;
   }

   public void updateTask() {
      if (this.target != null) {
         this.theGolem.getLookHelper().setLookPosition(this.target.xCoord + (double)0.5F, this.target.yCoord + (double)1.0F, this.target.zCoord + (double)0.5F, 30.0F, 30.0F);
         float chance = this.quality + (float)this.theGolem.getGolemStrength() * 1.5E-4F;
         if (this.theworld.getRandom().nextFloat() < chance) {
            this.theGolem.startRightArmTimer();
            int qq = 1;
            if (this.theGolem.getUpgradeAmount(0) > 0 && this.theworld.getRandom().nextInt(10) < this.theGolem.getUpgradeAmount(0)) {
               ++qq;
            }

            for(int a = 0; a < qq; ++a) {
               ItemStack fs = this.getFishingResult();
               if (this.theGolem.getUpgradeAmount(2) > 0) {
                  ItemStack sr = FurnaceRecipes.smelting().getSmeltingResult(fs);
                  if (sr != null) {
                     fs = sr.copy();
                  }
               }

               EntityItem entityitem = new EntityItem(this.theWorld, this.target.xCoord + (double)0.5F, this.target.yCoord + (double)1.0F, this.target.zCoord + (double)0.5F, fs);
               if (this.theGolem.getUpgradeAmount(2) > 0) {
                  entityitem.setFire(2);
               }

               entityitem.delayBeforeCanPickup = 20;
               double d1 = this.theGolem.posX + (double)this.theworld.getRandom().nextFloat() - (double)this.theworld.getRandom().nextFloat() - this.target.xCoord + (double)0.5F;
               double d3 = this.theGolem.posY - this.target.yCoord + (double)1.0F;
               double d5 = this.theGolem.posZ + (double)this.theworld.getRandom().nextFloat() - (double)this.theworld.getRandom().nextFloat() - this.target.zCoord + (double)0.5F;
               double d7 = MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
               double d9 = 0.1;
               entityitem.motionX = d1 * d9;
               entityitem.motionY = d3 * d9 + (double)MathHelper.sqrt_double(d7) * 0.08;
               entityitem.motionZ = d5 * d9;
               this.theWorld.spawnEntityInWorld(entityitem);
            }

            if (this.bobber != null) {
               this.bobber.playSound("random.splash", 0.15F, 1.0F + (this.theworld.getRandom().nextFloat() - this.theworld.getRandom().nextFloat()) * 0.4F);
               ((WorldServer)this.theWorld).func_147487_a("splash", this.bobber.posX, this.bobber.posY + (double)0.5F, this.bobber.posZ, 20 + this.theworld.getRandom().nextInt(20), 0.1F, 0.0F, 0.1F, 0.0F);
               this.bobber.setDead();
            }

            this.target = null;
         }
      }

   }

   public void resetTask() {
      if (this.bobber != null) {
         this.bobber.setDead();
      }

      this.target = null;
      this.count = -1;
   }

   public void startExecuting() {
      this.count = 300 + this.theworld.getRandom().nextInt(200);
      this.theGolem.startRightArmTimer();
   }

   private Vec3 findWater() {
      Random rand = this.theGolem.getRNG();

      for(int var2 = 0; (float)var2 < this.distance * 2.0F; ++var2) {
         int x = (int)((float)(this.theGolem.getHomePosition().posX + rand.nextInt((int)(1.0F + this.distance * 2.0F))) - this.distance);
         int y = (int)((float)(this.theGolem.getHomePosition().posY + rand.nextInt((int)(1.0F + this.distance))) - this.distance / 2.0F);
         int z = (int)((float)(this.theGolem.getHomePosition().posZ + rand.nextInt((int)(1.0F + this.distance * 2.0F))) - this.distance);
         if (this.theWorld.getBlock(x, y, z).getMaterial() == Material.water && this.theWorld.isAirBlock(x, y + 1, z)) {
            Vec3 v = Vec3.createVectorHelper(x, y, z);
            return v;
         }
      }

      return null;
   }

   private ItemStack getFishingResult() {
      float f = this.theworld.getRandom().nextFloat();
      float f1 = 0.1F - (float)this.theGolem.getUpgradeAmount(5) * 0.025F;
      float f2 = 0.05F + (float)this.theGolem.getUpgradeAmount(4) * 0.0125F;
      int x = (int)this.target.xCoord;
      int y = (int)this.target.yCoord;
      int z = (int)this.target.zCoord;

      for(int a = 2; a <= 5; ++a) {
         ForgeDirection dir = ForgeDirection.getOrientation(a);
         if (this.theWorld.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).getMaterial() == Material.water && this.theWorld.isAirBlock(x + dir.offsetX, y + 1 + dir.offsetY, z + dir.offsetZ)) {
            f1 -= 0.005F;
            f2 += 0.00125F;
            if (this.theWorld.canBlockSeeTheSky(x + dir.offsetX, y + 1 + dir.offsetY, z + dir.offsetZ)) {
               f1 -= 0.005F;
               f2 += 0.00125F;
            }

            for(int depth = 1; depth <= 3; ++depth) {
               if (this.theWorld.getBlock(x + dir.offsetX, y - depth + dir.offsetY, z + dir.offsetZ).getMaterial() == Material.water) {
                  f2 += 0.001F;
               }
            }
         }
      }

      f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
      f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
      if (f < f1) {
         return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.theworld.getRandom(), LOOTCRAP)).func_150708_a(this.theworld.getRandom());
      } else {
         f -= f1;
         if (f < f2) {
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.theworld.getRandom(), LOOTRARE)).func_150708_a(this.theworld.getRandom());
         } else {
            float var10000 = f - f2;
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.theworld.getRandom(), LOOTFISH)).func_150708_a(this.theworld.getRandom());
         }
      }
   }

   static {
      LOOTCRAP = Arrays.asList((new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 5), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10));
      LOOTRARE = Arrays.asList(new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack(Items.bow), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).func_150707_a());
      LOOTFISH = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.fish, 1, FishType.COD.func_150976_a()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, FishType.SALMON.func_150976_a()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, FishType.CLOWNFISH.func_150976_a()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, FishType.PUFFERFISH.func_150976_a()), 13));
   }
}
