package thaumcraft.common.items.baubles.amulet.visamulet;

import com.linearity.opentc4.annotations.ModifiableCopy;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.ArrayCentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.LinkedHashCentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.ICentiVisContainerItem;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.math.BigDecimal;
import java.util.List;

import static com.linearity.opentc4.Consts.VisAmuletCompoundTagAccessors.VIS_AMULET_OWING_VIS;

public class VisAmuletItem extends AccessoryItem implements
        IAugmentationRunicShieldProviderItem,
        ICentiVisContainerItem<Aspect>
{
    public VisAmuletItem(Properties properties) {
        super(properties);
    }
    public VisAmuletItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean tryCastAspectClass(Class<? extends Aspect> aspClass) {
        return true;
    }

    @Override
    public @ModifiableCopy CentiVisList<Aspect> getAllCentiVisOwning(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) {
            return new ArrayCentiVisList<>();
        }
        return VIS_AMULET_OWING_VIS.readFromCompoundTag(tag);
    }

    @Override
    public void storeCentiVisOwning(ItemStack itemStack, CentiVisList<Aspect> aspects) {
        VIS_AMULET_OWING_VIS.writeToCompoundTag(itemStack.getOrCreateTag(), aspects);
    }

    private static final @Unmodifiable CentiVisList<Aspect> CAPACITY = UnmodifiableCentiVisList.of(
            Aspects.AIR,2500,
            Aspects.WATER,2500,
            Aspects.FIRE,2500,
            Aspects.EARTH,2500,
            Aspects.ENTROPY,2500,
            Aspects.ORDER,2500
    );
    @Override
    public @UnmodifiableView CentiVisList<Aspect> getAllCentiVisCapacity(ItemStack stack) {
        return CAPACITY;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("item.ItemAmuletVis.text").withStyle(ChatFormatting.AQUA));
        var centiVisOwning = getAllCentiVisOwning(itemStack);
        getAllCentiVisCapacity(itemStack).forEach(
                (aspect, amount) -> {
                    int currentOwning = centiVisOwning.get(aspect);
                    var currentOwningString = new BigDecimal(currentOwning).movePointLeft(2).toPlainString();
                    var currentCapacityString = new BigDecimal(amount).movePointLeft(2).toPlainString();

                    var displayAmountString = currentOwningString + "/" + currentCapacityString;
                    list.add(aspect.getNameColored().copy().append(
                            Component.literal(" x " + displayAmountString).withStyle(s -> s.withColor(aspect.getColor()))
                    ));
                }
        );
    }

    protected int getMaxCentiVisToStoreEachTime(ItemStack selfStack) {
        return 5;
    }

    @Override
    public void tick(ItemStack selfStack, SlotReference reference) {
        super.tick(selfStack, reference);
        var living = reference.entity();
        if (living == null){
            return;
        }
        if (living.tickCount % 5 != 0){
            return;
        }
        var selfStackOwningAspect = getAllCentiVisOwning(selfStack);
        boolean[] selfModified = {false};
        var holdingStacks = new ItemStack[]{living.getMainHandItem(),living.getOffhandItem()};
        for (var holdingStack : holdingStacks) {
            if (!holdingStack.isEmpty()
                    && holdingStack.getItem() instanceof ICentiVisContainerItem<? extends Aspect> containerToFillNotCasted) {
                var containerToFill = (ICentiVisContainerItem<Aspect>) containerToFillNotCasted;
                selfModified[0] |= fillCentiVisContainerHolding(selfStack, holdingStack, containerToFill, selfStackOwningAspect);
            }
        }

        var selfCapacity = getAllCentiVisCapacity(selfStack);
        var drainPos = living.blockPosition();
        var level = living.level();
        selfModified[0] |= drainCentiVisFromNearVisNet(selfCapacity, selfStackOwningAspect, level, drainPos);
        if (selfModified[0]){
            this.storeCentiVisOwning(selfStack,selfStackOwningAspect);
        }
    }

    //true if modified self
    protected boolean drainCentiVisFromNearVisNet(
            CentiVisList<Aspect> selfCapacity,
            CentiVisList<Aspect> selfStackOwningAspect,
            Level level,
            BlockPos drainPos
    ) {
        boolean[] selfModified = {false};
        selfCapacity.forEach(
                (aspect,capacity) -> {
                    var canFill = capacity - selfStackOwningAspect.get(aspect);
                    if (canFill > 0){
                        int drained = VisNetHandler.drainCentiVis(level, drainPos,aspect,canFill);
                        if (drained > 0){
                            selfStackOwningAspect.addAll(aspect, drained);
                            selfModified[0] = true;
                        }
                    }
                }
        );
        return selfModified[0];
    }
    //true if modified self
    protected boolean fillCentiVisContainerHolding(
            ItemStack selfStack,
            ItemStack holdingStack,
            ICentiVisContainerItem<Aspect> containerToFill,
            CentiVisList<Aspect> selfStackOwningAspect
    ) {
        boolean[] selfModified = {false};
        CentiVisList<Aspect> centiVisToMigrate = new ArrayCentiVisList<>();

        var allVisToSaveForFilling = containerToFill.getAllCentiVisOwning(holdingStack);
        var capacity = containerToFill.getAllCentiVisCapacity(holdingStack);

        CentiVisList<Aspect> centiVisListNeededToFill = new LinkedHashCentiVisList<>(allVisToSaveForFilling.size(),1.F);

        allVisToSaveForFilling.forEach((aspect,vis)->{
            var remainingRoom = capacity.getOrDefault(aspect,0) - vis;
            if (remainingRoom > 0){
                centiVisListNeededToFill.put(aspect,remainingRoom);
            }
        });

        selfStackOwningAspect.forEach(
                (canMigrateAspect, canMigrateAmount) -> {
                    int canFill = centiVisListNeededToFill.get(canMigrateAspect);
                    if (canFill > 0){
                        int shouldFill = Math.min(canFill, canMigrateAmount);
                        shouldFill = Math.min(shouldFill, getMaxCentiVisToStoreEachTime(selfStack));
                        if (shouldFill > 0){
                            centiVisToMigrate.addAll(canMigrateAspect, shouldFill);
                            selfModified[0] = true;
                            allVisToSaveForFilling.addAll(canMigrateAspect,shouldFill);
                        }
                    }
                }
        );
        centiVisToMigrate.forEach(
                selfStackOwningAspect::reduceAndRemoveIfNotPositive
        );
        containerToFill.storeCentiVisOwning(holdingStack,allVisToSaveForFilling);
        return selfModified[0];
    }
}
