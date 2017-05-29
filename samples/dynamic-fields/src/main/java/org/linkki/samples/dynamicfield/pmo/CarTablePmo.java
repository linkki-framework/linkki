package org.linkki.samples.dynamicfield.pmo;

import org.linkki.core.ButtonPmo;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.SimpleItemSupplier;
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


    public CarTablePmo(List<Car> carStorage, Handler addCarAction) {
        this.addCarAction = addCarAction;
        this.items = new SimpleItemSupplier<>(() -> carStorage, CarRowPmo::new);
    }


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
}
