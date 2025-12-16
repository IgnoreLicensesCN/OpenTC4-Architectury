package tc4tweak.modules.objectTag;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.world.item.Item;
import tc4tweak.modules.FlushableCache;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//congratulations!we dont need itemmeta! consider all of them 0!
class ObjectTagsCache extends FlushableCache<ConcurrentMap<Item, AspectList>> {

//    private static TIntObjectMap<AspectList> bakeSubmap(@SuppressWarnings("rawtypes") Map.Entry<List, AspectList> e) {
//        TIntObjectMap<AspectList> submap = new TIntObjectHashMap<>();
//        Object o = e.getKey().get(1);
//        if (o instanceof Integer) {
//            submap.put((Integer) o, e.getValue());
//        } else if (o instanceof int[]) {
//            int[] metas = (int[]) o;
//            for (int meta : metas) {
//                submap.put(meta, e.getValue());
//            }
//        } else {
//            GetObjectTags.log.error("Unrecognized key in objectTags map! {}", e.getKey());
//        }
//        return submap;
//    }


    @Override
    public ConcurrentMap<Item, AspectList> createCache() {
        ConcurrentHashMap<Item, AspectList> map = new ConcurrentHashMap<>();
        ThaumcraftApi.objectTags.entrySet().parallelStream().forEach(e -> {
            Item key = e.getKey();
            AspectList val = e.getValue();

            map.merge(key, val.copy(),
                    (a, b) -> {
                        OpenTC4.LOGGER.warn("duplicate aspect tag for {}", key);
                        return a.copy().addAll(b);
                    }
            );
        });
        return map;
    }
}
