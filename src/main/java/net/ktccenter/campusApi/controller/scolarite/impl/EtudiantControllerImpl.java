package net.ktccenter.campusApi.controller.scolarite.impl;

import net.ktccenter.campusApi.controller.scolarite.EtudiantController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportEtudiantRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.service.scolarite.EtudiantService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/scolarite/etudiants")
@RestController
@CrossOrigin("*")
public class EtudiantControllerImpl implements EtudiantController {
    private final EtudiantService service;

    public EtudiantControllerImpl(EtudiantService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EtudiantDTO save(@Valid @RequestBody EtudiantRequestDTO dto) {
        if(!MyUtils.isValidEmailAddress(dto.getEmail()))
            throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
        if (service.existByEmail(dto.getEmail()))
            throw new ResourceNotFoundException("L'apprenant avec l'email " + dto.getEmail() + " existe déjà");
        if (service.existByTelephone(dto.getTelephone()))
            throw new ResourceNotFoundException("L'apprenant avec le téléphone " + dto.getTelephone() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteEtudiantDTO> saveAll(@Valid @RequestBody List<ImportEtudiantRequestDTO> dtos) {
        for (ImportEtudiantRequestDTO dto : dtos) {
            if(!MyUtils.isValidEmailAddress(dto.getEmail()))
                throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
            if (service.existByEmail(dto.getEmail()))
                throw new ResourceNotFoundException("L'apprenant avec l'email " + dto.getEmail() + " existe déjà");
            if (service.existByTelephone(dto.getTelephone()))
                throw new ResourceNotFoundException("L'apprenant avec le téléphone " + dto.getTelephone() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public EtudiantDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new APIException("L'apprenant  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteEtudiantDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/by/session/{sessionId}/{salleId}/{niveauId}")
    public List<LiteEtudiantDTO> getAllBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("salleId") Long salleId, @PathVariable("niveauId") Long niveauId) {
        return service.getAllBySession(sessionId, salleId, niveauId);
    }

    @Override
    @GetMapping("/with/unpaid")
    public List<LiteEtudiantDTO> getAllWithUnpaid() {
        return service.getAllWithUnpaid();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteEtudiantDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody EtudiantRequestDTO dto, @PathVariable("id") Long id) {
        if(!MyUtils.isValidEmailAddress(dto.getEmail()))
            throw new APIException("L'email " + dto.getEmail() + " est invalide.");
        if (service.findById(id) == null) throw new APIException("L'apprenant avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("L'apprenant avec les données suivante : " + dto + " existe déjà");
        service.update(dto, id);
    }
}
