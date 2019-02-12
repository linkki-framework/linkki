package org.linkki.core.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestUiLayoutComponent extends TestUiComponent {

    private List<TestUiComponent> children = new LinkedList<>();

    public TestUiLayoutComponent(TestUiComponent... children) {
        this.children.addAll(Arrays.asList(children));
    }

    public List<TestUiComponent> getChildren() {
        return children;
    }

    public void addChild(TestUiComponent child) {
        this.children.add(child);
    }

}