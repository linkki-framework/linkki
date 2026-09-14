import {html, LitElement} from 'lit';

class MinimalLitTemplate extends LitElement {

    render() {
        return html`<div id="content"></div>`;
    }
}

customElements.define('minimal-lit-template', MinimalLitTemplate);
