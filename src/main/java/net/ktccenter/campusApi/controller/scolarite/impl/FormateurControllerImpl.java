package net.ktccenter.campusApi.controller.scolarite.impl;

import net.ktccenter.campusApi.controller.scolarite.FormateurController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.branch.FormateurBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.FormateurService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/scolarite/formateurs")
@RestController
@CrossOrigin("*")
public class FormateurControllerImpl implements FormateurController {
  private final FormateurService service;

  public FormateurControllerImpl(FormateurService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FormateurDTO save(@Valid @RequestBody FormateurRequestDTO dto) {
    if(!MyUtils.isValidEmailAddress(dto.getEmail()))
      throw new APIException("L'email " + dto.getEmail() + " est invalide.");
    if (service.existByEmail(dto.getEmail()))
      throw new APIException("Le formateur avec l'email " + dto.getEmail() + " existe déjà");
    if (service.existByTelephone(dto.getEmail()))
      throw new APIException("Le formateur avec le téléphone " + dto.getTelephone() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteFormateurDTO> saveAll(@Valid @RequestBody List<ImportFormateurRequestDTO> dtos) {
    for (ImportFormateurRequestDTO dto : dtos) {
      if(!MyUtils.isValidEmailAddress(dto.getEmail()))
        throw new APIException("L'email " + dto.getEmail() + " est invalide.");
      if (service.existByEmail(dto.getEmail()))
        throw new APIException("Le formateur avec l'email " + dto.getEmail() + " existe déjà");
      if (service.existByTelephone(dto.getEmail()))
        throw new APIException("Le formateur avec le téléphone " + dto.getTelephone() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public FormateurDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("Le formateur avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<FormateurBranchDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteFormateurDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody FormateurRequestDTO dto, @PathVariable("id") Long id) {
    if(!MyUtils.isValidEmailAddress(dto.getEmail()))
      throw new APIException("L'email " + dto.getEmail() + " est invalide.");
    if (service.findById(id) == null) throw new APIException("Le formateur avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("Le formateur avec les données suivante : " + dto + " existe déjà");
    service.update(dto, id);
  }
}