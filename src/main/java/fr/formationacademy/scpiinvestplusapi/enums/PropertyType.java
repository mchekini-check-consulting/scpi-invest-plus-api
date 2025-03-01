package fr.formationacademy.scpiinvestplusapi.enums;

public enum PropertyType {
    PLEINE_PROPRIETE("Pleine propriété"),
    NUE_PROPRIETE("Nue-propriétaire"),
    USUFRUIT("Usufruit");

    private final String label;

    PropertyType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}