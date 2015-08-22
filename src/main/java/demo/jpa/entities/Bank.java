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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    public String toString() {
        return String.format("%s-%s", id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        if (this.getId() != bank.getId()) return false;
        if (this.getName() != null ? !this.getName().equals(bank.getName()) : bank.getName() != null) return false;
        return !(this.getAddress() != null ? !this.getAddress().equals(bank.getAddress()) : bank.getAddress() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (this.getId() ^ this.getId() >>> 32);
        result = 31 * result + (this.getName() != null ? this.getName().hashCode() : 0);
        result = 31 * result + (this.getAddress() != null ? this.getAddress().hashCode() : 0);
        return result;
    }
}
