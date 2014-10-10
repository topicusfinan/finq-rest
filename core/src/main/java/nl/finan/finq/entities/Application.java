package nl.finan.finq.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FINQ_APPLICATION")
public class Application extends GenericEntity {

    @Column(name = "TITLE")
    private String title;

    @Column(name="SUBJECT")
    private String subject;

    @Column(name="AUTHENTICATE")
    private Boolean authenticate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(Boolean authenticate) {
        this.authenticate = authenticate;
    }
}
