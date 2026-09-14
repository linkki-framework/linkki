import {html, LitElement} from 'lit';

class MinimalLitTemplateDefaultLocation extends LitElement {

    render() {
        return html`<div id="content"></div>`;
    }
}

customElements.define('minimal-lit-template-default-location', MinimalLitTemplateDefaultLocation);
