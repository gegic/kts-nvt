package rs.ac.uns.ftn.ktsnvt.kultura.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingMainPhotoRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CulturalOfferingMainPhotoService {

    private CulturalOfferingMainPhotoRepository repository;
    private Mapper mapper;
    @Autowired
    public CulturalOfferingMainPhotoService(CulturalOfferingMainPhotoRepository repository,
                                            Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CulturalOfferingPhotoDto addPhoto(MultipartFile photoFile) {
        CulturalOfferingMainPhoto photo = new CulturalOfferingMainPhoto();
        BufferedImage bufferedImage;
        BufferedImage thumbnail;

        try {
            bufferedImage = Thumbnails.of(photoFile.getInputStream()).size(1000, 1000).asBufferedImage();
            thumbnail = Thumbnails.of(photoFile.getInputStream()).size(200, 200).asBufferedImage();
        } catch (IOException e) {
            return null;
        }
        String token;
        try {
            token = ((String) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        } catch (NullPointerException e) {
            token = "";
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        photo.setWidth(width);
        photo.setHeight(height);
        photo.setToken(token);

        photo = repository.save(photo);

        try {
            savePhoto("/main", bufferedImage, photo);
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

    @Transactional
    public void clearPhotos() {
        String token;
        try {
            token = ((String) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        } catch (NullPointerException e) {
            token = "";
        }
        List<CulturalOfferingMainPhoto> photos = repository.getNullOffering(token);
        photos.parallelStream().map(p -> new File("./photos/main/thumbnail/" + p.getId() + ".png"))
                .forEach(File::delete);
        photos.parallelStream().map(p -> new File("./photos/main/" + p.getId() + ".png"))
                .forEach(File::delete);
        repository.deleteAll(photos);
    }

    @Transactional
    public void deletePhoto(CulturalOfferingMainPhoto photo) {

        if (photo!=null){
            new File("./photos/main/thumbnail/" + photo.getId() + ".png").delete();
            new File("./photos/main/" + photo.getId() + ".png").delete();

            repository.delete(photo);
        }
    }
    
}
