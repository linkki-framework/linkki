import {LitElement, css, html} from 'lit';
import {property} from 'lit/decorators.js';

class LinkkiText extends LitElement {

  // An empty value would wrongly affect component size, so the default must be undefined
  @property({reflect: true})
  label?: string;

  static styles = css`
    :host {
      --lumo-text-field-size: var(--lumo-size-m);

      display: inline-flex;
      outline: none;
      color: var(--lumo-body-text-color);
      font-size: var(--lumo-font-size-m);
      font-family: var(--lumo-font-family);

      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
      -webkit-tap-highlight-color: transparent;
    }

    ::slotted([slot="prefix"]),
    ::slotted([slot="suffix"]) {
        flex: none;
    }

    :host:before {
      /* set the default height to normal line height */
      content: '\\200b';
      display: inline-block;
    }

    :host([hidden]) {
      display: none !important;
    }

    .linkki-text-container {
      display: flex;
      flex-direction: column;
      min-width: 100%;
      max-width: 100%;
      justify-content: center;
    }

    .content {
      flex-grow: 1;
      display: flex;
      align-items: center;
      flex-direction: row;
      gap: var(--lumo-space-s);
      white-space: break-spaces;
      margin-left: calc(var(--lumo-border-radius-m) / 4);
    }

    :host(:not([label])) .label {
      display: none;
    }

    .label {
      align-self: flex-start;
      color: var(--lumo-secondary-text-color);
      font-weight: 500;
      font-size: var(--lumo-font-size-s);
      margin-left: calc(var(--lumo-border-radius-m) / 4);
      transition: color 0.2s;
      line-height: 1;
      padding-right: 1em;
      padding-bottom: 0.5em;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      position: relative;
      max-width: 100%;
      box-sizing: border-box;
    }

    :host([dir='rtl']) .label {
      margin-left: 0;
        margin-right: calc(var(--lumo-border-radius-m) / 4);
    }

    :host([dir='rtl']) .label {
      padding-left: 1em;
      padding-right: 0;
    }

    :host([label]) {
      padding: var(--lumo-space-xs) 0;
    }

    :host([label])::before {
      content: '\\200b';
      height: var(--lumo-text-field-size);
      box-sizing: border-box;
      display: inline-flex;
      align-items: center;
      margin-top: calc(var(--lumo-font-size-s) * 1.5);
    }

    :host([theme~='compact']) {
      --lumo-text-field-size: var(--lumo-size-xs);
    }

    :host([theme~='compact']) .label {
      padding-bottom: 0;
    }

    :host([theme~='small']) {
      font-size: var(--lumo-font-size-s);
      --lumo-text-field-size: var(--lumo-size-s);
    }

    :host([theme~='small'][label])::before {
      margin-top: calc(var(--lumo-font-size-xs) * 1.5);
    }

    :host([theme~='small'][label]) .label {
      font-size: var(--lumo-font-size-xs);
    }
  `;

  render() {
    return html`
      <div class="linkki-text-container">
        <div class="label">
          <label>${this.label}</label>
        </div>

        <div class="content">
          <slot name="prefix" slot="prefix"></slot>
          <slot></slot>
          <slot name="suffix" slot="suffix"></slot>
        </div>
      </div>
    `;
  }
}

customElements.define('linkki-text', LinkkiText);
