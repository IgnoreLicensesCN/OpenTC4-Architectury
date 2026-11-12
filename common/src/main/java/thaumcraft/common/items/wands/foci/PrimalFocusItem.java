package thaumcraft.common.items.wands.foci;


import com.linearity.opentc4.utils.EasyLCGGenerator;
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
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    protected EasyLCGGenerator LCGGen = new EasyLCGGenerator(RANDOM_COSTS.length);

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        return RANDOM_COSTS[LCGGen.next()];
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
