<%include "header.gsp"%>
	
	<%include "menu.gsp"%>
	
	<div class="page-header">
		<%def counter = 1
		sortedByFileName = published_posts.sort{ it.uri }
		sortedByFileName.each {post -> if(post.tags != null){ if(content.tags[0] == post.tags[0]){ if(content.uri == post.uri){%>
			<h1>Kapitel ${counter}: ${post.title}</h1>
		<%}
		counter++;}}}%>
		
		<em>${new java.text.SimpleDateFormat("dd MMMM yyyy", Locale.GERMAN).format(content.date)}</em>
		</p>
		<h3 style="text-align:left;">
		<%def prev = "empty"
		sortedByFileName = published_posts.sort{ it.uri }
		sortedByFileName.each {post -> 
			if(post.tags != null){ 
				if(content.tags[0] == post.tags[0]){
					if(content.uri == post.uri){
						if(prev != "empty"){%>
							<a href="../${prev}">Zurück</a>
						<%}else{
								published_overviews.each {view -> if(view.tags != null && view.type == "overview"){ if(content.tags[0] == view.tags[0]){%>
									<a href="../${view.uri}">Übersicht</a>
								<%}}}
							}
					}
					prev = post.uri
				}
			}
		}%>
		<span style="float:right;">
		<% def found = 0;
		sortedByFileName = published_posts.sort{ it.uri }
		sortedByFileName.each {post -> if(post.tags != null){ 
			if(content.tags[0] == post.tags[0]){
				if(found == 1){%>
					<a href="../${post.uri}">Weiter</a>
				<%found = 2}
				if(content.uri == post.uri){
				found = 1}}}}
				if(found != 2){
					published_overviews.each {view -> if(view.tags != null && view.type == "overview"){ if(content.tags[0] == view.tags[0]){%>
					<a href="../${view.uri}">Übersicht</a>
				<%}}}}%>
		</span>
		</h3>
	</div>

	<p>${content.body}</p>

	<hr />
	
	<h3 style="text-align:left;">
		<%prev = "empty"
		sortedByFileName = published_posts.sort{ it.uri }
		sortedByFileName.each {post -> 
			if(post.tags != null){ 
				if(content.tags[0] == post.tags[0]){
					if(content.uri == post.uri){
						if(prev != "empty"){%>
							<a href="../${prev}">Zurück</a>
						<%}else{
								published_overviews.each {view -> if(view.tags != null && view.type == "overview"){ if(content.tags[0] == view.tags[0]){%>
									<a href="../${view.uri}">Übersicht</a>
								<%}}}
							}
					}
					prev = post.uri
				}
			}
		}%>
		<span style="float:right;">
		<%found = 0;
		sortedByFileName = published_posts.sort{ it.uri }
		sortedByFileName.each {post -> if(post.tags != null){ 
			if(content.tags[0] == post.tags[0]){
				if(found == 1){%>
					<a href="../${post.uri}">Weiter</a>
				<%found = 2}
				if(content.uri == post.uri){
				found = 1}}}}
				if(found != 2){
					published_overviews.each {view -> if(view.tags != null && view.type == "overview"){ if(content.tags[0] == view.tags[0]){%>
					<a href="../${view.uri}">Übersicht</a>
				<%}}}}%>
		</span>
		</h3>
	
<%include "footer.gsp"%>