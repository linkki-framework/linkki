<%include "header.gsp"%>
	
<%include "menu.gsp"%>
	
<div class="book-body">
	<div class="body-inner">
	    <div class="page-wrapper" tabindex="-1" role="main">
            <div class="page-inner">			
				<section class="normal markdown-section">
				
					<p>
				
					<%published_chapters.each {chapter -> if(content.uri.substring(0, content.uri.indexOf('/')) == chapter.uri.substring(0, chapter.uri.indexOf('/'))){%>
						<h2>${chapter.title}</h2>
					<%
					}}%>

					${content.body}
					
				</section>
			</div>
		</div>			
	</div>
	<%include "arrows.gsp"%>
</div>

<%include "footer.gsp"%>