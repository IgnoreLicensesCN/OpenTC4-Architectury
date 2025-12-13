package thaumcraft.codechicken.lib.raytracer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.Vec3;
import net.minecraft.util.HitResult.MovingObjectType;
import net.minecraft.world.level.Level;
import thaumcraft.codechicken.lib.math.MathHelper;
import thaumcraft.codechicken.lib.vec.BlockCoord;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Vector3;

public class RayTracer {
   private Vector3 vec = new Vector3();
   private Vector3 vec2 = new Vector3();
   private Vector3 s_vec = new Vector3();
   private double s_dist;
   private int s_side;
   private IndexedCuboid6 c_cuboid;
   private static final ThreadLocal<RayTracer> t_inst = new ThreadLocal<>();

   public static RayTracer instance() {
      RayTracer inst = t_inst.get();
      if (inst == null) {
         t_inst.set(inst = new RayTracer());
      }

      return inst;
   }

   private void traceSide(int side, Vector3 start, Vector3 end, Cuboid6 cuboid) {
      this.vec.set(start);
      Vector3 hit = null;
      switch (side) {
         case 0:
            hit = this.vec.XZintercept(end, cuboid.min.y);
            break;
         case 1:
            hit = this.vec.XZintercept(end, cuboid.max.y);
            break;
         case 2:
            hit = this.vec.XYintercept(end, cuboid.min.z);
            break;
         case 3:
            hit = this.vec.XYintercept(end, cuboid.max.z);
            break;
         case 4:
            hit = this.vec.YZintercept(end, cuboid.min.x);
            break;
         case 5:
            hit = this.vec.YZintercept(end, cuboid.max.x);
      }

      if (hit != null) {
         switch (side) {
            case 0:
            case 1:
               if (!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) {
                  return;
               }
               break;
            case 2:
            case 3:
               if (!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y)) {
                  return;
               }
               break;
            case 4:
            case 5:
               if (!MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) {
                  return;
               }
         }

         double dist = this.vec2.set(hit).subtract(start).magSquared();
         if (dist < this.s_dist) {
            this.s_side = side;
            this.s_dist = dist;
            this.s_vec.set(this.vec);
         }

      }
   }

   public HitResult rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid) {
      this.s_dist = Double.MAX_VALUE;
      this.s_side = -1;

      for(int i = 0; i < 6; ++i) {
         this.traceSide(i, start, end, cuboid);
      }

      if (this.s_side < 0) {
         return null;
      } else {
         HitResult mop = new HitResult(0, 0, 0, this.s_side, this.s_vec.toVec3D());
         mop.typeOfHit = null;
         return mop;
      }
   }

   public HitResult rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids) {
      double c_dist = Double.MAX_VALUE;
      HitResult c_hit = null;

      for(IndexedCuboid6 cuboid : cuboids) {
         HitResult mop = this.rayTraceCuboid(start, end, cuboid);
         if (mop != null && this.s_dist < c_dist) {
            c_dist = this.s_dist;
            c_hit = mop;
            this.c_cuboid = cuboid;
         }
      }

      return c_hit;
   }

   public HitResult rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockCoord pos, Block block) {
      HitResult mop = this.rayTraceCuboids(start, end, cuboids);
      if (mop != null) {
         mop.typeOfHit = MovingObjectType.BLOCK;
         mop.blockX = pos.x;
         mop.blockY = pos.y;
         mop.blockZ = pos.z;
         if (block != null) {
            this.c_cuboid.add(new Vector3(-pos.x, -pos.y, -pos.z)).setBlockBounds(block);
         }
      }

      return mop;
   }


   public static HitResult retraceBlock(World world, Player player, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      Vec3 headVec = getCorrectedHeadVec(player);
      Vec3 lookVec = player.getLook(1.0F);
      double reach = getBlockReachDistance(player);
      Vec3 endVec = headVec.addVector(lookVec.xCoord * reach,
              lookVec.yCoord * reach,
              lookVec.zCoord * reach);
      return block.collisionRayTrace(world, x, y, z, headVec, endVec);
   }

   private static double getBlockReachDistance_server(ServerPlayer player) {
      return player.theItemInWorldManager.getBlockReachDistance();
   }

   @SideOnly(Side.CLIENT)
   private static double getBlockReachDistance_client() {
      return Minecraft.getMinecraft().playerController.getBlockReachDistance();
   }

   public static Vec3 getCorrectedHeadVec(Player player) {
      Vec3 v = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         v.yCoord += player.getEyeHeight() - player.getDefaultEyeHeight();
      } else {
         v.yCoord += player.getEyeHeight();
         if (player instanceof ServerPlayer && player.isSneaking()) {
            v.yCoord -= 0.08;
         }
      }

      return v;
   }
   public static double getBlockReachDistance(Player player) {
      return (Platform.getEnvironment() == Env.CLIENT) ? getBlockReachDistance_client() : (player instanceof ServerPlayer ? getBlockReachDistance_server((ServerPlayer)player) : (double)5.0F);
   }

}
