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

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.math.Point;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class TooltipListEntry<T> extends AbstractConfigListEntry<T> {
    @Nullable private Function<T, Optional<Component[]>> tooltipGetter;

    public static <V, U> Function<V, U> supplierAsFunction(Supplier<U> supplier) {
        return supplier == null ? null : v -> supplier.get();
    }
    
    @ApiStatus.Internal
    @Deprecated
    public TooltipListEntry(Component fieldName, @Nullable Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, tooltipSupplier, false);
    }

    @ApiStatus.Internal
    @Deprecated
    public TooltipListEntry(Component fieldName, @Nullable Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        this(fieldName, supplierAsFunction(tooltipSupplier), requiresRestart);
    }

    @ApiStatus.Internal
    @Deprecated
    public TooltipListEntry(Component fieldName, @Nullable Function<T, Optional<Component[]>> tooltipGetter) {
        this(fieldName, tooltipGetter, false);
    }

    @ApiStatus.Internal
    @Deprecated
    public TooltipListEntry(Component fieldName, @Nullable Function<T, Optional<Component[]>> tooltipGetter, boolean requiresRestart) {
        super(fieldName, requiresRestart);
        this.tooltipGetter = tooltipGetter;
    }
    
    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        if (isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight)) {
            Optional<Component[]> tooltip = getTooltip(mouseX, mouseY);
            if (tooltip.isPresent() && tooltip.get().length > 0)
                addTooltip(Tooltip.of(new Point(mouseX, mouseY), postProcessTooltip(tooltip.get())));
        }
    }
    
    private FormattedCharSequence[] postProcessTooltip(Component[] tooltip) {
        return Arrays.stream(tooltip).flatMap(component -> Minecraft.getInstance().font.split(component, getConfigScreen().width).stream())
                .toArray(FormattedCharSequence[]::new);
    }
    
    public Optional<Component[]> getTooltip() {
        if (tooltipGetter != null)
            return tooltipGetter.apply(getValue());
        return Optional.empty();
    }
    
    public Optional<Component[]> getTooltip(int mouseX, int mouseY) {
        return getTooltip();
    }

    @Nullable
    public Function<T, Optional<Component[]>> getTooltipGetter() {
        return tooltipGetter;
    }
    
    @Nullable
    @Deprecated
    public Supplier<Optional<Component[]>> getTooltipSupplier() {
        return tooltipGetter == null ? null : () -> {
            return tooltipGetter.apply(getValue());
        };
    }

    @Deprecated
    public void setTooltipSupplier(@Nullable Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipGetter = supplierAsFunction(tooltipSupplier);
    }
}
