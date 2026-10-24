package thaumcraft.common.entities.golems.properties;

public class ExtraAbilityDataMap extends ExtraPropertiesMap {
    public ExtraAbilityDataMap() {
        super(ExtraPropertiesDefinition.REGISTERED_DEFAULT_VALUES_MAP_VIEW.delegate.internalMap);
    }

    public ExtraAbilityDataMap(ExtraPropertiesDefinitionMap definitionMap) {
        super(definitionMap.internalMap);
    }

    @Override
    public <T> T get(ExtraPropertiesMarker<T> marker) {
        var result = super.get(marker);
        if (result == null) {
            throw new IllegalArgumentException("Marker " + marker + " is not registered");
        }
        return result;
    }

    public <T> T getOrDefault(ExtraPropertiesMarker<T> marker, T defaultValue) {
        var result = super.get(marker);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    public void setTo(ExtraPropertiesDefinitionMap definitionMap) {
        this.internalMap.putAll(definitionMap.internalMap);
    }
}
