package thaumcraft.common.aspects;

import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.items.ThaumcraftItemInstances;

import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.registerItemBasicAspects;

public class ThaumcraftItemAspects {

    public static void init(){
        initBasic();
    }
    public static void initBasic(){
        //TODO:All of them in vanilla tc4 and maybe i have to add some for newer mc items
        registerItemBasicAspects(ThaumcraftItemInstances.WAND_CASTING(), new LinkedHashAspectList<>() {{
            this.addAll(Aspects.TOOL, 3);
            this.addAll(Aspects.MAGIC, 3);
        }});
        registerItemBasicAspects(ThaumcraftItemInstances.STAFF_CASTING(), new LinkedHashAspectList<>() {{
            this.addAll(Aspects.TOOL, 6);
            this.addAll(Aspects.MAGIC, 6);
        }});
        registerItemBasicAspects(ThaumcraftItemInstances.SCEPTRE_CASTING(), new LinkedHashAspectList<>() {{
            this.addAll(Aspects.TOOL, 3);
            this.addAll(Aspects.MAGIC, 3);
            this.addAll(Aspects.CRAFT, 3);
        }});
    }
}
