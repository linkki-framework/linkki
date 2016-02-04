/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.ComboBox;

/**
 * Extends {@link ComboBox} and overrides various methods in order to implement a different behavior
 * for obtaining item captions, namely to use an {@link #getItemCaptionProvider() item caption
 * provider} function when an item's caption is needed.
 */
public class LinkkiComboBox extends ComboBox {

    private static final long serialVersionUID = 967930632677587809L;

    private Function<Object, String> itemCaptionProvider;

    /** Creates a new LinkkiComboBox that uses {@link IdAndNameCaptionProvider}. */
    public LinkkiComboBox() {
        this(new IdAndNameCaptionProvider());
    }

    /** Creates a new LinkkiComboBox that uses the given function as its item caption provider. */
    public LinkkiComboBox(@Nonnull Function<Object, String> itemCaptionProvider) {
        super();
        this.itemCaptionProvider = requireNonNull(itemCaptionProvider);
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this class does not allow explicit item
     * captions. Instead, the {@link #getItemCaptionProvider() item caption provider} is used to get
     * captions for items.
     */
    @Override
    public void setItemCaption(Object itemId, String caption) {
        throw new UnsupportedOperationException("LinkkiComboBox does not support explicit item captions");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this class does not allow different item
     * caption modes. Instead, the {@link #getItemCaptionProvider() item caption provider} is always
     * used to get captions for items.
     */
    @Override
    public void setItemCaptionMode(ItemCaptionMode mode) {
        throw new UnsupportedOperationException("LinkkiComboBox does not allow to specify the item caption mode");
    }

    /**
     * Returns {@link com.vaadin.ui.AbstractSelect.ItemCaptionMode#ITEM} as the closest
     * approximation of the behavior this class implements, namely the usage of the
     * {@link #getItemCaptionProvider() item caption provider} to get captions for items.
     */
    @Override
    public ItemCaptionMode getItemCaptionMode() {
        return ItemCaptionMode.ITEM;
    }

    /** Sets the function to use as the item caption provider. */
    public void setItemCaptionProvider(Function<Object, String> newItemCaptionProvider) {
        itemCaptionProvider = requireNonNull(newItemCaptionProvider);
    }

    /** Returns the function that is used to obtain an item's caption. */
    public Function<Object, String> getItemCaptionProvider() {
        return itemCaptionProvider;
    }

    /**
     * Returns the caption of the given item using this object's {@link #getItemCaptionProvider()
     * item caption provider}.
     * 
     * @param itemId an item
     * @return the item's caption
     */
    @Override
    public String getItemCaption(Object itemId) {
        if (itemId == null) {
            return StringUtils.EMPTY;
        }
        return itemCaptionProvider.apply(itemId);
    }

    /**
     * A caption provider that returns a string in the format "name [id]" and invokes methods
     * {@code getName} and {@code getId} to obtain these values.
     */
    public static class IdAndNameCaptionProvider implements Function<Object, String> {

        @Override
        public String apply(Object o) {
            return getName(o) + " [" + getId(o) + "]";
        }

        private String getId(Object value) {
            return getPropertyValue(value, "getId");
        }

        private String getName(Object value) {
            return getPropertyValue(value, "getName");
        }

        private String getPropertyValue(Object value, String methodName) {
            try {
                Method method = value.getClass().getDeclaredMethod(methodName);
                return (String)method.invoke(value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new IllegalStateException("Can't get value from method " + methodName + ", value was " + value);
            }
        }

    }

}
