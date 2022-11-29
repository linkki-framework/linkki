# Vaadin7 Prerequisites
s|org.linkki.framework.ui.LinkkiStyles|org.linkki.framework.ui.LinkkiApplicationTheme|g
s|LinkkiStyles|LinkkiApplicationTheme|g

s|org.linkki.core.binding.aspect.Aspect([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.Aspect\1|g
s|org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition\1|g
s|org.linkki.core.binding.aspect.AspectAnnotationReader([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader\1|g
s|org.linkki.core.binding.aspect.AspectDefinitionCreator([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator\1|g
s|org.linkki.core.binding.aspect.LinkkiAspect([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect\1|g
s|org.linkki.core.binding.aspect.LinkkiAspects([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspects\1|g
s|org.linkki.core.binding.aspect.definition.ApplicableAspectDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.base.ApplicableAspectDefinition\1|g
s|org.linkki.core.binding.aspect.definition.ApplicableTypeAspectDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.base.ApplicableTypeAspectDefinition\1|g
s|org.linkki.core.binding.aspect.definition.CompositeAspectDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition\1|g
s|org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition\1|g
s|org.linkki.core.binding.aspect.definition.StaticModelToUiAspectDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.BindingDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.LinkkiBindingDefinition([[:punct:][:space:]])|org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition\1|g
s|org.linkki.core.binding.property.BoundProperty([[:punct:][:space:]])|org.linkki.core.binding.descriptor.property.BoundProperty\1|g
s|org.linkki.core.binding.property.BoundPropertyAnnotationReader([[:punct:][:space:]])|org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader\1|g
s|org.linkki.core.binding.property.BoundPropertyCreator([[:punct:][:space:]])|org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator\1|g
s|org.linkki.core.binding.property.LinkkiBoundProperty([[:punct:][:space:]])|org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty\1|g
s|org.linkki.core.binding.PropertyDispatcherFactory([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.PropertyDispatcherFactory\1|g
s|org.linkki.core.binding.dispatcher.BehaviorDependentDispatcher([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.behavior.BehaviorDependentDispatcher\1|g
s|org.linkki.core.binding.behavior.PropertyBehavior([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.behavior.PropertyBehavior\1|g
s|org.linkki.core.binding.dispatcher.PropertyBehaviorProvider([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider\1|g
s|org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher\1|g
s|org.linkki.core.binding.dispatcher.PropertyNamingConvention([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.PropertyNamingConvention\1|g
s|org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.ReflectionPropertyDispatcher\1|g
s|org.linkki.core.binding.dispatcher.accessor.AbstractMethod([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.accessor.AbstractMethod\1|g
s|org.linkki.core.binding.dispatcher.accessor.InvokeMethod([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.accessor.InvokeMethod\1|g
s|org.linkki.core.binding.dispatcher.accessor.PropertyAccessDescriptor([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessDescriptor\1|g
s|org.linkki.core.binding.dispatcher.accessor.PropertyAccessor([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessor\1|g
s|org.linkki.core.binding.dispatcher.accessor.PropertyAccessorCache([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessorCache\1|g
s|org.linkki.core.binding.dispatcher.accessor.ReadMethod([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.accessor.ReadMethod\1|g
s|org.linkki.core.binding.dispatcher.accessor.WriteMethod([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.reflection.accessor.WriteMethod\1|g
s|org.linkki.core.binding.dispatcher.StaticValueDispatcher([[:punct:][:space:]])|org.linkki.core.binding.dispatcher.staticvalue.StaticValueDispatcher\1|g
s|org.linkki.core.binding.BindingManager([[:punct:][:space:]])|org.linkki.core.binding.manager.BindingManager\1|g
s|org.linkki.core.binding.DefaultBindingManager([[:punct:][:space:]])|org.linkki.core.binding.manager.DefaultBindingManager\1|g
s|org.linkki.core.binding.UiUpdateObserver([[:punct:][:space:]])|org.linkki.core.binding.manager.UiUpdateObserver\1|g
s|org.linkki.core.message.Message([[:punct:][:space:]])|org.linkki.core.binding.validation.message.Message\1|g
s|org.linkki.core.message.MessageList([[:punct:][:space:]])|org.linkki.core.binding.validation.message.MessageList\1|g
s|org.linkki.core.message.MessageListCollector([[:punct:][:space:]])|org.linkki.core.binding.validation.message.MessageListCollector\1|g
s|org.linkki.core.message.ObjectProperty([[:punct:][:space:]])|org.linkki.core.binding.validation.message.ObjectProperty\1|g
s|org.linkki.core.message.Severity([[:punct:][:space:]])|org.linkki.core.binding.validation.message.Severity\1|g
s|org.linkki.core.ui.components.ComponentWrapper([[:punct:][:space:]])|org.linkki.core.binding.wrapper.ComponentWrapper\1|g
s|org.linkki.core.ui.components.ComponentWrapperFactory([[:punct:][:space:]])|org.linkki.core.binding.wrapper.ComponentWrapperFactory\1|g
s|org.linkki.core.ui.components.WrapperType([[:punct:][:space:]])|org.linkki.core.binding.wrapper.WrapperType\1|g
s|org.linkki.core.ui.columnbased.ColumnBasedComponentCreator([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.ColumnBasedComponentCreator\1|g
s|org.linkki.core.ui.columnbased.ColumnBasedComponentFactory([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.ColumnBasedComponentFactory\1|g
s|org.linkki.core.ui.columnbased.ColumnBasedComponentWrapper([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper\1|g
s|org.linkki.core.ui.columnbased.aspect.ColumnBasedComponentAspectDefinition([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.aspects.ColumnBasedComponentAspectDefinition\1|g
s|org.linkki.core.ui.columnbased.aspect.ColumnBasedComponentAspectDefinitions([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.aspects.ColumnBasedComponentAspectDefinitions\1|g
s|org.linkki.core.ui.columnbased.aspect.ColumnBasedComponentFooterAspectDefinition([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.aspects.ColumnBasedComponentFooterAspectDefinition\1|g
s|org.linkki.core.ui.columnbased.aspect.ColumnBasedComponentItemsAspectDefinition([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.aspects.ColumnBasedComponentItemsAspectDefinition\1|g
s|org.linkki.core.ui.columnbased.aspect.ColumnBasedComponentPageLengthAspectDefinition([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.aspects.ColumnBasedComponentPageLengthAspectDefinition\1|g
s|org.linkki.core.ui.table.ContainerPmo([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.pmo.ContainerPmo\1|g
s|org.linkki.core.ui.table.HierarchicalRowPmo([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo\1|g
s|org.linkki.core.ui.table.SimpleItemSupplier([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier\1|g
s|org.linkki.core.ui.table.SimpleTablePmo([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo\1|g
s|org.linkki.core.ui.table.TableFooterPmo([[:punct:][:space:]])|org.linkki.core.defaults.columnbased.pmo.TableFooterPmo\1|g
s|org.linkki.core.ui.formatters.Formatter([[:punct:][:space:]])|org.linkki.core.defaults.formatters.Formatter\1|g
s|org.linkki.core.ui.formatters.LocalDateFormatter([[:punct:][:space:]])|org.linkki.core.defaults.formatters.LocalDateFormatter\1|g
s|org.linkki.core.ui.formatters.LocalDateTimeFormatter([[:punct:][:space:]])|org.linkki.core.defaults.formatters.LocalDateTimeFormatter\1|g
s|org.linkki.core.ui.formatters.TemporalAccessorFormatter([[:punct:][:space:]])|org.linkki.core.defaults.formatters.TemporalAccessorFormatter\1|g
s|org.linkki.core.nls.DefaultNlsService([[:punct:][:space:]])|org.linkki.core.defaults.nls.DefaultNlsService\1|g
s|org.linkki.core.nls.pmo.DefaultPmoNlsService([[:punct:][:space:]])|org.linkki.core.defaults.nls.DefaultPmoNlsService\1|g
s|org.linkki.core.ui.section.Sections([[:punct:][:space:]])|org.linkki.core.defaults.section.Sections\1|g
s|org.linkki.core.ui.application.ApplicationStyles([[:punct:][:space:]])|org.linkki.core.defaults.style.LinkkiApplicationTheme\1|g
s|ApplicationStyles|LinkkiTheme|g
s|org.linkki.core.ui.section.annotations.aspect.AvailableValuesProvider([[:punct:][:space:]])|org.linkki.core.defaults.ui.element.AvailableValuesProvider\1|g
s|org.linkki.core.ui.components.ItemCaptionProvider([[:punct:][:space:]])|org.linkki.core.defaults.ui.element.ItemCaptionProvider\1|g
s|org.linkki.core.ui.UiElementCreator([[:punct:][:space:]])|org.linkki.core.defaults.ui.element.UiCreator\1|g
s|UiElementCreator|UiCreator|g
s|org.linkki.core.ui.section.annotations.aspect.EnabledAspectDefinition([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.BindTooltipAspectDefinition([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.TooltipAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.BindTooltip([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.annotations.BindTooltip\1|g
s|org.linkki.core.defaults.ui.aspects.annotations.BindTooltip.TooltipType([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.types.TooltipType\1|g
s|org.linkki.core.ui.section.annotations.AvailableValuesType([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.types.AvailableValuesType\1|g
s|org.linkki.core.ui.section.annotations.CaptionType([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.types.CaptionType\1|g
s|org.linkki.core.ui.section.annotations.EnabledType([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.types.EnabledType\1|g
s|org.linkki.core.ui.section.annotations.RequiredType([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.types.RequiredType\1|g
s|org.linkki.core.ui.section.annotations.VisibleType([[:punct:][:space:]])|org.linkki.core.defaults.ui.aspects.types.VisibleType\1|g
s|org.linkki.core.nls.pmo.PmoBundleNameGenerator([[:punct:][:space:]])|org.linkki.core.nls.PmoBundleNameGenerator\1|g
s|org.linkki.core.nls.pmo.PmoNlsService([[:punct:][:space:]])|org.linkki.core.nls.PmoNlsService\1|g
s|org.linkki.core.ButtonPmo([[:punct:][:space:]])|org.linkki.core.pmo.ButtonPmo\1|g
s|org.linkki.core.ui.section.annotations.ModelObject([[:punct:][:space:]])|org.linkki.core.pmo.ModelObject\1|g
s|org.linkki.core.PresentationModelObject([[:punct:][:space:]])|org.linkki.core.pmo.PresentationModelObject\1|g
s|org.linkki.core.ui.section.annotations.SectionID([[:punct:][:space:]])|org.linkki.core.pmo.SectionID\1|g
s|org.linkki.core.ui.UiFramework([[:punct:][:space:]])|org.linkki.core.uiframework.UiFramework\1|g
s|org.linkki.core.ui.UiFrameworkExtension([[:punct:][:space:]])|org.linkki.core.uiframework.UiFrameworkExtension\1|g

# linkki-core-vaadin8 specific
s|org.linkki.core.ui.section.annotations.UISection([[:punct:][:space:]])|org.linkki.core.ui.layout.annotation.UISection\1|g
s|org.linkki.core.binding.annotations.Bind([[:punct:][:space:]])|org.linkki.core.ui.bind.annotation.Bind\1|g
s|org.linkki.core.binding.annotations.aspect.BindAnnotationAspectDefinitionCreator([[:punct:][:space:]])|org.linkki.core.ui.bind.annotation.BindAnnotationAspectDefinitionCreator\1|g
s|org.linkki.core.ui.util.ComponentFactory([[:punct:][:space:]])|org.linkki.core.vaadin.component.ComponentFactory\1|g
s|org.linkki.core.ui.area.Area([[:punct:][:space:]])|org.linkki.core.vaadin.component.area.Area\1|g
s|org.linkki.core.ui.area.TabSheetArea([[:punct:][:space:]])|org.linkki.core.vaadin.component.area.TabSheetArea\1|g
s|org.linkki.core.ui.page.AbstractPage([[:punct:][:space:]])|org.linkki.core.vaadin.component.page.AbstractPage\1|g
s|org.linkki.core.ui.page.Page([[:punct:][:space:]])|org.linkki.core.vaadin.component.page.Page\1|g
s|org.linkki.core.ui.section.AbstractSection([[:punct:][:space:]])|org.linkki.core.vaadin.component.section.AbstractSection\1|g
s|org.linkki.core.ui.section.BaseSection([[:punct:][:space:]])|org.linkki.core.vaadin.component.section.BaseSection\1|g
s|org.linkki.core.ui.section.CustomLayoutSection([[:punct:][:space:]])|org.linkki.core.vaadin.component.section.CustomLayoutSection\1|g
s|org.linkki.core.ui.section.FormSection([[:punct:][:space:]])|org.linkki.core.vaadin.component.section.FormSection\1|g
s|org.linkki.core.ui.section.HorizontalSection([[:punct:][:space:]])|org.linkki.core.vaadin.component.section.HorizontalSection\1|g
s|org.linkki.core.ui.table.TableSection([[:punct:][:space:]])|org.linkki.core.vaadin.component.section.TableSection\1|g
s|org.linkki.core.binding.ButtonPmoBinder([[:punct:][:space:]])|org.linkki.core.ui.creation.section.ButtonPmoBinder\1|g
s|org.linkki.core.ui.section.DefaultPmoBasedSectionFactory([[:punct:][:space:]])|org.linkki.core.ui.creation.section.DefaultPmoBasedSectionFactory\1|g
s|org.linkki.core.ui.section.PmoBasedSectionFactory([[:punct:][:space:]])|org.linkki.core.ui.creation.section.PmoBasedSectionFactory\1|g
s|org.linkki.core.ui.section.SectionCreationContext([[:punct:][:space:]])|org.linkki.core.ui.creation.section.SectionCreationContext\1|g
s|org.linkki.core.ui.table.PmoBasedTableFactory([[:punct:][:space:]])|org.linkki.core.ui.creation.table.PmoBasedTableFactory\1|g
s|org.linkki.core.ui.table.PmoBasedTableSectionFactory([[:punct:][:space:]])|org.linkki.core.ui.creation.table.PmoBasedTableSectionFactory\1|g
s|org.linkki.core.ui.table.TableComponentWrapper([[:punct:][:space:]])|org.linkki.core.ui.creation.table.TableComponentWrapper\1|g
s|org.linkki.core.ui.table.TableCreator([[:punct:][:space:]])|org.linkki.core.ui.creation.table.TableCreator\1|g
s|org.linkki.core.container.DummyItemImplementation([[:punct:][:space:]])|org.linkki.core.ui.creation.table.container.DummyItemImplementation\1|g
s|org.linkki.core.container.LinkkiInMemoryContainer([[:punct:][:space:]])|org.linkki.core.ui.creation.table.container.LinkkiInMemoryContainer\1|g
s|org.linkki.core.ui.section.annotations.BindReadOnly([[:punct:][:space:]])|org.linkki.core.ui.aspects.annotation.BindReadOnly\1|g
s|org.linkki.core.ui.section.annotations.BindStyleNames([[:punct:][:space:]])|org.linkki.core.ui.aspects.annotation.BindStyleNames\1|g
s|org.linkki.core.ui.section.annotations.aspect.FieldAspectDefinitionCreator([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.FieldAspectDefinitionCreator\1|g
s|org.linkki.core.ui.section.annotations.SectionLayout([[:punct:][:space:]])|org.linkki.core.ui.layout.annotation.SectionLayout\1|g
s|org.linkki.core.ui.section.annotations.UIButton([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UIButton\1|g
s|org.linkki.core.ui.section.annotations.UICheckBox([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UICheckBox\1|g
s|org.linkki.core.ui.section.annotations.UIComboBox([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UIComboBox\1|g
s|org.linkki.core.ui.section.annotations.UICustomField([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UICustomField\1|g
s|org.linkki.core.ui.section.annotations.UIDateField([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UIDateField\1|g
s|org.linkki.core.ui.section.annotations.UIDoubleField([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UIDoubleField\1|g
s|org.linkki.core.ui.section.annotations.UIIntegerField([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UIIntegerField\1|g
s|org.linkki.core.ui.section.annotations.UILabel([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UILabel\1|g
s|org.linkki.core.ui.section.annotations.UISubsetChooser([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UISubsetChooser\1|g
s|org.linkki.core.ui.section.annotations.UITextArea([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UITextArea\1|g
s|org.linkki.core.ui.section.annotations.UITextField([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UITextField\1|g
s|org.linkki.core.ui.section.annotations.UIYesNoComboBox([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.UIYesNoComboBox\1|g
s|org.linkki.core.ui.section.annotations.aspect.ValueAspectDefinitionCreator([[:punct:][:space:]])|org.linkki.core.ui.element.annotation.ValueAspectDefinitionCreator\1|g
s|org.linkki.core.ui.section.annotations.aspect.AvailableValuesAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.AvailableValuesAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.BindReadOnlyAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.BindReadOnlyAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.BindStyleAnnotationAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.BindStyleAnnotationAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.BindStyleNamesAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.BindStyleNamesAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.ButtonInvokeAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.ButtonInvokeAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.CaptionAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.CaptionAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.DerivedReadOnlyAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.HasItemsAvailableValuesAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.HasItemsAvailableValuesAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.LabelAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.LabelAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.LabelValueAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.LabelValueAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.RequiredAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.RequiredAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.aspect.ValueAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.aspects.ValueAspectDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.ButtonBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.ButtonBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.CheckboxBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.CheckboxBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.ComboboxBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.ComboboxBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.CustomFieldBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.CustomFieldBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.DateFieldBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.DateFieldBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.DoubleFieldBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.DoubleFieldBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.IntegerFieldBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.IntegerFieldBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.LabelBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.LabelBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.SubsetChooserBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.SubsetChooserBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.TextAreaBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.TextAreaBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.TextFieldBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.TextFieldBindingDefinition\1|g
s|org.linkki.core.ui.section.annotations.adapters.YesNoComboBoxBindingDefinition([[:punct:][:space:]])|org.linkki.core.ui.element.bindingdefinitions.YesNoComboBoxBindingDefinition\1|g
s|org.linkki.core.ui.element.annotations.SectionLayout([[:punct:][:space:]])|org.linkki.core.ui.layout.annotation.SectionLayout\1|g
s|org.linkki.core.vaadin8.nls.NlsText([[:punct:][:space:]])|org.linkki.core.ui.nls.NlsText\1|g
s|org.linkki.core.ButtonPmoBuilder([[:punct:][:space:]])|org.linkki.core.ui.pmo.ButtonPmoBuilder\1|g
s|org.linkki.core.ui.section.annotations.UITableColumn([[:punct:][:space:]])|org.linkki.core.ui.table.column.annotation.UITableColumn\1|g
s|org.linkki.core.ui.table.column.aspect.ColumnCollapseAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.table.column.aspects.ColumnCollapseAspectDefinition\1|g
s|org.linkki.core.ui.table.column.aspect.ColumnExpandRatioAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.table.column.aspects.ColumnExpandRatioAspectDefinition\1|g
s|org.linkki.core.ui.table.column.aspect.ColumnWidthAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.table.column.aspects.ColumnWidthAspectDefinition\1|g
s|org.linkki.core.ui.table.column.aspect.StaticColumnAspectDefinition([[:punct:][:space:]])|org.linkki.core.ui.table.column.aspects.StaticColumnAspectDefinition\1|g
s|org.linkki.core.vaadin8.Vaadin8([[:punct:][:space:]])|org.linkki.core.ui.uiframework.Vaadin8\1|g
s|org.linkki.core.vaadin8.components.Vaadin8ComponentWrapperFactory([[:punct:][:space:]])|org.linkki.core.ui.uiframework.Vaadin8ComponentWrapperFactory\1|g
s|org.linkki.core.message.SeverityErrorLevelConverter([[:punct:][:space:]])|org.linkki.core.ui.validation.message.SeverityErrorLevelConverter\1|g
s|org.linkki.core.ui.components.CaptionComponentWrapper([[:punct:][:space:]])|org.linkki.core.ui.wrapper.CaptionComponentWrapper\1|g
s|org.linkki.core.ui.components.LabelComponentWrapper([[:punct:][:space:]])|org.linkki.core.ui.wrapper.LabelComponentWrapper\1|g