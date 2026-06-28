package thaumcraft.common.entities.abstracts;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.Zombie;

public interface IZombieConvertableEntity {
    //true if show dead anim
    default boolean onKilledByZombie(ServerLevel serverLevel, Zombie zombieKiller){
        if ((serverLevel.getDifficulty() == Difficulty.NORMAL || serverLevel.getDifficulty() == Difficulty.HARD) ) {
            if (serverLevel.getDifficulty() != Difficulty.HARD && zombieKiller.getRandom().nextBoolean()) {
                return true;
            }
            return !zombify(serverLevel, zombieKiller);
        }
        return true;
    };
    //FALSE if show dead anim(not zombified,true if zombified)
    boolean zombify(ServerLevel level,Zombie killer);
}
