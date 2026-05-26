package com.linearity.opentc4.utils;

import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NonNullListWithConstructor<E> extends NonNullList<E> {
    public NonNullListWithConstructor(List<E> list, @NotNull E object) {
        super(list, object);
    }
}
