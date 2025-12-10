package thaumcraft.client.lib;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.IArchitect;
import thaumcraft.api.IGoggles;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.research.ScanResult;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.renderers.tile.TileNodeRenderer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.ItemGolemBell;
import thaumcraft.common.entities.golems.ItemGolemPlacer;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.items.armor.ItemFortressArmor;
import thaumcraft.common.items.armor.ItemVoidRobeArmor;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketNote;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.tiles.TileSensor;
import thaumcraft.common.tiles.TileWandPedestal;
import truetyper.FontLoader;
import truetyper.TrueTypeFont;

import static thaumcraft.client.renderers.tile.TileNodeRenderer.renderFacingStrip_tweaked;

public class RenderEventHandler {
   TrueTypeFont font = null;
   public static List blockTags = new ArrayList<>();
   int q = 0;
   public static float tagscale = 0.0F;
   public long scanCount = 0L;
   public int scanX = 0;
   public int scanY = 0;
   public int scanZ = 0;
   int[][][] scannedBlocks = new int[17][17][17];
   @SideOnly(Side.CLIENT)
   public REHWandHandler wandHandler;
   @SideOnly(Side.CLIENT)
   public REHNotifyHandler notifyHandler;
   public static boolean resetShaders = false;
   private static int oldDisplayWidth = 0;
   private static int oldDisplayHeight = 0;
   public static HashMap<Integer,ShaderGroup> shaderGroups = new HashMap<>();
   public static boolean fogFiddled = false;
   public static float fogTarget = 0.0F;
   public static int fogDuration = 0;
   public static float prevVignetteBrightness = 0.0F;
   public static float targetBrightness = 1.0F;
   protected static final ResourceLocation vignetteTexPath = new ResourceLocation("thaumcraft", "textures/misc/vignette.png");

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderOverlay(RenderGameOverlayEvent event) {
      if (this.font == null) {
         this.font = FontLoader.loadSystemFont("Arial", 12.0F, true);
      }

      Minecraft mc = Minecraft.getMinecraft();
      long time = System.nanoTime() / 1000000L;
      if (this.wandHandler == null) {
         this.wandHandler = new REHWandHandler();
      }

      if (this.notifyHandler == null) {
         this.notifyHandler = new REHNotifyHandler();
      }

      if (event.type == ElementType.TEXT) {
         this.notifyHandler.handleNotifications(mc, time, event);
         this.wandHandler.handleFociRadial(mc, time, event);
      }

      if (event.type == ElementType.PORTAL) {
         this.renderVignette(targetBrightness, event.resolution.getScaledWidth_double(), event.resolution.getScaledHeight_double());
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderShaders(RenderGameOverlayEvent.Pre event) {
      if (Config.shaders && event.type == ElementType.ALL) {
         Minecraft mc = Minecraft.getMinecraft();
         long time = System.nanoTime() / 1000000L;
         if (OpenGlHelper.shadersSupported && !shaderGroups.isEmpty()) {
            this.updateShaderFrameBuffers(mc);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();

            for(ShaderGroup sg : shaderGroups.values()) {
               GL11.glPushMatrix();

               try {
                  sg.loadShaderGroup(event.partialTicks);
               } catch (Exception ignored) {
               }

               GL11.glPopMatrix();
            }

            mc.getFramebuffer().bindFramebuffer(true);
         }
      }

   }

   private void updateShaderFrameBuffers(Minecraft mc) {
      if (resetShaders || mc.displayWidth != oldDisplayWidth || oldDisplayHeight != mc.displayHeight) {
         for(ShaderGroup sg : shaderGroups.values()) {
            sg.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
         }

         oldDisplayWidth = mc.displayWidth;
         oldDisplayHeight = mc.displayHeight;
         resetShaders = false;
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void blockHighlight(DrawBlockHighlightEvent event) {
      int ticks = event.player.ticksExisted;
      MovingObjectPosition target = event.target;
      if (!blockTags.isEmpty()) {
         int x = (Integer)blockTags.get(0);
         int y = (Integer)blockTags.get(1);
         int z = (Integer)blockTags.get(2);
         AspectList ot = (AspectList)blockTags.get(3);
         ForgeDirection dir = ForgeDirection.getOrientation((Integer)blockTags.get(4));
         if (x == target.blockX && y == target.blockY && z == target.blockZ) {
            if (tagscale < 0.5F) {
               tagscale += 0.031F - tagscale / 10.0F;
            }

            this.drawTagsOnContainer((float)target.blockX + (float)dir.offsetX / 2.0F, (float)target.blockY + (float)dir.offsetY / 2.0F, (float)target.blockZ + (float)dir.offsetZ / 2.0F, ot, 220, dir, event.partialTicks);
         }
      }

      if (event.player.inventory.armorItemInSlot(3) != null && event.player.inventory.armorItemInSlot(3).getItem() instanceof IGoggles && ((IGoggles)event.player.inventory.armorItemInSlot(3).getItem()).showIngamePopups(event.player.inventory.armorItemInSlot(3), event.player)) {
         boolean spaceAbove = event.player.worldObj.isAirBlock(target.blockX, target.blockY + 1, target.blockZ);
         TileEntity te = event.player.worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ);
         if (te != null) {
            int note = -1;
            if (te instanceof TileEntityNote) {
               note = ((TileEntityNote)te).note;
            } else if (te instanceof TileSensor) {
               note = ((TileSensor)te).note;
            } else if (te instanceof IAspectContainer && ((IAspectContainer)te).getAspects() != null && ((IAspectContainer)te).getAspects().size() > 0) {
               float shift = 0.0F;
               if (te instanceof TileWandPedestal) {
                  shift = 0.6F;
               }

               if (tagscale < 0.3F) {
                  tagscale += 0.031F - tagscale / 10.0F;
               }

               this.drawTagsOnContainer(target.blockX, (float)target.blockY + (spaceAbove ? 0.4F : 0.0F) + shift, target.blockZ, ((IAspectContainer)te).getAspects(), 220, spaceAbove ? ForgeDirection.UP : ForgeDirection.getOrientation(event.target.sideHit), event.partialTicks);
            }

            if (note >= 0) {
               if (ticks % 5 == 0) {
                  PacketHandler.INSTANCE.sendToServer(new PacketNote(target.blockX, target.blockY, target.blockZ, event.player.worldObj.provider.dimensionId));
               }

               this.drawTextInAir(target.blockX, target.blockY + 1, target.blockZ, event.partialTicks, "Note: " + note);
            }
         }
      }

      if (this.wandHandler == null) {
         this.wandHandler = new REHWandHandler();
      }

      if (target.typeOfHit == MovingObjectType.BLOCK && event.player.getHeldItem() != null && event.player.getHeldItem().getItem() instanceof IArchitect && !(event.player.getHeldItem().getItem() instanceof ItemFocusBasic) && this.wandHandler.handleArchitectOverlay(event.player.getHeldItem(), event, ticks, target)) {
         event.setCanceled(true);
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderLast(RenderWorldLastEvent event) {
      if (tagscale > 0.0F) {
         tagscale -= 0.005F;
      }

      float partialTicks = event.partialTicks;
      Minecraft mc = Minecraft.getMinecraft();
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
         long time = System.currentTimeMillis();
         if (player.inventory.getCurrentItem() != null && (player.inventory.getCurrentItem().getItem() instanceof ItemGolemPlacer || player.inventory.getCurrentItem().getItem() instanceof ItemGolemBell)) {
            this.renderMarkedBlocks(event, partialTicks, player, time);
         }

         if (this.scanCount > time) {
            this.showScannedBlocks(partialTicks, player, time);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void fogDensityEvent(EntityViewRenderEvent.RenderFogEvent event) {
      if (fogFiddled && fogTarget > 0.0F) {
         GL11.glFogi(2917, 2048);
         GL11.glFogf(2914, fogTarget);
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderPlayerSpecialsEvent(RenderPlayerEvent.Specials.Pre event) {
      if (event.entityPlayer != null && event.entityPlayer.inventory.armorInventory[2] != null && (event.entityPlayer.inventory.armorInventory[2].getItem() instanceof ItemFortressArmor || event.entityPlayer.inventory.armorInventory[2].getItem() instanceof ItemVoidRobeArmor)) {
         event.renderCape = false;
      }

   }

   public void drawTagsOnContainer(double x, double y, double z, AspectList tags, int bright, ForgeDirection dir, float partialTicks) {
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer && tags != null && tags.size() > 0) {
         EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
         double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
         double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
         double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
         int e = 0;
         int rowsize = 5;
         int current = 0;
         float shifty = 0.0F;
         int left = tags.size();

         for(Aspect tag : tags.getAspects()) {
            int div = Math.min(left, rowsize);
            if (current >= rowsize) {
               current = 0;
               shifty -= tagscale * 1.05F;
               left -= rowsize;
               if (left < rowsize) {
                  div = left % rowsize;
               }
            }

            float shift = ((float)current - (float)div / 2.0F + 0.5F) * tagscale * 4.0F;
            shift *= tagscale;
            Color color = new Color(tag.getColor());
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            GL11.glTranslated(-iPX + x + (double)0.5F + (double)(tagscale * 2.0F * (float)dir.offsetX), -iPY + y - (double)shifty + (double)0.5F + (double)(tagscale * 2.0F * (float)dir.offsetY), -iPZ + z + (double)0.5F + (double)(tagscale * 2.0F * (float)dir.offsetZ));
            float xd = (float)(iPX - (x + (double)0.5F));
            float zd = (float)(iPZ - (z + (double)0.5F));
            float rotYaw = (float)(Math.atan2(xd, zd) * (double)180.0F / Math.PI);
            GL11.glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(shift, 0.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(tagscale, tagscale, tagscale);
            if (!Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), tag)) {
               UtilsFX.renderQuadCenteredFromTexture("textures/aspects/_unknown.png", 1.0F, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, bright, 771, 0.75F);
               new Color(11184810);
            } else {
               UtilsFX.renderQuadCenteredFromTexture(tag.getImage(), 1.0F, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, bright, 771, 0.75F);
            }

            if (tags.getAmount(tag) >= 0) {
               String am = "" + tags.getAmount(tag);
               GL11.glScalef(0.04F, 0.04F, 0.04F);
               GL11.glTranslated(0.0F, 6.0F, -0.1);
               int sw = Minecraft.getMinecraft().fontRenderer.getStringWidth(am);
               GL11.glEnable(GL11.GL_BLEND);
               Minecraft.getMinecraft().fontRenderer.drawString(am, 14 - sw, 1, 1118481);
               GL11.glTranslated(0.0F, 0.0F, -0.1);
               Minecraft.getMinecraft().fontRenderer.drawString(am, 13 - sw, 0, 16777215);
            }

            GL11.glEnable(2929);
            GL11.glPopMatrix();
            ++current;
         }
      }

   }

   public void drawTextInAir(double x, double y, double z, float partialTicks, String text) {
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
         double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
         double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
         double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
         GL11.glPushMatrix();
         GL11.glTranslated(-iPX + x + (double)0.5F, -iPY + y + (double)0.5F, -iPZ + z + (double)0.5F);
         float xd = (float)(iPX - (x + (double)0.5F));
         float zd = (float)(iPZ - (z + (double)0.5F));
         float rotYaw = (float)(Math.atan2(xd, zd) * (double)180.0F / Math.PI);
         GL11.glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glScalef(0.02F, 0.02F, 0.02F);
         int sw = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
         GL11.glEnable(GL11.GL_BLEND);
         Minecraft.getMinecraft().fontRenderer.drawString(text, 1 - sw / 2, 1, 1118481);
         GL11.glTranslated(0.0F, 0.0F, -0.1);
         Minecraft.getMinecraft().fontRenderer.drawString(text, -sw / 2, 0, 16777215);
         GL11.glPopMatrix();
      }

   }

   public void startScan(Entity player, int x, int y, int z, long time, int range) {
      this.scannedBlocks = new int[17][17][17];
      this.scanX = x;
      this.scanY = y;
      this.scanZ = z;
      this.scanCount = time;

      for(int xx = -range; xx <= range; ++xx) {
         for(int yy = -range; yy <= range; ++yy) {
            for(int zz = -range; zz <= range; ++zz) {
               int value = -1;
               Block bi = player.worldObj.getBlock(x + xx, y + yy, z + zz);
               if (bi != Blocks.air && bi != Blocks.bedrock) {
                  if (bi.getMaterial() == Material.lava) {
                     value = -10;
                  } else if (bi.getMaterial() == Material.water) {
                     value = -5;
                  } else {
                     int md = bi.getDamageValue(player.worldObj, x + xx, y + yy, z + zz);
                     int[] od = OreDictionary.getOreIDs(new ItemStack(bi, 1, md));
                     boolean ore = false;
                     if (od != null) {
                        for(int id : od) {
                           if (OreDictionary.getOreName(id) != null && OreDictionary.getOreName(id).toUpperCase().contains("ORE")) {
                              ore = true;
                              value = 0;
                              break;
                           }
                        }
                     }

                     if (ore) {
                        try {
                           ScanResult scan = new ScanResult((byte)1, Block.getIdFromBlock(bi), md, null, "");
                           value = ScanManager.getScanAspects(scan, player.worldObj).visSize();
                        } catch (Exception var21) {
                           try {
                              ScanResult scan = new ScanResult((byte)1, Item.getIdFromItem(bi.getItem(player.worldObj, x + xx, y + yy, z + zz)), bi.getDamageValue(player.worldObj, x + xx, y + yy, z + zz), null, "");
                              value = ScanManager.getScanAspects(scan, player.worldObj).visSize();
                           } catch (Exception ignored) {
                           }
                        }
                     }
                  }
               }

               this.scannedBlocks[xx + 8][yy + 8][zz + 8] = value;
            }
         }
      }

   }

   public void showScannedBlocks(float partialTicks, EntityPlayer player, long time) {
      Minecraft mc = Minecraft.getMinecraft();
      long dif = this.scanCount - time;
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glDisable(2929);

      for(int xx = -8; xx <= 8; ++xx) {
         for(int yy = -8; yy <= 8; ++yy) {
            for(int zz = -8; zz <= 8; ++zz) {
               int value = this.scannedBlocks[xx + 8][yy + 8][zz + 8];
               float alpha = 1.0F;
               if (dif > 4750L) {
                  alpha = 1.0F - (float)(dif - 4750L) / 5.0F;
               }

               if (dif < 1500L) {
                  alpha = (float)dif / 1500.0F;
               }

               float dist = 1.0F - (float)(xx * xx + yy * yy + zz * zz) / 64.0F;
               alpha *= dist;
               if (value == -5) {
                  this.drawSpecialBlockoverlay(this.scanX + xx, this.scanY + yy, this.scanZ + zz, partialTicks, 3986684, alpha);
               } else if (value == -10) {
                  this.drawSpecialBlockoverlay(this.scanX + xx, this.scanY + yy, this.scanZ + zz, partialTicks, 16734721, alpha);
               } else if (value >= 0) {
                  GL11.glPushMatrix();
                  GL11.glEnable(GL11.GL_BLEND);
                  GL11.glBlendFunc(770, 1);
                  GL11.glAlphaFunc(516, 0.003921569F);
                  GL11.glDisable(2884);
                  UtilsFX.bindTexture(TileNodeRenderer.nodetex);
                  this.drawPickScannedObject(this.scanX + xx, this.scanY + yy, this.scanZ + zz, partialTicks, alpha, (int)(time / 50L % 32L), (float)value / 7.0F);
                  GL11.glAlphaFunc(516, 0.1F);
                  GL11.glDisable(GL11.GL_BLEND);
                  GL11.glEnable(2884);
                  GL11.glPopMatrix();
               }
            }
         }
      }

      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public void drawPickScannedObject(double x, double y, double z, float partialTicks, float alpha, int cframe, float size) {
      GL11.glPushMatrix();
      //            UtilsFX.renderFacingStrip
      renderFacingStrip_tweaked
              (x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, 0.0F, 0.2F * size, alpha, 32, 0, cframe, partialTicks, 11184657);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      //            UtilsFX.renderFacingStrip
      renderFacingStrip_tweaked
              (x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, 0.0F, 0.5F * size, alpha, 32, 0, cframe, partialTicks, 11145506);
      GL11.glPopMatrix();
   }

   public void drawSpecialBlockoverlay(double x, double y, double z, float partialTicks, int color, float alpha) {
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      float time = (float)(System.nanoTime() / 30000000L);
      Color cc = new Color(color);
      r = (float)cc.getRed() / 255.0F;
      g = (float)cc.getGreen() / 255.0F;
      b = (float)cc.getBlue() / 255.0F;

      for(int side = 0; side < 6; ++side) {
         GL11.glPushMatrix();
         ForgeDirection dir = ForgeDirection.getOrientation(side);
         GL11.glTranslated(-iPX + x + (double)0.5F, -iPY + y + (double)0.5F, -iPZ + z + (double)0.5F);
         GL11.glRotatef(90.0F, (float)(-dir.offsetY), (float)dir.offsetX, (float)(-dir.offsetZ));
         if (dir.offsetZ < 0) {
            GL11.glTranslated(0.0F, 0.0F, 0.5F);
         } else {
            GL11.glTranslated(0.0F, 0.0F, -0.5F);
         }

         GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
         UtilsFX.renderQuadCenteredFromTexture("textures/blocks/wardedglass.png", 1.0F, r, g, b, 200, 1, alpha);
         GL11.glPopMatrix();
      }

   }

   @SideOnly(Side.CLIENT)
   public void renderMarkedBlocks(RenderWorldLastEvent event, float partialTicks, EntityPlayer player, long time) {
      Minecraft mc = Minecraft.getMinecraft();
      if (player.inventory.getCurrentItem().hasTagCompound() && player.inventory.getCurrentItem().stackTagCompound.hasKey("markers")) {
         Entity golem = null;
         ChunkCoordinates cc = null;
         int face = -1;
         if (player.inventory.getCurrentItem().getItem() instanceof ItemGolemBell) {
            cc = ItemGolemBell.getGolemHomeCoords(player.inventory.getCurrentItem());
            face = ItemGolemBell.getGolemHomeFace(player.inventory.getCurrentItem());
            int gid = ItemGolemBell.getGolemId(player.inventory.getCurrentItem());
            if (gid > -1) {
               golem = player.worldObj.getEntityByID(gid);
            }

            if (!(golem instanceof EntityGolemBase)) {
               return;
            }
         }

         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         if (golem != null && cc != null && face > -1 && player.getDistanceSq(cc.posX, cc.posY, cc.posZ) < (double)4096.0F) {
            GL11.glPushMatrix();
            this.drawGolemHomeOverlay(cc.posX, cc.posY, cc.posZ, face, partialTicks);
            GL11.glPopMatrix();
         }

         NBTTagList tl = player.inventory.getCurrentItem().stackTagCompound.getTagList("markers", 10);

         for(int q = 0; q < tl.tagCount(); ++q) {
            NBTTagCompound nbttagcompound1 = tl.getCompoundTagAt(q);
            double x = nbttagcompound1.getInteger("x");
            double y = nbttagcompound1.getInteger("y");
            double z = nbttagcompound1.getInteger("z");
            int ox = nbttagcompound1.getInteger("x");
            int oy = nbttagcompound1.getInteger("y");
            int oz = nbttagcompound1.getInteger("z");
            int dim = nbttagcompound1.getInteger("dim");
            byte s = nbttagcompound1.getByte("side");
            byte c = nbttagcompound1.getByte("color");
            x += ForgeDirection.getOrientation(s).offsetX;
            y += ForgeDirection.getOrientation(s).offsetY;
            z += ForgeDirection.getOrientation(s).offsetZ;
            if (dim == player.worldObj.provider.dimensionId && player.getDistanceSq(x, y, z) < (double)4096.0F) {
               GL11.glPushMatrix();
               this.drawMarkerOverlay(x, y, z, s, partialTicks, c);
               GL11.glPopMatrix();
               if (player.worldObj.isAirBlock(ox, oy, oz)) {
                  GL11.glPushMatrix();

                  for(int a = 0; a < 6; ++a) {
                     this.drawAirBlockoverlay(ox + ForgeDirection.getOrientation(a).offsetX, oy + ForgeDirection.getOrientation(a).offsetY, oz + ForgeDirection.getOrientation(a).offsetZ, a, partialTicks, c);
                  }

                  GL11.glPopMatrix();
               }

               if (golem != null && Config.golemLinkQuality > 3) {
                  x -= (double)ForgeDirection.getOrientation(s).offsetX * (double)0.5F;
                  y -= (double)ForgeDirection.getOrientation(s).offsetY * (double)0.5F;
                  z -= (double)ForgeDirection.getOrientation(s).offsetZ * (double)0.5F;
                  GL11.glPushMatrix();
                  this.drawMarkerLine(x, y, z, s, partialTicks, c, golem);
                  GL11.glPopMatrix();
               }
            }
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      }

   }

   public void drawAirBlockoverlay(double x, double y, double z, int side, float partialTicks, int color) {
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      float time = (float)(System.nanoTime() / 30000000L);
      if (color == -1) {
         r = MathHelper.sin(time % 32767.0F / 12.0F + (float)side) * 0.2F + 0.8F;
         g = MathHelper.sin(time % 32767.0F / 14.0F + (float)side) * 0.2F + 0.8F;
         b = MathHelper.sin(time % 32767.0F / 16.0F + (float)side) * 0.2F + 0.8F;
      } else {
         Color cc = new Color(UtilsFX.colors[color]);
         r = (float)cc.getRed() / 255.0F;
         g = (float)cc.getGreen() / 255.0F;
         b = (float)cc.getBlue() / 255.0F;
      }

      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glDisable(2884);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      ForgeDirection dir = ForgeDirection.getOrientation(side);
      GL11.glTranslated(-iPX + x + (double)0.5F - (double)((float)dir.offsetX * 0.01F), -iPY + y + (double)0.5F - (double)((float)dir.offsetY * 0.01F), -iPZ + z + (double)0.5F - (double)((float)dir.offsetZ * 0.01F));
      GL11.glRotatef(90.0F, (float)(-dir.offsetY), (float)dir.offsetX, (float)(-dir.offsetZ));
      GL11.glPushMatrix();
      if (dir.offsetZ < 0) {
         GL11.glTranslated(0.0F, 0.0F, 0.5F);
      } else {
         GL11.glTranslated(0.0F, 0.0F, -0.5F);
      }

      GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glScalef(0.98F, 0.98F, 0.98F);
      UtilsFX.renderQuadCenteredFromTexture("textures/blocks/empty.png", 1.0F, r, g, b, 200, 1, 1.0F);
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(2884);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   public void drawMarkerOverlay(double x, double y, double z, int side, float partialTicks, int color) {
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      float time = (float)(System.nanoTime() / 30000000L);
      if (color == -1) {
         r = MathHelper.sin(time % 32767.0F / 12.0F + (float)side) * 0.2F + 0.8F;
         g = MathHelper.sin(time % 32767.0F / 14.0F + (float)side) * 0.2F + 0.8F;
         b = MathHelper.sin(time % 32767.0F / 16.0F + (float)side) * 0.2F + 0.8F;
      } else {
         Color cc = new Color(UtilsFX.colors[color]);
         r = (float)cc.getRed() / 255.0F;
         g = (float)cc.getGreen() / 255.0F;
         b = (float)cc.getBlue() / 255.0F;
      }

      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glDisable(2884);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      ForgeDirection dir = ForgeDirection.getOrientation(side);
      GL11.glTranslated(-iPX + x + (double)0.5F + (double)((float)dir.offsetX * 0.01F), -iPY + y + (double)0.5F + (double)((float)dir.offsetY * 0.01F), -iPZ + z + (double)0.5F + (double)((float)dir.offsetZ * 0.01F));
      GL11.glRotatef(90.0F, (float)(-dir.offsetY), (float)dir.offsetX, (float)(-dir.offsetZ));
      GL11.glPushMatrix();
      if (dir.offsetZ < 0) {
         GL11.glTranslated(0.0F, 0.0F, 0.5F);
      } else {
         GL11.glTranslated(0.0F, 0.0F, -0.5F);
      }

      GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glScalef(0.4F, 0.4F, 0.4F);
      UtilsFX.renderQuadCenteredFromTexture("textures/misc/mark.png", 1.0F, r, g, b, 200, 1, 1.0F);
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(2884);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   public void drawGolemHomeOverlay(double x, double y, double z, int side, float partialTicks) {
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      float time = (float)(System.nanoTime() / 30000000L);
      r = MathHelper.sin(time % 32767.0F / 12.0F + (float)side) * 0.2F + 0.8F;
      g = MathHelper.sin(time % 32767.0F / 14.0F + (float)side) * 0.2F + 0.8F;
      b = MathHelper.sin(time % 32767.0F / 16.0F + (float)side) * 0.2F + 0.8F;
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glDisable(2884);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      ForgeDirection dir = ForgeDirection.getOrientation(side);
      GL11.glTranslated(-iPX + x + (double)0.5F + (double)((float)dir.offsetX * 0.01F), -iPY + y + (double)0.5F + (double)((float)dir.offsetY * 0.01F), -iPZ + z + (double)0.5F + (double)((float)dir.offsetZ * 0.01F));
      GL11.glRotatef(90.0F, (float)(-dir.offsetY), (float)dir.offsetX, (float)(-dir.offsetZ));
      GL11.glPushMatrix();
      if (dir.offsetZ < 0) {
         GL11.glTranslated(0.0F, 0.0F, 0.5F);
      } else {
         GL11.glTranslated(0.0F, 0.0F, -0.5F);
      }

      GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glScalef(0.65F, 0.65F, 0.65F);
      UtilsFX.renderQuadCenteredFromTexture("textures/misc/home.png", 1.0F, r, g, b, 200, 1, 1.0F);
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(2884);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   public void drawMarkerLine(double x, double y, double z, int side, float partialTicks, int color, Entity cc) {
      EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      double ePX = cc.prevPosX + (cc.posX - cc.prevPosX) * (double)partialTicks;
      double ePY = cc.prevPosY + (cc.posY - cc.prevPosY) * (double)partialTicks;
      double ePZ = cc.prevPosZ + (cc.posZ - cc.prevPosZ) * (double)partialTicks;
      GL11.glTranslated(-iPX + ePX, -iPY + ePY + (double)cc.height, -iPZ + ePZ);
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      float time = (float)(System.nanoTime() / 30000000L);
      if (color > -1) {
         Color co = new Color(UtilsFX.colors[color]);
         r = (float)co.getRed() / 255.0F;
         g = (float)co.getGreen() / 255.0F;
         b = (float)co.getBlue() / 255.0F;
      }

      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      Tessellator tessellator = Tessellator.instance;
      double ds1y = ePY + (double)cc.height;
      double dd1x = x + (double)0.5F + (double)ForgeDirection.getOrientation(side).offsetX * (double)0.5F;
      double dd1y = y + (double)0.5F + (double)ForgeDirection.getOrientation(side).offsetY * (double)0.5F;
      double dd1z = z + (double)0.5F + (double)ForgeDirection.getOrientation(side).offsetZ * (double)0.5F;
      double dc1x = (float)(dd1x - ePX);
      double dc1y = (float)(dd1y - ds1y);
      double dc1z = (float)(dd1z - ePZ);
      double ds2x = x + (double)0.5F;
      double ds2y = y + (double)0.5F;
      double ds2z = z + (double)0.5F;
      double dc22x = (float)(ds2x - ePX);
      double dc22y = (float)(ds2y - ds1y);
      double dc22z = (float)(ds2z - ePZ);
      UtilsFX.bindTexture("textures/misc/script.png");
      GL11.glDisable(2884);
      tessellator.startDrawing(5);
      float f4 = 0.0F;
      double dx2 = 0.0F;
      double dy2 = 0.0F;
      double dz2 = 0.0F;
      double d3 = x - ePX;
      double d4 = y - ePY;
      double d5 = z - ePZ;
      float dist = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
      float blocks = (float)Math.round(dist);
      float length = blocks * (float)Config.golemLinkQuality;
      float f9 = 0.0F;
      float f10 = 1.0F;
      int count = 0;

      for(int i = 0; (float)i <= length; ++i) {
         float f2 = (float)i / length;
         float f2a = (float)i * 1.5F / length;
         f2a = Math.min(0.75F, f2a);
         float f3 = 1.0F - Math.abs((float)i - length / 2.0F) / (length / 2.0F);
         f4 = 0.0F;
         if (color == -1) {
            r = MathHelper.sin(time % 32767.0F / 12.0F + (float)side + (float)i) * 0.2F + 0.8F;
            g = MathHelper.sin(time % 32767.0F / 14.0F + (float)side + (float)i) * 0.2F + 0.8F;
            b = MathHelper.sin(time % 32767.0F / 16.0F + (float)side + (float)i) * 0.2F + 0.8F;
         }

         double dx = dc1x + (double)(MathHelper.sin((float)(((double)(side * 20) + z % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality) - (double)(time % 32767.0F / 5.0F)) / (double)4.0F)) * 0.5F * f3);
         double dy = dc1y + (double)(MathHelper.sin((float)(((double)(side * 20) + x % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality) - (double)(time % 32767.0F / 5.0F)) / (double)3.0F)) * 0.5F * f3);
         double dz = dc1z + (double)(MathHelper.sin((float)(((double)(side * 20) + y % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality) - (double)(time % 32767.0F / 5.0F)) / (double)2.0F)) * 0.5F * f3);
         if ((float)i > length - (float)(Config.golemLinkQuality / 2)) {
            dx2 = dc22x + (double)(MathHelper.sin((float)(((double)(side * 20) + z % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality) - (double)(time % 32767.0F / 5.0F)) / (double)4.0F)) * 0.5F * f3);
            dy2 = dc22y + (double)(MathHelper.sin((float)(((double)(side * 20) + x % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality) - (double)(time % 32767.0F / 5.0F)) / (double)3.0F)) * 0.5F * f3);
            dz2 = dc22z + (double)(MathHelper.sin((float)(((double)(side * 20) + y % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality) - (double)(time % 32767.0F / 5.0F)) / (double)2.0F)) * 0.5F * f3);
            f3 = (length - (float)i) / ((float)Config.golemLinkQuality / 2.0F);
            f4 = 1.0F - f3;
            dx = dx * (double)f3 + dx2 * (double)f4;
            dy = dy * (double)f3 + dy2 * (double)f4;
            dz = dz * (double)f3 + dz2 * (double)f4;
         }

         tessellator.setColorRGBA_F(r, g, b, f2a * (1.0F - f4));
         float f13 = (1.0F - f2) * dist - time * 0.005F;
         tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 - 0.05, dz * (double)f2, f13, f10);
         tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 + 0.05, dz * (double)f2, f13, f9);
      }

      tessellator.draw();
      GL11.glEnable(2884);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
   }

   protected void renderVignette(float brightness, double sw, double sh) {
      int k = (int)sw;
      int l = (int)sh;
      brightness = 1.0F - brightness;
      prevVignetteBrightness = (float)((double)prevVignetteBrightness + (double)(brightness - prevVignetteBrightness) * 0.01);
      if (prevVignetteBrightness > 0.0F) {
         float b = prevVignetteBrightness * (1.0F + MathHelper.sin((float)Minecraft.getMinecraft().thePlayer.ticksExisted / 2.0F) * 0.1F);
         GL11.glPushMatrix();
         GL11.glClear(256);
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         GL11.glOrtho(0.0F, sw, sh, 0.0F, 1000.0F, 3000.0F);
         Minecraft.getMinecraft().getTextureManager().bindTexture(vignetteTexPath);
         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
         GL11.glDisable(2929);
         GL11.glDepthMask(false);
         OpenGlHelper.glBlendFunc(0, 769, 1, 0);
         GL11.glColor4f(b, b, b, 1.0F);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV(0.0F, l, -90.0F, 0.0F, 1.0F);
         tessellator.addVertexWithUV(k, l, -90.0F, 1.0F, 1.0F);
         tessellator.addVertexWithUV(k, 0.0F, -90.0F, 1.0F, 0.0F);
         tessellator.addVertexWithUV(0.0F, 0.0F, -90.0F, 0.0F, 0.0F);
         tessellator.draw();
         GL11.glDepthMask(true);
         GL11.glEnable(2929);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         GL11.glPopMatrix();
      }

   }

   @SubscribeEvent
   public void livingTick(LivingEvent.LivingUpdateEvent event) {
      if (event.entity.worldObj.isRemote && event.entity instanceof EntityMob && !event.entity.isDead) {
         EntityMob mob = (EntityMob)event.entity;
         int t = (int)mob.getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue();
         if (t >= 0) {
            ChampionModifier.mods[t].effect.showFX(mob);
         }
      }

   }
}
