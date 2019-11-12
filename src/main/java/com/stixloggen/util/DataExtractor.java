package com.stixloggen.util;

import java.util.ArrayList;
import java.util.HashMap;

public class DataExtractor {
    String str;

    HashMap<type, ArrayList<String>> data = new HashMap<>();

    enum type {
        IP_ADDRESS, DOMAIN_NAME, URL
    }

    DataExtractor(String str) {
        this.str = str;
    }

    private void extract () {

    }
}
