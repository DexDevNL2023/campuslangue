package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.RubriqueController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportRubriqueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.RubriqueDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.RubriqueRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.RubriqueService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/finance/rubriques")
@RestController
@CrossOrigin("*")
public class RubriqueControllerImpl implements RubriqueController {
	private final RubriqueService service;
	private final AutorisationService autorisationService;

	public RubriqueControllerImpl(RubriqueService service, AutorisationService autorisationService) {
		this.service = service;
		this.autorisationService = autorisationService;
	}

	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@AuthorizeUser(actionKey = "rubrique-add")
	public RubriqueDTO save(@Valid @RequestBody RubriqueRequestDTO dto) {
		autorisationService.addDroit(new SaveDroitDTO("Finances", "Ajouter une rubrique", "rubrique-add", "POST", false));
		if (service.existByCode(dto.getCode()))
			throw new APIException("Le rubrique avec le code " + dto.getCode() + " existe déjà");
		return service.save(dto);
	}

	@Override
	@PostMapping("/imports")
	@ResponseStatus(HttpStatus.CREATED)
	@AuthorizeUser(actionKey = "rubrique-import")
	public List<LiteRubriqueDTO> saveAll(@Valid @RequestBody List<ImportRubriqueRequestDTO> dtos) {
		autorisationService.addDroit(new SaveDroitDTO("Finances", "Importer des rubriques", "rubrique-import", "POST", false));
		for (ImportRubriqueRequestDTO dto : dtos) {
			if (service.existByCode(dto.getCode()))
				throw new APIException("Le rubrique avec le code " + dto.getCode() + " existe déjà");
		}
		return service.save(dtos);
	}

	@Override
	@GetMapping("/{id}")
	@AuthorizeUser(actionKey = "rubrique-details")
	public RubriqueDTO findById(@PathVariable("id") Long id) {
		autorisationService.addDroit(new SaveDroitDTO("Finances", "Détails d'une rubrique", "rubrique-details", "GET", false));
		return service.getOne(id);
	}

	@Override
	@DeleteMapping("/{id}")
	@AuthorizeUser(actionKey = "rubrique-delet")
	public void delete(@PathVariable("id") Long id) {
		autorisationService.addDroit(new SaveDroitDTO("Finances", "Supprimer une rubrique", "rubrique-delet", "DELET", false));
		if (service.findById(id) == null) throw new APIException("Le rubrique  avec l'id " + id + " n'existe pas");
		service.deleteById(id);
	}

	@Override
	@GetMapping
	@AuthorizeUser(actionKey = "rubrique-list")
	public List<LiteRubriqueDTO> list() {
		autorisationService.addDroit(new SaveDroitDTO("Finances", "Lister les rubriques", "rubrique-list", "GET", false));
		return service.findAll();
	}

	@Override
	@GetMapping("/page-query")
	public Page<LiteRubriqueDTO> pageQuery(Pageable pageable) {
		return service.findAll(pageable);
	}

	@Override
	@PutMapping("/{id}")
	@AuthorizeUser(actionKey = "rubrique-edit")
	public void update(@Valid @RequestBody RubriqueRequestDTO dto, @PathVariable("id") Long id) {
		autorisationService.addDroit(new SaveDroitDTO("Finances", "Modifier une rubrique", "rubrique-edit", "PUT", false));
		if (service.findById(id) == null) throw new APIException("Le rubrique avec l'id " + id + " n'existe pas");
		if (service.equalsByDto(dto, id))
			throw new APIException("Le rubrique avec les données suivante : " + dto.toString() + " existe déjà");
		service.update(dto, id);
	}
}