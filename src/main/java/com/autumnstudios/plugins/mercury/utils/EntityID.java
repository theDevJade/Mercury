package com.autumnstudios.mercury.utils;

import com.comphenix.protocol.utility.MinecraftReflection;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityID<Field> {

    private static AtomicInteger entityIDGenerator = null;

    static {
        for (java.lang.reflect.Field field : MinecraftReflection.getEntityClass().getDeclaredFields()) {
            if (field.getType() != AtomicInteger.class)
                continue;

            try {
                field.setAccessible(true);
                entityIDGenerator = (AtomicInteger) field.get(null);
                break;
            } catch (IllegalAccessException ignored) { }
        }

        if (entityIDGenerator == null)
            throw new RuntimeException("Unable to find Entity ID Field!");
    }

    public static int getNewEntityID() {
        return entityIDGenerator.incrementAndGet();
    }
}