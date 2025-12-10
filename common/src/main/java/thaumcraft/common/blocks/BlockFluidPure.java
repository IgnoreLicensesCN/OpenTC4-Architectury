package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXBubble;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

import java.util.Random;

public class BlockFluidPure extends BlockFluidClassic {
   public IIcon iconStill;
   public IIcon iconFlow;

   public BlockFluidPure() {
      super(ConfigBlocks.FLUIDPURE, Material.water);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconStill = ir.registerIcon("thaumcraft:fluidpure");
      this.iconFlow = ir.registerIcon("thaumcraft:fluidpure");
      ConfigBlocks.FLUIDPURE.setIcons(this.iconStill, this.iconFlow);
   }

   public IIcon getIcon(int par1, int par2) {
      return this.iconStill;
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if (!world.isRemote && this.isSourceBlock(world, x, y, z) && entity instanceof EntityPlayer && !((EntityPlayer)entity).isPotionActive(Config.potionWarpWardID)) {
         int warp = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(entity.getCommandSenderName());
         int div = 1;
         if (warp > 0) {
            div = (int)Math.sqrt(warp);
            if (div < 1) {
               div = 1;
            }
         }

         ((EntityPlayer)entity).addPotionEffect(new PotionEffect(Config.potionWarpWardID, Math.min(32000, 200000 / div), 0, true));
         world.setBlockToAir(x, y, z);
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int meta = world.getBlockMetadata(x, y, z);
      FXBubble fb = new FXBubble(world, (float)x + rand.nextFloat(), (float)y + 0.125F * (float)(8 - meta), (float)z + rand.nextFloat(), 0.0F, 0.0F, 0.0F, 0);
      fb.setAlphaF(0.25F);
      fb.setRGB(1.0F, 1.0F, 1.0F);
      ParticleEngine.instance.addEffect(world, fb);
      if (rand.nextInt(25) == 0) {
         double var21 = (float)x + rand.nextFloat();
         double var22 = (double)y + this.maxY;
         double var23 = (float)z + rand.nextFloat();
         world.playSound(var21, var22, var23, "liquid.lavapop", 0.1F + rand.nextFloat() * 0.1F, 0.9F + rand.nextFloat() * 0.15F, false);
      }

      super.randomDisplayTick(world, x, y, z, rand);
   }
}
