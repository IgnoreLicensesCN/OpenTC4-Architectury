package thaumcraft.common.tiles;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.IAspectContainerBlockEntity;
import thaumcraft.api.aspects.IEssentiaTransportBlockEntity;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;

public class TileThaumatorium extends TileThaumcraft implements IAspectContainerBlockEntity, IEssentiaTransportBlockEntity, ISidedInventory {
   public ItemStack inputStack = null;
   public AspectList<Aspect>essentia = new AspectList<>();
   public ArrayList<Integer> recipeHash = new ArrayList<>();
   public ArrayList<AspectList> recipeEssentia = new ArrayList<>();
   public ArrayList<String> recipePlayer = new ArrayList<>();
   public int currentCraft = -1;
   public int maxRecipes = 1;
   public Direction facing;
   public Aspect currentSuction;
   int venting;
   int counter;
   boolean heated;
   CrucibleRecipe currentRecipe;
   public Container eventHandler;

   public TileThaumatorium() {
      this.facing = Direction.NORTH;
      this.currentSuction = null;
      this.venting = 0;
      this.counter = 0;
      this.heated = false;
      this.currentRecipe = null;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2);
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = Direction.getOrientation(nbttagcompound.getByte("facing"));
      this.essentia.readFromNBT(nbttagcompound);
      this.maxRecipes = nbttagcompound.getByte("maxrec");
      this.recipeEssentia = new ArrayList<>();
      this.recipeHash = new ArrayList<>();
      this.recipePlayer = new ArrayList<>();
      int[] hashes = nbttagcompound.getIntArray("recipes");
      if (hashes != null) {
         for(int hash : hashes) {
            CrucibleRecipe recipe = CrucibleRecipe.getCrucibleRecipeFromHash(hash);
            if (recipe != null) {
               this.recipeEssentia.add(recipe.aspects.copy());
               this.recipePlayer.add("");
               this.recipeHash.add(hash);
            }
         }
      }

   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("facing", (byte)this.facing.ordinal());
      nbttagcompound.setByte("maxrec", (byte)this.maxRecipes);
      this.essentia.writeToNBT(nbttagcompound);
      int[] hashes = new int[this.recipeHash.size()];
      int a = 0;

      for(Integer i : this.recipeHash) {
         hashes[a] = i;
         ++a;
      }

