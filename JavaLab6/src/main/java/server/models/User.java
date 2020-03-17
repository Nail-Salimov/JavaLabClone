package server.models;

public class User {
    private Integer id;
    private String password;
    private String role;

    public User(Integer id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public User(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
