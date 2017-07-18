<%include "header.gsp"%>
	
<%include "menu.gsp"%>
	
<div class="book-body">
	<div class="body-inner">
	    <div class="page-wrapper" tabindex="-1" role="main">
            <div class="page-inner">			
				<section class="normal markdown-section">

					${content.body}
					
					<p>
					
					<%sortedByOverviewName = published_sections.sort{ it.uri }
					sortedByOverviewName.each {sectionTitle ->
					if(sectionTitle.uri.substring(0, sectionTitle.uri.indexOf('/')) == content.uri.substring(0, content.uri.indexOf('/'))){
					%>
						<a href="../${sectionTitle.uri}"><h3>${sectionTitle.title}</h3></a>
					<%}}%>		
					
				</section>
			</div>
		</div>
	</div>
	<%include "arrows.gsp"%>
</div>

<%include "footer.gsp"%>