package net.ktccenter.campusApi.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public enum ResultatFilter {
    ALPHABETIQUE("Alphabetique"),
    MERITE("Merite");

    private final String value;

    ResultatFilter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<ResultatFilter> toEnum(String label) {
        if (label == null) {
            return Optional.empty();
        }

        for (ResultatFilter mine : ResultatFilter.values()) {
            if (label.equals(mine.getValue())) {
                return Optional.of(mine);
            }
        }

        throw new IllegalArgumentException("no supported");
    }

    public static List<ResultatFilter> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(ResultatFilter.values()));
    }
}