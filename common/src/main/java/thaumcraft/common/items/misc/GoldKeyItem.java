package thaumcraft.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import thaumcraft.common.tiles.abstracts.owned.IKeyAccessibleOwnedBlockEntity;

public class GoldKeyItem extends AbstractKeyItem {
    public GoldKeyItem(Properties properties) {
        super(properties);
    }
    public GoldKeyItem() {
        this(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
    }

    @Override
    protected void onAddingPermission(IKeyAccessibleOwnedBlockEntity keyable, Player player){
        keyable.addUser(player);
        keyable.addOwner(player);
        player.displayClientMessage(
                Component.literal("you can use this " + keyable.getKeyableName() +" now.").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC)
                        .append(Component.translatable("tc.key4").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC))
                ,true);//TODO:Translation key
    }
}
