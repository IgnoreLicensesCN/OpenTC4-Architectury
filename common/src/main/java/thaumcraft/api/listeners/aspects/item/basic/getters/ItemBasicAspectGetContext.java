package thaumcraft.api.listeners.aspects.item.basic.getters;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;

public class ItemBasicAspectGetContext {
    public @NotNull UnmodifiableAspectList<Aspect> result = UnmodifiableAspectList.EMPTY;
    public boolean doFinishGetting = false;
    public final Item itemToGet;

    public ItemBasicAspectGetContext(Item itemToGet) {
        this.itemToGet = itemToGet;
    }

    public void pickNotEmptyMinimizedOneAsResult(@NotNull UnmodifiableAspectList<Aspect> recommendResult) {
        if (result.isEmpty()) {
            result = recommendResult;
        }
        else if (!recommendResult.isEmpty()) {
            if (result.visSize() > recommendResult.visSize()) {
                result = recommendResult;
            }
        }
    }
}
