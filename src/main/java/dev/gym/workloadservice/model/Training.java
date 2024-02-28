package dev.gym.workloadservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration", nullable = false)
    private int trainingDuration;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Trainer trainer;
}
