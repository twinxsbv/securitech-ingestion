package nl.accoda.platform.ingestionsecuritech.repositories;


import nl.accoda.platform.ingestionsecuritech.model.securitech.ProxyItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProxyItemRepository extends MongoRepository<ProxyItem, String> {

}
