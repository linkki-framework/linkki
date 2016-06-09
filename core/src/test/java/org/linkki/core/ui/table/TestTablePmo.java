package org.linkki.core.ui.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.linkki.core.ButtonPmo;
import org.linkki.core.TableFooterPmo;
import org.linkki.core.ui.section.annotations.UISection;

@UISection(caption = TestTablePmo.CAPTION)
public class TestTablePmo implements ContainerPmo<TestRowPmo> {

    public static final String CAPTION = "container";

    private final List<TestRowPmo> pmos = new ArrayList<>();

    private int pageLength = ContainerPmo.DEFAULT_PAGE_LENGTH;

    private TableFooterPmo footerPmo = null;

    @Override
    public Class<TestRowPmo> getItemPmoClass() {
        return TestRowPmo.class;
    }

    public TestRowPmo addItem() {
        TestRowPmo columnPmo = new TestRowPmo(this);
        pmos.add(columnPmo);
        return columnPmo;
    }

    public void deleteItem(TestRowPmo columnPmo) {
        pmos.remove(columnPmo);
    }

    @Override
    public List<TestRowPmo> getItems() {
        return pmos;
    }

    @Override
    public Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.of(ButtonPmo.newAddButton(this::addItem));
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional.ofNullable(footerPmo);
    }

    public void setFooterPmo(TableFooterPmo footerPmo) {
        this.footerPmo = footerPmo;
    }
}