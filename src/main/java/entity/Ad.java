
package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ad")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private String image;

    private Integer price;

    private String title;

    private String description;

    public Ad() {}

    public Ad(Integer pk, User author, String image, Integer price, String title, String description) {
        this.pk = pk;
        this.author = author;
        this.image = image;
        this.price = price;
        this.title = title;
        this.description = description;
    }

    public static Ad builder() {
        return new Ad();
    }

    public Ad withPk(Integer pk) { this.pk = pk; return this; }
    public Ad withAuthor(User author) { this.author = author; return this; }
    public Ad withImage(String image) { this.image = image; return this; }
    public Ad withPrice(Integer price) { this.price = price; return this; }
    public Ad withTitle(String title) { this.title = title; return this; }
    public Ad withDescription(String description) { this.description = description; return this; }

    public Integer getPk() { return pk; }
    public void setPk(Integer pk) { this.pk = pk; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
