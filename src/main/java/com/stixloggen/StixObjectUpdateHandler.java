package com.stixloggen;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.sros.Sighting;

import java.util.ArrayList;
import java.util.List;

class StixObjectUpdateHandler {

    static void
    execute (ArrayList<IdentifiedStixObject> objects)
    {
        for (IdentifiedStixObject object : objects) {
            IdentifiedStixObject stixObject = null;

            // Only update objects that need updating
            switch (object.getType()) {
                case SIGHTING:
                    Sighting sighting = (Sighting) object;

                    stixObject = StixObjectHandler.getInstance().get(sighting.getId());

                    Sighting originalSighting = (Sighting) stixObject;
                    originalSighting.setLastSeen(sighting.getLastSeen());

                    Integer count = originalSighting.getCount() + sighting.getCount();
                    originalSighting.setCount(count);

                    List<String> sightedAt = originalSighting.getWhereSightedRefs();
                    sightedAt.addAll(sighting.getWhereSightedRefs());
                    originalSighting.setWhereSightedRefs(sightedAt);

                    originalSighting.setModified(sighting.getModified());

                    break;
            }
        }
    }

}
