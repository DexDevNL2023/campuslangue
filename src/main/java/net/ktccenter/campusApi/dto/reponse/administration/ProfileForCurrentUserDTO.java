package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.entities.administration.User;

@Setter
@Getter
@NoArgsConstructor
public class ProfileForCurrentUserDTO {
  private Long id;
  private String nom;
  private String prenom;
  private String email;
  private String langKey;
  private String imageUrl;
  private LiteBrancheDTO branche;
  private RoleDTO role;

  public ProfileForCurrentUserDTO(User user) {
    this.id = user.getId();
    this.nom = user.getNom();
    this.prenom = user.getPrenom();
    this.email = user.getEmail();
    this.imageUrl = user.getImageUrl();
    this.langKey = user.getLangKey();
  }
}
