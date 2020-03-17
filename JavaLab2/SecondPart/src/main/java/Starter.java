import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Starter {
    @Parameter(names = "--files", description = "link to picture")
    private String urlPictures;


    public void start() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("db.properties"));
        } catch (IOException e) {
            throw  new IllegalStateException(e);
        }
        String name = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String url = properties.getProperty("db.url");

        ImageDownloader imageDownloader = ImageDownloader.create(name, password, url);
        String[] picture = urlPictures.split("#");
        for (String i : picture) {
            imageDownloader.load(i);
        }
    }
}
