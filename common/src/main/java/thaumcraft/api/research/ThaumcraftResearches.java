package thaumcraft.api.research;

import thaumcraft.api.research.impl.basics.AspectDisplayingResearch;
import thaumcraft.api.research.impl.basics.ResearchBasicResearch;
import thaumcraft.api.research.impl.basics.ResearchExpertiseResearch;
import thaumcraft.api.research.impl.basics.ResearchMasteryResearch;
import thaumcraft.api.research.impl.basics.CrimsonResearch;
import thaumcraft.api.research.impl.eldritch.FocusPrimalResearch;
import thaumcraft.api.research.impl.thaumaturgy.BasicThaumaturgyResearch;


public class ThaumcraftResearches {
    public static final AspectDisplayingResearch ASPECTS = new AspectDisplayingResearch();
    public static final ResearchBasicResearch RESEARCH_BASIC = new ResearchBasicResearch();
    public static final ResearchExpertiseResearch RESEARCH_EXPERTISE = new ResearchExpertiseResearch();
    public static final ResearchMasteryResearch RESEARCH_MASTERY = new ResearchMasteryResearch();

    public static final ResearchItem CRIMSON = new CrimsonResearch();

    public static final ResearchItem NODE_PRESERVE = ;
    public static final ResearchItem NODE_TAKE_IT_ALL = ;
    public static final ResearchItem MASTER_NODE_TRAPPING = ;
    public static final FocusPrimalResearch FOCUS_PRIMAL = new FocusPrimalResearch();

    public static final ResearchItem ENTER_OUTER = ;
    public static final ResearchItem PRIME_PEARL_NODE_CONTROL = ;
    public static final ResearchItem FOCAL_MANIPULATION = ;

    public static final ResearchItem SCEPTRE_CRAFTING = ;
    public static final BasicThaumaturgyResearch BASIC_THAUMATURGY = new BasicThaumaturgyResearch();
    public static final ResearchItem STAFF_CRAFTING = ;

    public static void init(){

    }
}
