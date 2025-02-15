package fr.formationacademy.scpiinvestplusapi.batch.reader;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.enums.ScpiField;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component

public class ScpiRequestFieldSetMapper implements FieldSetMapper<ScpiDto> {
    @Override
    public ScpiDto mapFieldSet(FieldSet fieldSet) {
        return ScpiDto.builder()
                .name(fieldSet.readString(ScpiField.NOM.getColumnName()))
                .minimumSubscription(fieldSet.readInt(ScpiField.MINIMUM_SOUSCRIPTION.getColumnName()))
                .manager(fieldSet.readString(ScpiField.GERANT.getColumnName()))
                .capitalization(fieldSet.readBigDecimal(ScpiField.CAPITALISATION.getColumnName()))
                .subscriptionFees(fieldSet.readFloat(ScpiField.FRAIS_SOUSCRIPTION.getColumnName()))
                .managementCosts(fieldSet.readFloat(ScpiField.FRAIS_GESTION.getColumnName()))
                .enjoymentDelay(fieldSet.readInt(ScpiField.DELAI_JOUISSANCE.getColumnName()))
                .iban(fieldSet.readString(ScpiField.IBAN.getColumnName()))
                .bic(fieldSet.readString(ScpiField.BIC.getColumnName()))
                .scheduledPayment(fieldSet.readBoolean(ScpiField.VERSEMENT_PROGRAMME.getColumnName()))
                .cashback(fieldSet.readBigDecimal(ScpiField.CASHBACK.getColumnName()))
                .advertising(fieldSet.readString(ScpiField.PUBLICITE.getColumnName()))
                .locations(fieldSet.readString(ScpiField.LOCALISATION.getColumnName()))
                .sectors(fieldSet.readString(ScpiField.SECTEURS.getColumnName()))
                .build();
    }
}
