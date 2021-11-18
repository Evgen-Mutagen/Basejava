package com.urise.webapp.exception;

public class StorageException extends RuntimeException {
    private String uuid;


    public StorageException(String message, String uuid) {

        this.uuid = uuid;
    }

    public StorageException(Exception e) {
        this(e.getMessage(), e);
    }

    public StorageException(String message, Exception e) {
        this(message, null, e);
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }

    public StorageException(String message) {
        super(message);
    }


    public String getUuid() {
        return uuid;
    }

}