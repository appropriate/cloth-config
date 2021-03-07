package me.shedaniel.clothconfig2.gui.entries;

import me.shedaniel.clothconfig2.gui.widget.ManagedSliderWidget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A config entry list consisting of bounded values that use one
 * {@link AbstractSliderListCell} per entry.
 *
 * Any bounded values that can be respresented as a {@code double} can be
 * listed using this entry list by implementing a specialized subclass of
 * {@link AbstractSliderListCell}.
 *
 * @param <T>    the configuration object type
 * @param <C>    the cell type
 * @param <SELF> the "curiously recurring template pattern" type parameter
 * @see AbstractListListEntry
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractSliderListEntry<T, C extends AbstractSliderListEntry.AbstractSliderListCell<T, C, SELF>, SELF extends AbstractSliderListEntry<T, C, SELF>> extends AbstractListListEntry<T, C, SELF> {
    protected final T minimum, maximum, cellDefaultValue;
    protected Function<T, Component> textGetter;

    public AbstractSliderListEntry(Component fieldName, T minimum, T maximum, List<T> value, boolean defaultExpanded, Function<List<T>, Optional<Component[]>> tooltipGetter, Consumer<List<T>> saveConsumer, Supplier<List<T>> defaultValue, T cellDefaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<T, SELF, C> createNewCell) {
        super(fieldName, value, defaultExpanded, tooltipGetter, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, createNewCell);

        this.minimum = requireNonNull(minimum);
        this.maximum = requireNonNull(maximum);
        this.cellDefaultValue = requireNonNull(cellDefaultValue);
    }

    public SELF setTextGetter(Function<T, Component> textGetter) {
        this.textGetter = textGetter;
        this.cells.forEach(c -> c.sliderWidget.updateMessage());
        return self();
    }

    /**
     * A config entry within a parent {@link AbstractSliderListEntry}
     * containing a single bounded value with an {@link ManagedSliderWidget}
     * for user display and input.
     *
     * Any bounded value that can be respresented as a {@code double} can be
     * listed by subclassing this class and its parent {@link AbstractSliderListEntry},
     * implementing the {@link #getValueForSlider()} and
     * {@link #setValueFromSlider(double)} methods.
     *
     * @param <T>          the configuration object type
     * @param <SELF>       the "curiously recurring template pattern" type parameter for this class
     * @param <OUTER_SELF> the "curiously recurring template pattern" type parameter for the outer class
     * @see AbstractSliderListEntry
     */
    public abstract static class AbstractSliderListCell<T, SELF extends AbstractSliderListEntry.AbstractSliderListCell<T, SELF, OUTER_SELF>, OUTER_SELF extends AbstractSliderListEntry<T, SELF, OUTER_SELF>> extends AbstractListListEntry.AbstractListCell<T, SELF, OUTER_SELF> {
        protected final ManagedSliderWidget sliderWidget;
        private boolean isSelected;

        public AbstractSliderListCell(T value, OUTER_SELF listListEntry) {
            super(value, listListEntry);
            this.sliderWidget = new ManagedSliderWidget(0, 0, 152, 20, sliderContext()) {
                @Override
                protected void renderBgHighlight(PoseStack matrices, Minecraft client, int mouseX, int mouseY) {
                    if (isSelected && listListEntry.isEditable())
                        fill(matrices, x, y + 19, x + width, y + 20, getConfigError().isPresent() ? 0xffff5555 : 0xffa0a0a0);
                }

            };
        }

        protected abstract double getValueForSlider();

        protected abstract void setValueFromSlider(double value);

        protected void syncValueToSlider() {
            sliderWidget.syncValueFromContext();
        }

        protected Component getValueForMessage() {
            if (listListEntry.textGetter == null) {
                return null;
            } else {
                return listListEntry.textGetter.apply(getValue());
            }
        }

        @Override
        public void onAdd() {
            syncValueToSlider();
            sliderWidget.updateMessage();
        }

        @Override
        public void updateSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        @Override
        public Optional<Component> getError() {
            return Optional.empty();
        }

        @Override
        public int getCellHeight() {
            return 22;
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isFocusedCell, float delta) {
            sliderWidget.x = x;
            sliderWidget.y = y;
            sliderWidget.setWidth(entryWidth - 12);
            sliderWidget.active = listListEntry.isEditable();
            sliderWidget.render(matrices, mouseX, mouseY, delta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(sliderWidget);
        }

        private ManagedSliderWidget.Context sliderContext() {
            return new ManagedSliderWidget.Context() {
                public Component message() {
                    return getValueForMessage();
                }

                public double value() {
                    return getValueForSlider();
                }

                public void valueApplied(double value) {
                    setValueFromSlider(value);
                }

                public boolean editable() {
                    return listListEntry.isEditable();
                }
            };
        }
    }
}
