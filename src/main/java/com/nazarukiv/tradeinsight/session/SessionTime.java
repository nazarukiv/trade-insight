package com.nazarukiv.tradeinsight.session;

import java.time.ZonedDateTime;

public class SessionTime {

    private final String name;
    private final ZonedDateTime start;
    private final ZonedDateTime end;

    public SessionTime(String name, ZonedDateTime start, ZonedDateTime end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }
}