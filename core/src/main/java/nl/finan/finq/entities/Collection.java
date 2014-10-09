package nl.finan.finq.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FINQ_COLLECTION")
public class Collection extends GenericEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "FINQ_COLLECTION_SET",
            joinColumns = {@JoinColumn(name = "BUNDLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "COLLECTION_ID", referencedColumnName = "ID")})
    private List<Book> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
