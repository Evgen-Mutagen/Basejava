package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void updateResume(Resume r, Resume resume) {
        storage.replace(r.getUuid(), r);
    }

    @Override
    protected void keepResume(Resume r, Resume resume) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected Resume getResume(Resume resume) {
        return resume;
    }

    @Override
    protected boolean existKey(Resume resume) {
        return resume != null;
    }

    @Override
    protected void removeResume(Resume resume) {
        storage.remove(resume.getUuid());
    }

    @Override
    protected Resume findSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }
}
