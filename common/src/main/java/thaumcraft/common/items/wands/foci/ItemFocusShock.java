package thaumcraft.common.items.wands.foci;

@Deprecated(forRemoval = true)
public class ItemFocusShock {
//   private static final AspectList<Aspect>costBase;
//   private static final AspectList<Aspect>costChain;
//   private static final AspectList<Aspect>costGround;
//   public static FocusUpgradeType chainlightning;
//   public static FocusUpgradeType earthshock;

//   public ItemFocusShock() {
//      this.setCreativeTab(Thaumcraft.tabTC);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:focus_shock");
//   }
//
//   public String getSortingHelper(ItemStack itemstack) {
//      return "BL" + super.getSortingHelper(itemstack);
//   }
//
//   public int getFocusColor(ItemStack itemstack) {
//      return 10466239;
//   }

//   public AspectList<Aspect>getVisCost(ItemStack itemstack) {
//      return this.isUpgradedWith(itemstack, chainlightning) ? costChain : (this.isUpgradedWith(itemstack, earthshock) ? costGround : costBase);
//   }

//   public int getActivationCooldown(ItemStack focusstack) {
//      return this.isUpgradedWith(focusstack, chainlightning) ? 500 : (this.isUpgradedWith(focusstack, earthshock) ? 1000 : 250);
//   }

//   public WandFocusAnimation getAnimation(ItemStack itemstack) {
//      return this.isUpgradedWith(itemstack, earthshock) ? WandFocusAnimation.WAVE : WandFocusAnimation.CHARGE;
//   }

//   public static void shootLightning(World world, EntityLivingBase living, double xx, double yy, double zz, boolean offset) {
//      double px = living.posX;
//      double py = living.posY;
//      double pz = living.posZ;
//      if (living.getEntityId() != FMLClientHandler.instance().getClient().thePlayer.getEntityId()) {
//         py = living.boundingBox.minY + (double)(living.height / 2.0F) + (double)0.25F;
//      }
//
//      px += -MathHelper.cos((float) (living.rotationYaw / 180.0F * Math.PI)) * 0.06F;
//      py -= 0.06F;
//      pz += -MathHelper.sin((float) (living.rotationYaw / 180.0F * Math.PI)) * 0.06F;
//      if (living.getEntityId() != FMLClientHandler.instance().getClient().thePlayer.getEntityId()) {
//         py = living.boundingBox.minY + (double)(living.height / 2.0F) + (double)0.25F;
//      }
//
//      Vec3 vec3d = living.getLook(1.0F);
//      px += vec3d.xCoord * 0.3;
//      py += vec3d.yCoord * 0.3;
//      pz += vec3d.zCoord * 0.3;
//      FXLightningBolt bolt = new FXLightningBolt(world, px, py, pz, xx, yy, zz, world.getRandom().nextLong(), 6, 0.5F, 8);
//      bolt.defaultFractal();
//      bolt.setType(2);
//      bolt.setWidth(0.125F);
//      bolt.finalizeBolt();
//   }

//   public ItemStack onFocusRightClick(ItemStack itemstack, World world, Player p, HitResult HitResult) {
//      WandCastingItem wand = (WandCastingItem)itemstack.getItem();
//      if (this.isUpgradedWith(wand.getFocusItem(itemstack), earthshock)) {
//         if (wand.consumeAllCentiVis(
//                 itemstack,
//                 p,
//                 this.getVisCost(itemstack),
//                 Platform.getEnvironment() != Env.CLIENT,
//                 false)
//         ) {
//            if (Platform.getEnvironment() != Env.CLIENT) {
//               EntityShockOrb orb = new EntityShockOrb(world, p);
//               orb.area += this.getUpgradeLevel(wand.getFocusItem(itemstack), ThaumcraftFocusUpgradeTypes.ENLARGE) * 2;
//               orb.damage = (int)((double)orb.damage + (double)wand.getFocusPotency(itemstack) * 1.33);
//               world.spawnEntityInWorld(orb);
//               world.playSoundAtEntity(orb, "thaumcraft:zap", 1.0F, 1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F);
//            }
//
//            p.swingItem();
//         }
//      } else {
//         p.setItemInUse(itemstack, Integer.MAX_VALUE);
//         WandManager.setCooldown(p, -1);
//      }
//
//      return itemstack;
//   }

//   public void onUsingFocusTick(ItemStack stack, Player p, int count) {
//      this.doLightningBolt(stack, p, count);
//   }

//   public void doLightningBolt(ItemStack stack, Player p, int count) {
//      WandCastingItem wand = (WandCastingItem)stack.getItem();
//      if (!wand.consumeAllCentiVis(stack, p, this.getVisCost(stack), Platform.getEnvironment() != Env.CLIENT, false)) {
//         p.stopUsingItem();
//      } else
//      {
//         int potency = wand.getFocusPotency(stack);
//         Entity pointedEntity = EntityUtils.getPointedEntity(p, 20);
//         if (p.level().isRemote) {
//            HitResult mop = BlockUtils.getTargetBlock(p.level(), p, false);
//            Vec3 v = p.getLook(2.0F);
//            double px = p.posX + v.xCoord * (double)10.0F;
//            double py = p.posY + v.yCoord * (double)10.0F;
//            double pz = p.posZ + v.zCoord * (double)10.0F;
//            if (mop != null) {
//               px = mop.hitVec.xCoord;
//               py = mop.hitVec.yCoord;
//               pz = mop.hitVec.zCoord;
//
//               for(int a = 0; a < 5; ++a) {
//                  ClientFXUtils.sparkle(
//                          (float)px + (p.level().rand.nextFloat() - p.level().rand.nextFloat()) * 0.3F,
//                          (float)py + (p.level().rand.nextFloat() - p.level().rand.nextFloat()) * 0.3F,
//                          (float)pz + (p.level().rand.nextFloat() - p.level().rand.nextFloat()) * 0.3F,
//                          2.0F + p.level().rand.nextFloat(), 2, 0.05F + p.level().rand.nextFloat() * 0.05F);
//               }
//            }
//
//            if (pointedEntity != null) {
//               px = pointedEntity.posX;
//               py = pointedEntity.boundingBox.minY + (double)(pointedEntity.height / 2.0F);
//               pz = pointedEntity.posZ;
//
//               for(int a = 0; a < 5; ++a) {
//                  ClientFXUtils.sparkle(
//                          (float)px + (p.level().rand.nextFloat() - p.level().rand.nextFloat()) * 0.6F,
//                          (float)py + (p.level().rand.nextFloat() - p.level().rand.nextFloat()) * 0.6F,
//                          (float)pz + (p.level().rand.nextFloat() - p.level().rand.nextFloat()) * 0.6F,
//                          2.0F + p.level().rand.nextFloat(),
//                          2,
//                          0.05F + p.level().rand.nextFloat() * 0.05F
//                  );
//               }
//            }
//
//            shootLightning(p.level(), p, px, py, pz, true);
//         } else {
//            p.level().playSoundEffect(p.posX, p.posY, p.posZ, "thaumcraft:shock", 0.25F, 1.0F);
//            if (pointedEntity instanceof EntityLivingBase
//                    && (!(pointedEntity instanceof Player) || MinecraftServer.getServer().isPVPEnabled())
//            ) {
//               int cl = this.getUpgradeLevel(wand.getFocusItem(stack), chainlightning) * 2;
//               pointedEntity.attackEntityFrom(DamageSource.causePlayerDamage(p), (float)((cl > 0 ? 6 : 4) + potency));
//               if (cl > 0) {
//                  cl += this.getUpgradeLevel(wand.getFocusItem(stack), ThaumcraftFocusUpgradeTypes.ENLARGE) * 2;
//                  EntityLivingBase center = (EntityLivingBase)pointedEntity;
//                  ArrayList<Integer> targets = new ArrayList<>();
//                  targets.add(pointedEntity.getEntityId());
//
//                  while(cl > 0) {
//                     --cl;
//                     ArrayList<Entity> list = EntityUtils.getEntitiesInRange(p.level(), center.posX, center.posY, center.posZ, p, EntityLivingBase.class, 8.0F);
//                     double d = Double.MAX_VALUE;
//                     Entity closest = null;
//
//                     for(Entity e : list) {
//                        if (!targets.contains(e.getEntityId()) && (!(e instanceof Player) || MinecraftServer.getServer().isPVPEnabled())) {
//                           double dd = e.getDistanceSqToEntity(center);
//                           if (dd < d) {
//                              closest = e;
//                              d = dd;
//                           }
//                        }
//                     }
//
//                     if (closest != null) {
//                        PacketHandler.INSTANCE.sendToAllAround(new PacketFXZap(center.getEntityId(), closest.getEntityId()), new NetworkRegistry.TargetPoint(p.level().dimension(), center.posX, center.posY, center.posZ, 64.0F));
//                        targets.add(closest.getEntityId());
//                        closest.attackEntityFrom(DamageSource.causePlayerDamage(p), (float)(4 + potency));
//                        center = (EntityLivingBase)closest;
//                     }
//                  }
//               }
//            }
//         }
//
//      }
//   }

//   public boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type, int rank) {
//      return !type.equals(ThaumcraftFocusUpgradeTypes.ENLARGE)
//              || this.isUpgradedWith(focusstack, chainlightning)
//              || this.isUpgradedWith(focusstack, earthshock);
//   }
//
//   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
//      switch (rank) {
//         case 1:
//            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY};
//         case 2:
//            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY};
//         case 3:
//            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY, chainlightning, earthshock};
//         case 4:
//            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY, ThaumcraftFocusUpgradeTypes.ENLARGE};
//         case 5:
//            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY, ThaumcraftFocusUpgradeTypes.ENLARGE};
//         default:
//            return null;
//      }
//   }
//
//   static {
//      costBase = (new LinkedHashAspectList<>()).addAll(Aspects.AIR, 25);
//      costChain = (new LinkedHashAspectList<>()).addAll(Aspects.AIR, 40).addAll(Aspects.WATER, 10);
//      costGround = (new LinkedHashAspectList<>()).addAll(Aspects.AIR, 75).addAll(Aspects.EARTH, 25);
//   }
}
