package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import server.entities.dto.FileModelDto;
import server.entities.dto.UserDto;
import server.services.FileService;
import server.services.MailConfirmationService;
import server.view.FileDownload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Controller
public class FilesController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MailConfirmationService mailService;

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse resp, HttpServletRequest req) {

        HttpSession session = req.getSession(false);
        UserDto userDto = (UserDto) session.getAttribute("user");

        FileModelDto dto = fileService.saveFile(file, userDto);
        return ResponseEntity.ok().body("success");

    }

    @RequestMapping(value = "/file_list", method = RequestMethod.POST)
    public ResponseEntity<String> getFileList() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(fileService.findAllFiles());
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("download");
        return modelAndView;
    }

    @RequestMapping(value = "/files/{file-name:.+}", method = RequestMethod.GET)
    public ModelAndView getFile(@PathVariable("file-name") String fileName) {
        fileName = fileName.split(":")[1];
        System.out.println(fileName);

        File file = fileService.downloadFile(fileName);


        return new ModelAndView(new FileDownload(), "downloadFile", file);
    }
}
