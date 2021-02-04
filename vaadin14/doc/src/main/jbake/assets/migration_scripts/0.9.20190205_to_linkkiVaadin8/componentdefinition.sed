s/(@LinkkiBindingDefinition.*)/\1\n@LinkkiBoundProperty(BindingDefinitionBoundPropertyCreator.class)\n@LinkkiComponent(BindingDefinitionComponentDefinition.Creator.class)\n@LinkkiPositioned/g
# /@LinkkiPositioned/ : Beginn des Patterns
# :marker setzt ein Label, N aktiviert multiline, einlesen bis 'int position'
# Im eingelesenen Text wird vor 'int position' die Annotation hinzugefügt. (\s*) merkt sich dabei die Whitespaces für die Formatierung (inkl \n)
/@LinkkiPositioned/ {:marker;N;/int position/!bmarker;s/(\s*)int position/\1@LinkkiPositioned.Position\1int position/g}