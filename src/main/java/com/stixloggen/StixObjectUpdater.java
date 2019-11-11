package com.stixloggen;

import eu.csaware.stix2.sros.Sighting;

import java.util.ArrayList;
import java.util.List;

class StixObjectUpdater {
    static void updateSighting (Sighting originalSighting, Sighting newSighting) {
        newSighting.setId(originalSighting.getId());
        newSighting.setLastSeen(originalSighting.getLastSeen());

        Integer count = newSighting.getCount() + originalSighting.getCount();
        newSighting.setCount(count);

        ArrayList<String> sightedAt = (ArrayList<String>) newSighting.getWhereSightedRefs();

        for (String originalSight : originalSighting.getWhereSightedRefs()) {
            if (!doesExist(sightedAt, originalSight)) {
                sightedAt.add(originalSight);
            }
        }

        newSighting.setWhereSightedRefs(sightedAt);

        newSighting.setModified(originalSighting.getModified());
    }

    private static boolean doesExist (ArrayList<String> strings, String element) {
        for (String str : strings) {
            if (element.equals(str)) {
                return true;
            }
        }

        return false;
    }
}