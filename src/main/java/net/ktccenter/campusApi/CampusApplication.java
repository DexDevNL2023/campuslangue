package net.ktccenter.campusApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.*;
import net.ktccenter.campusApi.dao.cours.PlageHoraireRepository;
import net.ktccenter.campusApi.dao.scolarite.DiplomeRepository;
import net.ktccenter.campusApi.dao.scolarite.FormateurRepository;
import net.ktccenter.campusApi.dao.scolarite.ModePaiementRepository;
import net.ktccenter.campusApi.dao.scolarite.RubriqueRepository;
import net.ktccenter.campusApi.entities.administration.*;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.entities.scolarite.ModePaiement;
import net.ktccenter.campusApi.entities.scolarite.Rubrique;
import net.ktccenter.campusApi.enums.Jour;
import net.ktccenter.campusApi.enums.Sexe;
import net.ktccenter.campusApi.enums.TypeUser;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SecurityScheme(
		name = "bearerAuth",
		scheme = "bearer",
		bearerFormat = "JWT",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER
)
public class CampusApplication implements CommandLineRunner {
    @Autowired
    private MainService mainService;
    private final BrancheRepository brancheRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final InstitutionRepository institutionRepository;
    private final RubriqueRepository rubriqueRepository;
    private final ModePaiementRepository modePaiementRepository;
    private final FormateurRepository formateurRepository;
    private final PlageHoraireRepository plageHoraireRepository;
    private final ParametreInstitutionRepository parametreRepository;
    private final JourOuvrableRepository jourOuvrableRepository;

    private final DiplomeRepository diplomeRepository;
    private final String password = "passwords";
    private final String username = "admin@admin.com";
    private final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public CampusApplication(BrancheRepository brancheRepository, UserRepository userRepository, RoleRepository roleRepository, InstitutionRepository institutionRepository, RubriqueRepository rubriqueRepository, ModePaiementRepository modePaiementRepository, FormateurRepository formateurRepository, PlageHoraireRepository plageHoraireRepository, ParametreInstitutionRepository parametreRepository, JourOuvrableRepository jourOuvrableRepository, DiplomeRepository diplomeRepository) {
        this.brancheRepository = brancheRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.institutionRepository = institutionRepository;
        this.rubriqueRepository = rubriqueRepository;
        this.modePaiementRepository = modePaiementRepository;
        this.formateurRepository = formateurRepository;
        this.plageHoraireRepository = plageHoraireRepository;
        this.parametreRepository = parametreRepository;
        this.jourOuvrableRepository = jourOuvrableRepository;
        this.diplomeRepository = diplomeRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CampusApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        buildDefaultBranch();
        buildRole();
        buildAdmin();
        buildDefaultInstitution();
        buildDefaultParametreInstitution();
        buildDefautRubrique();
        buildDefautModePaiement();
        buildDefautFormateur();
        buildDefautJourOuvrable();
        buildDefautPlage();
    }

    private void buildDefaultParametreInstitution() {
        ParametreInstitution parametres = new ParametreInstitution();
        parametres.setBareme(20);
        parametres.setDevise("XAF");
        parametres.setDureeCours(1.0);
        parametres.setPourcentageTestModule(0);
        parametreRepository.save(parametres);
    }

    private void buildDefaultBranch() {
        if (brancheRepository.findByParDefaut(true) == null) brancheRepository.save(new Branche()
                .setCode("YDE")
                .setVille("Yaoundé")
                .setTelephone("237674746071")
                .setEmail("yaounde@kamer-center.net")
                .setParDefaut(true)
        );
    }

    private void buildDefautJourOuvrable() {
        for (Jour jour : Jour.values()) {
            jourOuvrableRepository.save(new JourOuvrable(jour));
        }
    }

