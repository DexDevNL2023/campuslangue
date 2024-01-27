package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.ModePaiementController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportModePaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModePaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModePaiementRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.ModePaiementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/finance/modes-paiement")
@RestController
@CrossOrigin("*")
public class ModePaiementControllerImpl implements ModePaiementController {
  private final ModePaiementService service;

  public ModePaiementControllerImpl(ModePaiementService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ModePaiementDTO save(@Valid @RequestBody ModePaiementRequestDTO dto) {
    if (service.existByCode(dto.getCode()))
      throw new APIException("Le mode de paiement avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteModePaiementDTO> saveAll(@Valid @RequestBody List<ImportModePaiementRequestDTO> dtos) {
    for (ImportModePaiementRequestDTO dto : dtos) {
      if (service.existByCode(dto.getCode()))
        throw new APIException("Le mode de paiement avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public ModePaiementDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("Le mode de paiement  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<LiteModePaiementDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteModePaiementDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody ModePaiementRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("Le mode de paiement avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("Le mode de paiement avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}