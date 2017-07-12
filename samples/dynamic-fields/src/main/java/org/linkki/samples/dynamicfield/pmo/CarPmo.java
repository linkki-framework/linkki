package org.linkki.samples.dynamicfield.pmo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.components.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIDoubleField;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.samples.dynamicfield.model.Car;
import org.linkki.samples.dynamicfield.model.CarModels;
import org.linkki.samples.dynamicfield.model.CarType;

import com.vaadin.ui.UI;

public abstract class CarPmo implements PresentationModelObject, Serializable {

    private static final long serialVersionUID = -3117310513102291997L;


    private final Car car;


    public CarPmo(Car car) {
        this.car = car;
    }


    @ModelObject
    public Car getCar() {
        return car;
    }


    @UITextField(position = 10, label = "Make", modelAttribute = Car.PROPERTY_MAKE, required = RequiredType.REQUIRED_IF_ENABLED)
    public void make() {
        /* model binding */
    }

    // tag::ui-combobox[]
    @UIComboBox(position = 20, 
            label = "Model", 
            modelAttribute = Car.PROPERTY_MODEL, 
            required = RequiredType.REQUIRED_IF_ENABLED, 
            content = AvailableValuesType.DYNAMIC, 
            itemCaptionProvider = ToStringCaptionProvider.class)
    public void model() {
        /* model binding */
    }
    // end::ui-combobox[]

    public List<String> getModelAvailableValues() {
        return CarModels.getModels(getCar().getMake()).orElse(Collections.singletonList("Sonstige"));
    }

    // tag::ui-dynamic-field[]
    @UIDoubleField(position = 30,
            label = "Retention",
            modelAttribute = Car.PROPERTY_RETENTION,
            required = RequiredType.REQUIRED_IF_ENABLED)
    @UIComboBox(position = 30,
            label = "Retention",
            modelAttribute = Car.PROPERTY_RETENTION,
            required = RequiredType.REQUIRED_IF_ENABLED,
            content = AvailableValuesType.DYNAMIC,
            itemCaptionProvider = RetentionCaptionProvider.class)
    public void retention() {
        /* model binding */
    }

    public List<Double> getRetentionAvailableValues() {
        return Arrays.asList(2_000.0, 5_000.0, 10_000.0);
    }

    public Class<?> getRetentionComponentType() {
        return car.getCarType() == CarType.STANDARD ? UIDoubleField.class : UIComboBox.class;
    }
    // end::ui-dynamic-field[]

    // must be public, otherwise linkki can not access it
    public final static class RetentionCaptionProvider implements ItemCaptionProvider<Double> {

        @Override
        public String getCaption(Double value) {
            return String.format(UI.getCurrent().getLocale(), "%,.2f", value);
        }
    }
}
