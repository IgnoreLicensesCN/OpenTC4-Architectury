package thaumcraft.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.TileEldritchLock;

import java.util.List;

import static thaumcraft.common.ThaumcraftSounds.RUNIC_SHIELD_CHARGE;

public class RunedTabletItem extends Item {
    public RunedTabletItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable("item.RunedTableItem.text")
                .withStyle(ChatFormatting.DARK_PURPLE));
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {

        var world = useOnContext.getLevel();
        var player = useOnContext.getPlayer();
        var pos = useOnContext.getClickedPos();

        if (world.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {return super.useOn(useOnContext);}

        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEldritchLock lock && lock.count < 0) {
            lock.count = 0;
            world.sendBlockUpdated(pos,te.getBlockState(), te.getBlockState(), 3);
            lock.markDirtyAndUpdateSelf();
            useOnContext.getItemInHand().shrink(1);
            world.playSound(null,pos, RUNIC_SHIELD_CHARGE,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(useOnContext);
    }
}
