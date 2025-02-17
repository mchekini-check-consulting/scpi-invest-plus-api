package fr.formationacademy.scpiinvestplusapi.enums;

public enum ScpiField {
    NOM("Nom"),
    TAUX_DISTRIBUTION("taux_distribution"),
    MINIMUM_SOUSCRIPTION("minimum_souscription"),
    LOCALISATION("localisation"),
    SECTEURS("secteurs"),
    PRIX_PART("prix_part"),
    CAPITALISATION("capitalisation"),
    GERANT("Gerant"),
    FRAIS_SOUSCRIPTION("frais_souscription"),
    FRAIS_GESTION("frais_gestion"),
    DELAI_JOUISSANCE("delai_jouissance"),
    FREQUENCE_LOYERS("frequence_loyers"),
    VALEUR_RECONSTITUTION("valeur_reconstitution"),
    IBAN("iban"),
    BIC("bic"),
    DECOTE_DEMEMBREMENT("decote_demembrement"),
    DEMEMBREMENT("demembrement"),
    CASHBACK("cashback"),
    VERSEMENT_PROGRAMME("versement_programme"),
    PUBLICITE("publicite"),
    PRIX_DE_LA_PART("prix_part"),
    VALEUR_DE_RECONSTITUTION("valeur_reconstitution");

    private final String columnName;

    ScpiField(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
