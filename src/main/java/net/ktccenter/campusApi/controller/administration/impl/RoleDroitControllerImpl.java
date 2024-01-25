package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.RoleDroitController;
import net.ktccenter.campusApi.dto.importation.administration.ImportRoleDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDroitDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleDroitRequestDTO;
import net.ktccenter.campusApi.service.administration.RoleDroitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/administration/role-droits")
@RestController
@CrossOrigin("*")
public class RoleDroitControllerImpl implements RoleDroitController {
    private final RoleDroitService service;

    public RoleDroitControllerImpl(RoleDroitService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDroitDTO save(@Valid @RequestBody RoleDroitRequestDTO dto) {
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteRoleDroitDTO> saveAll(@Valid @RequestBody List<ImportRoleDroitRequestDTO> dtos) {
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public RoleDroitDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("La permission avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteRoleDroitDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteRoleDroitDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public RoleDroitDTO update(@Valid @RequestBody RoleDroitRequestDTO roleDroitDTO, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("La permission avec l'id " + id + " n'existe pas");
        return service.update(roleDroitDTO, id);
    }
}
