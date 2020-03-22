package nl.accoda.platform.ingestionsecuritech.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.accoda.platform.ingestionsecuritech.config.properties.ApplicationProperties;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectionPropertiesSDTO;
import nl.accoda.platform.ingestionsecuritech.dto.PDFReportDTO;
import nl.accoda.platform.ingestionsecuritech.dto.ProxyScanDTO;
import nl.accoda.platform.ingestionsecuritech.error.ConnectionPropertiesException;
import nl.accoda.platform.ingestionsecuritech.model.base.DocumentScan;
import nl.accoda.platform.ingestionsecuritech.model.base.PersonalData;
import nl.accoda.platform.ingestionsecuritech.model.base.SecuritechDocumentScan;
import nl.accoda.platform.ingestionsecuritech.model.securitech.*;
import nl.accoda.platform.ingestionsecuritech.repositories.ProxyScanRepository;
import nl.accoda.platform.ingestionsecuritech.util.ImageType;
import nl.accoda.platform.ingestionsecuritech.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Transactional
public class ProxyScanService {

    private static Logger logger = LoggerFactory.getLogger(ProxyScanService.class);


    private final ProxyScanRepository proxyScanRepository;
    private final ApplicationProperties applicationProperties;

    public ProxyScanService(ProxyScanRepository proxyScanRepository, ApplicationProperties applicationProperties) {
        this.proxyScanRepository = proxyScanRepository;
        this.applicationProperties = applicationProperties;
    }


    // ======================================================================
    // Service Calls
    // ======================================================================

