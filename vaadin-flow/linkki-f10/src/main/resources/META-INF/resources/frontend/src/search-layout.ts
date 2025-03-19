import {css, html, LitElement} from 'lit';

class SearchLayout extends LitElement {

    static styles = css`
        :host {
            display: flex;
            flex-direction: column;
            width: 100%;
            height: 100%;
            box-sizing: border-box;
            gap: calc(2* var(--lumo-space-m));
            padding: var(--lumo-space-m);
        }

        #search-split {
            flex-grow: 1;
        }
    `;

    render() {
        return html`
                <slot name="headline"></slot>
                <vaadin-split-layout id="search-split" orientation="horizontal"">
                    <div slot="primary">
                        <slot name="input"></slot>
                    </div>
                    <div slot="secondary">
                        <slot name="result"></slot>
                    </div>
                </vaadin-split-layout>
            `;
    }
}

customElements.define('search-layout', SearchLayout);
