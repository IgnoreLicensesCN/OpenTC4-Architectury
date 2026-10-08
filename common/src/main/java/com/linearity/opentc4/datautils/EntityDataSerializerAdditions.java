package com.linearity.opentc4.datautils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EntityDataSerializerAdditions {
    public static final EntityDataSerializer<List<ItemStack>> ITEM_STACK_NON_NULL_LIST = new EntityDataSerializer.ForValueType<>() {
        public void write(FriendlyByteBuf friendlyByteBuf, List<ItemStack> list) {
            friendlyByteBuf.writeVarInt(list.size());
            for (ItemStack stack : list) {
                EntityDataSerializers.ITEM_STACK.write(friendlyByteBuf, stack);
            }
        }

        public @NotNull List<ItemStack> read(FriendlyByteBuf friendlyByteBuf) {
            int i = friendlyByteBuf.readVarInt();
            List<ItemStack> result = new ArrayList<>(i);
            for (int j = 0; j < i; j++) {
                result.add(EntityDataSerializers.ITEM_STACK.read(friendlyByteBuf));
            }
            return result;
        }
    };

    public static void registerSerializers(){
        EntityDataSerializers.registerSerializer(ITEM_STACK_NON_NULL_LIST);
    }
}
