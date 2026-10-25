package thaumcraft.api.damage.type;

import lombok.Builder;

@Builder
public class InvulnerableTimeCategory {
    @Builder.Default
    public final float timeChangeScale = 1;
}
