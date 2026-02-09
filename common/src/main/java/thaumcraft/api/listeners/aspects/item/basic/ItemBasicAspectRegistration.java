package thaumcraft.api.listeners.aspects.item.basic;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.UnmodifiableAspectList;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class ItemBasicAspectRegistration {
    private static final Map<Item, UnmodifiableAspectList<Aspect>> REGISTERED_BASIC_ASPECTS_FOR_ITEMS = new HashMap<>() {{
        put(
                Items.AIR, UnmodifiableAspectList.EMPTY);
    }};
    private static final Map<TagKey<Item>, UnmodifiableAspectList<Aspect>> REGISTERED_BASIC_ASPECTS_FOR_TAGS = new HashMap<>();

    private static final Map<Item, UnmodifiableAspectList<Aspect>> BASIC_ASPECTS_CALCULATED_WITH_ADDITIONAL_COMPLETELY_FROM_REGISTERED_CACHE = new HashMap<>();
    private static final Set<Item> ITEMS_WITH_REGISTERED_BASIC_ASPECT_INTERNAL = new HashSet<>() {};
    public static final Set<Item> ITEMS_WITH_REGISTERED_BASIC_ASPECTS = Collections.unmodifiableSet(ITEMS_WITH_REGISTERED_BASIC_ASPECT_INTERNAL);

    @NotNull("null -> empty")
    static UnmodifiableAspectList<Aspect> getCalculatedRegisteredBasicAspect(@NotNull Item item) {
        return BASIC_ASPECTS_CALCULATED_WITH_ADDITIONAL_COMPLETELY_FROM_REGISTERED_CACHE.computeIfAbsent(
                item,i -> {
                    AtomicReference<UnmodifiableAspectList<Aspect>> basic = new AtomicReference<>(
                            REGISTERED_BASIC_ASPECTS_FOR_ITEMS.getOrDefault(item, UnmodifiableAspectList.EMPTY)
                    );
                    if (basic.get().isEmpty()) {
                        for (var entry:REGISTERED_BASIC_ASPECTS_FOR_TAGS.entrySet()){
                            var tag = entry.getKey();
                            var basicForTag = entry.getValue();
                            if (i.builtInRegistryHolder().is(tag)){
                                if (basic.get().isEmpty()){
                                    basic.set(basicForTag);
                                }else if (!basicForTag.isEmpty()){
                                    if (basic.get().visSize() > basicForTag.visSize()){
                                        basic.set(basicForTag);
                                    }
                                }
                            }
                        }
                    }
                    return basic.get();
                }
        );
    }
    public static void registerItemBasicAspects(Item item, @NotNull AspectList<Aspect> aspects) {
        if (REGISTERED_BASIC_ASPECTS_FOR_ITEMS.containsKey(item)) {
            throw new IllegalArgumentException("Item " + item + " is already registered");
        }
        REGISTERED_BASIC_ASPECTS_FOR_ITEMS.put(item, new UnmodifiableAspectList<>(aspects));
        ITEMS_WITH_REGISTERED_BASIC_ASPECT_INTERNAL.add(item);
    }

    public static void registerItemTagBasicAspects(TagKey<Item> tag, @NotNull AspectList<Aspect> aspects) {
        if (REGISTERED_BASIC_ASPECTS_FOR_TAGS.containsKey(tag)) {
            throw new RuntimeException(String.format("Tag %s already registered", tag));
        }
        REGISTERED_BASIC_ASPECTS_FOR_TAGS.put(tag, new UnmodifiableAspectList<>(aspects));
        var items = platformUtils.getItemsFromTag(tag);
        if (items == null) {
            return;
        }
        items.forEach(i -> {
            BASIC_ASPECTS_CALCULATED_WITH_ADDITIONAL_COMPLETELY_FROM_REGISTERED_CACHE.remove(i);
            ITEMS_WITH_REGISTERED_BASIC_ASPECT_INTERNAL.add(i);
        });
    }

    static void onDatapackReload(){
        BASIC_ASPECTS_CALCULATED_WITH_ADDITIONAL_COMPLETELY_FROM_REGISTERED_CACHE.clear();

    }



}
