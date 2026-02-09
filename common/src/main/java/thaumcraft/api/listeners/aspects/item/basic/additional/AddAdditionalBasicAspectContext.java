package thaumcraft.api.listeners.aspects.item.basic.additional;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;

public class AddAdditionalBasicAspectContext {
    public final Item item;
    public final @NotNull UnmodifiableAspectList<Aspect> basicAspects;
    public @NotNull UnmodifiableAspectList<Aspect> additionalAspects = UnmodifiableAspectList.EMPTY;
    public boolean doReturn = false;

    public AddAdditionalBasicAspectContext(Item item, @NotNull UnmodifiableAspectList<Aspect> basicAspects) {
        this.item = item;
        this.basicAspects = basicAspects;
    }

    public void submitAdditional(UnmodifiableAspectList<Aspect> additionalToAdd){
        if(additionalToAdd.isEmpty()){
            return;
        }
        this.additionalAspects.addAllAsNew(additionalToAdd);
    }

}
