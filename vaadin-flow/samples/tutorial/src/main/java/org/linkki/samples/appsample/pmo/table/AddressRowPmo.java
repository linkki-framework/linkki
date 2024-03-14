package org.linkki.samples.appsample.pmo.table;

import java.util.function.Consumer;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.samples.appsample.model.Address;
import org.linkki.samples.appsample.pmo.dialog.AddressPmo;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

// tag::rowAfterExtraction[]
public class AddressRowPmo extends AddressPmo {

    // tag::deleteButton[]
    private Consumer<Address> deleteConsumer;

    // end::deleteButton[]
    public AddressRowPmo(Address address, Consumer<Address> deleteConsumer) {
        super(address);
        this.deleteConsumer = deleteConsumer;
    }

    @BindReadOnlyBehavior(value = ReadOnlyBehaviorType.WRITABLE)
    @UITableColumn(width = 50)
    // tag::deleteButton[]
    @UIButton(position = 60, captionType = CaptionType.NONE, showIcon = true, icon = VaadinIcon.TRASH, variants = {
            ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL })
    public void deleteButton() {
        deleteConsumer.accept(address);
    }
    // end::deleteButton[]
}
// end::rowAfterExtraction[]
