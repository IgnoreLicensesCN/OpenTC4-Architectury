package thaumcraft.common.runicshield.shieldtypes;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;

public class ChargedRunicShield extends AbstractRunicShieldType<Void> {
    public ChargedRunicShield(RunicShieldTypeResourceLocation shieldTypeResourceLocation,int priority) {
        super(shieldTypeResourceLocation,priority);
    }

    @Override
    public @Nullable CompoundTagAccessor<Void> getOwningTagAccessor() {
        return null;
    }

    public Component getShieldName(){
        if (shieldName == null){
            shieldName = Component.translatable("runic_shield.thaumcraft.runic_charged").withStyle(ChatFormatting.YELLOW);
        }
        return shieldName;
    }
}
