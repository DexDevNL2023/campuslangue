package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.DroitController;
import net.ktccenter.campusApi.dto.importation.administration.ImportDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.DroitRequestDTO;
import net.ktccenter.campusApi.service.administration.DroitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/administration/droits")
@RestController
@CrossOrigin("*")
public class DroitControllerImpl implements DroitController {
    private final DroitService service;

    public DroitControllerImpl(DroitService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DroitDTO save(@Valid @RequestBody DroitRequestDTO dto) {
        if (service.existsByVerbeAndKeyAndLibelle(dto.getVerbe(), dto.getKey(), dto.getLibelle()))
            throw new RuntimeException("Le droit avec le verbe " + dto.getVerbe() + ", la key " + dto.getKey() + ", le libelle " + dto.getLibelle() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteDroitDTO> saveAll(@Valid @RequestBody List<ImportDroitRequestDTO> dtos) {
        for (ImportDroitRequestDTO dto : dtos) {
            if (service.existsByVerbeAndKeyAndLibelle(dto.getVerbe(), dto.getKey(), dto.getLibelle()))
                throw new RuntimeException("Le droit avec le verbe " + dto.getVerbe() + ", la key " + dto.getKey() + ", le libelle " + dto.getLibelle() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public DroitDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("Le droit avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteDroitDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteDroitDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody DroitRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("Le droit avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("Le droit avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
