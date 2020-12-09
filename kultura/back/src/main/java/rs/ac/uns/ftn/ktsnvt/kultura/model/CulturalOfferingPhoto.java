package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
public class CulturalOfferingPhoto extends AbstractPhoto {

    public CulturalOfferingPhoto(int width, int height) {
        super(width, height);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private CulturalOffering culturalOffering;

    public long getId() {
        return this.id;
    }

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

}