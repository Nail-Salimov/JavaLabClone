package server.services;

import server.dto.ReplyPayload;
import server.dto.TokenPayload;
import server.dto.UserDto;
import server.jwt.Tokenizer;
import server.models.User;
import server.repositories.GeneralRepository;

import java.util.Optional;

public class GeneralServiceImpl implements GeneralService {

    private GeneralRepository generalRepository;

    public GeneralServiceImpl(GeneralRepository generalRepository) {
        this.generalRepository = generalRepository;
    }

    public GeneralServiceImpl(){};

    @Override
    public ReplyPayload checkClient(Long id, String password) {
        if (generalRepository.checkClient(id, password)) {
            return ReplyPayload.getPayload("pass", getUser(id).getRole());

        } else {
            return ReplyPayload.getPayload("refuse", "wrong data");
        }
    }

    @Override
    public UserDto getUser(Long id) {
        Optional<User> optionalUser = generalRepository.getUser(id);

        return optionalUser.map(UserDto::from).orElse(null);
    }

    @Override
    public ReplyPayload checkToken(String token, Long id, String role) {
        if (Tokenizer.checkClient(token, id, role)) {
            return ReplyPayload.getPayload("pass", "right token");

        } else {
            return ReplyPayload.getPayload("refuse", "bad token");
        }
    }

    @Override
    public TokenPayload createToken(Long id, String role) {
        String token = Tokenizer.getToken(id, role);
        return TokenPayload.getPayload(token);
    }

    @Override
    public ReplyPayload disconnect(){
        return ReplyPayload.getPayload("logout", "GoodBye");
    }

}
