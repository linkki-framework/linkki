	<!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <!-- <a class="navbar-brand" href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>">JBake</a> -->
		  <a class="navbar-brand" href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>index.html"><img src="<%if (content.rootpath.length() > 0) {%>${content.rootpath}<% } else { %><% }%>images/logos/fips_single_final_big.png" style="padding-top:8px;padding-right:20px;padding-left:20px"></a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>index.html">Home</a></li>
			<%sortedByOverviewName = published_chapters.sort{ it.order }
			sortedByOverviewName.each {view ->%>
				<!-- <li><a href="${view.uri}">${view.title}</a></li> -->
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">${view.title}<b class="caret"></b></a>
					<ul class="dropdown-menu">
					<li><a href="<%if (content.rootpath.length() > 0) {%>${content.rootpath}<% } else { %><% }%>${view.uri}">Übersicht: ${view.title}</a></li>
					<%def counter = 1
					sortedByFileName = published_sections.sort{ it.uri }
					sortedByFileName.each {section -> if(section.tags != null){ if(view.tags[0] == section.tags[0]){%>
						<li><a href="<%if (content.rootpath.length() > 0) {%>${content.rootpath}<% } else { %><% }%>${section.uri}">Kapitel ${counter}: ${section.title}</a></li>
					<%counter++}}}%>
				  </ul>
				</li>
			<%}%>
			<!-- <li><a href="<%if (content.rootpath) {%>${content.rootpath}<% } else { %><% }%>../../files/ExampleProject.zip"">Example Project</a></li> -->
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
    <div class="container">