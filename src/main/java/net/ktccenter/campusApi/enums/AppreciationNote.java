package net.ktccenter.campusApi.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum AppreciationNote {
    EXCELLENTE("Sehr Gut Bestanden"),
    BIEN("Gut Bestanden"),
    PASSABLE("Bestanden"),
    ECHOUE("Nicht Bestanden");

    private final String value;

    AppreciationNote(String value) {
    this.value = value;
    }

    public String getValue() {
    	return value;
    }

    public static Optional<AppreciationNote> toEnum(String label) {
        if (label == null) {
          return Optional.empty();
        }

        for (AppreciationNote mine : AppreciationNote.values()) {
          if (label.equals(mine.getValue())) {
            return Optional.of(mine);
          }
        }

        throw new IllegalArgumentException("no supported");
    }

    public static List<AppreciationNote> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(AppreciationNote.values()));
    }
}
