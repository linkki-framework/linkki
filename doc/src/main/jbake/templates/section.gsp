<%include "header.gsp"%>
	
	<%include "menu.gsp"%>
	
	<div class="page-header">
		<%def counter = 1
		sortedByFileName = published_sections.sort{ it.uri }
		sortedByFileName.each {section -> if(section.tags != null){ if(content.tags[0] == section.tags[0]){ if(content.uri == section.uri){%>
			<h1>Kapitel ${counter}: ${section.title}</h1>
		<%}
		counter++;}}}%>
		<!--- <em>${new java.text.SimpleDateFormat("dd MMMM yyyy", Locale.GERMAN).format(content.date)}</em> --->
		</p>

		<div id="pagemenucontainercontainer">
		  <div style="float: left">
		  <%def prev = "empty"
			sortedByFileName = published_sections.sort{ it.uri }
			sortedByFileName.each {section -> 
				if(section.tags != null){ 
					if(content.tags[0] == section.tags[0]){
						if(content.uri == section.uri){
							if(prev != "empty"){%>
								<a href="../${prev}">Zurück</a>
							<%}else{%>
								
								<%
							}
					}
					prev = section.uri
				}
			}
		}%>
		  </div>
		  <div style="float: right">
		  <% def found = 0;
			sortedByFileName = published_sections.sort{ it.uri }
			sortedByFileName.each {section -> if(section.tags != null){ 
				if(content.tags[0] == section.tags[0]){
					if(found == 1){%>
						<a href="../${section.uri}">Weiter</a>
					<%found = 2}
					if(content.uri == section.uri){
					found = 1}}}}

			%>
		  </div>
		  <div style="margin: 0 auto; width: 100px;">
			<%published_chapters.each {view -> if(view.tags != null && view.type == "chapter"){ if(content.tags[0] == view.tags[0]){%>
									<a href="../${view.uri}">Übersicht</a>
			<%}}}%>
		  </div>
		</div>
	</div>

	<p>${content.body}</p>

	<hr />
	
	<div id="pagemenucontainercontainer">
		  <div style="float: left">
		  <%prev = "empty"
			sortedByFileName = published_sections.sort{ it.uri }
			sortedByFileName.each {section -> 
				if(section.tags != null){ 
					if(content.tags[0] == section.tags[0]){
						if(content.uri == section.uri){
							if(prev != "empty"){%>
								<a href="../${prev}">Zurück</a>
							<%}else{%>
								
								<%
							}
					}
					prev = section.uri
				}
			}
		}%>
		  </div>
		  <div style="float: right">
		  <%found = 0;
			sortedByFileName = published_sections.sort{ it.uri }
			sortedByFileName.each {section -> if(section.tags != null){ 
				if(content.tags[0] == section.tags[0]){
					if(found == 1){%>
						<a href="../${section.uri}">Weiter</a>
					<%found = 2}
					if(content.uri == section.uri){
					found = 1}}}}

			%>
		  </div>
		  <div style="margin: 0 auto; width: 100px;">
			<%published_chapters.each {view -> if(view.tags != null && view.type == "chapter"){ if(content.tags[0] == view.tags[0]){%>
									<a href="../${view.uri}">Übersicht</a>
			<%}}}%>
		  </div>
		</div>
	
<%include "footer.gsp"%>