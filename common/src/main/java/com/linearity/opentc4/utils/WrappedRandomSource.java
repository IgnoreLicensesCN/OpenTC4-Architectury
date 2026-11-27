package com.linearity.opentc4.utils;

import lombok.AllArgsConstructor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.random.RandomGenerator;

@AllArgsConstructor
public class WrappedRandomSource extends Random implements RandomSource, RandomGenerator {
    protected final RandomSource randomSource;

    @Override
    public @NotNull RandomSource fork() {
        return randomSource.fork();
    }

    @Override
    public @NotNull PositionalRandomFactory forkPositional() {
        return randomSource.forkPositional();
    }

    @Override
    public void setSeed(long l) {
        randomSource.setSeed(l);
    }

    @Override
    public int nextInt() {
        return randomSource.nextInt();
    }

    @Override
    public int nextInt(int i) {
        return randomSource.nextInt(i);
    }

    @Override
    public int nextIntBetweenInclusive(int i, int j) {
        return randomSource.nextIntBetweenInclusive(i, j);
    }

    @Override
    public long nextLong() {
        return randomSource.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return randomSource.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return randomSource.nextFloat();
    }

    @Override
    public double nextDouble() {
        return randomSource.nextDouble();
    }

    @Override
    public double nextGaussian() {
        return randomSource.nextGaussian();
    }

    @Override
    public double triangle(double d, double e) {
        return randomSource.triangle(d, e);
    }

    @Override
    public void consumeCount(int i) {
        randomSource.consumeCount(i);
    }

    @Override
    public int nextInt(int i, int j) {
        return randomSource.nextInt(i, j);
    }

}
