package net.ktccenter.campusApi.dto.reponse;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Setter
@Getter
public abstract class AbstractDTO {
    private Long id;
    private String createdByUser;
    private Instant createdDate;
    private String lastModifiedByUser;
    private Instant lastModifiedDate;
    private Boolean IsDefault;
}
