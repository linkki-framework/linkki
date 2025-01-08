import {css, html, LitElement} from 'lit';

class BoardLayout extends LitElement {

    static styles = css`
        :host {
            background-color: var(--lumo-contrast-5pct);
            overflow: auto;
        }
        
        :host > div {
            gap: var(--lumo-space-m);
            padding: var(--lumo-space-m);
            box-sizing: border-box;
            flex-flow: row wrap;
            display: flex;
        }
    `;

    render() {
        return html`
            <div>
                <slot></slot>
            </div>`;
    }
}

customElements.define('linkki-board-layout', BoardLayout);