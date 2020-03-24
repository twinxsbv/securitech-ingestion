package nl.accoda.platform.ingestionsecuritech.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.accoda.platform.ingestionsecuritech.model.securitech.DocumentSide;
import nl.accoda.platform.ingestionsecuritech.model.securitech.ProxyImage;
import nl.accoda.platform.ingestionsecuritech.model.securitech.ProxyScan;
import nl.accoda.platform.ingestionsecuritech.util.ImageType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class ProxyScanDTO {

    private String id;
    private String customerNbr;
    private String locationCode;
    private String appId;
    private String firstName;
    private String lastName;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime birthDate;
    private String gender;
    private String bsn;
    private String nationality;
    private String documentNumber;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expirationDate;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTimeScan;
    private byte[] photo;
    private String status;


    public ProxyScanDTO (ProxyScan scan) {

        ProxyScanDTO proxyScanDTO = new ProxyScanDTO();

        proxyScanDTO.setId(scan.getId());
        proxyScanDTO.setAppId(scan.getAppId());
        proxyScanDTO.setCustomerNbr(scan.getCustomerNbr());
        proxyScanDTO.setLocationCode(scan.getLocationCode());
        proxyScanDTO.setDateTimeScan(scan.getDateTimeScan());
        proxyScanDTO.setStatus(scan.getStatus().toString());

        proxyScanDTO.setFirstName(scan.getDocument().getPersonalData().getFirstName());
        proxyScanDTO.setLastName(scan.getDocument().getPersonalData().getLastName());
        proxyScanDTO.setBirthDate(scan.getDocument().getPersonalData().getBirthDate());

        proxyScanDTO.setBsn(scan.getDocument().getPersonalData().getBsn());

        proxyScanDTO.setGender(scan.getDocument().getRawData().getGender());
        proxyScanDTO.setNationality(scan.getDocument().getRawData().getNationality());

        proxyScanDTO.setDocumentNumber(scan.getDocument().getDocumentData().getDocumentNumber());
        proxyScanDTO.setExpirationDate(scan.getDocument().getDocumentData().getExpirationDate());
        List<DocumentSide> sides = scan.getDocument().getDocumentSides();
        for (DocumentSide side : sides) {
            List<ProxyImage> images = side.getImages();
            List<ProxyImage> photoImages = new ArrayList<>(images);

            for (ProxyImage image : images) {

                if (!image.getImageType().equals(ImageType.SCP) & !image.getImageType().equals(ImageType.PC)) {
                    photoImages.remove(image);
                }
            }

            if (photoImages.size() == 2) {
                photoImages.forEach(i -> {
                    if (i.getImageType().equals(ImageType.SCP))
                        proxyScanDTO.setPhoto(i.getImage());
                });
            } else if (photoImages.size() == 1) {
                proxyScanDTO.setPhoto(photoImages.get(0).getImage());
            }

        }

    }

}
