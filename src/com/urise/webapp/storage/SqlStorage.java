package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume ");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeSql("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        " LEFT JOIN section s " +
                        "        ON r.uuid = s.resume_uuid " +
                        "     WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        saveContact(rs, r);
                        saveSection(rs, r);
                    } while (rs.next());
                    return r;
                });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            String uuid = r.getUuid();
            try (PreparedStatement ps = conn.prepareStatement("UPDATE  resume  SET full_name =? WHERE uuid =?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, uuid);
                ps.execute();
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            try (PreparedStatement prs = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid =?")) {
                prs.setString(1, uuid);
                prs.executeUpdate();
            }
            insertContacts(r, conn);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid =?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertSection(r, conn);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    insertContacts(r, conn);
                    insertSection(r, conn);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeSql("DELETE FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> storage = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY full_name,uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    String full_name = rs.getString("full_name");
                    storage.put(uuid, new Resume(uuid, full_name));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact c ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = storage.get(rs.getString("resume_uuid"));
                    saveContact(rs, r);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section s ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume resume = storage.get(rs.getString("resume_uuid"));
                    saveSection(rs, resume);
                }
            }
            return new ArrayList<>(storage.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.executeSql("SELECT count(*) FROM resume ", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void saveContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void saveSection(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value_section");
        if (value != null) {
            SectionType sectionType = SectionType.valueOf(rs.getString("type_section"));
            switch (sectionType) {
                case OBJECTIVE, PERSONAL -> r.addSection(sectionType, new TextSection(value));
                case ACHIEVEMENT, QUALIFICATIONS -> r.addSection(sectionType, new ListSection(List.of(value)));
            }
        }
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSection(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type_section, value_section) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                switch (e.getKey()) {
                    case OBJECTIVE, PERSONAL -> ps.setString(3, ((TextSection) e.getValue()).getTitle());
                    case ACHIEVEMENT, QUALIFICATIONS -> ps.setString(3, String.join("/n", ((ListSection) e.getValue()).getList()));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}


