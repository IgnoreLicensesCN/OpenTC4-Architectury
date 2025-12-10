package thaumcraft.common.entities.ai.interact;

import com.mojang.authlib.GameProfile;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;

public class AIHarvestLogs extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int xx;
   private int yy;
   private int zz;
   private float movementSpeed;
   private float distance;
   private World theWorld;
   private Block block;
   private int blockMd;
   private int delay;
   private int maxDelay;
   private int mod;
   FakePlayer player;
   private int count;

   public AIHarvestLogs(EntityGolemBase par1EntityCreature) {
      this.block = Blocks.air;
      this.blockMd = 0;
      this.delay = -1;
      this.maxDelay = 1;
      this.mod = 1;
      this.count = 0;
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(3);
      this.distance = (float)MathHelper.ceiling_float_int(this.theGolem.getRange() / 3.0F);
      if (this.theWorld instanceof WorldServer) {
         this.player = FakePlayerFactory.get((WorldServer)this.theWorld, new GameProfile(null, "FakeThaumcraftGolem"));
      }

   }

   public boolean shouldExecute() {
      if (this.delay < 0 && this.theGolem.ticksExisted % Config.golemDelay <= 0 && this.theGolem.getNavigator().noPath()) {
         Vec3 var1 = this.findLog();
         if (var1 == null) {
            return false;
         } else {
            this.xx = (int)var1.xCoord;
            this.yy = (int)var1.yCoord;
            this.zz = (int)var1.zCoord;
            this.block = this.theWorld.getBlock(this.xx, this.yy, this.zz);
            this.blockMd = this.theWorld.getBlockMetadata(this.xx, this.yy, this.zz);
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.theWorld.getBlock(this.xx, this.yy, this.zz) == this.block && this.theWorld.getBlockMetadata(this.xx, this.yy, this.zz) == this.blockMd && this.count-- > 0 && (this.delay > 0 || Utils.isWoodLog(this.theWorld, this.xx, this.yy, this.zz) || !this.theGolem.getNavigator().noPath());
   }

   public void updateTask() {
      double dist = this.theGolem.getDistanceSq((double)this.xx + (double)0.5F, (double)this.yy + (double)0.5F, (double)this.zz + (double)0.5F);
      this.theGolem.getLookHelper().setLookPosition((double)this.xx + (double)0.5F, (double)this.yy + (double)0.5F, (double)this.zz + (double)0.5F, 30.0F, 30.0F);
      if (dist <= (double)4.0F) {
         if (this.delay < 0) {
            this.delay = (int)Math.max(5.0F, (20.0F - (float)this.theGolem.getGolemStrength() * 3.0F) * this.block.getBlockHardness(this.theWorld, this.xx, this.yy, this.zz));
            this.maxDelay = this.delay;
            this.mod = this.delay / Math.round((float)this.delay / 6.0F);
         }

         if (this.delay > 0) {
            if (--this.delay > 0 && this.delay % this.mod == 0 && this.theGolem.getNavigator().noPath()) {
               this.theGolem.startActionTimer();
               this.theWorld.playSoundEffect((float)this.xx + 0.5F, (float)this.yy + 0.5F, (float)this.zz + 0.5F, this.block.stepSound.getBreakSound(), (this.block.stepSound.getVolume() + 0.7F) / 8.0F, this.block.stepSound.getPitch() * 0.5F);
               BlockUtils.destroyBlockPartially(this.theWorld, this.theGolem.getEntityId(), this.xx, this.yy, this.zz, (int)(9.0F * (1.0F - (float)this.delay / (float)this.maxDelay)));
            }

            if (this.delay == 0) {
               this.harvest();
               if (Utils.isWoodLog(this.theWorld, this.xx, this.yy, this.zz)) {
                  this.delay = -1;
                  this.block = this.theWorld.getBlock(this.xx, this.yy, this.zz);
                  this.blockMd = this.theWorld.getBlockMetadata(this.xx, this.yy, this.zz);
                  this.startExecuting();
               } else {
                  this.checkAdjacent();
               }
            }
         }
      }

   }

   private void checkAdjacent() {
      for(int x2 = -1; x2 <= 1; ++x2) {
         for(int z2 = -1; z2 <= 1; ++z2) {
            for(int y2 = -1; y2 <= 1; ++y2) {
               int x = this.xx + x2;
               int y = this.yy + y2;
               int z = this.zz + z2;
               if (!((float)Math.abs(this.theGolem.getHomePosition().posX - x) > this.distance) && !((float)Math.abs(this.theGolem.getHomePosition().posY - y) > this.distance) && !((float)Math.abs(this.theGolem.getHomePosition().posZ - z) > this.distance) && Utils.isWoodLog(this.theWorld, x, y, z)) {
                  Vec3 var1 = Vec3.createVectorHelper(x, y, z);
                  if (var1 != null) {
                     this.xx = (int)var1.xCoord;
                     this.yy = (int)var1.yCoord;
                     this.zz = (int)var1.zCoord;
                     this.block = this.theWorld.getBlock(this.xx, this.yy, this.zz);
                     this.blockMd = this.theWorld.getBlockMetadata(this.xx, this.yy, this.zz);
                     this.delay = -1;
                     this.startExecuting();
                     return;
                  }
               }
            }
         }
      }

   }

   public void resetTask() {
      BlockUtils.destroyBlockPartially(this.theWorld, this.theGolem.getEntityId(), this.xx, this.yy, this.zz, -1);
      this.delay = -1;
   }

   public void startExecuting() {
      this.count = 200;
      this.theGolem.getNavigator().tryMoveToXYZ((double)this.xx + (double)0.5F, (double)this.yy + (double)0.5F, (double)this.zz + (double)0.5F, this.theGolem.getAIMoveSpeed());
   }

   void harvest() {
      this.count = 200;
      this.theWorld.playAuxSFX(2001, this.xx, this.yy, this.zz, Block.getIdFromBlock(this.block) + (this.blockMd << 12));
      BlockUtils.breakFurthestBlock(this.theWorld, this.xx, this.yy, this.zz, this.block, this.player);
      this.theGolem.startActionTimer();
   }

   private Vec3 findLog() {
      Random rand = this.theGolem.getRNG();

      for(int var2 = 0; (float)var2 < this.distance * 4.0F; ++var2) {
         int x = (int)((float)(this.theGolem.getHomePosition().posX + rand.nextInt((int)(1.0F + this.distance * 2.0F))) - this.distance);
         int y = (int)((float)(this.theGolem.getHomePosition().posY + rand.nextInt((int)(1.0F + this.distance))) - this.distance / 2.0F);
         int z = (int)((float)(this.theGolem.getHomePosition().posZ + rand.nextInt((int)(1.0F + this.distance * 2.0F))) - this.distance);
         if (Utils.isWoodLog(this.theWorld, x, y, z)) {
            Vec3 v = Vec3.createVectorHelper(x, y, z);
            double dist = this.theGolem.getDistanceSq((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F);

            for(int yy = 1; Utils.isWoodLog(this.theWorld, x, y - yy, z) && this.theGolem.getDistanceSq((double)x + (double)0.5F, (double)(y - yy) + (double)0.5F, (double)z + (double)0.5F) < dist; ++yy) {
               v = Vec3.createVectorHelper(x, y - yy, z);
               dist = this.theGolem.getDistanceSq((double)x + (double)0.5F, (double)(y - yy) + (double)0.5F, (double)z + (double)0.5F);
            }

            return v;
         }
      }

      return null;
   }
}
