package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BrancheDTO extends AbstractDTO {
  private String code;
  private String ville;
  private String telephone;
  private String email;
  private Boolean parDefaut;
  private Set<LiteCampusDTO> campus = new HashSet<>();
  private Set<LiteSessionDTO> sessions = new HashSet<>();
}
