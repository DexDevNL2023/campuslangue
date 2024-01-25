package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.PaiementController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportPaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.PaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.PaiementRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.PaiementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/finance/paiements")
@RestController
@CrossOrigin("*")
public class PaiementControllerImpl implements PaiementController {
  private final PaiementService service;

  public PaiementControllerImpl(PaiementService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PaiementDTO save(@Valid @RequestBody PaiementRequestDTO dto) {
    if (service.existByRefPaiement(dto.getRefPaiement()))
      throw new APIException("Le paiement avec la reference " + dto.getRefPaiement() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LitePaiementDTO> saveAll(@Valid @RequestBody List<ImportPaiementRequestDTO> dtos) {
    for (ImportPaiementRequestDTO dto : dtos) {
      if (service.existByRefPaiement(dto.getRefPaiement()))
        throw new APIException("Le paiement avec la reference " + dto.getRefPaiement() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public PaiementDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("Le paiement  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<LitePaiementDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LitePaiementDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public PaiementDTO update(@Valid @RequestBody PaiementRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("Le paiement avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("Le paiement avec les données suivante : " + dto.toString() + " existe déjà");
    return service.update(dto, id);
  }

  @Override
  @GetMapping("/campus/{campusId}")
  public List<LitePaiementDTO> listByCampus(@PathVariable("campusId") Long campusId) {
    return service.findAllByCampus(campusId);
  }
}