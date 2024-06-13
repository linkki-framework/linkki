import {html, LitElement} from 'lit';

class ComponentWithCustomResource extends LitElement {

  render() {
    return html`
      <div class="super-component"><h3 id="captionInResource">caption in resource</h3></div>
    `;
  }
}

customElements.define('component-with-custom-resource', ComponentWithCustomResource);
