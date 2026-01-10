package thaumcraft.common.tiles;

public class TileEldritchObelisk /*extends TileThaumcraft*/ {
//   private int counter = 0;
//
//   public boolean canUpdate() {
//       return super.canUpdate();
//   }
//
//   @SideOnly(Side.CLIENT)
//   public AxisAlignedBB getRenderBoundingBox() {
//      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 5, this.zCoord + 1);
//   }
//
//   public double getMaxRenderDistanceSquared() {
//      return 9216.0F;
//   }
//
//   public void updateEntity() {
//      if (Platform.getEnvironment() != Env.CLIENT && this.counter % 20 == 0) {
//         ArrayList<Entity> list = EntityUtils.getEntitiesInRange(this.getLevel(), (double)this.xCoord + (double)0.5F, this.yCoord, (double)this.zCoord + (double)0.5F, null, EntityLivingBase.class, 6.0F);
//         if (list != null && !list.isEmpty()) {
//            for(Entity e : list) {
//               if (e instanceof IEldritchMob && e instanceof EntityLivingBase && !((EntityLivingBase)e).isPotionActive(Potion.regeneration.id)) {
//                  try {
//                     ((EntityLivingBase)e).addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 40, 0, true));
//                     ((EntityLivingBase)e).addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 40, 0, true));
//                  } catch (Exception ignored) {
//                  }
//               }
//            }
//         }
//      }
//
//      if ((Platform.getEnvironment() == Env.CLIENT)) {
//         ArrayList<Entity> list = EntityUtils.getEntitiesInRange(this.getLevel(), (double)this.xCoord + (double)0.5F, this.yCoord, (double)this.zCoord + (double)0.5F, null, EntityLivingBase.class, 6.0F);
//         if (list != null && !list.isEmpty()) {
//            for(Entity e : list) {
//               if (e instanceof IEldritchMob && e instanceof EntityLivingBase) {
//                  Thaumcraft.proxy.wispFX4(this.getLevel(), (double)this.xCoord + (double)0.5F, (float)(this.yCoord + 1) + this.level().rand.nextFloat() * 3.0F, (double)this.zCoord + (double)0.5F, e, 5, true, 1.0F);
//               }
//            }
//         }
//      }
//
//   }
}
