package demo.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by victor on 8/18/15.
 */
@Entity
public class City {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToOne(optional = false)
    private Country country;

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

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

        City city = (City) o;

        if (this.getId() != city.getId()) return false;
        if (!this.getName().equals(city.getName())) return false;
        return getCountry().equals(city.getCountry());

    }

    @Override
    public int hashCode() {
        int result = (int) (this.getId() ^ getId() >>> 32);
        result = 31 * result + this.getName().hashCode();
        result = 31 * result + this.getCountry().hashCode();
        return result;
    }
}
