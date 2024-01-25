package net.ktccenter.campusApi.dto.reponse.administration;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private Long id;
    private String email;
    private List<String> roles;
    private ProfileDTO profile;

    public JwtResponse(String accessToken, Long id,  String email, List<String> roles, ProfileDTO profile) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.profile = profile;
    }
}
