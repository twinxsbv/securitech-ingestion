package nl.accoda.platform.ingestionsecuritech.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import nl.accoda.platform.ingestionsecuritech.util.PDFReportType;

import java.io.IOException;


public class DrConnectPdfReportDeserializer extends JsonDeserializer<PDFReportType> {

    @Override
    public PDFReportType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

       switch (p.getValueAsInt()){
           case 0:
               return PDFReportType.PDF_WITH_IMAGES;
           case 1:
               return PDFReportType.PDF_WITHOUT_IMAGES;
           default:
               return PDFReportType.UNKNOWN;
       }

    }

}



