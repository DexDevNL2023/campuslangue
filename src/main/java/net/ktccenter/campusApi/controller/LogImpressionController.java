package net.ktccenter.campusApi.controller;

import net.ktccenter.campusApi.dto.reponse.scolarite.LogImpressionDTO;
import net.ktccenter.campusApi.service.LogImpressionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/log/impression")
@CrossOrigin("*")
public class LogImpressionController {
    private final LogImpressionService logImpressionService;

    public LogImpressionController(LogImpressionService logImpressionService) {
        this.logImpressionService = logImpressionService;
    }

    @GetMapping(path = "/for/{inscriptionId}")
    public List<LogImpressionDTO> list(@PathVariable("inscriptionId") Long inscriptionId) {
        return logImpressionService.findAll(inscriptionId);
    }
}

