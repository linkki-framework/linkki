import {css, html, LitElement} from 'lit';

// tag::bindSlot-layout-typescript[]
class SampleSlotLayout extends LitElement {

    static styles = css`
        :host {
            width: 100%;
            height: 100%;
        }

        ::slotted([slot="right-slot"]) {
            padding-right: 32px;
        }
        
        #slot-container {
            display: flex;
        }
        
        .right-slot-container {
            flex-grow: 1;
            display: flex;
            justify-content: end;
        }
    `;

    render() {
        return html`
            <vaadin-horizontal-layout id="slot-container">
                <div>
                    <slot name="left-slot"></slot>
                </div>
                <div class="right-slot-container">
                    <slot name="right-slot"></slot>
                </div>
            </vaadin-horizontal-layout>
        `;
    }
}

customElements.define('sample-slot-layout', SampleSlotLayout);
// end::bindSlot-layout-typescript[]