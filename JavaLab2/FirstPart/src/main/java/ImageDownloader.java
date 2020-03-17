public class ImageDownloader {

    private String name;
    private String password;
    private String url;
    private ThreadPool threadPool;
    private static BdLogics bdLogics;
    private static ImageDownloader load;

    private ImageDownloader(String name, String password, String url) {
        this.name = name;
        this.password = password;
        this.url = url;
        threadPool = new ThreadPool(5);
    }

    public static ImageDownloader create(String name, String password, String url) {
        load = new ImageDownloader(name, password, url);
        bdLogics = new BdLogics(name, password, url);
        return load;

    }

    public void load(String imageUrl) {
        threadPool.submit(new Work(imageUrl, "/home/nail/Изображения/", bdLogics));
    }
}
