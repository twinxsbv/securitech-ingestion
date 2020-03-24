package nl.accoda.platform.ingestionsecuritech.tasks;


import nl.accoda.platform.ingestionsecuritech.services.ProxyScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanUpDrConnectTask {

    private static Logger logger = LoggerFactory.getLogger(CleanUpDrConnectTask.class);
    private final Environment environment;
    private final ProxyScanService proxyScanService;

    public CleanUpDrConnectTask(Environment environment, ProxyScanService proxyScanService) {
        this.environment = environment;
        this.proxyScanService = proxyScanService;
    }

    @Scheduled(cron = "${drconnect.retention.cron}")
    private void executeRetentionPolicy() {

//        int retentionDays = Integer.valueOf(environment.getProperty("drconnect.retention.days"));
//
//
//        List<ProxyScan> proxyScans = proxyScanService.getRetentionPolicyCandidates();


//        proxyScans.stream()
//                .forEach(scan -> {

//                    LocalDateTime retententionExpireDate = scan.getDateTimeScan().plusDays(retentionDays);

//                    try {
//                        if (retententionExpireDate.isBefore(LocalDateTime.now())) {
//                            proxyScanService.deleteScanfromDrConnect(scan.getToken(),
//                                    scan.getLocationCode(),
//                                    scan.getDateTimeScan().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
//                            logger.info(new StringBuilder()
//                                            .append("Successfully executed DrConnect Scan Retention Policy and therefore deleted scan '{}' from ")
//                                            .append("DRConnect database").toString(),
//                                   scan.getId());
//                        }
//                    } catch (ConnectionPropertiesException e) {
//                        e.printStackTrace();
//                    }
//
//
//                });


//    }
    }
}
