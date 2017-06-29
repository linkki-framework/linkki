<%include "header.gsp"%>

	<%include "menu.gsp"%>
	
	<div class="page-header">
		<h1>${content.title}</h1>
	</div>

	<!-- <p><em>${new java.text.SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(content.date)}</em></p> -->

	<%def counter = 1
	sortedByFileName = published_sections.sort{ it.uri }
	sortedByFileName.each {section -> if(section.tags != null){ if(content.tags[0] == section.tags[0]){%>
		<a href="../${section.uri}"><h3>Kapitel ${counter}: ${section.title}</h3></a>
		<!-- <p>${new java.text.SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(section.date)}</p> -->
  	<%counter++}}}%>

	<hr />

<%include "footer.gsp"%>