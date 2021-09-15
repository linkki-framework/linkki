import {LitElement, html} from 'lit-element';

class LinkkiTabLayout extends LitElement {

    render() {
        return html`
            <style>
                :host {
                  display: flex;
                }

                :host([orientation~="horizontal"]) {
                  flex-direction: column;
                }
                :host([orientation~="horizontal"]) > ::slotted(vaadin-tabs) {
                  width: 100%;
                }

                :host([orientation~="vertical"]) {
                  flex-direction: vertical;
                }
                :host([orientation~="vertical"]) > ::slotted(vaadin-tabs) {
                  height: 100%;
                }

                :host([theme~="solid"]) > ::slotted(vaadin-tabs) {
                    --sidebar-background-color: var(--lumo-contrast-5pct);
                    background-color: var(--sidebar-background-color);
                }
                
                :host > ::slotted(div[slot="content"]) {
                  overflow: auto;
                  box-sizing: border-box;
                  flex-grow: 1;
                }

                /* Avoid padding collapsing */
                :host > ::slotted(div[slot="content"]) > * {
                  display: block;
                }

                :host([orientation~="vertical"]) > ::slotted(vaadin-tabs[slot="tabs"]) {
                  flex-shrink: 0;
                  overflow-y: hidden;
                  --lumo-icon-size-m: 2em;
                }
            </style>
            
            <slot name="tabs"></slot>
            <slot name="content"></slot>`;
    }
}

customElements.define('linkki-tab-layout', LinkkiTabLayout);