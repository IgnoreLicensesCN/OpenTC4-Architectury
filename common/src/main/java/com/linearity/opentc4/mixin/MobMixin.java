package com.linearity.opentc4.mixin;

import com.linearity.opentc4.utils.vanilla1710.BiomeType;
import com.linearity.opentc4.utils.vanilla1710.BiomeWithTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.dim.Cell;
import thaumcraft.common.lib.world.dim.CellLoc;
import thaumcraft.common.lib.world.dim.MazeHandler;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static thaumcraft.common.lib.utils.EntityUtils.CHAMPION_MOD_BASE_VALUE_ATTACHED_NOT_AFFECTED;
import static thaumcraft.common.lib.utils.EntityUtils.CHAMPION_MOD_BASE_VALUE_NOT_ATTACHED;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "finalizeSpawn",at=@At("TAIL"))
    public void opentc4$finalizeSpawn(ServerLevelAccessor serverLevelAccessor,
                                      DifficultyInstance difficultyInstance,
                                      MobSpawnType mobSpawnType,
                                      SpawnGroupData spawnGroupData,
                                      CompoundTag compoundTag,
                                      CallbackInfoReturnable<SpawnGroupData> cir) {
        var mob = (Mob)(Object)this;
        if (mob instanceof Monster monster) {

            var random = serverLevelAccessor.getRandom();
            var difficulty = serverLevelAccessor.getDifficulty();
            var level = serverLevelAccessor.getLevel();
            var dim = level.dimension();
            var pos = monster.blockPosition();
            AttributeInstance championModInstance = monster.getAttribute(EntityUtils.CHAMPION_MOD);
            if (championModInstance != null) {
                if (championModInstance.getBaseValue() == CHAMPION_MOD_BASE_VALUE_NOT_ATTACHED) {
                    int championChance = random.nextInt(100);
                    if (difficulty == Difficulty.EASY || !Config.championMobs) {
                        championChance += 2;
                    }

                    if (difficulty == Difficulty.HARD) {
                        championChance -= Config.championMobs ? 2 : 0;
                    }

                    if (dim == Config.dimensionOuter) {
                        championChance -= 3;
                    }

                    Holder<Biome> biomeHolder = level.getBiome(pos);
                    AtomicReference<ResourceKey<Biome>> biomeResKeyRef = new AtomicReference<>();
                    biomeHolder.unwrapKey().ifPresent(biomeResKeyRef::set);
                    if (biomeResKeyRef.get() == null) {
                        biomeResKeyRef.set(BiomeWithTypes.getBiomeResKey(biomeHolder.value()));
                    }
                    Collection<BiomeType> biomeTypes = BiomeWithTypes.getBiomeTypes(biomeResKeyRef.get());

                    if (biomeTypes.contains(BiomeType.SPOOKY)
                            || biomeTypes.contains(BiomeType.NETHER)
                            || biomeTypes.contains(BiomeType.END)) {
                        championChance -= Config.championMobs ? 2 : 1;
                    }

                    if (opentc4$isDangerousLocation(level, pos)){
                        championChance -= Config.championMobs ? 10 : 3;
                    }

                    int cc = 0;
                    boolean whitelisted = false;

                    for (Class<?> clazz : ConfigEntities.championModWhitelist.keySet()) {
                        if (clazz.isAssignableFrom(mob.getClass())) {
                            whitelisted = true;
                            if (Config.championMobs || monster instanceof EntityThaumcraftBoss) {
                                cc = Math.max(cc, ConfigEntities.championModWhitelist.get(clazz) - 1);
                            }
                        }
                    }

                    championChance -= cc;
                    AttributeInstance maxHealthAttr = monster.getAttribute(Attributes.MAX_HEALTH);
                    if (whitelisted
                            && championChance <= 0
                            && maxHealthAttr != null
                            && maxHealthAttr.getBaseValue() >= (double) 10.0F
                    ) {
                        EntityUtils.makeChampion(monster, false);
                    } else {
                        championModInstance.setBaseValue(CHAMPION_MOD_BASE_VALUE_ATTACHED_NOT_AFFECTED);
                    }
                }
            }
        }
    }

    @Unique
    private static boolean opentc4$isDangerousLocation(Level world, BlockPos blockPos) {
        if (world.dimension() == Config.dimensionOuter) {
            int xx = blockPos.getX() >> 4;
            int zz = blockPos.getZ() >> 4;
            Cell c = MazeHandler.getFromHashMap(new CellLoc(xx, zz));
            return c != null && (c.feature == 6 || c.feature == 8);
        }

        return false;
    }
}
