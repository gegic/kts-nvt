package rs.ac.uns.ftn.ktsnvt.kultura.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Service
public class CulturalOfferingPhotoService {


    private final CulturalOfferingPhotoRepository culturalOfferingPhotoRepository;
    private final CulturalOfferingRepository culturalOfferingRepository;
    private final Mapper mapper;
    private final PhotosConfig photosConfig;

    @Autowired
    public CulturalOfferingPhotoService(CulturalOfferingPhotoRepository culturalOfferingPhotoRepository,
                                        Mapper mapper,
                                        CulturalOfferingRepository culturalOfferingRepository,
                                        PhotosConfig photosConfig) {
        this.culturalOfferingPhotoRepository = culturalOfferingPhotoRepository;
        this.culturalOfferingRepository = culturalOfferingRepository;
        this.mapper = mapper;
        this.photosConfig = photosConfig;
    }

    public Page<CulturalOfferingPhotoDto> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return culturalOfferingPhotoRepository.findAllByCulturalOfferingId(culturalOfferingId, p)
                .map(culturalOfferingPhoto -> mapper.fromEntity(culturalOfferingPhoto, CulturalOfferingPhotoDto.class));
    }

    public Optional<CulturalOfferingPhotoDto> readById(long id) {
        return culturalOfferingPhotoRepository.findById(id).map(culturalOfferingPhoto -> mapper.fromEntity(culturalOfferingPhoto, CulturalOfferingPhotoDto.class));
    }

    @Transactional
    public CulturalOfferingPhotoDto create(MultipartFile photoFile, long culturalOfferingId) {
        CulturalOfferingPhoto photo = new CulturalOfferingPhoto();

        CulturalOffering culturalOffering = culturalOfferingRepository.findById(culturalOfferingId)
                .orElseThrow(() -> new ResourceNotFoundException("Cultural offering with given id doesn't exist"));

        BufferedImage bufferedImage;
        BufferedImage thumbnail;

        try {
            bufferedImage = Thumbnails.of(photoFile.getInputStream()).size(1000, 1000).asBufferedImage();
            thumbnail = Thumbnails.of(photoFile.getInputStream()).size(200, 200).asBufferedImage();
        } catch (IOException e) {
            return null;
        }

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        photo.setWidth(width);
        photo.setHeight(height);
        photo.setCulturalOffering(culturalOffering);

        photo = culturalOfferingPhotoRepository.save(photo);

        try {
            savePhoto(photosConfig.getPath(), bufferedImage, photo);
            savePhoto(photosConfig.getPath() + "thumbnail", thumbnail, photo);
        } catch (IOException e) {
            culturalOfferingPhotoRepository.delete(photo);
            System.out.println("Exception:" + e);
        }
        return mapper.fromEntity(photo, CulturalOfferingPhotoDto.class);
    }

    private void savePhoto(String path, BufferedImage bufferedImage, CulturalOfferingPhoto p) throws IOException {
        Path fileStorageLocation = Paths.get(path)
                .toAbsolutePath().normalize();
        Path targetLocation = fileStorageLocation.resolve(String.format("%d.png", p.getId()));
        File output = new File(targetLocation.toString());
        ImageIO.write(bufferedImage, "png", output);
    }

    public void delete(long id) {
        if (!culturalOfferingRepository.existsById(id)) {
            throw new ResourceNotFoundException("A photo with the given id " + id + " was not found.");
        }
        File photo = new File(photosConfig.getPath() + id + ".png");
        File thumbnail = new File(photosConfig.getPath() + "thumbnail/" + id + ".png");
        photo.delete();
        thumbnail.delete();
        culturalOfferingPhotoRepository.deleteById(id);
    }

    public void deleteByCulturalOffering(long culturalOfferingId) {
        List<CulturalOfferingPhoto> photos = culturalOfferingPhotoRepository.findAllByCulturalOfferingId(culturalOfferingId);
        for (CulturalOfferingPhoto photo : photos) {
            new File(photosConfig.getPath() + photo.getId() + ".png").delete();
            new File(photosConfig.getPath() + "thumbnail" + photo.getId() + ".png").delete();
        }

        culturalOfferingPhotoRepository.deleteAll(photos);
    }
}
