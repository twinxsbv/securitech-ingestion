package nl.accoda.platform.ingestionsecuritech;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }
}
