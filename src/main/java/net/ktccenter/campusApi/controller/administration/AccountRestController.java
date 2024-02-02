package net.ktccenter.campusApi.controller.administration;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.config.security.PasswordValidator;
import net.ktccenter.campusApi.config.security.jwt.JwtUtils;
import net.ktccenter.campusApi.config.security.services.UserDetailsImpl;
import net.ktccenter.campusApi.dto.reponse.administration.JwtResponse;
import net.ktccenter.campusApi.dto.reponse.administration.ProfileDTO;
import net.ktccenter.campusApi.dto.request.administration.AuthRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.UserPasswordResetDTO;
import net.ktccenter.campusApi.dto.request.administration.UserSendLinkDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.AuthenticationException;
import net.ktccenter.campusApi.mapper.administration.UserMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path= "/api/account")
@CrossOrigin("*")
@Slf4j
public class AccountRestController {

    @Value("${app.account.validation.success.url}")
    private String APP_ACCOUNT_VALIDATION_SUCCESS_URL;

    @Value("${app.account.validation.failed.url}")
    private String APP_ACCOUNT_VALIDATION_FAILED_URL;


    @Autowired
    private MainService mainService;
    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtUtils jwtUtils;

    public AccountRestController(
            UserService userService,  UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO request){

        log.info(request.getEmail()+" "+request.getPassword());
        try{
            if(!MyUtils.isValidEmailAddress(request.getEmail())){
                System.out.println("Bad email");
                throw new AuthenticationException("Email is invalid");
            }

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
          log.info("7");
            return ResponseEntity.ok().body(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getEmail(),
                    roles,
                    getUserByEmail(userDetails.getEmail())
            ));
        } catch (Exception ex) {
            throw new AuthenticationException(ex.getMessage());
        }
    }

  private ProfileDTO getUserByEmail(String email) {
    return userService.findProfileByEmail(email);
  }

  @PostMapping(path = "/password/send_link")
    void passwordSendLink(@RequestBody UserSendLinkDTO userSendLinkDTO, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        if(userSendLinkDTO.getEmail() == null || userSendLinkDTO.getEmail().trim().isEmpty()){
            throw new APIException("Email is required");
        }

        if(!MyUtils.isValidEmailAddress(userSendLinkDTO.getEmail())){
            throw new AuthenticationException("Email is invalid");
        }
        userService.passwordResetSendCode(userSendLinkDTO.getEmail(), MyUtils.getSiteURL(request));
    }

    @PostMapping(path = "/password/reset")
    void resetPassword(@RequestBody UserPasswordResetDTO userPassword) {
        if(userPassword.getPasswordResetCode() == null || userPassword.getPasswordResetCode().trim().isEmpty()) {
            throw new APIException("Password reset Code is required");
        }
        if(!userPassword.getPassword().equals(userPassword.getConfirmPassword())){
            throw new APIException("Password and Confirmation must be the same");
        }
        if(PasswordValidator.isValid(userPassword.getPassword())){
            userService.resetPassword(userPassword);
        }else{
            throw new APIException("The password must contain at least one At least\n" +
                    "    - one lowercase character,\n" +
                    "    - one uppercase character,\n" +
                    "    - one digit,\n" +
                    "    - one special character\n" +
                    "    - and length between 8 to 20. ");
        }

    }

    @GetMapping("/password/verify/resetCode")
    public ResponseEntity<String> verifyResetPassword(@Param("code") String code) {
        if(code == null || code.trim().isEmpty()){
            throw new APIException("code is required");
        }
        try{
            String response = userService.verifyPasswordCode(code);
            if (response.equals("OK")) {
                return ResponseEntity.ok().body("OK");
            }else{
                throw new APIException(response);
            }
        }catch(Exception e){
            throw new APIException(e.getMessage());
        }
    }
}
