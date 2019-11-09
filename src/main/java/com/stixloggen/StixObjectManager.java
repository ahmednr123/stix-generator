package com.stixloggen;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.sdos.Identity;
import eu.csaware.stix2.sdos.Indicator;
import eu.csaware.stix2.sros.Sighting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class StixObjectManager {

    static void
    execute (ArrayList<IdentifiedStixObject> objects)
    {
        IDHandler idHandler = new IDHandler();

        updateObjectIDs(objects, idHandler);

        StixObjectHandler.getInstance().pushAll(objects);
        idHandler.clear();
    }

    /**
     * This method gets IDs of objects that may already be present
     * in the StixObjectPool. If incase the object is not present. It
     * generates new IDs for the objects.
     *
     * The IDHandler keeps track of which temporary ID was set to what
     * new ID. Which will be used while updating references within
     * the objects.
     *
     * @param objects
     * @param idHandler
     */
    private static void
    updateObjectIDs (ArrayList<IdentifiedStixObject> objects, IDHandler idHandler)
    {
        for (IdentifiedStixObject object : objects) {
            String ID = getObjectID(object, generateID(object.getType()));
            idHandler.set(object.getId(), ID);
            object.setId(ID);
        }

        updateObjectReferences(objects, idHandler);
    }

    private static void
    updateObjectReferences (ArrayList<IdentifiedStixObject> objects, IDHandler idHandler)
    {
        for (IdentifiedStixObject object : objects) {
            if (object.getType() == Stix2Type.SIGHTING) {
                Sighting sighting = (Sighting) object;
                String tempSightingRef = sighting.getSightingOfRef();
                String sightingOfRef = idHandler.get(tempSightingRef);
                sighting.setSightingOfRef(sightingOfRef);

                List<String> sightingAtRefs = sighting.getWhereSightedRefs();
                String tempSightingAtRef = sightingAtRefs.get(0);
                String sightingAtRef = idHandler.get(tempSightingAtRef);
                sightingAtRefs.set(0, sightingAtRef);

                Sighting originalSighting = (Sighting) StixObjectHandler.getInstance().find(Stix2Type.SIGHTING, sighting.getSightingOfRef());
                if (originalSighting != null) {
                    StixObjectUpdater.updateSighting(originalSighting, sighting);
                }
            }
        }
    }

    private static
    String getObjectID (IdentifiedStixObject object, String generatedID)
    {
        String data;
        switch (object.getType()) {
            case IDENTITY:
                data = ((Identity)object).getName();
                break;
            case INDICATOR:
                data = ((Indicator)object).getPattern();
                break;
            case SIGHTING:
                data = ((Sighting)object).getSightingOfRef();
                break;
            default:
                throw new RuntimeException("The object type: " + object.getType() + " is not supported yet");
        }

        IdentifiedStixObject originalObject =
                        StixObjectHandler.getInstance().find(object.getType(), data);

        if (originalObject != null) {
            generatedID = originalObject.getId();
        }

        return generatedID;
    }

    private static
    String generateID (Stix2Type objType)
    {
        String type = objType.toString();
        return (type + "--" + UUID.randomUUID().toString());
    }
}