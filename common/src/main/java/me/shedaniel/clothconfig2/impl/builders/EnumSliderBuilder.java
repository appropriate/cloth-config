package me.shedaniel.clothconfig2.impl.builders;

import me.shedaniel.clothconfig2.gui.entries.EnumSliderEntry;

import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class EnumSliderBuilder<T extends Enum<?>> extends FieldBuilder<T, EnumSliderEntry<T>> {
    private final T[] enumValues;
    private final T value;
    private Supplier<T> defaultValue;
    private Consumer<T> saveConsumer = null;
    private Function<T, Component> textGetter = null;
    private Function<T, Optional<Component[]>> tooltipGetter = i -> Optional.empty();

    public EnumSliderBuilder(Component resetButtonKey, Component fieldNameKey, Class<T> enumClass, T value) {
        this(resetButtonKey, fieldNameKey, enumClass.getEnumConstants(), value);
    }

    public EnumSliderBuilder(Component resetButtonKey, Component fieldNameKey, T[] enumValues, T value) {
        super(resetButtonKey, fieldNameKey);

        this.enumValues = requireNonNull(enumValues);
        this.value = value;
    }

    public EnumSliderBuilder<T> setDefaultValue(Supplier<T> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public EnumSliderBuilder<T> setDefaultValue(T defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public EnumSliderBuilder<T> setSaveConsumer(Consumer<T> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public EnumSliderBuilder<T> setTextGetter(Function<T, Component> textGetter) {
        this.textGetter = textGetter;
        return this;
    }

    public EnumSliderBuilder<T> setTooltipGetter(Function<T, Optional<Component[]>> tooltipGetter) {
        this.tooltipGetter = tooltipGetter;
        return this;
    }

    public EnumSliderBuilder<T> setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipGetter = i -> tooltipSupplier.get();
        return this;
    }

    public EnumSliderBuilder<T> setTooltip(Component... tooltip) {
        this.tooltipGetter = i -> Optional.ofNullable(tooltip);
        return this;
    }

    public EnumSliderEntry<T> build() {
        EnumSliderEntry<T> entry = new EnumSliderEntry<>(
            getFieldNameKey(), enumValues, value, getResetButtonKey(), defaultValue,
            saveConsumer, tooltipGetter, isRequireRestart());

        if (textGetter != null) {
            entry.setTextGetter(textGetter);
        }

        return entry;
    }
}
