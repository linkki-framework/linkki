/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.core.binding.descriptor.aspect;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.reflection.ReflectionPropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.util.handler.Handler;

/**
 * The implementation of this interface defines the binding of an aspect of a bound property.
 * <p>
 * The linkki binding is defined for every property in a presentation model object. For example a
 * text field may be bound to the property name. That means that the value of the property in the
 * presentation model object is synchronized with the value of the corresponding text field. But the
 * value is only one aspect of many. Other aspects may be the enabled state, the visible state or
 * for example the available values of a combo box. Every one of these aspects could be defined in
 * one {@link LinkkiAspectDefinition}.
 * <p>
 * Looking deeper into the aspects in detail you see two directions of data flow:
 * <p>
 * Firstly, there might be a change in the UI field, for example a user has changed the text. From
 * point of view of the UI this leads to a push aspect because the value needs to be pushed to the
 * model. To support this case, the aspect definition needs to register an appropriate listener at
 * the UI component and react on changes. When a change occurs the aspect definition needs to
 * specify how to get the value from of the component and hand it over to
 * {@link PropertyDispatcher#push(Aspect)}. The method {@link PropertyDispatcher#push(Aspect)} is
 * responsible for writing the value to the bound model. Afterwards the aspect definition needs to
 * apply a given {@link Handler} that triggers the UI update in the {@link BindingContext}.
 * <p>
 * Secondly, the UI will be updated upon changes in model. This is called a pull-aspect because the
 * data is pulled from model. Corresponding to the previous example this is the value of the
 * component in the first place but there are a lot of other pull-aspects like "enabled", "visible",
 * "required", "available values" that could also be bound. To do so, the aspect definition provides
 * a {@link Handler} that is triggered when an UI update is requested. In this handler
 * {@link PropertyDispatcher#pull(Aspect)} is asked for the aspect value as described below in
 * {@link #createUiUpdater(PropertyDispatcher, ComponentWrapper)}. The value that is returned by
 * {@link PropertyDispatcher#pull(Aspect)} has to be set in the component. That means the aspect
 * definition describes which aspect value should be set in the component.
 * <p>
 * Because there might be many different sources for an aspect value, the aspect definition always
 * has to ask the {@link PropertyDispatcher} for the correct value. For example, the read-only
 * aspect might be defined (static) for a text field to be writable. However there could be a
 * {@link PropertyDispatcher} that overrules this value because the UI might be in a browse mode.
 * Therefore it is always important to specify the correct name of an aspect and call
 * {@link PropertyDispatcher#pull(Aspect)} even if it already contains a value. On the other hand
 * there might be a visible aspect that has no specified value, that means it is derived
 * dynamically. To give the {@link PropertyDispatcher} a hint how to resolve the value, the aspect
 * needs a name. This name might be used as suffix of a method that is called by the
 * {@link ReflectionPropertyDispatcher}. For example a dynamic visible aspect for the property
 * "address" might look for a method called "isAddressVisible()". This leads to the specification of
 * an {@link Aspect} which has the following properties:
 * <ul>
 * <li>An optional value</li>
 * <li>A name that describes the kind of aspect</li>
 * </ul>
 * <p>
 * The aspect definition always creates an {@link Aspect} with a name and optionally with a value.
 * The value might be read from the field annotation for example. But the aspect definition should
 * not use the value directly as long as it might be changed by a property dispatcher. The
 * {@link PropertyDispatcher} is responsible to return either the value, a dynamic value by calling
 * a method, or to return any other value depending on other context.
 * <p>
 * A {@link LinkkiAspectDefinition} might be specified with the annotation {@link LinkkiAspect}
 * using a {@link org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator}. The
 * creator reads the properties of the annotation and provides them for the
 * {@link LinkkiAspectDefinition}. The annotation {@link LinkkiAspect} is defined as meta-annotation
 * for example on a UI field annotation.
 * <p>
 * A {@link LinkkiAspectDefinition} is instantiated for every property in the PMO. But the same PMO
 * might be used multiple times for example for every row in a table. That means it is not allowed
 * to keep any state about the {@link PropertyDispatcher} or the {@link ComponentWrapper}.
 */
public interface LinkkiAspectDefinition {

    /**
     * This is the name to be used by {@link LinkkiAspectDefinition LinkkiAspectDefinitions}
     * defining an {@link Aspect} that directly binds the value returned by the annotated method.
     * The name is an empty String so that the {@code get<Property><Aspect>()} method defaults to
     * the annotated {@code get<Property>()} method.
     */
    public static final String VALUE_ASPECT_NAME = "";

