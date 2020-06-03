package gg.steve.mc.batman.sf.framework.nbt.utils.annotations;

import gg.steve.mc.batman.sf.framework.nbt.utils.MinecraftVersion;
import gg.steve.mc.batman.sf.framework.nbt.NbtApiException;

import java.lang.reflect.Method;

public class CheckUtil {

    public static boolean isAvaliable(Method method) {
        if (MinecraftVersion.getVersion().getVersionId() < method.getAnnotation(AvaliableSince.class).version().getVersionId())
            throw new NbtApiException("The Method '" + method.getName() + "' is only avaliable for the Versions " + method.getAnnotation(AvaliableSince.class).version() + "+, but still got called!");
        return true;
    }
}