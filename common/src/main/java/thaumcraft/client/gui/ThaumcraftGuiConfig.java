package thaumcraft.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import thaumcraft.common.config.Config;

import static thaumcraft.common.config.Config.*;

public class ThaumcraftGuiConfig extends GuiConfig {
   public ThaumcraftGuiConfig(GuiScreen parent) {
      super(parent, getConfigElements(), "Thaumcraft", false, false, GuiConfig.getAbridgedConfigPath(Config.config.toString()));
   }

   private static List<IConfigElement> getConfigElements() {
      List<IConfigElement> list = new ArrayList<>();
      list.addAll((new ConfigElement(Config.config.getCategory("general"))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory(CATEGORY_SPAWN))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory(CATEGORY_GEN))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory(CATEGORY_REGEN))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory(CATEGORY_RESEARCH))).getChildElements());
      return list;
   }
}
