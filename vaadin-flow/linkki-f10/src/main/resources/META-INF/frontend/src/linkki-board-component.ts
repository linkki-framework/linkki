import {LitElement, css, html} from 'lit';

class BoardComponent extends LitElement {

    static styles = css`
      :host {

        /* Sizing */
        --linkki-card-size-m: 720px;
        --linkki-card-size-l: 1440px;

        /* Style */
        box-sizing: border-box;
        background-color: var(--lumo-base-color);
        padding: var(--lumo-space-l) var(--lumo-space-m) var(--lumo-space-l) var(--lumo-space-m);
        border-radius: var(--lumo-border-radius-m);
        flex-grow: 1;
      }

      :host([theme~="medium"]) {
        width: var(--linkki-card-size-m);
      }

      :host([theme~="large"]) {
        width: var(--linkki-card-size-l);
      }

      div.board-component-header {
        padding-bottom: var(--lumo-space-m)
      }
    `;

    render() {
        return html`
            <div class="board-component-header">
                <slot name="header"></slot>
            </div>
            <div>
                <slot></slot>
            </div>`;
    }
}

customElements.define('linkki-board-component', BoardComponent);