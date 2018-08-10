<%include "header.gsp"%>

<%include "menu.gsp"%>

<div class="book-body">
	<div class="body-inner">
	    <div class="page-wrapper" tabindex="-1" role="main">
            <div class="page-inner">
				<section class="normal markdown-section">
					${content.body}

					<%sectionsSortedByOrder = published_sections.sort{ it.uri }
					                           .sort{ it.order }
																		 .findAll{ it.uri.substring(0, it.uri.indexOf('/')) == content.uri.substring(0, content.uri.indexOf('/'))}
					  if(sectionsSortedByOrder.size() > 0) {%>
							<hr>
							<h3>Content</h3>
					    <ol>
          	    <%sectionsSortedByOrder.each {sectionTitle -> %>
									  <li><a href="../${sectionTitle.uri}">${sectionTitle.title}</a></li>
							  <%}%>
							</ol>
						<%}%>
				</section>
			</div>
		</div>
	</div>
	<%include "arrows.gsp"%>
</div>

<%include "footer.gsp"%>
