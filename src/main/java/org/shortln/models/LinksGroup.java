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
public class LinksGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "group", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Link> links;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH}, optional=false)
    @JoinColumn(name="author_id")
    private Account author;
}
