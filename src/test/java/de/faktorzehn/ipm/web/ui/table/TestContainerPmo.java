package de.faktorzehn.ipm.web.ui.table;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Sets;

import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

@UISection(caption = TestContainerPmo.CAPTION)
public class TestContainerPmo implements ContainerPmo<TestColumnPmo> {

    public static final String CAPTION = "container";

    private final List<TestColumnPmo> pmos = Lists.newArrayList(new TestColumnPmo(), new TestColumnPmo());
    private final Set<PageLengthListener> pageLengthListeners = Sets.newHashSet();

    private int pageLength = ContainerPmo.DEFAULT_PAGE_LENGTH;
    private Optional<Consumer<TestColumnPmo>> deleteAction = Optional.empty();

    @Override
    public Class<TestColumnPmo> getItemPmoClass() {
        return TestColumnPmo.class;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    public TestColumnPmo addItem() {
        TestColumnPmo columnPmo = new TestColumnPmo();
        pmos.add(columnPmo);
        return columnPmo;
    }

    @Override
    public List<TestColumnPmo> getItems() {
        return pmos;
    }

    @Override
    public Optional<Consumer<TestColumnPmo>> deleteItemAction() {
        return deleteAction;
    }

    void setDeleteAction(Consumer<TestColumnPmo> deleteAction) {
        this.deleteAction = Optional.of(deleteAction);
    }

    @Override
    public Optional<Supplier<TestColumnPmo>> addItemAction() {
        return Optional.of(this::addItem);
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

    public Set<PageLengthListener> pageLengthListeners() {
        return pageLengthListeners;
    }

    public void cleanPageLengthListeners() {
        pageLengthListeners.clear();
    }
}