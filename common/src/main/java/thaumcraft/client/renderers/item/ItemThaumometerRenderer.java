package thaumcraft.client.renderers.item;

@Deprecated(forRemoval = true,since = "see ThaumometerItemRenderer")
public class ItemThaumometerRenderer /*implements IItemRenderer*/ {
////   private IModelCustom model;
//   private static final ResourceLocation SCANNER = new ResourceLocation("thaumcraft", "textures/models/scanner.obj");
//
////   private IModel model;
//
//   public ItemThaumometerRenderer() {
//      // 加载 OBJ 模型
////      this.model = ObjLoader.INSTANCE.loadModel(SCANNER);
//   }
//
////   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
////      return true;
////   }
////
////   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
////      return true;
////   }
//
////   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
////      Minecraft mc = Minecraft.getMinecraft();
////      int rve_id = 0;
////      int player_id = 0;
////      if (type == ItemRenderType.EQUIPPED) {
////         rve_id = mc.renderViewEntity.getEntityId();
////         player_id = ((EntityLivingBase)data[1]).getEntityId();
////      }
////
////      LocalPlayer playermp = mc.thePlayer;
////      float par1 = UtilsFX.getTimer(mc).renderPartialTicks;
////      float var7 = 0.8F;
////      LocalPlayer playersp = playermp;
////      GL11.glPushMatrix();
////      if (type == ItemRenderType.EQUIPPED_FIRST_PERSON && player_id == rve_id && mc.gameSettings.thirdPersonView == 0) {
////         GL11.glTranslatef(1.0F, 0.75F, -1.0F);
////         GL11.glRotatef(135.0F, 0.0F, -1.0F, 0.0F);
////         float f3 = playersp.prevRenderArmPitch + (playersp.renderArmPitch - playersp.prevRenderArmPitch) * par1;
////         float f4 = playersp.prevRenderArmYaw + (playersp.renderArmYaw - playersp.prevRenderArmYaw) * par1;
////         GL11.glRotatef((playermp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
////         GL11.glRotatef((playermp.rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
////         float var10000 = playermp.prevRotationPitch + (playermp.rotationPitch - playermp.prevRotationPitch) * par1;
////         float f1 = UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer) + (UtilsFX.getEquippedProgress(mc.entityRenderer.itemRenderer) - UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer)) * par1;
////         GL11.glTranslatef(-0.7F * var7, -(-0.65F * var7) + (1.0F - f1) * 1.5F, 0.9F * var7);
////         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
////         GL11.glTranslatef(0.0F, 0.0F * var7, -0.9F * var7);
////         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
////         GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
////         GL11.glEnable(32826);
////         GL11.glPushMatrix();
////         GL11.glScalef(5.0F, 5.0F, 5.0F);
////         mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());
////
////         for(int var9 = 0; var9 < 2; ++var9) {
////            int var22 = var9 * 2 - 1;
////            GL11.glPushMatrix();
////            GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float)var22);
////            GL11.glRotatef((float)(-45 * var22), 1.0F, 0.0F, 0.0F);
////            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
////            GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
////            GL11.glRotatef((float)(-65 * var22), 0.0F, 1.0F, 0.0F);
////            Render var24 = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
////            RenderPlayer var26 = (RenderPlayer)var24;
////            float var13 = 1.0F;
////            GL11.glScalef(var13, var13, var13);
////            var26.renderFirstPersonArm(mc.thePlayer);
////            GL11.glPopMatrix();
////         }
////
////         GL11.glPopMatrix();
////         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
////         GL11.glTranslatef(0.4F, -0.4F, 0.0F);
////         GL11.glEnable(32826);
////         GL11.glScalef(2.0F, 2.0F, 2.0F);
////      } else {
////         GL11.glScalef(0.5F, 0.5F, 0.5F);
////         if (type == ItemRenderType.EQUIPPED) {
////            GL11.glTranslatef(1.6F, 0.3F, 2.0F);
////            GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
////            GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
////         } else if (type == ItemRenderType.INVENTORY) {
////            GL11.glRotatef(60.0F, 1.0F, 0.0F, 0.0F);
////            GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
////            GL11.glRotatef(248.0F, 0.0F, -1.0F, 0.0F);
////         }
////      }
////
////      UtilsFX.bindTexture("textures/models/scanner.png");
////      this.model.renderAll();
////      GL11.glPushMatrix();
////      GL11.glTranslatef(0.0F, 0.11F, 0.0F);
////      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
////      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
////      UtilsFX.renderQuadCenteredFromTexture("textures/models/scanscreen.png", 2.5F, 1.0F, 1.0F, 1.0F, (int)(190.0F + MathHelper.sin((float)(playermp.ticksExisted - playermp.level().rand.nextInt(2))) * 10.0F + 10.0F), 771, 1.0F);
////      if (playermp instanceof Player && type == ItemRenderType.EQUIPPED_FIRST_PERSON && player_id == rve_id && mc.gameSettings.thirdPersonView == 0) {
////         RenderHelper.disableStandardItemLighting();
////         int j = (int)(190.0F + MathHelper.sin((float)(playermp.ticksExisted - playermp.level().rand.nextInt(2))) * 10.0F + 10.0F);
////         int k = j % 65536;
////         int l = j / 65536;
////         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
////         ScanResult scan = this.doScan(playermp.inventory.getCurrentItem(), playermp);
////         if (scan != null) {
////            AspectList<Aspect>aspects = null;
////            GL11.glTranslatef(0.0F, 0.0F, -0.01F);
////            String text = "?";
////            ItemStack stack = null;
////            if (scan.id > 0) {
////               stack = new ItemStack(Item.getItemById(scan.id), 1, scan.meta);
////               if (ScanManager.hasBeenScanned(playermp, scan)) {
////                  aspects = ScanManager.getScanAspects(scan, playermp.level());
////               }
////            }
////
////            if (scan.type == 2) {
////               if (scan.entity instanceof EntityItem) {
////                  stack = ((EntityItem)scan.entity).getEntityItem();
////               } else {
////                  text = scan.entity.getCommandSenderName();
////               }
////
////               if (ScanManager.hasBeenScanned(playermp, scan)) {
////                  aspects = ScanManager.getScanAspects(scan, playermp.level());
////               }
////            }
////
////            if (scan.type == 3 && scan.phenomena.startsWith("NODE") && ScanManager.hasBeenScanned(playermp, scan)) {
////               HitResult mop = null;
////               if (stack != null && stack.getItem() != null) {
////                  mop = EntityUtils.getHitResultFromPlayer(playermp.level(), playermp, true);
////               }
////
////               if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
////                  TileEntity tile = playermp.level().getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
////                  if (tile instanceof INode) {
////                     aspects = ((INode)tile).getAspects();
////                     GL11.glPushMatrix();
////                     GL11.glEnable(GL11.GL_BLEND);
////                     GL11.glBlendFunc(770, 1);
////                     String t = StatCollector.translateToLocal("nodetype." + ((INode)tile).getNodeType() + ".name");
////                     if (((INode)tile).getNodeModifier() != null) {
////                        t = t + ", " + StatCollector.translateToLocal("nodemod." + ((INode)tile).getNodeModifier() + ".name");
////                     }
////
////                     int sw = mc.fontRenderer.getStringWidth(t);
////                     float scale = 0.004F;
////                     GL11.glScalef(scale, scale, scale);
////                     mc.fontRenderer.drawString(t, -sw / 2, -40, 15642134);
////                     GL11.glDisable(GL11.GL_BLEND);
////                     GL11.glPopMatrix();
////                  }
////               }
////            }
////
////            if (stack != null) {
////               if (stack.getItem() != null) {
////                  try {
////                     text = stack.getDisplayName();
////                  } catch (Exception ignored) {
////                  }
////               } else if (stack.getItem() != null) {
////                  try {
////                     text = stack.getItem().getItemStackDisplayName(stack);
////                  } catch (Exception ignored) {
////                  }
////               }
////            }
////
////            if (aspects != null) {
////               int posX = 0;
////               int posY = 0;
////               int aa = aspects.size();
////               int baseX = Math.min(5, aa) * 8;
////
////               for(Aspect aspect : aspects.getAspectsSorted()) {
////                  GL11.glPushMatrix();
////                  GL11.glScalef(0.0075F, 0.0075F, 0.0075F);
////                  j = (int)(190.0F + MathHelper.sin((float)(posX + playermp.ticksExisted - playermp.level().rand.nextInt(2))) * 10.0F + 10.0F);
////                  k = j % 65536;
////                  l = j / 65536;
////                  OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
////                  UtilsFX.drawTag(-baseX + posX * 16, -8 + posY * 16, aspect, (float)aspects.getAmount(aspect), 0, 0.01, 1, 1.0F, false);
////                  GL11.glPopMatrix();
////                  ++posX;
////                  if (posX >= 5 - posY) {
////                     posX = 0;
////                     ++posY;
////                     aa -= 5 - posY;
////                     baseX = Math.min(5 - posY, aa) * 8;
////                  }
////               }
////            }
////
////            if (text == null) {
////               text = "?";
////            }
////
////            if (!text.isEmpty()) {
////               RenderHelper.disableStandardItemLighting();
////               GL11.glPushMatrix();
////               GL11.glEnable(GL11.GL_BLEND);
////               GL11.glBlendFunc(770, 1);
////               GL11.glTranslatef(0.0F, -0.25F, 0.0F);
////               int sw = mc.fontRenderer.getStringWidth(text);
////               float scale = 0.005F;
////               if (sw > 90) {
////                  scale -= 2.5E-5F * (float)(sw - 90);
////               }
////
////               GL11.glScalef(scale, scale, scale);
////               mc.fontRenderer.drawString(text, -sw / 2, 0, 16777215);
////               GL11.glDisable(GL11.GL_BLEND);
////               GL11.glPopMatrix();
////            }
////         }
////
////         RenderHelper.enableGUIStandardItemLighting();
////      }
////
////      GL11.glPopMatrix();
////      GL11.glPopMatrix();
////   }
//
//   public void renderItem(ItemStack stack, Player player, PoseStack matrix, MultiBufferSource buffer, ItemTransforms.TransformType transformType, float partialTicks, int light) {
//      // 获取指向的实体或方块进行扫描
//      if (player.level().isClientSide() && player.equals(mc.player)) {
//         ScanResult scan = doScan(stack, player);
//         if (scan != null && scan.equals(startScan)) {
//            renderScan(matrix, buffer, scan, player, partialTicks, light);
//         }
//      }
//   }
//
//   private void renderScan(PoseStack matrix, MultiBufferSource buffer, ScanResult scan, Player player, float partialTicks, int light) {
//      // 渲染扫描效果和扫描屏幕
//      matrix.pushPose();
//
//      // 绑定扫描屏幕纹理
//      UtilsFX.bindTexture(SCAN_SCREEN_TEXTURE);
//
//      // 可视化扫描屏幕
//      float flicker = (float)(190 + MathHelper.sin(player.tickCount - player.level().getRandom().nextInt(2)) * 10 + 10);
//      UtilsFX.renderQuadCenteredFromTexture(SCAN_SCREEN_TEXTURE, 2.5F, 1F, 1F, 1F, (int)flicker, 771, 1.0F);
//
//      // 渲染实体或物品扫描信息
//      if (scan.type == 2 && scan.entity != null) {
//         String text = scan.entity.getName().getString();
//         renderFloatingText(matrix, buffer, text, player);
//      }
//
//      if (scan.type == 3 && scan.phenomena.startsWith("NODE")) {
//         // 渲染节点名称
//         renderFloatingText(matrix, buffer, Component.literal("block.thaumcraft.aura_node").toString(), player);
////         INode node = (INode) player.level().getBlockEntity(scan.blockPos);
////         if (node != null) {
////            String name = "NODE" + node.getId();
////            renderFloatingText(matrix, buffer, name, player);
////         }
//      }
//
//      matrix.popPose();
//   }
//
//   private void renderFloatingText(PoseStack matrix, MultiBufferSource buffer, String text, Player player) {
//      var mc = Minecraft.getInstance();
//      matrix.pushPose();
//      matrix.translate(0, -0.25, 0);
//      float scale = 0.005F;
//      int sw = mc.font.width(text);
//      if (sw > 90) scale -= 2.5E-5F * (sw - 90);
//      matrix.scale(scale, scale, scale);
//      mc.font.drawInBatch(text, -sw / 2F, 0F, 0xFFFFFF, false, matrix.last().pose(), buffer, false, 0, 15728880);
//      matrix.popPose();
//   }
//
//
//   private ScanResult doScan(ItemStack stack, Player p) {
//      if (stack != null && p != null) {
//         Entity pointedEntity = EntityUtils.getPointedEntity( p, 0.5F, 10.0F, 0.0F, true);
//         if (pointedEntity != null) {
//            ScanResult sr = new ScanResult((byte)2, (Item) null, pointedEntity, "");
//            return sr;
//         } else {
//            HitResult mop = EntityUtils.getHitResultFromPlayer(p.level(), p, true);
//            if (mop instanceof BlockHitResult blockHitResult && mop.getType() != HitResult.Type.MISS) {
//               BlockState bi = p.level().getBlockState(blockHitResult.getBlockPos());
//               BlockEntity tile = p.level().getBlockEntity(blockHitResult.getBlockPos());
//               if (tile instanceof INode node) {
////                  int md = bi.getDamageValue(p.level(), mop.blockX, mop.blockY, mop.blockZ);
//                   return new ScanResult((byte)3, bi.getBlock().asItem(), null, "NODE" + (node.getId()));
//               }
//
//               if (bi.getBlock() != Blocks.AIR) {
//                  ItemStack is = bi.getBlock().asItem().getDefaultInstance();//getPickBlock(mop, p.level(), mop.blockX, mop.blockY, mop.blockZ);
//                  ScanResult sr = null;
//                  sr = new ScanResult((byte)1, bi.getBlock().asItem(), null, "");
//
//                  return sr;
//               }
//            }
//
//            for(IScanEventHandler seh : ThaumcraftApi.scanEventhandlers) {
//               ScanResult scan = seh.scanPhenomena(stack, p.level(), p);
//               if (scan != null) {
//                  return scan;
//               }
//            }
//
//            return null;
//         }
//      } else {
//         return null;
//      }
//   }
}
