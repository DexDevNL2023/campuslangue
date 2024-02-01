package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Compte;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class LiteCompteDTO {
  private Long id;
  private String code;
  private BigDecimal solde;
  private BigDecimal resteApayer;
  private Set<LitePaiementDTO> paiements = new HashSet<>();

  public LiteCompteDTO(Compte compte) {
    this.id = compte.getId();
    this.code = compte.getCode();
  }
}
