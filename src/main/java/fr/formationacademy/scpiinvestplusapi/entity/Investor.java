package fr.formationacademy.scpiinvestplusapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Investor {

    @Id
    private String email;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private Integer annualIncome;
    private String phoneNumber;
    private String maritalStatus;
    private String numberOfChildren;
    @JsonIgnore
    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Simulation> simulations;

}
