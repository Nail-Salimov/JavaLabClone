package server.constructor.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailCreatorImpl implements EmailCreator {

    @Override
    public String createEmail(String... args) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setDirectoryForTemplateLoading(new File(
                    "/home/nail/Progy/JavaLab/JavaLabProject/src/main/webapp/templates/"));
            Template template = cfg.getTemplate("message.ftl");


            Map<String, Object> root = new HashMap<>();


            root.put("name", args[0]);
            root.put("mail", args[1]);
            root.put("t", args[2]);
            root.put("u", args[3]);

            Writer out = new StringWriter();
            template.process(root, out);

            return out.toString();
        } catch (IOException | TemplateException e){
           throw  new IllegalArgumentException(e);
        }

    }
}
