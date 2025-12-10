package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockMagicalLeaves extends Block implements IShearable {
    public static final String[] leafType = new String[]{"greatwood", "silverwood"};
    int[] adjacentTreeBlocks;
    public IIcon[] icon = new IIcon[4];

    public BlockMagicalLeaves() {
        super(Material.leaves);
        this.setTickRandomly(true);
        this.setCreativeTab(Thaumcraft.tabTC);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
    }

   @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.icon[0] = ir.registerIcon("thaumcraft:greatwoodleaves");
        this.icon[1] = ir.registerIcon("thaumcraft:greatwoodleaveslow");
        this.icon[2] = ir.registerIcon("thaumcraft:silverwoodleaves");
        this.icon[3] = ir.registerIcon("thaumcraft:silverwoodleaveslow");
    }

   @Override
    public IIcon getIcon(int par1, int par2) {
        int idx = !Blocks.leaves.isOpaqueCube() ? 0 : 1;
        return (par2 & 1) == 1 ? this.icon[idx + 2] : this.icon[idx];
    }

   @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

   @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        Block var6 = par1IBlockAccess.getBlock(par2, par3, par4);
        return (!Blocks.leaves.isOpaqueCube() || var6 != this) && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        double var1 = 0.5F;
        double var3 = 1.0F;
        return ColorizerFoliage.getFoliageColor(var1, var3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int par1) {
        return (par1 & 1) == 0 ? ColorizerFoliage.getFoliageColorBasic() : 8952234;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        if ((var5 & 1) == 1) {
            return 8952234;
        } else {
            int var6 = 0;
            int var7 = 0;
            int var8 = 0;

            for (int var9 = -1; var9 <= 1; ++var9) {
                for (int var10 = -1; var10 <= 1; ++var10) {
                    int var11 = par1IBlockAccess.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor(par2, par3, par4);
                    var6 += (var11 & 16711680) >> 16;
                    var7 += (var11 & '\uff00') >> 8;
                    var8 += var11 & 255;
                }
            }

            return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) & 1) == 1 ? 7 : super.getLightValue(world, x, y, z);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
        byte var7 = 1;
        int var8 = var7 + 1;
        if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
            for (int var9 = -var7; var9 <= var7; ++var9) {
                for (int var10 = -var7; var10 <= var7; ++var10) {
                    for (int var11 = -var7; var11 <= var7; ++var11) {
                        Block var12 = par1World.getBlock(par2 + var9, par3 + var10, par4 + var11);
                        if (var12 != Blocks.air) {
                            var12.beginLeavesDecay(par1World, par2 + var9, par3 + var10, par4 + var11);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void updateTick(World par1World, int x, int y, int z, Random par5Random) {
        if (!par1World.isRemote) {
            int metadata = par1World.getBlockMetadata(x, y, z);
            if ((metadata & 8) != 0 && (metadata & 4) == 0) {
                byte chunksRadius = 4;
                int chunksRange = chunksRadius + 1;
                byte var9 = 32;
                int var10 = var9 * var9;
                int var11 = var9 / 2;
                if (this.adjacentTreeBlocks == null) {
                    this.adjacentTreeBlocks = new int[var9 * var9 * var9];
                }

                if (par1World.checkChunksExist(
                        x - chunksRange,
                        y - chunksRange,
                        z - chunksRange,
                        x + chunksRange,
                        y + chunksRange,
                        z + chunksRange)
                ) {
                    for (int var12 = -chunksRadius; var12 <= chunksRadius; ++var12) {
                        for (int var13 = -chunksRadius; var13 <= chunksRadius; ++var13) {
                            for (int var14 = -chunksRadius; var14 <= chunksRadius; ++var14) {
                                Block block = par1World.getBlock(x + var12, y + var13, z + var14);

                                int i = (var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11;
                                if (block != null && block.canSustainLeaves(par1World, x + var12, y + var13, z + var14)) {
                                    this.adjacentTreeBlocks[i] = 0;
                                } else if (block != null && block.isLeaves(par1World, x + var12, y + var13, z + var14)) {
                                    this.adjacentTreeBlocks[i] = -2;
                                } else {
                                    this.adjacentTreeBlocks[i] = -1;
                                }
                            }
                        }
                    }

                    int var15 = 0;

                    for (int var17 = 1; var17 <= 4; ++var17) {
                        for (int var13 = -chunksRadius; var13 <= chunksRadius; ++var13) {
                            for (int var14 = -chunksRadius; var14 <= chunksRadius; ++var14) {
                                for (int var21 = -chunksRadius; var21 <= chunksRadius; ++var21) {
                                    if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + var21 + var11] == var17 - 1) {
                                        int i = (var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var21 + var11;
                                        if (this.adjacentTreeBlocks[i] == -2) {
                                            this.adjacentTreeBlocks[i] = var17;
                                        }

                                        int i1 = (var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var21 + var11;
                                        if (this.adjacentTreeBlocks[i1] == -2) {
                                            this.adjacentTreeBlocks[i1] = var17;
                                        }

                                        int i2 = (var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var21 + var11;
                                        if (this.adjacentTreeBlocks[i2] == -2) {
                                            this.adjacentTreeBlocks[i2] = var17;
                                        }

                                        int i3 = (var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var21 + var11;
                                        if (this.adjacentTreeBlocks[i3] == -2) {
                                            this.adjacentTreeBlocks[i3] = var17;
                                        }
                                        int i4 = (var13 + var11) * var10 + (var14 + var11) * var9 + (var21 + var11 - 1);
                                        if (this.adjacentTreeBlocks[i4] == -2) {
                                            this.adjacentTreeBlocks[i4] = var17;
                                        }

                                        int i5 = (var13 + var11) * var10 + (var14 + var11) * var9 + var21 + var11 + 1;
                                        if (this.adjacentTreeBlocks[i5] == -2) {
                                            this.adjacentTreeBlocks[i5] = var17;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int var12 = this.adjacentTreeBlocks[var11 * var10 + var11 * var9 + var11];
                if (var12 >= 0) {
                    par1World.setBlockMetadataWithNotify(x, y, z, metadata, 4);
//               par1World.setBlock(x, y, z, this, metadata & -9, 3);
                } else {
                    this.removeLeaves(par1World, x, y, z);
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (par1World.canLightningStrikeAt(par2, par3 + 1, par4) && !World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4) && par5Random.nextInt(15) == 1) {
            double var6 = (float) par2 + par5Random.nextFloat();
            double var8 = (double) par3 - 0.05;
            double var10 = (float) par4 + par5Random.nextFloat();
            par1World.spawnParticle("dripWater", var6, var8, var10, 0.0F, 0.0F, 0.0F);
        }

        int md = par1World.getBlockMetadata(par2, par3, par4);
        if ((md & 1) == 1 && par5Random.nextInt(500) == 0) {
            Thaumcraft.proxy.sparkle((float) par2 + 0.5F + par1World.rand.nextFloat() - par1World.rand.nextFloat(), (float) par3 + 0.5F + par1World.rand.nextFloat() - par1World.rand.nextFloat(), (float) par4 + 0.5F + par1World.rand.nextFloat() - par1World.rand.nextFloat(), 2.0F, 7, 0.0F);
        }

    }

    private void removeLeaves(World par1World, int par2, int par3, int par4) {
        this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
        par1World.setBlockToAir(par2, par3, par4);
    }

    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int meta, float par6, int par7) {
        if (!par1World.isRemote && (meta & 8) != 0 && (meta & 4) == 0) {
            if ((meta & 1) == 0 && par1World.rand.nextInt(200) == 0) {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 0));
            } else if ((meta & 1) == 1 && par1World.rand.nextInt(250) == 0) {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 1));
            }
        }

    }

    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    public int damageDropped(int par1) {
        return par1 & 1;
    }

    public int quantityDropped(Random par1Random) {
        return 0;
    }

    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return Item.getItemById(0);
    }

    public boolean isOpaqueCube() {
        return Blocks.leaves.isOpaqueCube();
    }

    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
        return true;
    }

    public ArrayList onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 3));
        return ret;
    }

    public void beginLeavesDecay(World world, int x, int y, int z) {
        world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 8, 4);
    }

    public boolean isLeaves(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        int md = world.getBlockMetadata(x, y, z);
        return new ItemStack(this, 1, md & 1);
    }

    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 60;
    }

    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 30;
    }
}
