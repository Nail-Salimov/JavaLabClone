package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import server.entities.dto.UserDto;
import server.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@SessionAttributes(value = "user")
@Controller
public class SignInController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method =  RequestMethod.GET)
    public ModelAndView view() {
        System.out.println("asdsadsd");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signIn");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method =  RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("username") String name,
                                   @RequestParam("password") String password, HttpServletRequest req, HttpServletResponse resp) {

        Optional<UserDto> optionalUserDto = userService.findUserByUniqueData(name, password);
        if(optionalUserDto.isPresent()){
            UserDto userDto = optionalUserDto.get();

            HttpSession session = req.getSession();
            session.setAttribute("user", userDto);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(userDto);
            modelAndView.setViewName("download");
            return modelAndView;
        }else {
            return view();
        }
    }
}
