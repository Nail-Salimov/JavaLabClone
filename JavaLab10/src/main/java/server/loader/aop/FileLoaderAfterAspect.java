package server.loader.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.entities.dto.UserDto;
import server.entities.model.FileModel;
import server.services.MailConfirmationService;

@Aspect
@Component
public class FileLoaderAfterAspect {

    @Autowired
    private MailConfirmationService mailService;

    @AfterReturning(value = "execution(* server.loader.FileLoader.uploadFile(..))", returning = "result")
    public void sendEmail(JoinPoint point, Object result) {
        if (result != null) {
            UserDto userDto = (UserDto) point.getArgs()[1];
            FileModel fileModel = (FileModel) result;

            System.out.println(userDto.getMail());
            mailService.send("Ваш файл", "pologies12314s@mail.ru", userDto.getMail(), fileModel.getStorageName(), fileModel.getOriginalName());
        }
    }
}