    private void buildDefautPlage() {
        if (plageHoraireRepository.count() > 0) return;
        ParametreInstitution parametres = parametreRepository.findFirstByOrderById();
        double dureeCours = parametres.getDureeCours(); // Récupérer la durée du cours en demi-heures
        for (Jour jour : Jour.values()) {
            JourOuvrable ouvrable = jourOuvrableRepository.findByJour(jour);
            double debutIntervalle = ouvrable.getIntervalle()[0] * 2.0; // Convertir en demi-heures
            double finIntervalle = ouvrable.getIntervalle()[1] * 2.0; // Convertir en demi-heures
            for (double i = debutIntervalle; i < finIntervalle; i += dureeCours * 2) { // Utiliser dureeCours comme pas
                double startTimeValue = i / 2;
                double endTimeValue = (i / 2 + dureeCours);
                String code = jour.getValue().substring(0, 3) + "-" + (int) startTimeValue + "h" + (convertToMinutes(startTimeValue) == 0 ? "" : convertToMinutes(startTimeValue)) + "-" + (int) endTimeValue + "h" + (convertToMinutes(endTimeValue) == 0 ? "" : convertToMinutes(endTimeValue)); // Convertir en heures
                LocalTime startTime = LocalTime.of((int) startTimeValue, convertToMinutes(startTimeValue));
                LocalTime endTime = LocalTime.of((int) endTimeValue, convertToMinutes(endTimeValue));
                Optional<PlageHoraire> result = plageHoraireRepository.findByCode(code);
                if (!result.isPresent()) {
                    PlageHoraire plage = new PlageHoraire();
                    plage.setCode(code);
                    plage.setJour(jour);
                    plage.setStartTime(startTime); // Convertir en heures et minutes
                    plage.setEndTime(endTime); // Convertir en heures et minutes
                    plageHoraireRepository.save(plage);
                }
            }
        }
    }

    private int convertToMinutes(double decimalValue) {
        double minutes = decimalValue % 1 * 60;
        return (int) minutes;
    }

    private void buildDefautFormateur() {
        log.info("Formateur create begin");
        Formateur formateur = formateurRepository.findByIsDefault(true).orElse(
                createDefaultFormateur()
        );
    }

    private Formateur createDefaultFormateur() {
      Formateur formateur = new Formateur();
      log.info("Ajout du formateur par defaut");
      // On vérifie que l'etudiant à une adresse mail, si oui on creer son compte utilisateur
      if (userRepository.findByEmail("sonnymba@gmail.com") != null) return null;
      Role authority = roleRepository.findByRoleName("ROLE_FORMATEUR");
      if (authority == null) {
        authority = new Role();
        authority.setIsSuper(true);
        authority.setIsDefault(false);
        authority.setLibelle("ROLE_FORMATEUR");
        authority = roleRepository.save(authority);
      }
      //here I try to retrieve the Admin from my persistence layer
      User newUser = new User();
      newUser.setNom("Mba");
      newUser.setPrenom("Sonny");
      newUser.setEmail("sonnymba@gmail.com");
      newUser.setUniqueId(MyUtils.UniqueId());
      newUser.setTypeUser(TypeUser.INTERNE);
      newUser.setLangKey("Fr");
      newUser.setAccountNonExpired(true);
      newUser.setAccountNonLocked(true);
      newUser.setCredentialsNonExpired(true);
      newUser.setEnabled(true);
        newUser.setIsGrant(true);
      String encodedPassword = bCryptPasswordEncoder.encode("default");
      newUser.setPassword(encodedPassword);
      newUser.setAuthorities(authority);
        newUser.setBranche(mainService.getDefaultBranch());
      userRepository.save(newUser);
      formateur.setUser(newUser);
      log.info("user create successful "+ newUser);

      String matricule = MyUtils.GenerateMatricule("DEFAULT-TRAINER");
      log.info("Personnel-Formateur 4");
      formateur.setMatricule(matricule);
      formateur.setTelephone("237 691178625");
      formateur.setNom("Mba");
      formateur.setSexe(Sexe.MASCULIN);
      formateur.setPrenom("Sonny");
      formateur.setEmail("sonnymba@gmail.com");
      formateur.setIsDefault(true);
        formateur.setBranche(mainService.getDefaultBranch());
        Diplome diplome = diplomeRepository.findFirstByOrderById();
        if (diplome == null) {
            diplome = new Diplome("L3", "LICENCE");
        }
        formateur.setDiplome(diplome);
      formateur = formateurRepository.save(formateur);
      log.info("Personnel-Formateur : "+ formateur);
      return formateur;
    }

