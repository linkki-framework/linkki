<%include "header.gsp"%>
	
<%include "menu.gsp"%>
	
<div class="book-body">
	<div class="body-inner">
	    <div class="page-wrapper" tabindex="-1" role="main">
            <div class="page-inner">			
				<section class="normal markdown-section">
				
					<p>
				
					<h1>${config.title}</h1>
					<%sortedByOverviewName = published_chapters.sort{ it.order }
					sortedByOverviewName.each {view ->%>
						<a href="${view.uri}"><h3>${view.title}</h3></a>
					<%}%>
				</section>
			</div>
		</div>
	</div>
	<%include "arrows.gsp"%>
</div>

<%include "footer.gsp"%>