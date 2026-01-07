package thaumcraft.common.tiles;

@Deprecated(forRemoval = true)
public class TileArcaneFurnace /*extends TileThaumcraft*/ {
//   private ItemStack[] furnaceItemStacks = new ItemStack[32];
//   public int furnaceCookTime = 0;
//   public int furnaceMaxCookTime = 0;
//   public int speedyTime = 0;
//   public int facingX = -5;
//   public int facingZ = -5;
//
//   public int getSizeInventory() {
//      return this.furnaceItemStacks.length;
//   }
//
//   public ItemStack getStackInSlot(int i) {
//      return this.furnaceItemStacks[i];
//   }
//
//   public ItemStack decrStackSize(int i, int j) {
//      if (this.furnaceItemStacks[i] != null) {
//         if (this.furnaceItemStacks[i].stackSize <= j) {
//            ItemStack itemstack = this.furnaceItemStacks[i];
//            this.furnaceItemStacks[i] = null;
//            this.markDirty();
//            return itemstack;
//         } else {
//            ItemStack itemstack1 = this.furnaceItemStacks[i].splitStack(j);
//            if (this.furnaceItemStacks[i].stackSize == 0) {
//               this.furnaceItemStacks[i] = null;
//            }
//
//            this.markDirty();
//            return itemstack1;
//         }
//      } else {
//         return null;
//      }
//   }
//
//   public void setInventorySlotContents(int i, ItemStack itemstack) {
//      this.furnaceItemStacks[i] = itemstack;
//      if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
//         itemstack.stackSize = this.getInventoryStackLimit();
//      }
//
//      this.markDirty();
//   }
//
//   private int getInventoryStackLimit() {
//      return 64;
//   }
//
//   public void readFromNBT(NBTTagCompound nbttagcompound) {
//      super.readFromNBT(nbttagcompound);
//      NBTTagList nbttaglist = nbttagcompound.getTagList("ThaumcraftItems", 10);
//      this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
//
//      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
//         NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
//         byte byte0 = nbttagcompound1.getByte("Slot");
//         if (byte0 >= 0 && byte0 < this.furnaceItemStacks.length) {
//            this.furnaceItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
//         }
//      }
//
//      this.furnaceCookTime = nbttagcompound.getShort("CookTime");
//      this.speedyTime = nbttagcompound.getShort("SpeedyTime");
//   }
//
//   public void writeToNBT(NBTTagCompound nbttagcompound) {
//      super.writeToNBT(nbttagcompound);
//      nbttagcompound.setShort("CookTime", (short)this.furnaceCookTime);
//      nbttagcompound.setShort("SpeedyTime", (short)this.speedyTime);
//      NBTTagList nbttaglist = new NBTTagList();
//
//      for(int i = 0; i < this.furnaceItemStacks.length; ++i) {
//         if (this.furnaceItemStacks[i] != null) {
//            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
//            nbttagcompound1.setByte("Slot", (byte)i);
//            this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
//            nbttaglist.appendTag(nbttagcompound1);
//         }
//      }
//
//      nbttagcompound.setTag("ThaumcraftItems", nbttaglist);
//   }
//
//   public void updateEntity() {
//      super.updateEntity();
//      if (this.facingX == -5) {
//         this.getFacing();
//      }
//
//      if (Platform.getEnvironment() != Env.CLIENT) {
//         boolean cookedflag = false;
//         if (this.furnaceCookTime > 0) {
//            --this.furnaceCookTime;
//            cookedflag = true;
//         }
//
//         if (cookedflag && this.speedyTime > 0) {
//            --this.speedyTime;
//         }
//
//         if (this.speedyTime <= 0) {
//            this.speedyTime = VisNetHandler.drainVis(this.level(), this.xCoord, this.yCoord, this.zCoord, Aspect.FIRE, 5);
//         }
//
//         if (this.furnaceMaxCookTime == 0) {
//            this.furnaceMaxCookTime = this.calcCookTime();
//         }
//
//         if (this.furnaceCookTime > this.furnaceMaxCookTime) {
//            this.furnaceCookTime = this.furnaceMaxCookTime;
//         }
//
//         if (this.furnaceCookTime == 0 && cookedflag) {
//            for(int a = 0; a < this.getSizeInventory(); ++a) {
//               if (this.furnaceItemStacks[a] != null) {
//                  ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[a]);
//                  if (itemstack != null) {
//                     this.ejectItem(itemstack.copy(), this.furnaceItemStacks[a]);
//                     this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockArcaneFurnace, 3, 0);
//                     --this.furnaceItemStacks[a].stackSize;
//                     if (this.furnaceItemStacks[a].stackSize <= 0) {
//                        this.furnaceItemStacks[a] = null;
//                     }
//                     break;
//                  }
//               }
//            }
//         }
//
//         if (this.furnaceCookTime == 0 && !cookedflag) {
//            for(int a = 0; a < this.getSizeInventory(); ++a) {
//               if (this.furnaceItemStacks[a] != null && this.canSmelt(a)) {
//                  this.furnaceMaxCookTime = this.calcCookTime();
//                  this.furnaceCookTime = this.furnaceMaxCookTime;
//                  break;
//               }
//            }
//         }
//      }
//
//   }
//
//   private int getBellows() {
//      int bellows = 0;
//
//      for(Direction dir : Direction.VALID_DIRECTIONS) {
//         if (dir != Direction.UP) {
//            int xx = this.xCoord + dir.offsetX * 2;
//            int yy = this.yCoord + dir.offsetY * 2;
//            int zz = this.zCoord + dir.offsetZ * 2;
//            TileEntity tile = this.level().getTileEntity(xx, yy, zz);
//            if (tile instanceof TileBellows && ((TileBellows) tile).orientation == dir.getOpposite().ordinal() && !this.level().isBlockIndirectlyGettingPowered(xx, yy, zz)) {
//               ++bellows;
//            }
//         }
//      }
//
//      return Math.min(3, bellows);
//   }
//
//   private int calcCookTime() {
//      return (this.speedyTime > 0 ? 80 : 140) - 20 * this.getBellows();
//   }
//
//   public boolean addItemsToInventory(ItemStack items) {
//      for(int a = 0; a < this.getSizeInventory(); ++a) {
//         if (this.furnaceItemStacks[a] != null && this.furnaceItemStacks[a].isItemEqual(items) && this.furnaceItemStacks[a].stackSize + items.stackSize <= items.getMaxStackSize()) {
//            ItemStack var10000 = this.furnaceItemStacks[a];
//            var10000.stackSize += items.stackSize;
//            if (!this.canSmelt(a)) {
//               this.destroyItem(a);
//            }
//
//            this.markDirty();
//            return true;
//         }
//
//         if (this.furnaceItemStacks[a] == null) {
//            this.setInventorySlotContents(a, items);
//            if (!this.canSmelt(a)) {
//               this.destroyItem(a);
//            }
//
//            this.markDirty();
//            return true;
//         }
//      }
//
//      return false;
//   }
//
//   private void destroyItem(int slot) {
//      this.furnaceItemStacks[slot] = null;
//      this.level().playSound((float)this.xCoord + 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.5F, "random.fizz", 0.3F, 2.6F + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.8F, false);
//      double var21 = (float)this.xCoord + this.level().rand.nextFloat();
//      double var22 = this.yCoord + 1;
//      double var23 = (float)this.zCoord + this.level().rand.nextFloat();
//      this.level().spawnParticle("lava", var21, var22, var23, 0.0F, 0.0F, 0.0F);
//   }
//
//   private void getFacing() {
//      this.facingX = 0;
//      this.facingZ = 0;
//      if (this.level().getBlock(this.xCoord - 1, this.yCoord, this.zCoord) == ConfigBlocks.blockArcaneFurnace && this.level().getBlockMetadata(this.xCoord - 1, this.yCoord, this.zCoord) == 10) {
//         this.facingX = -1;
//      } else if (this.level().getBlock(this.xCoord + 1, this.yCoord, this.zCoord) == ConfigBlocks.blockArcaneFurnace && this.level().getBlockMetadata(this.xCoord + 1, this.yCoord, this.zCoord) == 10) {
//         this.facingX = 1;
//      } else if (this.level().getBlock(this.xCoord, this.yCoord, this.zCoord - 1) == ConfigBlocks.blockArcaneFurnace && this.level().getBlockMetadata(this.xCoord, this.yCoord, this.zCoord - 1) == 10) {
//         this.facingZ = -1;
//      } else {
//         this.facingZ = 1;
//      }
//
//   }
//
//   public void ejectItem(ItemStack items, ItemStack furnaceItemStack) {
//      if (items != null) {
//         ItemStack bit = items.copy();
//         int bellows = this.getBellows();
//         float lx = 0.5F;
//         lx += (float)this.facingX * 1.2F;
//         float lz = 0.5F;
//         lz += (float)this.facingZ * 1.2F;
//         float mx = this.facingX == 0 ? (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.03F : (float)this.facingX * 0.13F;
//         float mz = this.facingZ == 0 ? (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.03F : (float)this.facingZ * 0.13F;
//         EntityItem entityitem = new EntityItem(this.level(), (float)this.xCoord + lx, (float)this.yCoord + 0.4F, (float)this.zCoord + lz, items);
//         entityitem.motionX = mx;
//         entityitem.motionZ = mz;
//         entityitem.motionY = 0.0F;
//         this.level().spawnEntityInWorld(entityitem);
//         if (ThaumcraftApi.getSmeltingBonus(furnaceItemStack) != null) {
//            ItemStack bonus = ThaumcraftApi.getSmeltingBonus(furnaceItemStack).copy();
//            if (bonus != null) {
//               if (bellows == 0) {
//                  if (this.level().rand.nextInt(4) == 0) {
//                     ++bonus.stackSize;
//                  }
//               } else {
//                  for(int a = 0; a < bellows; ++a) {
//                     if (this.level().rand.nextFloat() < 0.44F) {
//                        ++bonus.stackSize;
//                     }
//                  }
//               }
//            }
//
//            if (bonus != null && bonus.stackSize > 0) {
//               mx = this.facingX == 0 ? (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.03F : (float)this.facingX * 0.13F;
//               mz = this.facingZ == 0 ? (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.03F : (float)this.facingZ * 0.13F;
//               EntityItem entityitem2 = new EntityItem(this.level(), (float)this.xCoord + lx, (float)this.yCoord + 0.4F, (float)this.zCoord + lz, bonus);
//               entityitem2.motionX = mx;
//               entityitem2.motionZ = mz;
//               entityitem2.motionY = 0.0F;
//               this.level().spawnEntityInWorld(entityitem2);
//            }
//         }
//
//         int var2 = items.stackSize;
//         float var3 = FurnaceRecipes.smelting().func_151398_b(bit);
//         if (var3 == 0.0F) {
//            var2 = 0;
//         } else if (var3 < 1.0F) {
//            int var4 = MathHelper.floor_float((float)var2 * var3);
//            if (var4 < MathHelper.ceiling_float_int((float)var2 * var3) && (float)Math.random() < (float)var2 * var3 - (float)var4) {
//               ++var4;
//            }
//
//            var2 = var4;
//         }
//
//         while(var2 > 0) {
//            int var4 = EntityXPOrb.getXPSplit(var2);
//            var2 -= var4;
//            EntityXPOrb xp = new EntityXPOrb(this.level(), (float)this.xCoord + lx, (float)this.yCoord + 0.4F, (float)this.zCoord + lz, var4);
//            mx = this.facingX == 0 ? (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.025F : (float)this.facingX * 0.13F;
//            mz = this.facingZ == 0 ? (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.025F : (float)this.facingZ * 0.13F;
//            xp.motionX = mx;
//            xp.motionZ = mz;
//            xp.motionY = 0.0F;
//            this.level().spawnEntityInWorld(xp);
//         }
//
//      }
//   }
//
//   private boolean canSmelt(int slotIn) {
//      if (this.furnaceItemStacks[slotIn] == null) {
//         return false;
//      } else {
//         ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[slotIn]);
//         return itemstack != null;
//      }
//   }
//
//   public boolean receiveClientEvent(int i, int j) {
//      if (i != 3) {
//         return super.receiveClientEvent(i, j);
//      } else {
//         if ((Platform.getEnvironment() == Env.CLIENT)) {
//            for(int a = 0; a < 5; ++a) {
//               Thaumcraft.proxy.furnaceLavaFx(this.level(), this.xCoord, this.yCoord, this.zCoord, this.facingX, this.facingZ);
//               this.level().playSound((float)this.xCoord + 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.5F, "liquid.lavapop", 0.1F + this.level().rand.nextFloat() * 0.1F, 0.9F + this.level().rand.nextFloat() * 0.15F, false);
//            }
//         }
//
//         return true;
//      }
//   }
}
