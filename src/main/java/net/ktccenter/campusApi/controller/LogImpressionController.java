package net.ktccenter.campusApi.controller;

import net.ktccenter.campusApi.dto.reponse.scolarite.LogImpressionDTO;
import net.ktccenter.campusApi.service.LogImpressionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
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
    public List<LogImpressionDTO> list(@NotNull Long inscriptionId) {
        return logImpressionService.findAll(inscriptionId);
    }
}

