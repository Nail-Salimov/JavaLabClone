package server.State;

import server.dto.UserDto;

public class ClientState {
    private String role;
    private boolean enter;

    public ClientState() {
        this.enter = false;
    }

    public static ClientState asClientState(UserDto userDto) {
        ClientState state = new ClientState();
        state.enter = true;
        state.role = userDto.getRole();
        return state;
    }

    public void readData(UserDto userDto){
        role = userDto.getRole();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }
}
