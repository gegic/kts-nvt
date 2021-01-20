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

    public LocalDateTime getTimeAdded() {
        return this.timeAdded;
    }

    public void setTimeAdded(LocalDateTime timeAdded) {
        this.timeAdded = timeAdded;
    }

    public void setCulturalOffering(CulturalOffering culturalOffering) {
        if (this.culturalOffering != null) { this.culturalOffering.internalAddCulturalOfferingPhoto(this); }
        this.culturalOffering = culturalOffering;
        if (culturalOffering != null) { culturalOffering.internalRemoveCulturalOfferingPhoto(this); }
    }
}
