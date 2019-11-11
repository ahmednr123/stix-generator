package com.stixloggen;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.sdos.*;
import eu.csaware.stix2.sros.Relationship;
import eu.csaware.stix2.sros.Sighting;
import eu.csaware.stix2.util.Stix2Gson;
import eu.csaware.stix2.util.Stix2Util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class StixObjectHandler {
    private static String ROOT_PATH = "C:\\Users\\inc-611\\Documents\\StixLogGenerator\\stix-object-repo\\";
    private static Logger LOGGER = Logger.getLogger(StixObjectHandler.class.getName());

    private static StixObjectHandler instance;

    private HashMap<String, IdentifiedStixObject> mappedObjects;
    private ArrayList<Indicator> indicators;

    static {
        instance = new StixObjectHandler();
    }

    private StixObjectHandler () {
        mappedObjects = new HashMap<>();
        indicators = new ArrayList<>();

        LOGGER.info("Loading identity patterns");
        File directory = new File(ROOT_PATH + "indicator");
        File[] files = directory.listFiles();

        if (directory.exists() && files != null)
        for (File file : files) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                Indicator indicator = Stix2Gson.PRODUCTION.fromJson(reader, Indicator.class);
                reader.close();

                LOGGER.info("Loaded: " + indicator.getId());
                indicators.add(indicator);
            } catch (Exception e) {
                LOGGER.info("Failed to load: " + file.getName());
                e.printStackTrace();
            }
        }

    }

    public ArrayList<Indicator> getIndicators () {
        return indicators;
    }

    public static StixObjectHandler getInstance () {
        return instance;
    }

    public ArrayList<IdentifiedStixObject> getAll () {
        ArrayList<IdentifiedStixObject> objects = new ArrayList<>(mappedObjects.values());

        updateRepo(objects);
        pullRelatedObjects(objects);

        // mappedObjects.values() is called again cause it
        // will be updated by the previous method.
        return new ArrayList<>(mappedObjects.values());
    }

    public void pushAll (ArrayList<IdentifiedStixObject> stixObjects) {
        for (IdentifiedStixObject object : stixObjects) {
            if (object.getType() == Stix2Type.SIGHTING) {
                Sighting sighting = (Sighting) object;
            }
            mappedObjects.put(object.getId(), object);
        }
    }

    private void updateRepo (ArrayList<IdentifiedStixObject> objects) {
        for (IdentifiedStixObject object : objects) {
            try {
                String objectTypeString = object.getType().toString().toLowerCase();
                FileWriter fileWriter = new FileWriter(ROOT_PATH + objectTypeString + "\\" + object.getId());

                String json = Stix2Gson.PRODUCTION.toJson(object, getStixClass(object.getType()));
                LOGGER.info("Updating: " + object.getId());
                LOGGER.info("Content: " + json);

                fileWriter.write( json );
                fileWriter.close();
            } catch (Exception e) {
                LOGGER.info("Failed to update/create: " + object.getId() + "\n" + Stix2Gson.PRODUCTION.toJson(object));
            }
        }
    }

    private void pullRelatedObjects (ArrayList<IdentifiedStixObject> objects) {
        for (IdentifiedStixObject object : objects) {
            ArrayList<IdentifiedStixObject> relatedObjects = getRelatedObjects(object);
            for (IdentifiedStixObject relatedObject : relatedObjects) {
                mappedObjects.put(relatedObject.getId(), relatedObject);
            }

            pullRelatedObjects(relatedObjects);
        }
    }

    private ArrayList<IdentifiedStixObject> getRelatedObjects (IdentifiedStixObject object) {
        ArrayList<IdentifiedStixObject> refs = new ArrayList<>();

        // These objects poses no RELATIONSHIPS
        if (object.getType() == Stix2Type.SIGHTING
                || object.getType() == Stix2Type.IDENTITY
                || object.getType() == Stix2Type.RELATIONSHIP)
        {
            return refs;
        }

        File directory = new File(ROOT_PATH + "relationship");
        File[] files = directory.listFiles();

        if (directory.exists() && files != null)
        for (File file : files) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                Relationship relationship = Stix2Gson.PRODUCTION.fromJson(reader, Relationship.class);
                reader.close();

                refs.add(relationship);

                try {
                    if (relationship.getSourceRef().equals(object.getId())) {
                        Stix2Type targetType = Stix2Util.extractTypeFromId(relationship.getTargetRef());
                        String targetTypeString = targetType.toString().toLowerCase();

                        BufferedReader reader1 = new BufferedReader(new FileReader(ROOT_PATH + targetTypeString + "\\" + relationship.getTargetRef()));
                        refs.add((IdentifiedStixObject) Stix2Gson.PRODUCTION.fromJson(reader1, getStixClass(targetType)));
                        reader1.close();
                    }
                } catch (Exception e) {
                    LOGGER.info("Failed to load: " + relationship.getSourceRef());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                LOGGER.info("Failed to load: " + file.getName());
                e.printStackTrace();
            }
        }

        return refs;
    }

    public IdentifiedStixObject find (Stix2Type objType, String data) {
        // First check if the object exists in mappedObjects
        for (IdentifiedStixObject object : mappedObjects.values()) {
            switch (object.getType()) {
                case IDENTITY:
                    Identity identity = (Identity) object;
                    if (identity.getName().equals(data))
                        return identity;
                    break;
                case INDICATOR:
                    Indicator indicator = (Indicator) object;
                    if (indicator.getPattern().equals(data))
                        return indicator;
                    break;
                case SIGHTING:
                    Sighting sighting = (Sighting) object;
                    if (sighting.getSightingOfRef().equals(data))
                        return sighting;
                    break;
                default:
                    throw new RuntimeException("Finder doesn't support type: " + objType);
            }
        }

        // If object doesn't exist in mappedObjects look the object up in Stix Object Repo
        File directory = new File(ROOT_PATH + objType.toString().toLowerCase());
        File[] files = directory.listFiles();

        if (directory.exists() && files != null)
        for (File file : files) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                switch (objType) {
                    case IDENTITY:
                        Identity identity = Stix2Gson.PRODUCTION.fromJson(reader, Identity.class);
                        if (identity.getName().equals(data)) {
                            LOGGER.info("Found " + identity.getId());
                            return identity;
                        }
                        break;
                    case INDICATOR:
                        Indicator indicator = Stix2Gson.PRODUCTION.fromJson(reader, Indicator.class);
                        if (indicator.getPattern().equals(data)) {
                            LOGGER.info("Found " + indicator.getId());
                            return indicator;
                        }
                        break;
                    case SIGHTING:
                        Sighting sighting = Stix2Gson.PRODUCTION.fromJson(reader, Sighting.class);
                        if (sighting.getSightingOfRef().equals(data)) {
                            LOGGER.info("Found " + sighting.getId());
                            return sighting;
                        }
                        break;
                    default:
                        throw new RuntimeException("Finder doesn't support type: " + objType);
                }
                reader.close();
            } catch (Exception e) {
                LOGGER.info("Failed to load: " + file.getName());
                e.printStackTrace();
            }
        }

        LOGGER.info("Object[type="+objType+"] with data=" + data + " not found");
        return null;
    }

    private static Class getStixClass (Stix2Type type) {
        switch (type) {
            case ATTACK_PATTERN:
                return AttackPattern.class;
            case CAMPAIGN:
                return Campaign.class;
            case COURSE_OF_ACTION:
                return CourseOfAction.class;
            case IDENTITY:
                return Identity.class;
            case INDICATOR:
                return Indicator.class;
            case INTRUSION_SET:
                return IntrusionSet.class;
            case MALWARE:
                return Malware.class;
            case OBSERVED_DATA:
                return ObservedData.class;
            case REPORT:
                return Report.class;
            case THREAT_ACTOR:
                return ThreatActor.class;
            case TOOL:
                return Tool.class;
            case VULNERABILITY:
                return Vulnerability.class;
            case RELATIONSHIP:
                return Relationship.class;
            case SIGHTING:
                return Sighting.class;
            default:
                throw new RuntimeException("Invalid Object");
        }
    }
}