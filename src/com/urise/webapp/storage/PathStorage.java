package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.strategy.Strategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final Strategy strategy;

    protected PathStorage(String dir, Strategy strategy) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
        this.strategy = strategy;
    }

    @Override
    protected void updateResume(Resume r, Path path) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected void keepResume(Resume r, Path path) {
        try {
            Files.createFile(path);

        } catch (IOException e) {
            throw new StorageException("IO error", path.toString());
        }
        updateResume(r, path);
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (Path file : checkFiles().toList()) {
            list.add(getResume(file));
        }
        return list;
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return strategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
        }
    }

    @Override
    protected boolean existKey(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void removeResume(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
        }
    }

    @Override
    protected Path findSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    public void clear() {
        checkFiles().forEach(this::removeResume);
    }

    @Override
    public int size() {
        return (int) checkFiles().count();

    }

    public Stream<Path> checkFiles() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Path error", null);
        }
    }
}