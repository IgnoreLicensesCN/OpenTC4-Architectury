package thaumcraft.common.researches;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;

public class ThaumcraftResearchCategories {
    public static final ResearchCategory BASICS = new ResearchCategory(
            new ResearchCategoryResourceLocation(Thaumcraft.MOD_ID, "basics"),
            new ResourceLocation("thaumcraft", "textures/items/thaumonomiconcheat.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png")
    );
    public static final ResearchCategory THAUMATURGY = new ResearchCategory(
            new ResearchCategoryResourceLocation(Thaumcraft.MOD_ID, "thaumaturgy"),
            new ResourceLocation("thaumcraft", "textures/misc/r_thaumaturgy.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png")
    );
    public static final ResearchCategory ALCHEMY = new ResearchCategory(
            new ResearchCategoryResourceLocation(Thaumcraft.MOD_ID, "alchemy"),
            new ResourceLocation("thaumcraft", "textures/misc/r_crucible.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png")
    );
    public static final ResearchCategory ARTIFICE = new ResearchCategory(
            new ResearchCategoryResourceLocation(Thaumcraft.MOD_ID, "artifice"),
            new ResourceLocation("thaumcraft", "textures/misc/r_artifice.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png")
    );
    public static final ResearchCategory GOLEMANCY = new ResearchCategory(
            new ResearchCategoryResourceLocation(Thaumcraft.MOD_ID, "goldemancy"),
            new ResourceLocation("thaumcraft", "textures/misc/r_golemancy.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png")
    );
    public static final ResearchCategory ELDRITCH = new ResearchCategory(
            new ResearchCategoryResourceLocation(Thaumcraft.MOD_ID, "eldritch"),
            new ResourceLocation("thaumcraft", "textures/misc/r_eldritch.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchbackeldritch.png")
    );
}
