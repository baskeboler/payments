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

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ this.getId() >>> 32);
        result = 31 * result + getName().hashCode();
        result = 31 * result + getCountry().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (getId() != city.getId()) return false;
        if (!getName().equals(city.getName())) return false;
        return this.getCountry().equals(city.getCountry());

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }
}
