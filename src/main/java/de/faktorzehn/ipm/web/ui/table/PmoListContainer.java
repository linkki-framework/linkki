/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vaadin.viritin.ListContainer;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * This implementation of {@link Container} wraps a {@link ContainerPmo} to provide its content to a
 * {@link Table}.
 *
 * @author dirmeier
 */
public class PmoListContainer<T extends PresentationModelObject> extends ListContainer<T> {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private final ContainerPmo<T> containerPmo;

    private final List<String> porpertyIds = new ArrayList<>();

    public PmoListContainer(ContainerPmo<T> containerPmo) {
        super(containerPmo.getItems());
        this.containerPmo = containerPmo;
    }

    protected void updateItems() {
        setCollection(getContainerPmo().getItems());
    }

    public ContainerPmo<T> getContainerPmo() {
        return containerPmo;
    }

    public void addColumn(String id) {
        porpertyIds.add(id);
    }

    @Override
    public Collection<String> getContainerPropertyIds() {
        return porpertyIds;
    }

    @Override
    public String toString() {
        return "Container for " + containerPmo.toString();
    }
}
