package thaumcraft.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tc4tweak.ClientUtils;
import tc4tweak.ConfigurationHandler;
import tc4tweak.modules.researchBrowser.BrowserPaging;
import tc4tweak.modules.researchBrowser.DrawResearchBrowserBorders;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketPlayerCompleteToServer;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.InventoryUtils;

import static tc4tweak.modules.researchBrowser.DrawResearchBrowserBorders.BORDER_HEIGHT;
import static tc4tweak.modules.researchBrowser.DrawResearchBrowserBorders.BORDER_WIDTH;

@SideOnly(Side.CLIENT)
public class GuiResearchBrowser extends GuiScreen {
    private static int guiMapTop;
    private static int guiMapLeft;
    private static int guiMapBottom;
    private static int guiMapRight;
    protected int paneWidth = getResearchBrowserWidth();//256
    protected int paneHeight = getResearchBrowserHeight();//230
    protected int mouseX = 0;
    protected int mouseY = 0;
    protected double field_74117_m;
    protected double field_74115_n;
    protected double guiMapX;
    protected double guiMapY;
    protected double field_74124_q;
    protected double field_74123_r;
    private int isMouseButtonDown = 0;
    public static int lastX = -5;
    public static int lastY = -6;
    private GuiButton button;
    private final LinkedList<ResearchItem> research = new LinkedList<>();
    public static HashMap<String, ArrayList<String>> completedResearch = new HashMap<>();
    public static ArrayList<String> highlightedItem = new ArrayList<>();
    private static String selectedCategory = null;
    private final FontRenderer galFontRenderer;
    private ResearchItem currentHighlight = null;
    private String player = "";
    long popuptime = 0L;
    String popupmessage = "";
    public boolean hasScribestuff = false;

    public GuiResearchBrowser() {
        short var2 = 141;
        short var3 = 141;
        this.field_74117_m = this.guiMapX = this.field_74124_q = lastX * 24 - var2 / 2. - 12;
        this.field_74115_n = this.guiMapY = this.field_74123_r = lastY * 24 - var3 / 2.;
        this.updateResearch();
        this.galFontRenderer = FMLClientHandler.instance().getClient().standardGalacticFontRenderer;
        this.player = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
    }

    public GuiResearchBrowser(double x, double y) {
        this.field_74117_m = this.guiMapX = this.field_74124_q = x;
        this.field_74115_n = this.guiMapY = this.field_74123_r = y;
        this.updateResearch();
        this.galFontRenderer = FMLClientHandler.instance().getClient().standardGalacticFontRenderer;
        this.player = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
    }

    public void updateResearch() {
        if (this.mc == null) {
            this.mc = Minecraft.getMinecraft();
        }

        this.research.clear();
        this.hasScribestuff = false;
        if (selectedCategory == null) {
//         Collection<String> cats = ResearchCategories.researchCategories.keySet();
            Collection<String> cats = getTabsOnCurrentPage(this.player).keySet();
            selectedCategory = cats.iterator().next();
        }

        this.research.addAll(ResearchCategories.getResearchList(selectedCategory).research.values());

        if (ResearchManager.consumeInkFromPlayer(this.mc.thePlayer, false) && InventoryUtils.isPlayerCarrying(this.mc.thePlayer, new ItemStack(Items.paper)) >= 0) {
            this.hasScribestuff = true;
        }

        guiMapTop = getNewGuiMapTop(ResearchCategories.getResearchList(selectedCategory).minDisplayColumn * 24 - 85);
        guiMapLeft = getNewGuiMapLeft(ResearchCategories.getResearchList(selectedCategory).minDisplayRow * 24 - 112);
        guiMapBottom = getNewGuiMapBottom(ResearchCategories.getResearchList(selectedCategory).maxDisplayColumn * 24 - 112);
        guiMapRight = getNewGuiMapRight(ResearchCategories.getResearchList(selectedCategory).maxDisplayRow * 24 - 61);
    }

    public void onGuiClosed() {
        short var2 = 141;
        short var3 = 141;
        lastX = (int) ((this.guiMapX + (double) (var2 / 2) + (double) 12.0F) / (double) 24.0F);
        lastY = (int) ((this.guiMapY + (double) (var3 / 2)) / (double) 24.0F);
        super.onGuiClosed();
    }

    public void initGui() {
        super.initGui();
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
    }

    protected void keyTyped(char par1, int par2) {
        if (par2 == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            highlightedItem.clear();
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        } else {
            if (par2 == 1) {
                highlightedItem.clear();
            }

            super.keyTyped(par1, par2);
        }

    }

