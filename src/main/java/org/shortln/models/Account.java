package org.shortln.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String nickname;
    @JsonIgnore
    private String passwordHash;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<LinksGroup> linksGroups;
}
