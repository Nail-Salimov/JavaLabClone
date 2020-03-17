package server.services;

import server.dto.ReplyPayload;
import server.dto.TokenPayload;
import server.dto.UserDto;

public interface GeneralService {
    ReplyPayload checkClient(int id, String password);
    UserDto getUser(int id);
    ReplyPayload checkToken(String token, int id, String role);
    TokenPayload createToken(int id, String role);
    ReplyPayload disconnect();
}
