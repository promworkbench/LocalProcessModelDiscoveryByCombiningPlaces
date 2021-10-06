package org.processmining.placebasedlpmdiscovery.model.fpgrowth;


import org.processmining.placebasedlpmdiscovery.model.exception.OutOfWindowException;

import java.util.*;
import java.util.stream.Collectors;

public class WindowTreeNode<INPUT, DATA> {
    private final int windowWidth;
    private final int position;
    private final List<List<WindowTreeNode<INPUT, DATA>>> children;

    private final DATA value;

    private final DataFactory<INPUT, DATA> dataFactory;

    public WindowTreeNode(int position, int windowWidth, DATA value, DataFactory<INPUT, DATA> dataFactory) {
        this.windowWidth = windowWidth;
        this.position = position;
        this.children = new ArrayList<>(windowWidth);
        for (int i = 0; i < windowWidth; ++i) {
            this.children.add(new LinkedList<>());
        }
        this.value = value;
        this.dataFactory = dataFactory;
    }

    public void tryAddChild(int findPos, int setPos, INPUT input) {
        Optional<DATA> newValue = dataFactory.create(input, findPos, setPos, this.value, this.position);
        if (newValue.isPresent()) {
            int index = getChildIndex(setPos);
            int childWindowWidth = this.windowWidth - index + 1;
            WindowTreeNode<INPUT, DATA> child =
                    new WindowTreeNode<INPUT, DATA>(setPos, childWindowWidth, newValue.get(), this.dataFactory);
            this.children.get(index).add(child);
        } else {
            this.getChildrenUntilPosition(findPos).forEach(child -> child.tryAddChild(findPos, setPos, input));
        }
    }

    public List<WindowTreeNode<INPUT, DATA>> getChildrenOnPosition(int position) {
        int index = getChildIndex(position);
        return this.children.get(index);
    }

    public List<WindowTreeNode<INPUT, DATA>> getChildrenUntilPosition(int position) {
        // +1 is added because the second index is exclusive
        return this.children.subList(0, getChildIndex(position) + 1)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private int getChildIndex(int position) {
        int index = position - this.position - 1;
        if (index < 0 || index >= windowWidth) {
            throw new OutOfWindowException(windowWidth, index, this.position, position);
        }
        return index;
    }

    public DATA getValue() {
        return value;
    }

    public Collection<DATA> getData() {
        List<DATA> list = this.children
                .stream()
                .filter(col -> !col.isEmpty())
                .flatMap(Collection::stream)
                .map(WindowTreeNode::getData)
                .filter(col -> !col.isEmpty())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        list.add(value);
        return list;
    }
}
