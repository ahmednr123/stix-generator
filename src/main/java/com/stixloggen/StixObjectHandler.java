package com.stixloggen;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.common.TypedStixObject;
import eu.csaware.stix2.sdos.Identity;
import eu.csaware.stix2.sdos.Indicator;
import eu.csaware.stix2.sros.Sighting;
import eu.csaware.stix2.util.Stix2Util;

import java.util.ArrayList;
import java.util.HashMap;

public class StixObjectHandler {

    private static StixObjectHandler instance;

    private HashMap<String, IdentifiedStixObject> mappedObjects;

    static {
        instance = new StixObjectHandler();
    }

    private StixObjectHandler () {
        mappedObjects = new HashMap<>();
    }

    public static StixObjectHandler getInstance () {
        return instance;
    }

    public ArrayList<IdentifiedStixObject> getAll () {
        return new ArrayList<>(mappedObjects.values());
    }

    public IdentifiedStixObject get (String objectID) {
        if (mappedObjects.containsKey(objectID)) {
            return mappedObjects.get(objectID);
        }

        return null;
    }

    public void set (String ID, IdentifiedStixObject stixObject) {
        mappedObjects.put(ID, stixObject);
    }

    public IdentifiedStixObject find (Stix2Type objType, String data) {
        System.out.println("FOR: " + objType + ", DATA: " + data);

        for (IdentifiedStixObject object : mappedObjects.values()) {
            switch (object.getType()) {
                case IDENTITY:
                    Identity identity = (Identity) object;
                    System.out.println("\tType: IDENTITY, Data: " + identity.getName());
                    if (identity.getName().equals(data))
                        return identity;
                    break;
                case INDICATOR:
                    Indicator indicator = (Indicator) object;
                    System.out.println("\tType: INDICATOR, Data: " + indicator.getPattern());
                    if (indicator.getPattern().equals(data))
                        return indicator;
                    break;
                case SIGHTING:
                    Sighting sighting = (Sighting) object;
                    System.out.println("\tType: SIGHTING, Data: " + sighting.getSightingOfRef());
                    if (sighting.getSightingOfRef().equals(data))
                        return sighting;
                    break;
                default:
                    throw new RuntimeException("Finder doesn't support type: " + objType);
            }
        }

        System.out.println("NOT FOUND");
        return null;
    }
}