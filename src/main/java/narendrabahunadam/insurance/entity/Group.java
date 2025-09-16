package narendrabahunadam.insurance.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Data
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Plan> plans = new ArrayList<>();
}