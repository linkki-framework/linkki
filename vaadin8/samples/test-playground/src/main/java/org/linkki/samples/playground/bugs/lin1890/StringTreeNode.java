package org.linkki.samples.playground.bugs.lin1890;

import java.util.ArrayList;
import java.util.List;

public class StringTreeNode {

    private final List<StringTreeNode> children = new ArrayList<StringTreeNode>();
    private final String value;

    public StringTreeNode(String value, List<StringTreeNode> children) {
        this.value = value;
        this.children.addAll(children);
    }

    public List<StringTreeNode> getChildren() {
        return children;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
