package thaumcraft.client.renderers.item;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;

public class ItemThaumometerRenderer implements IItemRenderer {
   private IModelCustom model;
   private static final ResourceLocation SCANNER = new ResourceLocation("thaumcraft", "textures/models/scanner.obj");

   public ItemThaumometerRenderer() {
      this.model = AdvancedModelLoader.loadModel(SCANNER);
   }

   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return true;
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return true;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      Minecraft mc = Minecraft.getMinecraft();
      int rve_id = 0;
      int player_id = 0;
      if (type == ItemRenderType.EQUIPPED) {
         rve_id = mc.renderViewEntity.getEntityId();
         player_id = ((EntityLivingBase)data[1]).getEntityId();
      }

      EntityPlayerSP playermp = mc.thePlayer;
      float par1 = UtilsFX.getTimer(mc).renderPartialTicks;
      float var7 = 0.8F;
      EntityPlayerSP playersp = playermp;
      GL11.glPushMatrix();
      if (type == ItemRenderType.EQUIPPED_FIRST_PERSON && player_id == rve_id && mc.gameSettings.thirdPersonView == 0) {
         GL11.glTranslatef(1.0F, 0.75F, -1.0F);
         GL11.glRotatef(135.0F, 0.0F, -1.0F, 0.0F);
         float f3 = playersp.prevRenderArmPitch + (playersp.renderArmPitch - playersp.prevRenderArmPitch) * par1;
         float f4 = playersp.prevRenderArmYaw + (playersp.renderArmYaw - playersp.prevRenderArmYaw) * par1;
         GL11.glRotatef((playermp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef((playermp.rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
         float var10000 = playermp.prevRotationPitch + (playermp.rotationPitch - playermp.prevRotationPitch) * par1;
         float f1 = UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer) + (UtilsFX.getEquippedProgress(mc.entityRenderer.itemRenderer) - UtilsFX.getPrevEquippedProgress(mc.entityRenderer.itemRenderer)) * par1;
         GL11.glTranslatef(-0.7F * var7, -(-0.65F * var7) + (1.0F - f1) * 1.5F, 0.9F * var7);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(0.0F, 0.0F * var7, -0.9F * var7);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
         GL11.glEnable(32826);
         GL11.glPushMatrix();
         GL11.glScalef(5.0F, 5.0F, 5.0F);
         mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());

         for(int var9 = 0; var9 < 2; ++var9) {
            int var22 = var9 * 2 - 1;
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float)var22);
            GL11.glRotatef((float)(-45 * var22), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float)(-65 * var22), 0.0F, 1.0F, 0.0F);
            Render var24 = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
            RenderPlayer var26 = (RenderPlayer)var24;
            float var13 = 1.0F;
            GL11.glScalef(var13, var13, var13);
            var26.renderFirstPersonArm(mc.thePlayer);
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(0.4F, -0.4F, 0.0F);
         GL11.glEnable(32826);
         GL11.glScalef(2.0F, 2.0F, 2.0F);
      } else {
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         if (type == ItemRenderType.EQUIPPED) {
            GL11.glTranslatef(1.6F, 0.3F, 2.0F);
            GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
            GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
         } else if (type == ItemRenderType.INVENTORY) {
            GL11.glRotatef(60.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
            GL11.glRotatef(248.0F, 0.0F, -1.0F, 0.0F);
         }
      }

      UtilsFX.bindTexture("textures/models/scanner.png");
      this.model.renderAll();
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.11F, 0.0F);
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      UtilsFX.renderQuadCenteredFromTexture("textures/models/scanscreen.png", 2.5F, 1.0F, 1.0F, 1.0F, (int)(190.0F + MathHelper.sin((float)(playermp.ticksExisted - playermp.worldObj.rand.nextInt(2))) * 10.0F + 10.0F), 771, 1.0F);
      if (playermp instanceof EntityPlayer && type == ItemRenderType.EQUIPPED_FIRST_PERSON && player_id == rve_id && mc.gameSettings.thirdPersonView == 0) {
         RenderHelper.disableStandardItemLighting();
         int j = (int)(190.0F + MathHelper.sin((float)(playermp.ticksExisted - playermp.worldObj.rand.nextInt(2))) * 10.0F + 10.0F);
         int k = j % 65536;
         int l = j / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         ScanResult scan = this.doScan(playermp.inventory.getCurrentItem(), playermp);
         if (scan != null) {
            AspectList aspects = null;
            GL11.glTranslatef(0.0F, 0.0F, -0.01F);
            String text = "?";
            ItemStack stack = null;
            if (scan.id > 0) {
               stack = new ItemStack(Item.getItemById(scan.id), 1, scan.meta);
               if (ScanManager.hasBeenScanned(playermp, scan)) {
                  aspects = ScanManager.getScanAspects(scan, playermp.worldObj);
               }
            }

            if (scan.type == 2) {
               if (scan.entity instanceof EntityItem) {
                  stack = ((EntityItem)scan.entity).getEntityItem();
               } else {
                  text = scan.entity.getCommandSenderName();
               }

               if (ScanManager.hasBeenScanned(playermp, scan)) {
                  aspects = ScanManager.getScanAspects(scan, playermp.worldObj);
               }
            }

            if (scan.type == 3 && scan.phenomena.startsWith("NODE") && ScanManager.hasBeenScanned(playermp, scan)) {
               MovingObjectPosition mop = null;
               if (stack != null && stack.getItem() != null) {
                  mop = EntityUtils.getMovingObjectPositionFromPlayer(playermp.worldObj, playermp, true);
               }

               if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                  TileEntity tile = playermp.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                  if (tile instanceof INode) {
                     aspects = ((INode)tile).getAspects();
                     GL11.glPushMatrix();
                     GL11.glEnable(GL11.GL_BLEND);
                     GL11.glBlendFunc(770, 1);
                     String t = StatCollector.translateToLocal("nodetype." + ((INode)tile).getNodeType() + ".name");
                     if (((INode)tile).getNodeModifier() != null) {
                        t = t + ", " + StatCollector.translateToLocal("nodemod." + ((INode)tile).getNodeModifier() + ".name");
                     }

                     int sw = mc.fontRenderer.getStringWidth(t);
                     float scale = 0.004F;
                     GL11.glScalef(scale, scale, scale);
                     mc.fontRenderer.drawString(t, -sw / 2, -40, 15642134);
                     GL11.glDisable(GL11.GL_BLEND);
                     GL11.glPopMatrix();
                  }
               }
            }

            if (stack != null) {
               if (stack.getItem() != null) {
                  try {
                     text = stack.getDisplayName();
                  } catch (Exception ignored) {
                  }
               } else if (stack.getItem() != null) {
                  try {
                     text = stack.getItem().getItemStackDisplayName(stack);
                  } catch (Exception ignored) {
                  }
               }
            }

            if (aspects != null) {
               int posX = 0;
               int posY = 0;
               int aa = aspects.size();
               int baseX = Math.min(5, aa) * 8;

               for(Aspect aspect : aspects.getAspectsSorted()) {
                  GL11.glPushMatrix();
                  GL11.glScalef(0.0075F, 0.0075F, 0.0075F);
                  j = (int)(190.0F + MathHelper.sin((float)(posX + playermp.ticksExisted - playermp.worldObj.rand.nextInt(2))) * 10.0F + 10.0F);
                  k = j % 65536;
                  l = j / 65536;
                  OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
                  UtilsFX.drawTag(-baseX + posX * 16, -8 + posY * 16, aspect, (float)aspects.getAmount(aspect), 0, 0.01, 1, 1.0F, false);
                  GL11.glPopMatrix();
                  ++posX;
                  if (posX >= 5 - posY) {
                     posX = 0;
                     ++posY;
                     aa -= 5 - posY;
                     baseX = Math.min(5 - posY, aa) * 8;
                  }
               }
            }

            if (text == null) {
               text = "?";
            }

            if (!text.isEmpty()) {
               RenderHelper.disableStandardItemLighting();
               GL11.glPushMatrix();
               GL11.glEnable(GL11.GL_BLEND);
               GL11.glBlendFunc(770, 1);
               GL11.glTranslatef(0.0F, -0.25F, 0.0F);
               int sw = mc.fontRenderer.getStringWidth(text);
               float scale = 0.005F;
               if (sw > 90) {
                  scale -= 2.5E-5F * (float)(sw - 90);
               }

               GL11.glScalef(scale, scale, scale);
               mc.fontRenderer.drawString(text, -sw / 2, 0, 16777215);
               GL11.glDisable(GL11.GL_BLEND);
               GL11.glPopMatrix();
            }
         }

