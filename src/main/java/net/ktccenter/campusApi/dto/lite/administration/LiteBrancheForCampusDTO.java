package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LiteBrancheForCampusDTO {
  private Long id;
  private String code;
  private String ville;
}
