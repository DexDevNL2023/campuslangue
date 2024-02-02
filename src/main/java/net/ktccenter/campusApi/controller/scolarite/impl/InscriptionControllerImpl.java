package net.ktccenter.campusApi.controller.scolarite.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.controller.scolarite.InscriptionController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportInscriptionRequestDTO;
import net.ktccenter.campusApi.dto.lite.LiteNewInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.InscriptionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.InscriptionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscriptionRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireExitStudientRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireNewStudientRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.InscriptionService;
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

  public InscriptionControllerImpl(InscriptionService service) {
    this.service = service;
  }

  @Override
  @PostMapping("/exist/studient")
  @ResponseStatus(HttpStatus.CREATED)
  public LiteNewInscriptionDTO inscrireExitStudient(@Valid @RequestBody InscrireExitStudientRequestDTO dto) {
    return service.inscrireExitStudient(dto);
  }

  @Override
  @PostMapping("/new/studient")
  @ResponseStatus(HttpStatus.CREATED)
  public LiteNewInscriptionDTO inscrireNewStudient(@Valid @RequestBody InscrireNewStudientRequestDTO dto) {
    return service.inscrireNewStudient(dto);
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public InscriptionDTO save(@Valid @RequestBody InscriptionRequestDTO dto) {
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteInscriptionDTO> saveAll(@Valid @RequestBody List<ImportInscriptionRequestDTO> dtos) {
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public InscriptionDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("L'inscription  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<InscriptionBranchDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteInscriptionDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody InscriptionRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("L'inscription avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("L'inscription avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }

  @Override
  @GetMapping("/download/attestation/{inscriptionId}")
  @CrossOrigin(origins = {"*"}, exposedHeaders = {"Content-Disposition"})
  public void download(@PathVariable("inscriptionId") Long inscriptionId, HttpServletResponse response) throws URISyntaxException, JRException, IOException {

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
      log.info("Copy to " + response.getStatus());
    } catch (IOException ex) {
      System.out.println("IOException");
    }
  }

  /*@RequestMapping("/csv/detail")
  public ResponseEntity<?> getDetailedCSV(@RequestBody ReportRequestDTO dto) {
    HttpHeaders headers = new HttpHeaders();
    byte[] contents;
    try {
      contents = IOUtils.toByteArray(new FileInputStream(reportService.getPDFSummary((dto))));
      headers.setContentType(MediaType.parseMediaType("application/pdf"));
      headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
      ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
      return response;
    } catch (FileNotFoundException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (IOException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }*/
}