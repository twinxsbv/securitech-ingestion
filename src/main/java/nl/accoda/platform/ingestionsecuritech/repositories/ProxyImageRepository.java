package nl.accoda.platform.ingestionsecuritech.repositories;

import nl.accoda.platform.ingestionsecuritech.model.securitech.ProxyImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProxyImageRepository extends MongoRepository<ProxyImage, String> {
}
