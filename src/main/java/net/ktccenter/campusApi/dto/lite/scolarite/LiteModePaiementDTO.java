package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.ModePaiement;

@Setter
@Getter
@NoArgsConstructor
public class LiteModePaiementDTO {
  private Long id;
  private String code;
  private String libelle;

  public LiteModePaiementDTO(ModePaiement modePaiement) {
    this.id = modePaiement.getId();
    this.code = modePaiement.getCode();
    this.libelle = modePaiement.getLibelle();
  }
}
