package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForCompteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementForCompteDTO;
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
  private LiteInscriptionForCompteDTO inscription;
  private Set<LitePaiementForCompteDTO> paiements = new HashSet<>();
}
