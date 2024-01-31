package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ProfileDTO;
import net.ktccenter.campusApi.dto.reponse.administration.UserDTO;
import net.ktccenter.campusApi.dto.reponse.branch.UserBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.UserPasswordResetDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.enums.TypeUser;
import net.ktccenter.campusApi.service.GenericService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService extends GenericService<User, UserRequestDTO, UserDTO, LiteUserDTO, ImportUserRequestDTO> {
  void passwordResetSendCode(String email, String siteUrl) throws UnsupportedEncodingException, MessagingException;
  String verifyEmailCode(String verificationCode);
  String verifyPasswordCode(String passwordResetCode);
  void resetPassword(UserPasswordResetDTO userPassword);
  String changePassword();

  User createUser(String nom, String prenom, String email, String roleName, String imageUrl, String passwordText, TypeUser typeUser, Boolean isGrant, Branche branche);
  User updateUser(String email, String password, Long id);
  ProfileDTO findProfileByEmail(String email);
  boolean existsByNomAndEmail(String nom, String email);
  boolean equalsByDto(UserRequestDTO dto, Long id);
  List<UserBranchDTO> findAll();
}
