package org.linkki.samples.dynamicfield.pmo;

import org.linkki.core.ButtonPmo;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.SimpleItemSupplier;
import org.linkki.core.ui.table.TableFooterPmo;
import org.linkki.samples.dynamicfield.model.Car;
import org.linkki.util.handler.Handler;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@UISection(caption = "Cars")
public class CarTablePmo implements ContainerPmo<CarRowPmo>, Serializable {

    private static final long serialVersionUID = 8770409786960309300L;


    private final Handler addCarAction;
    private final SimpleItemSupplier<CarRowPmo, Car> items;

    //tag::table-footer[]
    private final TableFooterPmo footer;

    public CarTablePmo(List<Car> carStorage, Handler addCarAction) {
        this.addCarAction = addCarAction;
        this.items = new SimpleItemSupplier<>(() -> carStorage, CarRowPmo::new);

        this.footer = c -> calculateTotalRetention(c, carStorage);
    }
    //end::table-footer[]

    @Override
    public List<CarRowPmo> getItems() {
        return items.get();
    }

    @Override
    public Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.of(ButtonPmo.newAddButton(addCarAction));
    }

    @Override
    public int getPageLength() {
        return Math.min(ContainerPmo.super.getPageLength(), getItems().size());
    }
    //tag::table-footer[]

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional.of(footer);
    }

    private String calculateTotalRetention(String column, List<Car> cars) {

        switch (column) {
            case Car.PROPERTY_RETENTION:

                return String.format("%,.2f", cars.stream()
                        .mapToDouble(Car::getRetention)
                        .sum());

            case Car.PROPERTY_CAR_TYPE:

                return "Total Retention:";

            default:

                return "";
        }
    }
    //end::table-footer[]
}
