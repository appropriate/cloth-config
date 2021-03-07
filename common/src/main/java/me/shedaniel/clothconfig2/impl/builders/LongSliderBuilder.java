/*
 * This file is part of Cloth Config.
 * Copyright (C) 2020 - 2021 shedaniel
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package me.shedaniel.clothconfig2.impl.builders;

import me.shedaniel.clothconfig2.gui.entries.LongSliderEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class LongSliderBuilder extends FieldBuilder<Long, LongSliderEntry> {
    
    private Consumer<Long> saveConsumer = null;
    private Function<Long, Optional<Component[]>> tooltipGetter = l -> Optional.empty();
    private final long value;
    private final long max;
    private final long min;
    private Function<Long, Component> textGetter = null;
    
    public LongSliderBuilder(Component resetButtonKey, Component fieldNameKey, long value, long min, long max) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
        this.max = max;
        this.min = min;
    }
    
    public LongSliderBuilder setErrorSupplier(Function<Long, Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public LongSliderBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public LongSliderBuilder setTextGetter(Function<Long, Component> textGetter) {
        this.textGetter = textGetter;
        return this;
    }
    
    public LongSliderBuilder setSaveConsumer(Consumer<Long> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public LongSliderBuilder setDefaultValue(Supplier<Long> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public LongSliderBuilder setDefaultValue(long defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public LongSliderBuilder setTooltipGetter(Function<Long, Optional<Component[]>> tooltipGetter) {
        this.tooltipGetter = tooltipGetter;
        return this;
    }

    @Deprecated
    public LongSliderBuilder setTooltipSupplier(Function<Long, Optional<Component[]>> tooltipGetter) {
        return setTooltipGetter(tooltipGetter);
    }
    
    public LongSliderBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipGetter = i -> tooltipSupplier.get();
        return this;
    }
    
    public LongSliderBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipGetter = i -> tooltip;
        return this;
    }
    
    public LongSliderBuilder setTooltip(Component... tooltip) {
        this.tooltipGetter = i -> Optional.ofNullable(tooltip);
        return this;
    }
    
    
    @NotNull
    @Override
    public LongSliderEntry build() {
        LongSliderEntry entry = new LongSliderEntry(getFieldNameKey(), min, max, value, saveConsumer, getResetButtonKey(), defaultValue, tooltipGetter, isRequireRestart());
        if (textGetter != null)
            entry.setTextGetter(textGetter);
        if (errorSupplier != null)
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        return entry;
    }
    
}
