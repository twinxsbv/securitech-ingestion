package nl.accoda.platform.ingestionsecuritech.repositories;

import nl.accoda.platform.ingestionsecuritech.model.securitech.DocumentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DocumentInfoRepository extends MongoRepository<DocumentInfo, String> {
}
