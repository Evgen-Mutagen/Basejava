package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.XmlStreamSerialize;


public class XmlPathStorageTest extends AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamSerialize.XmlStreamSerializer()));
    }
}
