package thaumcraft.client.lib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simpleutils.bauble.BaubleConsumer;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.blocks.BlockCosmeticOpaque;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemFocusPouch;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.events.KeyHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;

import static simpleutils.bauble.BaubleUtils.forEachBauble;

public class REHWandHandler {
   static float radialHudScale = 0.0F;
   TreeMap<String,Integer> foci = new TreeMap<>();
   HashMap<String,ItemStack> fociItem = new HashMap<>();
   HashMap<String,Boolean> fociHover = new HashMap<>();
   HashMap<String,Float> fociScale = new HashMap<>();
   long lastTime = 0L;
   boolean lastState = false;
   RenderBlocks renderBlocks = new RenderBlocks();
   int lastArcHash = 0;
   ArrayList<BlockCoordinates> architectBlocks = new ArrayList<>();
   String tex = "textures/misc/architect_arrows.png";

   private void putFocusBasic(ItemStack stack,ItemFocusBasic focusBasic,int fociAt) {

      this.foci.put(focusBasic.getSortingHelper(stack), fociAt);
      this.fociItem.put(focusBasic.getSortingHelper(stack), stack.copy());
      this.fociScale.put(focusBasic.getSortingHelper(stack), 1.0F);
      this.fociHover.put(focusBasic.getSortingHelper(stack), false);
   }

