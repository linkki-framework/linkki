import { LitElement, html, css } from 'lit';

class LinkkiSection extends LitElement {

  static styles = css`
    :host {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      width: 100%;
      --linkki-section-content-gap: var(--lumo-space-m);
      --lumo-header-text-color: var(--linkki-section-caption-header-color, var(--lumo-contrast-80pct));
      --linkki-grid-background: var(--linkki-section-background, var(--lumo-base-color));
    }

    ::slotted([slot="content"]) {
      display: flex;
      flex-grow: 1;
      flex-wrap: wrap;
      flex-direction: column;
      align-items: baseline;
      /* Sections should be scrollable see https://jira.faktorzehn.de/browse/LIN-2915 */
      overflow: auto;
      box-sizing: border-box;
      width: 100%;
      gap: var(--linkki-section-content-gap);
      background: var(--linkki-section-background, transparent);
      padding: var(--linkki-section-padding, 0);
      border-radius: var(--linkki-section-border-radius, var(--lumo-border-radius-m));
    }

    :host([theme~="horizontal"]) > ::slotted([slot="content"]) {
      flex-direction: row;
    }

    :host([theme~="form"]) > ::slotted([slot="content"]) {
      row-gap: 0;
    }

    .linkki-section-header, .linkki-section-header-components, .linkki-section-right-header-components {
      gap: var(--lumo-space-m);
      display: flex;
      white-space: nowrap;
      align-items: center;
    }
    
    .linkki-section-right-header-components {
      flex-grow: 1;
      justify-content: end;
    }

    ::slotted([slot="header-components"]), ::slotted([slot="close-toggle"]), ::slotted([slot="right-aligned-components"]) {
      margin-bottom: var(--linkki-section-header-margin-bottom, var(--lumo-space-xs)) !important;
      min-height: var(--lumo-size-s);
    }

  `;

  render() {
    return html`
      <div class="linkki-section-header">
        <div class="linkki-section-header-components">
          <slot name="header-components">
        </div>
        <slot name="close-toggle"></slot>
        <div class="linkki-section-right-header-components"> 
         <slot name="right-header-components"></slot>
        </div>
      </div>
      <slot name="content"></slot>
    `;
  }
}

customElements.define('linkki-section', LinkkiSection);
