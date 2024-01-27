package net.ktccenter.campusApi.service;

import net.ktccenter.campusApi.dao.administration.BrancheRepository;
import net.ktccenter.campusApi.dao.administration.RoleDroitRepository;
import net.ktccenter.campusApi.dao.administration.UserRepository;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.BrancheMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MainService {

    @Autowired
    public BrancheRepository brancheRepository;


    @Autowired
    public BrancheMapper brancheMapper;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RoleDroitRepository roleDroitRepository;

    public List<Branche> getAllBranches() {
        return (List<Branche>) brancheRepository.findAll();
    }

    public Branche getCurrentUserBranch() {
        return getCurrentUser().getBranche();
    }


    public boolean hasGrantAuthorized() {
        for (Role role : getCurrentUser().getRoles()) {
            if (role.getIsGrant()) {
                return true;
            }
        }
        return false;
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResourceNotFoundException("Impossible de retouver l'utilisateur courant!");
        }
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByNomOrEmail(userPrincipal.getUsername());
        if (user == null)
            throw new ResourceNotFoundException("Aucun utilisateur n'existe avec le nom utilisateur " + userPrincipal.getUsername());
        return user;
    }

    public boolean isAuthorized(String actionKey) {
        for (Role role : getCurrentUser().getRoles()) {
            if (role.getIsSuper()) {
                return true;
            } else {
                List<RoleDroit> permissions = roleDroitRepository.findAllByRole(role);
                for (RoleDroit permission : permissions) {
                    if (permission.getHasDroit() && permission.getDroit().getKey().equals(actionKey)) return true;
                }
            }
        }
        return false;
    }

    public LiteBrancheDTO getCurrentLiteBranche() {
        return new LiteBrancheDTO(getCurrentUser().getBranche());
    }

}
