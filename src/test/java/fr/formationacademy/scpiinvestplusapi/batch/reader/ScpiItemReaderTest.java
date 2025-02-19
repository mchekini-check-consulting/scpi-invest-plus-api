package fr.formationacademy.scpiinvestplusapi.batch.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;

@ExtendWith(MockitoExtension.class)
public class ScpiItemReaderTest {

    @Test
    public void testReadValidCsvFile() throws Exception {
        ScpiItemReader reader = new ScpiItemReader();
        FlatFileItemReader<ScpiDto> itemReader = reader.reader();

        try {
            itemReader.open(new ExecutionContext());
            ScpiDto firstItem = itemReader.read();

            assertNotNull(firstItem, "Le premier élément lu ne doit pas être null");
            assertAll("Vérification des valeurs du premier élément du fichier CSV",
                    () -> assertEquals("Transitions Europe", firstItem.getName(), "Nom incorrect"),
                    () -> assertEquals(5000999, firstItem.getMinimumSubscription(), "MinimumSubscription incorrect"),
                    () -> assertEquals("Arkea REIM", firstItem.getManager(), "Manager incorrect"),
                    () ->assertEquals(289_000_000L, firstItem.getCapitalization(), "Capitalization incorrecte"),
                    () -> assertEquals(10.0f, firstItem.getSubscriptionFees(), 0.01, "SubscriptionFees incorrect"),
                    () -> assertEquals(10.0f, firstItem.getManagementCosts(), 0.01, "ManagementCosts incorrect"),
                    () -> assertEquals("FR76 12345 67890 12345678901 12", firstItem.getIban(), "IBAN incorrect"),
                    () -> assertEquals("ABCDFRPP", firstItem.getBic(), "BIC incorrect"),
                    () -> assertEquals(5.0f, firstItem.getCashback(), 0.01, "Cashback incorrect"),
                    () -> assertEquals(
                            "Nous recommandons vivement cette SCPI qui, grâce à une diversification optimale, a su générer un rendement de plus de 8% sur les trois derniéres années",
                            firstItem.getAdvertising(),
                            "Advertising incorrect"
                    ),
                    () -> assertEquals("Pays-bas,47,Espagne,24,Irlande,12,Pologne,11,Allemagne,6", firstItem.getLocations(), "Locations incorrectes"),
                    () -> assertEquals("Bureaux,46,Hotels,18,Logistique,9,Sante,18,Commerce,9", firstItem.getSectors(), "Sectors incorrects")
            );


        } finally {
            itemReader.close();
        }
    }

    @Test
    public void testMapAllCsvColumnsToScpiDtoFields() throws Exception {
        FlatFileItemReader<ScpiDto> reader = new ScpiItemReader().reader();

        try {
            reader.open(new ExecutionContext());
            ScpiDto scpiDto = reader.read();

            assertNotNull(scpiDto, "Le DTO lu ne doit pas être null");
            assertAll("Vérification des champs du ScpiDto",
                    () -> assertEquals("Transitions Europe", scpiDto.getName(), "Nom incorrect"),
                    () -> assertEquals(5000999, scpiDto.getMinimumSubscription(), "MinimumSubscription incorrect"),
                    () -> assertEquals("Arkea REIM", scpiDto.getManager(), "Manager incorrect"),
                    () -> assertEquals(289_000_000L, scpiDto.getCapitalization(), "Capitalization incorrecte"), // Capitalization est un Long
                    () -> assertEquals(10.0f, scpiDto.getSubscriptionFees(), 0.01, "SubscriptionFees incorrect"), // Comparaison Float avec delta
                    () -> assertEquals(10.0f, scpiDto.getManagementCosts(), 0.01, "ManagementCosts incorrect"), // Comparaison Float avec delta
                    () -> assertEquals(5, scpiDto.getEnjoymentDelay(), "EnjoymentDelay incorrect"),
                    () -> assertEquals("FR76 12345 67890 12345678901 12", scpiDto.getIban(), "IBAN incorrect"),
                    () -> assertEquals("ABCDFRPP", scpiDto.getBic(), "BIC incorrect"),
                    () -> assertEquals(5.0f, scpiDto.getCashback(), 0.01, "Cashback incorrect"), // Comparaison Float avec delta
                    () -> assertEquals("Pays-bas,47,Espagne,24,Irlande,12,Pologne,11,Allemagne,6", scpiDto.getLocations(), "Locations incorrect"),
                    () -> assertEquals("Bureaux,46,Hotels,18,Logistique,9,Sante,18,Commerce,9", scpiDto.getSectors(), "Sectors incorrect")
            );

        } finally {
            reader.close();
        }
    }

    @Test
    public void testSkipHeaderRowOfCsvFile() throws Exception {
        FlatFileItemReader<ScpiDto> reader = new ScpiItemReader().reader();
        reader.open(new ExecutionContext());
        ScpiDto firstRecord = reader.read();
        assertNotNull(firstRecord);
        assertNotEquals("HeaderName", firstRecord.getName());
    }
}