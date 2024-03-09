package net.ktccenter.campusApi.service;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.scolarite.LogImpressionRepository;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.LogImpressionDTO;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.ktccenter.campusApi.entities.scolarite.LogImpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class LogImpressionService {
    private final LogImpressionRepository repository;

    public LogImpressionService(LogImpressionRepository repository) {
        this.repository = repository;
    }

    public LogImpressionDTO save(Inscription inscription) {
        LogImpression logImpression = new LogImpression();
        logImpression.setInscription(inscription);
        return buildLogImpressionDto(repository.save(logImpression));
    }

    public List<LogImpressionDTO> findAll(Long inscriptionId) {
        return repository.findAllByInscriptionId(inscriptionId).stream().map(this::buildLogImpressionDto).collect(Collectors.toList());
    }

    private LogImpressionDTO buildLogImpressionDto(LogImpression logImpression) {
        LogImpressionDTO dto = new LogImpressionDTO(logImpression);
        dto.setInscription(new LiteInscriptionDTO(logImpression.getInscription()));
        return dto;
    }
}
