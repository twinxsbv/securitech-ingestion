package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.accoda.platform.ingestionsecuritech.util.ImageType;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProxyImage {


    @JsonProperty(value = "Image")
    private byte[] image;
    @JsonProperty(value = "ImageType")
    private ImageType imageType;

    public ProxyImage(ImageType imageType) {
        this.imageType = imageType;
    }
}