   @SideOnly(Side.CLIENT)
   public void handleFociRadial(Minecraft mc, long time, RenderGameOverlayEvent event) {
      if (KeyHandler.radialActive || radialHudScale > 0.0F) {
         long timeDiff = System.currentTimeMillis() - KeyHandler.lastPressF;
         if (!KeyHandler.radialActive) {
            if (mc.currentScreen == null && this.lastState) {
               if (Display.isActive() && !mc.inGameHasFocus) {
                  mc.inGameHasFocus = true;
                  mc.mouseHelper.grabMouseCursor();
               }

               this.lastState = false;
            }
         } else {
            if (mc.currentScreen != null) {
               KeyHandler.radialActive = false;
               KeyHandler.radialLock = true;
               mc.setIngameFocus();
               mc.setIngameNotInFocus();
               return;
            }

            if (radialHudScale == 0.0F) {
               this.foci.clear();
               this.fociItem.clear();
               this.fociHover.clear();
               this.fociScale.clear();
               final int[] pouchcount = {0};
               final ItemStack[] item = {null};
               BaubleConsumer<ItemFocusPouch> focusPouchConsumer = (slot,stack,pouch) -> {
                  ++pouchcount[0];
                  ItemStack[] inv = pouch.getInventory(stack);

                  for(int q = 0; q < inv.length; ++q) {
                     stack = inv[q];
                     if (stack != null && stack.getItem() instanceof ItemFocusBasic) {
                        ItemFocusBasic focusBasic = (ItemFocusBasic) stack.getItem();
                        putFocusBasic(stack,focusBasic,q + pouchcount[0]*1000);
                     }
                  }
                  return false;
               };
               forEachBauble(mc.thePlayer,ItemFocusPouch.class,focusPouchConsumer);

               for(int a = 0; a < 36; ++a) {
                  ItemStack stack = mc.thePlayer.inventory.mainInventory[a];
                  if (stack == null){
                     continue;
                  }
                  Item stackItem = stack.getItem();
                  if (stackItem instanceof ItemFocusBasic) {
                     ItemFocusBasic focusBasic = (ItemFocusBasic) stackItem;
                     putFocusBasic(stack,focusBasic,a);
                  }

                  if (stackItem instanceof ItemFocusPouch) {
                     focusPouchConsumer.accept(a,stack, (ItemFocusPouch) stackItem);
                  }
               }

               if (!this.foci.isEmpty() && mc.inGameHasFocus) {
                  mc.inGameHasFocus = false;
                  mc.mouseHelper.ungrabMouseCursor();
               }
            }
         }

         this.renderFocusRadialHUD(event.resolution.getScaledWidth_double(), event.resolution.getScaledHeight_double(), time, event.partialTicks);
         if (time > this.lastTime) {
            for(String key : this.fociHover.keySet()) {
               if (this.fociHover.get(key)) {
                  if (!KeyHandler.radialActive && !KeyHandler.radialLock) {
                     PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(mc.thePlayer, key));
                     KeyHandler.radialLock = true;
                  }

                  if (this.fociScale.get(key) < 1.3F) {
                     this.fociScale.put(key, this.fociScale.get(key) + 0.025F);
                  }
               } else if (this.fociScale.get(key) > 1.0F) {
                  this.fociScale.put(key, this.fociScale.get(key) - 0.025F);
               }
            }

            if (!KeyHandler.radialActive) {
               radialHudScale -= 0.05F;
            } else if (radialHudScale < 1.0F) {
               radialHudScale += 0.05F;
            }

            if (radialHudScale > 1.0F) {
               radialHudScale = 1.0F;
            }

            if (radialHudScale < 0.0F) {
               radialHudScale = 0.0F;
               KeyHandler.radialLock = false;
            }

            this.lastTime = time + 5L;
            this.lastState = KeyHandler.radialActive;
         }
      }

   }

   @SideOnly(Side.CLIENT)
   private void renderFocusRadialHUD(double sw, double sh, long time, float partialTicks) {
      RenderItem ri = new RenderItem();
      Minecraft mc = Minecraft.getMinecraft();
      if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemWandCasting) {
         ItemWandCasting wand = (ItemWandCasting)mc.thePlayer.getCurrentEquippedItem().getItem();
         ItemFocusBasic focus = wand.getFocus(mc.thePlayer.getCurrentEquippedItem());
         int i = (int)((double)Mouse.getEventX() * sw / (double)mc.displayWidth);
         int j = (int)(sh - (double)Mouse.getEventY() * sh / (double)mc.displayHeight - (double)1.0F);
         int k = Mouse.getEventButton();
         if (!this.fociItem.isEmpty()) {
            GL11.glPushMatrix();
            GL11.glClear(256);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0F, sw, sh, 0.0F, 1000.0F, 3000.0F);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glPushMatrix();
            GL11.glTranslated(sw / (double)2.0F, sh / (double)2.0F, 0.0F);
            ItemStack tt = null;
            float width = 16.0F + (float)this.fociItem.size() * 2.5F;
            UtilsFX.bindTexture("textures/misc/radial.png");
            GL11.glPushMatrix();
            GL11.glRotatef(partialTicks + (float)(mc.thePlayer.ticksExisted % 720) / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
            UtilsFX.renderQuadCenteredFromTexture(width * 2.75F * radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
            UtilsFX.bindTexture("textures/misc/radial2.png");
            GL11.glPushMatrix();
            GL11.glRotatef(-(partialTicks + (float)(mc.thePlayer.ticksExisted % 720) / 2.0F), 0.0F, 0.0F, 1.0F);
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
            UtilsFX.renderQuadCenteredFromTexture(width * 2.55F * radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
            if (focus != null) {
               GL11.glPushMatrix();
               GL11.glEnable(32826);
               RenderHelper.enableGUIStandardItemLighting();
               ItemStack item = wand.getFocusItem(mc.thePlayer.getCurrentEquippedItem()).copy();
               item.stackTagCompound = null;
               ri.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, item, -8, -8);
               RenderHelper.disableStandardItemLighting();
               GL11.glDisable(32826);
               GL11.glPopMatrix();
               int mx = (int)((double)i - sw / (double)2.0F);
               int my = (int)((double)j - sh / (double)2.0F);
               if (mx >= -10 && mx <= 10 && my >= -10 && my <= 10) {
                  tt = wand.getFocusItem(mc.thePlayer.getCurrentEquippedItem());
               }
            }

            GL11.glScaled(radialHudScale, radialHudScale, radialHudScale);
            float currentRot = -90.0F * radialHudScale;
            float pieSlice = 360.0F / (float)this.fociItem.size();
            String key = this.foci.firstKey();

            for(int a = 0; a < this.fociItem.size(); ++a) {
               double xx = MathHelper.cos(currentRot / 180.0F * (float)Math.PI) * width;
               double yy = MathHelper.sin(currentRot / 180.0F * (float)Math.PI) * width;
               currentRot += pieSlice;
               GL11.glPushMatrix();
               GL11.glTranslated(xx, yy, 100.0F);
               GL11.glScalef(this.fociScale.get(key), this.fociScale.get(key), this.fociScale.get(key));
               GL11.glEnable(32826);
               RenderHelper.enableGUIStandardItemLighting();
               ItemStack item = this.fociItem.get(key).copy();
               item.stackTagCompound = null;
               ri.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, item, -8, -8);
               RenderHelper.disableStandardItemLighting();
               GL11.glDisable(32826);
               GL11.glPopMatrix();
               if (!KeyHandler.radialLock && KeyHandler.radialActive) {
                  int mx = (int)((double)i - sw / (double)2.0F - xx);
                  int my = (int)((double)j - sh / (double)2.0F - yy);
                  if (mx >= -10 && mx <= 10 && my >= -10 && my <= 10) {
                     this.fociHover.put(key, true);
                     tt = this.fociItem.get(key);
                     if (k == 0) {
                        KeyHandler.radialActive = false;
                        KeyHandler.radialLock = true;
                        PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(mc.thePlayer, key));
                        break;
                     }
                  } else {
                     this.fociHover.put(key, false);
                  }
               }

               key = this.foci.higherKey(key);
            }

            GL11.glPopMatrix();
            if (tt != null) {
               UtilsFX.drawCustomTooltip(mc.currentScreen, ri, mc.fontRenderer, tt.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips), -4, 20, 11);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean handleArchitectOverlay(ItemStack stack, DrawBlockHighlightEvent event, int playerticks, MovingObjectPosition target) {
      Minecraft mc = Minecraft.getMinecraft();
      IArchitect af = (IArchitect)stack.getItem();
      String h = target.blockX + "" + target.blockY + target.blockZ + target.sideHit + playerticks / 5;
      int hc = h.hashCode();
      if (hc != this.lastArcHash) {
         this.lastArcHash = hc;
         this.architectBlocks = af.getArchitectBlocks(stack, mc.theWorld, target.blockX, target.blockY, target.blockZ, target.sideHit, event.player);
      }

      if (this.architectBlocks != null && !this.architectBlocks.isEmpty()) {
         this.drawArchitectAxis(target.blockX, target.blockY, target.blockZ, event.partialTicks, af.showAxis(stack, mc.theWorld, event.player, target.sideHit, IArchitect.EnumAxis.X), af.showAxis(stack, mc.theWorld, event.player, target.sideHit, IArchitect.EnumAxis.Y), af.showAxis(stack, mc.theWorld, event.player, target.sideHit, IArchitect.EnumAxis.Z));

         for(BlockCoordinates cc : this.architectBlocks) {
            this.drawOverlayBlock(cc.x, cc.y, cc.z, playerticks, mc, event.partialTicks);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return true;
      } else {
         return false;
      }
   }

   private boolean isConnectedBlock(World world, int x, int y, int z) {
      return this.architectBlocks.contains(new BlockCoordinates(x, y, z));
   }

   @SideOnly(Side.CLIENT)
   private IIcon getIconOnSide(World world, int x, int y, int z, int side, int ticks) {
      boolean[] bitMatrix = new boolean[8];
      if (side == 0 || side == 1) {
         bitMatrix[0] = this.isConnectedBlock(world, x - 1, y, z - 1);
         bitMatrix[1] = this.isConnectedBlock(world, x, y, z - 1);
         bitMatrix[2] = this.isConnectedBlock(world, x + 1, y, z - 1);
         bitMatrix[3] = this.isConnectedBlock(world, x - 1, y, z);
         bitMatrix[4] = this.isConnectedBlock(world, x + 1, y, z);
         bitMatrix[5] = this.isConnectedBlock(world, x - 1, y, z + 1);
         bitMatrix[6] = this.isConnectedBlock(world, x, y, z + 1);
         bitMatrix[7] = this.isConnectedBlock(world, x + 1, y, z + 1);
      }

      if (side == 2 || side == 3) {
         bitMatrix[0] = this.isConnectedBlock(world, x + (side == 2 ? 1 : -1), y + 1, z);
         bitMatrix[1] = this.isConnectedBlock(world, x, y + 1, z);
         bitMatrix[2] = this.isConnectedBlock(world, x + (side == 3 ? 1 : -1), y + 1, z);
         bitMatrix[3] = this.isConnectedBlock(world, x + (side == 2 ? 1 : -1), y, z);
         bitMatrix[4] = this.isConnectedBlock(world, x + (side == 3 ? 1 : -1), y, z);
         bitMatrix[5] = this.isConnectedBlock(world, x + (side == 2 ? 1 : -1), y - 1, z);
         bitMatrix[6] = this.isConnectedBlock(world, x, y - 1, z);
         bitMatrix[7] = this.isConnectedBlock(world, x + (side == 3 ? 1 : -1), y - 1, z);
      }

      if (side == 4 || side == 5) {
         bitMatrix[0] = this.isConnectedBlock(world, x, y + 1, z + (side == 5 ? 1 : -1));
         bitMatrix[1] = this.isConnectedBlock(world, x, y + 1, z);
         bitMatrix[2] = this.isConnectedBlock(world, x, y + 1, z + (side == 4 ? 1 : -1));
         bitMatrix[3] = this.isConnectedBlock(world, x, y, z + (side == 5 ? 1 : -1));
         bitMatrix[4] = this.isConnectedBlock(world, x, y, z + (side == 4 ? 1 : -1));
         bitMatrix[5] = this.isConnectedBlock(world, x, y - 1, z + (side == 5 ? 1 : -1));
         bitMatrix[6] = this.isConnectedBlock(world, x, y - 1, z);
         bitMatrix[7] = this.isConnectedBlock(world, x, y - 1, z + (side == 4 ? 1 : -1));
      }

      int idBuilder = 0;

      for(int i = 0; i <= 7; ++i) {
         idBuilder += bitMatrix[i] ? (i == 0 ? 1 : (i == 1 ? 2 : (i == 2 ? 4 : (i == 3 ? 8 : (i == 4 ? 16 : (i == 5 ? 32 : (i == 6 ? 64 : 128))))))) : 0;
      }

      IIcon var10;
      if (idBuilder <= 255 && idBuilder >= 0) {
         BlockCosmeticOpaque var11 = (BlockCosmeticOpaque)ConfigBlocks.blockCosmeticOpaque;
         var10 = BlockCosmeticOpaque.wardedGlassIcon[UtilsFX.connectedTextureRefByID[idBuilder]];
      } else {
         BlockCosmeticOpaque var10000 = (BlockCosmeticOpaque)ConfigBlocks.blockCosmeticOpaque;
         var10 = BlockCosmeticOpaque.wardedGlassIcon[0];
      }

      return var10;
   }

   private boolean shouldSideBeRendered(int x, int y, int z, int side) {
      return !this.architectBlocks.contains(new BlockCoordinates(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]));
   }

   @SideOnly(Side.CLIENT)
   public void drawOverlayBlock(int x, int y, int z, int ticks, Minecraft mc, float partialTicks) {
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glDisable(2884);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glAlphaFunc(516, 0.003921569F);
      EntityPlayer player = (EntityPlayer)mc.renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      GL11.glTranslated(-iPX + (double)x + (double)0.5F, -iPY + (double)y, -iPZ + (double)z + (double)0.5F);
      GL11.glDisable(2896);
      Tessellator t = Tessellator.instance;
      this.renderBlocks.setRenderBounds(-0.001F, -0.001F, -0.001F, 1.001F, 1.001F, 1.001F);
      float r = MathHelper.sin((float)ticks / 2.0F + (float)x) * 0.2F + 0.3F;
      float g = MathHelper.sin((float)ticks / 3.0F + (float)y) * 0.2F + 0.3F;
      float b = MathHelper.sin((float)ticks / 4.0F + (float)z) * 0.2F + 0.8F;
      GL11.glColor4f(r, g, b, 0.2F);
      t.startDrawingQuads();
      t.setBrightness(200);
      mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      GL11.glTexEnvi(8960, 8704, 260);
      if (this.shouldSideBeRendered(x, y, z, 1)) {
         this.renderBlocks.renderFaceYNeg(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, this.getIconOnSide(mc.theWorld, x, y, z, 0, ticks));
      }

      if (this.shouldSideBeRendered(x, y, z, 0)) {
         this.renderBlocks.renderFaceYPos(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, this.getIconOnSide(mc.theWorld, x, y, z, 1, ticks));
      }

      if (this.shouldSideBeRendered(x, y, z, 3)) {
         this.renderBlocks.renderFaceZNeg(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, this.getIconOnSide(mc.theWorld, x, y, z, 2, ticks));
      }

      if (this.shouldSideBeRendered(x, y, z, 2)) {
         this.renderBlocks.renderFaceZPos(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, this.getIconOnSide(mc.theWorld, x, y, z, 3, ticks));
      }

      if (this.shouldSideBeRendered(x, y, z, 5)) {
         this.renderBlocks.renderFaceXNeg(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, this.getIconOnSide(mc.theWorld, x, y, z, 4, ticks));
      }

      if (this.shouldSideBeRendered(x, y, z, 4)) {
         this.renderBlocks.renderFaceXPos(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, this.getIconOnSide(mc.theWorld, x, y, z, 5, ticks));
      }

      t.draw();
      GL11.glTexEnvi(8960, 8704, 8448);
      GL11.glEnable(2896);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(2884);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public void drawArchitectAxis(double x, double y, double z, float partialTicks, boolean dx, boolean dy, boolean dz) {
      if (dx || dy || dz) {
         EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
         double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
         double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
         double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
         float r = MathHelper.sin((float)((double)((float)player.ticksExisted / 4.0F) + x)) * 0.2F + 0.3F;
         float g = MathHelper.sin((float)((double)((float)player.ticksExisted / 3.0F) + y)) * 0.2F + 0.3F;
         float b = MathHelper.sin((float)((double)((float)player.ticksExisted / 2.0F) + z)) * 0.2F + 0.8F;
         GL11.glPushMatrix();
         GL11.glDepthMask(false);
         GL11.glDisable(2929);
         GL11.glDisable(2884);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 1);
         GL11.glTranslated(-iPX + x + (double)0.5F, -iPY + y + (double)0.5F, -iPZ + z + (double)0.5F);
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.33F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         if (dx) {
            GL11.glPushMatrix();
            UtilsFX.renderQuadCenteredFromTexture(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            UtilsFX.renderQuadCenteredFromTexture(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glPopMatrix();
         }

         if (dz) {
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            UtilsFX.renderQuadCenteredFromTexture(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            UtilsFX.renderQuadCenteredFromTexture(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glPopMatrix();
         }

         if (dy) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            UtilsFX.renderQuadCenteredFromTexture(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            UtilsFX.renderQuadCenteredFromTexture(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
         }

         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glEnable(2884);
         GL11.glEnable(2929);
         GL11.glDepthMask(true);
         GL11.glPopMatrix();
      }
   }
}
