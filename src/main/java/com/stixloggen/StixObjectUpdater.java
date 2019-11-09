package com.stixloggen;

import eu.csaware.stix2.sros.Sighting;

import java.util.List;

class StixObjectUpdater {
    static void updateSighting (Sighting originalSighting, Sighting newSighting) {
        newSighting.setId(originalSighting.getId());
        newSighting.setLastSeen(originalSighting.getLastSeen());

        Integer count = newSighting.getCount() + originalSighting.getCount();
        newSighting.setCount(count);

        List<String> sightedAt = newSighting.getWhereSightedRefs();
        sightedAt.addAll(originalSighting.getWhereSightedRefs());
        newSighting.setWhereSightedRefs(sightedAt);

        newSighting.setModified(originalSighting.getModified());
    }
}