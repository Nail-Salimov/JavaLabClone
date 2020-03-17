package server.constructor.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailCreatorImpl implements EmailCreator {

    @Override
    public String createEmail(String... args) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setDirectoryForTemplateLoading(new File(
                    "/home/nail/Progy/JavaLab/JavaLab10/src/main/webapp/templates/"));
            Template template = cfg.getTemplate("message.ftl");

            Map<String, Object> root = new HashMap<>();

            root.put("storageName", args[0]);
            root.put("originalName", args[1]);

            Writer out = new StringWriter();
            template.process(root, out);

            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
