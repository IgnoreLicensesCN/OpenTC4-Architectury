package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.blocks.BlockCosmeticOpaque;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusWarding;
import thaumcraft.common.tiles.TileWarded;

@SideOnly(Side.CLIENT)
public class TileWardedRenderer extends TileEntitySpecialRenderer {
   static HashMap<WorldCoordinates,IIcon> iconCache = new HashMap<>();

   public void renderTileEntityAt(TileWarded tile, double x, double y, double z, float f) {
      EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
      if (viewer instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)viewer;
         if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemWandCasting) {
            ItemWandCasting wand = (ItemWandCasting)player.getCurrentEquippedItem().getItem();
            if (wand.getFocus(player.getCurrentEquippedItem()) != null && wand.getFocus(player.getCurrentEquippedItem()) instanceof ItemFocusWarding) {
               GL11.glPushMatrix();
               GL11.glEnable(GL11.GL_BLEND);
               GL11.glBlendFunc(770, 1);
               GL11.glAlphaFunc(516, 0.003921569F);
               GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
               World world = tile.getWorldObj();
               RenderBlocks renderBlocks = new RenderBlocks();
               GL11.glDisable(2896);
               Tessellator t = Tessellator.instance;
               renderBlocks.setRenderBounds(-0.001F, -0.001F, -0.001F, 1.001F, 1.001F, 1.001F);
               if (tile.owner == viewer.getCommandSenderName().hashCode()) {
                  float r = MathHelper.sin((float)player.ticksExisted / 2.0F + (float)tile.xCoord) * 0.2F + 0.8F;
                  float g = MathHelper.sin((float)player.ticksExisted / 3.0F + (float)tile.yCoord) * 0.2F + 0.7F;
                  float b = MathHelper.sin((float)player.ticksExisted / 4.0F + (float)tile.zCoord) * 0.2F + 0.28F;
                  GL11.glColor4f(r, g, b, 0.5F);
               } else {
                  float r = MathHelper.sin((float)player.ticksExisted / 2.0F + (float)tile.xCoord) * 0.2F + 0.8F;
                  float g = MathHelper.sin((float)player.ticksExisted / 3.0F + (float)tile.yCoord) * 0.2F + 0.28F;
                  float b = MathHelper.sin((float)player.ticksExisted / 4.0F + (float)tile.zCoord) * 0.2F + 0.28F;
                  GL11.glColor4f(r, g, b, 0.25F);
               }

               t.startDrawingQuads();
               t.setBrightness(200);
               this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
               GL11.glTexEnvi(8960, 8704, 260);
               if (this.shouldSideBeRendered(world, tile.xCoord, tile.yCoord, tile.zCoord, 1)) {
                  renderBlocks.renderFaceYNeg(ConfigBlocks.blockJar, -0.5001, 0.0F, -0.5001, this.getIconOnSide(world, tile.xCoord, tile.yCoord, tile.zCoord, 0, tile.owner, player.ticksExisted));
               }

               if (this.shouldSideBeRendered(world, tile.xCoord, tile.yCoord, tile.zCoord, 0)) {
                  renderBlocks.renderFaceYPos(ConfigBlocks.blockJar, -0.5001, 0.0F, -0.5001, this.getIconOnSide(world, tile.xCoord, tile.yCoord, tile.zCoord, 1, tile.owner, player.ticksExisted));
               }

               if (this.shouldSideBeRendered(world, tile.xCoord, tile.yCoord, tile.zCoord, 3)) {
                  renderBlocks.renderFaceZNeg(ConfigBlocks.blockJar, -0.5001, 0.0F, -0.5001, this.getIconOnSide(world, tile.xCoord, tile.yCoord, tile.zCoord, 2, tile.owner, player.ticksExisted));
               }

               if (this.shouldSideBeRendered(world, tile.xCoord, tile.yCoord, tile.zCoord, 2)) {
                  renderBlocks.renderFaceZPos(ConfigBlocks.blockJar, -0.5001, 0.0F, -0.5001, this.getIconOnSide(world, tile.xCoord, tile.yCoord, tile.zCoord, 3, tile.owner, player.ticksExisted));
               }

               if (this.shouldSideBeRendered(world, tile.xCoord, tile.yCoord, tile.zCoord, 5)) {
                  renderBlocks.renderFaceXNeg(ConfigBlocks.blockJar, -0.5001, 0.0F, -0.5001, this.getIconOnSide(world, tile.xCoord, tile.yCoord, tile.zCoord, 4, tile.owner, player.ticksExisted));
               }

               if (this.shouldSideBeRendered(world, tile.xCoord, tile.yCoord, tile.zCoord, 4)) {
                  renderBlocks.renderFaceXPos(ConfigBlocks.blockJar, -0.5001, 0.0F, -0.5001, this.getIconOnSide(world, tile.xCoord, tile.yCoord, tile.zCoord, 5, tile.owner, player.ticksExisted));
               }

               t.draw();
               GL11.glTexEnvi(8960, 8704, 8448);
               GL11.glEnable(2896);
               GL11.glAlphaFunc(516, 0.1F);
               GL11.glDisable(GL11.GL_BLEND);
               GL11.glColor3f(1.0F, 1.0F, 1.0F);
               GL11.glPopMatrix();
            }
         }
      }

   }

   private boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
      if (world.getBlockMetadata(x, y, z) != world.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side])) {
         return true;
      } else {
         return world.getBlock(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]) != ConfigBlocks.blockWarded;
      }
   }

   private boolean isConnectedBlock(World world, int x, int y, int z, int owner) {
      if (world.getBlock(x, y, z) == ConfigBlocks.blockWarded) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileWarded) {
            return ((TileWarded)tile).owner == owner;
         }
      }

      return false;
   }

   private IIcon getIconOnSide(World world, int x, int y, int z, int side, int owner, int ticks) {
      WorldCoordinates wc = new WorldCoordinates(x, y, z, side);
      IIcon out = iconCache.get(wc);
      if ((ticks + side) % 10 == 0 || out == null) {
         boolean[] bitMatrix = new boolean[8];
         if (side == 0 || side == 1) {
            bitMatrix[0] = this.isConnectedBlock(world, x - 1, y, z - 1, owner);
            bitMatrix[1] = this.isConnectedBlock(world, x, y, z - 1, owner);
            bitMatrix[2] = this.isConnectedBlock(world, x + 1, y, z - 1, owner);
            bitMatrix[3] = this.isConnectedBlock(world, x - 1, y, z, owner);
            bitMatrix[4] = this.isConnectedBlock(world, x + 1, y, z, owner);
            bitMatrix[5] = this.isConnectedBlock(world, x - 1, y, z + 1, owner);
            bitMatrix[6] = this.isConnectedBlock(world, x, y, z + 1, owner);
            bitMatrix[7] = this.isConnectedBlock(world, x + 1, y, z + 1, owner);
         }

         if (side == 2 || side == 3) {
            bitMatrix[0] = this.isConnectedBlock(world, x + (side == 2 ? 1 : -1), y + 1, z, owner);
            bitMatrix[1] = this.isConnectedBlock(world, x, y + 1, z, owner);
            bitMatrix[2] = this.isConnectedBlock(world, x + (side == 3 ? 1 : -1), y + 1, z, owner);
            bitMatrix[3] = this.isConnectedBlock(world, x + (side == 2 ? 1 : -1), y, z, owner);
            bitMatrix[4] = this.isConnectedBlock(world, x + (side == 3 ? 1 : -1), y, z, owner);
            bitMatrix[5] = this.isConnectedBlock(world, x + (side == 2 ? 1 : -1), y - 1, z, owner);
            bitMatrix[6] = this.isConnectedBlock(world, x, y - 1, z, owner);
            bitMatrix[7] = this.isConnectedBlock(world, x + (side == 3 ? 1 : -1), y - 1, z, owner);
         }

         if (side == 4 || side == 5) {
            bitMatrix[0] = this.isConnectedBlock(world, x, y + 1, z + (side == 5 ? 1 : -1), owner);
            bitMatrix[1] = this.isConnectedBlock(world, x, y + 1, z, owner);
            bitMatrix[2] = this.isConnectedBlock(world, x, y + 1, z + (side == 4 ? 1 : -1), owner);
            bitMatrix[3] = this.isConnectedBlock(world, x, y, z + (side == 5 ? 1 : -1), owner);
            bitMatrix[4] = this.isConnectedBlock(world, x, y, z + (side == 4 ? 1 : -1), owner);
            bitMatrix[5] = this.isConnectedBlock(world, x, y - 1, z + (side == 5 ? 1 : -1), owner);
            bitMatrix[6] = this.isConnectedBlock(world, x, y - 1, z, owner);
            bitMatrix[7] = this.isConnectedBlock(world, x, y - 1, z + (side == 4 ? 1 : -1), owner);
         }

         int idBuilder = 0;

         for(int i = 0; i <= 7; ++i) {
            idBuilder += bitMatrix[i] ? (i == 0 ? 1 : (i == 1 ? 2 : (i == 2 ? 4 : (i == 3 ? 8 : (i == 4 ? 16 : (i == 5 ? 32 : (i == 6 ? 64 : 128))))))) : 0;
         }

         IIcon var13;
         if (idBuilder <= 255 && idBuilder >= 0) {
            BlockCosmeticOpaque var14 = (BlockCosmeticOpaque)ConfigBlocks.blockCosmeticOpaque;
            var13 = BlockCosmeticOpaque.wardedGlassIcon[UtilsFX.connectedTextureRefByID[idBuilder]];
         } else {
            BlockCosmeticOpaque var10000 = (BlockCosmeticOpaque)ConfigBlocks.blockCosmeticOpaque;
            var13 = BlockCosmeticOpaque.wardedGlassIcon[0];
         }

         out = var13;
         iconCache.put(wc, out);
      }

      return out;
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileWarded)tileEntity, par2, par4, par6, par8);
   }
}
