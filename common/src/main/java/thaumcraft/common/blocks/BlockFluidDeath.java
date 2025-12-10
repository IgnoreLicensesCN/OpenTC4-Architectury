package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSlimyBubble;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

import java.util.Random;

public class BlockFluidDeath extends BlockFluidFinite {
   public IIcon iconStill;
   public IIcon iconFlow;

   public BlockFluidDeath() {
      super(ConfigBlocks.FLUIDDEATH, Material.water);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setQuantaPerBlock(4);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconStill = ir.registerIcon("thaumcraft:fluiddeath");
      this.iconFlow = ir.registerIcon("thaumcraft:fluiddeath");
      ConfigBlocks.FLUIDDEATH.setIcons(this.iconStill, this.iconFlow);
   }

   public IIcon getIcon(int par1, int par2) {
      return this.iconStill;
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if (!world.isRemote && entity instanceof EntityLivingBase) {
         entity.attackEntityFrom(DamageSourceThaumcraft.dissolve, (float)(world.getBlockMetadata(x, y, z) + 1));
      }

   }

   public int getQuanta() {
      return this.quantaPerBlock;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int meta = world.getBlockMetadata(x, y, z);
      float h = rand.nextFloat() * 0.075F;
      FXSlimyBubble ef = new FXSlimyBubble(world, (float)x + rand.nextFloat(), (float)y + 0.1F + 0.225F * (float)meta, (float)z + rand.nextFloat(), 0.075F + h);
      ef.setAlphaF(0.8F);
      ef.setRBGColorF(0.3F - rand.nextFloat() * 0.1F, 0.0F, 0.4F + rand.nextFloat() * 0.1F);
      ParticleEngine.instance.addEffect(world, ef);
      if (rand.nextInt(50) == 0) {
         double var21 = (float)x + rand.nextFloat();
         double var22 = (double)y + this.maxY;
         double var23 = (float)z + rand.nextFloat();
         world.playSound(var21, var22, var23, "liquid.lavapop", 0.1F + rand.nextFloat() * 0.1F, 0.9F + rand.nextFloat() * 0.15F, false);
      }

      super.randomDisplayTick(world, x, y, z, rand);
   }
}
