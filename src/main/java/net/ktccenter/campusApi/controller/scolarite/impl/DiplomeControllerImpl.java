package net.ktccenter.campusApi.controller.scolarite.impl;

import net.ktccenter.campusApi.controller.scolarite.DiplomeController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportDiplomeRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.DiplomeDTO;
import net.ktccenter.campusApi.dto.request.scolarite.DiplomeRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.DiplomeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/scolarite/diplomes")
@RestController
@CrossOrigin("*")
public class DiplomeControllerImpl implements DiplomeController {
    private final DiplomeService service;

    public DiplomeControllerImpl(DiplomeService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiplomeDTO save(@Valid @RequestBody DiplomeRequestDTO dto) {
        if (service.existByCode(dto.getCode()))
            throw new APIException("Le diplôme avec le code " + dto.getCode() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteDiplomeDTO> saveAll(@Valid @RequestBody List<ImportDiplomeRequestDTO> dtos) {
        for (ImportDiplomeRequestDTO dto : dtos) {
            if (service.existByCode(dto.getCode()))
                throw new APIException("Le diplôme avec le code " + dto.getCode() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public DiplomeDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new APIException("Le diplôme  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteDiplomeDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteDiplomeDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody DiplomeRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new APIException("Le diplôme avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("Le diplôme avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
