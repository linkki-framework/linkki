<%include "header.gsp"%>

<%include "menu.gsp"%>

<div class="book-body">
  <div class="body-inner">
    <div class="page-wrapper" tabindex="-1" role="main">
      <div class="page-inner">
        <section class="normal markdown-section">
          <p>
          ${content.body}
          <p>
          <h4>Content</h4>
          <%chaptersSortedByOrder = published_chapters.sort{ it.order }
          chaptersSortedByOrder.each {chapter ->%>
            <a href="${chapter.uri}"><h5>${chapter.title}</h5></a>
          <%}%>
        </section>
      </div>
    </div>
  </div>
  <%include "arrows.gsp"%>
</div>

<%include "footer.gsp"%>
