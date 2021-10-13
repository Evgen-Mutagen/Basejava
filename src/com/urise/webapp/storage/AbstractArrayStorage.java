package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void updateResume(Resume r, Integer index) {
        storage[index] = r;
    }

    protected void keepResume(Resume r, Integer index) {
        if (size >= storage.length) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        saveResume(r, index);
        size++;
    }

    protected abstract void saveResume(Resume r, int index);

    public Resume getResume(Integer index) {
        return storage[index];
    }

    protected void removeResume(Integer index) {
        size--;
        deleteResume(index);
    }

    protected abstract void deleteResume(int index);

    protected List<Resume> getAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    public int size() {
        return size;
    }

    @Override
    protected boolean existKey(Integer searchKey) {
        return searchKey >= 0;
    }

    protected abstract Integer findSearchKey(String uuid);
}
