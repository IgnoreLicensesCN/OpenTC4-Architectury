package tc4tweak.modules.objectTag;

//congratulations!we dont need itemmeta! consider all of them 0!
class ObjectTagsCache /*extends FlushableCache<ConcurrentMap<Item, AspectList<Aspect>>> */{

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
//
//
//    @Override
//    public ConcurrentMap<Item, AspectList<Aspect>> createCache() {
//        ConcurrentHashMap<Item, AspectList<Aspect>> map = new ConcurrentHashMap<>();
//        ThaumcraftApi.objectTags.entrySet().parallelStream().forEach(e -> {
//            Item key = e.getKey();
//            AspectList<Aspect> val = e.getValue();
//
//            map.merge(key, val.copy(),
//                    (a, b) -> {
//                        OpenTC4.LOGGER.warn("duplicate aspect tag for {}", key);
//                        return a.copy().addAll(b);
//                    }
//            );
//        });
//        return map;
//    }
}
