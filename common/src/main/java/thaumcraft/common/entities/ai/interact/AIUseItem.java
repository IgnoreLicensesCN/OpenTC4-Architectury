package thaumcraft.common.entities.ai.interact;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.level.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraft.core.Direction;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;

import java.util.Iterator;

public class AIUseItem extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int xx;
   private int yy;
   private int zz;
   private float movementSpeed;
   private float distance;
   private World theWorld;
   private Block block;
   private int blockMd;
   FakePlayer player;
   private int count;
   private int color;
   ItemInWorldManager im;
   int nextTick;

   public AIUseItem(EntityGolemBase par1EntityCreature) {
      this.block = Blocks.air;
      this.blockMd = 0;
      this.count = 0;
      this.color = -1;
      this.nextTick = 0;
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.level();
      this.setMutexBits(3);
      this.distance = (float)MathHelper.ceiling_float_int(this.theGolem.getRange() / 3.0F);
      if (this.theWorld instanceof WorldServer) {
         this.player = FakePlayerFactory.get((WorldServer)this.theWorld, new GameProfile(null, "FakeThaumcraftGolem"));
      }

      try {
         this.nextTick = this.theGolem.ticksExisted + this.theworld.getRandom().nextInt(6);
      } catch (Exception ignored) {
      }

   }

   public boolean shouldExecute() {
      boolean ignoreItem = false;
      ChunkCoordinates home = this.theGolem.getHomePosition();
      Direction facing = Direction.getOrientation(this.theGolem.homeFacing);
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      TileEntity tile = this.theGolem.level().getTileEntity(cX, cY, cZ);
      if (!(tile instanceof IInventory)) {
         ignoreItem = true;
      }

      int d = 5 - this.theGolem.ticksExisted;
      if (d < 1) {
         d = 1;
      }

      if ((this.theGolem.itemCarried != null || ignoreItem) && this.theGolem.ticksExisted >= this.nextTick && this.theGolem.getNavigator().noPath()) {
         this.nextTick = this.theGolem.ticksExisted + d * 3;
         return this.findSomething();
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.theWorld.getBlock(this.xx, this.yy, this.zz) == this.block && this.theWorld.getBlockMetadata(this.xx, this.yy, this.zz) == this.blockMd && this.count-- > 0 && !this.theGolem.getNavigator().noPath();
   }

   public void updateTask() {
      this.theGolem.getLookHelper().setLookPosition((double)this.xx + (double)0.5F, (double)this.yy + (double)0.5F, (double)this.zz + (double)0.5F, 30.0F, 30.0F);
      double dist = this.theGolem.getDistanceSq((double)this.xx + (double)0.5F, (double)this.yy + (double)0.5F, (double)this.zz + (double)0.5F);
      if (dist <= (double)4.0F) {
         this.click();
      }

   }

   public void resetTask() {
      this.count = 0;
      this.theGolem.getNavigator().clearPathEntity();
   }

   public void startExecuting() {
      this.count = 200;
      this.theGolem.getNavigator().tryMoveToXYZ((double)this.xx + (double)0.5F, (double)this.yy + (double)0.5F, (double)this.zz + (double)0.5F, this.theGolem.getAIMoveSpeed());
   }

   void click() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      boolean ignoreItem = false;
      Direction facing = Direction.getOrientation(this.theGolem.homeFacing);
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      TileEntity tile = this.theGolem.level().getTileEntity(cX, cY, cZ);
      if (!(tile instanceof IInventory)) {
         ignoreItem = true;
      }

      this.player.setPositionAndRotation(this.theGolem.posX, this.theGolem.posY, this.theGolem.posZ, this.theGolem.rotationYaw, this.theGolem.rotationPitch);
      this.player.setCurrentItemOrArmor(0, this.theGolem.itemCarried);
      this.player.setSneaking(this.theGolem.getToggles()[2]);
      Iterator i$ = GolemHelper.getMarkedSides(this.theGolem, this.xx, this.yy, this.zz, this.theGolem.level().dimension(), (byte)this.color).iterator();
      if (i$.hasNext()) {
         Integer side = (Integer)i$.next();
         int x = 0;
         int y = 0;
         int z = 0;
         if (this.theGolem.level().isAirBlock(this.xx, this.yy, this.zz)) {
            x = Direction.getOrientation(side).getOpposite().offsetX;
            y = Direction.getOrientation(side).getOpposite().offsetY;
            z = Direction.getOrientation(side).getOpposite().offsetZ;
         }

         if (this.im == null) {
            this.im = new ItemInWorldManager(this.theGolem.level());
         }

         if (this.theGolem.itemCarried == null && !ignoreItem) {
            this.resetTask();
         } else {
            try {
               if (this.theGolem.getToggles()[1]) {
                  this.theGolem.startLeftArmTimer();
                  this.im.onBlockClicked(this.xx + x, this.yy + y, this.zz + z, side);
               } else if (this.im.activateBlockOrUseItem(this.player, this.theGolem.level(), this.theGolem.itemCarried, this.xx + x, this.yy + y, this.zz + z, side, 0.5F, 0.5F, 0.5F)) {
                  this.theGolem.startRightArmTimer();
               }

               this.theGolem.itemCarried = this.player.getCurrentEquippedItem();
               if (this.theGolem.itemCarried.stackSize <= 0) {
                  this.theGolem.itemCarried = null;
               }

               for(int a = 1; a < this.player.inventory.mainInventory.length; ++a) {
                  if (this.player.inventory.getStackInSlot(a) != null) {
                     if (this.theGolem.itemCarried == null) {
                        this.theGolem.itemCarried = this.player.inventory.getStackInSlot(a).copy();
                     } else {
                        this.player.dropPlayerItemWithRandomChoice(this.player.inventory.getStackInSlot(a), false);
                     }

                     this.player.inventory.setInventorySlotContents(a, null);
                  }
               }

               this.theGolem.updateCarried();
               this.resetTask();
            } catch (Exception var14) {
               this.resetTask();
            }
         }
      }
   }

   boolean findSomething() {
      for(byte col : this.theGolem.getColorsMatching(this.theGolem.itemCarried)) {
         for(Marker marker : this.theGolem.getMarkers()) {
            if ((marker.color == col || col == -1) && (!this.theGolem.getToggles()[0] || this.theGolem.level().isAirBlock(marker.x, marker.y, marker.z)) && (this.theGolem.getToggles()[0] || !this.theGolem.level().isAirBlock(marker.x, marker.y, marker.z))) {
               Direction opp = Direction.getOrientation(marker.side);
               if (this.theGolem.level().isAirBlock(marker.x + opp.offsetX, marker.y + opp.offsetY, marker.z + opp.offsetZ)) {
                  this.color = col;
                  this.xx = marker.x;
                  this.yy = marker.y;
                  this.zz = marker.z;
                  this.block = this.theWorld.getBlock(this.xx, this.yy, this.zz);
                  this.blockMd = this.theWorld.getBlockMetadata(this.xx, this.yy, this.zz);
                  return true;
               }
            }
         }
      }

      return false;
   }
}
