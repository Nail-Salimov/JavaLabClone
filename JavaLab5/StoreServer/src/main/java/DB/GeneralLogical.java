package DB;

public interface GeneralLogical {
    boolean checkClient(int id, String password);
    String getRole(int id);
}
