package nl.accoda.platform.ingestionsecuritech.repositories;


import nl.accoda.platform.ingestionsecuritech.model.securitech.DocumentSide;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DocumentSideReporitory extends MongoRepository<DocumentSide, String> {
}
