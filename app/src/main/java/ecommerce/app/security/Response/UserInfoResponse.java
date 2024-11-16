package ecommerce.app.security.Response;

import java.util.List;





public class UserInfoResponse {

    private Long id;
    private String jwtToken;
    private String username;
    private List<String> roles;

    public UserInfoResponse(String username, List<String> roles, String jwtToken) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }

    public UserInfoResponse(Long id, String username2, List<String> roles2, String jwtToken2) {
        this.id = id;
        this.username = username2;
        this.roles = roles2;
        this.jwtToken = jwtToken2;
    }

    public UserInfoResponse(Long id2, String username2, List<String> roles2) {
        this.id = id2;
        this.username = username2;
        this.roles = roles2;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}


