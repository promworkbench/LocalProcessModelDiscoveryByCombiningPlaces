package org.processmining.placebasedlpmdiscovery.model.logs;

public class SimpleActivity implements Activity{

    private final String name;

    public SimpleActivity(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
