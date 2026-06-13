package thaumcraft.api.research;

import thaumcraft.api.research.impl.basics.AspectDisplayingResearch;
import thaumcraft.api.research.impl.basics.ResearchBasicResearch;
import thaumcraft.api.research.impl.basics.ResearchExpertiseResearch;
import thaumcraft.api.research.impl.basics.ResearchMasteryResearch;
import thaumcraft.api.research.impl.basics.CrimsonResearch;
import thaumcraft.api.research.impl.eldritch.FocusPrimalResearch;
import thaumcraft.api.research.impl.thaumaturgy.BasicThaumaturgyResearch;


public class ThaumcraftResearches {
    public static AspectDisplayingResearch ASPECTS;
    public static ResearchBasicResearch RESEARCH_BASIC;
    public static ResearchExpertiseResearch RESEARCH_EXPERTISE;
    public static ResearchMasteryResearch RESEARCH_MASTERY;
    public static ResearchItem CRIMSON;

    public static ResearchItem NODE_PRESERVE;
    public static ResearchItem NODE_TAKE_IT_ALL;
    public static ResearchItem MASTER_NODE_TRAPPING;

    public static FocusPrimalResearch FOCUS_PRIMAL;
    public static ResearchItem ENTER_OUTER;
    public static ResearchItem PRIME_PEARL_NODE_CONTROL;

    public static ResearchItem FOCAL_MANIPULATION;
    public static ResearchItem SCEPTRE_CRAFTING;
    public static BasicThaumaturgyResearch BASIC_THAUMATURGY;
    public static ResearchItem STAFF_CRAFTING;

    public static void onDatapackReloaded() {
        ResearchItem.onDatapackReload();
        ASPECTS = new AspectDisplayingResearch();
        RESEARCH_BASIC = new ResearchBasicResearch();
        RESEARCH_EXPERTISE = new ResearchExpertiseResearch();
        RESEARCH_MASTERY = new ResearchMasteryResearch();
        CRIMSON = new CrimsonResearch();

        NODE_PRESERVE = ;
        NODE_TAKE_IT_ALL = ;
        MASTER_NODE_TRAPPING = ;

        FOCUS_PRIMAL = new FocusPrimalResearch();
        ENTER_OUTER = ;
        PRIME_PEARL_NODE_CONTROL = ;

        FOCAL_MANIPULATION = ;
        SCEPTRE_CRAFTING = ;
        BASIC_THAUMATURGY = new BasicThaumaturgyResearch();
        STAFF_CRAFTING = ;

    }
}
