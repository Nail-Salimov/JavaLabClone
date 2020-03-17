package server.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.models.client.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String mail;
    private String token;

    public static UserDto getDto(User user){
        return new UserDto(user.getId(), user.getName(), user.getMail(), user.getToken());
    }
}
