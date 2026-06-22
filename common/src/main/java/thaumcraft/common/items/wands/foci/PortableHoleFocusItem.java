package thaumcraft.common.items.wands.foci;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.common.tiles.technique.HoleBlockEntity;

import static thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes.CONSUMPTION_FOCUS;
import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;

import java.util.List;

public class PortableHoleFocusItem extends BasicFocusItem{

    public PortableHoleFocusItem(Properties properties) {
        super(properties);
    }
    public PortableHoleFocusItem(){
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    public static final List<FocusUpgradeType> UPGRADES = List.of(FRUGAL, ENLARGE, EXTEND);


    @Override
    public @NotNull List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusStack, int rank) {
        if (rank > 4){
            return List.of();
        }
        return UPGRADES;
    }
    public static final UnmodifiableCentiVisList<Aspect> COST_PER_STEP = UnmodifiableCentiVisList.of(
            Aspects.ENTROPY, 10,
            Aspects.AIR, 10
    );
    private static final Int2ObjectMap<CentiVisList<Aspect>> MULTIPLIED_COST_PER_STEP = new Int2ObjectOpenHashMap<>();

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        var enlarge = upgrades.getInt(ENLARGE);
        int spreadDistance = 33 + enlarge * 8;
        return MULTIPLIED_COST_PER_STEP.computeIfAbsent(spreadDistance, COST_PER_STEP::multiplyAsNew);
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        return 0;//azanor didnt set it
    }

    protected @Nullable BlockHitResult getValidBlockHitResultForUser(Level level, LivingEntity user) {
        var hitResult = user.pick(5, 0, false);
        if (hitResult.getType() == HitResult.Type.MISS){
            return null;
        }
        if (!(hitResult instanceof BlockHitResult blockHitResult)){
            return null;
        }
        if (!HoleBlockEntity.HoleSpreader.canSpreadHoleAtPos(level,blockHitResult.getBlockPos())){
            return null;
        }
        return blockHitResult;
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        var blockHitResult = getValidBlockHitResultForUser(level,user);
        if (blockHitResult == null){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }

        if (!(wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerNotCasted)){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerNotCasted;
        if (!checkAndSetCooldown(focusStack,wandStack,user)){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);

        var enlarge = upgrades.getInt(ENLARGE);
        int spreadDistance = 33 + enlarge * 8;
        var extend = upgrades.getInt(EXTEND);
        var duration = 120 + 60 * extend;
        if (!centiVisContainer.consumeAllCentiVis(
                wandStack,
                user,
                getCentiVisCost(focusStack,upgrades),
                !level.isClientSide,
                CONSUMPTION_FOCUS,
                !level.isClientSide
        )){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }
        var atPos = blockHitResult.getBlockPos();
        if (!level.isClientSide){
            HoleBlockEntity.HoleSpreader.doSpreadInitialHoleAt(
                    level,atPos,
                    blockHitResult.getDirection(),
                    spreadDistance,
                    duration
            );
            user.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }else {
            if (level instanceof ClientLevel clientLevel){
                ClientFXUtils.blockSparkle(clientLevel, atPos, 4194368, 1);
            }
        }
        user.swing(interactionHand);
        return InteractionResultHolder.sidedSuccess(wandStack,level.isClientSide);
    }
}
