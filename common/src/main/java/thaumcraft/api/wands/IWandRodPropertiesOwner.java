package thaumcraft.api.wands;


import thaumcraft.api.aspects.Aspect;

//every rod should have this
public interface IWandRodPropertiesOwner<Asp extends Aspect> extends IAspectCapacityOwner<Asp>, IWandComponentNameOwner {
}
