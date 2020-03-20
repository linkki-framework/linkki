package org.linkki.core.vaadin.component.client;

import java.util.Arrays;

import org.linkki.core.vaadin.component.MultiformatDateField;
import org.linkki.core.vaadin.component.shared.MultiformatDateFieldState;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.datefield.PopupDateFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Client side connector for {@link MultiformatDateField}.
 */
@Connect(MultiformatDateField.class)
public class MultiformatDateFieldConnector extends PopupDateFieldConnector {

    private static final long serialVersionUID = 1L;

    @Override
    public MultiformatDateFieldWidget getWidget() {
        return (MultiformatDateFieldWidget)super.getWidget();
    }

    @Override
    public MultiformatDateFieldState getState() {
        return (MultiformatDateFieldState)super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (!Arrays.equals(getWidget().getAlternativeFormats(), getState().getAlternativeFormats())) {
            getWidget().setAlternativeFormats(getState().getAlternativeFormats());
        }
    }
}
