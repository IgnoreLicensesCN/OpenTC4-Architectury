package thaumcraft.client.gui;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.client.ClientProxy;
import thaumcraft.client.lib.TCFontRenderer;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.utils.InventoryUtils;

import static tc4tweak.ClientUtils.cacheUsed;

@SideOnly(Side.CLIENT)
public class GuiResearchRecipe extends GuiScreen {
    protected static RenderItem itemRenderer = new RenderItem();
    public static LinkedList<Object[]> history = new LinkedList<>();
    protected int paneWidth = 256;
    protected int paneHeight = 181;
    protected double guiMapX;
    protected double guiMapY;
    protected int mouseX = 0;
    protected int mouseY = 0;
    private GuiButton button;
    private ResearchItem research;
    private ResearchPage[] pages = null;
    private int page = 0;
    private int maxPages = 0;
    TCFontRenderer fr = null;
    HashMap<Aspect, ArrayList<ItemStack>> aspectItems = new HashMap<>();
    public static ConcurrentHashMap<Integer, ItemStack> cache = new ConcurrentHashMap<>();
    String tex1 = "textures/gui/gui_researchbook.png";
    String tex2 = "textures/gui/gui_researchbook_overlay.png";
    private Object[] tooltip = null;
    private long lastCycle = 0L;
    ArrayList<List<?>> reference = new ArrayList<>();
    private int cycle = -1;

    public static void putToCache(int key, ItemStack stack) {
        cache.put(key, stack);
    }

    public static ItemStack getFromCache(int key) {
        cacheUsed.lazySet(true);
        return cache.get(key);
    }

