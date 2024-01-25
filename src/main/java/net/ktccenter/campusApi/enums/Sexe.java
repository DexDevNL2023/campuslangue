package net.ktccenter.campusApi.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The Sexe enumeration.
 */
public enum Sexe {
    MASCULIN("Masculin"),
    FEMININ("Feminin");

    private final String value;

    Sexe(String value) {
    	this.value = value;
    }

    public String getValue() {
    	return value;
    }

  public static Optional<Sexe> toEnum(String label) {
    if (label == null) {
      return Optional.empty();
    }

    for (Sexe mine : Sexe.values()) {
      if (label.equals(mine.getValue())) {
        return Optional.of(mine);
      }
    }

    throw new IllegalArgumentException("no supported");
  }

  public static List<Sexe> orderedValues = new ArrayList<>();

  static {
    orderedValues.addAll(Arrays.asList(Sexe.values()));
  }
}
