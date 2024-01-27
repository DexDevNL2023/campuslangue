package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.CompteController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportCompteRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.CompteDTO;
import net.ktccenter.campusApi.dto.request.scolarite.CompteRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.CompteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/finance/comptes")
@RestController
@CrossOrigin("*")
public class CompteControllerImpl implements CompteController {
    private final CompteService service;

    public CompteControllerImpl(CompteService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompteDTO save(@Valid @RequestBody CompteRequestDTO dto) {
        if (service.existByCode(dto.getCode()))
            throw new APIException("Le compte avec le code " + dto.getCode() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteCompteDTO> saveAll(@Valid @RequestBody List<ImportCompteRequestDTO> dtos) {
        for (ImportCompteRequestDTO dto : dtos) {
            if (service.existByCode(dto.getCode()))
                throw new APIException("Le compte avec le code " + dto.getCode() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public CompteDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new APIException("Le compte  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteCompteDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteCompteDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody CompteRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new APIException("Le compte avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("Le compte avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}