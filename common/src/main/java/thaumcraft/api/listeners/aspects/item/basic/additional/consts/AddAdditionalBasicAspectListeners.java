package thaumcraft.api.listeners.aspects.item.basic.additional.consts;

import thaumcraft.api.listeners.aspects.item.basic.additional.AddAdditionalBasicAspectContext;
import thaumcraft.api.listeners.aspects.item.basic.additional.listeners.AddAdditionalBasicAspectListener;

import static thaumcraft.api.listeners.aspects.item.basic.ItemAdditionalBasicAspectRegistered.getAdditionalBasicAspects;

public enum AddAdditionalBasicAspectListeners {
    REGISTERED_ADDITIONAL(
            new AddAdditionalBasicAspectListener(0) {
                @Override
                public void considerAddAdditional(AddAdditionalBasicAspectContext context) {
                    context.submitAdditional(getAdditionalBasicAspects(context.item));
                }
            }
    );

    public final AddAdditionalBasicAspectListener listener;

    AddAdditionalBasicAspectListeners(AddAdditionalBasicAspectListener listener) {
        this.listener = listener;
    }
}
