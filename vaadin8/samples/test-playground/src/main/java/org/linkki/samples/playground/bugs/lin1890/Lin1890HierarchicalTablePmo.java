package org.linkki.samples.playground.bugs.lin1890;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "LIN-1890")
public class Lin1890HierarchicalTablePmo extends SimpleTablePmo<StringTreeNode, Lin1890HierarchicalRowPmo> {

    private static final int PAGE_LENGTH = 15;

    public Lin1890HierarchicalTablePmo() {
        super(Arrays.asList(createTree()));
    }

    @Override
    public int getPageLength() {
        return PAGE_LENGTH;
    }

    @Override
    protected Lin1890HierarchicalRowPmo createRow(StringTreeNode modelObject) {
        return new Lin1890HierarchicalRowPmo(modelObject);
    }

    // Create tree with 10 child nodes that also have 10 children
    private static StringTreeNode createTree() {
        return new StringTreeNode("root", IntStream.rangeClosed(0, 9)
                .mapToObj(v1 -> new StringTreeNode(Integer.toString(v1), IntStream
                        .rangeClosed(0, 9)
                        .mapToObj(v2 -> new StringTreeNode(v1 + Integer.toString(v2), Collections.emptyList()))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList()));
    }

}
