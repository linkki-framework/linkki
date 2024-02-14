/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.defaults.columnbased.pmo;

import java.util.List;
import java.util.function.Supplier;

/**
 * Base class for table (section) PMOs based on a given list of model objects.
 * <p>
 * The simplest way to create a table section PMO is to pass the list of model objects in the super
 * constructor and implement {@link #createRow(Object)} to create a row PMO for a given model
 * object.
 * <p>
 * You should use the constructor {@link #SimpleTablePmo(List)} only if you have either a list that
 * never changes or if you are sure that you have a reference to the original list (if it is
 * updated). If you want to reflect any changes to your underlying list it is safer to use
 * {@link #SimpleTablePmo(Supplier)} for example like this:
 * 
 * <pre>
 * public MyTablePmo(Parent parent) {
 *     super(parent::getChildren);
 * }
 * </pre>
 * 
 * In the above example the parent object is given and the table should display all children,
 * provided by the <code>getChildren()</code> method. The method reference creates a supplier that
 * is called every time the table needs to be updated.
 * 
 * @param <MO> The model object class
 * @param <ROW> The row PMO class.
 */
public abstract class SimpleTablePmo<MO, ROW> implements ContainerPmo<ROW> {

    private final SimpleItemSupplier<ROW, ? extends MO> rowSupplier;

    /**
     * Creates a table (section) PMO showing the given model objects. Use only if you have either a
     * list that never changes or if you are sure that you have a reference to the original list (if
     * it is updated). Prefer {@link #SimpleTablePmo(Supplier)} if the list reference may change or
     * the list is a copy of the model object.
     * 
     * @see #SimpleTablePmo(Supplier)
     */
    protected SimpleTablePmo(List<? extends MO> modelObjects) {
        this(() -> modelObjects);
    }

    /**
     * Creates a table (section) PMO showing the model objects supplied by the given supplier. The
     * supplier is called every time the table gets updated.
     */
    protected SimpleTablePmo(Supplier<List<? extends MO>> modelObjectsSupplier) {
        super();
        this.rowSupplier = new SimpleItemSupplier<>(modelObjectsSupplier, mo -> createRow(mo));
    }

    /**
     * Creates a row PMO for the given model object.
     */
    protected abstract ROW createRow(MO modelObject);

    @Override
    public List<ROW> getItems() {
        return rowSupplier.get();
    }

    /**
     * The <code>SimpleTablePmoReturns</code> returns 0 as page length so that all rows are
     * displayed.
     */
    @Override
    public int getPageLength() {
        return 0;
    }

}
