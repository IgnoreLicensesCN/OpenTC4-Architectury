package com.linearity.opentc4.utils.entity.category;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Horse;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityCategoryByClass<E extends Entity> extends EntityCategory<E> {
    public final Class<E> clz;

    @Override
    public boolean isInCategory(Entity entity) {
        return clz.isInstance(entity);
    }

    private static final Map<Class<?>,EntityCategoryByClass<?>> instances = new HashMap<>();
    static {
        instances.put(
                Animal.class,
                new EntityCategoryByClass<>(Animal.class) {
                    @Override
                    public boolean isInCategory(Entity entity) {
                        return entity instanceof Animal;
                    }
                }
        );
        instances.put(
                Sheep.class,
                new EntityCategoryByClass<>(Animal.class) {
                    @Override
                    public boolean isInCategory(Entity entity) {
                        return entity instanceof Sheep;
                    }
                }
        );
        instances.put(
                Cow.class,
                new EntityCategoryByClass<>(Animal.class) {
                    @Override
                    public boolean isInCategory(Entity entity) {
                        return entity instanceof Cow;
                    }
                }
        );
        instances.put(
                Chicken.class,
                new EntityCategoryByClass<>(Animal.class) {
                    @Override
                    public boolean isInCategory(Entity entity) {
                        return entity instanceof Chicken;
                    }
                }
        );
        instances.put(
                Pig.class,
                new EntityCategoryByClass<>(Animal.class) {
                    @Override
                    public boolean isInCategory(Entity entity) {
                        return entity instanceof Pig;
                    }
                }
        );
        instances.put(
                Horse.class,
                new EntityCategoryByClass<>(Animal.class) {
                    @Override
                    public boolean isInCategory(Entity entity) {
                        return entity instanceof Horse;
                    }
                }
        );
    }
    //TODO:new instances by make "instanceof" with bytecode
    public static <E extends Entity> EntityCategoryByClass<E> of(Class<E> clazz) {
        return (EntityCategoryByClass<E>) instances.computeIfAbsent(clazz, clz -> new EntityCategoryByClass<>((Class<Entity>) clz));
    }
}
