package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    protected void saveResume(Resume r, int index) {
        storage[size] = r;
    }

    protected void deleteResume(int index) {
        storage[index] = storage[size];
    }

    protected Integer findSearchKey(String uuid) {
        for (int index = 0; index < size; index++) {
            if (storage[index].getUuid().equals(uuid)) {
                return index;
            }
        }
        return -1;
    }
}
