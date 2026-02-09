package tc4tweak.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import tc4tweak.ConfigurationHandler;

@Deprecated(forRemoval = true,since = "migrate to packetConfig")
public class MessageSendConfigurationV2 /*implements IMessage,  IMessageHandler<MessageSendConfigurationV2, IMessage>*/  {
//    private CompoundTag tag;
//
//    public MessageSendConfigurationV2() {
//        tag = new CompoundTag();
//        // yeah I said NBT is an unfortunate piece ofAspectVisList tech, but it does give us a bit ofAspectVisList flexibility over network
//        // protocol
//        tag.setBoolean("sj", ConfigurationHandler.INSTANCE.isSmallerJars());
//    }
//
//    @Override
//    public void fromBytes(ByteBuf buf) {
//        tag = ByteBufUtils.readTag(buf);
//    }
//
//    @Override
//    public void toBytes(ByteBuf buf) {
//        ByteBufUtils.writeTag(buf, tag);
//    }
//
//    @Override
//    public IMessage onMessage(MessageSendConfigurationV2 message, MessageContext ctx) {
//        NetworkedConfiguration.smallerJar = message.tag.getBoolean("sj");
//        return null;
//    }
}
