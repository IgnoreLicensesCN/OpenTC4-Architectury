package thaumcraft.common.items.wands.foci;


import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.primalfocus.PrimalOrbEntity;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;

import java.util.List;

import static thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes.CONSUMPTION_FOCUS;
import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;
import static thaumcraft.common.items.wands.foci.PrimalFocusItem$Cost.RANDOM_COSTS;

public class PrimalFocusItem extends BasicFocusItem{


    public PrimalFocusItem(Properties properties) {
        super(properties);
    }
    public PrimalFocusItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    protected int easyLCGState = 0;
    protected static final int[] LCGNeededPrimesExample = {
            3877, 6737, 7237, 62327, 39439, 53791, 53549, 16759,
            1987, 35897, 46589, 59369, 26647, 56629, 26387, 1931,
            43451, 4409, 823, 14947, 22907, 9533, 36343, 46601,
            36833, 26903, 1667, 4519, 53777, 38917, 37181, 56417,
            14923, 42989, 58481, 12577, 54151, 18691, 44927, 47591,
            29569, 54499, 16223, 63997, 12149, 6551, 59341, 30553,
            58909, 34883, 1759, 11093, 13873, 64621, 13367, 16741,
            14221, 28429, 50873, 9127, 54721, 2447, 16057, 28183
    };
    protected int LCGStep = LCGNeededPrimesExample[Math.abs(System.identityHashCode(this)% LCGNeededPrimesExample.length)];

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        easyLCGState = (easyLCGState + LCGStep) % RANDOM_COSTS.length;
        return RANDOM_COSTS[easyLCGState];
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        return 10;
    }

    public static final List<FocusUpgradeType> UPGRADES = List.of(FRUGAL);
    public static final List<FocusUpgradeType> RANK_2_UPGRADES = List.of(FRUGAL,SEEKER);

    @Override
    public @NotNull List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusStack, int rank) {
        if (rank > 4){
            return List.of();
        }
        if (rank == 2){
            return RANK_2_UPGRADES;
        }
        return UPGRADES;
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        if (!(wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerItemNotCasted)) {
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        if (!checkAndSetCooldown(focusStack,wandStack,user)){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerItemNotCasted;
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
        if (!centiVisContainer.consumeAllCentiVis(wandStack,user,getCentiVisCost(focusStack,upgrades),!level.isClientSide,CONSUMPTION_FOCUS,!level.isClientSide)){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        if (!level.isClientSide) {
            PrimalOrbEntity orb = new PrimalOrbEntity(user,level);
            orb.setSeeker(upgrades.getInt(SEEKER) > 0);
            level.addFreshEntity(orb);
            orb.playSound(ThaumcraftSounds.ICE, 0.3F, 0.8F + level.getRandom().nextFloat() * 0.1F);
        }

        user.swing(interactionHand);

        return InteractionResultHolder.sidedSuccess(wandStack,level.isClientSide);
    }
}