    public void drawScreen(int mx, int my, float par3) {
        int var4 = (this.width - getResearchBrowserWidth()) / 2;
        int var5 = (this.height - getResearchBrowserHeight()) / 2;
        if (Mouse.isButtonDown(0)) {
            int var6 = var4 + 8;
            int var7 = var5 + 17;
            if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && mx >= var6
                    && mx < var6 + (getResearchBrowserWidth() - 2 * BORDER_WIDTH)//224
                    && my >= var7 && my < var7 + (getResearchBrowserHeight() - 2 * BORDER_HEIGHT)//196
            ) {
                if (this.isMouseButtonDown == 0) {
                    this.isMouseButtonDown = 1;
                } else {
                    this.guiMapX -= mx - this.mouseX;
                    this.guiMapY -= my - this.mouseY;
                    this.field_74124_q = this.field_74117_m = this.guiMapX;
                    this.field_74123_r = this.field_74115_n = this.guiMapY;
                }

                this.mouseX = mx;
                this.mouseY = my;
            }

            if (this.field_74124_q < (double) guiMapTop) {
                this.field_74124_q = guiMapTop;
            }

            if (this.field_74123_r < (double) guiMapLeft) {
                this.field_74123_r = guiMapLeft;
            }

            if (this.field_74124_q >= (double) guiMapBottom) {
                this.field_74124_q = guiMapBottom - 1;
            }

            if (this.field_74123_r >= (double) guiMapRight) {
                this.field_74123_r = guiMapRight - 1;
            }
        } else {
            this.isMouseButtonDown = 0;
        }

        this.drawDefaultBackground();
        this.genResearchBackground(mx, my, par3);
        if (this.popuptime > System.currentTimeMillis()) {
            int xq = var4 + 128;
            int yq = var5 + 128;
            int var41 = this.fontRendererObj.splitStringWidth(this.popupmessage, 150) / 2;
            this.drawGradientRect(xq - 78, yq - var41 - 3, xq + 78, yq + var41 + 3, -1073741824, -1073741824);
            this.fontRendererObj.drawSplitString(this.popupmessage, xq - 75, yq - var41, 150, -7302913);
        }

        Collection<String> cats = getTabsOnCurrentPage(this.player).keySet();//ResearchCategories.researchCategories.keySet();
        int count = 0;
        boolean swop = false;

        for (String obj : cats) {
            if (count == getTabPerSide()//9
            ) {
                count = 0;
                swop = true;
            }

            ResearchCategoryList rcl = ResearchCategories.getResearchList(obj);
            if (!obj.equals("ELDRITCH") || ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR")) {
                int mposx = mx - (var4 - 24 + (swop ? getTabIconDistance()//280
                        : 0));
                int mposy = my - (var5 + count * 24);
                if (mposx >= 0 && mposx < 24 && mposy >= 0 && mposy < 24) {
                    this.fontRendererObj.drawStringWithShadow(ResearchCategories.getCategoryName(obj), mx, my - 8, 16777215);
                }

                ++count;
            }
        }

    }

    public void updateScreen() {
        this.field_74117_m = this.guiMapX;
        this.field_74115_n = this.guiMapY;
        double var1 = this.field_74124_q - this.guiMapX;
        double var3 = this.field_74123_r - this.guiMapY;
        if (var1 * var1 + var3 * var3 < (double) 4.0F) {
            this.guiMapX += var1;
            this.guiMapY += var3;
        } else {
            this.guiMapX += var1 * 0.85;
            this.guiMapY += var3 * 0.85;
        }

    }

    protected void genResearchBackground(int par1, int par2, float par3) {
        long t = System.nanoTime() / 50000000L;
        int var4 = MathHelper.floor_double(this.field_74117_m + (this.guiMapX - this.field_74117_m) * (double) par3);
        int var5 = MathHelper.floor_double(this.field_74115_n + (this.guiMapY - this.field_74115_n) * (double) par3);
        if (var4 < guiMapTop) {
            var4 = guiMapTop;
        }

        if (var5 < guiMapLeft) {
            var5 = guiMapLeft;
        }

        if (var4 >= guiMapBottom) {
            var4 = guiMapBottom - 1;
        }

        if (var5 >= guiMapRight) {
            var5 = guiMapRight - 1;
        }

        int var8 = (this.width - getResearchBrowserWidth()) / 2;
        int var9 = (this.height - getResearchBrowserHeight()) / 2;
        int var10 = var8 + 16;
        int var11 = var9 + 17;
        this.zLevel = 0.0F;
        GL11.glDepthFunc(518);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(3553);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 1.0F);
        int vx = (int) ((float) (var4 - guiMapTop) / (float) Math.abs(guiMapTop - guiMapBottom) * 288.0F);
        int vy = (int) ((float) (var5 - guiMapLeft) / (float) Math.abs(guiMapLeft - guiMapRight) * 316.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ResearchCategories.getResearchList(selectedCategory).background);
