package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;


public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).
            thenComparing(Resume::getUuid);

    public void update(Resume r) {
        LOG.info("Update " + r);
        SK searchKey = getSearchKeyForExist(r.getUuid());
        updateResume(r, searchKey);
    }

    protected abstract void updateResume(Resume r, SK searchKey);

    public void save(Resume r) {
        LOG.info("Save " + r);
        SK searchKey =  getSearchKeyForNotExist(r.getUuid());
        keepResume(r, searchKey);
    }

    protected abstract void keepResume(Resume r, SK searchKey);

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> resumes = getAll();
        resumes.sort(RESUME_COMPARATOR);
        return resumes;
    }

    protected abstract List<Resume> getAll();

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK searchKey = getSearchKeyForExist(uuid);
        return getResume(searchKey);
    }

    protected abstract Resume getResume(SK searchKey);

    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK searchKey = getSearchKeyForExist(uuid);
        removeResume(searchKey);
    }

    public SK getSearchKeyForExist(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (!existKey(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    public SK getSearchKeyForNotExist(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (existKey(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract boolean existKey(SK searchKey);

    protected abstract void removeResume(SK SearchKey);

    protected abstract SK findSearchKey(String uuid);
}