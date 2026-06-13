package thaumcraft.common.items.consumable.aspectowning;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;
import thaumcraft.api.listeners.manabean.ManaBeanEatContext;
import thaumcraft.api.listeners.manabean.ManaBeanEatManager;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import static com.linearity.opentc4.Consts.ManaBeanBlockEntityOrItemStackTagAccessors.OWNING_ASPECT;

public class ManaBeanItem extends BlockItem implements IBonusAspectOwnerItem<Aspect>, IAspectDisplayItem<Aspect> {
    public ManaBeanItem(Properties properties) {
        super(ThaumcraftBlocks.ThaumcraftBlockInstances.MANA_BEAN(),properties);
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
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay(ItemStack stack) {
        return UnmodifiableAspectList.ofSingle(getContainingAspectFromStack(stack));
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
