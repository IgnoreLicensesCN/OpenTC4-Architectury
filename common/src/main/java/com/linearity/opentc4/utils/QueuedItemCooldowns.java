package com.linearity.opentc4.utils;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class QueuedItemCooldowns extends ItemCooldowns {

    protected final Map<Item, CooldownInstance> cooldowns = new IdentityHashMap<>();
    protected final PriorityQueue<ObjectIntPair<Item>> cooldownQueue = new PriorityQueue<>(ObjectIntPair::compareTo);
    private int tickCount;

    @Override
    public boolean isOnCooldown(Item item) {
        return this.cooldowns.containsKey(item);
    }

    public int getTickCount() {
        return tickCount;
    }

    @Override
    public float getCooldownPercent(Item arg, float partialTicks) {
        CooldownInstance cooldownInstance = this.cooldowns.get(arg);
        if (cooldownInstance != null) {
            float duration = cooldownInstance.duration;
            float h = cooldownInstance.endTime - (this.tickCount + partialTicks);
            return Mth.clamp(h / duration, 0.0F, 1.0F);
        }
        return 0.0F;
    }

    @Override
    public void tick() {
        this.tickCount++;
        while (!this.cooldownQueue.isEmpty() && this.cooldownQueue.peek().rightInt() <= this.tickCount) {
            var expired = this.cooldownQueue.poll();
            var fromMap = this.cooldowns.get(expired.left());
            if (fromMap != null && fromMap.endTime == expired.rightInt()) {
                this.cooldowns.remove(expired.left());
                this.onCooldownEnded(expired.left());
            }
        }
    }

    @Override
    public void addCooldown(Item arg, int duration) {
        this.cooldowns.put(arg, new CooldownInstance(duration, this.tickCount + duration));
        this.onCooldownStarted(arg, this.tickCount + duration);
    }

    @Override
    public void removeCooldown(Item arg) {
        var removed = this.cooldowns.remove(arg);
        if (removed != null) {
            this.cooldownQueue.remove(new ObjectIntPair<>(arg, removed.endTime));
        }
        this.onCooldownEnded(arg);
    }

    public static class CooldownInstance {
        public final int duration;
        public final int endTime;

        public CooldownInstance(int duration, int endTime) {
            this.duration = duration;
            this.endTime = endTime;
        }

        @Override
        public final boolean equals(Object o) {
            if (!(o instanceof CooldownInstance that)) return false;

            return duration == that.duration && endTime == that.endTime;
        }

        @Override
        public int hashCode() {
            int result = duration;
            result = 31 * result + endTime;
            return result;
        }
    }
}
