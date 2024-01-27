package net.ktccenter.campusApi.controller.scolarite.impl;

import net.ktccenter.campusApi.controller.scolarite.NiveauController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportNiveauRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.NiveauDTO;
import net.ktccenter.campusApi.dto.request.scolarite.NiveauRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.NiveauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/scolarite/niveaux")
@RestController
@CrossOrigin("*")
public class NiveauControllerImpl implements NiveauController {
    private final NiveauService service;

    public NiveauControllerImpl(NiveauService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NiveauDTO save(@Valid @RequestBody NiveauRequestDTO dto) {
        if (service.existByCode(dto.getCode()))
            throw new APIException("Le niveau avec le code " + dto.getCode() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteNiveauDTO> saveAll(@Valid @RequestBody List<ImportNiveauRequestDTO> dtos) {
        for (ImportNiveauRequestDTO dto : dtos) {
            if (service.existByCode(dto.getCode()))
                throw new APIException("Le niveau avec le code " + dto.getCode() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public NiveauDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new APIException("Le niveau  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteNiveauDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteNiveauDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody NiveauRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new APIException("Le niveau avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("Le niveau avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
