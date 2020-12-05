package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PhotoRepository;

import javax.imageio.ImageIO;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;


@Service
public class CulturalOfferingPhotoService {


    private final PhotoRepository photoRepository;
    private final Mapper mapper;

    @Autowired
    public CulturalOfferingPhotoService(PhotoRepository photoRepository, Mapper mapper) {
        this.photoRepository = photoRepository;
        this.mapper = mapper;
    }

    public Page<CulturalOfferingPhotoDto> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return photoRepository.findAllByCulturalOfferingId(culturalOfferingId, p)
                .map(culturalOfferingPhoto -> mapper.fromEntity(culturalOfferingPhoto, CulturalOfferingPhotoDto.class));
    }

    public Optional<CulturalOfferingPhotoDto> readById(long id) {
        return photoRepository.findById(id).map(culturalOfferingPhoto -> mapper.fromEntity(culturalOfferingPhoto, CulturalOfferingPhotoDto.class));
    }

    @Transactional
    public CulturalOfferingPhotoDto create(MultipartFile photoFile) {
        CulturalOfferingPhoto p = new CulturalOfferingPhoto();

        return save(photoFile, p);
    }

    @Transactional
    public CulturalOfferingPhotoDto update(MultipartFile photoFile, long id) {
        CulturalOfferingPhoto p = photoRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return save(photoFile, p);
    }

    @Transactional
    public CulturalOfferingPhotoDto save(MultipartFile photoFile, CulturalOfferingPhoto p) {
        BufferedImage bufferedImage;
        BufferedImage thumbnail;
        try {
            bufferedImage = ImageIO.read(photoFile.getInputStream());
//            thumbnail = (BufferedImage) ImageIO.read(photoFile.getInputStream())
//                    .getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);

        } catch (IOException e) {
            return null;
        }

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        p.setWidth(width);
        p.setHeight(height);

        p = photoRepository.save(p);



        try {
            savePhoto(".\\cultural_offering_photos", bufferedImage, p);
//            savePhoto(".\\cultural_offering_photos\\thumbnails", thumbnail, p);
        } catch (Exception ex) {
            photoRepository.delete(p);
            System.out.println("Exception:" + ex);
        }

        return mapper.fromEntity(p, CulturalOfferingPhotoDto.class);
    }

    private void savePhoto(String path, BufferedImage bufferedImage, CulturalOfferingPhoto p) throws IOException {
        Path fileStorageLocation = Paths.get(path)
                .toAbsolutePath().normalize();
        Path targetLocation = fileStorageLocation.resolve(String.format("%d.png", p.getId()));
        File output = new File(targetLocation.toString());
        ImageIO.write(bufferedImage, "png", output);
    }

    public void delete(long id) {
        photoRepository.deleteById(id);
    }
}
