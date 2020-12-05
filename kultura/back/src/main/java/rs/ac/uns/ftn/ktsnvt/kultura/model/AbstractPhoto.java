package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPhoto {
    @Id
    @Getter
    @Setter
    protected long id;
    @Getter
    @Setter
    protected int width;
    @Getter
    @Setter
    protected int height;
    @Getter
    @Setter
    protected LocalDateTime timeAdded = LocalDateTime.now();

    public AbstractPhoto(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
