package thaumcraft.common.items.equipment.armor;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.collectionlike.IntIntPair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IAspectDisplayItem;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.abstracts.IBundleLikeItem;
import thaumcraft.common.items.abstracts.IEssentiaFuelProviderItem;
import thaumcraft.common.items.abstracts.IFlyingAbilityProviderWearing;
import thaumcraft.common.items.abstracts.harness.IHarnessFlyingSpeedModifier;
import thaumcraft.common.items.abstracts.harness.IHarnessFuelDurationMultiplier;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.linearity.opentc4.utils.equip.EquipmentSlotSlotAccess.CHESTPLATE_ACCESS;
import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBaubleAndArmor;

public class ThaumostaticHarnessItem extends ArmorItem implements
        IVisDiscountGear,
        IAugmentationRunicShieldProviderItem,
        IBundleLikeItem,
        IAspectDisplayItem<Aspect>,
        IFlyingAbilityProviderWearing
{
    public ThaumostaticHarnessItem(ArmorMaterial armorMaterial,Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public ThaumostaticHarnessItem(){
        this(ThaumcraftItems.ToolAndArmorMaterial.SPECIAL, Type.CHESTPLATE, new Properties().stacksTo(1).rarity(Rarity.EPIC).durability(400));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack harnessStack, Slot slot, ClickAction clickAction, Player player) {
        return bundleOverrideStackedOnOther(harnessStack, slot, clickAction, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack harnessStack, ItemStack stackInSlot, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        return bundleOverrideOtherStackedOnMe(harnessStack, stackInSlot, slot, clickAction, player, slotAccess);
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        return aspect == Aspects.AIR?5:2;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,Aspects.AIR);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,null);
        addShieldToolTip(itemStack,level,list,tooltipFlag);
    }

    @Override
    public int getMaxItemCount(ItemStack bundleStack) {
        return 1;
    }

    @Override
    public boolean canInsertStack(ItemStack bundleStack, ItemStack stackToInsert) {
        return stackToInsert.getItem() instanceof IEssentiaFuelProviderItem;
    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay(ItemStack itemStack) {
        AspectList<Aspect> owningAspects = new HashAspectList<>();
        var stacksInside = getStacksInside(itemStack);
        if (!stacksInside.isEmpty()) {
            for (var stackInside : stacksInside) {
                if (stackInside.getItem() instanceof IEssentiaFuelProviderItem fuelProviderItem) {
                    owningAspects.addAll(fuelProviderItem.getAspects(stackInside));
                }
            }
        }
        return owningAspects;
    }

    protected Aspect getRequiringAspect() {
        return Aspects.ENERGY;
    }

    protected boolean consumeFuel(ItemStack harnessStack){
        var stacks = getStacksInside(harnessStack);
        for (var stack : stacks) {
            if (stack.getItem() instanceof IEssentiaFuelProviderItem fuelProviderItem) {
                if (fuelProviderItem.consumeFuelEssentiaAmount(stack,getRequiringAspect(),1) > 0){
                    setStacksInside(harnessStack,stacks);
                    return true;
                }
            }
        }
        return false;
    }


    protected IntIntPair getFuelProgressAndMaxProgress(ItemStack harnessStack) {
        AtomicInteger capacity = new AtomicInteger(0);
        AtomicInteger progress = new AtomicInteger(0);
        var stacksInside = getStacksInside(harnessStack);
        var requiringAspect = getRequiringAspect();
        stacksInside.forEach(stack -> {
            if (stack.getItem() instanceof IEssentiaFuelProviderItem fuelProviderItem) {
                capacity.addAndGet(fuelProviderItem.getMaxFuelEssentiaAmount(stack, requiringAspect));
                progress.addAndGet(fuelProviderItem.getFuelEssentiaAmount(stack, requiringAspect));
            }
        });
        return new IntIntPair(capacity.get(), progress.get());
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return getFuelProgressAndMaxProgress(itemStack).rightInt() > 0;
    }

    private static final int barColor = Aspects.ENERGY.color;
    @Override
    public int getBarColor(ItemStack itemStack) {
        return barColor;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        var progressInfo = getFuelProgressAndMaxProgress(itemStack);
        return Math.min(1 + 12 * progressInfo.leftInt() / progressInfo.rightInt(), 13);
    }

    private static final Map<Player,AtomicInteger> PLAYER_NEXT_COST_ASPECT_TICK = new MapMaker().weakKeys().makeMap();
    public static final int DEFAULT_COST_TICK_DURATION = 360;
    @Override
    public void inventoryTick(ItemStack harnessStack, Level level, Entity entity, int i, boolean bl) {
        if (i != Type.CHESTPLATE.getSlot().getIndex() + 36){
            return;
        }
        if (entity instanceof Player p) {
            var abilities = p.getAbilities();
            var fuelInfo = getFuelProgressAndMaxProgress(harnessStack);
            var costTicker = PLAYER_NEXT_COST_ASPECT_TICK.get(p);

            boolean costTickerTicking = true;
            if (costTicker != null){
                if (costTicker.get() <= 0){
                    costTickerTicking = false;
                }
            }
            boolean canFly = (fuelInfo.leftInt() != 0 || costTickerTicking);

            if (abilities.flying) {
                checkAndConsumeFuel(harnessStack, p);
                multiplySpeed(harnessStack,p);
                p.resetFallDistance();
            }
            if (abilities.mayfly && !canFly) {
                abilities.mayfly = false;
                FlyingAbilityProviderCheck.unregisterFlyingProviderForPlayer(p, CHESTPLATE_ACCESS);
                abilities.flying = false;
                if (entity instanceof ServerPlayer serverPlayer){
                    serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(abilities));
                    serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(
                            0, serverPlayer.containerMenu.incrementStateId(), 6, harnessStack
                    ));
                }
            } else if (!abilities.mayfly && canFly) {
                abilities.mayfly = true;
                FlyingAbilityProviderCheck.registerFlyingProviderForPlayer(p, CHESTPLATE_ACCESS);
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(abilities));
                    serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(
                            0,serverPlayer.containerMenu.incrementStateId(),6,harnessStack
                    ));
                }
            }
        }
    }

    protected void multiplySpeed(ItemStack harnessStack,Player player){
        int haste = EnchantmentHelper.getItemEnchantmentLevel(ThaumcraftEnchantments.HASTE, harnessStack);
        final float[] flyingSpeedModifier = {0.7F + 0.075F * (float) haste};
        forEachBaubleAndArmor(
                player,stack -> {
                    if (stack.getItem() instanceof IHarnessFlyingSpeedModifier speedModifier) {
                        flyingSpeedModifier[0] += speedModifier.getHarnessSpeedMultiplier(stack);
                    }
                }
        );
        var speed = player.getDeltaMovement();
        player.setDeltaMovement(speed.x * flyingSpeedModifier[0], speed.y, speed.z * flyingSpeedModifier[0]);
    }
    protected void checkAndConsumeFuel(ItemStack itemStack, Player p) {
        var costAtomic = PLAYER_NEXT_COST_ASPECT_TICK.computeIfAbsent(p, _ignored -> new AtomicInteger());
        if (costAtomic.decrementAndGet() <= 0) {
            if (consumeFuel(itemStack)){
                float[] fuelMultiplier = {1};
                forEachBaubleAndArmor(p, stack -> {
                    if (stack.getItem() instanceof IHarnessFuelDurationMultiplier fuelDurationMultiplier) {
                        fuelMultiplier[0] *= fuelDurationMultiplier.getHarnessFuelDurationMultiplier(stack);
                    }
                });
                costAtomic.addAndGet((int) (DEFAULT_COST_TICK_DURATION * fuelMultiplier[0]));
            }
        }
    }

    @Override
    public boolean canProvideFlyingAbilityWhenEquipped(ItemStack selfStack, Player player) {
        var costTicker = PLAYER_NEXT_COST_ASPECT_TICK.get(player);
        if (costTicker != null) {
            if (costTicker.get() > 0){
                return true;
            }
        }
        return getFuelProgressAndMaxProgress(selfStack).leftInt() > 0;
    }
}
