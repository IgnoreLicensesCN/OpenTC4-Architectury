package thaumcraft.api.wands;

import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


//i wanted enum but maybe other mods wants its type
public class WandSpellEventType {
    public static final WandSpellEventType ON_BLOCK = new WandSpellEventType("ON_BLOCK");
    public static final WandSpellEventType WAND_FOCUS = new WandSpellEventType("WAND_FOCUS");
    public static final WandSpellEventType CRAFTING = new WandSpellEventType("CRAFTING");

    //don't match this so i set to private,maybe other mods want its own type not called other.
    private static final WandSpellEventType OTHER = new WandSpellEventType("OTHER");


    public final String name;
    public WandSpellEventType(String name) {
        this.name = name;
        addType(this);
    }



    private final List<WandSpellEventType> TYPES = new CopyOnWriteArrayList<>();
    private final List<WandSpellEventType> TYPES_VIEW = Collections.unmodifiableList(TYPES);

    @UnmodifiableView
    public List<WandSpellEventType> values() {
        return TYPES_VIEW;
    }

    private void addType(WandSpellEventType type) {
        TYPES.add(type);
    }
}
