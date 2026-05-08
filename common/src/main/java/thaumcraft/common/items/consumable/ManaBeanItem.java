package thaumcraft.common.items.consumable;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;
import thaumcraft.api.listeners.manabean.ManaBeanEatContext;
import thaumcraft.api.listeners.manabean.ManaBeanEatManager;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import java.util.List;

import static com.linearity.opentc4.Consts.ManaBeanBlockEntityOrItemStackTagAccessors.OWNING_ASPECT;
import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

public class ManaBeanItem extends BlockItem implements IBonusAspectOwnerItem<Aspect> {
    public ManaBeanItem(Properties properties) {
        super(ThaumcraftBlocks.MANA_BEAN,properties);
    }
    public ManaBeanItem() {
        this(
                new Item.Properties().food(
                new FoodProperties.Builder()
                        .alwaysEat()
                        .fast()
                        .nutrition(1)
                        .saturationMod(0.5F)
                        .build()
                )
        );
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        Aspect aspect = getContainingAspectFromStack(itemStack);
        if (!aspect.isEmpty() && !level.isClientSide()) {
            ManaBeanEatManager.onEatManaBean(new ManaBeanEatContext(
                    aspect,itemStack,level,livingEntity
            ));
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    public @NotNull Aspect getContainingAspectFromStack(ItemStack stack) {
        if (!stack.hasTag()){
            return Aspects.EMPTY;
        }
        var tag = stack.getTag();
        if (tag == null){
            return Aspects.EMPTY;
        }
        return OWNING_ASPECT.readFromCompoundTag(tag);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (Platform.getEnvironment() != Env.CLIENT) {
            return;
        }
        var aspect = getContainingAspectFromStack(itemStack);
        if (aspect.isEmpty()){
            return;
        }
        var player = Minecraft.getInstance().player;
        addAspectDescriptionToList(UnmodifiableAspectList.ofSingle(aspect), player, list);
    }

    @Override
    public @NotNull AspectList<Aspect> getOwningBonusAspects(ItemStack stack) {
        var aspect = getContainingAspectFromStack(stack);
        if (aspect.isEmpty()){
            return UnmodifiableAspectList.of();
        }
        return UnmodifiableAspectList.ofSingle(aspect);
    }

    public void writeOwningBonusAspect(ItemStack stack, Aspect aspect) {
        OWNING_ASPECT.writeToCompoundTag(stack.getOrCreateTag(), aspect);
    }
}
