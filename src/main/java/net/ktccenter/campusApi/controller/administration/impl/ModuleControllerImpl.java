package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.ModuleController;
import net.ktccenter.campusApi.dto.importation.administration.ImportModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.ModuleRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
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
    private final AutorisationService autorisationService;

    public ModuleControllerImpl(ModuleService service, AutorisationService autorisationService) {
        this.service = service;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@AuthorizeUser(actionKey = "module-add")
    public ModuleDTO save(@Valid @RequestBody ModuleRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Ajouter un module", "module-add", "POST", false));
        if (service.existsByName(dto.getName()))
            throw new RuntimeException("Le module avec le nom " + dto.getName() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    //@AuthorizeUser(actionKey = "module-import")
    public List<LiteModuleDTO> saveAll(@Valid @RequestBody List<ImportModuleRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Importer des modules", "module-import", "POST", false));
        for (ImportModuleRequestDTO dto : dtos) {
            if (service.existsByName(dto.getName()))
                throw new RuntimeException("Le module avec le nom " + dto.getName() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    //@AuthorizeUser(actionKey = "module-details")
    public ModuleDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Détails d'un module", "module-details", "GET", false));
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    //@AuthorizeUser(actionKey = "module-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Supprimer un module", "module-delet", "DELET", false));
        if (service.findById(id) == null) throw new RuntimeException("Le module avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    //@AuthorizeUser(actionKey = "module-list")
    public List<LiteModuleDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Lister les modules", "module-list", "GET", false));
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteModuleDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    //@AuthorizeUser(actionKey = "module-edit")
    public void update(@Valid @RequestBody ModuleRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Modifier un module", "module-edit", "PUT", false));
        if (service.findById(id) == null) throw new RuntimeException("Le module avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("Le module avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
