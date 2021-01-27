import com.github.javafaker.Faker;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class DataGenerator {
    public static final int MAX_OFFERINGS = 300;
    public static final int MAX_USERS = 100;
    public static final int MAX_CATEGORIES = 10;
    public static final int MAX_SUBCATEGORIES = 20;
    public static final int MAX_POSTS = 60;

    private long categoriesNumber = 0;
    private long subcategoriesNumber = 0;
    private long culturalOfferingsNumber = 0;
    private long usersNumber = 0;

    private void insertAuthorities(FileWriter writer) throws IOException {
        System.out.println("Inserting authorities...");
        writer.append(new Authority("ROLE_ADMIN").toString());
        writer.append(new Authority("ROLE_MODERATOR").toString());
        writer.append(new Authority("ROLE_USER").toString());
    }

    private void insertCategories(FileWriter writer, Faker f) throws IOException {
        System.out.println("Inserting categories...");
        for(int i = 0; i < MAX_CATEGORIES; ++i) {
            writer.append(new Category(f.lorem().sentence(2)).toString());
            categoriesNumber++;
        }
    }

    private void insertSubcategories(FileWriter writer, Faker f) throws IOException {
        System.out.println("Inserting subcategories...");
        for(int i = 0; i < MAX_SUBCATEGORIES; ++i) {
            long categoryId = f.number().numberBetween(1, categoriesNumber + 1);
            writer.append(new Subcategory(f.lorem().sentence(3), categoryId).toString());
            subcategoriesNumber++;
        }
    }

    private void addMainPhotos() throws IOException {
        System.out.println("Adding main photo files...");
        File f = new File("photo_sources");
        if(!f.isDirectory()) {
            return;
        }
        File[] photoFiles = f.listFiles();
        int numPhotos = Objects.requireNonNull(photoFiles).length;
        LongStream.range(0, culturalOfferingsNumber).parallel().forEach(i -> {
            int photoIndex = ThreadLocalRandom.current().nextInt(numPhotos);
            File photoFile = Objects.requireNonNull(photoFiles)[photoIndex];
            if (!photoFile.exists()) {
                return;
            }
            BufferedImage bufferedImage;
            BufferedImage thumbnail;

            try {
                bufferedImage = Thumbnails.of(new FileInputStream(photoFile)).size(1000, 1000).asBufferedImage();
                thumbnail = Thumbnails.of(new FileInputStream(photoFile)).size(200, 200).asBufferedImage();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try {
                savePhoto( "../back/photos/main", bufferedImage, ((int) i + 1));
                savePhoto("../back/photos/main/thumbnail", thumbnail, ((int) i + 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void savePhoto(String path,
                           BufferedImage bufferedImage,
                           int id) throws IOException {
        Path fileStorageLocation = Paths.get(path)
                .toAbsolutePath().normalize();
        if(Files.notExists(fileStorageLocation)) {
            System.out.println("Location nonexistent.");
            Files.createDirectories(fileStorageLocation);
        }
        Path targetLocation = fileStorageLocation.resolve(String.format("%d.png", id));
        File output = new File(targetLocation.toString());
        ImageIO.write(bufferedImage, "png", output);
    }

    private void insertMainData(FileWriter writer, Faker f) throws IOException {
        List<User> users = new ArrayList<>();
        List<UserAuthority> userAuthorities = new ArrayList<>();
        List<CulturalOffering> culturalOfferings = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();
        users.add(new User("admin@mail.com", true, "Admin", "Adminic"));
        userAuthorities.add(new UserAuthority(1, 1));
        for(int i = 1; i < MAX_USERS; ++i) {
            users.add(new User(f.internet().emailAddress(), f.bool().bool(), f.name().firstName(), f.name().lastName()));
            long authority = 3;
            if (f.number().numberBetween(0, 10) == 0) {
                authority = 2;
            }
            userAuthorities.add(new UserAuthority(i + 1, authority));
            usersNumber++;
        }
        for(int i = 0; i < MAX_OFFERINGS; ++i) {
            culturalOfferings.add(new CulturalOffering(
                    i + 1,
                    f.book().title(),
                    f.address().fullAddress(),
                    f.lorem().sentence(5),
                    f.number().randomDouble(13, 19, 22),
                    f.number().randomDouble(13, 42, 45),
                    f.number().numberBetween(1, subcategoriesNumber + 1),
                    0,
                    0f
            ));
            culturalOfferingsNumber++;
        }

        for(int i = 0; i < culturalOfferingsNumber; ++i) {
            for(int j = 0; j < usersNumber; ++j) {
                if(f.number().numberBetween(0, 15) == 0 && userAuthorities.get(j).authorityId == 3 && users.get(j).verified) {
                    Review r = new Review(f.lorem().sentence(5), f.number().numberBetween(1, 6), i + 1 , j + 1);
                    culturalOfferings.get(i).addRating(r);
                    reviews.add(r);
                }
            }
        }
        System.out.println("Inserting users...");
        users.forEach(u -> {
            try {
                writer.append(u.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Inserting user authorities...");
        userAuthorities.forEach(ua -> {
            try {
                writer.append(ua.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Inserting cultural offerings...");
        culturalOfferings.forEach(c -> {
            try {
                writer.append(c.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Inserting reviews...");
        reviews.forEach(r -> {
            try {
                writer.append(r.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void insertPosts(FileWriter writer, Faker faker) throws IOException {
        System.out.println("Inserting posts...");
        for(int i = 0; i < culturalOfferingsNumber; ++i) {
            long numPosts = faker.number().numberBetween(6, MAX_POSTS);
            for(int j = 0; j < numPosts; ++j) {
                Post p;
                int num = faker.number().numberBetween(0, 4);
                if (num == 0) {
                    p = new Post(faker.howIMetYourMother().catchPhrase(), i + 1);
                } else if (num == 1) {
                    p = new Post(faker.friends().quote(), i + 1);
                } else if (num == 2) {
                    p = new Post(faker.howIMetYourMother().quote(), i + 1);
                } else {
                    p = new Post(faker.rickAndMorty().quote(), i + 1);
                }
                writer.append(p.toString());
            }
        }
    }

    public void insertPhotos(FileWriter writer, Faker faker) throws IOException {
        System.out.println("Inserting photos...");
        File f = new File("photo_sources");
        if(!f.isDirectory()) {
            return;
        }
        File[] photoFiles = f.listFiles();
        int numPhotos = Objects.requireNonNull(photoFiles).length;

        for(int i = 0; i < culturalOfferingsNumber; ++i) {
            int index = i;
            long numPhotosToAdd = faker.number().numberBetween(0, 4);
            if (faker.number().numberBetween(0, 8) == 0) {
                continue;
            }
            LongStream.range(0, numPhotosToAdd).forEach(j -> {
                synchronized (writer) {
                    try {
                        writer.append(new Photo(index + 1).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                int photoIndex = ThreadLocalRandom.current().nextInt(numPhotos);
                File photoFile = Objects.requireNonNull(photoFiles)[photoIndex];
                if (!photoFile.exists()) {
                    return;
                }
                BufferedImage bufferedImage;
                BufferedImage thumbnail;

                try {
                    bufferedImage = Thumbnails.of(new FileInputStream(photoFile)).size(1000, 1000).asBufferedImage();
                    thumbnail = Thumbnails.of(new FileInputStream(photoFile)).size(200, 200).asBufferedImage();
                } catch (IOException e) {
                    return;
                }

                try {
                    savePhoto( "../back/photos", bufferedImage, index + 1);
                    savePhoto("../back/photos/thumbnail", thumbnail, index + 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public static void main(String[] args) throws IOException {
        Faker f = new Faker();
        DataGenerator generator = new DataGenerator();
        File importFile = new File("../back/src/main/resources/import.sql");
        importFile.createNewFile();
        FileWriter writer = new FileWriter(importFile);
        generator.insertAuthorities(writer);
        generator.insertCategories(writer, f);
        generator.insertSubcategories(writer, f);
        generator.insertMainData(writer, f);
        generator.addMainPhotos();
        generator.insertPosts(writer, f);
        generator.insertPhotos(writer, f);
        writer.close();
    }
}
