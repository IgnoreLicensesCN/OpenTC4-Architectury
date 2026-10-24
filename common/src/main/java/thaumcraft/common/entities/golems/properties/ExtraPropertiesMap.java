package thaumcraft.common.entities.golems.properties;

import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ExtraPropertiesMap {
    public ExtraPropertiesMap() {
        this.internalMap = new IdentityHashMap<>();
    }

    public ExtraPropertiesMap(ExtraPropertiesMap another){
        this(another.internalMap);
    }

    public ExtraPropertiesMap(View view){
        this(view.delegate.internalMap);
    }

    public ExtraPropertiesMap(Map<ExtraPropertiesMarker<?>, Object> from) {
        this.internalMap = new IdentityHashMap<>(from);
    }

    protected final Map<ExtraPropertiesMarker<?>, Object> internalMap;

    public <T> T get(ExtraPropertiesMarker<T> marker) {
        return (T) internalMap.get(marker);
    }

    @SuppressWarnings("ConstantValue")
    public <T> T put(ExtraPropertiesMarker<T> marker, @NotNull T value) {
        if (value == null) {
            throw new IllegalArgumentException("cannot put null object");
        }
        return (T) internalMap.put(marker, value);
    }

    public ExtraPropertiesMap copy() {
        return new ExtraPropertiesMap(internalMap);
    }

    public void restoreTo(ExtraPropertiesMap fromMap) {
        this.internalMap.clear();
        this.internalMap.putAll(fromMap.internalMap);
    }

    public void safetyCheckAndFix() {
        this.internalMap.replaceAll((k, v) -> ((ExtraPropertiesMarker<Object>) k).safetyCheck(v));
    }

    public static class View {
        public ExtraPropertiesMap delegate;

        public View(ExtraPropertiesMap map) {
            this.delegate = map;
        }

        public <T> T get(ExtraPropertiesMarker<T> marker) {
            return delegate.get(marker);
        }

        public ExtraPropertiesMap copy() {
            return delegate.copy();
        }

    }
}
