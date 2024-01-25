package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class SalleDTO extends AbstractDTO {
  private String code;
  private String libelle;
  private Integer capacite;
  private LiteCampusDTO campus;
  private Set<LiteOccupationSalleDTO> occupations = new HashSet<>();
}
