package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
public class ReviewPhoto extends AbstractPhoto {
    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Review review;
}