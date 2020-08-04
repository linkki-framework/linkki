package org.linkki.samples.playground.bugs.lin1917;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private ArrayList<TreeNode> children = new ArrayList<>();
    private String value;

    public TreeNode(String value) {
        this.value = value;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

}
