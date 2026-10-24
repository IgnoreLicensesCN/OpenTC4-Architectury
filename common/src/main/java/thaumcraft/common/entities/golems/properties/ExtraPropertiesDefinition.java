package thaumcraft.common.entities.golems.properties;

public class ExtraPropertiesDefinition {

    private static final ExtraPropertiesDefinitionMap REGISTERED_DEFAULT_VALUES_MAP = new ExtraPropertiesDefinitionMap();
    public static final ExtraPropertiesMap.View REGISTERED_DEFAULT_VALUES_MAP_VIEW = new ExtraPropertiesMap.View(REGISTERED_DEFAULT_VALUES_MAP);

    public static <MarkedClass> void registerDefaultExtraProperties(ExtraPropertiesMarker<MarkedClass> propertyMarker, MarkedClass defaultValue) {
        REGISTERED_DEFAULT_VALUES_MAP.put(propertyMarker, defaultValue);
    }

}
