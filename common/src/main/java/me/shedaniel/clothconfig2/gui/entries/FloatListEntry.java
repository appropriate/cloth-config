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

package me.shedaniel.clothconfig2.gui.entries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class FloatListEntry extends TextFieldListEntry<Float> {
    
    private static final Function<String, String> stripCharacters = s -> {
        StringBuilder stringBuilder_1 = new StringBuilder();
        char[] var2 = s.toCharArray();
        int var3 = var2.length;
        
        for (char c : var2)
            if (Character.isDigit(c) || c == '-' || c == '.')
                stringBuilder_1.append(c);
        
        return stringBuilder_1.toString();
    };
    private float minimum, maximum;
    private final Consumer<Float> saveConsumer;
    
    @ApiStatus.Internal
    @Deprecated
    public FloatListEntry(Component fieldName, Float value, Component resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer) {
        this(fieldName, value, resetButtonKey, defaultValue, saveConsumer, null);
    }
    
    @ApiStatus.Internal
    @Deprecated
    public FloatListEntry(Component fieldName, Float value, Component resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer, Function<Float, Optional<Component[]>> tooltipGetter) {
        this(fieldName, value, resetButtonKey, defaultValue, saveConsumer, tooltipGetter, false);
    }

    @ApiStatus.Internal
    @Deprecated
    public FloatListEntry(Component fieldName, Float value, Component resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        this(fieldName, value, resetButtonKey, defaultValue, saveConsumer, supplierAsFunction(tooltipSupplier), requiresRestart);
    }

    @ApiStatus.Internal
    @Deprecated
    public FloatListEntry(Component fieldName, Float value, Component resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer, Function<Float, Optional<Component[]>> tooltipGetter, boolean requiresRestart) {
        super(fieldName, value, resetButtonKey, defaultValue, tooltipGetter, requiresRestart);
        this.minimum = -Float.MAX_VALUE;
        this.maximum = Float.MAX_VALUE;
        this.saveConsumer = saveConsumer;
    }
    
    @Override
    protected String stripAddText(String s) {
        return stripCharacters.apply(s);
    }
    
    @Override
    protected void textFieldPreRender(EditBox widget) {
        try {
            double i = Float.parseFloat(textFieldWidget.getValue());
            if (i < minimum || i > maximum)
                widget.setTextColor(16733525);
            else
                widget.setTextColor(14737632);
        } catch (NumberFormatException ex) {
            widget.setTextColor(16733525);
        }
    }
    
    @Override
    protected boolean isMatchDefault(String text) {
        return getDefaultValue().isPresent() && text.equals(defaultValue.get().toString());
    }
    
    public FloatListEntry setMinimum(float minimum) {
        this.minimum = minimum;
        return this;
    }
    
    public FloatListEntry setMaximum(float maximum) {
        this.maximum = maximum;
        return this;
    }
    
    @Override
    public void save() {
        if (saveConsumer != null)
            saveConsumer.accept(getValue());
    }
    
    @Override
    public Float getValue() {
        try {
            return Float.valueOf(textFieldWidget.getValue());
        } catch (Exception e) {
            return 0f;
        }
    }
    
    @Override
    public Optional<Component> getError() {
        try {
            float i = Float.parseFloat(textFieldWidget.getValue());
            if (i > maximum)
                return Optional.of(new TranslatableComponent("text.cloth-config.error.too_large", maximum));
            else if (i < minimum)
                return Optional.of(new TranslatableComponent("text.cloth-config.error.too_small", minimum));
        } catch (NumberFormatException ex) {
            return Optional.of(new TranslatableComponent("text.cloth-config.error.not_valid_number_float"));
        }
        return super.getError();
    }
}
