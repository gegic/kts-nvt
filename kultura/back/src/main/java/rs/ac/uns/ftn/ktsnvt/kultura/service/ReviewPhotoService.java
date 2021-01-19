package rs.ac.uns.ftn.ktsnvt.kultura.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.ReviewPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewPhotoRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewPhotoService {

    private final ReviewPhotoRepository repository;
    private final Mapper mapper;
    private final PhotosConfig photosConfig;
    @Autowired
    public ReviewPhotoService(ReviewPhotoRepository repository,
                              Mapper mapper,
                              PhotosConfig photosConfig) {
        this.repository = repository;
        this.mapper = mapper;
        this.photosConfig = photosConfig;
    }

    public List<ReviewPhotoDto> addPhotos(MultipartFile[] photoFile) {
        List<ReviewPhotoDto> photoDtos = new ArrayList<>();
        for (MultipartFile multipartFile : photoFile) {
            photoDtos.add(this.addPhoto(multipartFile));
        }
        return photoDtos;
    }

    private ReviewPhotoDto addPhoto(MultipartFile photoFile) {
        ReviewPhoto photo = new ReviewPhoto();
        BufferedImage bufferedImage;
        BufferedImage thumbnail;

        try {
            bufferedImage = Thumbnails.of(photoFile.getInputStream()).size(700, 700).asBufferedImage();
            thumbnail = Thumbnails.of(photoFile.getInputStream()).size(100, 100).asBufferedImage();
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
            savePhoto(photosConfig.getPath() + "review", bufferedImage, photo);
            savePhoto(photosConfig.getPath() + "review/thumbnail", thumbnail, photo);
        } catch (IOException e) {
            repository.delete(photo);
            System.out.println("Exception:" + e);
        }
        return mapper.fromEntity(photo, ReviewPhotoDto.class);

    }

    private void savePhoto(String path,
                           BufferedImage bufferedImage,
                           ReviewPhoto p) throws IOException {
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
        List<ReviewPhoto> photos = repository.getNullReview(token);
        photos.parallelStream().map(p -> new File(photosConfig.getPath() + "review/thumbnail/" + p.getId() + ".png"))
                .forEach(File::delete);
        photos.parallelStream().map(p -> new File(photosConfig.getPath() + "review/" + p.getId() + ".png"))
                .forEach(File::delete);

        repository.deleteAll(photos);
    }

    @Transactional
    public void deleteForReview(long reviewId) {
        List<ReviewPhoto> photos = repository.getAllByReviewId(reviewId);
        photos.parallelStream().map(p -> new File(photosConfig.getPath() + "review/thumbnail/" + p.getId() + ".png"))
                .forEach(File::delete);
        photos.parallelStream().map(p -> new File(photosConfig.getPath() + "review/" + p.getId() + ".png"))
                .forEach(File::delete);

        repository.deleteAll(photos);
    }
    
}
