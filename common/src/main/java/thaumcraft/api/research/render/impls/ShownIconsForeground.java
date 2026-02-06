package thaumcraft.api.research.render.impls;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.research.render.ItemStackShownIconForeground;
import thaumcraft.api.research.render.SimpleShownIconForeground;
import thaumcraft.common.items.ThaumcraftItems;

public class ShownIconsForeground {
    public static final SimpleShownIconForeground RESEARCH_EXPERTISE_FOREGROUND_ICON =
            new SimpleShownIconForeground(
                    new ResourceLocation("thaumcraft", "textures/misc/r_researcher1.png")
            );
    public static final SimpleShownIconForeground RESEARCH_MASTERY_FOREGROUND_ICON =
            new SimpleShownIconForeground(
                    new ResourceLocation("thaumcraft", "textures/misc/r_researcher2.png")
            );
    public static final ItemStackShownIconForeground RESEARCH_FOREGROUND_ICON =
            new ItemStackShownIconForeground(
                    new ItemStack(ThaumcraftItems.INK_WELL)
            );
}
