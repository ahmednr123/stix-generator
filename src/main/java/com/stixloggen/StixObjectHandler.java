package com.stixloggen;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.sdos.Identity;
import eu.csaware.stix2.sdos.Indicator;
import eu.csaware.stix2.sros.Sighting;
import eu.csaware.stix2.util.Stix2Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class StixObjectHandler {
    private static Logger LOGGER = Logger.getLogger(StixObjectHandler.class.getName());

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
        LOGGER.info("Setting ID=" + ID + " with " + Stix2Gson.PRODUCTION.toJson(stixObject));
        mappedObjects.put(ID, stixObject);
    }

    public void pushAll (ArrayList<IdentifiedStixObject> stixObjects) {
        for (IdentifiedStixObject object : stixObjects) {
            mappedObjects.put(object.getId(), object);
        }
    }

    public IdentifiedStixObject find (Stix2Type objType, String data) {
        System.out.println("FOR: " + objType + ", DATA: " + data);

        for (IdentifiedStixObject object : mappedObjects.values()) {
            switch (object.getType()) {
                case IDENTITY:
                    Identity identity = (Identity) object;
                    if (identity.getName().equals(data)) {
                        LOGGER.info("Found " + identity.getId());
                        return identity;
                    }
                    break;
                case INDICATOR:
                    Indicator indicator = (Indicator) object;
                    if (indicator.getPattern().equals(data)) {
                        LOGGER.info("Found " + indicator.getId());
                        return indicator;
                    }
                    break;
                case SIGHTING:
                    Sighting sighting = (Sighting) object;
                    if (sighting.getSightingOfRef().equals(data)) {
                        LOGGER.info("Found " + sighting.getId());
                        return sighting;
                    }
                    break;
                default:
                    throw new RuntimeException("Finder doesn't support type: " + objType);
            }
        }

        LOGGER.info("Object[type="+objType+"] with data=" + data + " not found");
        return null;
    }
}