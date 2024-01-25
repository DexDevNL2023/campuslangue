package net.ktccenter.campusApi.reports;

import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportGenerator reportGenerator;

    public ReportServiceImpl(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @Override
    public Path downloadAttestationFormationAllemandToPdf(Inscription inscription) throws JRException, IOException {
        return reportGenerator.downloadAttestationFormationAllemandToPdf(inscription);
    }
}
