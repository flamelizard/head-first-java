package com.headfirst.battleships;

import java.util.ArrayList;

/**
 * Created by Tom on 3/2/2016.
 */
public class Ship {
    private ArrayList<String> location = new ArrayList<>();

    private String name;
    public Ship(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<String> loc) {
        for (String lo: loc) {
            location.add(lo.toLowerCase());
        }
    }

    public boolean isHit(String p1) {
        for (int i = 0; i < location.size(); i++) {
            if (location.contains(p1.toLowerCase())) {
                location.remove(p1.toLowerCase());
                return true;
            }
        }
        return false;
    }

    public boolean isDown() {
        return location.isEmpty();
    }
}

