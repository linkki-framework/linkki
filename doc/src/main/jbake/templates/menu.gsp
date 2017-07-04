	<!-- Fixed navbar -->
	<div class="book font-size-2 font-family-1 with-summary">
		<div class="book-summary">
          <!-- <a class="navbar-brand" href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>">JBake</a> -->
		  <% if(config.logo != "false"){%>
		  <a class="navbar-brand" href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>index.html"><img src="<%if (content.rootpath.length() > 0) {%>${content.rootpath}<% } else { %><% }%>images/${config.logo}" style="padding-top:8px;padding-right:20px;padding-left:20px"></a>
		  <%}%>
			<form  id="book-search-input" role="search" action="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>search/search.html">
				<input type="text" name="q" id="tipue_search_input" placeholder="Type to search" pattern=".{3,}" title="At least 3 characters" required>
			</form >
			<nav role="navigation">
				<ul class="summary">

					<!--  Home -->				
					<%if(content.uri == null)
					{%>
					<li class="chapter active">
					<%}
					else
					{%>
					<li class="chapter ">
					<%}%>
						<a href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>index.html">Home</a>
					</li>
					
					<!--  Chapters -->	
					<%
					sortedByOverviewName = published_chapters.sort{ it.order }
					sortedByOverviewName.each {
						view ->
						if(view.uri == content.uri)
						{%>
						<li class="chapter active">
						<%}
						else
						{%>
						<li class="chapter ">
						<%}%>
							<a href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>${view.uri}">${view.title}</a>
						
							<ul class="articles">
							<%
							sortedByFileName = published_sections.sort{ it.uri }
							sortedByFileName.each 
							{
								section -> 
								if(section.uri.substring(0, section.uri.indexOf('/')) == view.uri.substring(0, view.uri.indexOf('/')))
								{
									if(section.uri == content.uri)
									{%>				
									<li class="chapter active">
									<%
									}else
									{%>
									<li class="chapter ">
									<%}%>
										<a href="<%if (content.rootpath.length() > 0) {%>${content.rootpath}<% } else { %><% }%>${section.uri}">${section.title}</a>
									</li>
								<%}%>
							<%}%>
							</ul>
						
						</li>
					<%}%>

				</ul> <!-- summary -->
				<!--
				<li>
					<a href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>../../files/ExampleProject.zip"">Example Project</a>
				</li>
				-->
		</nav>
	</div>
    <!-- closing </div> in footer -->
	
	

    