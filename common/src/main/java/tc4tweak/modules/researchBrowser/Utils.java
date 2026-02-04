package tc4tweak.modules.researchBrowser;

import net.minecraft.resources.ResourceLocation;
import tc4tweak.CommonUtils;
import thaumcraft.client.gui.GuiResearchBrowser;

import java.lang.reflect.Field;

class Utils {
    private static final int selectedCategoryIdent = 21;
    private static final Field f_selectedCategory = CommonUtils.getField(GuiResearchBrowser.class, "selectedCategory", selectedCategoryIdent);

    static ResourceLocation getActiveCategory() {
        return GuiResearchBrowser.selectedCategory;
    }
}
