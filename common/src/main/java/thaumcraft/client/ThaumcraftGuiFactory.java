package thaumcraft.client;

import cpw.mods.fml.client.IModGuiFactory;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import thaumcraft.client.gui.ThaumcraftGuiConfig;

public class ThaumcraftGuiFactory implements IModGuiFactory {
   public void initialize(Minecraft minecraftInstance) {
   }

   public Class<? extends GuiScreen> mainConfigGuiClass() {
      return ThaumcraftGuiConfig.class;
   }

   public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
      return null;
   }

   public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element) {
      return null;
   }
}
