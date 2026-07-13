package thaumcraft.api.listeners.manabean.consts;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import thaumcraft.api.listeners.manabean.ManaBeanEatContext;
import thaumcraft.api.listeners.manabean.listeners.ManaBeanEatListener;
import thaumcraft.api.research.ResearchAndScannedInfo;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum ManaBeanEatListeners {
    RANDOM_EFFECT(
            new ManaBeanEatListener(500) {
                private List<MobEffect> allEffects = null;
                @Override
                public void onEatManaBean(ManaBeanEatContext context) {
                    var level = context.atLevel;
                    if (allEffects == null) {
                        allEffects = BuiltInRegistries.MOB_EFFECT.stream().filter(
                                BLACKLISTED_MANA_BEAN_EFFECTS::contains).toList();
                    }
                    if (allEffects.isEmpty()) return;
                    var eater = context.eater;
                    var randomEffect = allEffects.get(level.getRandom().nextInt(allEffects.size()));
                    if (randomEffect.isInstantenous()) {
                        randomEffect.applyInstantenousEffect(null, null, eater, 1, 1.0D);
                    } else {
                        int duration = 160 + level.getRandom().nextInt(80);
                        eater.addEffect(new MobEffectInstance(randomEffect, duration, 0));
                    }
                }
            }
    ),
    GIVE_ASPECT(
            new ManaBeanEatListener(1000) {
                @Override
                public void onEatManaBean(ManaBeanEatContext context) {
                    var level = context.atLevel;
                    var eater = context.eater;
                    if (!(eater instanceof ServerPlayer player)){
                        return;
                    }
                    var aspect = context.aspectOwning;
                    if (!aspect.isEmpty() && level.random.nextInt(4) == 0){
                        var info = ResearchAndScannedInfo.getFromLiving(player);
                        info.addResearchAspectAndTrySyncToPlayer(aspect,1,player);
                    }
                }
            }
    )
    ;
    public final ManaBeanEatListener listener;
    public static final Set<MobEffect> BLACKLISTED_MANA_BEAN_EFFECTS = ConcurrentHashMap.newKeySet();
    static {
        BLACKLISTED_MANA_BEAN_EFFECTS.add(MobEffects.BAD_OMEN);//endless emerald machine(wtf)
    }
    ManaBeanEatListeners(ManaBeanEatListener listener){
        this.listener = listener;
    }
}
