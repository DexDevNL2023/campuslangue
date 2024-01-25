package net.ktccenter.campusApi.reports;

import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.nio.file.Path;

public interface ReportService {

    Path downloadAttestationFormationAllemandToPdf(Inscription inscription) throws JRException, IOException;
}
