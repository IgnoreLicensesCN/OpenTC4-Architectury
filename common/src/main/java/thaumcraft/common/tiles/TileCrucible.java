package thaumcraft.common.tiles;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.*;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainerBlockEntity;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

import java.awt.*;

public class TileCrucible extends TileThaumcraft implements IFluidHandler, IWandable, IAspectContainerBlockEntity {
   public short heat;
   public AspectList<Aspect>aspects = new AspectList<>();
   public final int maxTags = 100;
   int bellows = -1;
   private int delay = 0;
   public FluidTank tank;
   private long counter;
   int prevcolor;
   int prevx;
   int prevy;

   public TileCrucible() {
      this.tank = new FluidTank(FluidRegistry.WATER, 0, 1000);
      this.counter = -100L;
      this.prevcolor = 0;
      this.prevx = 0;
      this.prevy = 0;
      this.heat = 0;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.heat = nbttagcompound.getShort("Heat");
      this.tank.readFromNBT(nbttagcompound);
      if (nbttagcompound.hasKey("Empty")) {
         this.tank.setFluid(null);
      }

      this.aspects.readFromNBT(nbttagcompound);
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setShort("Heat", this.heat);
      this.tank.writeToNBT(nbttagcompound);
      this.aspects.writeToNBT(nbttagcompound);
   }

