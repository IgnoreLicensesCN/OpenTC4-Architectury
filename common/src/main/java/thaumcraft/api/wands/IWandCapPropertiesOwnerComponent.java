package thaumcraft.api.wands;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;

//every cap should have this
@UtilityLikeAbstraction(reason = "if someone created component neither cap nor rod i need to keep compatibility")
public interface IWandCapPropertiesOwnerComponent extends IVisCostModifierOwnerComponent {
}
