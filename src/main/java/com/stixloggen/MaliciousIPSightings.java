package com.stixloggen;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.sdos.Identity;
import eu.csaware.stix2.sdos.Indicator;
import eu.csaware.stix2.sros.Sighting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MaliciousIPSightings extends StixStrategy {
    private static ArrayList<String> maliciousIPs = new ArrayList<>();

    public MaliciousIPSightings (ArrayList<String> Logs) {
        super(Logs);

        String line;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\inc-611\\Downloads\\Send-Archive\\malicious-ips.txt"));
            while ((line = bufferedReader.readLine()) != null) {
                maliciousIPs.add(line.trim());
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<IdentifiedStixObject>
    processLog (String log)
    {
        String[] data = log.split(" ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(data[0], formatter);
        String device_ip = data[1];
        String source_ip = data[2];

        ArrayList<IdentifiedStixObject> stixObjects = new ArrayList<>();

        if (maliciousIPs.contains(source_ip)) {
            Identity identity = (Identity) createStixObject(Stix2Type.IDENTITY, dateTime);
            identity.setName("Device:" + device_ip);

            Indicator indicator = (Indicator) createStixObject(Stix2Type.INDICATOR, dateTime);
            indicator.setName("MaliciousIP:" + source_ip);
            indicator.setPattern("[ipv4-addr:value='" + source_ip + "']");
            indicator.setValidFrom(dateTime);
            ArrayList<String> labels = new ArrayList<>();
            labels.add("malicious-activity");
            indicator.setLabels(labels);

            Sighting sighting = (Sighting) createStixObject(Stix2Type.SIGHTING, dateTime);
            sighting.setCount(1);
            sighting.setSightingOfRef(indicator.getId());
            ArrayList<String> sightedByRefs = new ArrayList<>();
            sightedByRefs.add(identity.getId());
            sighting.setWhereSightedRefs(sightedByRefs);

            stixObjects.add(identity);
            stixObjects.add(indicator);
            stixObjects.add(sighting);
        }

        return stixObjects;
    }
}