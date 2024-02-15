package dev.gym.workloadservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "username", nullable = false, unique = true)
    protected String username;

    @Column(name = "firstname", nullable = false)
    protected String firstName;

    @Column(name = "lastname", nullable = false)
    protected String lastName;

    @Column(name = "is_active", nullable = false)
    protected boolean isActive;

    @OneToMany(mappedBy = "trainer", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Training> trainings = new ArrayList<>();
}
