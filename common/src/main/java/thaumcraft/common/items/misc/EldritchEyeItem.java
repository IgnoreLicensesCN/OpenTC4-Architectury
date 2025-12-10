package thaumcraft.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.TileEldritchAltar;

import java.util.List;

import static thaumcraft.common.ThaumcraftSounds.CRYSTAL;
import static thaumcraft.common.ThaumcraftSounds.ResourceLocations.RES_CRYSTAL;

public class EldritchEyeItem extends Item {
    public EldritchEyeItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable("item.EldritchEyeItem.text")
                .withStyle(ChatFormatting.DARK_PURPLE));
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var world = useOnContext.getLevel();
        var player = useOnContext.getPlayer();
        if (world.isClientSide || player == null) {return super.useOn(useOnContext);}
        var pos = useOnContext.getClickedPos();

        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEldritchAltar altar) {
            if (altar.getEyes() < 4) {
                if (altar.getEyes() >= 2) {
                    altar.setSpawner(true);
                    altar.setSpawnType((byte)1);
                }

                altar.setEyes((byte)(altar.getEyes() + 1));
                altar.checkForMaze();
                useOnContext.getItemInHand().shrink(1);
                altar.updateEntity();
                world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
//                altar.markDirty();
//                world.markBlockForUpdate(x, y, z);
                world.playSound((null),pos, CRYSTAL, SoundSource.BLOCKS,.2f,1.f);
//                world.playSoundEffect(x, y, z, "thaumcraft:crystal", 0.2F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(useOnContext);
    }
}