   public void updateEntity() {
      ++this.counter;
      int prevheat = this.heat;
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (this.bellows < 0) {
            this.getBellows();
         }

         if (this.tank.getFluidAmount() <= 0) {
            if (this.heat > 0) {
               --this.heat;
            }
         } else {
            Material mat = this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord).getMaterial();
            Block bi = this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord);
            int md = this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord);
            if (mat == Material.lava || mat == Material.fire || bi == ConfigBlocks.blockAiry && md == 1) {
               if (this.heat < 200) {
                  this.heat = (short)(this.heat + 1 + this.bellows * 2);
                  if (prevheat < 151 && this.heat >= 151) {
                     this.markDirty();
                     this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                  }
               }
            } else if (this.heat > 0) {
               --this.heat;
               if (this.heat == 149) {
                  this.markDirty();
                  this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
               }
            }
         }

         if (this.tagAmount() > 100 && this.counter % 5L == 0L) {
            AspectList<Aspect>tt = this.takeRandomFromSource();
            this.spill();
         }

         if (this.counter > 100L && this.heat > 150) {
            this.counter = 0L;
            if (this.tagAmount() > 0) {
               int s = this.aspects.getAspects().length;
               Aspect a = this.aspects.getAspects()[this.level().rand.nextInt(s)];
               if (a.isPrimal()) {
                  a = this.aspects.getAspects()[this.level().rand.nextInt(s)];
               }

               this.tank.drain(2, true);
               this.aspects.reduceAndRemoveIfNotPositive(a, 1);
               if (!a.isPrimal()) {
                  if (this.level().rand.nextBoolean()) {
                     this.aspects.addAll(a.getComponents()[0], 1);
                  } else {
                     this.aspects.addAll(a.getComponents()[1], 1);
                  }
               } else {
                  this.spill();
               }
            }

            this.markDirty();
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }
      } else if (this.tank.getFluidAmount() > 0) {
         this.drawEffects();
      }

      if ((Platform.getEnvironment() == Env.CLIENT) && prevheat < 151 && this.heat >= 151) {
         ++this.heat;
      }

   }

   private void drawEffects() {
      if (this.heat > 150) {
         Thaumcraft.proxy.crucibleFroth(this.level(), (float)this.xCoord + 0.2F + this.level().rand.nextFloat() * 0.6F, (float)this.yCoord + this.getFluidHeight(), (float)this.zCoord + 0.2F + this.level().rand.nextFloat() * 0.6F);
         if (this.tagAmount() > 100) {
            for(int a = 0; a < 2; ++a) {
               Thaumcraft.proxy.crucibleFrothDown(this.level(), (float)this.xCoord, (float)(this.yCoord + 1), (float)this.zCoord + this.level().rand.nextFloat());
               Thaumcraft.proxy.crucibleFrothDown(this.level(), (float)(this.xCoord + 1), (float)(this.yCoord + 1), (float)this.zCoord + this.level().rand.nextFloat());
               Thaumcraft.proxy.crucibleFrothDown(this.level(), (float)this.xCoord + this.level().rand.nextFloat(), (float)(this.yCoord + 1), (float)this.zCoord);
               Thaumcraft.proxy.crucibleFrothDown(this.level(), (float)this.xCoord + this.level().rand.nextFloat(), (float)(this.yCoord + 1), (float)(this.zCoord + 1));
            }
         }
      }

      if (this.level().rand.nextInt(6) == 0 && this.aspects.size() > 0) {
         int color = this.aspects.getAspects()[this.level().rand.nextInt(this.aspects.size())].getColor() - 16777216;
         int x = 5 + this.level().rand.nextInt(22);
         int y = 5 + this.level().rand.nextInt(22);
         this.delay = this.level().rand.nextInt(10);
         this.prevcolor = color;
         this.prevx = x;
         this.prevy = y;
         Color c = new Color(color);
         float r = (float)c.getRed() / 255.0F;
         float g = (float)c.getGreen() / 255.0F;
         float b = (float)c.getBlue() / 255.0F;
         Thaumcraft.proxy.crucibleBubble(this.level(), (float)this.xCoord + (float)x / 32.0F + 0.015625F, (float)this.yCoord + 0.05F + this.getFluidHeight(), (float)this.zCoord + (float)y / 32.0F + 0.015625F, r, g, b);
      }

   }

   public void spill() {
      if (this.level().rand.nextInt(4) == 0) {
         if (this.level().isAirBlock(this.xCoord, this.yCoord + 1, this.zCoord)) {
            if (this.level().rand.nextBoolean()) {
               this.level().setBlock(this.xCoord, this.yCoord + 1, this.zCoord, ConfigBlocks.blockFluxGas, 0, 3);
            } else {
               this.level().setBlock(this.xCoord, this.yCoord + 1, this.zCoord, ConfigBlocks.blockFluxGoo, 0, 3);
            }
         } else {
            Block bi = this.level().getBlock(this.xCoord, this.yCoord + 1, this.zCoord);
            int md = this.level().getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);
            if (bi == ConfigBlocks.blockFluxGoo && md < 7) {
               this.level().setBlock(this.xCoord, this.yCoord + 1, this.zCoord, ConfigBlocks.blockFluxGoo, md + 1, 3);
            } else if (bi == ConfigBlocks.blockFluxGas && md < 7) {
               this.level().setBlock(this.xCoord, this.yCoord + 1, this.zCoord, ConfigBlocks.blockFluxGas, md + 1, 3);
            } else {
               int x = -1 + this.level().rand.nextInt(3);
               int y = -1 + this.level().rand.nextInt(3);
               int z = -1 + this.level().rand.nextInt(3);
               if (this.level().isAirBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z)) {
                  if (this.level().rand.nextBoolean()) {
                     this.level().setBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z, ConfigBlocks.blockFluxGas, 0, 3);
                  } else {
                     this.level().setBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z, ConfigBlocks.blockFluxGoo, 0, 3);
                  }
               }
            }
         }
      }

   }

   public void spillRemnants() {
      if (this.tank.getFluidAmount() > 0 || this.aspects.visSize() > 0) {
         this.tank.setFluid(null);

         for(int a = 0; a < this.aspects.visSize() / 2; ++a) {
            this.spill();
         }

         this.aspects = new AspectList<>();
         this.markDirty();
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockMetalDevice, 2, 5);
      }

   }

   public void ejectItem(ItemStack items) {
      int stacks = 1;
      boolean first = true;

      do {
         ItemStack spitout = items.copy();
         if (spitout.stackSize > spitout.getMaxStackSize()) {
            spitout.stackSize = spitout.getMaxStackSize();
         }

         items.stackSize -= spitout.stackSize;
         EntitySpecialItem entityitem = new EntitySpecialItem(this.level(), (float)this.xCoord + 0.5F, (float)this.yCoord + 0.71F, (float)this.zCoord + 0.5F, spitout);
         entityitem.motionY = 0.1F;
         entityitem.motionX = first ? (double)0.0F : (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.01F);
         entityitem.motionZ = first ? (double)0.0F : (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.01F);
         this.level().spawnEntityInWorld(entityitem);
         first = false;
      } while(items.stackSize > 0);

   }

   public void attemptSmelt(EntityItem entity) {
      boolean bubble = false;
      boolean event = false;
      ItemStack item = entity.getEntityItem();
      NBTTagCompound itemData = entity.getEntityData();
      String username = itemData.getString("thrower");
      int stacksize = item.stackSize;

      for(int a = 0; a < stacksize; ++a) {
         CrucibleRecipe rc = ThaumcraftCraftingManager.findMatchingCrucibleRecipe(username, this.aspects, item);
         if (rc != null && this.tank.getFluidAmount() > 0) {
            ItemStack out = rc.getRecipeOutput().copy();
            Player p = this.level().getPlayerEntityByName(username);
            if (p != null) {
               FMLCommonHandler.instance().firePlayerCraftingEvent(p, out, new InventoryFake(new ItemStack[]{item}));
            }

            this.aspects = rc.removeMatching(this.aspects);
            this.tank.drain(50, true);
            this.ejectItem(out);
            event = true;
            --stacksize;
            this.counter = -250L;
         } else {
            AspectList<Aspect>ot = ThaumcraftCraftingManager.getObjectTags(item);
            ot = ThaumcraftCraftingManager.getBonusAspects(item, ot);
            if (ot == null || ot.size() == 0) {
               entity.motionY = 0.35F;
               entity.motionX = (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.2F;
               entity.motionZ = (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.2F;
               this.level().playSoundAtEntity(entity, "random.pop", 0.2F, (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.7F + 1.0F);
               return;
            }

            for(Aspect tag : ot.getAspects()) {
               this.aspects.addAll(tag, ot.getAmount(tag));
            }

            bubble = true;
            --stacksize;
            this.counter = -150L;
         }
      }

      if (bubble) {
         this.level().playSoundAtEntity(entity, "thaumcraft:bubble", 0.2F, 1.0F + this.level().rand.nextFloat() * 0.4F);
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockMetalDevice, 2, 1);
      }

      if (event) {
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockMetalDevice, 2, 5);
      }

      if (stacksize <= 0) {
         entity.setDead();
      } else {
         item.stackSize = stacksize;
         entity.setEntityItemStack(item);
      }

      this.markDirty();
   }

   public int tagAmount() {
      int tt = 0;
      if (this.aspects.size() <= 0) {
         return 0;
      } else {
         for(Aspect tag : this.aspects.getAspects()) {
            tt += this.aspects.getAmount(tag);
         }

         return tt;
      }
   }

   public float getFluidHeight() {
      float base = 0.3F + 0.5F * ((float)this.tank.getFluidAmount() / (float)this.tank.getCapacity());
      float out = base + (float)this.tagAmount() / 100.0F * (1.0F - base);
      if (out > 1.0F) {
         out = 1.001F;
      }

      if (out == 1.0F) {
         out = 0.9999F;
      }

      return out;
   }

   public AspectList<Aspect>takeRandomFromSource() {
      AspectList<Aspect>output = new AspectList<>();
      if (this.aspects.size() > 0) {
         Aspect tag = this.aspects.getAspects()[this.level().rand.nextInt(this.aspects.getAspects().length)];
         output.addAll(tag, 1);
         this.aspects.reduceAndRemoveIfNotPositive(tag, 1);
      }

      this.markDirty();
      this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      return output;
   }

   public boolean receiveClientEvent(int i, int j) {
      if (i == 1) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            ClientFXUtils.blockSparkle(this.level(), this.xCoord, this.yCoord, this.zCoord, -9999, 5);
         }

         return true;
      } else if (i != 2) {
         return super.receiveClientEvent(i, j);
      } else {
         ClientFXUtils.crucibleBoilSound(this.level(), this.xCoord, this.yCoord, this.zCoord);
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            for(int q = 0; q < 10; ++q) {
               int x = 5 + this.level().rand.nextInt(22);
               int y = 5 + this.level().rand.nextInt(22);
               Thaumcraft.proxy.crucibleBoil(this.level(), this.xCoord, this.yCoord, this.zCoord, this, j);
            }
         }

         return true;
      }
   }

   public void getBellows() {
      this.bellows = 0;

      for(int a = 2; a < 6; ++a) {
         Direction dir = Direction.getOrientation(a);
         int xx = this.xCoord + dir.offsetX;
         int zz = this.zCoord + dir.offsetZ;
         Block bi = this.level().getBlock(xx, this.yCoord, zz);
         int md = this.level().getBlockMetadata(xx, this.yCoord, zz);
         if (bi == ConfigBlocks.blockWoodenDevice && md == 0) {
            ++this.bellows;
         }
      }

   }

   public int fill(Direction from, FluidStack resource, boolean doFill) {
      if (resource != null && resource.getFluidID() != FluidRegistry.WATER.getID()) {
         return 0;
      } else {
         if (doFill) {
            this.markDirty();
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }

         return this.tank.fill(resource, doFill);
      }
   }

   public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
      if (resource != null && resource.isFluidEqual(this.tank.getFluid())) {
         if (doDrain) {
            this.markDirty();
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }

         return this.tank.drain(resource.amount, doDrain);
      } else {
         return null;
      }
   }

   public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
      return this.tank.drain(maxDrain, doDrain);
   }

   public boolean canFill(Direction from, Fluid fluid) {
      return fluid != null && fluid.getID() == FluidRegistry.WATER.getID();
   }

   public boolean canDrain(Direction from, Fluid fluid) {
      return true;
   }

   public FluidTankInfo[] getTankInfo(Direction from) {
      return new FluidTankInfo[]{this.tank.getInfo()};
   }

   public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
      return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
      if (Platform.getEnvironment() != Env.CLIENT && player.isSneaking()) {
         this.spillRemnants();
      }

      return wandstack;
   }

   public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
   }

   public @NotNull AspectList<Aspect>getAspects() {
      return this.aspects;
   }

   public void setAspects(AspectList<Aspect>aspects) {
   }

   public int addIntoContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList<Aspect>ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList<Aspect>ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }
}
