package thaumcraft.api.researchtable;

import net.minecraft.network.chat.Component;

public class ResearchCreateReasons {
    public static final ResearchCreateReason NO_PREREQUISITES = new ResearchCreateReason(false,Component.translatable("tc.research.no_prerequisites"));
    public static final ResearchCreateReason SUSPICIOUS_CALL = new ResearchCreateReason(false,Component.translatable("tc.research.suspicious_call"));
    public static final ResearchCreateReason NO_INK_OR_PAPER = new ResearchCreateReason(false, Component.translatable("tc.research.shortprim"));
    public static final ResearchCreateReason CAN_CREATE = new ResearchCreateReason(true, Component.empty());
}
