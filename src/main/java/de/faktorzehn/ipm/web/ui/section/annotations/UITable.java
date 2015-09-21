package de.faktorzehn.ipm.web.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.server.FontAwesome;

/**
 * Annotation to customize how a {@link de.faktorzehn.ipm.web.ui.table.PmoBasedTable} is created
 * from a {@link de.faktorzehn.ipm.web.ui.table.ContainerPmo}.
 * <p>
 * Note that this annotation is optional, i.e. a container PMO does <em>not</em> have to be
 * annotated with this annotation if no customization is needed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UITable {

    /** The default icon for the add item button if no custom icon is set. */
    static final FontAwesome DEFAULT_ADD_ITEM_ICON = FontAwesome.PLUS;

    /** The header text for the delete column (if a delete column is displayed). */
    String deleteItemColumnHeader() default "";

    /** The icon for the add item button (if such a button is displayed). */
    FontAwesome addItemIcon() default FontAwesome.PLUS; // Cannot use DEFAULT_ADD_ITEM_ICON as
                                                        // explained here:
                                                        // http://stackoverflow.com/a/13253879

}
