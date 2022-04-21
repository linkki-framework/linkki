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
import org.linkki.core.ui.uiframework.VaadinComponentWrapperFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class BindReadOnlyBehaviorAspectDefinitionTest {

    private final TextField component = new TextField();
    private final ComponentWrapper componentWrapper = VaadinComponentWrapperFactory.INSTANCE
            .createComponentWrapper(component);
    private final TestBehaviorProvider behaviorProvider = new TestBehaviorProvider();
    private final PropertyDispatcher propertyDispatcher = new BehaviorDependentDispatcher(
            new StaticValueDispatcher(
                    new TestPropertyDispatcher()),
            behaviorProvider);

    @Test
    void testReadOnlyBehavior_WhenReadOnly_ThenDisabled() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.DISABLED);

        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(false));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenWriteable_ThenNotDisabled() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.DISABLED);

        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_GivenReadOnlyDisabled_WhenWriteable_ThenEnabled() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.DISABLED);
        setDispatcherReadOnly();
        uiUpdater.apply();

        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenReadOnly_ThenInvisible() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.INVISIBLE);

        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(false));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenWriteable_ThenVisible() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.INVISIBLE);

        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_GivenReadOnlyInvisible_WhenWriteable_ThenWriteableVisible() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.INVISIBLE);
        setDispatcherReadOnly();
        uiUpdater.apply();

        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenReadOnly_ThenWriteable() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.WRITABLE);

        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenWriteable_ThenWritable() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.WRITABLE);

        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_GivenReadOnly_WhenWriteable_ThenWriteable() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.WRITABLE);
        setDispatcherReadOnly();
        uiUpdater.apply();

        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isEnabled(), is(true));
        assertThat(component.isVisible(), is(true));
        assertThat(component.isReadOnly(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenWriteableInvisible_ThenInvisibleAndDisabled() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.INVISIBLE, ReadOnlyBehaviorType.INVISIBLE_IF_WRITABLE);
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isVisible(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenWriteableVisible_ThenInvisibleAndDisabled() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.INVISIBLE_IF_WRITABLE);
        setDispatcherWritable();
        uiUpdater.apply();

        assertThat(component.isVisible(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenReadOnlyInvisible_ThenVisible() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.INVISIBLE, ReadOnlyBehaviorType.INVISIBLE_IF_WRITABLE);
        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isVisible(), is(false));
    }

    @Test
    void testReadOnlyBehavior_WhenReadOnlyVisible_ThenVisibleAndEnabled() {
        Handler uiUpdater = createAspectUiUpdater(VisibleType.VISIBLE, ReadOnlyBehaviorType.INVISIBLE_IF_WRITABLE);
        setDispatcherReadOnly();
        uiUpdater.apply();

        assertThat(component.isVisible(), is(true));
    }

    private Handler createAspectUiUpdater(VisibleType visibleType, ReadOnlyBehaviorType readOnlyBehaviorTypes) {
        return new CompositeAspectDefinition(
                new EnabledAspectDefinition(EnabledType.ENABLED),
                new VisibleAspectDefinition(visibleType),
                new BindReadOnlyBehaviorAspectDefinition(readOnlyBehaviorTypes))
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
        private final Object boundObject = new Object();

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