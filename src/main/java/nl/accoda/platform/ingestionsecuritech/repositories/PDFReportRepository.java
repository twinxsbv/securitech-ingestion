package nl.accoda.platform.ingestionsecuritech.repositories;


import nl.accoda.platform.ingestionsecuritech.model.securitech.PDFReport;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PDFReportRepository extends MongoRepository<PDFReport, String> {

}
