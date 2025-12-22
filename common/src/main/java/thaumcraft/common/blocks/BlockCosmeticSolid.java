package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.misc.ItemWispEssence;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.TileWardingStone;

import java.util.List;
import java.util.Random;

//"0": "黑曜石图腾",
//    "1": "黑曜石瓦块",
//    "2": "旅行者铺路石",
//    "3": "守卫者铺路石",
//    "4": "神秘方块",
//    "5": "油脂方块",
//    "6": "奥术石块",
//    "7": "奥术石砖",
//    "8": "蕴灵黑曜石图腾",
//    "9": "傀儡脚镣",
//    "10": "激活傀儡脚镣",
//    "11": "荒古石头",
//    "12": "荒古岩石",
//    "13": "荒古石头",//not made,unknown usage,i want to merge to 11
//    "14": "陈年石头",
//    "15": "荒古石座",
public class BlockCosmeticSolid extends Block {
   public IIcon[] icon = new IIcon[27];

   public BlockCosmeticSolid() {
      super(Material.rock);
      this.setResistance(10.0F);
      this.setHardness(2.0F);
      this.setStepSound(soundTypeStone);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setTickRandomly(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:obsidiantile");
      this.icon[1] = ir.registerIcon("thaumcraft:obsidiantotembase");
      this.icon[2] = ir.registerIcon("thaumcraft:obsidiantotem1");
      this.icon[3] = ir.registerIcon("thaumcraft:obsidiantotem2");
      this.icon[4] = ir.registerIcon("thaumcraft:obsidiantotem3");
      this.icon[5] = ir.registerIcon("thaumcraft:obsidiantotem4");
      this.icon[6] = ir.registerIcon("thaumcraft:obsidiantotembaseshaded");
      
      this.icon[7] = ir.registerIcon("thaumcraft:paving_stone_travel");
      this.icon[8] = ir.registerIcon("thaumcraft:paving_stone_warding");

      this.icon[9] = ir.registerIcon("thaumcraft:thaumiumblock");

      this.icon[10] = ir.registerIcon("thaumcraft:tallowblock");
      this.icon[11] = ir.registerIcon("thaumcraft:tallowblock_top");

      this.icon[12] = ir.registerIcon("thaumcraft:pedestal_top");//arcane stone block
      this.icon[13] = ir.registerIcon("thaumcraft:arcane_stone");//...brick

      this.icon[14] = ir.registerIcon("thaumcraft:golem_stone_top");
      this.icon[15] = ir.registerIcon("thaumcraft:golem_stone_side");
      this.icon[16] = ir.registerIcon("thaumcraft:golem_stone_top_active");

      this.icon[17] = ir.registerIcon("thaumcraft:ancient_stone_1");
      this.icon[18] = ir.registerIcon("thaumcraft:ancient_stone_2");
      this.icon[19] = ir.registerIcon("thaumcraft:ancient_stone_3");
      this.icon[20] = ir.registerIcon("thaumcraft:ancient_stone_4");

      this.icon[21] = ir.registerIcon("thaumcraft:ancient_rock_1");
      this.icon[22] = ir.registerIcon("thaumcraft:ancient_rock_2");
      this.icon[23] = ir.registerIcon("thaumcraft:ancient_rock_3");
      this.icon[24] = ir.registerIcon("thaumcraft:ancient_rock_4");

      this.icon[25] = ir.registerIcon("thaumcraft:crust");
      this.icon[26] = ir.registerIcon("thaumcraft:es_p");
   }

   //0 = DOWN
   //1 = UP
   //2 = NORTH (-Z)
   //3 = SOUTH (+Z)
   //4 = WEST  (-X)
   //5 = EAST  (+X)
   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int direction, int meta) {
      if (meta > 1 && meta != 8) {
         if (meta == 2) {
            return this.icon[7];
         } else if (meta == 3) {
            return this.icon[8];
         } else if (meta == 4) {
            return this.icon[9];
         } else if (meta == 5) {
            return direction > 1 ? this.icon[10] : this.icon[11];
         } else if (meta == 6) {
            return this.icon[12];
         } else if (meta == 7) {
            return this.icon[13];
         } else if (meta != 9 && meta != 10) {
            if (meta != 11 && meta != 13) {
               if (meta == 12) {
                  return this.icon[21];
               } else if (meta == 14) {
                  return this.icon[25];
               } else if (meta == 15) {
                  return direction <= 1 ? this.icon[17] : this.icon[26];
               } else {
                  return super.getIcon(direction, meta);
               }
            } else {
               return this.icon[17];
            }
         } else {
            return direction == 0 ? this.icon[13] :
                    (direction == 1 ? (meta == 9 ? this.icon[14] : this.icon[16])
                    : this.icon[15]);
         }
      }
      else {
         return this.icon[0];
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess ba, int x, int y, int z, int side) {
      int md = ba.getBlockMetadata(x, y, z);
      if ((md == 0 || md == 8)
              && side > 1
              && side < 100
      ) {
         //totem

         if (ba.getBlock(x, y + 1, z) != this || ba.getBlockMetadata(x, y + 1, z) != 0 && ba.getBlockMetadata(x, y + 1, z) != 8) {
            //no totem at top
            boolean randomSideFlag = ba.getBlock(x, y - 1, z) == this && (ba.getBlockMetadata(x, y - 1, z) == 0
                    || ba.getBlockMetadata(x, y - 1, z) == 8);//totemAtBelow
            if (randomSideFlag) {
               return this.icon[2 + Math.abs((side + x % 4 + z % 4 + y % 4) % 4)];
            }
            return this.icon[1];
         } else {
            //totem at top
            return this.icon[6];
         }
      }
      else if (md != 11
              && md != 13
              && side < 100) {
         if (md == 12) {
            switch (side) {
               case 0:
               case 1:
                  return this.icon[21 + Math.abs(x % 2) + Math.abs(z % 2) * 2];
               case 2:
               case 3:
                  return this.icon[21 + Math.abs(x % 2) + Math.abs(y % 2) * 2];
               case 4:
               case 5:
                  return this.icon[21 + Math.abs(z % 2) + Math.abs(y % 2) * 2];
            }
         }

         return super.getIcon(ba, x, y, z, side);
      } else {
         String l = x + "" + y + z;
         Random r1 = new Random(Math.abs(l.hashCode() * 100) + 1);
         int i = r1.nextInt(12345 + side) % 4;
         return this.icon[17 + i];
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void setBlockBoundsForItemRender() {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md != 2 && md != 3 && md != 13 && super.canCreatureSpawn(type, world, x, y, z);
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 4));
      par3List.add(new ItemStack(par1, 1, 5));
      par3List.add(new ItemStack(par1, 1, 6));
      par3List.add(new ItemStack(par1, 1, 7));
      par3List.add(new ItemStack(par1, 1, 8));
      par3List.add(new ItemStack(par1, 1, 9));
      par3List.add(new ItemStack(par1, 1, 11));
      par3List.add(new ItemStack(par1, 1, 12));
      par3List.add(new ItemStack(par1, 1, 14));
      par3List.add(new ItemStack(par1, 1, 15));
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      if (world.getBlock(x, y, z) != this) {
         return 4.0F;
      } else {
         int md = world.getBlockMetadata(x, y, z);
         if (md > 1 && md != 8) {
            return md != 4 && md != 6 && md != 7 ? super.getBlockHardness(world, x, y, z) : 4.0F;
         } else {
            return 30.0F;
         }
      }
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      if (world.getBlock(x, y, z) != this) {
         return 0;
      } else {
         int md = world.getBlockMetadata(x, y, z);
         if (md == 2) {
            return 9;
         } else {
            return md == 14 ? 4 : super.getLightValue(world, x, y, z);
         }
      }
   }

   public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
      if (world.getBlock(x, y, z) != this) {
         return 20.0F;
      } else {
         int md = world.getBlockMetadata(x, y, z);
         if (md > 1 && md != 8) {
            return md != 4 && md != 6 && md != 7 ? super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ) : 20.0F;
         } else {
            return 999.0F;
         }
      }
   }

   public int quantityDropped(Random par1Random) {
      return 1;
   }

   public int damageDropped(int par1) {
      return par1 == 8 ? 1 : (par1 == 10 ? 9 : par1);
   }

   public void onEntityWalking(World world, int x, int y, int z, Entity e) {
      if (world.getBlock(x, y, z) == this) {
         int md = world.getBlockMetadata(x, y, z);
         if (md == 2 && e instanceof EntityLivingBase) {
            if ((Platform.getEnvironment() == Env.CLIENT)) {
               ClientFXUtils.blockSparkle(world, x, y, z, 32768, 5);
            }

            ((EntityLivingBase)e).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 40, 1));
            ((EntityLivingBase)e).addPotionEffect(new PotionEffect(Potion.jump.id, 40, 0));
         }

         super.onEntityWalking(world, x, y, z, e);
      }
   }

   public boolean hasTileEntity(int metadata) {
      if (metadata == 3) {
         return true;
      } else {
         return metadata == 8 || super.hasTileEntity(metadata);
      }
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 3) {
         return new TileWardingStone();
      } else {
         return metadata == 8 ? new AbstractNodeBlockEntity() : super.createTileEntity(world, metadata);
      }
   }

   public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
      if (meta == 8) {
         Thaumcraft.proxy.burst(world, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 1.0F);
         world.playSound((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:craftfail", 1.0F, 1.0F, false);
      }

      return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
   }

   public void onBlockHarvested(Level par1World, int par2, int par3, int par4, int par5, Player par6Player) {
      if (par1World.getBlock(par2, par3, par4) == this) {
         if (par5 == 8 && !(Platform.getEnvironment() == Env.CLIENT)) {
            TileEntity te = par1World.getTileEntity(par2, par3, par4);
            if (te instanceof INode && ((INode) te).getAspects().size() > 0) {
               for(Aspect aspect : ((INode)te).getAspects().getAspects()) {
                  for(int a = 0; a <= ((INode)te).getAspects().getAmount(aspect) / 10; ++a) {
                     if (((INode)te).getAspects().getAmount(aspect) >= 5) {
                        ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
                        new AspectList();
                        ((ItemWispEssence)ess.getItem()).setAspects(ess, (new AspectList()).addAll(aspect, 2));
                        this.dropBlockAsItem(par1World, par2, par3, par4, ess);
                     }
                  }
               }
            }
         }

         super.onBlockHarvested(par1World, par2, par3, par4, par5, par6Player);
      }
   }

   public void randomDisplayTick(World world, int x, int y, int z, Random random) {
      if (world.getBlock(x, y, z) == this) {
         int md = world.getBlockMetadata(x, y, z);
         if (md == 3) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
               for(int a = 0; a < Thaumcraft.proxy.particleCount(2); ++a) {
                  ClientFXUtils.blockRunes(world, x, (float)y + 0.7F, z, 0.2F + world.getRandom().nextFloat() * 0.4F, world.getRandom().nextFloat() * 0.3F, 0.8F + world.getRandom().nextFloat() * 0.2F, 20, -0.02F);
               }
            } else if (world.getBlock(x, y + 1, z) != ConfigBlocks.blockAiry && world.getBlock(x, y + 1, z).getBlocksMovement(world, x, y + 1, z) || world.getBlock(x, y + 2, z) != ConfigBlocks.blockAiry && world.getBlock(x, y + 1, z).getBlocksMovement(world, x, y + 1, z)) {
               for(int a = 0; a < Thaumcraft.proxy.particleCount(3); ++a) {
                  ClientFXUtils.blockRunes(world, x, (float)y + 0.7F, z, 0.9F + world.getRandom().nextFloat() * 0.1F, world.getRandom().nextFloat() * 0.3F, world.getRandom().nextFloat() * 0.3F, 24, -0.02F);
               }
            } else {
               List<Entity> list = (List<Entity>)world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(1.0F, 1.0F, 1.0F));
               if (!list.isEmpty()) {
                  for(Entity entity : list) {
                     if (entity instanceof EntityLivingBase && !(entity instanceof Player)) {
                        ClientFXUtils.blockRunes(world, x, (float)y + 0.6F + world.getRandom().nextFloat() * Math.max(0.8F, entity.getEyeHeight()), z, 0.6F + world.getRandom().nextFloat() * 0.4F, 0.0F, 0.3F + world.getRandom().nextFloat() * 0.7F, 20, 0.0F);
                        break;
                     }
                  }
               }
            }
         }

      }
   }

   public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
      return worldObj.getBlock(x, y, z) == this;//anazor drunk too much so that a lot of blocks can be used to activate beacon--this comment will be sent to OpenTC4-1.7.10
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      if (world.getBlock(x, y, z) == this) {
         int md = world.getBlockMetadata(x, y, z);
         if (md == 9 && world.isBlockIndirectlyGettingPowered(x, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 10, 3);
         } else if (md == 10 && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 9, 3);
         }
      }

      super.onNeighborBlockChange(world, x, y, z, block);
   }
}
