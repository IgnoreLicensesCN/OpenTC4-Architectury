package thaumcraft.common.entities.golems.properties;

public class ExtraPropertiesValueMap extends ExtraPropertiesMap {
    public ExtraPropertiesValueMap() {
        super(ExtraPropertiesDefinition.REGISTERED_DEFAULT_VALUES_MAP_VIEW.delegate.internalMap);
    }

    public ExtraPropertiesValueMap(ExtraPropertiesDefinitionMap definitionMap) {
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

    public void setTo(ExtraPropertiesDefinitionMap definitionMap) {
        this.internalMap.putAll(definitionMap.internalMap);
    }
}