      nbttagcompound.setIntArray("recipes", hashes);
   }

   public void readFromNBT(NBTTagCompound nbtCompound) {
      super.readFromNBT(nbtCompound);
      NBTTagList nbttaglist = nbtCompound.getTagList("ThaumcraftItems", 10);
      if (nbttaglist.tagCount() > 0) {
         this.inputStack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(0));
      }

      NBTTagList nbttaglist2 = nbtCompound.getTagList("OutputPlayer", 8);

      for(int a = 0; a < nbttaglist2.tagCount(); ++a) {
         if (this.recipePlayer.size() > a) {
            this.recipePlayer.set(a, nbttaglist2.getStringTagAt(a));
         }
      }

   }

   public void writeToNBT(NBTTagCompound nbtCompound) {
      super.writeToNBT(nbtCompound);
      NBTTagList nbttaglist = new NBTTagList();
      if (this.inputStack != null) {
         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
         nbttagcompound1.setByte("Slot", (byte)0);
         this.inputStack.writeToNBT(nbttagcompound1);
         nbttaglist.appendTag(nbttagcompound1);
      }

      nbtCompound.setTag("ThaumcraftItems", nbttaglist);
      NBTTagList nbttaglist2 = new NBTTagList();
      if (!this.recipePlayer.isEmpty()) {
          for (String s : this.recipePlayer) {
              if (s != null) {
                  NBTTagString nbttagcompound1 = new NBTTagString(s);
                  nbttaglist2.appendTag(nbttagcompound1);
              }
          }
      }

      nbtCompound.setTag("OutputPlayer", nbttaglist2);
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   boolean checkHeat() {
      Material mat = this.level().getBlock(this.xCoord, this.yCoord - 2, this.zCoord).getMaterial();
      Block bi = this.level().getBlock(this.xCoord, this.yCoord - 2, this.zCoord);
      int md = this.level().getBlockMetadata(this.xCoord, this.yCoord - 2, this.zCoord);
      return mat == Material.lava || mat == Material.fire || bi == ConfigBlocks.blockAiry && md == 1;
   }

   public ItemStack getCurrentOutputRecipe() {
      ItemStack out = null;
      if (this.currentCraft >= 0 && this.recipeHash != null && !this.recipeHash.isEmpty()) {
         CrucibleRecipe recipe = CrucibleRecipe.getCrucibleRecipeFromHash(this.recipeHash.get(this.currentCraft));
         if (recipe != null) {
            out = recipe.getRecipeOutput().copy();
         }
      }

      return out;
   }

   public void updateEntity() {
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (this.counter == 0 || this.counter % 40 == 0) {
            this.heated = this.checkHeat();
            this.getUpgrades();
         }

         ++this.counter;
         if (this.heated && !this.gettingPower() && this.counter % 5 == 0 && this.recipeHash != null && !this.recipeHash.isEmpty()) {
            if (this.inputStack == null) {
               this.currentSuction = null;
               return;
            }

            if (this.currentCraft < 0 || this.currentCraft >= this.recipeHash.size() || this.currentRecipe == null || !this.currentRecipe.catalystMatches(this.inputStack)) {
               for(int a = 0; a < this.recipeHash.size(); ++a) {
                  CrucibleRecipe recipe = CrucibleRecipe.getCrucibleRecipeFromHash(this.recipeHash.get(a));
                  if (recipe.catalystMatches(this.inputStack)) {
                     this.currentCraft = a;
                     this.currentRecipe = recipe;
                     break;
                  }
               }
            }

            if (this.currentCraft < 0 || this.currentCraft >= this.recipeHash.size()) {
               return;
            }

            TileEntity inventory = this.level().getTileEntity(this.xCoord + this.facing.offsetX, this.yCoord, this.zCoord + this.facing.offsetZ);
            if (inventory instanceof IInventory) {
               ItemStack dropped = this.getCurrentOutputRecipe();
               dropped = InventoryUtils.placeItemStackIntoInventory(dropped, (IInventory)inventory, this.facing.getOpposite().ordinal(), false);
               if (dropped != null) {
                  return;
               }
            }

            boolean done = true;
            this.currentSuction = null;

            for(Aspect aspect : this.recipeEssentia.get(this.currentCraft).getAspectsSorted()) {
               if (this.essentia.getAmount(aspect) < this.recipeEssentia.get(this.currentCraft).getAmount(aspect)) {
                  this.currentSuction = aspect;
                  done = false;
                  break;
               }
            }

            if (done) {
               this.completeRecipe();
            } else if (this.currentSuction != null) {
               this.fill();
            }
         }
      } else if (this.venting > 0) {
         --this.venting;
         float fx = 0.1F - this.level().rand.nextFloat() * 0.2F;
         float fz = 0.1F - this.level().rand.nextFloat() * 0.2F;
         float fy = 0.1F - this.level().rand.nextFloat() * 0.2F;
         float fx2 = 0.1F - this.level().rand.nextFloat() * 0.2F;
         float fz2 = 0.1F - this.level().rand.nextFloat() * 0.2F;
         float fy2 = 0.1F - this.level().rand.nextFloat() * 0.2F;
         int color = 16777215;
         Thaumcraft.proxy.drawVentParticles(this.level(), (float)this.xCoord + 0.5F + fx + (float)this.facing.offsetX / 2.0F, (float)this.yCoord + 0.5F + fy, (float)this.zCoord + 0.5F + fz + (float)this.facing.offsetZ / 2.0F, (float)this.facing.offsetX / 4.0F + fx2, fy2, (float)this.facing.offsetZ / 4.0F + fz2, color);
      }

   }

   private void completeRecipe() {
      if (this.currentRecipe != null && this.currentCraft < this.recipeHash.size() && this.currentRecipe.matches(this.essentia, this.inputStack) && this.decrStackSize(0, 1) != null) {
         this.essentia = new AspectList<>();
         ItemStack dropped = this.getCurrentOutputRecipe();
         Player p = this.level().getPlayerEntityByName(this.recipePlayer.get(this.currentCraft));
         if (p != null) {
            FMLCommonHandler.instance().firePlayerCraftingEvent(p, dropped, new InventoryFake(new ItemStack[]{this.inputStack}));
         }

         TileEntity inventory = this.level().getTileEntity(this.xCoord + this.facing.offsetX, this.yCoord, this.zCoord + this.facing.offsetZ);
         if (inventory instanceof IInventory) {
            dropped = InventoryUtils.placeItemStackIntoInventory(dropped, (IInventory)inventory, this.facing.getOpposite().ordinal(), true);
         }

         if (dropped != null) {
            EntityItem ei = new EntityItem(this.level(), (double)this.xCoord + (double)0.5F + (double)this.facing.offsetX * 0.66, (double)this.yCoord + 0.33 + (double)this.facing.getOpposite().offsetY, (double)this.zCoord + (double)0.5F + (double)this.facing.offsetZ * 0.66, dropped.copy());
            ei.motionX = 0.075F * (float)this.facing.offsetX;
            ei.motionY = 0.025F;
            ei.motionZ = 0.075F * (float)this.facing.offsetZ;
            this.level().spawnEntityInWorld(ei);
            this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 0, 0);
         }

         this.level().playSoundEffect((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "random.fizz", 0.25F, 2.6F + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.8F);
         this.currentCraft = -1;
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }

   }

   void fill() {
      TileEntity te = null;
      IEssentiaTransportBlockEntity ic = null;

      for(int y = 0; y <= 1; ++y) {
         for(Direction dir : Direction.VALID_DIRECTIONS) {
            if (dir != this.facing && dir != Direction.DOWN && (y != 0 || dir != Direction.UP)) {
               te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord + y, this.zCoord, dir);
               if (te != null) {
                  ic = (IEssentiaTransportBlockEntity)te;
                  if (ic.getEssentiaAmount(dir.getOpposite()) > 0 && ic.getSuctionAmount(dir.getOpposite()) < this.getSuctionAmount(null) && this.getSuctionAmount(null) >= ic.getMinimumSuctionToDrainOut()) {
                     int ess = ic.takeEssentia(this.currentSuction, 1, dir.getOpposite());
                     if (ess > 0) {
                        this.addIntoContainer(this.currentSuction, ess);
                        return;
                     }
                  }
               }
            }
         }
      }

   }

   public int addIntoContainer(Aspect tt, int am) {
      int ce = this.currentRecipe.aspects.getAmount(tt) - this.essentia.getAmount(tt);
      if (this.currentRecipe != null && ce > 0) {
         int add = Math.min(ce, am);
         this.essentia.addAll(tt, add);
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
         return am - add;
      } else {
         return am;
      }
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.essentia.getAmount(tt) >= am) {
         this.essentia.reduceAndRemoveIfNotPositive(tt, am);
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList<Aspect>ot) {
      return false;
   }

   public boolean doesContainerContain(AspectList<Aspect>ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tt, int am) {
      return this.essentia.getAmount(tt) >= am;
   }

   public int containerContains(Aspect tt) {
      return this.essentia.getAmount(tt);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean receiveClientEvent(int i, int j) {
      if (i >= 0) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            this.venting = 7;
         }

         return true;
      } else {
         return super.receiveClientEvent(i, j);
      }
   }

   public boolean isConnectable(Direction face) {
      return face != this.facing;
   }

   public boolean canInputFrom(Direction face) {
      return face != this.facing;
   }

   public boolean canOutputTo(Direction face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
      this.currentSuction = aspect;
   }

   public Aspect getSuctionType(Direction loc) {
      return this.currentSuction;
   }

   public int getSuctionAmount(Direction loc) {
      return this.currentSuction != null ? 128 : 0;
   }

   public @NotNull Aspect getEssentiaType(Direction loc) {
      return null;
   }

   public int getEssentiaAmount(Direction loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, Direction face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, Direction face) {
      return this.canInputFrom(face) ? amount - this.addIntoContainer(aspect, amount) : 0;
   }

   public int getMinimumSuctionToDrainOut() {
      return 0;
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public @NotNull AspectList<Aspect>getAspects() {
      return this.essentia;
   }

   public void setAspects(AspectList<Aspect>aspects) {
      this.essentia = aspects;
   }

   public int getSizeInventory() {
      return 1;
   }

   public ItemStack getStackInSlot(int par1) {
      return this.inputStack;
   }

   public ItemStack decrStackSize(int par1, int par2) {
      if (this.inputStack != null) {
          ItemStack itemstack;
          if (this.inputStack.stackSize <= par2) {
              itemstack = this.inputStack;
            this.inputStack = null;

          } else {
              itemstack = this.inputStack.splitStack(par2);
            if (this.inputStack.stackSize == 0) {
               this.inputStack = null;
            }

          }
          if (this.eventHandler != null) {
             this.eventHandler.onCraftMatrixChanged(this);
          }
          return itemstack;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.inputStack != null) {
         ItemStack itemstack = this.inputStack;
         this.inputStack = null;
         return itemstack;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.inputStack = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

      if (this.eventHandler != null) {
         this.eventHandler.onCraftMatrixChanged(this);
      }

   }

   public String getInventoryName() {
      return "container.alchemyfurnace";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(Player par1Player) {
      return this.level().getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1Player.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
      return true;
   }

   public int[] getAccessibleSlotsFromSide(int par1) {
      return new int[]{0};
   }

   public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
      return true;
   }

   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return true;
   }

   public boolean gettingPower() {
      return this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord - 1, this.zCoord) || this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord + 1, this.zCoord);
   }

   public void getUpgrades() {
      int mr = 1;

      for(int yy = 0; yy <= 1; ++yy) {
         for(Direction dir : Direction.VALID_DIRECTIONS) {
            if (dir != Direction.DOWN && dir != this.facing) {
               int xx = this.xCoord + dir.offsetX;
               int zz = this.zCoord + dir.offsetZ;
               Block bi = this.level().getBlock(xx, this.yCoord + yy + dir.offsetY, zz);
               int md = this.level().getBlockMetadata(xx, this.yCoord + yy + dir.offsetY, zz);
               if (bi == ConfigBlocks.blockMetalDevice && md == 12) {
                  TileEntity te = this.level().getTileEntity(xx, this.yCoord + yy + dir.offsetY, zz);
                  if (te instanceof TileBrainbox && ((TileBrainbox) te).facing == dir.getOpposite()) {
                     mr += 2;
                  }
               }
            }
         }
      }

      if (mr != this.maxRecipes) {
         this.maxRecipes = mr;

         while(this.recipeHash.size() > this.maxRecipes) {
            this.recipeHash.remove(this.recipeHash.size() - 1);
         }

         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }

   }
}
