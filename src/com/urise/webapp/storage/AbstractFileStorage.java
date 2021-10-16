package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected void updateResume(Resume r, File file) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", r.getUuid(), e);
        }
    }
    @Override
    protected void keepResume(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }  updateResume(r,file);
    }

    protected abstract void doWrite(Resume r,  OutputStream os) throws IOException;

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            list.add(getResume(file));
        }
        return list;
    }

    protected abstract Resume doGet(InputStream file) throws IOException;

    @Override
    protected Resume getResume(File file) {
        try {
            return doGet(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean existKey(File file) {
        return file.exists();
    }

    @Override
    protected void removeResume(File file) {
        file.delete();

    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    public void clear() {
        if (directory.listFiles() != null) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                removeResume(file);
            }
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        assert list != null;
        return list.length;
    }
}
