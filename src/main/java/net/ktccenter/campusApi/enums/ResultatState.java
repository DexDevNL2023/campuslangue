package net.ktccenter.campusApi.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum ResultatState {
    ALL("All"),
    WIN("Win"),
    FAIL("Fail");

    private final String value;

    ResultatState(String value) {
    this.value = value;
    }

    public String getValue() {
    	return value;
    }

    public static Optional<ResultatState> toEnum(String label) {
        if (label == null) {
          return Optional.empty();
        }

        for (ResultatState mine : ResultatState.values()) {
          if (label.equals(mine.getValue())) {
            return Optional.of(mine);
          }
        }

        throw new IllegalArgumentException("no supported");
    }

    public static List<ResultatState> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(ResultatState.values()));
    }
}
