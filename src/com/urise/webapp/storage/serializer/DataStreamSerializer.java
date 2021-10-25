package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

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
                dos.writeUTF(entry.getKey().name());


                if ((entry.getKey() == SectionType.OBJECTIVE) || (entry.getKey() == SectionType.PERSONAL)) {
                    TextSection textSection = (TextSection) entry.getValue();
                    dos.writeUTF(textSection.getTitle());
                }

                if ((entry.getKey() == SectionType.ACHIEVEMENT) || (entry.getKey() == SectionType.QUALIFICATIONS)) {
                    ListSection listSection = (ListSection) entry.getValue();
                    for (String str : listSection.getList()) {
                        dos.writeUTF(str);
                    }
                }

                if ((entry.getKey() == SectionType.EXPERIENCE) || (entry.getKey() == SectionType.EDUCATION)) {
                    OrganizationSection organizationSection = (OrganizationSection) entry.getValue();

                    for (Organization org : organizationSection.getList()) {
                        dos.writeUTF(org.getHomePage().getName());
                        dos.writeUTF(org.getHomePage().getUrl());
                        dos.writeUTF(org.getPeriods().toString());
                        for (Period per : org.getPeriods()) {
                            dos.writeUTF(per.getTitle());
                            dos.writeUTF(per.getDescription());
                            dos.writeUTF(per.getEndOfWork().toString());
                            dos.writeUTF(per.getEndOfWork().toString());
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
                if ((section == SectionType.OBJECTIVE) || (section == SectionType.PERSONAL)) {
                    resume.addSection(section, new TextSection(dis.readUTF()));
                }

                if ((section == SectionType.ACHIEVEMENT) || (section == SectionType.QUALIFICATIONS)) {
                    int listSectionSize = dis.readInt();
                    ListSection listSection = new ListSection(new ArrayList<>());
                    for (int j = 0; j < listSectionSize; j++) {
                        listSection.save(dis.readUTF());
                    }
                    resume.addSection(SectionType.valueOf(dis.readUTF()), listSection);
                }

                if ((section == SectionType.EXPERIENCE) || (section == SectionType.EDUCATION)) {
                    List<Organization> org = new ArrayList<>();
                    int positionListSize = dis.readInt();
                    for (int k = 0; k < positionListSize; k++) {
                        Link link = new Link(dis.readUTF(), dis.readUTF());
                        List<Period> per = new ArrayList<>();
                        int perPosition = dis.readInt();
                        for (int m = 0; m < perPosition; m++) {
                            YearMonth startOfWork = YearMonth.parse(dis.readUTF());
                            YearMonth endOfWork = YearMonth.parse(dis.readUTF());
                            String title = dis.readUTF();
                            String description = dis.readUTF();
                            per.add(new Period(startOfWork, endOfWork, title, description));
                        }
                        org.add(new Organization(link, per));
                    }
                    resume.addSection(SectionType.valueOf(dis.readUTF()), new OrganizationSection(org));
                }
            }
            return resume;
        }
    }
}
