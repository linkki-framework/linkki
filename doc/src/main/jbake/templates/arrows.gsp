<%
		def found = 0
		def prev = "index.html"
		def prevChapter = ""
		def next = "index.html"
		sortedByOverviewName = published_chapters.sort{ it.order }
		sortedByOverviewName.each {
			view ->
				if(found == 1)
				{
					found = 2
					next = view.uri
				}
				if(view.uri == content.uri)
				{
					found = 1
				}
				if(found == 0)
				{
					prev = view.uri
					prevChapter = view.uri.substring(0, view.uri.indexOf('/'))
					if(content.uri == null)
					{
						found = 3
						next = view.uri
					}
				}
				
				sortedByFileName = published_sections.sort{ it.uri }
				sortedByFileName.each 
				{
					section -> 
					if(section.uri.substring(0, section.uri.indexOf('/')) == view.uri.substring(0, view.uri.indexOf('/')))
					{
						if(found == 1)
						{
							found = 2
							next = section.uri
						}
						if(section.uri == content.uri)
						{
							found = 1
						}
						if(found == 3 )
						{
							prev = section.uri
						}
					}
					if((found == 0 ) && section.uri.substring(0, section.uri.indexOf('/')) == prevChapter)
					{
						prev = section.uri
					}
					%>
				<%}%>
		<%}%>
		
		<a href="<%if(content.uri != null){%>../<%}%>${prev}" class="navigation navigation-prev "
			aria-label="Previous page"> <i class="fa fa-angle-left"></i>
		</a> 
		<a href="<%if(content.uri != null){%>../<%}%>${next}" class="navigation navigation-next "
			aria-label="Next page"> <i
			class="fa fa-angle-right"></i>
		</a>