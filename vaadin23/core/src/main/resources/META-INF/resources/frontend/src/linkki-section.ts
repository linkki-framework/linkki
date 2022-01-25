import {LitElement, html} from 'lit-element';

class LinkkiSection extends LitElement {

  render() {
    return html`
      <style>
        :host {
          display: flex;
          flex-direction: column;
          box-sizing: border-box;
          margin-bottom: 1em;
        }
        
        ::slotted([slot="content"]) {
          display: flex;
          flex-grow: 1;
        	flex-wrap: wrap;
        	flex-direction: column;
          align-items: baseline;
          overflow: auto;
          box-sizing: border-box;
          width: 100%;
        }

        :host([theme~="horizontal"]) > ::slotted([slot="content"]) {
	        flex-direction: row;
	        gap: var(--lumo-space-m);
        }

        .linkki-section-header, .linkki-section-header-components {
          gap: var(--lumo-space-m);
          display: flex;
	        white-space: nowrap;
          align-items: center;
        }

        .linkki-section-header > .linkki-section-header-components {
          color: var(--linkki-section-caption-header-color, var(--lumo-contrast-80pct));
        }

        ::slotted([slot="header-components"]), ::slotted([slot="close-toggle"]) {
          margin-bottom: .5em !important;
        }
      </style>

      <div class="linkki-section-header">
        <div class="linkki-section-header-components">
          <slot name="header-components">
        </div>
        <slot name="close-toggle">
      </div>
      <slot name="content"></slot>`;
  }
}

customElements.define('linkki-section', LinkkiSection);