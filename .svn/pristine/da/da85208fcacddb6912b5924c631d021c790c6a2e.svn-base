package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.utils.CircularList;

import java.util.Collection;
import java.util.stream.Collectors;

public class CircularWindowTree<INPUT, DATA> {

    private final CircularList<WindowTreeNode<INPUT, DATA>> circularList;
    private final int windowWidth;

    private final DataFactory<INPUT, DATA> dataFactory;

    public CircularWindowTree(int windowWidth, DataFactory<INPUT, DATA> dataFactory) {
        this.windowWidth = windowWidth;
        this.circularList = new CircularList<>(windowWidth - 1);
        this.dataFactory = dataFactory;
    }

    public void tryAddChildren(int findPos, int setPos, INPUT input) {
        if (this.circularList.get(findPos) == null)
            addRootNode(findPos);
        this.circularList.getAllUntil(findPos).forEach(n -> n.tryAddChild(findPos, setPos, input));
    }

    public void clear(int position) {
        this.circularList.clear(position);
    }

    private void addRootNode(int findPos) {
        WindowTreeNode<INPUT, DATA> newNode = new WindowTreeNode<>(
                findPos, this.windowWidth - 1, this.dataFactory.create(), this.dataFactory);
        this.circularList.set(findPos, newNode);
    }

    public Collection<DATA> getData() {
        return this.circularList.getAllNotNull()
                .stream()
                .map(WindowTreeNode::getData)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
