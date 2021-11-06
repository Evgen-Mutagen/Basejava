package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            writeStr(r.getUuid(), dos);
            writeStr(r.getFullName(), dos);

            Map<ContactType, String> contacts = r.getContacts();
            writeWithException(contacts.entrySet(), dos, entry -> {
                writeStr(entry.getKey().name(), dos);
                writeStr(entry.getValue(), dos);
            });

            Map<SectionType, AbstractSection> section = r.getSections();
            writeWithException(section.entrySet(), dos, entry -> {
                SectionType key = entry.getKey();
                writeStr(key.name(), dos);
                switch (key) {
                    case PERSONAL, OBJECTIVE -> {
                        TextSection textSection = (TextSection) entry.getValue();
                        writeStr(textSection.getTitle(), dos);
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection listSection = (ListSection) entry.getValue();
                        writeWithException(listSection.getList(), dos, str -> {
                            writeStr(str, dos);
                        });
                    }
                    case EDUCATION, EXPERIENCE -> {
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        writeWithException(organizationSection.getOrganizations(), dos, org -> {
                            Link homePage = org.getHomePage();
                            writeStr(homePage.getName(), dos);
                            writeIfNull(homePage.getUrl(), dos);
                            writeWithException(org.getPositions(), dos, pos -> {
                                writeDate(pos.getStartDate(), dos);
                                writeDate(pos.getEndDate(), dos);
                                writeStr(pos.getTitle(), dos);
                                writeIfNull(pos.getDescription(), dos);
                            });
                        });
                    }
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readWithException(dis, () ->
                    resume.addContact(ContactType.valueOf(readStr(dis)), readStr(dis)));

            readWithException(dis, () -> {
                SectionType section = SectionType.valueOf(readStr(dis));
                switch (section) {
                    case PERSONAL, OBJECTIVE -> resume.addSection(section, new TextSection(readStr(dis)));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> listSection = new ArrayList<>();
                        readWithException(dis, () -> listSection.add(readStr(dis)));
                        resume.addSection(section, new ListSection(listSection));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        List<Organization> org = new ArrayList<>();
                        readWithException(dis, () -> {
                            Link link = new Link(readStr(dis), readIfNull(dis));
                            List<Organization.Position> pos = new ArrayList<>();
                            readWithException(dis, () -> {
                                LocalDate startDate = readDate(dis);
                                LocalDate endDate = readDate(dis);
                                String title = readStr(dis);
                                String description = readIfNull(dis);
                                pos.add(new Organization.Position(startDate, endDate, title, description));
                            });
                            org.add(new Organization(link, pos));
                        });
                        resume.addSection(section, new OrganizationSection(org));
                    }
                }
            });
            return resume;
        }
    }

    @FunctionalInterface
    interface Record<T> {
        void write(T t) throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, Record<T> obj) throws IOException {
        dos.writeInt(collection.size());
        for (T col : collection) {
            obj.write(col);
        }
    }

    private void writeIfNull(String str, DataOutputStream dos) throws IOException {
        dos.writeUTF(Objects.requireNonNullElse(str, ""));
    }

    private void writeDate(LocalDate date, DataOutputStream dos) throws IOException {
         dos.writeInt(date.getYear());
         dos.writeInt(date.getMonth().getValue());
         dos.writeInt(date.getDayOfMonth());
    }

    private void writeStr(String str, DataOutputStream dos) throws IOException {
        dos.writeUTF(str);
    }

    @FunctionalInterface
    interface Readout {
        void read() throws IOException;

    }

    private void readWithException(DataInputStream dis, Readout obj) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            obj.read();
        }
    }

    private String readIfNull(DataInputStream dis) throws IOException {
        String str = dis.readUTF();
        return str.equals("") ? null : str;
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt() ,dis.readInt(), dis.readInt());
    }

    private String readStr(DataInputStream dis) throws IOException {
        return dis.readUTF();
    }
}
