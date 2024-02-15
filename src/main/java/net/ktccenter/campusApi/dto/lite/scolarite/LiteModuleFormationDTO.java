package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;

@Setter
@Getter
@NoArgsConstructor
public class LiteModuleFormationDTO {
  private Long id;
  private String code;
  private String libelle;
  private LiteNiveauForSessionDTO niveau;

  public LiteModuleFormationDTO(ModuleFormation module) {
    this.id = module.getId();
    this.code = module.getCode();
    this.libelle = module.getLibelle();
  }
}
