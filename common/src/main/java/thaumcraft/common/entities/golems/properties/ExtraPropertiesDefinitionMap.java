package thaumcraft.common.entities.golems.properties;

import java.util.Map;

public class ExtraPropertiesDefinitionMap extends ExtraPropertiesMap {

    public ExtraPropertiesDefinitionMap() {
        super();
    }

    public ExtraPropertiesDefinitionMap(ExtraPropertiesMap another) {
        super(another);
    }

    public ExtraPropertiesDefinitionMap(View view) {
        super(view);
    }

    public ExtraPropertiesDefinitionMap(Map<ExtraPropertiesMarker<?>, Object> from) {
        super(from);
    }
}
