package de.faktorzehn.ipm.web.ui.table;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Sets;

import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

@UISection(caption = TestContainerPmo.CAPTION)
public class TestContainerPmo implements ContainerPmo<TestColumnPmo> {

    public static final String CAPTION = "caption";

    public static TestColumnPmo PMO_0 = new TestColumnPmo();
    public static TestColumnPmo PMO_1 = new TestColumnPmo();

    private int pageLength = ContainerPmo.DEFAULT_PAGE_LENGTH;
    final Set<PageLengthListener> pageLengthListeners = Sets.newHashSet();

    Optional<AddItemAction<TestColumnPmo>> addAction = Optional.empty();
    Optional<DeleteItemAction<TestColumnPmo>> deleteAction = Optional.empty();

    @Override
    public Class<TestColumnPmo> getItemPmoClass() {
        return TestColumnPmo.class;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public List<TestColumnPmo> getItems() {
        return Lists.newArrayList(PMO_0, PMO_1);
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