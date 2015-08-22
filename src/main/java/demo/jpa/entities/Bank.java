package demo.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by victor on 8/18/15.
 */
@Entity
public class Bank {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String address;

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ getId() >>> 32);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        if (getId() != bank.getId()) return false;
        if (getName() != null ? !getName().equals(bank.getName()) : bank.getName() != null) return false;
        return !(getAddress() != null ? !getAddress().equals(bank.getAddress()) : bank.getAddress() != null);

    }

    @Override
    public String toString() {
        return String.format("%s-%s", this.id, this.name);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }
}
