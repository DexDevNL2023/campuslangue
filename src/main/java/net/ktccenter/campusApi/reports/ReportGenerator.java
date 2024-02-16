package net.ktccenter.campusApi.reports;

import net.ktccenter.campusApi.dao.administration.InstitutionRepository;
import net.ktccenter.campusApi.dao.cours.ExamenRepository;
import net.ktccenter.campusApi.dao.scolarite.InscriptionRepository;
import net.ktccenter.campusApi.entities.administration.Institution;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.utils.MyUtils;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportGenerator {
    private final ExamenRepository examenRepository;
    private final InstitutionRepository institutionRepository;
    private final InscriptionRepository inscriptionRepository;

    public ReportGenerator(ExamenRepository examenRepository, InstitutionRepository institutionRepository, InscriptionRepository inscriptionRepository) {
        this.examenRepository = examenRepository;
        this.institutionRepository = institutionRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    public Path downloadAttestationFormationAllemandToPdf(Inscription inscription) throws JRException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Institution institution = institutionRepository.findFirstByOrderByName().orElseThrow(
                () -> new ResourceNotFoundException("Aucune institution n'est enregistrée")
        );
        if (inscription == null) throw new ResourceNotFoundException("L'inscription avec l'id n'a été envoyée");
        if (inscription.getDateDelivranceAttestation() == null) {
            inscription.setDateDelivranceAttestation(MyUtils.currentDate());
            inscription = inscriptionRepository.save(inscription);
        }
        Examen examen = examenRepository.findByInscription(inscription);
        if (examen == null)
            throw new ResourceNotFoundException("Aucun examen pour cette inscription " + inscription);
        String fullName = !inscription.getEtudiant().getPrenom().isEmpty() ? inscription.getEtudiant().getNom() + " " + inscription.getEtudiant().getPrenom() : inscription.getEtudiant().getNom();
        // charge le fichier et compile-le
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("titre", "Bescheinigung über Sprachkenntnisse in Deutsch");
        params.put("fullName", fullName);
        params.put("dateDebut", dateFormat.format(inscription.getSession().getDateDebut()));
        params.put("dateFin", dateFormat.format(inscription.getSession().getDateFin()));
        params.put("moyenne", examen.getMoyenne());
        params.put("niveau", inscription.getSession().getNiveau().getCode());
        params.put("institution", institution.getName());
        params.put("adresse", institution.getAdresse());
        params.put("dateDelivrance", dateFormat.format(inscription.getDateDelivranceAttestation()));

        ClassPathResource file = new ClassPathResource("data/attestation-formation-allemand.jrxml", this.getClass().getClassLoader());
        JasperPrint print = JasperFillManager.fillReport(JasperCompileManager.compileReport(file.getPath()), params, new JREmptyDataSource());

        // crée un dossier pour stocker le rapport
        fullName = fullName.replace(" ", "_");
        String fileName = "/" + fullName + ".pdf";

        // créer une méthode privée qui renvoie le lien vers le fichier pdf spécifique
        return getReportPath(print, fileName);
    }

    private Path getReportPath(JasperPrint jasperPrint, String fileName) throws IOException, JRException {
        String uploadDir = StringUtils.cleanPath("/app/generated-reports");
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String link = StringUtils.cleanPath(uploadPath.toAbsolutePath() + fileName);
        uploadPath = Paths.get(link);
        // générer le rapport et l'enregistrer dans le dossier qui vient d'être créé
        JasperExportManager.exportReportToPdfFile(jasperPrint, link);
        //JasperExportManager.exportReportToPdfFile(jasperPrint, link); uploadPath.toAbsolutePath().toString()+fileName);
        return uploadPath;
    }
}
