package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockContainer;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileOwned;

import java.util.ArrayList;
import java.util.Random;

public class BlockArcaneDoor extends BlockContainer {
   public IIcon[] icon;

   public BlockArcaneDoor() {
      super(Material.iron);
      this.setStepSound(soundTypeMetal);
      this.disableStats();
      this.setResistance(999.0F);
      this.setHardness(Config.wardedStone ? -1.0F : 15.0F);
      float var3 = 0.5F;
      float var4 = 1.0F;
      this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = new IIcon[4];
      this.icon[0] = ir.registerIcon("thaumcraft:adoorbot");
      this.icon[1] = ir.registerIcon("thaumcraft:adoortop");
      this.icon[2] = new IconFlipped(this.icon[0], true, false);
      this.icon[3] = new IconFlipped(this.icon[1], true, false);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return this.icon[1];
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      if (par5 != 0 && par5 != 1) {
         int i1 = this.getFullMetadata(par1IBlockAccess, par2, par3, par4);
         int j1 = i1 & 3;
         boolean flag = (i1 & 4) != 0;
         boolean flag1 = false;
         boolean flag2 = (i1 & 8) != 0;
         if (flag) {
            if (j1 == 0 && par5 == 2) {
               flag1 = !flag1;
            } else if (j1 == 1 && par5 == 5) {
               flag1 = !flag1;
            } else if (j1 == 2 && par5 == 3) {
               flag1 = !flag1;
            } else if (j1 == 3 && par5 == 4) {
               flag1 = !flag1;
            }
         } else {
            if (j1 == 0 && par5 == 5) {
               flag1 = !flag1;
            } else if (j1 == 1 && par5 == 3) {
               flag1 = !flag1;
            } else if (j1 == 2 && par5 == 4) {
               flag1 = !flag1;
            } else if (j1 == 3 && par5 == 2) {
               flag1 = !flag1;
            }

            if ((i1 & 16) != 0) {
               flag1 = !flag1;
            }
         }

         return this.icon[(flag1 ? 2 : 0) + (flag2 ? 1 : 0)];
      } else {
         return this.icon[0];
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int var5 = this.getFullMetadata(par1IBlockAccess, par2, par3, par4);
      return (var5 & 4) != 0;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return 7;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(Level par1World, int par2, int par3, int par4) {
      this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
      return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(Level par1World, int par2, int par3, int par4) {
      this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
      return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      this.setDoorRotation(this.getFullMetadata(par1IBlockAccess, par2, par3, par4));
   }

   public int getDoorOrientation(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return this.getFullMetadata(par1IBlockAccess, par2, par3, par4) & 3;
   }

   public boolean isDoorOpen(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return (this.getFullMetadata(par1IBlockAccess, par2, par3, par4) & 4) != 0;
   }

   private void setDoorRotation(int par1) {
      float var2 = 0.1875F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
      int var3 = par1 & 3;
      boolean var4 = (par1 & 4) != 0;
      boolean var5 = (par1 & 16) != 0;
      if (var3 == 0) {
         if (var4) {
            if (!var5) {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            } else {
               this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
         }
      } else if (var3 == 1) {
         if (var4) {
            if (!var5) {
               this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            } else {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
         }
      } else if (var3 == 2) {
         if (var4) {
            if (!var5) {
               this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            } else {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }
         } else {
            this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         }
      } else {
         if (var4) {
            if (!var5) {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            } else {
               this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
         } else {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
         }
      }

   }

    public boolean onBlockActivated(World w, int x, int y, int z, Player p, int par6, float par7, float par8, float par9) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         TileEntity tile = w.getTileEntity(x, y, z);
         if (tile instanceof TileOwned) {
            if (!p.getCommandSenderName().equals(((TileOwned)tile).owner) && !((TileOwned)tile).accessList.contains("0" + p.getCommandSenderName()) && !((TileOwned)tile).accessList.contains("1" + p.getCommandSenderName())) {
               p.addChatMessage(new ChatComponentTranslation("The door refuses to budge."));
               w.playSoundEffect(x, y, z, "thaumcraft:doorfail", 0.66F, 1.0F);
            } else {
               int var10 = this.getFullMetadata(w, x, y, z);
               int var11 = var10 & 7;
               var11 ^= 4;
               if ((var10 & 8) == 0) {
                  w.setBlockMetadataWithNotify(x, y, z, var11, 2);
                  w.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
                  this.playDoorSound(w, x, y, z);
               } else {
                  w.setBlockMetadataWithNotify(x, y - 1, z, var11, 2);
                  w.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
                  this.playDoorSound(w, x, y, z);
               }
            }
         }
      }

      return true;
   }

   private void playDoorSound(World w, int x, int y, int z) {
      if (Math.random() < (double)0.5F) {
         w.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "random.door_open", 1.0F, w.rand.nextFloat() * 0.1F + 0.9F);
      } else {
         w.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "random.door_close", 1.0F, w.rand.nextFloat() * 0.1F + 0.9F);
      }

   }

   public void onPoweredBlockChange(Level par1World, int par2, int par3, int par4, boolean par5) {
      int var6 = this.getFullMetadata(par1World, par2, par3, par4);
      boolean var7 = (var6 & 4) != 0;
      if (var7 != par5) {
         int var8 = var6 & 7;
         var8 ^= 4;
         if ((var6 & 8) == 0) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var8, 2);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
         } else {
            par1World.setBlockMetadataWithNotify(par2, par3 - 1, par4, var8, 2);
            par1World.markBlockRangeForRenderUpdate(par2, par3 - 1, par4, par2, par3, par4);
         }

         par1World.playAuxSFXAtEntity(null, 1003, par2, par3, par4, 0);
      }

   }

