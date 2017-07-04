<%include "header.gsp"%>
	
<%include "menu.gsp"%>
	
<div class="book-body">
	<div class="body-inner">
	    <div class="page-wrapper" tabindex="-1" role="main">
            <div class="page-inner">			
				<section class="normal markdown-section">
					<!-- <h1>${content.title}</h1> -->
					
					<p>${content.body}</p>
					
					<%sortedByOverviewName = published_sections.sort{ it.uri }
					sortedByOverviewName.each {section ->
					if(section.uri.substring(0, section.uri.indexOf('/')) == content.uri.substring(0, content.uri.indexOf('/'))){
					%>
						<a href="../${section.uri}"><h3>${section.title}</h3></a>
					<%}}%>		
					
				</section>
			</div>
		</div>
	</div>
</div>

<%include "footer.gsp"%>