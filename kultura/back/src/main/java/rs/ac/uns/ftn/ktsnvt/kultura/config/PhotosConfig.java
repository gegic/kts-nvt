package rs.ac.uns.ftn.ktsnvt.kultura.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "photos")
public class PhotosConfig {

    private String path;

}