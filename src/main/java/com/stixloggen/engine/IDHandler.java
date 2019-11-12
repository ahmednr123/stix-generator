package com.stixloggen.engine;

import java.util.ArrayList;
import java.util.HashMap;

interface CallbackTask {
    void execute (String newValue);
}

public class IDHandler {
    private HashMap<String, String> idChanges;
    private HashMap<String, ArrayList<CallbackTask>> actions;

    public IDHandler () {
        idChanges = new HashMap<>();
        actions = new HashMap<>();
    }

    public String get (String key) {
        if (idChanges.containsKey(key)) {
            return idChanges.get(key);
        }
        return null;
    }

    public void set (String id, String newId) {
        idChanges.put(id, newId);
        executeTasksOf(id, newId);
    }

    public void attach (String id, CallbackTask task) {
        System.out.println();
        if (actions.containsKey(id)) {
            actions.get(id).add(task);
            return;
        }

        ArrayList<CallbackTask> tasks = new ArrayList<>();
        tasks.add(task);
        actions.put(id, tasks);
    }

    public void clear () {
        idChanges.clear();
    }

    public void executeTasksOf (String id, String newId) {
        if (actions.size() == 0) return;

        ArrayList<CallbackTask> tasks = actions.get(id);
        if (tasks != null)
        for (CallbackTask task : tasks) {
            task.execute(newId);
        }
    }
}