package nl.accoda.platform.ingestionsecuritech.repositories;


import nl.accoda.platform.ingestionsecuritech.model.securitech.ProxyScan;
import nl.accoda.platform.ingestionsecuritech.util.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProxyScanRepository extends MongoRepository<ProxyScan, String> {

    @Query("{ distinct : 'proxyScan', key : 'appId'}")
    List<String> findDistinctByAppId();

    @Query("{ distinct : 'proxyScan', key : 'customerNbr'}")
    List<String> findDistinctByCustomerNbr();

    @Query("{ distinct : 'proxyScan', key : 'locationCode'}")
    List<String> findDistinctByLocationCode();

    List<ProxyScan> getAllByCustomerNbrAndLocationCode(String customerNbr, String locationCode);

    ProxyScan findByCustomerNbrAndLocationCodeAndDateTimeScan(String customerNbr, String locationCode, LocalDateTime dateTimeScan);

    List<ProxyScan> getAllByAppId(String appId);

    List<ProxyScan> getAllByStatusNotIn(Status staus);
}
