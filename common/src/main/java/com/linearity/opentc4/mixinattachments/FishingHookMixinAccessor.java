package com.linearity.opentc4.mixinattachments;

public interface FishingHookMixinAccessor {
    FishHookState opentc4$getCurrentState();
    void opentc4$setCurrentState(FishHookState state);

    enum FishHookState {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;
    }

}
