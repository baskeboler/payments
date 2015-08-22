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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (this.getId() != country.getId()) return false;
        return !(getName() != null ? !getName().equals(country.getName()) : country.getName() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (this.getId() ^ this.getId() >>> 32);
        result = 31 * result + (this.getName() != null ? this.getName().hashCode() : 0);
        return result;
    }
}
