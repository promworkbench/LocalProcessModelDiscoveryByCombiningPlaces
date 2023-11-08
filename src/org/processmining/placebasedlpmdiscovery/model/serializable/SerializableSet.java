package org.processmining.placebasedlpmdiscovery.model.serializable;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SerializableSet<T extends Serializable> implements SerializableCollection<T> {

    private static final long serialVersionUID = -2512303328710773358L;
    private Set<T> elements;

    public SerializableSet() {
        this.elements = new HashSet<>();
    }

    public SerializableSet(Collection<T> elements) {
        this.elements = new HashSet<>(elements);
    }

    public boolean add(T element) {
        return this.elements.add(element);
    }

    public boolean contains(T element) {
        return this.elements.contains(element);
    }

    public boolean remove(T element) {
        return this.elements.remove(element);
    }

//    public void importFromStream(InputStream stream) {
//        try (ObjectInputStream ois = new ObjectInputStream(stream)) {
//            int size = ois.readInt();
//            while (size > 0) {
//                Object element = ois.readObject();
//                if (!this.getElementClass().isInstance(element)) {
//                    System.err.println("Collection object is not of type: " + this.getElementClass());
//                } else {
//                    this.elements.add((T) element);
//                    size--;
//                }
//            }
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void exportToFile(File file) {
//        try (FileOutputStream fos = new FileOutputStream(file);
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//
//            oos.writeInt(this.size());
//            for (T element : this.elements) {
//                oos.writeObject(element);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public int size() {
        return this.elements.size();
    }

    public Set<T> getElements() {
        return this.elements;
    }

    @Override
    public boolean addAll(Collection<T> elements) {
        return this.elements.addAll(elements);
    }

    public SerializableList<T> getList() {
        return new SerializableList<>(this.elements);
    }
}
