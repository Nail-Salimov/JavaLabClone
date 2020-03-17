package server.dto;

import server.models.User;

public class UserDto implements Dto {

    private Long id;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static UserDto from(User user) {
       return new UserDto.Builder()
               .withId(user.getId())
               .withRole(user.getRole())
               .build();
    }


    public static class Builder {

        private UserDto newUserDto;

        public Builder() {
            newUserDto = new UserDto();
        }

        public Builder withId(Long id) {
            newUserDto.id = id;
            return this;
        }

        public Builder withRole(String role) {
            newUserDto.role = role;
            return this;
        }

        public UserDto build() {
            return newUserDto;
        }
    }
}
