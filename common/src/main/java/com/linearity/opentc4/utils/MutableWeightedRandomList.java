package com.linearity.opentc4.utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class MutableWeightedRandomList<E extends WeightedEntry> {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<E> items = new ArrayList<>();
    private int totalWeight = 0;

    public MutableWeightedRandomList() {}

    @SafeVarargs
    public MutableWeightedRandomList(E... entries) {
        Collections.addAll(items, entries);
        recalcTotalWeight();
    }

    public MutableWeightedRandomList(List<E> list) {
        items.addAll(list);
        recalcTotalWeight();
    }

    private void recalcTotalWeight() {
        totalWeight = items.stream().mapToInt(t -> t.getWeight().asInt()).sum();
    }

    public void add(E entry) {
        lock.writeLock().lock();
        try {
            items.add(entry);
            totalWeight += entry.getWeight().asInt();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean remove(E entry) {
        lock.writeLock().lock();
        try {
            boolean removed = items.remove(entry);
            if (removed) recalcTotalWeight();
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return items.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<E> getRandom(RandomSource random) {
        lock.readLock().lock();
        try {
            if (totalWeight == 0) return Optional.empty();
            int i = random.nextInt(totalWeight);
            int sum = 0;
            for (E entry : items) {
                sum += entry.getWeight().asInt();
                if (i < sum) return Optional.of(entry);
            }
            return Optional.empty(); // should not reach
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<E> unwrap() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(items); // 返回副本，避免外部修改
        } finally {
            lock.readLock().unlock();
        }
    }
}

