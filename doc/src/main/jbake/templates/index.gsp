<%include "header.gsp"%>

<%include "menu.gsp"%>

<div class="book-body">
  <div class="body-inner">
    <div class="page-wrapper" tabindex="-1" role="main">
      <div class="page-inner">
        <section class="normal markdown-section">
          <p>
          ${content.body}
        </section>
      </div>
    </div>
  </div>
  <%include "arrows.gsp"%>
</div>

<%include "footer.gsp"%>
