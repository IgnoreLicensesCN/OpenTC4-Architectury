package thaumcraft.common.entities.golems.configuration;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

public class GolemConfigurationProviderMap {
    private final Map<GolemConfigurationKey<?>, GolemConfigurationProvider<?>> map = new IdentityHashMap<>();

    public <T extends GolemConfigurationProvider<?>> T get(GolemConfigurationKey<T> type){
        return (T) map.get(type);
    };

    public <T extends GolemConfigurationProvider<?>> T put(GolemConfigurationKey<T> type){
        return (T) map.put(type, type.provider);
    }


    public Collection<GolemConfigurationProvider<?>> values(){
        return map.values();
    }

    public void clear(){
        map.clear();
    }

    public static class View {
        private final GolemConfigurationProviderMap delegate;
        public View(GolemConfigurationProviderMap delegate) {
            this.delegate = delegate;
        }
        public <T extends GolemConfigurationProvider<?>> T get(GolemConfigurationKey<T> type){
            return delegate.get(type);
        };
        public Collection<GolemConfigurationProvider<?>> values(){
            return delegate.values();
        }
    }

}
