package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            writeWithException(contacts.entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            Map<SectionType, AbstractSection> section = r.getSections();
            writeWithException(section.entrySet(), dos, entry -> {
                SectionType key = entry.getKey();
                dos.writeUTF(key.name());
                switch (key) {
                    case PERSONAL, OBJECTIVE -> {
                        TextSection textSection = (TextSection) entry.getValue();
                        dos.writeUTF(textSection.getTitle());
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection listSection = (ListSection) entry.getValue();
                        writeWithException(listSection.getList(), dos, str -> {
                            dos.writeUTF(str);
                        });
                    }
                    case EDUCATION, EXPERIENCE -> {
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        writeWithException(organizationSection.getOrganizations(), dos, org -> {
                            Link homePage = org.getHomePage();
                            writeIfNull(String.valueOf(homePage.getName()), dos);
                            writeIfNull(String.valueOf(homePage.getUrl()), dos);
                            writeWithException(org.getPositions(), dos, pos -> {
                                dos.writeUTF(writeDates(pos.getStartDate()));
                                dos.writeUTF(writeDates(pos.getEndDate()));
                                dos.writeUTF(pos.getTitle());
                                dos.writeUTF(pos.getDescription());
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
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(readData(dis)), readData(dis));
            }

            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType section = SectionType.valueOf(readData(dis));
                switch (section) {
                    case PERSONAL, OBJECTIVE -> resume.addSection(section, new TextSection(readData(dis)));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        int listSectionSize = dis.readInt();
                        List<String> listSection = new ArrayList<>(listSectionSize);
                        for (int j = 0; j < listSectionSize; j++) {
                            listSection.add(readData(dis));
                        }
                        resume.addSection(section, new ListSection(listSection));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        List<Organization> org = new ArrayList<>();
                        int positionOrgSize = dis.readInt();
                        for (int k = 0; k < positionOrgSize; k++) {
                            Link link = new Link(readIfNull(dis), readIfNull(dis));
                            List<Organization.Position> pos = new ArrayList<>();
                            int positionListSize = dis.readInt();
                            for (int m = 0; m < positionListSize; m++) {
                                LocalDate startDate = ReadDates(dis);
                                LocalDate endDate = ReadDates(dis);
                                String title = readData(dis);
                                String description = readData(dis);
                                pos.add(new Organization.Position(startDate, endDate, title, description));
                            }
                            org.add(new Organization(link, pos));
                        }
                        resume.addSection(section, new OrganizationSection(org));
                    }
                }
            }
            return resume;
        }
    }

    @FunctionalInterface
    interface Write<T> {
        void write(T t) throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, Write<T> obj) throws IOException {
        dos.writeInt(collection.size());
        for (T col : collection) {
            obj.write(col);
        }
    }

    private void writeIfNull(String str, DataOutputStream dos) throws IOException {
        if (str == null) {
            dos.writeUTF("");
        } else {
            dos.writeUTF(str);
        }
    }

    private String writeDates(LocalDate date) {
        return String.valueOf(date);
    }

    private String readIfNull(DataInputStream dis) throws IOException {
        String str = dis.readUTF();
        if (str.equals("")) {
            return null;
        }
        return str;
    }

    private LocalDate ReadDates(DataInputStream dis) throws IOException {
        return LocalDate.parse(dis.readUTF());
    }

    private String readData(DataInputStream dis) throws IOException {
        return dis.readUTF();
    }
}
