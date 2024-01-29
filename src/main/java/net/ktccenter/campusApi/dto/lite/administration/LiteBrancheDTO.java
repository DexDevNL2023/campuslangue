package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.entities.administration.Branche;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LiteBrancheDTO {
  private Long id;
  private String code;
  private String ville;
  private String telephone;
  private String email;
  private Boolean parDefaut;

  public LiteBrancheDTO(Branche branche) {
    this.id = branche.getId();
    this.code = branche.getCode();
    this.ville = branche.getVille();
    this.telephone = branche.getTelephone();
    this.email = branche.getEmail();
  }
}