    public List<ProxyScanDTO> processNewScans(String token, String customerNbr, String locationCode) throws ConnectionPropertiesException {

        List<ProxyItem> proxyItems = getProxyItemsFromDrConnect(token, customerNbr, locationCode);
        List<ProxyScanDTO> result = new ArrayList<>();

        // do house keeping and save all new or not processed scans in Dronnectdb
        proxyItems.forEach(proxyItem -> {

            try {

                ProxyScan proxyScanFromDrConnect = getScanFromDrConnect(token,
                        locationCode, proxyItem.getScanDateTime().format(DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss")));


                if (proxyScanFromDrConnect == null)
                    return;

                ProxyScan scan = findByCustomerNbrAndLocationCodeAndDateTimeScan(proxyScanFromDrConnect.getCustomerNbr(),
                        proxyScanFromDrConnect.getLocationCode(),
                        proxyScanFromDrConnect.getDateTimeScan());


                if (scan == null) {
                    proxyScanFromDrConnect.setToken(token);
                    ProxyScan newScan = save(proxyScanFromDrConnect);
                    ProxyScanDTO proxyScanDTO = convertx(newScan);
                    result.add(proxyScanDTO);

                }
            } catch (ConnectionPropertiesException | JsonProcessingException e) {
                e.printStackTrace();
            }

        });
        return result;
    }

    public List<PDFReportDTO> getReportsForScan(String token, String locationCode, String scanDateTime) throws ConnectionPropertiesException {
        List<PDFReport> reports = getReportsForScanFromDrConnect(token, locationCode, scanDateTime);
        List<PDFReportDTO> results = new ArrayList<>();
        reports.stream().forEach(pdfReport -> results.add(convertReport(pdfReport)));
        return results;
    }

    public ProxyScanDTO getIndividualScan(String id) {
        ProxyScan scan = findById(id).get();
        if (scan == null)
            return null;
        return convertx(scan);
    }

    public ProxyScan save(ProxyScan data) {
        data = proxyScanRepository.save(data);
        return data;
    }

    public ProxyScanDTO setStatusToProcessed(String id, String status) {

        ProxyScan scan = findById(id).get();
        if (scan != null) {
            scan.setStatus(Status.valueOf(status));
            scan = save(scan);
        }

        if (scan != null)
            return convertx(scan);

        return null;
    }

    public Optional<ProxyScan> findById(String id) {
        return proxyScanRepository.findById(id);
    }

    public ProxyScan findByCustomerNbrAndLocationCodeAndDateTimeScan(String customerNbr, String locationCode, LocalDateTime dateTimeScan) {
        return proxyScanRepository.findByCustomerNbrAndLocationCodeAndDateTimeScan(customerNbr, locationCode, dateTimeScan);
    }

    public List<String> findDistinctByAppId() {
        return proxyScanRepository.findDistinctByAppId();
    }

    public List<String> findDistinctByCustomerNbr() {
        return proxyScanRepository.findDistinctByCustomerNbr();
    }

    public List<String> findDistinctByLocationCode() {
        return proxyScanRepository.findDistinctByLocationCode();
    }


    public List<ProxyScanDTO> getAll() {
        List<ProxyScanDTO> results = new ArrayList<>();

        for (ProxyScan proxyScan : proxyScanRepository.findAll()) {
            results.add(convertx(proxyScan));
        }
        return results;
    }

    public List<ProxyScanDTO> getAllByCustomerNbrAndLocationCode(String customerNumber, String locationCode) {
        List<ProxyScanDTO> results = new ArrayList<>();
        List<ProxyScan> scans = proxyScanRepository.getAllByCustomerNbrAndLocationCode(customerNumber, locationCode);
        scans.forEach(scan ->

                results.add(convertx(scan))
        );
        return results;
    }

    public void delete(ProxyScan scan) {
        proxyScanRepository.delete(scan);
    }


    // ======================================================================
    // Helpers
    // ======================================================================

    public Supplier getLatestSecuritechScans() {

        List<Supplier> results = new ArrayList<>();
        String token = applicationProperties.getSecuritech().getToken();

        applicationProperties.getSecuritech().getCustomers()
                .forEach(customer -> {

                    //get proxy Items from DR Connect
                    String customerNumber = customer.get("custnumber");
                    String locationCode = customer.get("locationcode");

                    List<ProxyItem> proxyItems;

                    try {
                        proxyItems = getProxyItemsFromDrConnect(token, customerNumber, locationCode);

                        // do house keeping and save all new or not processed scans in Dronnect db
                        // and put all new scans on bus

                        proxyItems.forEach(proxyItem -> {

                            try {

                                ProxyScan proxyScanFromDrConnect = getScanFromDrConnect(token, locationCode, proxyItem.getScanDateTime().format(DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss")));


                                if (proxyScanFromDrConnect == null)
                                    return;

//                        ProxyScan scan = proxyScanService.findByCustomerNbrAndLocationCodeAndDateTimeScan(proxyScanFromDrConnect.getCustomerNbr(),
//                                proxyScanFromDrConnect.getLocationCode(),
//                                proxyScanFromDrConnect.getDateTimeScan());
//
//
//                        if (scan == null) {
                                proxyScanFromDrConnect.setToken(token);
                                ProxyScan newScan = save(proxyScanFromDrConnect);
                                // convert to generic object SecuritechDocumentScan

                                // put generic scan on on bus
                                Supplier<SecuritechDocumentScan> supplier = () -> convert(newScan);
                                supplier.get();
                                results.add(supplier);


//                        }
                            } catch (ConnectionPropertiesException | JsonProcessingException e) {
                                e.printStackTrace();
                            }

                        });
                    } catch (ConnectionPropertiesException e) {
                        e.printStackTrace();
                    }

                });


        return this::getLatestSecuritechScans;
    }

    public SecuritechDocumentScan convert(ProxyScan proxyScan) {

        PersonalData personalData = convertToPersonalData(proxyScan);
        SecuritechDocumentScan securitechDocumentScan = convertToSecuritechDocumentScan(proxyScan, personalData);


//        List<DocumentSide> sides = scan.getDocument().getDocumentSides();
//        for (DocumentSide side : sides) {
//            List<ProxyImage> images = side.getImages();
//            List<ProxyImage> photoImages = new ArrayList<>(images);
//
//            for (ProxyImage image : images) {
//
//                if (!image.getImageType().equals(ImageType.SCP) & !image.getImageType().equals(ImageType.PC)) {
//                    photoImages.remove(image);
//                }
//            }
//
//            if (photoImages.size() == 2) {
//                photoImages.forEach(i -> {
//                    if (i.getImageType().equals(ImageType.SCP))
//                        result.setPhoto(i.getImage());
//                });
//            } else if (photoImages.size() == 1) {
//                result.setPhoto(photoImages.get(0).getImage());
//            }


//            List<PDFReport> reports = scan.getPdfReports();
//            reports.forEach(report -> {
//                if (report.getPdfReportType().equals(PDFReportType.PDF_WITH_IMAGES)) {
//                    result.setPdfReport(report.getReport());
//                    result.setPdfReportType("PDF_WITH_IMAGES");
//                }
//
//                if (report.getPdfReportType().equals(PDFReportType.PDF_WITHOUT_IMAGES)) {
//                    result.setPdfReport(report.getReport());
//                    result.setPdfReportType("PDF_WITHOUT_IMAGES");
//                }
//            });

        return securitechDocumentScan;
    }

    private SecuritechDocumentScan convertToSecuritechDocumentScan(ProxyScan proxyScan, PersonalData personalData) {
        SecuritechDocumentScan securitechDocumentScan = new SecuritechDocumentScan();
        securitechDocumentScan.setDocumentType(DocumentScan.DocumentType.ID_CARD);
        securitechDocumentScan.setDocumentNumber(proxyScan.getDocument().getDocumentData().getDocumentNumber());
        securitechDocumentScan.setDateOfIssue(null);
        securitechDocumentScan.setDateOfExpiry(proxyScan.getDocument().getDocumentData().getExpirationDate().toLocalDate());
        securitechDocumentScan.setIssuingCountry(proxyScan.getDocument().getDocumentData().getIssuingCountry());
        securitechDocumentScan.setPersonalData(personalData);
        securitechDocumentScan.setProcessed(true);
        securitechDocumentScan.setScanDate(proxyScan.getDateTimeScan().atZone(ZoneId.of("Europe/Paris")).toInstant());
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
        personalData.setBirthDate(proxyScan.getDocument().getPersonalData().getBirthDate());
        personalData.setBsn(proxyScan.getDocument().getPersonalData().getBsn());
        personalData.setRtw(false);
        personalData.setGender(proxyScan.getDocument().getRawData().getGender());
        personalData.setNationality(proxyScan.getDocument().getRawData().getNationality());
        return personalData;
    }

    public ProxyScanDTO convertx(ProxyScan scan) {

        ProxyScanDTO result = new ProxyScanDTO();

        result.setId(scan.getId());
        result.setAppId(scan.getAppId());
        result.setCustomerNbr(scan.getCustomerNbr());
        result.setLocationCode(scan.getLocationCode());
        result.setDateTimeScan(scan.getDateTimeScan());
        result.setStatus(scan.getStatus().toString());

        result.setFirstName(scan.getDocument().getPersonalData().getFirstName());
        result.setLastName(scan.getDocument().getPersonalData().getLastName());
        result.setBirthDate(scan.getDocument().getPersonalData().getBirthDate());

        result.setBsn(scan.getDocument().getPersonalData().getBsn());

        result.setGender(scan.getDocument().getRawData().getGender());
        result.setNationality(scan.getDocument().getRawData().getNationality());

        result.setDocumentNumber(scan.getDocument().getDocumentData().getDocumentNumber());
        result.setExpirationDate(scan.getDocument().getDocumentData().getExpirationDate());
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
                        result.setPhoto(i.getImage());
                });
            } else if (photoImages.size() == 1) {
                result.setPhoto(photoImages.get(0).getImage());
            }


//            List<PDFReport> reports = scan.getPdfReports();
//            reports.forEach(report -> {
//                if (report.getPdfReportType().equals(PDFReportType.PDF_WITH_IMAGES)) {
//                    result.setPdfReport(report.getReport());
//                    result.setPdfReportType("PDF_WITH_IMAGES");
//                }
//
//                if (report.getPdfReportType().equals(PDFReportType.PDF_WITHOUT_IMAGES)) {
//                    result.setPdfReport(report.getReport());
//                    result.setPdfReportType("PDF_WITHOUT_IMAGES");
//                }
//            });
        }


        return result;
    }

