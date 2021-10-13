package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapUuidStorage extends AbstractStorage<String> {
    private Map<String, Resume> storage = new TreeMap<>();

    @Override
    protected void updateResume(Resume r, String uuid) {
        storage.replace(uuid, r);
    }

    @Override
    protected void keepResume(Resume r, String uuid) {
        storage.put(uuid, r);
    }

    @Override
    protected Resume getResume(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void removeResume(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected String findSearchKey(String uuid) {
        return uuid;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected boolean existKey(String uuid) {
        return storage.containsKey(uuid);
    }
}
