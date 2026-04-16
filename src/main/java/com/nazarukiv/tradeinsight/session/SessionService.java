package com.nazarukiv.tradeinsight.session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionService {

    public List<SessionTime> getSessions(String userZone) {
        ZoneId userZoneId = ZoneId.of(userZone);
        ZoneId londonZone = ZoneId.of("Europe/London");

        ZonedDateTime nowUser = ZonedDateTime.now(userZoneId);
        ZonedDateTime nowLondon = nowUser.withZoneSameInstant(londonZone);
        LocalDate londonDate = nowLondon.toLocalDate();

        List<SessionTime> sessions = new ArrayList<>();

        sessions.add(createSession("Asia", 0, 0, 7, 0, londonDate, londonZone, userZoneId));
        sessions.add(createSession("Frankfurt", 7, 0, 8, 0, londonDate, londonZone, userZoneId));
        sessions.add(createSession("London", 8, 0, 16, 0, londonDate, londonZone, userZoneId));
        sessions.add(createSession("New York", 13, 0, 21, 0, londonDate, londonZone, userZoneId));

        return sessions;
    }

    private SessionTime createSession(String name,
                                      int startHour, int startMinute,
                                      int endHour, int endMinute,
                                      LocalDate date,
                                      ZoneId londonZone,
                                      ZoneId userZone) {

        ZonedDateTime startLondon = ZonedDateTime.of(date, LocalTime.of(startHour, startMinute), londonZone);
        ZonedDateTime endLondon = ZonedDateTime.of(date, LocalTime.of(endHour, endMinute), londonZone);

        ZonedDateTime startUser = startLondon.withZoneSameInstant(userZone);
        ZonedDateTime endUser = endLondon.withZoneSameInstant(userZone);

        return new SessionTime(name, startUser, endUser);
    }

    public String mapToZoneId(String input) {
        String value = input.trim().toLowerCase();

        switch (value) {
            case "london":
                return "Europe/London";
            case "kyiv":
                return "Europe/Kyiv";
            case "new york":
            case "newyork":
            case "ny":
                return "America/New_York";
            default:
                return input;
        }
    }
}