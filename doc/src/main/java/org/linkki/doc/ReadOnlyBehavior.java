package org.linkki.doc;

import org.linkki.core.binding.behavior.PropertyBehavior;

//tag::class[]
class ReadOnlyBehavior implements PropertyBehavior {

    private boolean editable;

    public ReadOnlyBehavior(boolean editable) {
       this.editable = editable;
    }
    
    @Override
    public boolean isWritable(Object boundObject, String property) {
        return editable;
    }
}
//end::class[]