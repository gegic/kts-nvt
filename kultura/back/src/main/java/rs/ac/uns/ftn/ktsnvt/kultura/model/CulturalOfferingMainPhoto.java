package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CulturalOfferingMainPhoto extends AbstractPhoto{
    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    private CulturalOffering culturalOffering;
    @Getter
    @Setter
    String token;


    public long getId() {
        return this.id;
    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public void setId(Long id) {
        this.id = id;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public LocalDateTime getTimeAdded() {
        return this.timeAdded;
    }

    public void setTimeAdded(LocalDateTime timeAdded) {
        this.timeAdded = timeAdded;
    }
}
