package thaumcraft.api.expands.worldgen.node.consts;

import net.minecraft.world.World;
import thaumcraft.api.expands.worldgen.node.listeners.DoGenerateCondition;
import thaumcraft.common.config.Config;

import java.util.Random;

public class DoGenerateConditions {
    public static final DoGenerateCondition ConfigCondition = new DoGenerateCondition(0) {
        @Override
        public boolean check(World world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
            return Config.genAura;
        }
    };
    public static final DoGenerateCondition ConfigRandomCondition = new DoGenerateCondition(1) {
        @Override
        public boolean check(World world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
            return random.nextInt(Config.nodeRarity) == 0;
        }
    };
    public static final DoGenerateCondition AuraGenChecker = new DoGenerateCondition(2) {
        @Override
        public boolean check(World world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
            return !auraGen;
        }
    };
}
