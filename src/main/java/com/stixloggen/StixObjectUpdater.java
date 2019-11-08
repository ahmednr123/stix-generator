package com.stixloggen;

import eu.csaware.stix2.sros.Sighting;

import java.util.List;

class StixObjectUpdater {

    static void updateSighting (Sighting originalSighting, Sighting newSighting) {
        originalSighting.setLastSeen(newSighting.getLastSeen());

        Integer count = originalSighting.getCount() + newSighting.getCount();
        originalSighting.setCount(count);

        List<String> sightedAt = originalSighting.getWhereSightedRefs();
        sightedAt.addAll(newSighting.getWhereSightedRefs());
        originalSighting.setWhereSightedRefs(sightedAt);

        originalSighting.setModified(newSighting.getModified());
    }

}
