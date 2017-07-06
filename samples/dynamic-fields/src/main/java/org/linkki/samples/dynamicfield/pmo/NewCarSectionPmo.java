package org.linkki.samples.dynamicfield.pmo;

import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.samples.dynamicfield.model.Car;
import org.linkki.util.handler.Handler;

import java.util.List;

@UISection
public class NewCarSectionPmo extends CarPmo {

    private static final long serialVersionUID = 6118622651214201674L;


    private final List<Car> carStorage;
    private final Handler afterOkAction;


    public NewCarSectionPmo(Car car, List<Car> carStorage, Handler afterOkAction) {
        super(car);
        this.carStorage = carStorage;
        this.afterOkAction = afterOkAction;
    }


    @UIButton(position = 40, caption = "OK", enabled = EnabledType.DYNAMIC)
    public void ok() {
        carStorage.add(getCar());
        afterOkAction.apply();
    }


    public boolean isOkEnabled() {
        Car model = getCar();
        return model.getMake() != null && model.getModel() != null && model.getRetention() != null;
    }

}
