package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportVagueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.VagueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.VagueRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Vague;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface VagueService extends GenericService<Vague, VagueRequestDTO, VagueDTO, LiteVagueDTO, ImportVagueRequestDTO> {

  boolean equalsByDto(VagueRequestDTO dto, Long id);

  Vague findByCode(String code);

  List<LiteVagueDTO> findAll();

  boolean existByCode(String code);
}
