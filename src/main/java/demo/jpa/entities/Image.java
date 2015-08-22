package demo.jpa.entities;

import javax.persistence.*;

/**
 * Created by victor on 8/21/15.
 */
@Entity
public class Image {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @Lob
    @Column(length = 20971520)
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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
