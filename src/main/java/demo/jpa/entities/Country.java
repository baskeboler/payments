package demo.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by victor on 8/18/15.
 */
@Entity
public class Country {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ getId() >>> 32);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (getId() != country.getId()) return false;
        return !(this.getName() != null ? !this.getName().equals(country.getName()) : country.getName() != null);

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
