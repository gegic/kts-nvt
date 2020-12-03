package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
public class ReviewPhoto extends AbstractPhoto {
    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Review review;
}