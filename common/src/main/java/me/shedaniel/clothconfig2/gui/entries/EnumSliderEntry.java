package me.shedaniel.clothconfig2.gui.entries;

import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnumSliderEntry<T extends Enum<?>> extends AbstractSliderEntry<T, EnumSliderEntry<T>> {
    private final T[] enumValues;
    private final double maxOrdinal;
    private T value;

    public EnumSliderEntry(Component fieldName, T[] enumValues, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer, Function<T, Optional<Component[]>> tooltipGetter, boolean requiresRestart) {
        // NOTE: Assumes enum is non-empty
        super(fieldName, enumValues[0], enumValues[enumValues.length - 1], value, resetButtonKey, defaultValue, saveConsumer, tooltipGetter, requiresRestart);
        this.enumValues = enumValues;
        this.maxOrdinal = enumValues.length - 1;
        this.value = value;
    }

    @Override
    protected EnumSliderEntry<T> self() {
        return this;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    protected void setValue(T value) {
        this.value = value;
    }

    @Override
    protected double getValueForSlider() {
        return this.value.ordinal() / maxOrdinal;
    }

    @Override
    protected void setValueFromSlider(double value) {
        this.value = enumValues[(int) (value * maxOrdinal)];
    }
}
