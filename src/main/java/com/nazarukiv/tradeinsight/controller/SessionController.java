package com.nazarukiv.tradeinsight.controller;

import com.nazarukiv.tradeinsight.session.SessionService;
import com.nazarukiv.tradeinsight.session.SessionTime;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService = new SessionService();

    @GetMapping
    public List<SessionTime> getSessions(@RequestParam(defaultValue = "London") String tz) {
        String zone = sessionService.mapToZoneId(tz);
        return sessionService.getSessions(zone);
    }
}