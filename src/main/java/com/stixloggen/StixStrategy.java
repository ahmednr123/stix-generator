package com.stixloggen;

import eu.csaware.stix2.common.Bundle;
import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.common.TypedStixObject;
import eu.csaware.stix2.sdos.Identity;
import eu.csaware.stix2.sdos.Indicator;
import eu.csaware.stix2.sros.Sighting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public abstract class StixStrategy {
    private long ID = 0;
    private ArrayList<String> Logs;

    private ArrayList<IdentifiedStixObject> stixObjects = new ArrayList<>();

    public StixStrategy (ArrayList<String> Logs) {
        this.Logs = Logs;
    }

    abstract ArrayList<IdentifiedStixObject> processLog (String log);

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
        ArrayList<TypedStixObject> typedStixObjects = new ArrayList<>(stixObjects);
        bundle.setObjects(typedStixObjects);
        stixObjects.clear();
        return bundle;
    }
}
