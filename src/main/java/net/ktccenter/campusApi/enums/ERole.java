package net.ktccenter.campusApi.enums;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum ERole {

  ROLE_SUPER("Super"),
  ROLE_OWNER("Owner"),
  ROLE_ADMIN("Admin"),
  ROLE_USER("User"),
  ROLE_DISABLED("Disabled"),
  ROLE_FORMATEUR("Formateur"),
  ROLE_ETUDIANT("Apprenant");

  private final String value;

  ERole(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Optional<ERole> toEnum(String label) {
    if (label == null) {
      return Optional.empty();
    }

    for (ERole mine : ERole.values()) {
      if (label.equals(mine.getValue())) {
        return Optional.of(mine);
      }
    }

    throw new IllegalArgumentException("no supported");
  }

  public static List<ERole> orderedValues = new ArrayList<>();

  static {
    orderedValues.addAll(Arrays.asList(ERole.values()));
  }
}