    private PDFReportDTO convertReport(PDFReport report) {

        PDFReportDTO dto = new PDFReportDTO();
        dto.setPdfReportType(report.getPdfReportType().toString());
        dto.setReport(report.getReport());
        return dto;
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
            ConnectionPropertiesException, JsonProcessingException {

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

    private List<DocumentSide> getImagesForScanFromDrConnect(DrConnectionPropertiesSDTO
                                                                     connectionProperties)
            throws ConnectionPropertiesException {

        if (connectionProperties.getToken() == null)
            throw new ConnectionPropertiesException("Token", "Token must be supplied");

        if (connectionProperties.getLocationCode() == null)
            throw new ConnectionPropertiesException("LocationCode", "LocationCode must be supplied");


        if (connectionProperties.getScanDateTime() == null)
            throw new ConnectionPropertiesException("ScanDatetime", "ScanDateTime must be supplied");

        String url = applicationProperties.getSecuritech().getUrl().getRest()
                .concat("/GetImagesForScan")
                .concat("?")
                .concat("Token=")
                .concat(connectionProperties.getToken())
                .concat("&")
                .concat("LocationCode=")
                .concat(connectionProperties.getLocationCode())
                .concat("&")
                .concat("ScanDateTime=")
                .concat(connectionProperties.getScanDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

        RestTemplate restTemplate = new RestTemplate();
        DocumentSide[] images = restTemplate.getForObject(url, DocumentSide[].class);
        return Arrays.asList(images);
    }

    public void deleteAllScansByLocationFromDrConnect(DrConnectionPropertiesSDTO connectionProperties)
            throws ConnectionPropertiesException {
        if (connectionProperties.getToken() == null)
            throw new ConnectionPropertiesException("Token", "Token must be supplied");

        if (connectionProperties.getLocationCode() == null)
            throw new ConnectionPropertiesException("LocationCode", "LocationCode must be supplied");


        String url = applicationProperties.getSecuritech().getUrl().getRest()
                .concat("/DeleteAllScansByLocation")
                .concat("?")
                .concat("Token=")
                .concat(connectionProperties.getToken())
                .concat("&")
                .concat("LocationCode=")
                .concat(connectionProperties.getLocationCode());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }

    public void deleteAllScansByLocationOnDateFromDrConnect(String token, String locationCode, String date) throws
            ConnectionPropertiesException {

        if (token == null)
            throw new ConnectionPropertiesException("Token", "Token must be supplied");

        if (locationCode == null)
            throw new ConnectionPropertiesException("LocationCode", "LocationCode must be supplied");


        if (date == null)
            throw new ConnectionPropertiesException("Date", "Date must be supplied");


        String url = applicationProperties.getSecuritech().getUrl().getRest()
                .concat("/DeleteAllScansByLocationOnDate")
                .concat("?")
                .concat("Token=")
                .concat(token)
                .concat("&")
                .concat("LocationCode=")
                .concat(locationCode)
                .concat("&")
                .concat("Date=")
                .concat(date);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }

    public void deleteScanfromDrConnect(String token, String locationCode, String scanDateTime)
            throws ConnectionPropertiesException {

        if (token == null)
            throw new ConnectionPropertiesException("Token", "Token must be supplied");

        if (locationCode == null)
            throw new ConnectionPropertiesException("LocationCode", "LocationCode must be supplied");


        if (scanDateTime == null)
            throw new ConnectionPropertiesException("ScanDateTime", "ScanDateTime must be supplied");

        String url = applicationProperties.getSecuritech().getUrl().getRest()
                .concat("/DeleteScan")
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
        // TODO: call raises error in DRConnect, consult Securitech
//        restTemplate.delete(url);

    }


    public List<ProxyScan> getRetentionPolicyCandidates() {
        return proxyScanRepository.getAllByStatusNotIn(Status.DELETED_FROM_DRCONNECT);
    }

}

