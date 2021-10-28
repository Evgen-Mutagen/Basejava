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
            dos.writeInt(contacts.size());
            for (Map.Entry<SectionType, AbstractSection> entry : section.entrySet()) {
                SectionType key = entry.getKey();
                dos.writeUTF(key.name());
                switch (key) {
                    case PERSONAL:
                    case OBJECTIVE:
                        TextSection textSection = (TextSection) entry.getValue();
                        dos.writeUTF(textSection.getTitle());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = (ListSection) entry.getValue();
                        for (String str : listSection.getList()) {
                            dos.writeUTF(str);
                        }
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        for (Organization org : organizationSection.getOrganizations()) {
                            dos.writeUTF(org.getHomePage().getName());
                            dos.writeUTF(org.getHomePage().getUrl());
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

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            for (int i = 0; i < size; i++) {
                SectionType section = SectionType.valueOf(dis.readUTF());
                switch (section) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(section, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        int listSectionSize = dis.readInt();
                        ListSection listSection = new ListSection(new ArrayList<>());
                        for (int j = 0; j < listSectionSize; j++) {
                            listSection.save(dis.readUTF());
                        }
                        resume.addSection(SectionType.valueOf(dis.readUTF()), listSection);
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> org = new ArrayList<>();
                        int positionOrgSize = dis.readInt();
                        for (int k = 0; k < positionOrgSize; k++) {
                            Link link = new Link(dis.readUTF(), dis.readUTF());
                            List<Organization.Position> pos = new ArrayList<>();
                            int positionListSize = dis.readInt();
                            for (int m = 0; m < positionListSize; m++) {
                                LocalDate startDate = LocalDate.parse(dis.readUTF());
                                LocalDate endDate = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = dis.readUTF();
                                pos.add(new Organization.Position(startDate, endDate, title, description));
                            }
                            org.add(new Organization(link, pos));
                        }
                        resume.addSection(SectionType.valueOf(dis.readUTF()), new OrganizationSection(org));
                }
            }
            return resume;
        }
    }
}
