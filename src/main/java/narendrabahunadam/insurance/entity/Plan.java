package narendrabahunadam.insurance.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private double coverageAmount;
    private double monthlyPremium;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
