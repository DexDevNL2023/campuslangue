package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LiteProfileDTO {
  private Long id;
  private String nom;
  private String prenom;
  private String email;
  private String langKey;
  private String imageUrl;
  private String fullName;

  public String getFullName() {
    return this.nom + " " + this.prenom;
  }
}
