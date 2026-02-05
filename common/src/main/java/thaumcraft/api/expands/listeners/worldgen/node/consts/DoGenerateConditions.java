package thaumcraft.api.expands.listeners.worldgen.node.consts;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import thaumcraft.api.expands.listeners.worldgen.node.listeners.DoGenerateCondition;
import thaumcraft.common.config.Config;

public class DoGenerateConditions {
    public static final DoGenerateCondition ConfigCondition = new DoGenerateCondition(0) {
        @Override
        public boolean check(Level world, RandomSource random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
            return Config.genAura;
        }
    };
    public static final DoGenerateCondition ConfigRandomCondition = new DoGenerateCondition(1) {
        @Override
        public boolean check(Level world, RandomSource random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
            return random.nextInt(Config.nodeRarity) == 0;
        }
    };
    public static final DoGenerateCondition AuraGenChecker = new DoGenerateCondition(2) {
        @Override
        public boolean check(Level world, RandomSource random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
            return !auraGen;
        }
    };
}
