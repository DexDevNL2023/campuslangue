package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportModePaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModePaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModePaiementRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.ModePaiement;
import net.ktccenter.campusApi.service.GenericService;

public interface ModePaiementService extends GenericService<ModePaiement, ModePaiementRequestDTO, ModePaiementDTO, LiteModePaiementDTO, ImportModePaiementRequestDTO> {

  boolean equalsByDto(ModePaiementRequestDTO dto, Long id);

  ModePaiement findByCode(String code);

  boolean existByCode(String code);

  ModePaiement findByCodeAndLibelle(String code, String libelle);
}
