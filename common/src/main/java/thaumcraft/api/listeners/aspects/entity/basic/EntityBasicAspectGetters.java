package thaumcraft.api.listeners.aspects.entity.basic;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.ShouldNotModify;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectView;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;

import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public enum EntityBasicAspectGetters {
    ENTITY_BASIC(
            new EntityBasicAspectGetter(0) {
                @Override
                public void onGetBasicAspect(Entity entityToGetAspect, @Modifiable AspectList<Aspect> aspects) {
                    aspects.addAll(EntityBasicAspectGetters.getBasicAspectsForEntityType(entityToGetAspect.getType()));
                }
            }
    ),
    PLAYER_BASIC(
            new EntityBasicAspectGetter(0) {
                @Override
                public void onGetBasicAspect(Entity entityToGetAspect, @Modifiable AspectList<Aspect> aspects) {
                    if (entityToGetAspect instanceof Player player) {
                        aspects.addAll(EntityBasicAspectGetters.getAspectsForPlayer(player));
                    }
                }
            }
    ),
    ;
    private static final Map<EntityType<?>,@Unmodifiable AspectList<Aspect>> ENTITY_TYPE_TO_ASPECTS = new ConcurrentHashMap<>();
    private static final Map<TagKey<EntityType<?>>,@Unmodifiable AspectList<Aspect>> ADDITIONAL_ENTITY_TYPE_ASPECTS = new ConcurrentHashMap<>();
    private static final Map<EntityType<?>,@Unmodifiable AspectList<Aspect>> CALCULATED_RESULT = new ConcurrentHashMap<>();
    private static final Map<Player,@Unmodifiable AspectList<Aspect>> ASPECTS_FOR_PLAYER = new MapMaker().weakKeys().makeMap();
    private static final Map<String,@Unmodifiable AspectList<Aspect>> ASPECTS_FOR_PLAYER_SPECIAL_NAMES = new ConcurrentHashMap<>();

    static {
        ASPECTS_FOR_PLAYER_SPECIAL_NAMES.put("azanor",UnmodifiableAspectList.of(Aspects.ELDRITCH, 20, Aspects.MAN, 4));//Greetings!
        ASPECTS_FOR_PLAYER_SPECIAL_NAMES.put("direwolf20",UnmodifiableAspectList.of(Aspects.BEAST, 20, Aspects.MAN, 4));//https://www.youtube.com/user/direwolf20
        ASPECTS_FOR_PLAYER_SPECIAL_NAMES.put("pahimar",UnmodifiableAspectList.of(Aspects.EXCHANGE, 20, Aspects.MAN, 4));//ee3 author?idk
        ASPECTS_FOR_PLAYER_SPECIAL_NAMES.put("acdeasdff",UnmodifiableAspectList.of(Aspects.MECHANISM, 20, Aspects.MAN, 4));//yeah this is just for me XD
    }

    public final EntityBasicAspectGetter listener;
    EntityBasicAspectGetters(EntityBasicAspectGetter listener) {
        this.listener = listener;
    }

    public static @Unmodifiable AspectList<Aspect> getBasicAspectsForEntityType(EntityType<?> entityType){
        return CALCULATED_RESULT.computeIfAbsent(
                entityType,
                type -> {
                    var result = new HashAspectList<>();
                    result.addAll(ENTITY_TYPE_TO_ASPECTS.getOrDefault(type, UnmodifiableAspectList.EMPTY));

                    for (var entry : ADDITIONAL_ENTITY_TYPE_ASPECTS.entrySet()) {
                        if (type.is(entry.getKey())){
                            result.addAll(entry.getValue());
                        }
                    }

                    return new UnmodifiableAspectView<>(result);
                }
        );
    }

    public static void addAspectsForEntityTag(TagKey<EntityType<?>> tag, @ShouldNotModify AspectList<Aspect> aspects, boolean overrideAllBefore){
        if (overrideAllBefore){
            ADDITIONAL_ENTITY_TYPE_ASPECTS.put(tag,aspects);
        }else {
            var toPut = new HashAspectList<>();
            toPut.addAll(aspects);
            toPut.addAll(ADDITIONAL_ENTITY_TYPE_ASPECTS.getOrDefault(tag, UnmodifiableAspectList.EMPTY));
            ADDITIONAL_ENTITY_TYPE_ASPECTS.put(tag,new UnmodifiableAspectView<>(toPut));
        }
        CALCULATED_RESULT.clear();
    }

    public static void addBasicAspectsForEntity(EntityType<?> entityType, @Unmodifiable AspectList<Aspect> aspects){
        var result = new HashAspectList<>();
        result.addAll(aspects);
        result.addAll(ENTITY_TYPE_TO_ASPECTS.getOrDefault(entityType,UnmodifiableAspectList.EMPTY));
        ENTITY_TYPE_TO_ASPECTS.put(entityType,result);
    }

    public static void onDatapackReload(){
        CALCULATED_RESULT.clear();
    }

    public static @Unmodifiable AspectList<Aspect> getAspectsForPlayer(Player player){
        var playerName = getSafeStringForResourceLocation(player.getGameProfile().getName());
        var specialCase = ASPECTS_FOR_PLAYER_SPECIAL_NAMES.getOrDefault(playerName,UnmodifiableAspectList.EMPTY);
        if (!specialCase.isEmpty()){
            return specialCase;
        }
        return ASPECTS_FOR_PLAYER.computeIfAbsent(player,p -> {
            Random rand = new Random(playerName.hashCode());
            Aspect[] posa = Aspects.ALL_ASPECTS.values()
                    .toArray(new Aspect[0]);
            return UnmodifiableAspectList.of(
                    Aspects.MAN, 4,
                    posa[rand.nextInt(posa.length)], 4,
                    posa[rand.nextInt(posa.length)], 4,
                    posa[rand.nextInt(posa.length)], 4
            );
        });
    }

    //oh i dont like chinese name but i may have to do this in case?
    public static @NotNull String getSafeStringForResourceLocation(@NotNull String input) {
        input = input.toLowerCase(Locale.ROOT);
        if (input.isEmpty()) return "default";

        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            char lowerC = Character.toLowerCase(c);
            if ((lowerC >= 'a' && lowerC <= 'z') ||
                    (lowerC >= '0' && lowerC <= '9') ||
                    lowerC == '_') {
                sb.append(lowerC);
            }
            else {
                int letterIndex = Math.abs(c) % 26;
                sb.append((char) ('a' + letterIndex));
            }
        }

        return sb.toString();
    }

    public static void init(){
        addBasicAspectsForEntity(EntityType.ZOMBIE,UnmodifiableAspectList.of(
                Aspects.UNDEAD, 2,
                Aspects.MAN, 1,
                Aspects.EARTH, 1)
        );
        //TODO:Fill all in
    }
}
