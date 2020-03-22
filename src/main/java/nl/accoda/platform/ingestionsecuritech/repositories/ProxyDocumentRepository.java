package nl.accoda.platform.ingestionsecuritech.repositories;


import nl.accoda.platform.ingestionsecuritech.model.securitech.ProxyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProxyDocumentRepository extends MongoRepository<ProxyDocument, String> {
}
