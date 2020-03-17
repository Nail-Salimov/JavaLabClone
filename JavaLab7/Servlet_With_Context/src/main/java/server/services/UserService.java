package server.services;

import server.dto.StateDto;
import server.dto.UserDto;
import server.model.User;

public interface UserService {
    UserDto checkClient(String name, String password);
    UserDto getUser(int id);
    UserDto addClient(String name, String password);
    StateDto checkName(String name);
}
