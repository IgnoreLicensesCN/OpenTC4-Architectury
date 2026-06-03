package com.linearity.opentc4.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;

public class EntityTypeTests {
    public static final EntityTypeTest<Entity, Entity> ENTITY_TEST = EntityTypeTest.forClass(Entity.class);
    public static final EntityTypeTest<Entity, LivingEntity> LIVING_TEST = EntityTypeTest.forClass(LivingEntity.class);
    public static final EntityTypeTest<Entity, Player> PLAYER_TEST = EntityTypeTest.forClass(Player.class);
    public static final EntityTypeTest<Entity, ServerPlayer> SERVER_PLAYER_TEST = EntityTypeTest.forClass(ServerPlayer.class);
    public static final EntityTypeTest<Entity, ExperienceOrb> EXPERIENCE_ORB_TEST = EntityTypeTest.forClass(ExperienceOrb.class);
    public static final EntityTypeTest<Entity, ItemEntity> ITEM_ENTITY_TEST = EntityTypeTest.forClass(ItemEntity.class);

}
