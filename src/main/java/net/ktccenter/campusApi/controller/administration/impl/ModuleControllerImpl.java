package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.ModuleController;
import net.ktccenter.campusApi.dto.importation.administration.ImportModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.ModuleRequestDTO;
import net.ktccenter.campusApi.service.administration.ModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/administration/modules")
@RestController
@CrossOrigin("*")
public class ModuleControllerImpl implements ModuleController {
    private final ModuleService service;

    public ModuleControllerImpl(ModuleService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleDTO save(@Valid @RequestBody ModuleRequestDTO dto) {
        if (service.existsByName(dto.getName()))
            throw new RuntimeException("Le module avec le nom " + dto.getName() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteModuleDTO> saveAll(@Valid @RequestBody List<ImportModuleRequestDTO> dtos) {
        for (ImportModuleRequestDTO dto : dtos) {
            if (service.existsByName(dto.getName()))
                throw new RuntimeException("Le module avec le nom " + dto.getName() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public ModuleDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("Le module avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteModuleDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteModuleDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody ModuleRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("Le module avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("Le module avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
