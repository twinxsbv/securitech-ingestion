package nl.accoda.platform.ingestionsecuritech.services;

import nl.accoda.platform.ingestionsecuritech.config.properties.ApplicationProperties;
import nl.accoda.platform.ingestionsecuritech.error.ConnectionPropertiesException;
import nl.accoda.platform.ingestionsecuritech.model.base.DocumentData;
import nl.accoda.platform.ingestionsecuritech.model.base.PersonalData;
import nl.accoda.platform.ingestionsecuritech.model.base.SecuritechDocumentScan;
import nl.accoda.platform.ingestionsecuritech.model.securitech.*;
import nl.accoda.platform.ingestionsecuritech.repositories.ProxyScanRepository;
import nl.accoda.platform.ingestionsecuritech.util.ImageType;
import nl.accoda.platform.ingestionsecuritech.util.PDFReportType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProxyScanService {

    private final ProxyScanRepository proxyScanRepository;
    private final ApplicationProperties applicationProperties;

    public ProxyScanService(ProxyScanRepository proxyScanRepository, ApplicationProperties applicationProperties) {
        this.proxyScanRepository = proxyScanRepository;
        this.applicationProperties = applicationProperties;
    }


    // ======================================================================
    // Service Calls
    // ======================================================================


    public ProxyScan save(ProxyScan data) {
        data = proxyScanRepository.save(data);
        return data;
    }

    public ProxyScan findByCustomerNbrAndLocationCodeAndDateTimeScan(String customerNbr, String locationCode, LocalDateTime dateTimeScan) {
        return proxyScanRepository.findByCustomerNbrAndLocationCodeAndDateTimeScan(customerNbr, locationCode, dateTimeScan);
    }


    // ======================================================================
    // Helpers
    // ======================================================================

    public SecuritechDocumentScan getLatestSecuritechScans() {

        SecuritechDocumentScan result = new SecuritechDocumentScan();
        String token = applicationProperties.getSecuritech().getToken();
        List<Map<String, String>> customers = applicationProperties.getSecuritech().getCustomers();

        for (Map<String, String> customer : customers
        ) {


            //get proxy Items from DR Connect
            String customerNumber = customer.get("custnumber");
            String locationCode = customer.get("locationcode");

            List<ProxyItem> proxyItems;

            try {
                proxyItems = getProxyItemsFromDrConnect(token, customerNumber, locationCode);

                // do house keeping and save all new or not processed scans in Dronnect db
                // and put all new scans on bus

                for (ProxyItem proxyItem : proxyItems) {
                    try {

                        ProxyScan proxyScanFromDrConnect = getScanFromDrConnect(token, locationCode, proxyItem.getScanDateTime().format(DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss")));


                        if (proxyScanFromDrConnect == null)
                            continue;

                        ProxyScan scan = findByCustomerNbrAndLocationCodeAndDateTimeScan(proxyScanFromDrConnect.getCustomerNbr(),
                                proxyScanFromDrConnect.getLocationCode(),
                                proxyScanFromDrConnect.getDateTimeScan());


                        if (scan == null) {
                            proxyScanFromDrConnect.setToken(token);
                            proxyScanFromDrConnect.setDocType(proxyItem.getDocType());
                            save(proxyScanFromDrConnect);
                            return convert(proxyScanFromDrConnect);

                        }
                    } catch (ConnectionPropertiesException e) {
                        e.printStackTrace();
                    }

                }
            } catch (ConnectionPropertiesException e) {
                e.printStackTrace();
            }


        }
        return result;
    }

    private SecuritechDocumentScan convert(ProxyScan proxyScan) throws ConnectionPropertiesException {

        PersonalData personalData = convertToPersonalData(proxyScan);
        SecuritechDocumentScan securitechDocumentScan = convertToSecuritechDocumentScan(proxyScan, personalData);
        securitechDocumentScan = convertPhoto(proxyScan, securitechDocumentScan);
        securitechDocumentScan = convertDocuments(proxyScan, securitechDocumentScan);

        return securitechDocumentScan;
    }

    private SecuritechDocumentScan convertDocuments(ProxyScan proxyScan, SecuritechDocumentScan securitechDocumentScan) throws ConnectionPropertiesException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        List<PDFReport> reports = getReportsForScanFromDrConnect(proxyScan.getToken(), proxyScan.getLocationCode(), proxyScan.getDateTimeScan().format(formatter));
        reports.forEach(report -> {
            if (report.getPdfReportType().equals(PDFReportType.PDF_WITH_IMAGES)) {
                DocumentData documentData = new DocumentData(DocumentData.ReportType.PDF_WITH_IMAGES, report.getReport());
                securitechDocumentScan.getDocumentData().add(documentData);
            }

            if (report.getPdfReportType().equals(PDFReportType.PDF_WITHOUT_IMAGES)) {
                DocumentData documentData = new DocumentData(DocumentData.ReportType.PDF_WITHOUT_IMAGES, report.getReport());
                securitechDocumentScan.getDocumentData().add(documentData);
            }
        });
        return securitechDocumentScan;
    }

    private SecuritechDocumentScan convertPhoto(ProxyScan proxyScan, SecuritechDocumentScan securitechDocumentScan) {
        List<DocumentSide> sides = proxyScan.getDocument().getDocumentSides();
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
                        securitechDocumentScan.getPersonalData().setPhoto(i.getImage());
                });
            } else if (photoImages.size() == 1) {
                securitechDocumentScan.getPersonalData().setPhoto(photoImages.get(0).getImage());
            }

        }

        return securitechDocumentScan;
    }

    private SecuritechDocumentScan convertToSecuritechDocumentScan(ProxyScan proxyScan, PersonalData personalData) {
        SecuritechDocumentScan securitechDocumentScan = new SecuritechDocumentScan();
        securitechDocumentScan.setDocumentType(proxyScan.getDocType());
        securitechDocumentScan.setDocumentNumber(proxyScan.getDocument().getDocumentData().getDocumentNumber());
        securitechDocumentScan.setDateOfIssue(null);
        securitechDocumentScan.setDateOfExpiry(proxyScan.getDocument().getDocumentData().getExpirationDate().toLocalDate());
        securitechDocumentScan.setIssuingCountry(proxyScan.getDocument().getDocumentData().getIssuingCountry());
        securitechDocumentScan.setPersonalData(personalData);
        securitechDocumentScan.setScanDate(proxyScan.getDateTimeScan().atZone(ZoneId.of("UTC")).toInstant());
        securitechDocumentScan.setToken(proxyScan.getToken());
        securitechDocumentScan.setCustomerNumber(proxyScan.getCustomerNbr());
        securitechDocumentScan.setLocationCode(proxyScan.getLocationCode());
        return securitechDocumentScan;
    }

    private PersonalData convertToPersonalData(ProxyScan proxyScan) {
        PersonalData personalData = new PersonalData();
        personalData.setFirstNames(proxyScan.getDocument().getPersonalData().getFirstName());
        personalData.setInitials(proxyScan.getDocument().getPersonalData().getInitials());
        personalData.setLastNamePrefix(proxyScan.getDocument().getPersonalData().getLastNamePrefix());
        personalData.setLastName(proxyScan.getDocument().getPersonalData().getLastName());
        personalData.setBirthDate(proxyScan.getDocument().getPersonalData().getBirthDate().toLocalDate());
        personalData.setBsn(proxyScan.getDocument().getPersonalData().getBsn());
        personalData.setRtw(false);
        personalData.setGender(proxyScan.getDocument().getRawData().getGender());
        personalData.setNationality(proxyScan.getDocument().getRawData().getNationality());
        return personalData;
    }

    // ======================================================================
    // Direct Rest Calls to DRConnect service on broker
    // ======================================================================

    public List<ProxyItem> getProxyItemsFromDrConnect(String token, String customerNbr, String
            locationCode) throws ConnectionPropertiesException {

        RestTemplate restTemplate = new RestTemplate();

        if (token == null)
            throw new ConnectionPropertiesException("Token", "Token must be supplied");

        if (customerNbr == null)
            throw new ConnectionPropertiesException("CustomerNbr", "CustomerNbr must be supplied");


        if (locationCode == null)
            throw new ConnectionPropertiesException("LocationCode", "LocationCode must be supplied");

        String url = applicationProperties.getSecuritech().getUrl().getRest()
                .concat("/GetScans")
                .concat("?")
                .concat("Token=")
                .concat(token)
                .concat("&")
                .concat("CustomerNbr=")
                .concat(customerNbr)
                .concat("&")
                .concat("OnlyNonRevoked=True")
                .concat("&")
                .concat("LocationCode=")
                .concat(locationCode);

        ProxyItem[] proxyItems = restTemplate.getForObject(url, ProxyItem[].class);
        return Arrays.asList(proxyItems);
    }

    public ProxyScan getScanFromDrConnect(String token, String locationCode, String scanDateTime) throws
            ConnectionPropertiesException {

        RestTemplate restTemplate = new RestTemplate();

        if (token == null)
            throw new ConnectionPropertiesException("Token", "Token must be supplied");

        if (locationCode == null)
            throw new ConnectionPropertiesException("LocationCode", "LocationCode must be supplied");


        if (scanDateTime == null)
            throw new ConnectionPropertiesException("ScanDatetime", "ScanDateTime must be supplied");

        String url = applicationProperties.getSecuritech().getUrl().getRest()
                .concat("/GetScan")
                .concat("?")
                .concat("Token=")
                .concat(token)
                .concat("&")
                .concat("LocationCode=")
                .concat(locationCode)
                .concat("&")
                .concat("ScanDateTime=")
                .concat(scanDateTime)
                .concat("&")
                .concat("Images=True")
                .concat("&")
                .concat("Reports=True");


        ProxyScan proxyScan = restTemplate.getForEntity(url, ProxyScan.class).getBody();
        return proxyScan;
    }

    private List<PDFReport> getReportsForScanFromDrConnect(String token, String locationCode, String
            scanDateTime) throws ConnectionPropertiesException {


        if (token == null)
            throw new ConnectionPropertiesException("Token", "Token must be supplied");

        if (locationCode == null)
            throw new ConnectionPropertiesException("LocationCode", "LocationCode must be supplied");


        if (scanDateTime == null)
            throw new ConnectionPropertiesException("ScanDateTime", "ScanDateTime must be supplied");

        String url = applicationProperties.getSecuritech().getUrl().getRest()
                .concat("/GetReportsForScan")
                .concat("?")
                .concat("Token=")
                .concat(token)
                .concat("&")
                .concat("LocationCode=")
                .concat(locationCode)
                .concat("&")
                .concat("ScanDateTime=")
                .concat(scanDateTime);

        RestTemplate restTemplate = new RestTemplate();
        PDFReport[] reports = restTemplate.getForObject(url, PDFReport[].class);
        return Arrays.asList(reports);
    }
}

