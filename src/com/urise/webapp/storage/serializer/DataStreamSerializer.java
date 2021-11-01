package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> section = r.getSections();
            dos.writeInt(section.size());
            for (Map.Entry<SectionType, AbstractSection> entry : section.entrySet()) {
                SectionType key = entry.getKey();
                dos.writeUTF(key.name());
                switch (key) {
                    case PERSONAL, OBJECTIVE -> {
                        TextSection textSection = (TextSection) entry.getValue();
                        dos.writeUTF(textSection.getTitle());
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection listSection = (ListSection) entry.getValue();
                        dos.writeInt(listSection.getList().size());
                        for (String str : listSection.getList()) {
                            dos.writeUTF(str);
                        }

                    }
                    case EDUCATION, EXPERIENCE -> {
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        dos.writeInt(organizationSection.getOrganizations().size());
                        for (Organization org : organizationSection.getOrganizations()) {
                            dos.writeUTF(org.getHomePage().getName());
                            dos.writeUTF(org.getHomePage().getUrl());
                            dos.writeInt(org.getPositions().size());
                            for (Organization.Position pos : org.getPositions()) {
                                dos.writeUTF(pos.getStartDate().toString());
                                dos.writeUTF(pos.getEndDate().toString());
                                dos.writeUTF(pos.getTitle());
                                dos.writeUTF(pos.getDescription());
                            }
                        }
                    }
                }
            }
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
                            Link link = new Link(readData(dis), readData(dis));
                            List<Organization.Position> pos = new ArrayList<>();
                            int positionListSize = dis.readInt();
                            for (int m = 0; m < positionListSize; m++) {
                                LocalDate startDate = LocalDate.parse(readData(dis));
                                LocalDate endDate = LocalDate.parse(readData(dis));
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

    private String readData(DataInputStream dis) throws IOException {
        return dis.readUTF();
    }
}
