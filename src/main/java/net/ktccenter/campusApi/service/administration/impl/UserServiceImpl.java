package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import net.ktccenter.campusApi.dao.administration.ModuleRepository;
import net.ktccenter.campusApi.dao.administration.RoleDroitRepository;
import net.ktccenter.campusApi.dao.administration.RoleRepository;
import net.ktccenter.campusApi.dao.administration.UserRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.*;
import net.ktccenter.campusApi.dto.reponse.PermissionModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.*;
import net.ktccenter.campusApi.dto.reponse.branch.UserBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.UpdateUserRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.UserPasswordResetDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import net.ktccenter.campusApi.entities.administration.Module;
import net.ktccenter.campusApi.entities.administration.*;
import net.ktccenter.campusApi.enums.TypeUser;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.UserMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.InstitutionService;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends MainService implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final JavaMailSender javaMailSender;
    private final InstitutionService institutionService;
    private final RoleRepository roleRepository;
    private final RoleDroitRepository roleDroitRepository;
    private final ModuleRepository moduleRepository;
    private final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @Value("${app.template.url}")
    private String APP_TEMPLATE_URL;

    @Value("${app.account.validation.url}")
    private String APP_ACCOUNT_VALIDATION_URL;

    @Value("${app.password.reset.url}")
    private String APP_PASSWORD_RESET_URL;


    public UserServiceImpl(UserRepository repository, UserMapper mapper, JavaMailSender javaMailSender, InstitutionService institutionService, RoleRepository roleRepository, RoleDroitRepository roleDroitRepository, ModuleRepository moduleRepository) {
      this.repository = repository;
      this.mapper = mapper;
      this.javaMailSender = javaMailSender;
      this.institutionService = institutionService;
      this.roleRepository = roleRepository;
        this.roleDroitRepository = roleDroitRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public User updateUser(String email, String password, Long id) {
      User existUser = findById(id);
      existUser.setEmail(email);
      String encodedPassword = bCryptPasswordEncoder.encode(password);
      existUser.setPassword(encodedPassword);
      return repository.save(existUser);
    }

  @Override
  public ProfileDTO findProfileByEmail(String email) {
    if (!repository.existsByEmail(email)) {
      throw new RuntimeException("User not found");
    }
    User user = repository.findByEmail(email);
    ProfileDTO profile = new ProfileDTO();
    profile.setId(user.getId());
    profile.setNom(user.getNom());
    profile.setPrenom(user.getPrenom());
    profile.setEmail(user.getEmail());
    profile.setImageUrl(user.getImageUrl());
    profile.setLangKey(user.getLangKey());
      profile.setBrancheId(user.getBranche().getId());
    return profile;
  }

    @Override
    public ProfileForCurrentUserDTO findProfileCurrentUser() {
        if (getCurrentUser() == null) {
            throw new RuntimeException("User not found");
        }
        User user = getCurrentUser();
        ProfileForCurrentUserDTO profile = new ProfileForCurrentUserDTO(user);
        profile.setBranche(new LiteBrancheDTO(user.getBranche()));
        Role role = user.getRoles().stream().findFirst().get();
        RoleDTO roleDto = new RoleDTO(role);
        roleDto.setPermissions(getAllPermissionsByRole(role));
        profile.setRole(roleDto);
        return profile;
    }

    private List<PermissionModuleDTO> getAllPermissionsByRole(Role role) {
        //return roleDroitRepository.findAllByRole(role).stream().map(this::buildPermissionLiteDto).collect(Collectors.toList());
        List<PermissionModuleDTO> list = new ArrayList<>();
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        for (Module module : modules) {
            PermissionModuleDTO dto = new PermissionModuleDTO();
            dto.setModule(new LiteModuleDTO(module));
            List<LiteRoleDroitDTO> data = roleDroitRepository.findAllByModuleAndRole(module, role).stream().map(this::buildPermissionLiteDto).collect(Collectors.toList());
            dto.setData(data);
            list.add(dto);
        }
        return list;
    }

    private LiteRoleDroitDTO buildPermissionLiteDto(RoleDroit roleDroit) {
        LiteRoleDroitDTO permissionLiteDto = new LiteRoleDroitDTO(roleDroit);
        permissionLiteDto.setDroit(new LiteDroitDTO(roleDroit.getDroit()));
        return permissionLiteDto;
    }

  @Override
  public User createUser(String nom, String prenom, String email, String roleName, String imageUrl, String passwordText, TypeUser typeUser, Boolean isGrant, Branche branche) {
      log.info("User 1");
      if (repository.existsByEmail(email)) {
        log.info("User 2");
        return repository.findByEmail(email);
      }
      log.info("User 3");

      Role authority = roleRepository.findByRoleName(roleName);
      log.info("User 4");
      if (authority == null) {
        authority = new Role();
        authority.setIsSuper(true);
        authority.setIsDefault(false);
        authority.setLibelle("ROLE_USER");
        authority = roleRepository.save(authority);
        log.info("User 5");
      }
      log.info("User 6 : "+authority.getId()+" "+authority.getLibelle());

      User newUser = new User();
      newUser.setNom(nom);
      newUser.setPrenom(prenom);
      newUser.setEmail(email);
      newUser.setImageUrl(imageUrl);
      String uniqueId = MyUtils.UniqueId();
      newUser.setUniqueId(uniqueId);
      log.debug("User 7 : "+newUser.getUniqueId());
      if (typeUser == null) {
          newUser.setTypeUser(TypeUser.INTERNE);
      } else {
          newUser.setTypeUser(typeUser);
      }
      String encodedPassword = null;
      if (passwordText == null) {
          encodedPassword = bCryptPasswordEncoder.encode(MyUtils.generatedPassWord());
      } else {
          encodedPassword = bCryptPasswordEncoder.encode(passwordText);
      }
      newUser.setPassword(encodedPassword);
      log.info("User 8 : "+newUser.getPassword());
      newUser.setAuthorities(authority);
      log.info("User 9 : "+newUser.getRoles().toString());
      newUser.setAccountNonExpired(false);
      newUser.setAccountNonLocked(false);
      newUser.setCredentialsNonExpired(false);
      newUser.setEnabled(true);
      newUser.setIsGrant(isGrant);
      newUser.setBranche(branche);
      newUser = repository.save(newUser);
      log.info("User 10 : "+newUser.getId());
      //sendHtmlMail(newUser);
      log.info("User 11");
      return newUser;
    }

    public void sendHtmlMail(User user) {
      try {
        InstitutionDTO parametre = institutionService.getCurrentInstitution();
        String toAddress = user.getEmail();
        String fromAddress = parametre.getEmail();
        String senderName = parametre.getName();
        String subject = "Creation du compte";
        String body = "Votre Compte utilisateur Campus Manager App a été crée avec sucèss, veuillez vous connecter en utilisant les itentifiants suivant : \n" +
          "Login : " + user.getEmail() + "\n" + "Password : " + user.getPassword();
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
        helper.setFrom(fromAddress, senderName);
        if (toAddress.contains(",")) {
          helper.setTo(toAddress.split(","));
        } else {
          helper.setTo(toAddress);
        }
        helper.setSubject(subject);
        helper.setText(body, true);
        javaMailSender.send(mail);
        System.out.println("Sent mail: " + subject);
      } catch (MessagingException | UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public UserDTO save(UserRequestDTO dto) {
        Branche branche = brancheRepository.findById(dto.getBrancheId()).orElse(null);
        if (branche == null)
            throw new ResourceNotFoundException("Aucune branche avec l'id " + dto.getBrancheId());
        Role authority = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (authority == null)
            throw new ResourceNotFoundException("Le rôle avec l'id " + dto.getRoleId() + " n'existe pas!");
        User user = createUser(dto.getNom(), dto.getPrenom(), dto.getEmail(), authority.getLibelle(), dto.getImageUrl(), dto.getPassword(), dto.getTypeUser(), dto.getIsGrant(), branche);
        UserDTO result = mapper.asDTO(user);
        result.setRole(new LiteRoleDTO(user.getRoles().stream().findFirst().get()));
        return result;
    }

    @Override
    public List<LiteUserDTO> save(List<ImportUserRequestDTO> dtos) {
        return  ((List<User>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        User user = findById(id);
        if (!user.getRoles().isEmpty())
            throw new ResourceNotFoundException("L'utilisateur avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("L'utilisateur avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public UserDTO getOne(Long id) {
        User user = findById(id);
        UserDTO result = mapper.asDTO(user);
        result.setRole(new LiteRoleDTO(user.getRoles().stream().findFirst().get()));
        return result;
    }

    @Override
    public Page<LiteUserDTO> findAll(Pageable pageable) {
        Page<User> entityPage = repository.findAll(pageable);
        List<User> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public UserDTO update(UserRequestDTO dto, Long id) {
        User exist =  findById(id);
        dto.setId(exist.getId());
        exist = mapper.asEntity(dto);
        Branche branche = brancheRepository.findById(dto.getBrancheId()).orElse(null);
        if (branche == null)
            throw new ResourceNotFoundException("Aucune branche avec l'id " + dto.getBrancheId());
        exist.setBranche(branche);
        Role authority = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (authority == null)
            throw new ResourceNotFoundException("Le rôle avec l'id " + dto.getRoleId() + " n'existe pas!");
        exist.setAuthorities(authority);
        return mapper.asDTO(repository.save(exist));
    }

    @Override
    public void passwordResetSendCode(String email, String siteUrl) throws UnsupportedEncodingException, MessagingException {
        User user = repository.findByEmail(email);
        if(user == null){
            throw new ResourceNotFoundException("L'utilisateur avec l'email " + email + " n'existe pas");
        }

        String randomCode = RandomString.make(64);
        user.setPasswordResetCode(randomCode);
        repository.save(user);
        String retour = MyUtils.sendMail(
                user,
                "Reset password",
                "Click on the button below to reset your password on our online payment platform",
                "Click to reset",
                APP_PASSWORD_RESET_URL, user.getPasswordResetCode());
        if(!retour.equals("OK")){
            throw new APIException(retour);
        }
    }

    @Override
    public String verifyEmailCode(String verificationCode) {
        return null;
    }

    @Override
    public String verifyPasswordCode(String verificationCode) {
        try{
            User user =  repository.findByPasswordResetCode(verificationCode);
            if (user == null) {
                return "Le code de verification est incorrect";
            } else {
                user.setPasswordResetCode(null);
                repository.save(user);
                return "OK";
            }
        }catch (Exception e) {
            throw new APIException(e.getLocalizedMessage());
        }
    }

    @Override
    public void resetPassword(UserPasswordResetDTO userPassword) {
        User user = repository.findByPasswordResetCode(userPassword.getPasswordResetCode());
        if(user == null){
            throw new ResourceNotFoundException("Reset password code  is not valid");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(userPassword.getPassword());
        user.setPassword(encodedPassword);
        user.setPasswordResetCode(null);
        repository.save(user);
    }

    @Override
    public String changePassword() {
        try{
            User user = getCurrentUser();
            String randomCode = RandomString.make(64);
            user.setPasswordResetCode(randomCode);
            User savedUser = repository.save(user);
            String response = MyUtils.sendMail(
                    savedUser,
                    "Change password",
                    "Click on the button below to change your password on our online payment platform",
                    "Click to change",
                    APP_PASSWORD_RESET_URL, user.getPasswordResetCode());
            if(!response.equals("OK")){
                throw new APIException(response);
            }
            return response;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public boolean existsByNomAndEmail(String nom, String email) {
        return repository.findByNomAndEmail(nom, email).isPresent();
    }

    @Override
    public boolean equalsByDto(UserRequestDTO dto, Long id) {
        User ressource = repository.findByNomAndEmail(dto.getNom(), dto.getEmail()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public List<UserBranchDTO> findAll() {
        List<UserBranchDTO> result = new ArrayList<>();
        if (hasGrantAuthorized()) {
            for (Branche b : getAllBranches()) {
                result.add(buildData(b));
            }
        } else {
            result.add(buildData(getCurrentUserBranch()));
        }
        return result;
    }

    @Override
    public UserDTO updateUserFrom(UpdateUserRequestDTO dto, Long id) {
        User exist = findById(id);
        dto.setId(exist.getId());
        exist = mapper.asEntity(dto);
        Branche branche = brancheRepository.findById(dto.getBrancheId()).orElse(null);
        if (branche == null)
            throw new ResourceNotFoundException("Aucune branche avec l'id " + dto.getBrancheId());
        exist.setBranche(branche);
        Role authority = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (authority == null)
            throw new ResourceNotFoundException("Le rôle avec l'id " + dto.getRoleId() + " n'existe pas!");
        exist.setAuthorities(authority);
        exist = repository.save(exist);
        UserDTO result = mapper.asDTO(exist);
        result.setRole(new LiteRoleDTO(exist.getRoles().stream().findFirst().get()));
        return result;
    }

    UserBranchDTO buildData(Branche branche) {
        UserBranchDTO dto = new UserBranchDTO();
        dto.setBranche(brancheMapper.asLite(branche));
        List<LiteUserDTO> list = new ArrayList<>();
        List<User> users = repository.findAllByBranche(branche);
        for (User user : users) {
            LiteUserDTO result = new LiteUserDTO(user);
            result.setRole(new LiteRoleDTO(user.getRoles().stream().findFirst().get()));
            list.add(result);
        }
        dto.setData(list);
        return dto;
    }
}
