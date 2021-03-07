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
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class StringListEntry extends TextFieldListEntry<String> {
    
    private final Consumer<String> saveConsumer;
    
    @ApiStatus.Internal
    @Deprecated
    public StringListEntry(Component fieldName, String value, Component resetButtonKey, Supplier<String> defaultValue, Consumer<String> saveConsumer) {
        this(fieldName, value, resetButtonKey, defaultValue, saveConsumer, null);
    }
    
    @ApiStatus.Internal
    @Deprecated
    public StringListEntry(Component fieldName, String value, Component resetButtonKey, Supplier<String> defaultValue, Consumer<String> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, value, resetButtonKey, defaultValue, saveConsumer, tooltipSupplier, false);
    }

    @ApiStatus.Internal
    @Deprecated
    public StringListEntry(Component fieldName, String value, Component resetButtonKey, Supplier<String> defaultValue, Consumer<String> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        this(fieldName, value, resetButtonKey, defaultValue, saveConsumer, supplierAsFunction(tooltipSupplier), requiresRestart);
    }

    @ApiStatus.Internal
    @Deprecated
    public StringListEntry(Component fieldName, String value, Component resetButtonKey, Supplier<String> defaultValue, Consumer<String> saveConsumer, Function<String, Optional<Component[]>> tooltipGetter, boolean requiresRestart) {
        super(fieldName, value, resetButtonKey, defaultValue, tooltipGetter, requiresRestart);
        this.saveConsumer = saveConsumer;
    }
    
    @Override
    public String getValue() {
        return textFieldWidget.getValue();
    }
    
    @Override
    public void save() {
        if (saveConsumer != null)
            saveConsumer.accept(getValue());
    }
    
    @Override
    protected boolean isMatchDefault(String text) {
        return getDefaultValue().isPresent() && text.equals(getDefaultValue().get());
    }
    
}
