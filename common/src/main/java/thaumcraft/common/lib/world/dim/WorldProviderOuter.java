package thaumcraft.common.lib.world.dim;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class WorldProviderOuter extends WorldProvider {
   @Override
   public String getDimensionName() {
      return "The Outer Lands";
   }

   @Override
   public String getWelcomeMessage() {
      return "Entering The Outer Lands";
   }

   @Override
   public String getDepartMessage() {
      return "Leaving The Outer Lands";
   }

   @Override
   public boolean shouldMapSpin(String entity, double x, double y, double z) {
      return true;
   }

   @Override
   public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
      return false;
   }

   @Override
   public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
      return false;
   }

   @Override
   public boolean canDoLightning(Chunk chunk) {
      return false;
   }

   @Override
   public boolean canDoRainSnowIce(Chunk chunk) {
      return false;
   }

   @Override
   public void registerWorldChunkManager() {
      this.worldChunkMgr = new WorldChunkManagerHell(ThaumcraftWorldGenerator.biomeEldritchLands, 0.0F);
      this.dimensionId = Config.dimensionOuterId;
      this.hasNoSky = true;
   }

   @Override
   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderOuter(this.worldObj, this.worldObj.getSeed(), true);
   }

   @Override
   public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
      return 0.0F;
   }

   @Override
   @SideOnly(Side.CLIENT)
   public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
      return null;
   }

   @Override
   @SideOnly(Side.CLIENT)
   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
      int i = 10518688;
      float f2 = MathHelper.cos(p_76562_1_ * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
      if (f2 < 0.0F) {
         f2 = 0.0F;
      }

      if (f2 > 1.0F) {
         f2 = 1.0F;
      }

      float f3 = (float)(i >> 16 & 255) / 255.0F;
      float f4 = (float)(i >> 8 & 255) / 255.0F;
      float f5 = (float)(i & 255) / 255.0F;
      f3 *= f2 * 0.0F + 0.15F;
      f4 *= f2 * 0.0F + 0.15F;
      f5 *= f2 * 0.0F + 0.15F;
      return Vec3.createVectorHelper(f3, f4, f5);
   }

   @Override
   @SideOnly(Side.CLIENT)
   public boolean isSkyColored() {
      return false;
   }

   @Override
   public boolean canRespawnHere() {
      return false;
   }

   @Override
   public boolean isSurfaceWorld() {
      return false;
   }

   @Override
   @SideOnly(Side.CLIENT)
   public float getCloudHeight() {
      return 1.0F;
   }

   @Override
   public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
      return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_).getMaterial().blocksMovement();
   }

   @Override
   public ChunkCoordinates getEntrancePortalLocation() {
       return super.getEntrancePortalLocation();
   }

   @Override
   public int getAverageGroundLevel() {
      return 50;
   }

   @Override
   @SideOnly(Side.CLIENT)
   public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
      return true;
   }
}
