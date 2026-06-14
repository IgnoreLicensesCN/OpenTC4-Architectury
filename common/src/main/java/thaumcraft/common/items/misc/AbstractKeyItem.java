package thaumcraft.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.abstracts.owned.IKeyAccessibleOwnedBlockEntity;

import java.util.List;

import static com.linearity.opentc4.Consts.KeyTagAccessors.KEY_POS;
import static com.linearity.opentc4.Consts.KeyTagAccessors.KEY_TYPE;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

//this one is weird and i dont want to fix it
// (it didn't check level info
//and it should generate a uuid (also for ownedBE) in case someone wants to break this door to reset info TODO:[maybe wont finished]fix gold/iron key and OwnedBE)
public abstract class AbstractKeyItem extends Item {
    public static final int ARCANE_DOOR_TYPE_INT = 0;
    public static final int PRESSURE_PLATE_TYPE_INT = 1;

    public AbstractKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        var tag = itemStack.getTag();
        if (tag != null) {
            String keyType = KEY_TYPE.readFromCompoundTag(tag);
            BlockPos pos = KEY_POS.readFromCompoundTag(tag);
            list.add(
                    Component.translatable("tc.key9")
                            .withStyle(ChatFormatting.DARK_PURPLE)
                            .withStyle(ChatFormatting.ITALIC)
            );
            list.add(
                    Component.literal(keyType)
                            .withStyle(ChatFormatting.DARK_PURPLE)
                            .withStyle(ChatFormatting.ITALIC)
            );
            list.add(
                    Component.literal(pos.toString())
                            .withStyle(ChatFormatting.DARK_PURPLE)
                            .withStyle(ChatFormatting.ITALIC)
            );
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        var player = useOnContext.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }
        var pos = useOnContext.getClickedPos();
        var level = useOnContext.getLevel();
        var stack = useOnContext.getItemInHand();
        var blockState = level.getBlockState(pos);
        var hand = useOnContext.getHand();
        if (getExistingBlockEntity(level,pos) instanceof IKeyAccessibleOwnedBlockEntity keyable){
            if (keyable.playerOwnThis(player) && !stack.hasTag()) {
                return setKeyToKeyable(keyable, stack, pos, player, hand, level);
            }

            var storedLocation = readBlockPosFromStack(stack);
            if (storedLocation != pos){
                if (level.isClientSide){
                    player.displayClientMessage(Component.translatable("tc.key7").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC),true);
                }
                return InteractionResult.FAIL;
            }
            if (keyable.playerOwnThis(player) || keyable.playerCanUseThis(player)){
                if (level.isClientSide){
                    player.displayClientMessage(Component.translatable("tc.key8").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC),true);
                }
                return InteractionResult.FAIL;
            }
            if (!keyable.playerOwnThis(player) && !keyable.playerCanUseThis(player)) {
                if (!level.isClientSide) {
                    onAddingPermission(keyable,player);
                }
                stack.shrink(1);
                player.swing(hand);
                player.playSound(ThaumcraftSounds.KEY, 1.0F, 0.9F);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    private static @NotNull InteractionResult setKeyToKeyable(IKeyAccessibleOwnedBlockEntity keyable, ItemStack stack, BlockPos pos, Player player, InteractionHand hand, Level level) {
        var tag = stack.getOrCreateTag();
        KEY_TYPE.writeToCompoundTag(tag, keyable.getKeyableName());
        KEY_POS.writeToCompoundTag(tag, pos);
        player.displayClientMessage(Component.literal("connected to" + keyable.getKeyableName()).withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC),true);//TODO:Translation key
        player.playSound(ThaumcraftSounds.KEY, 1.0F, 0.9F);
        player.swing(hand);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected @Nullable BlockPos readBlockPosFromStack(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null){
            return null;
        }
        if (!KEY_POS.compoundTagHasKey(tag)){
            return null;
        }
        return KEY_POS.readFromCompoundTag(tag);
    }

    protected void onAddingPermission(IKeyAccessibleOwnedBlockEntity keyable, Player player){
        keyable.addUser(player);
        player.displayClientMessage(Component.literal("you can use this " + keyable.getKeyableName() +" now."),true);//TODO:Translation key
    }
}
