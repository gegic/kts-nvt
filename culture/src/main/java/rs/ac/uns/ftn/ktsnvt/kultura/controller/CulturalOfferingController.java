package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/cultural-offering/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingController {

    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CulturalOffering> getAll(@RequestBody Pageable p) {
        // TODO videcemo da li slat cijelu stranu DTOa ili radit neki dto od svega ovoga :D

        return culturalOfferingService.readAll(p);

    }

}
