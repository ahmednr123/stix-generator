package com.stixloggen;

import eu.csaware.stix2.common.Bundle;
import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.sdos.Identity;
import eu.csaware.stix2.sdos.Indicator;
import eu.csaware.stix2.sros.Sighting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StixStrategy {
    private long ID = 0;
    private ArrayList<String> Logs;

    private ArrayList<IdentifiedStixObject> stixObjects = new ArrayList<>();

    public StixStrategy (ArrayList<String> Logs) {
        this.Logs = Logs;
    }

    abstract Identity extractIdentity (String log);
    abstract LocalDateTime extractDateTime (String log);

    ArrayList<IdentifiedStixObject> processLog (String log) {
        ArrayList<IdentifiedStixObject> stixObjects = new ArrayList<>();
        ArrayList<Indicator> indicators = StixObjectHandler.getInstance().getIndicators();

        Identity identity = extractIdentity(log);
        LocalDateTime dateTime = extractDateTime(log);

        Pattern ip_pattern = Pattern.compile("(?:\\d{1,3}\\.){3}\\d{1,3}");
        Pattern url_pattern = Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Pattern domain_pattern = Pattern.compile("www.*?\\.(.*?\\.[a-zA-Z]+)");

        Matcher ip_matcher = ip_pattern.matcher(log);
        Matcher domain_matcher = domain_pattern.matcher(log);
        Matcher url_matcher = url_pattern.matcher(log);

        ArrayList<String> IPs = new ArrayList<>();
        while (ip_matcher.find()) {
            IPs.add(ip_matcher.group());
        }

        String pattern = "";
        if (IPs.size() > 1) {
            pattern = "[ipv4-addr:value='" + IPs.get(1) + "']";
        } else if (url_matcher.find()) {
            pattern = "[url:value='" + url_matcher.group() + "']";
        } else if (domain_matcher.find()) {
            pattern = "[domain-name:value='" + domain_matcher.group() + "']";
        }

        if (!pattern.equals(""))
        for (Indicator indicator : indicators) {
            if (indicator.getPattern().equals(pattern)) {
                Sighting sighting = (Sighting) createStixObject(Stix2Type.SIGHTING, dateTime);
                sighting.setCount(1);
                sighting.setSightingOfRef(indicator.getId());
                sighting.setLastSeen(dateTime);
                ArrayList<String> sightedByRefs = new ArrayList<>();
                sightedByRefs.add(identity.getId());
                sighting.setWhereSightedRefs(sightedByRefs);

                stixObjects.add(identity);
                stixObjects.add(indicator);
                stixObjects.add(sighting);
                break;
            }
        }

        return stixObjects;
    };

    /**
     * The method will create a StixObject and assign an ID to it.
     * It also adds it to the stixObject ArrayList so that the objects
     * can be used later on.
     *
     * Changing the ID of the StixObject returned would cause weird
     * problems.
     *
     * @param objType
     * @param dateTime
     * @return
     */
    public IdentifiedStixObject createStixObject (Stix2Type objType, LocalDateTime dateTime) {
        IdentifiedStixObject object;
        String objectID = "object:" + ID++;

        switch (objType) {
            case IDENTITY:
                Identity identity = new Identity();
                identity.setCreated(dateTime);
                identity.setModified(dateTime);
                object = identity;
                break;
            case INDICATOR:
                Indicator indicator = new Indicator();
                indicator.setCreated(dateTime);
                indicator.setModified(dateTime);
                object = indicator;
                break;
            case SIGHTING:
                Sighting sighting = new Sighting();
                sighting.setCreated(dateTime);
                sighting.setModified(dateTime);
                object = sighting;
                break;
            default:
                throw new RuntimeException("Creator doesn't support type: " + objType);
        }

        object.setId(objectID);
        return object;
    }

    Bundle bundle () {
        for (String log : Logs) {
            ArrayList<IdentifiedStixObject> stixObjects = processLog(log);
            StixObjectManager.execute(stixObjects);
        }

        stixObjects = StixObjectHandler.getInstance().getAll();

        Bundle bundle = new Bundle();
        bundle.setId(Stix2Type.BUNDLE.toString() + "--" + UUID.randomUUID().toString());
        bundle.setObjects(new ArrayList<>(stixObjects));
        stixObjects.clear();
        return bundle;
    }
}
