package tc4tweak.network;

@Deprecated(forRemoval = true,since = "migrate to packetConfig")
public class MessageSendConfiguration /*implements IMessage, IMessageHandler<MessageSendConfiguration, IMessage>*/ {
//    private boolean checkWorkbenchRecipes;
//
//    public MessageSendConfiguration() {
//        this.checkWorkbenchRecipes = ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes();
//    }
//
//    public MessageSendConfiguration(boolean checkWorkbenchRecipes) {
//        this.checkWorkbenchRecipes = checkWorkbenchRecipes;
//    }
//
//    @Override
//    public void fromBytes(ByteBuf buf) {
//        checkWorkbenchRecipes = buf.readBoolean();
//    }
//
//    @Override
//    public void toBytes(ByteBuf buf) {
//        buf.writeBoolean(checkWorkbenchRecipes);
//    }
//
//    @Override
//    public IMessage onMessage(MessageSendConfiguration message, MessageContext ctx) {
//        NetworkedConfiguration.checkWorkbenchRecipes = checkWorkbenchRecipes;
//        return null;
//    }
}
