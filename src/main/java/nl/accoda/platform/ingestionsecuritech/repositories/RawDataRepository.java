package nl.accoda.platform.ingestionsecuritech.repositories;


import nl.accoda.platform.ingestionsecuritech.model.securitech.RawData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RawDataRepository extends MongoRepository<RawData, String> {
}
