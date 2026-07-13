package thaumcraft.api.listeners.infusion.instabilityevent;

import thaumcraft.api.internal.WeightedRandomCollection;

import java.util.function.Consumer;

import static thaumcraft.api.listeners.infusion.instabilityevent.ThaumcraftDefaultInstabilityActions.*;

public enum InfusionInstabilityEventListeners {
    DEFAULT_TRIGGER_EVENT(new InfusionInstabilityEventListener(0) {
        @Override
        public void onInstabilityEvent(InfusionInstabilityEventContext ctx) {
            EVENT_FOR_DEFAULT_TRIGGER.getRandom(
                    ctx.level.random
            ).accept(ctx);
        }
    });
    public static final WeightedRandomCollection<Consumer<InfusionInstabilityEventListener.InfusionInstabilityEventContext>> EVENT_FOR_DEFAULT_TRIGGER = new WeightedRandomCollection<>();
    static {
        EVENT_FOR_DEFAULT_TRIGGER.add(
                ctx -> infusionEjectItem(ctx,CONTAINER_DROP_STACK,IGNORE),4
        );//inEvEjectItem:0
        EVENT_FOR_DEFAULT_TRIGGER.add(
                ctx -> infusionEjectItem(ctx,CONTAINER_DROP_STACK,PLACE_FLUX_GOO),2
        );//inEvEjectItem:1
        EVENT_FOR_DEFAULT_TRIGGER.add(
                ctx -> infusionEjectItem(ctx,CONTAINER_DROP_STACK,PLACE_FLUX_GAS),2
        );//inEvEjectItem:2
        EVENT_FOR_DEFAULT_TRIGGER.add(
                ctx -> infusionEjectItem(ctx,CONTAINER_CLEAR_CONTENT,PLACE_FLUX_GAS),1
        );//inEvEjectItem:3
        EVENT_FOR_DEFAULT_TRIGGER.add(
                ctx -> infusionEjectItem(ctx,CONTAINER_CLEAR_CONTENT,PLACE_FLUX_GOO),1
        );//inEvEjectItem:4
        EVENT_FOR_DEFAULT_TRIGGER.add(
                ctx -> infusionEjectItem(ctx,CONTAINER_DROP_STACK,EXPLODE),2
        );//inEvEjectItem:5
        EVENT_FOR_DEFAULT_TRIGGER.add(ThaumcraftDefaultInstabilityActions::infusionZapOne,3);
        EVENT_FOR_DEFAULT_TRIGGER.add(ThaumcraftDefaultInstabilityActions::infusionZapAll,1);
        EVENT_FOR_DEFAULT_TRIGGER.add(ThaumcraftDefaultInstabilityActions::infusionFluxOne,2);
        EVENT_FOR_DEFAULT_TRIGGER.add(ThaumcraftDefaultInstabilityActions::infusionFluxAll,1);
        EVENT_FOR_DEFAULT_TRIGGER.add(ThaumcraftDefaultInstabilityActions::infusionExplodeSmall,1);
        EVENT_FOR_DEFAULT_TRIGGER.add(ThaumcraftDefaultInstabilityActions::infusionWarpPlayer,1);

    }
    public final InfusionInstabilityEventListener listener;
    InfusionInstabilityEventListeners(InfusionInstabilityEventListener listener) {
        this.listener = listener;
    }

}
