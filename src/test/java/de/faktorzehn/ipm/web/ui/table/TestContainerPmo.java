package de.faktorzehn.ipm.web.ui.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.thirdparty.guava.common.collect.Sets;

import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

@UISection(caption = TestContainerPmo.CAPTION)
public class TestContainerPmo implements ContainerPmo<TestColumnPmo> {

    public static final String CAPTION = "container";

    private List<TestColumnPmo> pmos = new ArrayList<TestColumnPmo>();
    {
        pmos.add(new TestColumnPmo());
        pmos.add(new TestColumnPmo());
    }

    private int pageLength = ContainerPmo.DEFAULT_PAGE_LENGTH;
    final Set<PageLengthListener> pageLengthListeners = Sets.newHashSet();

    Optional<AddItemAction<TestColumnPmo>> addAction = Optional.of(this::addItem);
    Optional<DeleteItemAction<TestColumnPmo>> deleteAction = Optional.empty();

    @Override
    public Class<TestColumnPmo> getItemPmoClass() {
        return TestColumnPmo.class;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    private TestColumnPmo addItem() {
        TestColumnPmo columnPmo = new TestColumnPmo();
        pmos.add(columnPmo);
        return columnPmo;
    }

    @Override
    public List<TestColumnPmo> getItems() {
        return pmos;
    }

    @Override
    public Optional<DeleteItemAction<TestColumnPmo>> deleteItemAction() {
        return deleteAction;
    }

    void setDeleteAction(DeleteItemAction<TestColumnPmo> deleteAction) {
        this.deleteAction = Optional.of(deleteAction);
    }

    @Override
    public Optional<AddItemAction<TestColumnPmo>> addItemAction() {
        return addAction;
    }

    void setAddAction(AddItemAction<TestColumnPmo> addAction) {
        this.addAction = Optional.of(addAction);
    }

    @Override
    public void setPageLength(final int newPageLength) {
        pageLength = newPageLength;
        pageLengthListeners.forEach(l -> l.pageLengthChanged(newPageLength));
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }

    @Override
    public void addPageLengthListener(PageLengthListener listener) {
        pageLengthListeners.add(listener);

    }

    @Override
    public void removePageLengthListener(PageLengthListener listener) {
        pageLengthListeners.remove(listener);
    }
}