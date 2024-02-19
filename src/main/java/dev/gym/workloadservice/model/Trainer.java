package dev.gym.workloadservice.model;

import dev.gym.workloadservice.dto.TrainerReportProjection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NamedNativeQuery;

import java.util.ArrayList;
import java.util.List;


@SqlResultSetMapping(
        name = "TrainerReportProjectionMapping",
        classes = {
                @ConstructorResult(
                        targetClass = TrainerReportProjection.class,
                        columns = {
                                @ColumnResult(name = "username", type = String.class),
                                @ColumnResult(name = "firstName", type = String.class),
                                @ColumnResult(name = "lastName", type = String.class),
                                @ColumnResult(name = "isActive", type = Boolean.class),
                                @ColumnResult(name = "year", type = Integer.class),
                                @ColumnResult(name = "month", type = Integer.class),
                                @ColumnResult(name = "totalTrainingDuration", type = Integer.class)
                        }
                )
        }
)
@NamedNativeQuery(
        name = "Trainer.calculateMonthlyReportByUsername",
        query = """
            SELECT 
                tr.username, 
                tr.firstname as firstName, 
                tr.lastname as lastName, 
                tr.is_active as isActive,
                EXTRACT(YEAR FROM tg.training_date) AS `year`,
                EXTRACT(MONTH FROM tg.training_date) AS `month`,
                SUM(tg.training_duration) AS totalTrainingDuration
            FROM 
                Trainer tr
                JOIN Training tg ON tr.id = tg.trainer_id
            WHERE 
                tr.username= ?1
            GROUP BY 
                tr.username, `year`, `month`
            ORDER BY 
                    `year` DESC, `month` DESC
        """,
        resultSetMapping = "TrainerReportProjectionMapping"
)
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
