package com.nazarukiv.tradeinsight.controller;

import com.nazarukiv.tradeinsight.calculator.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculate")
public class CalculatorController {

    private final PositionSizeCalculator calculator = new PositionSizeCalculator();

    @PostMapping
    public TradeResult calculate(@Valid @RequestBody TradeRequest request) {
        return calculator.calculate(request);
    }
}