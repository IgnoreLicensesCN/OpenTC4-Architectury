package thaumcraft.common.items.wands.foci;

@Deprecated(forRemoval = true)
public class ItemFocusExcavation /*extends ItemFocusBasic*/ {
//   private static final AspectList cost = (new AspectList()).add(Aspect.EARTH, 15);
//   static HashMap soundDelay;
//   static HashMap beam;
//   static HashMap breakcount;
//   static HashMap lastX;
//   static HashMap lastY;
//   static HashMap lastZ;
//   public static FocusUpgradeType dowsing;
//
//   static {
//      soundDelay = new HashMap<>();
//      beam = new HashMap<>();
//      breakcount = new HashMap<>();
//      lastX = new HashMap<>();
//      lastY = new HashMap<>();
//      lastZ = new HashMap<>();
//      dowsing = new FocusUpgradeType("dowsing", new ResourceLocation("thaumcraft", "textures/foci/dowsing.png"), "focus.upgrade.dowsing.name", "focus.upgrade.dowsing.text", (new AspectList()).add(Aspect.MINE, 1));
//   }
//
//   public ItemFocusExcavation() {
////      this.setCreativeTab(Thaumcraft.tabTC);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:focus_excavation");
//   }
//
//   public String getSortingHelper(ItemStack itemstack) {
//      return "BE" + super.getSortingHelper(itemstack);
//   }
//
//   public int getFocusColor(ItemStack itemstack) {
//      return 409606;
//   }
//
//   private static final AspectList withSilkTouchOrDowsing = (new AspectList())
//           .add(Aspect.AIR, 1)
//           .add(Aspect.FIRE, 1)
//           .add(Aspect.EARTH, 1)
//           .add(Aspect.WATER, 1)
//           .add(Aspect.ORDER, 1)
//           .add(Aspect.ENTROPY, 1)
//           .add(cost);
//   public AspectList getVisCost(ItemStack itemstack) {
//      if (!(itemstack.getItem() instanceof IWandFocusItem wandFocusItem)) {
//         return cost;
//      }
//      var upgrades = wandFocusItem.getAppliedWandUpgrades(itemstack);
//      if (upgrades.getOrDefault(FocusUpgradeType.silktouch,0) > 0
//              || upgrades.getOrDefault(dowsing,0) > 0
//      ) {
//         return withSilkTouchOrDowsing;
//      }
//      return cost;
//   }
//
//   public boolean isVisCostPerTick(ItemStack itemstack) {
//      return true;
//   }
//
//   public ItemStack onFocusRightClick(ItemStack itemstack, World world, Player p, HitResult mop) {
//      p.setItemInUse(itemstack, Integer.MAX_VALUE);
//      return itemstack;
//   }
//
//   public void onUsingFocusTick(ItemStack stack, Player p, int count) {
//      WandCastingItem wand = (WandCastingItem)stack.getItem();
//      if (!wand.consumeAllVis(stack, p, this.getVisCost(stack), false, false)) {
//         p.stopUsingItem();
//      } else {
//         String pp = "R" + p.getCommandSenderName();
//         if (Platform.getEnvironment() != Env.CLIENT) {
//            pp = "S" + p.getCommandSenderName();
//         }
//
//          soundDelay.putIfAbsent(pp, 0L);
//
//          breakcount.putIfAbsent(pp, 0.0F);
//
//          lastX.putIfAbsent(pp, 0);
//
//          lastY.putIfAbsent(pp, 0);
//
//          lastZ.putIfAbsent(pp, 0);
//
//         HitResult mop = BlockUtils.getTargetBlock(p.level(), p, false);
//         Vec3 v = p.getLookVec();
//         double tx = p.posX + v.xCoord * (double)10.0F;
//         double ty = p.posY + v.yCoord * (double)10.0F;
//         double tz = p.posZ + v.zCoord * (double)10.0F;
//         int impact = 0;
//         if (mop != null) {
//            tx = mop.hitVec.xCoord;
//            ty = mop.hitVec.yCoord;
//            tz = mop.hitVec.zCoord;
//            impact = 5;
//            if (Platform.getEnvironment() != Env.CLIENT && (Long)soundDelay.get(pp) < System.currentTimeMillis()) {
//               p.level().playSoundEffect(tx, ty, tz, "thaumcraft:rumble", 0.3F, 1.0F);
//               soundDelay.put(pp, System.currentTimeMillis() + 1200L);
//            }
//         } else {
//            soundDelay.put(pp, 0L);
//         }
//
//         if (p.level().isRemote) {
//            beam.put(pp, Thaumcraft.proxy.beamCont(p.level(), p, tx, ty, tz, 2, 65382, false, impact > 0 ? 2.0F : 0.0F, beam.get(pp), impact));
//         }
//
//         if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK && p.level().canMineBlock(p, mop.blockX, mop.blockY, mop.blockZ)) {
//            Block bi = p.level().getBlock(mop.blockX, mop.blockY, mop.blockZ);
//            int md = p.level().getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
//            float hardness = bi.getBlockHardness(p.level(), mop.blockX, mop.blockY, mop.blockZ);
//            if (hardness >= 0.0F) {
//               int pot = wand.getFocusPotency(stack);
//               float speed = 0.05F + (float)pot * 0.1F;
//               if (bi.getMaterial() == Material.rock || bi.getMaterial() == Material.grass || bi.getMaterial() == Material.ground || bi.getMaterial() == Material.sand) {
//                  speed = 0.25F + (float)pot * 0.25F;
//               }
//
//               if (bi == Blocks.obsidian) {
//                  speed *= 3.0F;
//               }
//
//               if ((Integer)lastX.get(pp) == mop.blockX && (Integer)lastY.get(pp) == mop.blockY && (Integer)lastZ.get(pp) == mop.blockZ) {
//                  float bc = (Float)breakcount.get(pp);
//                  if (p.level().isRemote && bc > 0.0F && bi != Blocks.air) {
//                     int progress = (int)(bc / hardness * 9.0F);
//                     Thaumcraft.proxy.excavateFX(mop.blockX, mop.blockY, mop.blockZ, p, Block.getIdFromBlock(bi), md, progress);
//                  }
//
//                  if (p.level().isRemote) {
//                     if (bc >= hardness) {
//                        breakcount.put(pp, 0.0F);
//                     } else {
//                        breakcount.put(pp, bc + speed);
//                     }
//                  } else if (bc >= hardness && wand.consumeAllVis(stack, p, this.getVisCost(stack), true, false)) {
//                     if (this.excavate(p.level(), stack, p, bi, md, mop.blockX, mop.blockY, mop.blockZ)) {
//                        for(int a = 0; a < wand.getFocusEnlarge(stack); ++a) {
//                           if (wand.consumeAllVis(stack, p, this.getVisCost(stack), false, false) && this.breakNeighbour(p, mop.blockX, mop.blockY, mop.blockZ, bi, md, stack)) {
//                              wand.consumeAllVis(stack, p, this.getVisCost(stack), true, false);
//                           }
//                        }
//                     }
//
//                     lastX.put(pp, Integer.MAX_VALUE);
//                     lastY.put(pp, Integer.MAX_VALUE);
//                     lastZ.put(pp, Integer.MAX_VALUE);
//                     breakcount.put(pp, 0.0F);
//                  } else {
//                     breakcount.put(pp, bc + speed);
//                  }
//               } else {
//                  lastX.put(pp, mop.blockX);
//                  lastY.put(pp, mop.blockY);
//                  lastZ.put(pp, mop.blockZ);
//                  breakcount.put(pp, 0.0F);
//               }
//            }
//         } else {
//            lastX.put(pp, Integer.MAX_VALUE);
//            lastY.put(pp, Integer.MAX_VALUE);
//            lastZ.put(pp, Integer.MAX_VALUE);
//            breakcount.put(pp, 0.0F);
//         }
//
//      }
//   }
//
//   private boolean excavate(World world, ItemStack stack, Player player, Block block, int md, int x, int y, int z) {
//      WorldSettings.GameType gt = GameType.SURVIVAL;
//      if (player.capabilities.allowEdit) {
//         if (player.capabilities.isCreativeMode) {
//            gt = GameType.CREATIVE;
//         }
//      } else {
//         gt = GameType.ADVENTURE;
//      }
//
//      BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world, gt, (ServerPlayer)player, x, y, z);
//      if (event.isCanceled()) {
//         return false;
//      } else {
//         WandCastingItem wand = (WandCastingItem)stack.getItem();
//         int fortune = wand.getFocusTreasure(stack);
//         boolean silk = this.isUpgradedWith(wand.getFocusItem(stack), FocusUpgradeType.silktouch);
//         if (silk && block.canSilkHarvest(player.level(), player, x, y, z, md)) {
//            ArrayList<ItemStack> items = new ArrayList<>();
//            ItemStack itemstack = BlockUtils.createStackedBlock(block, md);
//            if (itemstack != null) {
//               items.add(itemstack);
//            }
//
//            ForgeEventFactory.fireBlockHarvesting(items, world, block, x, y, z, md, 0, 1.0F, true, player);
//
//            for(ItemStack is : items) {
//               BlockUtils.dropBlockAsItem(world, x, y, z, is, block);
//            }
//         } else {
//            BlockUtils.dropBlockAsItemWithChance(world, block, x, y, z, md, 1.0F, fortune, player);
//            block.dropXpOnBlockBreak(world, x, y, z, block.getExpDrop(world, md, fortune));
//         }
//
//         world.setBlockToAir(x, y, z);
//         world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (md << 12));
//         return true;
//      }
//   }
//
//   boolean breakNeighbour(Player p, int x, int y, int z, Block block, int md, ItemStack stack) {
//      List<Direction> directions = Arrays.asList(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
//      Collections.shuffle(directions, p.level().rand);
//
//      for(Direction dir : directions) {
//         if (p.level().getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == block && p.level().getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == md && this.excavate(p.level(), stack, p, block, md, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
//            return true;
//         }
//      }
//
//      return false;
//   }
//
//   public void onPlayerStoppedUsing(ItemStack stack, World world, Player p, int count) {
//      String pp = "R" + p.getCommandSenderName();
//      if (Platform.getEnvironment() != Env.CLIENT) {
//         pp = "S" + p.getCommandSenderName();
//      }
//
//       soundDelay.putIfAbsent(pp, 0L);
//
//       breakcount.putIfAbsent(pp, 0.0F);
//
//       lastX.putIfAbsent(pp, 0);
//
//       lastY.putIfAbsent(pp, 0);
//
//       lastZ.putIfAbsent(pp, 0);
//
//      beam.put(pp, null);
//      lastX.put(pp, Integer.MAX_VALUE);
//      lastY.put(pp, Integer.MAX_VALUE);
//      lastZ.put(pp, Integer.MAX_VALUE);
//      breakcount.put(pp, 0.0F);
//   }
//
//   public WandFocusAnimation getAnimation(ItemStack itemstack) {
//      return WandFocusAnimation.CHARGE;
//   }
//
//   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
//      switch (rank) {
//         case 1:
//            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.treasure};
//         case 2:
//            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.enlarge};
//         case 3:
//            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.treasure, dowsing};
//         case 4:
//            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.enlarge};
//         case 5:
//            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.treasure, FocusUpgradeType.silktouch};
//         default:
//            return null;
//      }
//   }

}
