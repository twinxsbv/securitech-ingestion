package nl.accoda.platform.ingestionsecuritech.repositories;


import nl.accoda.platform.ingestionsecuritech.model.securitech.PersonalInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonalInfoRepository extends MongoRepository<PersonalInfo, String> {
}
