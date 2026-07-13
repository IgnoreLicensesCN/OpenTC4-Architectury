package thaumcraft.common.items.transport;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.mirror.MirrorBlockEntity;

import java.util.List;

import static com.linearity.opentc4.Consts.AbstractMirrorBlockEntityTagAccessors.LINKED_DIM;
import static com.linearity.opentc4.Consts.AbstractMirrorBlockEntityTagAccessors.LINKED_POS;
import static com.linearity.opentc4.OpenTC4.platformUtils;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

//bundle item like interaction
public class HandMirrorItem extends Item {
    public HandMirrorItem(Properties properties) {
        super(properties);
    }
    public HandMirrorItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var pos =  useOnContext.getClickedPos();
        var user = useOnContext.getPlayer();
        var stack = useOnContext.getItemInHand();
        if (!level.isClientSide) {
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof MirrorBlockEntity mirrorBlockEntity) {
                var tag = stack.getOrCreateTag();
                LINKED_POS.writeToCompoundTag(tag,mirrorBlockEntity.getBlockPos());
                LINKED_DIM.writeToCompoundTag(tag,level.dimension().location());
                if (user != null){
                    user.sendSystemMessage(
                            Component.translatable("tc.handmirrorlinked")
                                    .withStyle(ChatFormatting.DARK_PURPLE)
                                    .withStyle(ChatFormatting.ITALIC)
                    );
                }
            }
        }
        return super.useOn(useOnContext);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack mirrorStack, ItemStack stackInSlot, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY) {
            if (!stackInSlot.isEmpty()) {
                if (!player.level().isClientSide) {
                    var mirror = getMirrorLinked(stackInSlot);
                    if (mirror != null) {
                        mirror.addStack(stackInSlot.copy());
                        stackInSlot.setCount(0);
                        player.containerMenu.broadcastChanges();
                    }
                }
            }
            return true;
        }
        return super.overrideOtherStackedOnMe(mirrorStack, stackInSlot, slot, clickAction, player, slotAccess);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack mirrorStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction == ClickAction.SECONDARY) {
            ItemStack stackInSlot = slot.getItem();
            if (!stackInSlot.isEmpty()) {
                if (!player.level().isClientSide) {
                    var mirror = getMirrorLinked(stackInSlot);
                    if (mirror != null) {
                        mirror.addStack(stackInSlot.copy());
                        stackInSlot.setCount(0);
                        player.containerMenu.broadcastChanges();
                    }
                }
            }
            return true;
        }
        return super.overrideStackedOnOther(mirrorStack, slot, clickAction, player);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.hasTag()) {
            var tag = itemStack.getTag();
            if (tag != null){
                if (LINKED_POS.compoundTagHasKey(tag) && LINKED_DIM.compoundTagHasKey(tag)){
                    list.add(Component.translatable("tc.handmirrorlinkedto"));
                    list.add(Component.literal(LINKED_POS.readFromCompoundTag(tag).toString()));
                    list.add(Component.literal(LINKED_DIM.readFromCompoundTag(tag).toString()));
                }
            }
        }
    }
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
    protected @Nullable MirrorBlockEntity getMirrorLinked(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) return null;
        var pos = LINKED_POS.readFromCompoundTag(tag);
        var dimResLoc = LINKED_DIM.readFromCompoundTag(tag);
        final Level[] levelArr = {null};
        platformUtils.getServer().registryAccess()//sorry guys i have to use server registry not level(although they should be same in usual)
                .registry(Registries.DIMENSION).ifPresent(dimRegistry -> {
                    var level = dimRegistry.get(dimResLoc);
                    if (level != null) {
                        levelArr[0] = level;
                    }
                });
        if (levelArr[0] == null) return null;
        return LevelBlockEntityAccessing.getExistingBlockEntity(levelArr[0], pos) instanceof MirrorBlockEntity mirrorBlockEntity ? mirrorBlockEntity : null;
    }
}
