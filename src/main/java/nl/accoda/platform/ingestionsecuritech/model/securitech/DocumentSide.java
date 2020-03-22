package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DocumentSide {

    @JsonProperty(value = "Images")
    private List<ProxyImage> images = new ArrayList<>();
    private boolean isMrZSide;

}
