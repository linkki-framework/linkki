import { LitElement, html, css } from 'lit';

class LinkkiSection extends LitElement {

  static styles = css`
    :host {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      width: 100%;
      --linkki-section-gap: var(--lumo-space-m);
      --lumo-header-text-color: var(--linkki-section-caption-header-color, var(--lumo-contrast-80pct));
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
      gap: var(--linkki-section-gap);
      background-color: var(--linkki-section-background, transparent);
      padding: var(--linkki-section-padding, 0);
    }

    :host([theme~="horizontal"]) > ::slotted([slot="content"]) {
      flex-direction: row;
    }

    :host([theme~="form"]) > ::slotted([slot="content"]) {
      row-gap: 0;
    }

    .linkki-section-header, .linkki-section-header-components {
      gap: var(--lumo-space-m);
      display: flex;
      white-space: nowrap;
      align-items: center;
    }

    ::slotted([slot="header-components"]), ::slotted([slot="close-toggle"]) {
      margin-bottom: .5rem !important;
      min-height: var(--lumo-size-s);
    }
  `;

  render() {
    return html`
      <div class="linkki-section-header">
        <div class="linkki-section-header-components">
          <slot name="header-components">
        </div>
        <slot name="close-toggle">
      </div>
      <slot name="content"></slot>
    `;
  }
}

customElements.define('linkki-section', LinkkiSection);
