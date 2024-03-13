package net.ktccenter.campusApi.controller.scolarite.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.controller.scolarite.InscriptionController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportInscriptionRequestDTO;
import net.ktccenter.campusApi.dto.lite.LiteNewInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.InscriptionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.InscriptionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscriptionRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireExitStudientRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireNewStudientRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.InscriptionService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequestMapping("/api/scolarite/inscriptions")
@Slf4j
@RestController
@CrossOrigin("*")
public class InscriptionControllerImpl implements InscriptionController {
  private final InscriptionService service;
  private final AutorisationService autorisationService;

  public InscriptionControllerImpl(InscriptionService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping("/exist/studient")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "inscription-add")
  public LiteNewInscriptionDTO inscrireExitStudient(@Valid @RequestBody InscrireExitStudientRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Ajouter une inscription", "inscription-add", "POST", false));
    return service.inscrireExitStudient(dto);
  }

  @Override
  @PostMapping("/new/studient")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "inscription-add")
  public LiteNewInscriptionDTO inscrireNewStudient(@Valid @RequestBody InscrireNewStudientRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Ajouter une inscription", "inscription-add", "POST", false));
    return service.inscrireNewStudient(dto);
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "inscription-add")
  public InscriptionDTO save(@Valid @RequestBody InscriptionRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Ajouter une inscription", "inscription-add", "POST", false));
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "inscription-import")
  public List<LiteInscriptionDTO> saveAll(@Valid @RequestBody List<ImportInscriptionRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Importer des inscriptions", "inscription-import", "POST", false));
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "inscription-details")
  public InscriptionDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Détails d'une inscription", "inscription-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "inscription-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Supprimer une inscription", "inscription-delet", "DELET", false));
    if (service.findById(id) == null) throw new APIException("L'inscription  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "inscription-list")
  public List<InscriptionBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Lister les inscriptions", "inscription-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteInscriptionDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "inscription-edit")
  public void update(@Valid @RequestBody InscriptionRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Modifier une inscription", "inscription-edit", "PUT", false));
    if (service.findById(id) == null) throw new APIException("L'inscription avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("L'inscription avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }

  @Override
  @GetMapping("/download/attestation/{inscriptionId}")
  @CrossOrigin(origins = {"*"}, exposedHeaders = {"Content-Disposition"})
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "print-attestation")
  public void download(@PathVariable("inscriptionId") Long inscriptionId, HttpServletResponse response) throws URISyntaxException, JRException, IOException {
    autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Imprimer une attestation", "print-attestation", "GET", false));

    //File contains all stored paths, names, and extensions
    Path fileNamePath = service.downloadAttestation(inscriptionId);
    fileNamePath = fileNamePath.toAbsolutePath();

    log.info("Path " + fileNamePath);

    if (!Files.exists(fileNamePath)) {
      log.info("File not exist");
      return;
    }

    // Specifying a File
    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    response.setHeader("Content-Transfer-Encoding", "binary;");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNamePath + "\"");
    try {
      Files.copy(fileNamePath, response.getOutputStream());
      response.flushBuffer();
      log.info("Copy to " + response.getStatus());
    } catch (IOException ex) {
      System.out.println("IOException");
    }
  }
}