   public void onNeighborBlockChange(Level par1World, int par2, int par3, int par4, Block par5) {
      int var6 = par1World.getBlockMetadata(par2, par3, par4);
      if (par5 == ConfigBlocks.blockWoodenDevice) {
         ArrayList<String> users = new ArrayList<>();
         TileEntity tile = par1World.getTileEntity(par2, par3, par4);
         if (tile instanceof TileOwned) {
            users.add(((TileOwned)tile).owner);

            for(String u : ((TileOwned)tile).accessList) {
               users.add(u.substring(1));
            }
         }

         int open = 0;

         label98:
         for(int a = 2; a <= 5; ++a) {
            Direction dir = Direction.getOrientation(a);
            Block bi = par1World.getBlock(par2 + dir.offsetX, par3 + dir.offsetY, par4 + dir.offsetZ);
            int md = par1World.getBlockMetadata(par2 + dir.offsetX, par3 + dir.offsetY, par4 + dir.offsetZ);
            if (bi == ConfigBlocks.blockWoodenDevice && md == 3) {
               TileOwned to = (TileOwned)par1World.getTileEntity(par2 + dir.offsetX, par3 + dir.offsetY, par4 + dir.offsetZ);
               if (to instanceof TileOwned) {
                  for(String u : users) {
                     if (to.owner.equals(u) || to.accessList.contains(u)) {
                        open = 1;
                        break label98;
                     }
                  }
               }
            } else if (bi == ConfigBlocks.blockWoodenDevice && md == 2) {
               TileOwned to = (TileOwned)par1World.getTileEntity(par2 + dir.offsetX, par3 + dir.offsetY, par4 + dir.offsetZ);
               if (to instanceof TileOwned) {
                  for(String u : users) {
                     if (to.owner.equals(u) || to.accessList.contains(u)) {
                        open = -1;
                        break;
                     }
                  }
               }
            }
         }

         if (open != 0) {
            this.onPoweredBlockChange(par1World, par2, par3, par4, open == 1);
         }
      } else if ((var6 & 8) == 0) {
         boolean var7 = false;
         if (par1World.getBlock(par2, par3 + 1, par4) != this) {
            par1World.setBlockToAir(par2, par3, par4);
            var7 = true;
         }

         if (var7 && !(Platform.getEnvironment() == Env.CLIENT)) {
            this.dropBlockAsItem(par1World, par2, par3, par4, var6, 0);
         }
      } else {
         if (par1World.getBlock(par2, par3 - 1, par4) != this) {
            par1World.setBlockToAir(par2, par3, par4);
         }

         if (par5 != Blocks.air && par5 != this) {
            this.onNeighborBlockChange(par1World, par2, par3 - 1, par4, par5);
         }
      }

   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return Config.wardedStone ? Item.getItemById(0) : ((par1 & 8) != 0 ? Item.getItemById(0) : ConfigItems.itemArcaneDoor);
   }

   public HitResult collisionRayTrace(Level par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3) {
      this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
      return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
   }

   public boolean canPlaceBlockAt(Level par1World, int par2, int par3, int par4) {
      return par3 < 255 && World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4) && super.canPlaceBlockAt(par1World, par2, par3, par4) && super.canPlaceBlockAt(par1World, par2, par3 + 1, par4);
   }

   public int getMobilityFlag() {
      return 2;
   }

   public int getFullMetadata(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
      boolean var6 = (var5 & 8) != 0;
      int var7;
      int var8;
      if (var6) {
         var7 = par1IBlockAccess.getBlockMetadata(par2, par3 - 1, par4);
         var8 = var5;
      } else {
         var7 = var5;
         var8 = par1IBlockAccess.getBlockMetadata(par2, par3 + 1, par4);
      }

      boolean var9 = (var8 & 1) != 0;
      return var7 & 7 | (var6 ? 8 : 0) | (var9 ? 16 : 0);
   }

   public TileEntity createNewTileEntity(World var1, int m) {
      return new TileOwned();
   }

   public boolean canHarvestBlock(Player player, int meta) {
      return true;
   }

   public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
      return false;
   }

   public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
   }
}
