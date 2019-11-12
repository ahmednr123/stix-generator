package com.stixloggen.strategy;

import eu.csaware.stix2.common.IdentifiedStixObject;
import eu.csaware.stix2.sdos.Identity;

import java.time.LocalDateTime;
import java.util.ArrayList;

class LoginAttempt {
    String ipAddress;
    String username;
    Action action;

    enum Action {
        SUCCESS, FAILURE
    }

    LoginAttempt (String ipAddress, String username, Action action) {
        this.ipAddress = ipAddress;
        this.username = username;
        this.action = action;
    }
}

public class BruteforceAttempts extends StixStrategy {

    ArrayList<LoginAttempt> loginAttempts = new ArrayList<>();

    public BruteforceAttempts(ArrayList<String> Logs) {
        super(Logs);
    }

    @Override
    protected Identity extractIdentity(String log) {
        return null;
    }

    @Override
    LocalDateTime extractDateTime(String log) {
        return null;
    }

    @Override
    ArrayList<IdentifiedStixObject> processLog (String log) {
        ArrayList<IdentifiedStixObject> stixObjects = new ArrayList<>();



        loginAttempts.add(new LoginAttempt("","", LoginAttempt.Action.SUCCESS));
        return stixObjects;
    }
}
