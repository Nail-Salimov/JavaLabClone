import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;


public class Starter {
    @Parameter(names = "--files", description = "link to picture")
    private String urlPictures;

    @Parameter(names = "--folder", description = "path pictures")
    private String path;

    @Parameter(names = "--mode", description = "multi or single threat")
    private String mode;

    @Parameter(names = "--count", description = "count of threats")
    private int count = 1;

    public String getPath() {
        return path;
    }

    public String getMode() {
        return mode;
    }

    public int getCount() {
        return count;
    }

    public void start() {
        if ((mode.equals("one-thread")) && (count > 1)) {
            throw new ParameterException("One-threat mod cannot have parameter of the number of threads");
        }
        ThreadPool threadPool = new ThreadPool(count);
        String[] picture = urlPictures.split("#");
        for (String i : picture) {
            threadPool.submit(new Work(i, path));
        }
    }
}

