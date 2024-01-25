package net.ktccenter.campusApi.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum TypeUser {
    MINEFOP("Ministère de la Formation Professionnelle"),
    STRUCTURE("Structure"),
    INTERNE("Interne");

  private final String value;

  TypeUser(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Optional<TypeUser> toEnum(String label) {
    if (label == null) {
      return Optional.empty();
    }

    for (TypeUser mine : TypeUser.values()) {
      if (label.equals(mine.getValue())) {
        return Optional.of(mine);
      }
    }

    throw new IllegalArgumentException("no supported");
  }

  public static List<TypeUser> orderedValues = new ArrayList<>();

  static {
    orderedValues.addAll(Arrays.asList(TypeUser.values()));
  }
}
