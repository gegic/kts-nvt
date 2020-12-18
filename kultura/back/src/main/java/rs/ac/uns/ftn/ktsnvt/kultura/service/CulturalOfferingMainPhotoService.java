package rs.ac.uns.ftn.ktsnvt.kultura.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingMainPhotoRepository;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CulturalOfferingMainPhotoService {

    private ServletContext servletContext;
    private CulturalOfferingMainPhotoRepository repository;
    private Mapper mapper;
    @Autowired
    public CulturalOfferingMainPhotoService(CulturalOfferingMainPhotoRepository repository,
                                            Mapper mapper,
                                            ServletContext servletContext) {
        this.repository = repository;
        this.mapper = mapper;
        this.servletContext = servletContext;
    }

    public CulturalOfferingPhotoDto addPhoto(MultipartFile photoFile) {
        CulturalOfferingMainPhoto photo = new CulturalOfferingMainPhoto();
        BufferedImage bufferedImage;
        BufferedImage thumbnail;

        try {
            bufferedImage = ImageIO.read(photoFile.getInputStream());
            thumbnail = Thumbnails.of(bufferedImage).size(150, 150).asBufferedImage();
        } catch (IOException e) {
            return null;
        }

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        photo.setWidth(width);
        photo.setHeight(height);

        photo = repository.save(photo);

        try {
            savePhoto("./photos/main", bufferedImage, photo);
            savePhoto("./photos/main/thumbnail", thumbnail, photo);
        } catch (IOException e) {
            repository.delete(photo);
            System.out.println("Exception:" + e);
        }
        return mapper.fromEntity(photo, CulturalOfferingPhotoDto.class);
    }

    private void savePhoto(String path,
                           BufferedImage bufferedImage,
                           CulturalOfferingMainPhoto p) throws IOException {
        Path fileStorageLocation = Paths.get(path)
                .toAbsolutePath().normalize();
        Path targetLocation = fileStorageLocation.resolve(String.format("%d.png", p.getId()));
        File output = new File(targetLocation.toString());
        ImageIO.write(bufferedImage, "png", output);
    }

    public void clearPhotos() {
        repository.deleteNullOffering();
    }
    
}
