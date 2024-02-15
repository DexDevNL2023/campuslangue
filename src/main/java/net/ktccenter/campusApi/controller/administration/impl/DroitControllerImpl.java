package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.DroitController;
import net.ktccenter.campusApi.dto.importation.administration.ImportDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.DroitRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
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
    private final AutorisationService autorisationService;

    public DroitControllerImpl(DroitService service, AutorisationService autorisationService) {
        this.service = service;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@AuthorizeUser(actionKey = "droit-add")
    public DroitDTO save(@Valid @RequestBody DroitRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Ajouter un droit", "droit-add", "POST", false));
        if (service.existsByVerbeAndKeyAndLibelle(dto.getVerbe(), dto.getKey(), dto.getLibelle()))
            throw new RuntimeException("Le droit avec le verbe " + dto.getVerbe() + ", la key " + dto.getKey() + ", le libelle " + dto.getLibelle() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    //@AuthorizeUser(actionKey = "droit-import")
    public List<LiteDroitDTO> saveAll(@Valid @RequestBody List<ImportDroitRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Importer des droits", "droit-import", "POST", false));
        for (ImportDroitRequestDTO dto : dtos) {
            if (service.existsByVerbeAndKeyAndLibelle(dto.getVerbe(), dto.getKey(), dto.getLibelle()))
                throw new RuntimeException("Le droit avec le verbe " + dto.getVerbe() + ", la key " + dto.getKey() + ", le libelle " + dto.getLibelle() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    //@AuthorizeUser(actionKey = "droit-details")
    public DroitDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Détails d'un droit", "droit-details", "GET", false));
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    //@AuthorizeUser(actionKey = "droit-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Supprimer un droit", "droit-delet", "DELET", false));
        if (service.findById(id) == null) throw new RuntimeException("Le droit avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    //@AuthorizeUser(actionKey = "droit-list")
    public List<LiteDroitDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Lister les droits", "droit-list", "GET", false));
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteDroitDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    //@AuthorizeUser(actionKey = "droit-edit")
    public void update(@Valid @RequestBody DroitRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Modifier un droit", "droit-edit", "PUT", false));
        if (service.findById(id) == null) throw new RuntimeException("Le droit avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("Le droit avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
