package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileArcaneWorkbench;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GuiArcaneWorkbench extends GuiContainer {
    private TileArcaneWorkbench tileEntity;
    private InventoryPlayer ip;
    private int[][] aspectLocs = new int[][]{{72, 21}, {24, 43}, {24, 102}, {72, 124}, {120, 102}, {120, 43}};
    ArrayList<Aspect> primals = Aspect.getPrimalAspects();

    public GuiArcaneWorkbench(InventoryPlayer par1InventoryPlayer, TileArcaneWorkbench e) {
        super(new ContainerArcaneWorkbench(par1InventoryPlayer, e));
        this.tileEntity = e;
        this.ip = par1InventoryPlayer;
        this.ySize = 234;
        this.xSize = 190;
    }

    protected void drawGuiContainerForegroundLayer() {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        UtilsFX.bindTexture("textures/gui/gui_arcaneworkbench.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        GL11.glDisable(GL11.GL_BLEND);
        ItemWandCasting wand = null;
        if (this.tileEntity.getStackInSlot(10) != null && this.tileEntity.getStackInSlot(10).getItem() instanceof ItemWandCasting) {
            wand = (ItemWandCasting) this.tileEntity.getStackInSlot(10).getItem();
        }

        AspectList cost = null;
        if (ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.tileEntity, this.ip.player) != null) {
            cost = ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(this.tileEntity, this.ip.player);
            int count = 0;

            for (Aspect primal : this.primals) {
                float amt = (float) cost.getAmount(primal);
                if (cost.getAmount(primal) > 0) {
                    float alpha = 0.5F + (MathHelper.sin((float) (this.ip.player.ticksExisted + count * 10) / 2.0F) * 0.2F - 0.2F);
                    if (wand != null) {
                        amt *= wand.getConsumptionModifier(this.tileEntity.getStackInSlot(10), this.ip.player, primal, true);
                        if (amt * 100.0F <= (float) wand.getVis(this.tileEntity.getStackInSlot(10), primal)) {
                            alpha = 1.0F;
                        }
                    }

                    UtilsFX.drawTag(var5 + this.aspectLocs[count][0] - 8, var6 + this.aspectLocs[count][1] - 8, primal, amt, 0, this.zLevel, 771, alpha, false);
                }

                ++count;
                if (count > 5) {
                    break;
                }
            }
        }

        if (wand != null && cost != null && !wand.consumeAllVisCrafting(this.tileEntity.getStackInSlot(10), this.ip.player, cost, false)) {
            GL11.glPushMatrix();
            float var40 = 0.33F;
            GL11.glColor4f(var40, var40, var40, 0.66F);
            itemRender.renderWithColor = false;
            GL11.glEnable(2896);
            GL11.glEnable(2884);
            GL11.glEnable(GL11.GL_BLEND);
            itemRender.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine,
                    ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.tileEntity, this.ip.player),
                    var5 + 160, var6 + 64);
            itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine,
                    ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.tileEntity, this.ip.player),
                    var5 + 160, var6 + 64);
            itemRender.renderWithColor = true;
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(2896);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef((float) (var5 + 168), (float) (var6 + 46), 0.0F);
            GL11.glScalef(0.5F, 0.5F, 0.0F);
            String text = "Insufficient vis";
            int ll = this.fontRendererObj.getStringWidth(text) / 2;
            this.fontRendererObj.drawString(text, -ll, 0, 15625838);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }

    }
}