    private void buildDefautModePaiement() {
        if (modePaiementRepository.count() > 0) return;
        if (!modePaiementRepository.findByCode("MOMO").isPresent()) {
            ModePaiement modePaiement = new ModePaiement();
            modePaiement.setCode("MOMO");
            modePaiement.setIsDefault(false);
            modePaiement.setLibelle("MTN Mobile Money");
            modePaiementRepository.save(modePaiement);
        }
        if (!modePaiementRepository.findByCode("OM").isPresent()) {
            ModePaiement modePaiement = new ModePaiement();
            modePaiement.setCode("OM");
            modePaiement.setIsDefault(false);
            modePaiement.setLibelle("Orange Money");
            modePaiementRepository.save(modePaiement);
        }
        if (!modePaiementRepository.findByCode("CASH").isPresent()) {
            ModePaiement modePaiement = new ModePaiement();
            modePaiement.setCode("CASH");
            modePaiement.setIsDefault(false);
            modePaiement.setLibelle("Espèce");
            modePaiementRepository.save(modePaiement);
        }
    }

    private void buildDefaultInstitution() {
        if (institutionRepository.count() > 0) return;
        Institution institution = new Institution();
        institution.setName("KTC-CENTER");
        institution.setSigle("ktc");
        institution.setEstFonctionnel(true);
        institution.setAnneeOuverture(2019);
        institution.setEstAgree(true);
        institution.setEstLocataire(true);
        institution.setDisposeConventionEtat(true);
        institution.setNombreSitesOccupes(5);
        institution.setEstTerrainTitre(false);
        institution.setTelephone("+237 691 70 27 83");
        institution.setEmail("info@ktc-center.net");
        institution.setAdresse("Biyemassi");
        institution.setVille("Yaoundé");
        institution.setEnteteGauche("Ets Kamer Schulungs - und Beratungszentrum / Professionelles Schulungszentrum\n" +
                "Studium – Ausbildung – Software Edition – Erstellung & Hosting von Websites\n" +
                "Steuernummer: P108212503453X RC: YAO/2016/A/3141 Arr.053/MINEFOP/SG/DFOP/SDGSF/SAD");
        institution.setEnteteDroite("Ets Kamer Training and Consulting Center / Centre de Formation Professionnelle\n" +
                "Etudes – Formation – Edition de Logiciels – Création & Hébergement de sites web\n" +
                "N°Contribuable: P108212503453X RC : YAO/2016/A/3141 Arr.053/MINEFOP/SG/DFOP/SDGSF/SAD");
        institution.setPiedPage("E-Mail: info@kamer-center.net\n" +
                "www.kamer-center.net\n" +
                "Telefon/Fax: (237) 691 702 783/691 702 665\n" +
                "BP: 13501 Yaoundé, Kamerun\n" +
                "Sitz: Yaoundé-Abstieg Lycée de Biyem-assi gegenüber der Station Green Oil");
        institution.setIsDefault(true);
        institutionRepository.save(institution);
    }

