package thaumcraft.common.entities.abstracts.boss;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface IBossEntity {
    int thaumcraftBoss$getInvulnerableTicks();
    void thaumcraftBoss$setInvulnerableTicks(int ticks);
    int thaumcraftBoss$getInvulnerableTickLimit();

    default void thaumcraftBoss$startSeenByPlayer(ServerPlayer arg) {
        this.thaumcraftBoss$getBossEvent().addPlayer(arg);
    }
    default void thaumcraftBoss$stopSeenByPlayer(ServerPlayer arg) {
        this.thaumcraftBoss$getBossEvent().removePlayer(arg);
    }
    default void thaumcraftBoss$customServerAiStep(){
        this.thaumcraftBoss$getBossEvent().setProgress(this.getHealth() / this.getMaxHealth());
    }
    default void thaumcraftBoss$afterSetCustomName(@Nullable Component nameToSet /*not the final name*/ ) {
        this.thaumcraftBoss$getBossEvent().setName(this.getDisplayName());
    }
    ServerBossEvent thaumcraftBoss$getBossEvent();
    float getMaxHealth();
    float getHealth();
    Component getDisplayName();

    default void thaumcraftBoss$tick(){
        if (this.thaumcraftBoss$getInvulnerableTicks() > 0) {
            thaumcraftBoss$invulnerableTick();
        }else {
            thaumcraftBoss$vulnerableTick();
        }
    }

    int thaumcraftBoss$getTickCount();
    default void thaumcraftBoss$vulnerableTick(){

    }
    default void thaumcraftBoss$invulnerableTick(){

        int k1 = this.thaumcraftBoss$getInvulnerableTicks() - 1;
        float percent = k1 / (float) thaumcraftBoss$getInvulnerableTickLimit();
        this.thaumcraftBoss$getBossEvent().setProgress(1.0F - percent);
//            if (k1 <= 0) {
//                this.level().explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, Level.ExplosionInteraction.MOB);
//                if (!this.isSilent()) {
//                    this.level().globalLevelEvent(1023, this.blockPosition(), 0);
//                }
//            }

        this.thaumcraftBoss$setInvulnerableTicks(k1);
    }
}
