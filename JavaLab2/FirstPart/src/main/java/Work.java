import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Work implements Runnable {
    private BdLogics bdLogics;
    private String urlPicture;
    private String path;

    public Work(String urlPicture, String path, BdLogics bdLogics) {
        this.urlPicture = urlPicture;
        this.bdLogics = bdLogics;
        this.path = path;
    }

    @Override
    public void run() {
        try (InputStream in = new URL(urlPicture).openStream()) {
            String[] split = urlPicture.split("/");
            String namePicture = split[split.length - 1];
            long size = Files.copy(in, Paths.get(path + namePicture));  ///home/nail/Изображения/
            saveInfo(namePicture, size);
            System.out.println(urlPicture + " downloaded " + path);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void saveInfo(String name, long size){
        bdLogics.save(name, size);
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
