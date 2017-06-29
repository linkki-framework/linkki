<%include "header.gsp"%>

	<%include "menu.gsp"%>

	<div class="page-header">
		<h1>Dokumentationen</h1>
	</div>
	<%sortedByOverviewName = published_chapters.sort{ it.order }
	sortedByOverviewName.each {view ->%>
		<a href="${view.uri}"><h3>${view.title}</h3></a>
		<!-- <p>${new java.text.SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(view.date)}</p> -->
  	<%}%>
	
	<hr />
	
	<!-- <p>Older posts are available in the <a href="${content.rootpath}${config.archive_file}">archive</a>.</p> -->

<%include "footer.gsp"%>