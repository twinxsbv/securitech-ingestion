package nl.accoda.platform.ingestionsecuritech.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;


public class DrConnectSexDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

       switch (p.getValueAsInt()){
           case 0:
               return "M";
           case 1:
               return "F";
           default:
               return  "O";
       }

    }

}



