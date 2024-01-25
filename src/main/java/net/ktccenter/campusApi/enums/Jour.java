package net.ktccenter.campusApi.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The Langue enumeration.
 */
public enum Jour {
    LUNDI("Lundi"),
    MARDI("Mardi"),
    MERCREDI("Mercredi"),
    JEUDI("Jeudi"),
    VENDREDI("Vendredi"),
    SAMEDI("Samedi"),
    DIAMNCHE("Dimanche");

    private final String value;

    Jour(String value) {
    	this.value = value;
    }

    public String getValue() {
    	return value;
    }

  public static Optional<Jour> toEnum(String label) {
    if (label == null) {
      return Optional.empty();
    }

    for (Jour mine : Jour.values()) {
      if (label.equals(mine.getValue())) {
        return Optional.of(mine);
      }
    }

    throw new IllegalArgumentException("no supported");
  }

  public static List<Jour> orderedValues = new ArrayList<>();

  static {
    orderedValues.addAll(Arrays.asList(Jour.values()));
  }
}
