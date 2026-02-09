package thaumcraft.api.listeners.aspects.item.basic;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ItemAdditionalBasicAspectRegistered {
    private static final Map<Item, UnmodifiableAspectList<Aspect>> REGISTERED_ADDITIONAL_BASIC_ASPECTS_FOR_ITEMS = new HashMap<>() {{
        put(Items.AIR, UnmodifiableAspectList.EMPTY);
    }};
    private static final Map<TagKey<Item>, UnmodifiableAspectList<Aspect>> REGISTERED_ADDITIONAL_BASIC_ASPECTS_FOR_TAGS = new HashMap<>();


    public static void registerAdditionalBasicAspects(Item item, @NotNull UnmodifiableAspectList<Aspect> aspects) {
        REGISTERED_ADDITIONAL_BASIC_ASPECTS_FOR_ITEMS.merge(
                item,
                aspects,
                UnmodifiableAspectList::combine
        );
    }

    public static void registerAdditionalBasicAspects(TagKey<Item> tag, @NotNull UnmodifiableAspectList<Aspect> aspects) {
        REGISTERED_ADDITIONAL_BASIC_ASPECTS_FOR_TAGS.merge(tag, aspects, UnmodifiableAspectList::combine);
    }

    public static @NotNull UnmodifiableAspectList<Aspect> getAdditionalBasicAspects(@NotNull Item item) {
        AtomicReference<UnmodifiableAspectList<Aspect>> resultAtomic = new AtomicReference<>(
                REGISTERED_ADDITIONAL_BASIC_ASPECTS_FOR_ITEMS.getOrDefault(item, UnmodifiableAspectList.EMPTY));
        item.builtInRegistryHolder().tags().forEach(
                tag -> {
                    var forTag = REGISTERED_ADDITIONAL_BASIC_ASPECTS_FOR_TAGS.getOrDefault(tag,UnmodifiableAspectList.EMPTY);
                    if (!forTag.isEmpty()) {
                        resultAtomic.set(resultAtomic.get()
                                .addAllAsNew(forTag));
                    }
                }
        );
        return resultAtomic.get();
    }

}