         RenderHelper.enableGUIStandardItemLighting();
      }

      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   private ScanResult doScan(ItemStack stack, EntityPlayer p) {
      if (stack != null && p != null) {
         Entity pointedEntity = EntityUtils.getPointedEntity(p.worldObj, p, 0.5F, 10.0F, 0.0F, true);
         if (pointedEntity != null) {
            ScanResult sr = new ScanResult((byte)2, 0, 0, pointedEntity, "");
            return sr;
         } else {
            MovingObjectPosition mop = EntityUtils.getMovingObjectPositionFromPlayer(p.worldObj, p, true);
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
               Block bi = p.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
               TileEntity tile = p.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
               if (tile instanceof INode) {
                  int md = bi.getDamageValue(p.worldObj, mop.blockX, mop.blockY, mop.blockZ);
                  ScanResult sr = new ScanResult((byte)3, Block.getIdFromBlock(bi), md, null, "NODE" + ((INode)tile).getId());
                  return sr;
               }

               if (bi != Blocks.air) {
                  ItemStack is = bi.getPickBlock(mop, p.worldObj, mop.blockX, mop.blockY, mop.blockZ);
                  ScanResult sr = null;
                  int md = p.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);

                  try {
                     if (is == null) {
                        is = BlockUtils.createStackedBlock(bi, md);
                     }
                  } catch (Exception ignored) {
                  }

                  try {
                     if (is == null) {
                        sr = new ScanResult((byte)1, Block.getIdFromBlock(bi), md, null, "");
                     } else {
                        sr = new ScanResult((byte)1, Item.getIdFromItem(is.getItem()), is.getItemDamage(), null, "");
                     }
                  } catch (Exception ignored) {
                  }

                  return sr;
               }
            }

            for(IScanEventHandler seh : ThaumcraftApi.scanEventhandlers) {
               ScanResult scan = seh.scanPhenomena(stack, p.worldObj, p);
               if (scan != null) {
                  return scan;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }
}
