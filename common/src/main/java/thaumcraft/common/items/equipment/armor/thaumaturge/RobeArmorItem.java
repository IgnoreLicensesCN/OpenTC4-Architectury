package thaumcraft.common.items.equipment.armor.thaumaturge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

import static net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL;

public class RobeArmorItem extends DyeableArmorItem implements IAugmentationRunicShieldProviderItem, IVisDiscountGear {
    public RobeArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public RobeArmorItem(Type type){
        this(ThaumcraftItems.ToolAndArmorMaterial.ROBE,
                type,
                new Properties().stacksTo(1)
        );
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var clickedState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        if (clickedState.getBlock() == Blocks.WATER_CAULDRON){
            int waterLevel = clickedState.getValue(LEVEL) -1;
            if (waterLevel == 0){
                useOnContext.getLevel().setBlockAndUpdate(useOnContext.getClickedPos(), Blocks.CAULDRON.defaultBlockState());
            }else {
                useOnContext.getLevel().setBlockAndUpdate(useOnContext.getClickedPos(), clickedState.setValue(LEVEL, waterLevel));
            }
            clearColor(useOnContext.getItemInHand());
        }
        return super.useOn(useOnContext);
    }

    @Override
    public int getColor(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTagElement("display");
        return compoundTag != null && compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 0x6a3880;
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        return type == Type.BOOTS?1:2;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,null);
        addShieldToolTip(itemStack, level, list, tooltipFlag);
    }
}
