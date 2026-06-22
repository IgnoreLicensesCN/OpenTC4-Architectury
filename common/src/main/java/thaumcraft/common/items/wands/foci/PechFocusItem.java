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
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.pechfocus.PechBlastEntity;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;

import java.util.List;

import static thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes.CONSUMPTION_FOCUS;
import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;

public class PechFocusItem extends BasicFocusItem {

    public PechFocusItem(Properties properties) {
        super(properties);
    }
    public PechFocusItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    public static final List<FocusUpgradeType> RANK_0_UPGRADES =
            List.of(FRUGAL, POTENCY);
    public static final List<FocusUpgradeType> RANK_1_UPGRADES =
            List.of(FRUGAL, POTENCY, EXTEND);
    public static final List<FocusUpgradeType> RANK_2_UPGRADES =
            List.of(FRUGAL, POTENCY);
    public static final List<FocusUpgradeType> RANK_3_UPGRADES =
            List.of(FRUGAL, FRUGAL, EXTEND);
    public static final List<FocusUpgradeType> RANK_4_UPGRADES =
            List.of(FRUGAL, POTENCY, NIGHT_SHADE);

    @Override
    public @NotNull List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusStack, int rank) {
        if (focusStack == null) {
            return List.of();
        }
        if (rank == 0) return RANK_0_UPGRADES;
        if (rank == 1) return RANK_1_UPGRADES;
        if (rank == 2) return RANK_2_UPGRADES;
        if (rank == 3) return RANK_3_UPGRADES;
        if (rank == 4) return RANK_4_UPGRADES;
        return List.of();
    }

    public static final CentiVisList<Aspect> NIGHT_SHADE_COST = UnmodifiableCentiVisList.of(
            Aspects.AIR, 10,
            Aspects.FIRE, 10,
            Aspects.EARTH, 10,
            Aspects.ORDER, 10,
            Aspects.ENTROPY, 10,
            Aspects.WATER, 10
    );
    public static final CentiVisList<Aspect> BASE_COST = UnmodifiableCentiVisList.of(
            Aspects.EARTH, 10,
            Aspects.ENTROPY, 10,
            Aspects.WATER, 10
    );

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        if (upgrades.getInt(NIGHT_SHADE) > 0) {
            return NIGHT_SHADE_COST;
        }
        return BASE_COST;
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        return 5;
    }


    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        if (!(wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerNotCasted)) {
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        if (checkAndSetCooldown(focusStack,wandStack,user)){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerNotCasted;
        if (!level.isClientSide()) {
            var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
            if (centiVisContainer.consumeAllCentiVis(
                    wandStack,
                    user,
                    getCentiVisCost(focusStack,upgrades),
                    true,
                    CONSUMPTION_FOCUS,
                    true
            )){
                var blast = new PechBlastEntity(user,level);
                blast.strength = upgrades.getInt(POTENCY);
                blast.duration = upgrades.getInt(ENLARGE);
                blast.nightshade = upgrades.getInt(NIGHT_SHADE) > 0;
                level.addFreshEntity(blast);
                blast.playSound(ThaumcraftSounds.ICE, 0.4F, 1.0F + level.random.nextFloat() * 0.1F);
            }
        }
        user.swing(interactionHand);
        return InteractionResultHolder.sidedSuccess(wandStack,level.isClientSide);
    }
}
