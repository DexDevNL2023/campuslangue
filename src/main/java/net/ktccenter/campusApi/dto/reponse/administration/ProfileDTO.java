package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProfileDTO {
  private Long id;
  private String nom;
  private String prenom;
  private String email;
  private String langKey;
  private String imageUrl;
  private Long brancheId;
}
