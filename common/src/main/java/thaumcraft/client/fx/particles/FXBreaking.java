package thaumcraft.client.fx.particles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class FXBreaking extends EntityFX {
   public void setParticleMaxAge(int particleMaxAge) {
      this.particleMaxAge = particleMaxAge;
   }

   public FXBreaking(World par1World, double par2, double par4, double par6, Item par8Item) {
      super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
      this.setParticleIcon(par8Item.getIconFromDamage(0));
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.particleGravity = Blocks.snow_layer.blockParticleGravity;
      this.particleScale /= 2.0F;
   }

   public FXBreaking(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, Item par14Item) {
      this(par1World, par2, par4, par6, par14Item);
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      this.motionX += par8;
      this.motionY += par10;
      this.motionZ += par12;
   }

   public int getFXLayer() {
      return 2;
   }

   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      float f6 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
      float f7 = f6 + 0.015609375F;
      float f8 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
      float f9 = f8 + 0.015609375F;
      float f10 = 0.1F * this.particleScale;
      float fade = 1.0F - (float)this.particleAge / (float)this.particleMaxAge;
      f10 *= fade;
      if (this.particleIcon != null) {
         f6 = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0F * 16.0F);
         f7 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
         f8 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0F * 16.0F);
         f9 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
      }

      float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
      float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
      float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
      float f14 = 1.0F;
      par1Tessellator.setColorRGBA_F(f14 * this.particleRed, f14 * this.particleGreen, f14 * this.particleBlue, this.particleAlpha * fade);
      par1Tessellator.addVertexWithUV(f11 - par3 * f10 - par6 * f10, f12 - par4 * f10, f13 - par5 * f10 - par7 * f10, f6, f9);
      par1Tessellator.addVertexWithUV(f11 - par3 * f10 + par6 * f10, f12 + par4 * f10, f13 - par5 * f10 + par7 * f10, f6, f8);
      par1Tessellator.addVertexWithUV(f11 + par3 * f10 + par6 * f10, f12 + par4 * f10, f13 + par5 * f10 + par7 * f10, f7, f8);
      par1Tessellator.addVertexWithUV(f11 + par3 * f10 - par6 * f10, f12 - par4 * f10, f13 + par5 * f10 - par7 * f10, f7, f9);
   }
}
