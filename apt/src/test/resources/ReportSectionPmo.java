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


import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UICheckBox;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextArea;

@UISection
public class ReportSectionPmo {

	private final Report report;

	public ReportSectionPmo(Report report) {
		this.report = requireNonNull(report, "report must not be null");
	}

	@ModelObject
	public Report getReport() { return report; }

	@UITextArea(
			position = 10,
			label = "Description",
			modelAttribute = "description",
			rows = 5, columns = 50)
	public void description() {}

	@UICheckBox(position = 40, caption = "asdf")
	public void type() {
	}

	public boolean isTypeEnabled() {
		String description = report.getDescription();
		return description != null && !description.isEmpty() && report.getType() != null;
	}
	
	public Class<?> getTypeComponentType() {
		return null;
	}
	
	public List<ReportType> getTypeAvailableValues() {
		return Arrays.asList(ReportType.values());
	}
}