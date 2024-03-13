package net.ktccenter.campusApi.controller.scolarite.impl;

import net.ktccenter.campusApi.controller.scolarite.EtudiantController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportEtudiantRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.EtudiantService;
import net.ktccenter.campusApi.utils.MyUtils;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/scolarite/etudiants")
@RestController
@CrossOrigin("*")
public class EtudiantControllerImpl implements EtudiantController {
    private final EtudiantService service;
    private final MainService mainService;
    private final AutorisationService autorisationService;

    public EtudiantControllerImpl(EtudiantService service, MainService mainService, AutorisationService autorisationService) {
        this.service = service;
        this.mainService = mainService;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "apprenant-add")
    public EtudiantDTO save(@Valid @RequestBody EtudiantRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Ajouter un apprenant", "apprenant-add", "POST", false));
        if(!MyUtils.isValidEmailAddress(dto.getEmail()))
            throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
        if (service.existByEmail(dto.getEmail()))
            throw new ResourceNotFoundException("L'apprenant avec l'email " + dto.getEmail() + " existe déjà");
        if (service.existByTelephone(dto.getTelephone()))
            throw new ResourceNotFoundException("L'apprenant avec le téléphone " + dto.getTelephone() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "apprenant-import")
    public List<LiteEtudiantDTO> saveAll(@Valid @RequestBody List<ImportEtudiantRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Importer des apprenants", "apprenant-import", "POST", false));
        for (ImportEtudiantRequestDTO dto : dtos) {
            if(!MyUtils.isValidEmailAddress(dto.getEmail()))
                throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
            if (service.existByEmail(dto.getEmail()))
                throw new ResourceNotFoundException("L'apprenant avec l'email " + dto.getEmail() + " existe déjà");
            if (service.existByTelephone(dto.getTelephone()))
                throw new ResourceNotFoundException("L'apprenant avec le téléphone " + dto.getTelephone() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    @AuthorizeUser(actionKey = "apprenant-details")
    public EtudiantDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Détails d'un apprenant", "apprenant-details", "GET", false));
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @AuthorizeUser(actionKey = "apprenant-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Supprimer un apprenant", "apprenant-delet", "DELET", false));
        if (service.findById(id) == null) throw new APIException("L'apprenant  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    @AuthorizeUser(actionKey = "apprenant-list")
    public List<EtudiantBranchDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Lister les apprenants", "apprenant-list", "GET", false));
        List<EtudiantBranchDTO> result = service.findAll();
        if (result.isEmpty()) return getEmptyList();
        return result;
    }

    @Override
    @GetMapping("/by/session/{sessionId}/{salleId}/{niveauId}")
    @AuthorizeUser(actionKey = "apprenant-list")
    public List<EtudiantBranchDTO> getAllBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("salleId") Long salleId, @PathVariable("niveauId") Long niveauId) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Lister les apprenants", "apprenant-list", "GET", false));
        List<EtudiantBranchDTO> result = service.getAllBySession(sessionId, salleId, niveauId);
        if (result.isEmpty()) return getEmptyList();
        return result;
    }

    @Override
    @GetMapping("/is/rattrapage")
    @AuthorizeUser(actionKey = "apprenant-israttrapage")
    public List<EtudiantBranchDTO> getAllIsRattapage() {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Lister les apprenants en rattrapage", "apprenant-israttrapage", "GET", false));
        List<EtudiantBranchDTO> result = service.getAllIsRattapage();
        if (result.isEmpty()) return getEmptyList();
        return result;
    }

    private List<EtudiantBranchDTO> getEmptyList() {
        List<EtudiantBranchDTO> result = new ArrayList<>();
        EtudiantBranchDTO dto = new EtudiantBranchDTO();
        dto.setBranche(new LiteBrancheDTO(mainService.getCurrentUserBranch()));
        dto.setData(new ArrayList<>());
        result.add(dto);
        return result;
    }

    @Override
    @GetMapping("/with/unpaid")
    @AuthorizeUser(actionKey = "apprenant-unpaid")
    public List<EtudiantBranchDTO> getAllWithUnpaid() {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Lister les apprenants avec des impayés", "apprenant-unpaid", "GET", false));
        return service.getAllWithUnpaid();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteEtudiantDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    @AuthorizeUser(actionKey = "apprenant-edit")
    public void update(@Valid @RequestBody EtudiantRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Modifier un apprenant", "apprenant-edit", "PUT", false));
        if(!MyUtils.isValidEmailAddress(dto.getEmail()))
            throw new APIException("L'email " + dto.getEmail() + " est invalide.");
        if (service.findById(id) == null) throw new APIException("L'apprenant avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("L'apprenant avec les données suivante : " + dto + " existe déjà");
        service.update(dto, id);
    }
}
