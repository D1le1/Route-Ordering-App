package com.example.busik.other;

public class Stop {
    private String name;
    private int time;

    public Stop(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
