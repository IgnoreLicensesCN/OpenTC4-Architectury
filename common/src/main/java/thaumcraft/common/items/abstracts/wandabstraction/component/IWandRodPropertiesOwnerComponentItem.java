package thaumcraft.common.items.abstracts.wandabstraction.component;


import thaumcraft.api.aspects.Aspect;

//every rod should have this

public interface IWandRodPropertiesOwnerComponentItem<Asp extends Aspect> extends
        IAspectCapacityOwnerComponentItem<Asp>,
        IWandComponentNameOwnerComponentItem {
}
