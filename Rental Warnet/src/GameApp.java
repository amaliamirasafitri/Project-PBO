public class GameApp {

    public final String title;
    public final String genre;
    public final String year;
    public final String developer;
    public final String description;
    public final String imagePath;
    public final String imdbUrl;

    public GameApp(String title,
                   String genre,
                   String year,
                   String developer,
                   String description,
                   String imagePath,
                   String imdbUrl) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.developer = developer;
        this.description = description;
        this.imagePath = imagePath;
        this.imdbUrl = imdbUrl;
    }
}