package thaumcraft.api.wands;


import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import thaumcraft.api.aspects.Aspect;

//every rod should have this
@UtilityLikeAbstraction(reason = "if someone created component neither cap nor rod i need to keep compatibility")
public interface IWandRodPropertiesOwnerComponent<Asp extends Aspect> extends
        IAspectCapacityOwnerComponent<Asp>,
        IWandComponentNameOwnerItem {
}
