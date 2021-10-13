package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private List<Resume> storage = new ArrayList<>();

    @Override
    protected void updateResume(Resume r, Integer searchKey) {
        storage.set(searchKey, r);
    }

    @Override
    protected void keepResume(Resume r, Integer searchKey) {
        storage.add(r);
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return storage.get(searchKey);
    }


    @Override
    protected void removeResume(Integer searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected Integer findSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (uuid.equals(storage.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected boolean existKey(Integer searchKey) {
        return searchKey >= 0;
    }
}
