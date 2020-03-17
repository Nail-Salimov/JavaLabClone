package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.dto.client.UserDto;
import server.jwt.Tokenizer;
import server.models.client.User;
import server.repositories.UserRepositoryJdbcTemplate;

import java.util.Optional;

@Component()
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositoryJdbcTemplate repository;

    @Autowired
    private Tokenizer tokenizer;

    @Override
    public boolean save(String name, String password, String email) {
        if (repository.findByUsername(name).isPresent() || repository.findByEmail(email).isPresent()) {
            return false;
        } else {
            repository.deleteWithNameOrEmail(name, email);
            repository.save(new User(name, password, email));
            return true;
        }
    }

    @Override
    public Optional<UserDto> findUserById(Long id) {
        Optional<User> optionalUser = repository.find(id);
        return optionalUser.map(UserDto::getDto);
    }

    @Override
    public Optional<UserDto> findUserByUniqueData(String name, String password) {
        Optional<User> optionalUser = repository.findByUniqueData(name, password);
        return optionalUser.map(UserDto::getDto);
    }

    @Override
    public Optional<UserDto> findUserByName(String name) {
        Optional<User> optionalUser = repository.findByUsername(name);
        return  optionalUser.map(UserDto::getDto);
    }

    @Override
    public Optional<UserDto> findUserByNameNotConfirmed(String name) {
        Optional<User> optionalUser = repository.findByUsernameNotConfirmed(name);
        return  optionalUser.map(UserDto::getDto);
    }

    @Override
    public Optional<UserDto> findByNameAndEmailNotConfirmed(String name, String email){
        Optional<User> optionalUser = repository.findByNameAndEmailNotConfirmed(name, email);
        return optionalUser.map(UserDto::getDto);
    }

    @Override
    public boolean delete(Long id) {
        return repository.find(id).isPresent();
    }

    @Override
    public String getToken(Long id, String name, String mail) {
       return tokenizer.getToken(id, name, mail);
    }

    @Override
    public boolean checkMailToken(String token, Long id, String name, String mail) {
        return tokenizer.checkClient(token, id, name, mail);
    }

    @Override
    public void confirmed(String email) {
        repository.confirmUser(email);
    }

    @Override
    public void addToken(String name, String token) {
        repository.addToken(name, token);
    }
}
