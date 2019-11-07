package com.stixloggen;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.common.Stix2Type;
import eu.csaware.stix2.sdos.Identity;
import eu.csaware.stix2.sdos.Indicator;
import eu.csaware.stix2.sros.Sighting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class StixObjectIDHandler {

    static void
    execute (ArrayList<IdentifiedStixObject> objects)
    {
        IDHandler idHandler = new IDHandler();

        for (IdentifiedStixObject object : objects) {
            String ID = generateID(object.getType());
            switch (object.getType()) {
                case IDENTITY:
                    Identity identity =
                            (Identity)
                                StixObjectHandler.getInstance()
                                    .find(Stix2Type.IDENTITY, ((Identity)object).getName());
                    if (identity != null) {
                        System.out.println("FOUND PREV IDENTITY");
                        ID = identity.getId();
                    } else {
                        StixObjectHandler.getInstance().set(ID, object);
                    }
                    break;

                case INDICATOR:
                    Indicator indicator =
                            (Indicator)
                                StixObjectHandler.getInstance()
                                    .find(Stix2Type.INDICATOR, ((Indicator)object).getPattern());
                    if (indicator != null) {
                        System.out.println("FOUND PREV INDICATOR");
                        ID = indicator.getId();
                    } else {
                        StixObjectHandler.getInstance().set(ID, object);
                    }
                    break;
                case SIGHTING:
                    Sighting sighting = (Sighting) object;
                    String tempSightingRef = sighting.getSightingOfRef();
                    String sightingOfRef = idHandler.get(tempSightingRef);
                    if (sightingOfRef == null) {
                        idHandler.attach(tempSightingRef, sighting::setSightingOfRef);
                    } else {
                        sighting.setSightingOfRef(sightingOfRef);
                    }

                    List<String> sightingAtRefs = sighting.getWhereSightedRefs();
                    String tempSightingAtRef = sightingAtRefs.get(0);
                    String sightingAtRef = idHandler.get(tempSightingAtRef);
                    if (sightingAtRef == null) {
                        idHandler.attach(tempSightingAtRef, (newID) -> {
                            sightingAtRefs.set(0, newID);
                        });
                    } else {
                        sightingAtRefs.set(0, sightingAtRef);
                    }

                    Sighting originalSighting =
                            (Sighting)
                                StixObjectHandler.getInstance()
                                    .find(Stix2Type.SIGHTING, sighting.getSightingOfRef());

                    if (originalSighting != null) {
                        System.out.println("FOUND PREV SIGHTING");
                        ID = originalSighting.getId();
                    } else {
                        StixObjectHandler.getInstance().set(ID, object);
                    }
                    break;
            }

            idHandler.set(object.getId(), ID);
            object.setId(ID);
        }

        idHandler.clear();
    }

    private static String generateID (Stix2Type objType) {
        String type = objType.toString();
        return (type + "--" + UUID.randomUUID().toString());
    }
}
