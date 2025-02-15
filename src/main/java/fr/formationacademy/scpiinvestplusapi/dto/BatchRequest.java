package fr.formationacademy.scpiinvestplusapi.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import java.math.BigDecimal;


@Data
public class BatchRequest {
    @CsvBindByName(column = "Nom")
    private String nom;

    @CsvBindByName(column = "taux_distribution")
    private Float tauxDistribution;

    @CsvBindByName(column = "minimum_souscription")
    private Integer minimumSouscription;

    @CsvBindByName(column = "localisation")
    private String localisation;

    @CsvBindByName(column = "secteurs")
    private String secteurs;

    @CsvBindByName(column = "prix_part")
    private Float prixPart;

    @CsvBindByName(column = "capitalisation")
    private BigDecimal capitalisation;

    @CsvBindByName(column = "Gérant")
    private String gerant;

    @CsvBindByName(column = "frais_souscription")
    private Float fraisSouscription;

    @CsvBindByName(column = "frais_gestion")
    private Float fraisGestion;

    @CsvBindByName(column = "delai_jouissance/mois")
    private Integer delaiJouissance;

    @CsvBindByName(column = "fréquence_loyers")
    private String frequenceLoyers;

    @CsvBindByName(column = "valeur_reconstitution")
    private Float valeurReconstitution;
}
