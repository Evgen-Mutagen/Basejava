package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.ResumeTestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();
    protected Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(ResumeTestData.RESUME_1);
        storage.save(ResumeTestData.RESUME_2);
        storage.save(ResumeTestData.RESUME_3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        Resume updateResume = new Resume(ResumeTestData.UUID3, "pikachu");
        storage.update(updateResume);
        assertEquals(updateResume, storage.get(ResumeTestData.UUID3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(ResumeTestData.RESUME_4);
    }

    @Test
    public void save() throws Exception {
        storage.save(ResumeTestData.RESUME_4);
        assertEquals(4, storage.size());
        assertEquals(ResumeTestData.RESUME_4, storage.get(ResumeTestData.UUID4));

    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(ResumeTestData.RESUME_3);
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(ResumeTestData.RESUME_1, storage.get(ResumeTestData.RESUME_1.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(ResumeTestData.UUID2);
        assertEquals(2, storage.size());
        storage.get(ResumeTestData.UUID2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(ResumeTestData.UUID4);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> allResumes = storage.getAllSorted();
        assertEquals(3, allResumes.size());
        assertEquals(Arrays.asList(ResumeTestData.RESUME_1, ResumeTestData.RESUME_2, ResumeTestData.RESUME_3), allResumes);
        //  Assert.assertArrayEquals(allResumes, storage.getAll());
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }
}
