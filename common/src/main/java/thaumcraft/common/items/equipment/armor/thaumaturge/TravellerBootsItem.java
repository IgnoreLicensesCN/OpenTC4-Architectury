package thaumcraft.common.items.equipment.armor.thaumaturge;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;
import java.util.UUID;

import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.*;


public class TravellerBootsItem extends ArmorItem implements IAugmentationRunicShieldProviderItem {
    public TravellerBootsItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public TravellerBootsItem() {
        this(ThaumcraftToolAndArmorMaterial.SPECIAL, Type.BOOTS, new Properties().stacksTo(1).durability(350).rarity(Rarity.RARE));
    }

    public static final UUID TRAVELLER_STEP_BOOST_UUID = UUID.fromString("b2688e00-3eca-46b9-b774-52bd632e5939");
    public static final UUID TRAVELLER_FLYING_SPEED_CONTROL = UUID.fromString("9f92c252-cf88-48bd-8a93-9ce366fe2ebb");
    public static final UUID TRAVELLER_JUMP_MOTION = UUID.fromString("2d781df7-efd4-4a1f-b1e3-2c0e09787a55");
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();

        if (slot == this.getType().getSlot()) {
            modifiers.put(
                    STEP_HEIGHT_ADDITION_NOT_SNEAKING(), new AttributeModifier(
                    TRAVELLER_STEP_BOOST_UUID,
                    "Traveller boots step boost",
                    0.5,
                    AttributeModifier.Operation.ADDITION
            ));
            modifiers.put(
                    FLYING_SPEED_CONTROL_OVERRIDE(),new AttributeModifier(
                            TRAVELLER_FLYING_SPEED_CONTROL,
                            "Traveller boots flying horizontal speed control",
                            0.05,
                            AttributeModifier.Operation.ADDITION
                    )
            );
            modifiers.put(
                    JUMP_Y_VELOCITY_ADDITION_NOT_SNEAKING(),new AttributeModifier(
                            TRAVELLER_JUMP_MOTION,
                            "Traveller boots jump motion when not sneaking",
                            0.275,//from add motion Y
                            AttributeModifier.Operation.ADDITION
                    )
            );
        }

        return modifiers;
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addShieldToolTip(itemStack, level, list, tooltipFlag);
    }

    public static final Vec3 inputVecForward = new Vec3(0, 0, 1);
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (i != 36 + Type.BOOTS.getSlot().getIndex()){
            return;
        }
        if (entity.onGround()){
            float bonus = 0.055F;
            if (entity.isInWater()) {
                bonus /= 4.0F;
            }

            entity.moveRelative(bonus,inputVecForward);
        }

        if (entity.fallDistance > 0.0F) {
            entity.fallDistance -= 0.25F;
        }

    }@Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.FEET;
    }
}
