package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForNoteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementForcompteDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class CompteDTO extends AbstractDTO {
  private String code;
  private BigDecimal solde;
  private BigDecimal resteApayer;
  private LiteInscriptionForNoteDTO inscription;
  private Set<LitePaiementForcompteDTO> paiements = new HashSet<>();
}