//      this.drawTexturedModalRect
        drawResearchBrowserBackground
                (
                        this, var10 / 2, var11 / 2, vx / 2,
                        vy / 2, 112, 98);

        GL11.glScalef(0.5F, 0.5F, 1.0F);
        GL11.glPopMatrix();
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        if (completedResearch.get(this.player) != null) {
            for (ResearchItem o : this.research) {
                ResearchItem var33 = o;
                if (var33.parents != null && var33.parents.length > 0) {
                    for (int a = 0; a < var33.parents.length; ++a) {
                        if (var33.parents[a] != null && ResearchCategories.getResearch(var33.parents[a]).category.equals(selectedCategory)) {
                            ResearchItem parent = ResearchCategories.getResearch(var33.parents[a]);
                            if (!parent.isVirtual()) {
                                int var24 = var33.displayColumn * 24 - var4 + 11 + var10;
                                int var25 = var33.displayRow * 24 - var5 + 11 + var11;
                                int var26 = parent.displayColumn * 24 - var4 + 11 + var10;
                                int var27 = parent.displayRow * 24 - var5 + 11 + var11;
                                boolean var28 = completedResearch.get(this.player).contains(var33.key);
                                boolean var29 = completedResearch.get(this.player).contains(parent.key);
                                int var30 = Math.sin((double) (Minecraft.getSystemTime() % 600L) / (double) 600.0F * Math.PI * (double) 2.0F) > 0.6 ? 255 : 130;
                                if (var28) {
                                    this.drawLine(var24, var25, var26, var27, 0.1F, 0.1F, 0.1F, par3, false);
                                } else if (!var33.isLost() && (!var33.isHidden() && !var33.isLost() || completedResearch.get(this.player).contains("@" + var33.key)) && (!var33.isConcealed() || this.canUnlockResearch(var33))) {
                                    if (var29) {
                                        this.drawLine(var24, var25, var26, var27, 0.0F, 1.0F, 0.0F, par3, true);
                                    } else if ((!parent.isHidden() && !var33.isLost() || completedResearch.get(this.player).contains("@" + parent.key)) && (!parent.isConcealed() || this.canUnlockResearch(parent))) {
                                        this.drawLine(var24, var25, var26, var27, 0.0F, 0.0F, 1.0F, par3, true);
                                    }
                                }
                            }
                        }
                    }
                }

                if (var33.siblings != null && var33.siblings.length > 0) {
                    for (int a = 0; a < var33.siblings.length; ++a) {
                        if (var33.siblings[a] != null && ResearchCategories.getResearch(var33.siblings[a]).category.equals(selectedCategory)) {
                            ResearchItem sibling = ResearchCategories.getResearch(var33.siblings[a]);
                            if (!sibling.isVirtual() && (sibling.parents == null || sibling.parents != null && !Arrays.asList(sibling.parents).contains(var33.key))) {
                                int var24 = var33.displayColumn * 24 - var4 + 11 + var10;
                                int var25 = var33.displayRow * 24 - var5 + 11 + var11;
                                int var26 = sibling.displayColumn * 24 - var4 + 11 + var10;
                                int var27 = sibling.displayRow * 24 - var5 + 11 + var11;
                                boolean var28 = completedResearch.get(this.player).contains(var33.key);
                                boolean var29 = completedResearch.get(this.player).contains(sibling.key);
                                if (var28) {
                                    this.drawLine(var24, var25, var26, var27, 0.1F, 0.1F, 0.2F, par3, false);
                                } else if (!var33.isLost() && (!var33.isHidden() || completedResearch.get(this.player).contains("@" + var33.key)) && (!var33.isConcealed() || this.canUnlockResearch(var33))) {
                                    if (var29) {
                                        this.drawLine(var24, var25, var26, var27, 0.0F, 1.0F, 0.0F, par3, true);
                                    } else if ((!sibling.isHidden() || completedResearch.get(this.player).contains("@" + sibling.key)) && (!sibling.isConcealed() || this.canUnlockResearch(sibling))) {
                                        this.drawLine(var24, var25, var26, var27, 0.0F, 0.0F, 1.0F, par3, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        this.currentHighlight = null;
        RenderItem itemRenderer = new RenderItem();
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        if (completedResearch.get(this.player) != null) {
            for (ResearchItem o : this.research) {
                ResearchItem var35 = o;
                int var26 = var35.displayColumn * 24 - var4;
                int var27 = var35.displayRow * 24 - var5;
                if (!var35.isVirtual() && var26 >= -24 && var27 >= -24
                        && var26 <= getResearchBrowserWidth() - 2 * BORDER_WIDTH//224
                        && var27 <= getResearchBrowserHeight() - 2 * BORDER_HEIGHT//196
                ) {
                    int var42 = var10 + var26;
                    int var41 = var11 + var27;
                    if (completedResearch.get(this.player).contains(var35.key)) {
                        if (ThaumcraftApi.getWarp(var35.key) > 0) {
                            this.drawForbidden(var42 + 11, var41 + 11);
                        }

                        float var38 = 1.0F;
                        GL11.glColor4f(var38, var38, var38, 1.0F);
                    } else {
                        if (!completedResearch.get(this.player).contains("@" + var35.key) && (var35.isLost() || var35.isHidden() && !completedResearch.get(this.player).contains("@" + var35.key) || var35.isConcealed() && !this.canUnlockResearch(var35))) {
                            continue;
                        }

                        if (ThaumcraftApi.getWarp(var35.key) > 0) {
                            this.drawForbidden(var42 + 11, var41 + 11);
                        }

                        if (this.canUnlockResearch(var35)) {
                            float var38 = (float) Math.sin((double) (Minecraft.getSystemTime() % 600L) / (double) 600.0F * Math.PI * (double) 2.0F) * 0.25F + 0.75F;
                            GL11.glColor4f(var38, var38, var38, 1.0F);
                        } else {
                            float var38 = 0.3F;
                            GL11.glColor4f(var38, var38, var38, 1.0F);
                        }
                    }

                    UtilsFX.bindTexture("textures/gui/gui_research.png");
                    GL11.glEnable(2884);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(770, 771);
                    if (var35.isRound()) {
                        this.drawTexturedModalRect(var42 - 2, var41 - 2, 54, 230, 26, 26);
                    } else if (var35.isHidden()) {
                        if (Config.researchDifficulty != -1 && (Config.researchDifficulty != 0 || !var35.isSecondary())) {
                            this.drawTexturedModalRect(var42 - 2, var41 - 2, 86, 230, 26, 26);
                        } else {
                            this.drawTexturedModalRect(var42 - 2, var41 - 2, 230, 230, 26, 26);
                        }
                    } else if (Config.researchDifficulty != -1 && (Config.researchDifficulty != 0 || !var35.isSecondary())) {
                        this.drawTexturedModalRect(var42 - 2, var41 - 2, 0, 230, 26, 26);
                    } else {
                        this.drawTexturedModalRect(var42 - 2, var41 - 2, 110, 230, 26, 26);
                    }

                    if (var35.isSpecial()) {
                        this.drawTexturedModalRect(var42 - 2, var41 - 2, 26, 230, 26, 26);
                    }

                    if (!this.canUnlockResearch(var35)) {
                        float var40 = 0.1F;
                        GL11.glColor4f(var40, var40, var40, 1.0F);
                        itemRenderer.renderWithColor = false;
                    }

                    GL11.glDisable(GL11.GL_BLEND);
                    if (highlightedItem.contains(var35.key)) {
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(770, 771);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        this.mc.renderEngine.bindTexture(ParticleEngine.particleTexture);
                        int px = (int) (t % 16L) * 16;
                        GL11.glTranslatef((float) (var42 - 5), (float) (var41 - 5), 0.0F);
                        UtilsFX.drawTexturedQuad(0, 0, px, 80, 16, 16, 0.0F);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glPopMatrix();
                    }

                    if (var35.icon_item != null) {
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(770, 771);
                        RenderHelper.enableGUIStandardItemLighting();
                        GL11.glDisable(2896);
                        GL11.glEnable(32826);
                        GL11.glEnable(2903);
                        GL11.glEnable(2896);
                        itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, InventoryUtils.cycleItemStack(var35.icon_item), var42 + 3, var41 + 3);
                        GL11.glDisable(2896);
                        GL11.glDepthMask(true);
                        GL11.glEnable(2929);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glPopMatrix();
                    } else if (var35.icon_resource != null) {
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(770, 771);
                        this.mc.renderEngine.bindTexture(var35.icon_resource);
                        if (!itemRenderer.renderWithColor) {
                            GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
                        }

                        UtilsFX.drawTexturedQuadFull(var42 + 3, var41 + 3, this.zLevel);
                        GL11.glPopMatrix();
                    }

                    if (!this.canUnlockResearch(var35)) {
                        itemRenderer.renderWithColor = true;
                    }

                    if (par1 >= var10 && par2 >= var11
                            && par1 < var10 + getResearchBrowserWidth() - 2 * BORDER_WIDTH//224
                            && par2 < var11 + getResearchBrowserHeight() - 2 * BORDER_HEIGHT//196
                            && par1 >= var42 && par1 <= var42 + 22 && par2 >= var41 && par2 <= var41 + 22) {
                        this.currentHighlight = var35;
                    }

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }

        GL11.glDisable(2929);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Collection<String> cats = getTabsOnCurrentPage(this.player).keySet();
//        Collection<String> cats = ResearchCategories.researchCategories.keySet();
        int count = 0;
        boolean swop = false;

        for (String obj : cats) {
            ResearchCategoryList rcl = ResearchCategories.getResearchList(obj);
            if (!obj.equals("ELDRITCH") || ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR")) {
                GL11.glPushMatrix();
                if (count == getTabPerSide()//9
                ) {
                    count = 0;
                    swop = true;
                }

                int s0 = !swop ? 0 : getTabDistance();//264;
                int s1 = 0;
                int s2 = swop ? 14 : 0;
                if (!selectedCategory.equals(obj)) {
                    s1 = 24;
                    s2 = swop ? 6 : 8;
                }

                UtilsFX.bindTexture("textures/gui/gui_research.png");
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (swop) {
                    this.drawTexturedModalRectReversed(var8 + s0 - 8, var9 + count * 24, 176 + s1, 232, 24, 24);
                } else {
                    this.drawTexturedModalRect(var8 - 24 + s0, var9 + count * 24, 152 + s1, 232, 24, 24);
                }

                if (highlightedItem.contains(obj)) {
                    GL11.glPushMatrix();
                    this.mc.renderEngine.bindTexture(ParticleEngine.particleTexture);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    int px = (int) (16L * (t % 16L));
//                    UtilsFX.drawTexturedQuad
                    drawResearchCategoryHintParticles
                            (var8 - 27 + s2 + s0, var9 - 4 + count * 24, px, 80, 16, 16, -90.0F,this);
                    GL11.glPopMatrix();
                }

                GL11.glPushMatrix();
                this.mc.renderEngine.bindTexture(rcl.icon);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                UtilsFX.drawTexturedQuadFull(var8 - 19 + s2 + s0, var9 + 4 + count * 24, -80.0F);
                GL11.glPopMatrix();
                if (!selectedCategory.equals(obj)) {
                    UtilsFX.bindTexture("textures/gui/gui_research.png");
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    if (swop) {
                        this.drawTexturedModalRectReversed(var8 + s0 - 8, var9 + count * 24, 224, 232, 24, 24);
                    } else {
                        this.drawTexturedModalRect(var8 - 24 + s0, var9 + count * 24, 200, 232, 24, 24);
                    }
                }

                GL11.glPopMatrix();
                ++count;
            }
        }

        UtilsFX.bindTexture("textures/gui/gui_research.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.drawTexturedModalRect
        drawResearchBrowserBorders
                (this,var8, var9, 0, 0, getResearchBrowserWidth(), getResearchBrowserHeight());
        GL11.glPopMatrix();
        this.zLevel = 0.0F;
        GL11.glDepthFunc(515);
        GL11.glDisable(2929);
        GL11.glEnable(3553);
        super.drawScreen(par1, par2, par3);
        if (completedResearch.get(this.player) != null && this.currentHighlight != null) {
            String var34 = this.currentHighlight.getName();
            int var26 = par1 + 6;
            int var27 = par2 - 4;
            int var99 = 0;
            FontRenderer fr = this.fontRendererObj;
            if (!completedResearch.get(this.player).contains(this.currentHighlight.key) && !this.canUnlockResearch(this.currentHighlight)) {
                fr = this.galFontRenderer;
            }

            if (!this.canUnlockResearch(this.currentHighlight)) {
                GL11.glPushMatrix();
                int var42 = (int) Math.max((float) fr.getStringWidth(var34), (float) fr.getStringWidth(StatCollector.translateToLocal("tc.researchmissing")) / 1.5F);
                String var39 = StatCollector.translateToLocal("tc.researchmissing");
                int var30 = fr.splitStringWidth(var39, var42 * 2);
                this.drawGradientRect(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + var30 + 10, -1073741824, -1073741824);
                GL11.glTranslatef((float) var26, (float) (var27 + 12), 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                this.fontRendererObj.drawSplitString(var39, 0, 0, var42 * 2, -9416624);
                GL11.glPopMatrix();
            } else {
                boolean secondary = !completedResearch.get(this.player).contains(this.currentHighlight.key) && this.currentHighlight.tags != null && this.currentHighlight.tags.size() > 0 && (Config.researchDifficulty == -1 || Config.researchDifficulty == 0 && this.currentHighlight.isSecondary());
                boolean primary = !secondary && !completedResearch.get(this.player).contains(this.currentHighlight.key);
                int var42 = (int) Math.max((float) fr.getStringWidth(var34), (float) fr.getStringWidth(this.currentHighlight.getText()) / 1.9F);
                int var41 = fr.splitStringWidth(var34, var42) + 5;
                if (primary) {
                    var99 += 9;
                    var42 = (int) Math.max((float) var42, (float) fr.getStringWidth(StatCollector.translateToLocal("tc.research.shortprim")) / 1.9F);
                }

                if (secondary) {
                    var99 += 29;
                    var42 = (int) Math.max((float) var42, (float) fr.getStringWidth(StatCollector.translateToLocal("tc.research.short")) / 1.9F);
                }

                int warp = ThaumcraftApi.getWarp(this.currentHighlight.key);
                if (warp > 5) {
                    warp = 5;
                }

                String ws = StatCollector.translateToLocal("tc.forbidden");
                String wr = StatCollector.translateToLocal("tc.forbidden.level." + warp);
                String wte = ws.replaceAll("%n", wr);
                if (ThaumcraftApi.getWarp(this.currentHighlight.key) > 0) {
                    var99 += 9;
                    var42 = (int) Math.max((float) var42, (float) fr.getStringWidth(wte) / 1.9F);
                }

                this.drawGradientRect(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + var41 + 6 + var99, -1073741824, -1073741824);
                GL11.glPushMatrix();
                GL11.glTranslatef((float) var26, (float) (var27 + var41 - 1), 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                this.fontRendererObj.drawStringWithShadow(this.currentHighlight.getText(), 0, 0, -7302913);
                GL11.glPopMatrix();
                if (warp > 0) {
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) var26, (float) (var27 + var41 + 8), 0.0F);
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                    this.fontRendererObj.drawStringWithShadow(wte, 0, 0, 16777215);
                    GL11.glPopMatrix();
                    var41 += 9;
                }

                GL11.glPushMatrix();
                if (primary) {
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) var26, (float) (var27 + var41 + 8), 0.0F);
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                    if (ResearchManager.getResearchSlot(this.mc.thePlayer, this.currentHighlight.key) >= 0) {
                        this.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("tc.research.hasnote"), 0, 0, 16753920);
                    } else if (this.hasScribestuff) {
                        this.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("tc.research.getprim"), 0, 0, 8900331);
                    } else {
                        this.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("tc.research.shortprim"), 0, 0, 14423100);
                    }

                    GL11.glPopMatrix();
                } else if (secondary) {
                    boolean enough = true;
                    int cc = 0;

                    for (Aspect a : this.currentHighlight.tags.getAspectsSortedAmount()) {
                        if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(this.player, a)) {
                            float alpha = 1.0F;
                            if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(this.player, a) < this.currentHighlight.tags.getAmount(a)) {
                                alpha = (float) Math.sin((double) (Minecraft.getSystemTime() % 600L) / (double) 600.0F * Math.PI * (double) 2.0F) * 0.25F + 0.75F;
                                enough = false;
                            }

                            GL11.glPushMatrix();
                            GL11.glPushAttrib(1048575);
                            UtilsFX.drawTag(var26 + cc * 16, var27 + var41 + 8, a, (float) this.currentHighlight.tags.getAmount(a), 0, 0.0F, 771, alpha, false);
                            GL11.glPopAttrib();
                            GL11.glPopMatrix();
                        } else {
                            enough = false;
                            GL11.glPushMatrix();
                            UtilsFX.bindTexture("textures/aspects/_unknown.png");
                            GL11.glColor4f(0.5F, 0.5F, 0.5F, 0.5F);
                            GL11.glTranslated(var26 + cc * 16, var27 + var41 + 8, 0.0F);
                            UtilsFX.drawTexturedQuadFull(0, 0, 0.0F);
                            GL11.glPopMatrix();
                        }

                        ++cc;
                    }

                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) var26, (float) (var27 + var41 + 27), 0.0F);
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                    if (enough) {
                        this.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("tc.research.purchase"), 0, 0, 8900331);
                    } else {
                        this.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("tc.research.short"), 0, 0, 14423100);
                    }

                    GL11.glPopMatrix();
                }

                GL11.glPopMatrix();
            }

            fr.drawStringWithShadow(var34, var26, var27, this.canUnlockResearch(this.currentHighlight) ? (this.currentHighlight.isSpecial() ? -128 : -1) : (this.currentHighlight.isSpecial() ? -8355776 : -8355712));
        }

        GL11.glEnable(2929);
        GL11.glEnable(2896);
        RenderHelper.disableStandardItemLighting();
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        this.popuptime = System.currentTimeMillis() - 1L;
        if (this.currentHighlight != null && !completedResearch.get(this.player).contains(this.currentHighlight.key) && this.canUnlockResearch(this.currentHighlight)) {
            this.updateResearch();
            boolean secondary = this.currentHighlight.tags != null && this.currentHighlight.tags.size() > 0 && (Config.researchDifficulty == -1 || Config.researchDifficulty == 0 && this.currentHighlight.isSecondary());
            if (secondary) {
                boolean enough = true;

                for (Aspect a : this.currentHighlight.tags.getAspects()) {
                    if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(this.player, a) < this.currentHighlight.tags.getAmount(a)) {
                        enough = false;
                        break;
                    }
                }

                if (enough) {
                    PacketHandler.INSTANCE.sendToServer(new PacketPlayerCompleteToServer(this.currentHighlight.key, this.mc.thePlayer.getCommandSenderName(), this.mc.thePlayer.worldObj.provider.dimensionId, (byte) 0));
                }
            } else if (this.hasScribestuff && ResearchManager.getResearchSlot(this.mc.thePlayer, this.currentHighlight.key) == -1) {
                PacketHandler.INSTANCE.sendToServer(new PacketPlayerCompleteToServer(this.currentHighlight.key, this.mc.thePlayer.getCommandSenderName(), this.mc.thePlayer.worldObj.provider.dimensionId, (byte) 1));
                this.popuptime = System.currentTimeMillis() + 3000L;
                this.popupmessage = (new ChatComponentTranslation(StatCollector.translateToLocal("tc.research.popup"), this.currentHighlight.getName())).getUnformattedText();
            }
        } else if (this.currentHighlight != null && completedResearch.get(this.player).contains(this.currentHighlight.key)) {
            this.mc.displayGuiScreen(new GuiResearchRecipe(this.currentHighlight, 0, this.guiMapX, this.guiMapY));
        } else {
            int var4 = (this.width - getResearchBrowserWidth()) / 2;
            int var5 = (this.height - getResearchBrowserHeight()) / 2;
            Collection<String> cats = getTabsOnCurrentPage(this.player).keySet();//ResearchCategories.researchCategories.keySet();
            int count = 0;
            boolean swop = false;

            for (String obj : cats) {
                ResearchCategoryList rcl = ResearchCategories.getResearchList(obj);
                if (!obj.equals("ELDRITCH") || ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR")) {
                    if (count == getTabPerSide()//9
                    ) {
                        count = 0;
                        swop = true;
                    }

                    int mposx = par1 - (var4 - 24 + (swop
                            ? getTabIconDistance()//280
                            : 0));
                    int mposy = par2 - (var5 + count * 24);
                    if (mposx >= 0 && mposx < 24 && mposy >= 0 && mposy < 24) {
                        selectedCategory = obj;
                        this.updateResearch();
                        this.playButtonClick();
                        break;
                    }

                    ++count;
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    public void drawTexturedModalRectReversed(int par1, int par2, int par3, int par4, int par5, int par6) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1, par2 + par6, this.zLevel, (float) (par3) * f, (float) (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, this.zLevel, (float) (par3 - par5) * f, (float) (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2, this.zLevel, (float) (par3 - par5) * f, (float) (par4) * f1);
        tessellator.addVertexWithUV(par1, par2, this.zLevel, (float) (par3) * f, (float) (par4) * f1);
        tessellator.draw();
    }

    private void playButtonClick() {
        this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "thaumcraft:cameraclack", 0.4F, 1.0F, false);
    }

    private boolean canUnlockResearch(ResearchItem res) {
        if (res.parents != null) {
            for (String pt : res.parents) {
                ResearchItem parent = ResearchCategories.getResearch(pt);
                if (parent != null && !completedResearch.get(this.player).contains(parent.key)) {
                    return false;
                }
            }
        }

        if (res.parentsHidden != null) {
            for (String pt : res.parentsHidden) {
                ResearchItem parent = ResearchCategories.getResearch(pt);
                if (parent != null && !completedResearch.get(this.player).contains(parent.key)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawLine(int x, int y, int x2, int y2, float r, float g, float b, float te, boolean wiggle) {
        float count = (float) FMLClientHandler.instance().getClient().thePlayer.ticksExisted + te;
        Tessellator var12 = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glAlphaFunc(516, 0.003921569F);
        GL11.glDisable(3553);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        double d3 = x - x2;
        double d4 = y - y2;
        float dist = MathHelper.sqrt_double(d3 * d3 + d4 * d4);
        int inc = (int) (dist / 2.0F);
        float dx = (float) (d3 / (double) inc);
        float dy = (float) (d4 / (double) inc);
        if (Math.abs(d3) > Math.abs(d4)) {
            dx *= 2.0F;
        } else {
            dy *= 2.0F;
        }

        GL11.glLineWidth(3.0F);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        var12.startDrawing(3);

        for (int a = 0; a <= inc; ++a) {
            float r2 = r;
            float g2 = g;
            float b2 = b;
            float mx = 0.0F;
            float my = 0.0F;
            float op = 0.6F;
            if (wiggle) {
                float phase = (float) a / (float) inc;
                mx = MathHelper.sin((count + (float) a) / 7.0F) * 5.0F * (1.0F - phase);
                my = MathHelper.sin((count + (float) a) / 5.0F) * 5.0F * (1.0F - phase);
                r2 = r * (1.0F - phase);
                g2 = g * (1.0F - phase);
                b2 = b * (1.0F - phase);
                op *= phase;
            }

            var12.setColorRGBA_F(r2, g2, b2, op);
            var12.addVertex((float) x - dx * (float) a + mx, (float) y - dy * (float) a + my, 0.0F);
            if (Math.abs(d3) > Math.abs(d4)) {
                dx *= 1.0F - 1.0F / ((float) inc * 3.0F / 2.0F);
            } else {
                dy *= 1.0F - 1.0F / ((float) inc * 3.0F / 2.0F);
            }
        }

        var12.draw();
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2848);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(32826);
        GL11.glEnable(3553);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glPopMatrix();
    }

    private void drawForbidden(double x, double y) {
        int count = FMLClientHandler.instance().getClient().thePlayer.ticksExisted;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        UtilsFX.bindTexture(TileNodeRenderer.nodetex);
        int frames = 32;
        int part = count % frames;
        GL11.glTranslated(x, y, 0.0F);
        UtilsFX.renderAnimatedQuadStrip(80.0F, 0.66F, frames, 5, frames - 1 - part, 0.0F, 4456533);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }


    /**
     * Draw research browser borders. Called from {@link GuiResearchBrowser#genResearchBackground(int, int, float)}
     */
    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static void drawResearchBrowserBorders(GuiResearchBrowser gui, int x, int y, int u, int v, int width, int height) {
        DrawResearchBrowserBorders.drawResearchBrowserBorders(gui, x, y, u, v, width, height);
    }

    /**
     * Draw research browser background. Called from {@link GuiResearchBrowser#genResearchBackground(int, int, float)}
     */
    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static void drawResearchBrowserBackground(GuiResearchBrowser gui, int x, int y, int u, int v, int width, int height) {
        DrawResearchBrowserBorders.drawResearchBrowserBackground(gui, x, y, u, v, width, height);
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getResearchBrowserHeight() {
        return ConfigurationHandler.INSTANCE.getBrowserHeight();
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getResearchBrowserWidth() {
        return ConfigurationHandler.INSTANCE.getBrowserWidth();
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getTabDistance() {
        // why is this 8?
        return ConfigurationHandler.INSTANCE.getBrowserWidth() + 8;
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getTabIconDistance() {
        // why is this 24?
        return ConfigurationHandler.INSTANCE.getBrowserWidth() + 24;
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getNewGuiMapTop(int oldVal) {
        return (int) (oldVal - 85 * (ConfigurationHandler.INSTANCE.getBrowserScale() - 1));
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getNewGuiMapLeft(int oldVal) {
        return (int) (oldVal - 112 * (ConfigurationHandler.INSTANCE.getBrowserScale() - 1));
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getNewGuiMapBottom(int oldVal) {
        return (int) (oldVal - 112 * (ConfigurationHandler.INSTANCE.getBrowserScale() - 1));
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getNewGuiMapRight(int oldVal) {
        return (int) (oldVal - 61 * (ConfigurationHandler.INSTANCE.getBrowserScale() - 1));
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static int getTabPerSide() {
        return BrowserPaging.getTabPerSide();
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static LinkedHashMap<String, ResearchCategoryList> getTabsOnCurrentPage(String player) {
        return BrowserPaging.getTabsOnCurrentPage(player);
    }

    //@Callhook(adder = GuiResearchBrowserVisitor.class, module = ASMConstants.Modules.BiggerResearchBrowser)
    public static void drawResearchCategoryHintParticles(int x, int y, int u, int v, int width, int height, double zLevel, GuiResearchBrowser gui) {
        if (x < gui.width / 2)
            UtilsFX.drawTexturedQuad(x, y, u, v, width, height, zLevel);
        else {
            x += 16;
            ClientUtils.drawRectTextured(x, x + width, y, y + height, u + width, u, v + height, v, zLevel);
        }
    }
}
