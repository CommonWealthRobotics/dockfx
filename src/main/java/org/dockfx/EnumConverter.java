package org.dockfx;

public class EnumConverter<T extends Enum<T>> {
    private final Class<T> enumType;

    public EnumConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    public T convert(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Enum.valueOf(enumType, value.toUpperCase());
    }
}