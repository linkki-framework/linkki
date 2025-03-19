import {css, html, LitElement} from 'lit';

class SearchInputLayout extends LitElement {

    static styles = css`
        :host {
            /* make sure scrollbar sticks to the splitter */
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
            gap: var(--lumo-space-m);
         }

        ::slotted(*) {
            padding-right: var(--lumo-space-m);
        }

        ::slotted([slot="inputs"]) {
            overflow: auto;
        }
    `;

    render() {
        return html`
            <slot name="inputs"></slot>
            <slot name="buttons"></slot>
            `;
    }
}

customElements.define('search-input-layout', SearchInputLayout);