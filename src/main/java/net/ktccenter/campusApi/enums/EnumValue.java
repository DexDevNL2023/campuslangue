package net.ktccenter.campusApi.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "enums")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EnumValue {
  private String id;
  private String value;

  public EnumValue(String id, String value) {
    this.id = id;
    this.value = value;
  }
}
