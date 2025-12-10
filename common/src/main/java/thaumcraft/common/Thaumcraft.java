package com.linearity.opentc4;

import thaumcraft.common.lib.research.ResearchManager;

public class Thaumcraft {

    public static Thaumcraft instance = new Thaumcraft();
    ResearchManager researchManager;
    public ThaumcraftWorldGenerator worldGen;
    public EventHandlerWorld worldEventHandler;
    public EventHandlerNetwork networkEventHandler;
    public ServerTickEventsFML serverTickEvents;
    public EventHandlerEntity entityEventHandler;
    public EventHandlerRunic runicEventHandler;
    public RenderEventHandler renderEventHandler;
}