    /**
     * This value is used as default value for aspects for which linkki can derive a value, for
     * example using the property name as a label when no label is given - this is the default value
     * for the label property in linkki's {@code @UI~} annotations.
     */
    public static final String DERIVED_BY_LINKKI = "derived.by.linkki";

    /**
     * This method is called after the aspect was created and is meant to register a listener at the
     * UI component (which is wrapped in the {@link ComponentWrapper}) to react on changes in the
     * UI.
     * <p>
     * If any changes in the UI occurred the value should be provided to
     * {@link PropertyDispatcher#push(Aspect)}. After the value is given to the
     * {@link PropertyDispatcher} the {@code modelChanged} {@link Handler} has to be triggered.
     * <p>
     * This method might be called multiple times for example if the PMO defining this aspect
     * definition is used in a table. Hence it is not allowed to keep any state of the given
     * parameters.
     * 
     * @param propertyDispatcher the {@link PropertyDispatcher} that handles the value of an
     *            {@link Aspect}
     * @param componentWrapper UI component to register a listener
     * @param modelChanged handler that updates the UI upon model changes
     * 
     * @implSpec Note that the {@link ComponentWrapper#getComponent() given component} may need to
     *           be cast to the component class suiting this aspect.
     */
    default void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelChanged) {
        // does nothing
    }

    /**
     * Creates a handler that is triggered when the UI has to be updated. When this handler is
     * called, the value for the bound aspect should be retrieved by calling
     * {@link PropertyDispatcher#pull(Aspect)}. The given {@link Aspect} has to have the name of the
     * bound aspect and if there is a value (for example defined in the UI field annotation) its
     * value is also provided within this {@link Aspect}. It is up to the {@link PropertyDispatcher}
     * to use the value or to return a value from another data source though.
     * <p>
     * The handler has to set the value that is returned by the {@link PropertyDispatcher} to the
     * {@link ComponentWrapper}.
     * <p>
     * This method might be called for different component wrappers. For example if the aspect is
     * bound to a field within a table, one aspect instance is used for all fields in one column.
     * Hence it is not allowed to keep any state of the given parameters.
     * <p>
     * For every component wrapper this method is called exactly one time when creating the UI. If
     * the value that should be bound is static and should <em>not</em> be updated with every UI
     * update it is legal to set the value directly and return {@link Handler#NOP_HANDLER}.
     * 
     * @param propertyDispatcher dispatcher (chain) that retrieves the value of an {@link Aspect} by
     *            using {@link PropertyDispatcher#pull(Aspect)}
     * @param componentWrapper UI component that may need to be updated
     * 
     * @see ModelToUiAspectDefinition
     * @see StaticModelToUiAspectDefinition
     * 
     * @implSpec Note that the {@link ComponentWrapper#getComponent() given component} may need to
     *           be cast to the component class suiting this aspect.
     */
    Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper);

    /**
     * Returns <code>true</code> if the given {@link WrapperType} is supported by this
     * {@link LinkkiAspectDefinition}. The aspect is only evaluated for a specific
     * {@link ComponentWrapper} if the {@link ComponentWrapper#getType() type of the component
     * wrapper} is supported, that means this method returns <code>true</code> for that specific
     * type.
     * <p>
     * The supported types should not be used to distinguish specific available properties of
     * components. For example it is not intended to check whether a component supports available
     * values or not. In these cases it is more likely a misconfiguration(the annotation is falsely
     * applied to a property bound to an incompatible component) which should lead to an exception.
     * Different types must be distinguished if there are multiple {@link ComponentWrapper
     * ComponentWrappers} created for a single property and a set of {@link LinkkiAspectDefinition
     * aspect definitions} must be split between those {@link ComponentWrapper ComponentWrappers}.
     * <p>
     * For example a property in a row PMO is used to describe aspects concerning the field which is
     * in a cell of a table as well as some aspects that are responsible for the whole column. In
     * this case the column and the field/cell have a different {@link WrapperType} and the aspects
     * are only applicable for the one or the other.
     * 
     * @implSpec The default implementation supports all kinds of {@link WrapperType#COMPONENT},
     *           that means all {@link WrapperType wrapper types} where
     *           {@code WrapperType.COMPONENT.isAssignableFrom(type)} return <code>true</code>.
     * 
     * @return whether a given {@link WrapperType} is supported by this
     *         {@link LinkkiAspectDefinition}
     */
    default boolean supports(WrapperType type) {
        return WrapperType.COMPONENT.isAssignableFrom(type);
    }

}
