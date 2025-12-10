package thaumcraft.common.entities.ai.misc;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class AIConvertGrass extends EntityAIBase {
   private EntityLiving entity;
   private World world;
   int field_48399_a = 0;

   public AIConvertGrass(EntityLiving par1EntityLiving) {
      this.entity = par1EntityLiving;
      this.world = par1EntityLiving.worldObj;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      if (this.entity.getRNG().nextInt(250) != 0) {
         return false;
      } else {
         int var1 = MathHelper.floor_double(this.entity.posX);
         int var2 = MathHelper.floor_double(this.entity.posY);
         int var3 = MathHelper.floor_double(this.entity.posZ);
         return this.world.getBlock(var1, var2, var3) == Blocks.tallgrass && this.world.getBlockMetadata(var1, var2, var3) == 1 || this.world.getBlock(var1, var2 - 1, var3) == Blocks.grass;
      }
   }

   public void startExecuting() {
      this.field_48399_a = 40;
      this.world.setEntityState(this.entity, (byte)10);
      this.entity.getNavigator().clearPathEntity();
   }

   public void resetTask() {
      this.field_48399_a = 0;
   }

   public boolean continueExecuting() {
      return this.field_48399_a > 0;
   }

   public int func_48396_h() {
      return this.field_48399_a;
   }

   public void updateTask() {
      this.field_48399_a = Math.max(0, this.field_48399_a - 1);
      if (this.field_48399_a == 4) {
         int var1 = MathHelper.floor_double(this.entity.posX);
         int var2 = MathHelper.floor_double(this.entity.posY);
         int var3 = MathHelper.floor_double(this.entity.posZ);
         if (this.world.getBlock(var1, var2, var3) == Blocks.tallgrass) {
            this.world.playAuxSFX(2001, var1, var2, var3, Block.getIdFromBlock(Blocks.grass) + 4096);
            this.world.setBlockToAir(var1, var2, var3);
            this.world.setBlock(var1, var2, var3, ConfigBlocks.blockTaintFibres, 0, 3);
            Utils.setBiomeAt(this.world, var1, var3, ThaumcraftWorldGenerator.biomeTaint);
            this.entity.eatGrassBonus();
         } else if (this.world.getBlock(var1, var2 - 1, var3) == Blocks.grass) {
            this.world.playAuxSFX(2001, var1, var2 - 1, var3, Block.getIdFromBlock(Blocks.grass));
            this.world.setBlock(var1, var2, var3, ConfigBlocks.blockTaintFibres, 0, 3);
            Utils.setBiomeAt(this.world, var1, var3, ThaumcraftWorldGenerator.biomeTaint);
            this.entity.eatGrassBonus();
         }
      }

   }
}
