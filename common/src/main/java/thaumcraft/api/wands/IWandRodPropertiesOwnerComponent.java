package thaumcraft.api.wands;


import thaumcraft.api.aspects.Aspect;

//every rod should have this

public interface IWandRodPropertiesOwnerComponent<Asp extends Aspect> extends
        IAspectCapacityOwnerComponent<Asp>,
        IWandComponentNameOwnerItem {
}
