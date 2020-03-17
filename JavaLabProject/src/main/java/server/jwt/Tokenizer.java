package server.jwt;

public interface Tokenizer {
    String getToken(Long id, String username, String email);
    boolean checkClient(String token, Long id, String username, String email);
}
