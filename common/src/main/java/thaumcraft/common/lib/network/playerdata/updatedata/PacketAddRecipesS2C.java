package thaumcraft.common.lib.network.playerdata.updatedata;

import com.linearity.opentc4.mixin.ClientPacketListenerAccessor;
import com.linearity.opentc4.mixinaccessors.RecipeManagerAccessor;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.Recipe;
import thaumcraft.common.Thaumcraft;

import java.util.Collection;

public class PacketAddRecipesS2C extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":add_recipes";
    public static MessageType messageType;
    private final Collection<Recipe<?>> recipes;
    public PacketAddRecipesS2C(Collection<Recipe<?>> recipes){
        this.recipes = recipes;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(this.recipes, ClientboundUpdateRecipesPacket::toNetwork);
    }

    public static PacketAddRecipesS2C read(FriendlyByteBuf friendlyByteBuf) {
        return new PacketAddRecipesS2C(friendlyByteBuf.readList(ClientboundUpdateRecipesPacket::fromNetwork));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var connection = ((ClientPacketListenerAccessor)Minecraft.getInstance().getConnection());
            if (connection == null){
                return;
            }
            ((RecipeManagerAccessor)connection.opentc4$getRecipeManager()).opentc4$addRecipes(this.recipes);
        });
    }
}