    public GuiResearchRecipe(ResearchItem research, int page, double x, double y) {
        this.research = research;
        this.guiMapX = x;
        this.guiMapY = y;
        this.mc = Minecraft.getMinecraft();
        this.pages = research.getPages();
        ResearchPage[] p1 = this.pages;
        ArrayList<ResearchPage> p2 = new ArrayList<>();

        for (ResearchPage pp : p1) {
            if (pp == null || pp.type != ResearchPage.PageType.TEXT_CONCEALED || ThaumcraftApiHelper.isResearchComplete(this.mc.thePlayer.getCommandSenderName(), pp.research)) {
                p2.add(pp);
            }
        }

        this.pages = p2.toArray(new ResearchPage[0]);
        if (research.key.equals("ASPECTS")) {
            AspectList aspectsKnownSorted = Thaumcraft.proxy.getPlayerKnowledge().getAspectsDiscovered(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
            List<String> list = Thaumcraft.proxy.getScannedObjects().get(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
            if (list != null && !list.isEmpty()) {
                for (String s : list) {
                    try {
                        String s2 = s.substring(1);
                        ItemStack is = getFromCache(Integer.parseInt(s2));
                        if (is != null) {
                            AspectList tags = ThaumcraftCraftingManager.getObjectTags(is);
                            tags = ThaumcraftCraftingManager.getBonusTags(is, tags);
                            if (tags != null && tags.size() > 0) {
                                for (Aspect a : tags.getAspects()) {
                                    ArrayList<ItemStack> items = this.aspectItems.get(a);
                                    if (items == null) {
                                        items = new ArrayList<>();
                                    }

                                    ItemStack is2 = is.copy();
                                    is2.stackSize = tags.getAmount(a);
                                    items.add(is2);
                                    this.aspectItems.put(a, items);
                                }
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            ArrayList<ResearchPage> tpl = new ArrayList<>(Arrays.asList(research.getPages()));

            AspectList tal = new AspectList();
            if (aspectsKnownSorted != null) {
                int count = 0;

                for (Aspect aspect : aspectsKnownSorted.getAspectsSorted()) {
                    if (count <= 4) {
                        ++count;
                        tal.add(aspect, aspectsKnownSorted.getAmount(aspect));
                    }

                    if (count == 4) {
                        count = 0;
                        tpl.add(new ResearchPage(tal.copy()));
                        tal = new AspectList();
                    }
                }

                if (count > 0) {
                    tpl.add(new ResearchPage(tal));
                }
            }

            this.pages = tpl.toArray(this.pages);
        }

        this.maxPages = this.pages.length;
        this.fr = new TCFontRenderer(this.mc.gameSettings, TCFontRenderer.FONT_NORMAL, this.mc.renderEngine, true);
        if (page % 2 == 1) {
            --page;
        }

        this.page = page;
    }

    public void initGui() {
        super.initGui();
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
    }

    protected void keyTyped(char par1, int par2) {
        if (par2 != this.mc.gameSettings.keyBindInventory.getKeyCode() && par2 != 1) {
            super.keyTyped(par1, par2);
        } else {
            history.clear();
            this.mc.displayGuiScreen(new GuiResearchBrowser(this.guiMapX, this.guiMapY));
        }

    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.genResearchBackground(par1, par2, par3);
        int sw = (this.width - this.paneWidth) / 2;
        int sh = (this.height - this.paneHeight) / 2;
        if (!history.isEmpty()) {
            int mx = par1 - (sw + 118);
            int my = par2 - (sh + 189);
            if (mx >= 0 && my >= 0 && mx < 20 && my < 12) {
                this.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("recipe.return"), par1, par2, 16777215);
            }
        }

    }

    protected void genResearchBackground(int par1, int par2, float par3) {
        int sw = (this.width - this.paneWidth) / 2;
        int sh = (this.height - this.paneHeight) / 2;
        float var10 = ((float) this.width - (float) this.paneWidth * 1.3F) / 2.0F;
        float var11 = ((float) this.height - (float) this.paneHeight * 1.3F) / 2.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        UtilsFX.bindTexture(this.tex1);
        GL11.glPushMatrix();
        GL11.glTranslatef(var10, var11, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glScalef(1.3F, 1.3F, 1.0F);
        this.drawTexturedModalRect(0, 0, 0, 0, this.paneWidth, this.paneHeight);
        GL11.glPopMatrix();
        this.reference.clear();
        this.tooltip = null;
        int current = 0;

        for (ResearchPage researchPage : this.pages) {
            if ((current == this.page || current == this.page + 1) && current < this.maxPages) {
                this.drawPage(researchPage, current % 2, sw, sh, par1, par2);
            }

            ++current;
            if (current > this.page + 1) {
                break;
            }
        }

        if (this.tooltip != null) {
            UtilsFX.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, (List) this.tooltip[0], (Integer) this.tooltip[1], (Integer) this.tooltip[2], (Integer) this.tooltip[3]);
        }

        UtilsFX.bindTexture(this.tex1);
        int var10000 = par1 - (sw + 261);
        var10000 = par2 - (sh + 189);
        var10000 = par1 - (sw - 17);
        var10000 = par2 - (sh + 189);
        float bob = MathHelper.sin((float) this.mc.thePlayer.ticksExisted / 3.0F) * 0.2F + 0.1F;
        if (!history.isEmpty()) {
            GL11.glEnable(GL11.GL_BLEND);
            this.drawTexturedModalRectScaled(sw + 118, sh + 189, 38, 202, 20, 12, bob);
        }

        if (this.page > 0) {
            GL11.glEnable(GL11.GL_BLEND);
            this.drawTexturedModalRectScaled(sw - 16, sh + 190, 0, 184, 12, 8, bob);
        }

        if (this.page < this.maxPages - 2) {
            GL11.glEnable(GL11.GL_BLEND);
            this.drawTexturedModalRectScaled(sw + 262, sh + 190, 12, 184, 12, 8, bob);
        }

    }

    public void drawCustomTooltip(GuiScreen gui, RenderItem itemRenderer, FontRenderer fr, List var4, int par2, int par3, int subTipColor) {
        this.tooltip = new Object[]{var4, par2, par3, subTipColor};
    }

    private void drawPage(ResearchPage pageParm, int side, int x, int y, int mx, int my) {
        GL11.glPushAttrib(1048575);
        if (this.lastCycle < System.currentTimeMillis()) {
            ++this.cycle;
            this.lastCycle = System.currentTimeMillis() + 1000L;
        }

        if (this.page == 0 && side == 0) {
            this.drawTexturedModalRect(x + 4, y - 13, 24, 184, 96, 4);
            this.drawTexturedModalRect(x + 4, y + 4, 24, 184, 96, 4);
            int offset = this.fontRendererObj.getStringWidth(this.research.getName());
            if (offset <= 130) {
                this.fontRendererObj.drawString(this.research.getName(), x + 52 - offset / 2, y - 6, 3158064);
            } else {
                float vv = 130.0F / (float) offset;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) (x + 52) - (float) (offset / 2) * vv, (float) y - 6.0F * vv, 0.0F);
                GL11.glScalef(vv, vv, vv);
                this.fontRendererObj.drawString(this.research.getName(), 0, 0, 3158064);
                GL11.glPopMatrix();
            }

            y += 25;
        }

        GL11.glAlphaFunc(516, 0.003921569F);
        if (pageParm.type != ResearchPage.PageType.TEXT && pageParm.type != ResearchPage.PageType.TEXT_CONCEALED) {
            if (pageParm.type == ResearchPage.PageType.ASPECTS) {
                this.drawAspectPage(side, x - 8, y - 8, mx, my, pageParm.aspects);
            } else if (pageParm.type == ResearchPage.PageType.CRUCIBLE_CRAFTING) {
                this.drawCruciblePage(side, x - 4, y - 8, mx, my, pageParm);
            } else if (pageParm.type == ResearchPage.PageType.NORMAL_CRAFTING) {
                this.drawCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
            } else if (pageParm.type == ResearchPage.PageType.ARCANE_CRAFTING) {
                this.drawArcaneCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
            } else if (pageParm.type == ResearchPage.PageType.COMPOUND_CRAFTING) {
                this.drawCompoundCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
            } else if (pageParm.type == ResearchPage.PageType.INFUSION_CRAFTING) {
                this.drawInfusionPage(side, x - 4, y - 8, mx, my, pageParm);
            } else if (pageParm.type == ResearchPage.PageType.INFUSION_ENCHANTMENT) {
                this.drawInfusionEnchantingPage(side, x - 4, y - 8, mx, my, pageParm);
            } else if (pageParm.type == ResearchPage.PageType.SMELTING) {
                this.drawSmeltingPage(side, x - 4, y - 8, mx, my, pageParm);
            }
        } else {
            this.drawTextPage(side, x, y - 10, pageParm.getTranslatedText());
        }

        GL11.glAlphaFunc(516, 0.1F);
        GL11.glPopAttrib();
    }

    private void drawCompoundCraftingPage(int side, int x, int y, int mx, int my, ResearchPage page) {
        List r = (List) page.recipe;
        if (r != null) {
            AspectList aspects = (AspectList) r.get(0);
            int dx = (Integer) r.get(1);
            int dy = (Integer) r.get(2);
            int dz = (Integer) r.get(3);
            int xoff = 64 - (dx * 16 + dz * 16) / 2;
            int yoff = -dy * 25;
            List<ItemStack> items = (List) r.get(4);
            GL11.glPushMatrix();
            int start = side * 152;
            String text = StatCollector.translateToLocal("recipe.type.construct");
            int offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
            int mposx = mx;
            int mposy = my;
            if (aspects != null && aspects.size() > 0) {
                int count = 0;

                for (Aspect tag : aspects.getAspectsSortedAmount()) {
                    UtilsFX.drawTag(x + start + 14 + 18 * count + (5 - aspects.size()) * 8, y + 182, tag, (float) aspects.getAmount(tag), 0, 0.0F, 771, 1.0F, false);
                    ++count;
                }

                count = 0;

                for (Aspect tag : aspects.getAspectsSortedAmount()) {
                    int tx = x + start + 14 + 18 * count + (5 - aspects.size()) * 8;
                    int ty = y + 182;
                    if (mposx >= tx && mposy >= ty && mposx < tx + 16 && mposy < ty + 16) {
                        this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my - 8, 11);
                    }

                    ++count;
                }
            }

            UtilsFX.bindTexture(this.tex2);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(2896);
            if (aspects != null && aspects.size() > 0) {
                GL11.glPushMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glTranslatef((float) (x + start), (float) (y + 174), 0.0F);
                GL11.glScalef(2.0F, 2.0F, 1.0F);
                this.drawTexturedModalRect(0, 0, 68, 76, 12, 12);
                GL11.glPopMatrix();
            }

            GL11.glPushMatrix();
            float sz = 0.0F;
            if (dy > 3) {
                sz = (float) (dy - 3) * 0.2F;
                GL11.glTranslatef((float) (x + start) + (float) xoff * (1.0F + sz), (float) (y + 108) + (float) yoff * (1.0F - sz), 0.0F);
                GL11.glScalef(1.0F - sz, 1.0F - sz, 1.0F - sz);
            } else {
                GL11.glTranslatef((float) (x + start + xoff), (float) (y + 108 + yoff), 0.0F);
            }

            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (-8 - xoff), (float) (-119 + Math.max(3 - dx, 3 - dz) * 8 + dx * 4 + dz * 4 + dy * 50), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            this.drawTexturedModalRect(0, 0, 0, 72, 64, 44);
            GL11.glPopMatrix();
            int count = 0;

            for (int j = 0; j < dy; ++j) {
                for (int k = dz - 1; k >= 0; --k) {
                    for (int i = dx - 1; i >= 0; --i) {
                        int px = i * 16 + k * 16;
                        int py = -i * 8 + k * 8 + j * 50;
                        GL11.glPushMatrix();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderHelper.enableGUIStandardItemLighting();
                        GL11.glEnable(2884);
                        GL11.glTranslatef(0.0F, 0.0F, (float) (60 - j * 10));
                        if (items.get(count) != null) {
                            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items.get(count)), px, py);
                            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items.get(count)).copy().splitStack(1), px, py);
                        }

                        RenderHelper.disableStandardItemLighting();
                        GL11.glPopMatrix();
                        ++count;
                    }
                }
            }

            GL11.glEnable(2896);
            GL11.glPopMatrix();
            count = 0;

            for (int j = 0; j < dy; ++j) {
                for (int k = dz - 1; k >= 0; --k) {
                    for (int i = dx - 1; i >= 0; --i) {
                        int px = (int) ((float) (x + start) + (float) xoff * (1.0F + sz) + (float) (i * 16) * (1.0F - sz) + (float) (k * 16) * (1.0F - sz));
                        int py = (int) ((float) (y + 108) + (float) yoff * (1.0F - sz) - (float) (i * 8) * (1.0F - sz) + (float) (k * 8) * (1.0F - sz) + (float) (j * 50) * (1.0F - sz));
                        if (items.get(count) != null && mposx >= px && mposy >= py && (float) mposx < (float) px + 16.0F * (1.0F - sz) && (float) mposy < (float) py + 16.0F * (1.0F - sz)) {
                            List addtext = InventoryUtils.cycleItemStack(items.get(count)).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                            Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(items.get(count)));
                            if (ref != null && !ref[0].equals(this.research.key)) {
                                addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                                this.reference.add(
                                        Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1])
                                );
                            }

                            this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
                        }

                        ++count;
                    }
                }
            }

            GL11.glPopMatrix();
        }

    }

    private void drawAspectPage(int side, int x, int y, int mx, int my, AspectList aspects) {
        if (aspects != null && aspects.size() > 0) {
            GL11.glPushMatrix();
            int start = side * 152;
            int mposx = mx;
            int mposy = my;
            int count = 0;

            for (Aspect aspect : aspects.getAspectsSorted()) {
                if (aspect.getImage() != null) {
                    GL11.glPushMatrix();
                    int tx = x + start;
                    int ty = y + count * 50;
                    if (mposx >= tx && mposy >= ty && mposx < tx + 40 && mposy < ty + 40) {
                        UtilsFX.bindTexture("textures/aspects/_back.png");
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(770, 771);
                        GL11.glTranslated(x + start - 5, y + count * 50 - 5, 0.0F);
                        GL11.glScaled(2.5F, 2.5F, 0.0F);
                        UtilsFX.drawTexturedQuadFull(0, 0, this.zLevel);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glPopMatrix();
                    }

                    GL11.glScalef(2.0F, 2.0F, 2.0F);
                    UtilsFX.drawTag((x + start) / 2, (y + count * 50) / 2, aspect, (float) aspects.getAmount(aspect), 0, this.zLevel);
                    GL11.glPopMatrix();
                    String text = aspect.getName();
                    int offset = this.fr.getStringWidth(text) / 2;
                    this.fr.drawString(text, x + start + 16 - offset, y + 33 + count * 50, 5263440);
                    if (aspect.getComponents() != null) {
                        GL11.glPushMatrix();
                        GL11.glScalef(1.5F, 1.5F, 1.5F);
                        UtilsFX.drawTag((int) ((float) (x + start + 54) / 1.5F), (int) ((float) (y + 4 + count * 50) / 1.5F), aspect.getComponents()[0], 0.0F, 0, this.zLevel);
                        UtilsFX.drawTag((int) ((float) (x + start + 96) / 1.5F), (int) ((float) (y + 4 + count * 50) / 1.5F), aspect.getComponents()[1], 0.0F, 0, this.zLevel);
                        GL11.glPopMatrix();
                        text = aspect.getComponents()[0].getName();
                        offset = this.fr.getStringWidth(text) / 2;
                        this.fr.drawString("§o" + text, x + start + 16 - offset + 50, y + 30 + count * 50, 5263440);
                        text = aspect.getComponents()[1].getName();
                        offset = this.fr.getStringWidth(text) / 2;
                        this.fr.drawString("§o" + text, x + start + 16 - offset + 92, y + 30 + count * 50, 5263440);
                        this.fontRendererObj.drawString("=", x + start + 7 + 32, y + 12 + count * 50, 10066329);
                        this.fontRendererObj.drawString("+", x + start + 4 + 79, y + 12 + count * 50, 10066329);
                    } else {
                        this.fr.drawString(StatCollector.translateToLocal("tc.aspect.primal"), x + start + 48, y + 12 + count * 50, 4473924);
                    }
                }

                ++count;
            }

            count = 0;

            for (Aspect aspect : aspects.getAspectsSorted()) {
                int tx = x + start;
                int ty = y + count * 50;
                if (mposx >= tx && mposy >= ty && mposx < tx + 40 && mposy < ty + 40) {
                    ArrayList<ItemStack> items = this.aspectItems.get(aspect);
                    if (items != null && !items.isEmpty()) {
                        int xcount = 0;
                        int ycount = 0;

                        for (ItemStack item : items) {
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            RenderHelper.enableGUIStandardItemLighting();
                            GL11.glEnable(2884);
                            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(item), mposx + 8 + xcount * 17, 17 * ycount + (mposy - (4 + items.size() / 8 * 8)));
                            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(item), mposx + 8 + xcount * 17, 17 * ycount + (mposy - (4 + items.size() / 8 * 8)));
                            RenderHelper.disableStandardItemLighting();
                            GL11.glPopMatrix();
                            ++xcount;
                            if (xcount >= 8) {
                                xcount = 0;
                                ++ycount;
                            }
                        }

                        GL11.glEnable(2896);
                    }
                }

                ++count;
            }

            GL11.glPopMatrix();
        }

    }

    private void drawArcaneCraftingPage(int side, int x, int y, int mx, int my, ResearchPage pageParm) {
        IArcaneRecipe recipe = null;
        Object tr = null;
        if (pageParm.recipe instanceof Object[]) {
            try {
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            } catch (Exception var22) {
                this.cycle = 0;
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            }
        } else {
            tr = pageParm.recipe;
        }

        if (tr instanceof ShapedArcaneRecipe) {
            recipe = (ShapedArcaneRecipe) tr;
        } else if (tr instanceof ShapelessArcaneRecipe) {
            recipe = (ShapelessArcaneRecipe) tr;
        }

        if (recipe != null) {
            GL11.glPushMatrix();
            int start = side * 152;
            UtilsFX.bindTexture(this.tex2);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (x + start), (float) y, 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            this.drawTexturedModalRect(2, 27, 112, 15, 52, 52);
            this.drawTexturedModalRect(20, 7, 20, 3, 16, 16);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (x + start), (float) (y + 164), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            this.drawTexturedModalRect(0, 0, 68, 76, 12, 12);
            GL11.glPopMatrix();
            int mposx = mx;
            int mposy = my;
            AspectList tags = recipe.getAspects();
            if (tags != null && tags.size() > 0) {
                int count = 0;

                for (Aspect tag : tags.getAspectsSortedAmount()) {
                    UtilsFX.drawTag(x + start + 14 + 18 * count + (5 - tags.size()) * 8, y + 172, tag, (float) tags.getAmount(tag), 0, 0.0F, 771, 1.0F);
                    ++count;
                }

                count = 0;

                for (Aspect tag : tags.getAspectsSortedAmount()) {
                    int tx = x + start + 14 + 18 * count + (5 - tags.size()) * 8;
                    int ty = y + 172;
                    if (mposx >= tx && mposy >= ty && mposx < tx + 16 && mposy < ty + 16) {
                        this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my - 8, 11);
                    }

                    ++count;
                }
            }

            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(recipe.getRecipeOutput()), x + 48 + start, y + 22);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(recipe.getRecipeOutput()), x + 48 + start, y + 22);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            if (mposx >= x + 48 + start && mposy >= y + 27 && mposx < x + 48 + start + 16 && mposy < y + 27 + 16) {
                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, InventoryUtils.cycleItemStack(recipe.getRecipeOutput()).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips), mx, my, 11);
            }

            String text = StatCollector.translateToLocal("recipe.type.arcane");
            int offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
            if (recipe != null && recipe instanceof ShapedArcaneRecipe) {
                int rw = ((ShapedArcaneRecipe) recipe).width;
                int rh = ((ShapedArcaneRecipe) recipe).height;
                Object[] items = ((ShapedArcaneRecipe) recipe).getInput();

                for (int i = 0; i < rw && i < 3; ++i) {
                    for (int j = 0; j < rh && j < 3; ++j) {
                        if (items[i + j * rw] != null) {
                            GL11.glPushMatrix();
                            GL11.glTranslated(0.0F, 0.0F, 100.0F);
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            RenderHelper.enableGUIStandardItemLighting();
                            GL11.glEnable(2884);
                            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items[i + j * rw]), x + start + 16 + i * 32, y + 66 + j * 32);
                            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items[i + j * rw]).copy().splitStack(1), x + start + 16 + i * 32, y + 66 + j * 32);
                            RenderHelper.disableStandardItemLighting();
                            GL11.glEnable(2896);
                            GL11.glPopMatrix();
                        }
                    }
                }

                for (int i = 0; i < rw && i < 3; ++i) {
                    for (int j = 0; j < rh && j < 3; ++j) {
                        if (items[i + j * rw] != null && mposx >= x + 16 + start + i * 32 && mposy >= y + 66 + j * 32 && mposx < x + 16 + start + i * 32 + 16 && mposy < y + 66 + j * 32 + 16) {
                            List addtext = InventoryUtils.cycleItemStack(items[i + j * rw]).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                            Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(items[i + j * rw]));
                            if (ref != null && !ref[0].equals(this.research.key)) {
                                addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                                this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                            }

                            this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
                        }
                    }
                }
            }

            if (recipe != null && recipe instanceof ShapelessArcaneRecipe) {
                List<Object> items = ((ShapelessArcaneRecipe) recipe).getInput();

                for (int i = 0; i < items.size() && i < 9; ++i) {
                    if (items.get(i) != null) {
                        GL11.glPushMatrix();
                        GL11.glTranslated(0.0F, 0.0F, 100.0F);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderHelper.enableGUIStandardItemLighting();
                        GL11.glEnable(2884);
                        itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items.get(i)), x + start + 16 + i % 3 * 32, y + 66 + i / 3 * 32);
                        itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items.get(i)), x + start + 16 + i % 3 * 32, y + 66 + i / 3 * 32);
                        RenderHelper.disableStandardItemLighting();
                        GL11.glEnable(2896);
                        GL11.glPopMatrix();
                    }
                }

                for (int i = 0; i < items.size() && i < 9; ++i) {
                    if (items.get(i) != null && mposx >= x + 16 + start + i % 3 * 32 && mposy >= y + 66 + i / 3 * 32 && mposx < x + 16 + start + i % 3 * 32 + 16 && mposy < y + 66 + i / 3 * 32 + 16) {
                        List addtext = InventoryUtils.cycleItemStack(items.get(i)).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                        Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(items.get(i)));
                        if (ref != null && !ref[0].equals(this.research.key)) {
                            addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                            this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                        }

                        this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
                    }
                }
            }

            GL11.glPopMatrix();
        }
    }

    private void drawCraftingPage(int side, int x, int y, int mx, int my, ResearchPage pageParm) {
        IRecipe recipe = null;
        Object tr = null;
        if (pageParm.recipe instanceof Object[]) {
            try {
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            } catch (Exception var21) {
                this.cycle = 0;
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            }
        } else {
            tr = pageParm.recipe;
        }

        if (tr instanceof ShapedRecipes) {
            recipe = (ShapedRecipes) tr;
        } else if (tr instanceof ShapelessRecipes) {
            recipe = (ShapelessRecipes) tr;
        } else if (tr instanceof ShapedOreRecipe) {
            recipe = (ShapedOreRecipe) tr;
        } else if (tr instanceof ShapelessOreRecipe) {
            recipe = (ShapelessOreRecipe) tr;
        }

        if (recipe != null) {
            GL11.glPushMatrix();
            int start = side * 152;
            UtilsFX.bindTexture(this.tex2);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (x + start), (float) y, 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            this.drawTexturedModalRect(2, 32, 60, 15, 52, 52);
            this.drawTexturedModalRect(20, 12, 20, 3, 16, 16);
            GL11.glPopMatrix();
            int mposx = mx;
            int mposy = my;
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(recipe.getRecipeOutput()), x + 48 + start, y + 32);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(recipe.getRecipeOutput()), x + 48 + start, y + 32);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            if (mx >= x + 48 + start && my >= y + 32 && mx < x + 48 + start + 16 && my < y + 32 + 16) {
                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, InventoryUtils.cycleItemStack(recipe.getRecipeOutput()).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips), mx, my, 11);
            }

            if (recipe != null && (recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe)) {
                String text = StatCollector.translateToLocal("recipe.type.workbench");
                int offset = this.fontRendererObj.getStringWidth(text);
                this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
                int rw = 0;
                int rh = 0;
                Object[] items = null;
                if (recipe instanceof ShapedRecipes) {
                    rw = ((ShapedRecipes) recipe).recipeWidth;
                    rh = ((ShapedRecipes) recipe).recipeHeight;
                    items = ((ShapedRecipes) recipe).recipeItems;
                } else {
                    rw = ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe, new String[]{"width"});
                    rh = ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe, new String[]{"height"});
                    items = ((ShapedOreRecipe) recipe).getInput();
                }

                for (int i = 0; i < rw && i < 3; ++i) {
                    for (int j = 0; j < rh && j < 3; ++j) {
                        if (items[i + j * rw] != null) {
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            RenderHelper.enableGUIStandardItemLighting();
                            GL11.glEnable(2884);
                            GL11.glTranslated(0.0F, 0.0F, 100.0F);
                            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items[i + j * rw]), x + start + 16 + i * 32, y + 76 + j * 32);
                            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(items[i + j * rw]).copy().splitStack(1), x + start + 16 + i * 32, y + 76 + j * 32);
                            RenderHelper.disableStandardItemLighting();
                            GL11.glEnable(2896);
                            GL11.glPopMatrix();
                        }
                    }
                }

                for (int i = 0; i < rw && i < 3; ++i) {
                    for (int j = 0; j < rh && j < 3; ++j) {
                        if (items[i + j * rw] != null && mposx >= x + 16 + start + i * 32 && mposy >= y + 76 + j * 32 && mposx < x + 16 + start + i * 32 + 16 && mposy < y + 76 + j * 32 + 16) {
                            List addtext = InventoryUtils.cycleItemStack(items[i + j * rw]).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                            Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(items[i + j * rw]));
                            if (ref != null && !ref[0].equals(this.research.key)) {
                                addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                                this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                            }

                            this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
                        }
                    }
                }
            }

            if (recipe != null && (recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe)) {
                String text = StatCollector.translateToLocal("recipe.type.workbenchshapeless");
                int offset = this.fontRendererObj.getStringWidth(text);
                this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
                List<Object> items = null;
                Object var27;
                if (recipe instanceof ShapelessRecipes) {
                    var27 = ((ShapelessRecipes) recipe).recipeItems;
                } else {
                    var27 = ((ShapelessOreRecipe) recipe).getInput();
                }

                for (int i = 0; i < ((List) var27).size() && i < 9; ++i) {
                    if (((List) var27).get(i) != null) {
                        GL11.glPushMatrix();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderHelper.enableGUIStandardItemLighting();
                        GL11.glEnable(2884);
                        GL11.glTranslated(0.0F, 0.0F, 100.0F);
                        itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(((List) var27).get(i)), x + start + 16 + i % 3 * 32, y + 76 + i / 3 * 32);
                        itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(((List) var27).get(i)).copy().splitStack(1), x + start + 16 + i % 3 * 32, y + 76 + i / 3 * 32);
                        RenderHelper.disableStandardItemLighting();
                        GL11.glEnable(2896);
                        GL11.glPopMatrix();
                    }
                }

                for (int i = 0; i < ((List) var27).size() && i < 9; ++i) {
                    if (((List) var27).get(i) != null && mposx >= x + 16 + start + i % 3 * 32 && mposy >= y + 76 + i / 3 * 32 && mposx < x + 16 + start + i % 3 * 32 + 16 && mposy < y + 76 + i / 3 * 32 + 16) {
                        List addtext = InventoryUtils.cycleItemStack(((List) var27).get(i)).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                        Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(((List) var27).get(i)));
                        if (ref != null && !ref[0].equals(this.research.key)) {
                            addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                            this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                        }

                        this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
                    }
                }
            }

            GL11.glPopMatrix();
        }
    }

    private void drawCruciblePage(int side, int x, int y, int mx, int my, ResearchPage pageParm) {
        CrucibleRecipe rc = null;
        Object tr = null;
        if (pageParm.recipe instanceof Object[]) {
            try {
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            } catch (Exception var26) {
                this.cycle = 0;
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            }
        } else {
            tr = pageParm.recipe;
        }

        if (tr instanceof CrucibleRecipe) {
            rc = (CrucibleRecipe) tr;
        }

        if (rc != null) {
            GL11.glPushMatrix();
            int start = side * 152;
            String text = StatCollector.translateToLocal("recipe.type.crucible");
            int offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
            UtilsFX.bindTexture(this.tex2);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (x + start), (float) (y + 28), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            this.drawTexturedModalRect(0, 0, 0, 3, 56, 17);
            GL11.glTranslatef(0.0F, 32.0F, 0.0F);
            this.drawTexturedModalRect(0, 0, 0, 20, 56, 48);
            GL11.glTranslatef(21.0F, -8.0F, 0.0F);
            this.drawTexturedModalRect(0, 0, 100, 84, 11, 13);
            GL11.glPopMatrix();
            int mposx = mx;
            int mposy = my;
            int total = 0;
            int rows = (rc.aspects.size() - 1) / 3;
            int shift = (3 - rc.aspects.size() % 3) * 10;
            int sx = x + start + 28;
            int sy = y + 96 + 32 - 10 * rows;

            for (Aspect tag : rc.aspects.getAspectsSorted()) {
                int m = 0;
                if (total / 3 >= rows && (rows > 1 || rc.aspects.size() < 3)) {
                    m = 1;
                }

                int vx = sx + total % 3 * 20 + shift * m;
                int vy = sy + total / 3 * 20;
                UtilsFX.drawTag(vx, vy, tag, (float) rc.aspects.getAmount(tag), 0, this.zLevel);
                ++total;
            }

            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, rc.getRecipeOutput(), x + 48 + start, y + 36);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, rc.getRecipeOutput(), x + 48 + start, y + 36);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(rc.catalyst), x + 26 + start, y + 72);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(rc.catalyst).copy().splitStack(1), x + 26 + start, y + 72);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            if (mx >= x + 48 + start && my >= y + 36 && mx < x + 48 + start + 16 && my < y + 36 + 16) {
                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, rc.getRecipeOutput().getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips), mx, my, 11);
            }

            if (mx >= x + 26 + start && my >= y + 72 && mx < x + 26 + start + 16 && my < y + 72 + 16) {
                List addtext = InventoryUtils.cycleItemStack(rc.catalyst)
                        .getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips)
                        ;
                Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(rc.catalyst));
                if (ref != null && !ref[0].equals(this.research.key)) {
                    addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                    this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                }

                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
            }

            total = 0;

            for (Aspect tag : rc.aspects.getAspectsSorted()) {
                int m = 0;
                if (total / 3 >= rows && (rows > 1 || rc.aspects.size() < 3)) {
                    m = 1;
                }

                int vx = sx + total % 3 * 20 + shift * m;
                int vy = sy + total / 3 * 20;
                if (mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16) {
                    this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my, 11);
                }

                ++total;
            }

            GL11.glPopMatrix();
        }

    }

    private void drawSmeltingPage(int side, int x, int y, int mx, int my, ResearchPage pageParm) {
        ItemStack in = (ItemStack) pageParm.recipe;
        ItemStack out = null;
        if (in != null) {
            out = FurnaceRecipes.smelting().getSmeltingResult(in);
        }

        if (in != null && out != null) {
            GL11.glPushMatrix();
            int start = side * 152;
            String text = StatCollector.translateToLocal("recipe.type.smelting");
            int offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
            UtilsFX.bindTexture(this.tex2);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (x + start), (float) (y + 28), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            this.drawTexturedModalRect(0, 0, 0, 192, 56, 64);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, in, x + 48 + start, y + 64);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, in, x + 48 + start, y + 64);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, out, x + 48 + start, y + 144);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, out, x + 48 + start, y + 144);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            if (mx >= x + 48 + start && my >= y + 64 && mx < x + 48 + start + 16 && my < y + 64 + 16) {
                List addtext = in.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                Object[] ref = this.findRecipeReference(in);
                if (ref != null && !ref[0].equals(this.research.key)) {
                    addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                    this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                }

                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
            }

            if (mx >= x + 48 + start && my >= y + 144 && mx < x + 48 + start + 16 && my < y + 144 + 16) {
                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, out.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips), mx, my, 11);
            }

            GL11.glPopMatrix();
        }

    }

    private void drawInfusionPage(int side, int x, int y, int mx, int my, ResearchPage pageParm) {
        Object tr = null;
        if (pageParm.recipe instanceof Object[]) {
            try {
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            } catch (Exception var33) {
                this.cycle = 0;
                tr = ((Object[]) pageParm.recipe)[this.cycle];
            }
        } else {
            tr = pageParm.recipe;
        }

        InfusionRecipe ri = (InfusionRecipe) tr;
        if (ri != null) {
            GL11.glPushMatrix();
            int start = side * 152;
            String text = StatCollector.translateToLocal("recipe.type.infusion");
            int offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
            int inst = Math.min(5, ri.getInstability() / 2);
            text = StatCollector.translateToLocal("tc.inst") + " " + StatCollector.translateToLocal("tc.inst." + inst);
            offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y + 194, 5263440);
            UtilsFX.bindTexture(this.tex2);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (x + start), (float) (y + 20), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            this.drawTexturedModalRect(0, 0, 0, 3, 56, 17);
            GL11.glTranslatef(0.0F, 19.0F, 0.0F);
            this.drawTexturedModalRect(0, 0, 200, 77, 60, 44);
            GL11.glPopMatrix();
            int mposx = mx;
            int mposy = my;
            int total = 0;
            int rows = (ri.getAspects().size() - 1) / 5;
            int shift = (5 - ri.getAspects().size() % 5) * 10;
            int sx = x + start + 8;
            int sy = y + 164 - 10 * rows;

            for (Aspect tag : ri.getAspects().getAspectsSorted()) {
                int m = 0;
                if (total / 5 >= rows && (rows > 1 || ri.getAspects().size() < 5)) {
                    m = 1;
                }

                int vx = sx + total % 5 * 20 + shift * m;
                int vy = sy + total / 5 * 20;
                UtilsFX.drawTag(vx, vy, tag, (float) ri.getAspects().getAmount(tag), 0, this.zLevel);
                ++total;
            }

            ItemStack idisp = null;
            if (ri.getRecipeOutput() instanceof ItemStack) {
                idisp = InventoryUtils.cycleItemStack(ri.getRecipeOutput());
            } else {
                idisp = InventoryUtils.cycleItemStack(ri.getRecipeInput()).copy();
                Object[] obj = (Object[]) ri.getRecipeOutput();
                NBTBase tag = (NBTBase) obj[1];
                idisp.setTagInfo((String) obj[0], tag);
            }

            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, idisp, x + 48 + start, y + 28);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, idisp, x + 48 + start, y + 28);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(2884);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(ri.getRecipeInput()), x + 48 + start, y + 94);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(ri.getRecipeInput()).copy().splitStack(1), x + 48 + start, y + 94);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(2896);
            GL11.glEnable(2884);
            int le = ri.getComponents().length;
            ArrayList<Coord2D> coords = new ArrayList<>();
            float pieSlice = (float) (360 / le);
            float currentRot = -90.0F;

            for (int a = 0; a < le; ++a) {
                int xx = (int) (MathHelper.cos(currentRot / 180.0F * (float) Math.PI) * 40.0F) - 8;
                int yy = (int) (MathHelper.sin(currentRot / 180.0F * (float) Math.PI) * 40.0F) - 8;
                currentRot += pieSlice;
                coords.add(new Coord2D(xx, yy));
            }

            total = 0;
            sx = x + 56 + start;
            sy = y + 102;

            for (ItemStack ingredient : ri.getComponents()) {
                RenderHelper.enableGUIStandardItemLighting();
                int vx = sx + coords.get(total).x;
                int vy = sy + coords.get(total).y;
                itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(ingredient), vx, vy);
                itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(ingredient).copy().splitStack(1), vx, vy);
                RenderHelper.disableStandardItemLighting();
                ++total;
            }

            GL11.glEnable(2896);
            GL11.glPopMatrix();
            if (mx >= x + 48 + start && my >= y + 28 && mx < x + 48 + start + 16 && my < y + 28 + 16) {
                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, idisp.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips), mx, my, 11);
            }

            if (mx >= x + 48 + start && my >= y + 94 && mx < x + 48 + start + 16 && my < y + 94 + 16) {
                List addtext = InventoryUtils.cycleItemStack(ri.getRecipeInput()).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(ri.getRecipeInput()));
                if (ref != null && !ref[0].equals(this.research.key)) {
                    addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                    this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                }

                this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
            }

            total = 0;
            sx = x + 56 + start;
            sy = y + 102;

            for (ItemStack ingredient : ri.getComponents()) {
                int vx = sx + coords.get(total).x;
                int vy = sy + coords.get(total).y;
                if (mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16) {
                    List addtext = InventoryUtils.cycleItemStack(ingredient).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                    Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(ingredient));
                    if (ref != null && !ref[0].equals(this.research.key)) {
                        addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                        this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                    }

                    this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
                }

                ++total;
            }

            total = 0;
            rows = (ri.getAspects().size() - 1) / 5;
            shift = (5 - ri.getAspects().size() % 5) * 10;
            sx = x + start + 8;
            sy = y + 164 - 10 * rows;

            for (Aspect tag : ri.getAspects().getAspectsSorted()) {
                int m = 0;
                if (total / 5 >= rows && (rows > 1 || ri.getAspects().size() < 5)) {
                    m = 1;
                }

                int vx = sx + total % 5 * 20 + shift * m;
                int vy = sy + total / 5 * 20;
                if (mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16) {
                    this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my, 11);
                }

                ++total;
            }

            GL11.glPopMatrix();
        }

    }

    private void drawInfusionEnchantingPage(int side, int x, int y, int mx, int my, ResearchPage pageParm) {
        Object tr = pageParm.recipe;
        InfusionEnchantmentRecipe ri = (InfusionEnchantmentRecipe) tr;
        if (ri != null) {
            GL11.glPushMatrix();
            int start = side * 152;
            int level = (int) (1L + System.currentTimeMillis() / 1000L % (long) ri.enchantment.getMaxLevel());
            String text = StatCollector.translateToLocal("recipe.type.infusionenchantment");
            int offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y, 5263440);
            int inst = Math.min(5, ri.instability / 2);
            text = StatCollector.translateToLocal("tc.inst") + " " + StatCollector.translateToLocal("tc.inst." + inst);
            offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y + 194, 5263440);
            text = ri.enchantment.getTranslatedName(level);
            offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y + 24, 7360656);
            int xp = ri.recipeXP * level;
            text = xp + " levels";
            offset = this.fontRendererObj.getStringWidth(text);
            this.fontRendererObj.drawString(text, x + start + 56 - offset / 2, y + 40, 5277776);
            UtilsFX.bindTexture(this.tex2);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslatef((float) (x + start), (float) (y + 20), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            GL11.glTranslatef(0.0F, 19.0F, 0.0F);
            this.drawTexturedModalRect(0, 0, 200, 77, 60, 44);
            GL11.glPopMatrix();
            int mposx = mx;
            int mposy = my;
            int total = 0;
            int rows = (ri.aspects.size() - 1) / 5;
            int shift = (5 - ri.aspects.size() % 5) * 10;
            int sx = x + start + 8;
            int sy = y + 164 - 10 * rows;

            for (Aspect tag : ri.aspects.getAspectsSorted()) {
                int m = 0;
                if (total / 5 >= rows && (rows > 1 || ri.aspects.size() < 5)) {
                    m = 1;
                }

                int vx = sx + total % 5 * 20 + shift * m;
                int vy = sy + total / 5 * 20;
                UtilsFX.drawTag(vx, vy, tag, (float) (ri.aspects.getAmount(tag) * level), 0, this.zLevel);
                ++total;
            }

            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.0F, 100.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(2896);
            GL11.glEnable(2884);
            int le = ri.components.length;
            ArrayList<Coord2D> coords = new ArrayList<>();
            float pieSlice = (float) (360 / le);
            float currentRot = -90.0F;

            for (int a = 0; a < le; ++a) {
                int xx = (int) (MathHelper.cos(currentRot / 180.0F * (float) Math.PI) * 40.0F) - 8;
                int yy = (int) (MathHelper.sin(currentRot / 180.0F * (float) Math.PI) * 40.0F) - 8;
                currentRot += pieSlice;
                coords.add(new Coord2D(xx, yy));
            }

            total = 0;
            sx = x + 56 + start;
            sy = y + 102;

            for (ItemStack ingredient : ri.components) {
                RenderHelper.enableGUIStandardItemLighting();
                int vx = sx + coords.get(total).x;
                int vy = sy + coords.get(total).y;
                itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(ingredient), vx, vy);
                itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(ingredient).copy().splitStack(1), vx, vy);
                ++total;
                RenderHelper.disableStandardItemLighting();
            }

            GL11.glEnable(2896);
            GL11.glPopMatrix();
            total = 0;
            sx = x + 56 + start;
            sy = y + 102;

            for (ItemStack ingredient : ri.components) {
                int vx = sx + coords.get(total).x;
                int vy = sy + coords.get(total).y;
                if (mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16) {
                    List addtext = InventoryUtils.cycleItemStack(ingredient).getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                    Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(ingredient));
                    if (ref != null && !ref[0].equals(this.research.key)) {
                        addtext.add("§8§o" + StatCollector.translateToLocal("recipe.clickthrough"));
                        this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
                    }

                    this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, addtext, mx, my, 11);
                }

                ++total;
            }

            total = 0;
            rows = (ri.aspects.size() - 1) / 5;
            shift = (5 - ri.aspects.size() % 5) * 10;
            sx = x + start + 8;
            sy = y + 164 - 10 * rows;

            for (Aspect tag : ri.aspects.getAspectsSorted()) {
                int m = 0;
                if (total / 5 >= rows && (rows > 1 || ri.aspects.size() < 5)) {
                    m = 1;
                }

                int vx = sx + total % 5 * 20 + shift * m;
                int vy = sy + total / 5 * 20;
                if (mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16) {
                    this.drawCustomTooltip(this, itemRenderer, this.fontRendererObj, Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my, 11);
                }

                ++total;
            }

            GL11.glPopMatrix();
        }

    }

    private void drawTextPage(int side, int x, int y, String text) {
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL11.GL_BLEND);
        this.fr.drawSplitString(text, x - 15 + side * 152, y, 139, 0, this);
        GL11.glPopMatrix();
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        int var4 = (this.width - this.paneWidth) / 2;
        int var5 = (this.height - this.paneHeight) / 2;
        int mx = par1 - (var4 + 261);
        int my = par2 - (var5 + 189);
        if (this.page < this.maxPages - 2 && mx >= 0 && my >= 0 && mx < 14 && my < 10) {
            this.page += 2;
            this.lastCycle = 0L;
            this.cycle = -1;
            Minecraft.getMinecraft().theWorld.playSound(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
        }

        mx = par1 - (var4 - 17);
        my = par2 - (var5 + 189);
        if (this.page >= 2 && mx >= 0 && my >= 0 && mx < 14 && my < 10) {
            this.page -= 2;
            this.lastCycle = 0L;
            this.cycle = -1;
            Minecraft.getMinecraft().theWorld.playSound(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
        }

        if (!history.isEmpty()) {
            mx = par1 - (var4 + 118);
            my = par2 - (var5 + 189);
            if (mx >= 0 && my >= 0 && mx < 20 && my < 12) {
                Minecraft.getMinecraft().theWorld.playSound(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
                Object[] o = history.pop();
                this.mc.displayGuiScreen(new GuiResearchRecipe(ResearchCategories.getResearch((String) o[0]), (Integer) o[1], this.guiMapX, this.guiMapY));
            }
        }

        if (!this.reference.isEmpty()) {
            for (List coords : this.reference) {
                if (par1 >= (Integer) coords.get(0) && par2 >= (Integer) coords.get(1) && par1 < (Integer) coords.get(0) + 16 && par2 < (Integer) coords.get(1) + 16) {
                    Minecraft.getMinecraft().theWorld.playSound(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
                    history.push(new Object[]{this.research.key, this.page});
                    this.mc.displayGuiScreen(new GuiResearchRecipe(ResearchCategories.getResearch((String) coords.get(2)), (Integer) coords.get(3), this.guiMapX, this.guiMapY));
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public Object[] findRecipeReference(ItemStack item) {
        return ThaumcraftApi.getCraftingRecipeKey(this.mc.thePlayer, item);
    }

    public void drawTexturedModalRectScaled(int par1, int par2, int par3, int par4, int par5, int par6, float scale) {
        GL11.glPushMatrix();
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.instance;
        GL11.glTranslatef((float) par1 + (float) par5 / 2.0F, (float) par2 + (float) par6 / 2.0F, 0.0F);
        GL11.glScalef(1.0F + scale, 1.0F + scale, 1.0F);
        var9.startDrawingQuads();
        var9.addVertexWithUV((float) (-par5) / 2.0F, (float) par6 / 2.0F, this.zLevel, (float) (par3) * var7, (float) (par4 + par6) * var8);
        var9.addVertexWithUV((float) par5 / 2.0F, (float) par6 / 2.0F, this.zLevel, (float) (par3 + par5) * var7, (float) (par4 + par6) * var8);
        var9.addVertexWithUV((float) par5 / 2.0F, (float) (-par6) / 2.0F, this.zLevel, (float) (par3 + par5) * var7, (float) (par4) * var8);
        var9.addVertexWithUV((float) (-par5) / 2.0F, (float) (-par6) / 2.0F, this.zLevel, (float) (par3) * var7, (float) (par4) * var8);
        var9.draw();
        GL11.glPopMatrix();
    }

    static class Coord2D {
        int x;
        int y;

        Coord2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        ClientProxy.handleMouseInput(this);
    }
}
