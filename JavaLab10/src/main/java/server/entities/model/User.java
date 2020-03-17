package server.entities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String name;
    private String mail;
    private String password;
    private String token;

    public User(String name, String password, String mail) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }
}
