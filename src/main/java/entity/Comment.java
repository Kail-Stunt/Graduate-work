
package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    private String text;

    private LocalDateTime createdAt;

    public Comment() {}

    public Comment(Integer pk, User author, Ad ad, String text, LocalDateTime createdAt) {
        this.pk = pk;
        this.author = author;
        this.ad = ad;
        this.text = text;
        this.createdAt = createdAt;
    }

    public static Comment builder() {
        return new Comment();
    }

    public Comment withPk(Integer pk) { this.pk = pk; return this; }
    public Comment withAuthor(User author) { this.author = author; return this; }
    public Comment withAd(Ad ad) { this.ad = ad; return this; }
    public Comment withText(String text) { this.text = text; return this; }
    public Comment withCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

    public Integer getPk() { return pk; }
    public void setPk(Integer pk) { this.pk = pk; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Ad getAd() { return ad; }
    public void setAd(Ad ad) { this.ad = ad; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
