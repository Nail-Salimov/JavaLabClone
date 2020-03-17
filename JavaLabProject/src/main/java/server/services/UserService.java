package server.services;

import server.dto.client.UserDto;

import java.util.Optional;

public interface UserService {
    boolean save(String name, String password, String email);

    Optional<UserDto> findUserById(Long id);

    Optional<UserDto> findUserByUniqueData(String name, String password);

    Optional<UserDto> findUserByName(String name);
    Optional<UserDto> findUserByNameNotConfirmed(String name);


    boolean delete(Long id);

    String getToken(Long id, String name, String mail);

    boolean checkMailToken(String token, Long id, String name, String mail);

    void confirmed(String email);

    void addToken(String name, String token);

    Optional<UserDto> findByNameAndEmailNotConfirmed(String name, String email);


}
