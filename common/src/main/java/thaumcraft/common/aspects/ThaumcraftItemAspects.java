package thaumcraft.common.aspects;

import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.Aspects;

import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.registerItemBasicAspects;
import static thaumcraft.common.items.ThaumcraftItems.*;

public class ThaumcraftItemAspects {

    public static void init(){
        initBasic();
    }
    public static void initBasic(){
        //TODO:All of them in vanilla tc4 and maybe i have to add some for newer mc items
        registerItemBasicAspects(WAND_CASTING,new LinkedHashAspectList<>(){{this.addAll(Aspects.TOOL,3);this.addAll(Aspects.MAGIC,3);}});
        registerItemBasicAspects(STAFF_CASTING,new LinkedHashAspectList<>(){{this.addAll(Aspects.TOOL,6);this.addAll(Aspects.MAGIC,6);}});
        registerItemBasicAspects(SCEPTRE_CASTING,new LinkedHashAspectList<>(){{this.addAll(Aspects.TOOL,3);this.addAll(Aspects.MAGIC,3);this.addAll(Aspects.CRAFT,3);}});
    }
}
