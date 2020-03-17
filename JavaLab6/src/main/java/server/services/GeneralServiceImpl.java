package server.services;

import server.context.Component;
import server.dto.ReplyPayload;
import server.dto.TokenPayload;
import server.dto.UserDto;
import server.jwt.Tokenizer;
import server.models.User;
import server.repositories.GeneralRepository;

import java.util.Optional;

public class GeneralServiceImpl implements GeneralService, Component {

    private GeneralRepository generalRepository;

    public GeneralServiceImpl(GeneralRepository generalRepository) {
        this.generalRepository = generalRepository;
    }

    public GeneralServiceImpl(){};

    @Override
    public ReplyPayload checkClient(int id, String password) {
        if (generalRepository.checkClient(id, password)) {
            return ReplyPayload.getPayload("pass", getUser(id).getRole());

        } else {
            return ReplyPayload.getPayload("refuse", "wrong data");
        }
    }

    @Override
    public UserDto getUser(int id) {
        Optional<User> optionalUser = generalRepository.getUser(id);

        return optionalUser.map(UserDto::from).orElse(null);
    }

    @Override
    public ReplyPayload checkToken(String token, int id, String role) {
        if (Tokenizer.checkClient(token, id, role)) {
            return ReplyPayload.getPayload("pass", "right token");

        } else {
            return ReplyPayload.getPayload("refuse", "bad token");
        }
    }

    @Override
    public TokenPayload createToken(int id, String role) {
        String token = Tokenizer.getToken(id, role);
        return TokenPayload.getPayload(token);
    }

    @Override
    public ReplyPayload disconnect(){
        return ReplyPayload.getPayload("logout", "GoodBye");
    }

    @Override
    public String getName() {
        return  "GeneralService";
    }

    public GeneralRepository getGeneralRepository() {
        return generalRepository;
    }

    public void setGeneralRepository(GeneralRepository generalRepository) {
        this.generalRepository = generalRepository;
    }

    public static GeneralServiceImpl newFormation(){
        return new GeneralServiceImpl();
    }
}
