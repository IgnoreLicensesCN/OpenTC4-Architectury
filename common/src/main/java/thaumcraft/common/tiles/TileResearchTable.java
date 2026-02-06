package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import thaumcraft.api.IScribeTools;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.misc.ItemResearchNotes;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.HexEntry;
import thaumcraft.common.lib.research.HexType;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexCoord;
import thaumcraft.common.lib.utils.InventoryUtils;

public class TileResearchTable extends TileThaumcraft implements IInventory {
   public ItemStack[] contents = new ItemStack[2];
   public AspectList<Aspect>bonusAspects = new AspectList<>();
   int nextRecalc = 0;
   Player researcher = null;
   public ResearchNoteData data = null;

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      NBTTagList var2 = nbttagcompound.getTagList("Inventory", 10);
      this.contents = new ItemStack[this.getSizeInventory()];

      for(int var3 = 0; var3 < Math.min(2, var2.tagCount()); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         int var5 = var4.getByte("Slot") & 255;
         if (var5 >= 0 && var5 < this.contents.length) {
            this.contents[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

      this.nextRecalc = nbttagcompound.getInteger("nextRecalc");
      this.bonusAspects = new AspectList<>();
      var2 = nbttagcompound.getTagList("bonusAspects", 10);

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         String var5 = var4.getString("tag");
         if (Aspect.getAspect(var5) != null) {
            this.bonusAspects.mergeWithHighest(Aspect.getAspect(var5), 1);
         }
      }

   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.contents.length; ++var3) {
         if (this.contents[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.contents[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      nbttagcompound.setTag("Inventory", var2);
      nbttagcompound.setInteger("nextRecalc", this.nextRecalc);
      var2 = new NBTTagList();

      for(Aspect aspect : this.bonusAspects.getAspects()) {
         if (aspect != null && this.bonusAspects.getAmount(aspect) > 0) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setString("tag", aspect.getAspectKey());
            var2.appendTag(var4);
         }
      }

      nbttagcompound.setTag("bonusAspects", var2);
   }

   public void updateEntity() {
      super.updateEntity();
      if (Platform.getEnvironment() != Env.CLIENT && this.nextRecalc++ > 600) {
         this.nextRecalc = 0;
         this.recalculateBonus();
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }

   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void markDirty() {
      super.markDirty();
      this.gatherResults();
   }

   public void gatherResults() {
      this.data = null;
      if (this.contents[1] != null && this.contents[1].getItem() instanceof ItemResearchNotes) {
         this.data = ResearchManager.getData(this.contents[1]);
      }
   }

   public void placeAspect(int q, int r, Aspect aspect, Player player) {
      if (this.data == null) {
         this.gatherResults();
      }

      if (ResearchManager.consumeInkFromTable(this.contents[0], false)) {
         if (this.contents[1] != null && this.contents[1].getItem() instanceof ItemResearchNotes && this.data != null && this.contents[1].getItemDamage() < 64) {
            boolean researchExpertiseUnlocked = ResearchManager.isResearchComplete(player.getCommandSenderName(), "RESEARCHER1");
            boolean researchMasteryUnlocked = ResearchManager.isResearchComplete(player.getCommandSenderName(), "RESEARCHER2");
            HexCoord hex = new HexCoord(q, r);
            HexEntry he = null;
            if (aspect != null) {
               he = new HexEntry(aspect, HexType.WRITTEN);
               if (researchMasteryUnlocked && this.level.rand.nextFloat() < 0.1F) {
                  this.level.playSoundAtEntity(player, "random.orb", 0.2F, 0.9F + player.level().rand.nextFloat() * 0.2F);
               }
               else if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspect) <= 0) {
                  this.bonusAspects.reduceAndRemoveIfNotPositive(aspect, 1);
                  player.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                  this.markDirty();
               } else {
                  Thaumcraft.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspect, (short)-1);
                  ResearchManager.scheduleSave(player);
                  PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspect.getAspectKey(), (short) 0, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspect)), (ServerPlayer)player);
               }
            } else {
               float f = this.level().rand.nextFloat();
               if (this.data.hexGrid.get(hex.toString()).aspect != null
                       && (researchExpertiseUnlocked && f < 0.25F || researchMasteryUnlocked && f < 0.5F)) {
                  this.level().playSoundAtEntity(player, "random.orb", 0.2F, 0.9F + player.level().rand.nextFloat() * 0.2F);
                  Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), this.data.hexGrid.get(hex.toString()).aspect, (short)1);
                  ResearchManager.scheduleSave(player);
                  PacketHandler.INSTANCE.sendTo(new PacketAspectPool(this.data.hexGrid.get(hex.toString()).aspect.getAspectKey(), (short) 0, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), this.data.hexGrid.get(hex.toString()).aspect)), (ServerPlayer)player);
               }

               he = HexEntry.EMPTY;
            }

            this.data.hexGrid.put(hex.toString(), he);
            this.data.hexes.put(hex.toString(), hex);
            ResearchManager.updateData(this.contents[1], this.data);
            ResearchManager.consumeInkFromTable(this.contents[0], true);
            if (Platform.getEnvironment() != Env.CLIENT && ResearchNoteData.checkResearchNoteCompletion(this.contents[1], this.data, player.getCommandSenderName())) {
               this.contents[1].setItemDamage(64);
               this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockTable, 1, 1);
            }
         }

         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }
   }

   private void recalculateBonus() {
      if (!this.level().isDaytime()
              && this.level().getBlockLightValue(this.xCoord, this.yCoord + 1, this.zCoord) < 4
              && !this.level().canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord)
              && this.level().rand.nextInt(20) == 0) {
         this.bonusAspects.mergeWithHighest(Aspects.ENTROPY, 1);
      }

      if ((float)this.yCoord > (float)this.level().getActualHeight() * 0.5F && this.level().rand.nextInt(20) == 0) {
         this.bonusAspects.mergeWithHighest(Aspects.AIR, 1);
      }

      if ((float)this.yCoord > (float)this.level().getActualHeight() * 0.66F && this.level().rand.nextInt(20) == 0) {
         this.bonusAspects.mergeWithHighest(Aspects.AIR, 1);
      }

      if ((float)this.yCoord > (float)this.level().getActualHeight() * 0.75F && this.level().rand.nextInt(20) == 0) {
         this.bonusAspects.mergeWithHighest(Aspects.AIR, 1);
      }

      for(int x = -8; x <= 8; ++x) {
         for(int z = -8; z <= 8; ++z) {
            for(int y = -8; y <= 8; ++y) {
               if (y + this.yCoord > 0 && y + this.yCoord < this.level().getActualHeight()) {
                  Block bi = this.level().getBlock(x + this.xCoord, y + this.yCoord, z + this.zCoord);
                  int md = this.level().getBlockMetadata(x + this.xCoord, y + this.yCoord, z + this.zCoord);
                  Material bm = bi.getMaterial();
                  if (bi == ConfigBlocks.blockCustomOre && md == 1) {
                     if (this.bonusAspects.getAmount(Aspects.AIR) < 1 && this.level().rand.nextInt(20) == 0) {
                        this.bonusAspects.mergeWithHighest(Aspects.AIR, 1);
                        return;
                     }
                  } else if (bi == ConfigBlocks.blockCrystal && md == 0) {
                     if (this.bonusAspects.getAmount(Aspects.AIR) < 1 && this.level().rand.nextInt(10) == 0) {
                        this.bonusAspects.mergeWithHighest(Aspects.AIR, 1);
                        return;
                     }
                  } else if (bm != Material.fire && bm != Material.lava && (bi != ConfigBlocks.blockCustomOre || md != 2)) {
                     if (bi == ConfigBlocks.blockCrystal && md == 1) {
                        if (this.bonusAspects.getAmount(Aspects.FIRE) < 1 && this.level().rand.nextInt(10) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.FIRE, 1);
                           return;
                        }
                     } else if (bm == Material.ground) {
                        if (this.bonusAspects.getAmount(Aspects.EARTH) < 1 && this.level().rand.nextInt(20) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.EARTH, 1);
                           return;
                        }
                     } else if (bi == ConfigBlocks.blockCustomOre && md == 4) {
                        if (this.bonusAspects.getAmount(Aspects.EARTH) < 1 && this.level().rand.nextInt(20) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.EARTH, 1);
                           return;
                        }
                     } else if (bi == ConfigBlocks.blockCrystal && md == 3) {
                        if (this.bonusAspects.getAmount(Aspects.EARTH) < 1 && this.level().rand.nextInt(10) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.EARTH, 1);
                           return;
                        }
                     } else if (bm == Material.water) {
                        if (this.bonusAspects.getAmount(Aspects.WATER) < 1 && this.level().rand.nextInt(15) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.WATER, 1);
                           return;
                        }
                     } else if (bi == ConfigBlocks.blockCustomOre && md == 3) {
                        if (this.bonusAspects.getAmount(Aspects.WATER) < 1 && this.level().rand.nextInt(20) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.WATER, 1);
                           return;
                        }
                     } else if (bi == ConfigBlocks.blockCrystal && md == 2) {
                        if (this.bonusAspects.getAmount(Aspects.WATER) < 1 && this.level().rand.nextInt(10) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.WATER, 1);
                           return;
                        }
                     } else if (bm != Material.circuits && bm != Material.piston) {
                        if (bi == ConfigBlocks.blockCustomOre && md == 5) {
                           if (this.bonusAspects.getAmount(Aspects.ORDER) < 1 && this.level().rand.nextInt(20) == 0) {
                              this.bonusAspects.mergeWithHighest(Aspects.ORDER, 1);
                              return;
                           }
                        } else if (bi == ConfigBlocks.blockCrystal && md == 4) {
                           if (this.bonusAspects.getAmount(Aspects.ORDER) < 1 && this.level().rand.nextInt(10) == 0) {
                              this.bonusAspects.mergeWithHighest(Aspects.ORDER, 1);
                              return;
                           }
                        } else if (bi == ConfigBlocks.blockCustomOre && md == 6) {
                           if (this.bonusAspects.getAmount(Aspects.ENTROPY) < 1 && this.level().rand.nextInt(20) == 0) {
                              this.bonusAspects.mergeWithHighest(Aspects.ENTROPY, 1);
                              return;
                           }
                        } else if (bi == ConfigBlocks.blockCrystal && md == 5 && this.bonusAspects.getAmount(Aspects.ENTROPY) < 1 && this.level().rand.nextInt(10) == 0) {
                           this.bonusAspects.mergeWithHighest(Aspects.ENTROPY, 1);
                           return;
                        }
                     } else if (this.bonusAspects.getAmount(Aspects.ORDER) < 1 && this.level().rand.nextInt(20) == 0) {
                        this.bonusAspects.mergeWithHighest(Aspects.ORDER, 1);
                        return;
                     }
                  } else if (this.bonusAspects.getAmount(Aspects.FIRE) < 1 && this.level().rand.nextInt(20) == 0) {
                     this.bonusAspects.mergeWithHighest(Aspects.FIRE, 1);
                     return;
                  }

                  if (bi == Blocks.bookshelf && this.level().rand.nextInt(300) == 0 || bi == ConfigBlocks.blockJar && md == 1 && this.level().rand.nextInt(200) == 0) {
                     Aspect[] aspects = new Aspect[0];
                     aspects = Aspects.ALL_ASPECTS.values().toArray(aspects);
                     this.bonusAspects.mergeWithHighest(aspects[this.level().rand.nextInt(aspects.length)], 1);
                     return;
                  }
               }
            }
         }
      }

   }

   public int getSizeInventory() {
      return 2;
   }

   public ItemStack getStackInSlot(int var1) {
      return this.contents[var1];
   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (this.contents[var1] != null) {
          ItemStack var3;
          if (this.contents[var1].stackSize <= var2) {
              var3 = this.contents[var1];
            this.contents[var1] = null;
          } else {
              var3 = this.contents[var1].splitStack(var2);
            if (this.contents[var1].stackSize == 0) {
               this.contents[var1] = null;
            }

          }
          this.markDirty();
          return var3;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int var1) {
      if (this.contents[var1] != null) {
         ItemStack var2 = this.contents[var1];
         this.contents[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.contents[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
   }

   public String getInventoryName() {
      return StatCollector.translateToLocal("tile.blockTable.research.name");
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(Player var1) {
      return this.level().getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && var1.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public boolean isItemValidForSlot(int i, ItemStack itemstack) {
       if (itemstack != null) {
           switch (i) {
               case 0:
                   if (itemstack.getItem() instanceof IScribeTools) {
                       return true;
                   }
                   break;
               case 1:
                   if (itemstack.getItem() == ConfigItems.itemResearchNotes && itemstack.getItemDamage() < 64) {
                       return true;
                   }
           }

       }
       return false;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2);
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.level() != null && (Platform.getEnvironment() == Env.CLIENT)) {
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }

   }

   public boolean receiveClientEvent(int i, int j) {
      if (i == 1) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            this.level().playSound(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:learn", 1.0F, 1.0F, false);
         }

         return true;
      } else {
         return super.receiveClientEvent(i, j);
      }
   }

   public void duplicate(Player player) {
      if (this.data == null) {
         this.gatherResults();
      }

      if (this.contents[1] != null && this.contents[1].getItem() instanceof ItemResearchNotes && this.data != null && this.contents[1].getItemDamage() == 64 && InventoryUtils.isPlayerCarrying(player, new ItemStack(Items.paper)) >= 0 && InventoryUtils.isPlayerCarrying(player, new ItemStack(Items.dye, 1, 0)) >= 0) {
         ResearchItem rr = ResearchItem.getResearch(this.data.key);

         for(Aspect aspect : rr.tags.getAspects()) {
            if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspect) < rr.tags.getAmount(aspect) + this.data.copies) {
               return;
            }
         }

         for(Aspect aspect : rr.tags.getAspects()) {
            Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspect, (short)(-(rr.tags.getAmount(aspect) + this.data.copies)));
            ResearchManager.scheduleSave(player);
            PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspect.getAspectKey(), (short) 0, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspect)), (ServerPlayer)player);
         }

         InventoryUtils.consumeInventoryItem(player, Items.paper, 0);
         InventoryUtils.consumeInventoryItem(player, Items.dye, 0);
         this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockTable, 1, 1);
         ++this.data.copies;
         ResearchManager.updateData(this.contents[1], this.data);
         ++this.contents[1].stackSize;
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }

   }
}
