package net.ktccenter.campusApi.controller.administration.impl;

import net.ktccenter.campusApi.controller.administration.RoleDroitController;
import net.ktccenter.campusApi.dto.importation.administration.ImportRoleDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.dto.reponse.PermissionModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDroitDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleDroitRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.RoleDroitService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/administration/permission-droits")
@RestController
@CrossOrigin("*")
public class RoleDroitControllerImpl implements RoleDroitController {
    private final RoleDroitService service;
    private final AutorisationService autorisationService;

    public RoleDroitControllerImpl(RoleDroitService service, AutorisationService autorisationService) {
        this.service = service;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "permission-add")
    public RoleDroitDTO save(@Valid @RequestBody RoleDroitRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Ajouter une permission", "permission-add", "POST", false));
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "permission-import")
    public List<LiteRoleDroitDTO> saveAll(@Valid @RequestBody List<ImportRoleDroitRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Importer des permissions", "permission-import", "POST", false));
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    @AuthorizeUser(actionKey = "permission-details")
    public RoleDroitDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Détails d'une permission", "permission-details", "GET", false));
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @AuthorizeUser(actionKey = "permission-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Supprimer une permission", "permission-delet", "DELET", false));
        if (service.findById(id) == null) throw new RuntimeException("La permission avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    @AuthorizeUser(actionKey = "permission-list")
    public List<PermissionModuleDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Lister les permissions", "permission-list", "GET", false));
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteRoleDroitDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    @AuthorizeUser(actionKey = "permission-edit")
    public void update(@Valid @RequestBody RoleDroitRequestDTO permissionDroitDTO, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Modifier une permission", "permission-edit", "PUT", false));
        if (service.findById(id) == null) throw new RuntimeException("La permission avec l'id " + id + " n'existe pas");
        service.update(permissionDroitDTO, id);
    }
}
