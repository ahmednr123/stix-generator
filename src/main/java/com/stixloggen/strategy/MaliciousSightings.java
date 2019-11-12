package com.stixloggen.strategy;

import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.sdos.Identity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaliciousSightings extends StixStrategy {
    private static ArrayList<String> maliciousIPs = new ArrayList<>();

    private String serverName;

    public MaliciousSightings(ArrayList<String> Logs, String serverName) {
        super(Logs);

        this.serverName = serverName;
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

    @Override
    protected Identity extractIdentity(String log) {
        Pattern ip_pattern = Pattern.compile("192.168.(?:\\d{1,3}\\.){1}\\d{1,3}");
        Matcher ip_matcher = ip_pattern.matcher(log);

        Identity identity = (Identity) createStixObject(Stix2Type.IDENTITY, extractDateTime(log));
        if (ip_matcher.find()) {
            identity.setName("Device:" + ip_matcher.group());
            return identity;
        }

        identity.setName(serverName);
        return identity;
    }

    @Override
    LocalDateTime extractDateTime(String log) {
        String[] data = log.split(" ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(data[0], formatter);

        return dateTime;
    }
}