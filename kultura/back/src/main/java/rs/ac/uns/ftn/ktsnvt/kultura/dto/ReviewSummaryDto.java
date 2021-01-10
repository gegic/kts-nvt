package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
public class ReviewSummaryDto {
    @Getter
    @Setter
    Map<Integer, Long> ratings;

    @Getter
    @Setter
    Long ratingsSize;

    @Getter
    @Setter
    Float rating;
}
