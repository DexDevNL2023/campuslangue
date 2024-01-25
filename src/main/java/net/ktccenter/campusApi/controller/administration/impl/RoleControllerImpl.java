package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.RoleController;
import net.ktccenter.campusApi.dto.importation.administration.ImportRoleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleRequestDTO;
import net.ktccenter.campusApi.service.administration.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/administration/roles")
@RestController
@CrossOrigin("*")
public class RoleControllerImpl implements RoleController {
    private final RoleService service;

    public RoleControllerImpl(RoleService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDTO save(@Valid @RequestBody RoleRequestDTO dto) {
        if (service.existsByRoleName(dto.getLibelle()))
            throw new RuntimeException("Le role avec le nom " + dto.getLibelle() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteRoleDTO> saveAll(@Valid @RequestBody List<ImportRoleRequestDTO> dtos) {
        for (ImportRoleRequestDTO dto : dtos) {
            if (service.existsByRoleName(dto.getLibelle()))
                throw new RuntimeException("Le role avec le nom " + dto.getLibelle() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public RoleDTO findById(@PathVariable("id") Long id) {
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("Le role avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<LiteRoleDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteRoleDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public RoleDTO update(@Valid @RequestBody RoleRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("Le role avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("Le role avec les données suivante : " + dto.toString() + " existe déjà");
        return service.update(dto, id);
    }
}
