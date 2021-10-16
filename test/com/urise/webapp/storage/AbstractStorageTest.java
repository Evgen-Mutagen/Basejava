package com.urise.webapp.storage;

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
    protected static final File STORAGE_DIR = new File("E:\\TopJava\\basejava\\src\\storage");
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final Resume R1;
    private static final Resume R2;
    private static final Resume R3;
    private static final Resume R4;

    static {
        R1 = ResumeTestData.R1;
        R2 = ResumeTestData.R2;
        R3 = ResumeTestData.R3;
        R4 = ResumeTestData.R4;
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        Resume updateResume = new Resume(UUID_3, "pikachu");
        storage.update(updateResume);
        assertEquals(updateResume, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(R4);
    }

    @Test
    public void save() throws Exception {
        storage.save(R4);
        assertEquals(4, storage.size());
        assertEquals(R4, storage.get(UUID_4));

    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(R3);
    }

    @Test
    public void get() throws Exception {
        assertEquals(R1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
        storage.get(UUID_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(UUID_4);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> allResumes = storage.getAllSorted();
        Assert.assertEquals(Arrays.asList(R1, R2, R3), allResumes);
        //  Assert.assertArrayEquals(allResumes, storage.getAll());
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }
}
