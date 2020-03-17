package server.services;

import server.dto.ReplyPayload;
import server.dto.TokenPayload;
import server.dto.UserDto;

public interface GeneralService {
    ReplyPayload checkClient(Long id, String password);
    UserDto getUser(Long id);
    ReplyPayload checkToken(String token, Long id, String role);
    TokenPayload createToken(Long id, String role);
    ReplyPayload disconnect();
}
