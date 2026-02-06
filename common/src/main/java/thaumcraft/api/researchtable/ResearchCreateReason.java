package thaumcraft.api.researchtable;

import net.minecraft.network.chat.Component;

public record ResearchCreateReason(boolean canCreate, Component reason) {
}
