package de.faktorzehn.ipm.web.ui.table;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

@UISection(caption = TestContainerPmo.CAPTION)
public class TestContainerPmo implements ContainerPmo<TestColumnPmo> {

    public static final String CAPTION = "container";

    private final List<TestColumnPmo> pmos = Lists.newArrayList(new TestColumnPmo(), new TestColumnPmo());

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
    public Optional<Consumer<TestColumnPmo>> getDeleteItemConsumer() {
        return deleteAction;
    }

    void setDeleteAction(Consumer<TestColumnPmo> deleteAction) {
        this.deleteAction = Optional.of(deleteAction);
    }

    @Override
    public Optional<Runnable> getAddItemAction() {
        return Optional.of(this::addItem);
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }

}