package thaumcraft.api.researchtable;

import net.minecraft.network.chat.Component;

public class ResearchCreateReason {
    public final boolean canCreate;
    public final Component reason;

    public ResearchCreateReason(boolean canCreate, Component reason) {
        this.canCreate = canCreate;
        this.reason = reason;
    }
}
