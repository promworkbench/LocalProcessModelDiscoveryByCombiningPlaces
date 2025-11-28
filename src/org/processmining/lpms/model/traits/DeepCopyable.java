package org.processmining.lpms.model.traits;

public interface DeepCopyable<T extends DeepCopyable<T>> {
    T deepCopy();
}
