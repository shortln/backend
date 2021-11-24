package org.shortln.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkLog {
    enum Type {
        ACCESS(0);
        @Getter @Setter
        private int value;

        Type(int value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private Type type;
    private Timestamp ctime;


    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH}, optional=false)
    @JoinColumn(name="link_id")
    private Link link;
}
