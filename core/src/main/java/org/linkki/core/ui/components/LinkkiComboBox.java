/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.ui.components.ItemCaptionProvider.DefaultCaptionProvider;

import com.vaadin.ui.ComboBox;

/**
 * Extends {@link ComboBox} and overrides various methods in order to implement a different behavior
 * for obtaining item captions, namely to use an {@link #getItemCaptionProvider() item caption
 * provider} function when an item's caption is needed.
 */
public class LinkkiComboBox extends ComboBox {

    private static final long serialVersionUID = 967930632677587809L;

    private ItemCaptionProvider<?> itemCaptionProvider;

    /** Creates a new LinkkiComboBox that uses {@link DefaultCaptionProvider}. */
    public LinkkiComboBox() {
        this(new DefaultCaptionProvider());
    }

    /** Creates a new LinkkiComboBox that uses the given function as its item caption provider. */
    public LinkkiComboBox(ItemCaptionProvider<?> itemCaptionProvider) {
        super();
        requireNonNull(itemCaptionProvider, "itemCaptionProvider must not be null");
        this.itemCaptionProvider = itemCaptionProvider;
        pageLength = 0;
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this class does not allow explicit item
     * captions. Instead, the {@link #getItemCaptionProvider() item caption provider} is used to get
     * captions for items.
     */
    @Override
    public void setItemCaption(@Nullable Object itemId, @Nullable String caption) {
        throw new UnsupportedOperationException("LinkkiComboBox does not support explicit item captions");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this class does not allow different item
     * caption modes. Instead, the {@link #getItemCaptionProvider() item caption provider} is always
     * used to get captions for items.
     */
    @Override
    public void setItemCaptionMode(@Nullable ItemCaptionMode mode) {
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
    public void setItemCaptionProvider(ItemCaptionProvider<?> newItemCaptionProvider) {
        itemCaptionProvider = requireNonNull(newItemCaptionProvider, "newItemCaptionProvider must not be null");
    }

    /** Returns the function that is used to obtain an item's caption. */
    public ItemCaptionProvider<?> getItemCaptionProvider() {
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
    @CheckForNull
    public String getItemCaption(@Nullable Object itemId) {
        if (Objects.equals(itemId, getNullSelectionItemId())) {
            return itemCaptionProvider.getNullCaption();
        } else {
            requireNonNull(itemId, "itemId must not be null");
            return itemCaptionProvider.getUnsafeCaption(itemId);
        }
    }

}
