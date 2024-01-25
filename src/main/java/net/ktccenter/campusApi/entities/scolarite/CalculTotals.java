package net.ktccenter.campusApi.entities.scolarite;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculTotals {
    private BigDecimal solde = BigDecimal.valueOf(0.0);
    private BigDecimal resteApayer = BigDecimal.valueOf(0.0);
}
