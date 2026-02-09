package com.linearity.opentc4.utils;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.resources.language.I18n;

//lots ofAspectVisList translateToLocal
public class StatCollector {
    @Deprecated(forRemoval = true)
    public static String translateToLocal(String key) {
        // 客户端：使用 I18n.get()
        if (Platform.getEnvironment() == Env.CLIENT) {
            return I18n.get(key);
        }
        // 服务端无法翻译，返回 key
        return key;
    }
}
