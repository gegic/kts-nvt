package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CulturalOfferingPhoto extends AbstractPhoto{
    @Getter
    @Setter
    @OneToOne
    private CulturalOffering culturalOffering;
}
