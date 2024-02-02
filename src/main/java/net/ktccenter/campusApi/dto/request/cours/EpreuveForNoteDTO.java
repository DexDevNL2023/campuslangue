package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EpreuveForNoteDTO {
  private Long epreuveId;
    private Float noteObtenue = 0.0F;
    private Float noteRattrapage = 0.0F;
}
