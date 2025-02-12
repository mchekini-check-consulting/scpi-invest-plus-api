package fr.formationacademy.scpiinvestplusapi.batch.reader;

import static org.junit.jupiter.api.Assertions.*;

import fr.formationacademy.scpiinvestplusapi.model.dto.requests.ScpiDto;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.FlatFileItemReader;


class ScpiItemReaderTest {

    private final ScpiItemReader scpiItemReader = new ScpiItemReader();

    @Test
    void testReaderConfiguration() {
        FlatFileItemReader<ScpiDto> reader = scpiItemReader.reader();

        assertNotNull(reader, "Le lecteur ne doit pas être null");
        assertEquals("scpiRequestItemReader", reader.getName(), "Le nom du lecteur est incorrect");
    }
}
