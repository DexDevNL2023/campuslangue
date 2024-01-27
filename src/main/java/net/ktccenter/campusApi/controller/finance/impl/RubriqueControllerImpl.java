package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.RubriqueController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportRubriqueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.RubriqueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.RubriqueRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.RubriqueService;
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

	public RubriqueControllerImpl(RubriqueService service) {
		this.service = service;
	}

	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RubriqueDTO save(@Valid @RequestBody RubriqueRequestDTO dto) {
		if (service.existByCode(dto.getCode()))
			throw new APIException("Le rubrique avec le code " + dto.getCode() + " existe déjà");
		return service.save(dto);
	}

	@Override
	@PostMapping("/imports")
	@ResponseStatus(HttpStatus.CREATED)
	public List<LiteRubriqueDTO> saveAll(@Valid @RequestBody List<ImportRubriqueRequestDTO> dtos) {
		for (ImportRubriqueRequestDTO dto : dtos) {
			if (service.existByCode(dto.getCode()))
				throw new APIException("Le rubrique avec le code " + dto.getCode() + " existe déjà");
		}
		return service.save(dtos);
	}

	@Override
	@GetMapping("/{id}")
	public RubriqueDTO findById(@PathVariable("id") Long id) {
		return service.getOne(id);
	}

	@Override
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		if (service.findById(id) == null) throw new APIException("Le rubrique  avec l'id " + id + " n'existe pas");
		service.deleteById(id);
	}

	@Override
	@GetMapping
	public List<LiteRubriqueDTO> list() {
		return service.findAll();
	}

	@Override
	@GetMapping("/page-query")
	public Page<LiteRubriqueDTO> pageQuery(Pageable pageable) {
		return service.findAll(pageable);
	}

	@Override
	@PutMapping("/{id}")
	public void update(@Valid @RequestBody RubriqueRequestDTO dto, @PathVariable("id") Long id) {
		if (service.findById(id) == null) throw new APIException("Le rubrique avec l'id " + id + " n'existe pas");
		if (service.equalsByDto(dto, id))
			throw new APIException("Le rubrique avec les données suivante : " + dto.toString() + " existe déjà");
		service.update(dto, id);
	}
}