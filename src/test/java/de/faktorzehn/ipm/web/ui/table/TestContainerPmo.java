package de.faktorzehn.ipm.web.ui.table;

import java.util.List;
import java.util.Optional;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

@UISection(caption = TestContainerPmo.CAPTION)
public class TestContainerPmo implements ContainerPmo<TestColumnPmo> {

    public static final String CAPTION = "container";

    private final List<TestColumnPmo> pmos = Lists.newArrayList(new TestColumnPmo(), new TestColumnPmo());

    private int pageLength = ContainerPmo.DEFAULT_PAGE_LENGTH;

    @Override
    public Class<TestColumnPmo> getItemPmoClass() {
        return TestColumnPmo.class;
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
    public Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.of(this::addItem);
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

}