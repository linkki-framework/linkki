package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.dispatcher.AbstractPropertyDispatcherDecorator;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.behavior.BehaviorDependentDispatcher;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.staticvalue.StaticValueDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.core.ui.uiframework.Vaadin8ComponentWrapperFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class BindReadOnlyBehaviorAspectDefinitionTest {

    private TextField component = new TextField();
    private ComponentWrapper componentWrapper = Vaadin8ComponentWrapperFactory.INSTANCE
            .createComponentWrapper(component);
    private TestBehaviorProvider behaviorProvider = new TestBehaviorProvider();
    private PropertyDispatcher propertyDispatcher = new BehaviorDependentDispatcher(
            new StaticValueDispatcher(
                    new TestPropertyDispatcher()),
            behaviorProvider);

    @Test
    public void testCreateUiUpdater_ReadOnlyDisabled() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.DISABLED);
        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(false));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_WriteableDisabled() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.DISABLED);
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_WriteableDisabled_StateChanged() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.DISABLED);
        setDispatcherReadOnly();
        uiUpdater.apply();
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_ReadOnlyInvisible() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.INVISIBLE);
        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(false));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_ReadOnlyInvsibleTypo() {
        @SuppressWarnings("deprecation")
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.INVSIBLE);
        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(false));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_WriteableInvisible() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.INVISIBLE);
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_WriteableInvisible_StateChanged() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.INVISIBLE);
        setDispatcherReadOnly();
        uiUpdater.apply();
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_ReadOnlyWriteable() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.WRITABLE);
        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_WriteableWriteable() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.WRITABLE);
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    public void testCreateUiUpdater_WriteableWriteable_StateChanged() {
        Handler uiUpdater = createAspectUiUpdater(ReadOnlyBehaviorType.WRITABLE);
        setDispatcherReadOnly();
        uiUpdater.apply();
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    private Handler createAspectUiUpdater(ReadOnlyBehaviorType type) {
        return new CompositeAspectDefinition(
                new EnabledAspectDefinition(EnabledType.ENABLED),
                new VisibleAspectDefinition(VisibleType.VISIBLE),
                new BindReadOnlyBehaviorAspectDefinition(type))
                        .createUiUpdater(propertyDispatcher, componentWrapper);
    }

    private void setDispatcherReadOnly() {
        behaviorProvider.setReadOnlyMode(true);
    }

    private void setDispatcherWritable() {
        behaviorProvider.setReadOnlyMode(false);
    }

    private static class TestPropertyDispatcher extends AbstractPropertyDispatcherDecorator {

        @CheckForNull
        private Object boundObject = new Object();

        public TestPropertyDispatcher() {
            super(new ExceptionPropertyDispatcher("test"));
        }

        @Override
        public <T> boolean isPushable(Aspect<T> aspect) {
            return true;
        }

        @CheckForNull
        @Override
        public Object getBoundObject() {
            return boundObject;
        }

    }

    private static class TestBehaviorProvider implements PropertyBehaviorProvider {

        private boolean readOnlyMode = false;

        @Override
        public Collection<PropertyBehavior> getBehaviors() {
            return Arrays.asList(new PropertyBehavior() {

                @Override
                public boolean isWritable(Object boundObject, String property) {
                    return !isReadOnlyMode();
                }

            });
        }

        public boolean isReadOnlyMode() {
            return readOnlyMode;
        }

        public void setReadOnlyMode(boolean readOnlyMode) {
            this.readOnlyMode = readOnlyMode;
        }

    }
}