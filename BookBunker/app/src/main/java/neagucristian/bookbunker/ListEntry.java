package neagucristian.bookbunker;


import java.util.List;

public class ListEntry {
    private String title;
    private String author;
    private int id;
    private int rating;
    private String comment;

    public ListEntry(int id, String title, String author, int rating) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.rating = rating;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public int getRating() {
        return rating;
    }
}
