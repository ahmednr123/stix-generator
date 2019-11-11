package com.stixloggen;

import eu.csaware.stix2.common.Bundle;
import eu.csaware.stix2.util.Stix2Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StixGenerator {
    private StixStrategy stixStrategy;

    public StixGenerator (ArrayList<String> Logs, String serverName) {
        StixObjectHandler.getInstance();
        this.stixStrategy = new MaliciousSightings(Logs, serverName);
    }

    public void saveBundleToFile (File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            Bundle bundle = stixStrategy.bundle();
            fileWriter.write(Stix2Gson.PRODUCTION.toJson(bundle));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}