    private void buildDefautRubrique() {
        if (rubriqueRepository.count() > 0) return;
        if (!rubriqueRepository.findByCode("INSCRIPTION").isPresent()) {
            Rubrique rubrique = new Rubrique();
            rubrique.setCode("INSCRIPTION");
            rubrique.setIsDefault(false);
            rubrique.setLibelle("Frais d'inscription");
            rubriqueRepository.save(rubrique);
        }
        if (!rubriqueRepository.findByCode("PENSION").isPresent()) {
            Rubrique rubrique = new Rubrique();
            rubrique.setCode("PENSION");
            rubrique.setIsDefault(false);
            rubrique.setLibelle("Frais de pension");
            rubriqueRepository.save(rubrique);
        }
        if (!rubriqueRepository.findByCode("PREPARATION").isPresent()) {
            Rubrique rubrique = new Rubrique();
            rubrique.setCode("PREPARATION");
            rubrique.setIsDefault(false);
            rubrique.setLibelle("Cours de préparation");
            rubriqueRepository.save(rubrique);
        }
        if (!rubriqueRepository.findByCode("RATTRAPAGE").isPresent()) {
            Rubrique rubrique = new Rubrique();
            rubrique.setCode("RATTRAPAGE");
            rubrique.setIsDefault(false);
            rubrique.setLibelle("Frais de rattrapage");
            rubriqueRepository.save(rubrique);
        }
    }

    private void buildRole() {
        if (roleRepository.count() > 0) return;
        if (roleRepository.findByRoleName("ROLE_ADMIN") == null) {
            Role authority = new Role();
            authority.setIsSuper(true);
            authority.setIsDefault(false);
            authority.setLibelle("ROLE_ADMIN");
            roleRepository.save(authority);
        }
        if (roleRepository.findByRoleName("ROLE_USER") == null) {
            Role authority = new Role();
            authority.setIsSuper(false);
            authority.setIsDefault(true);
            authority.setLibelle("ROLE_USER");
            roleRepository.save(authority);
        }
        if (roleRepository.findByRoleName("ROLE_SUPER") == null) {
            Role authority = new Role();
            authority.setIsSuper(true);
            authority.setIsDefault(false);
            authority.setLibelle("ROLE_SUPER");
            roleRepository.save(authority);
        }
        if (roleRepository.findByRoleName("ROLE_OWNER") == null) {
            Role authority = new Role();
            authority.setIsSuper(false);
            authority.setIsDefault(false);
            authority.setLibelle("ROLE_OWNER");
            roleRepository.save(authority);
        }
        if (roleRepository.findByRoleName("ROLE_DISABLED") == null) {
            Role authority = new Role();
            authority.setIsSuper(false);
            authority.setIsDefault(false);
            authority.setLibelle("ROLE_DISABLED");
            roleRepository.save(authority);
        }
        if (roleRepository.findByRoleName("ROLE_FORMATEUR") == null) {
            Role authority = new Role();
            authority.setIsSuper(false);
            authority.setIsDefault(false);
            authority.setLibelle("ROLE_FORMATEUR");
            roleRepository.save(authority);
        }
        if (roleRepository.findByRoleName("ROLE_ETUDIANT") == null) {
            Role authority = new Role();
            authority.setIsSuper(false);
            authority.setIsDefault(false);
            authority.setLibelle("ROLE_ETUDIANT");
            roleRepository.save(authority);
        }
    }

    private void buildAdmin() {
        if (userRepository.findByEmail(username) != null) return;
        Role authority = roleRepository.findByRoleName("ROLE_ADMIN");
        if (authority == null) {
            authority = new Role();
            authority.setIsSuper(true);
            authority.setLibelle("ROLE_ADMIN");
            authority = roleRepository.save(authority);
        }
        //here I try to retrieve the Admin from my persistence layer
        User newUser = new User();
        newUser.setNom("Dexter");
        newUser.setPrenom("Victor");
        newUser.setEmail(username);
        newUser.setUniqueId(MyUtils.UniqueId());
        newUser.setTypeUser(TypeUser.INTERNE);
        newUser.setLangKey("Fr");
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        newUser.setIsGrant(true);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        newUser.setAuthorities(authority);
        newUser.setBranche(mainService.getDefaultBranch());
        userRepository.save(newUser);
    }
}
