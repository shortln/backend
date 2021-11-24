package org.shortln.models;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Link {
    static class Expire implements Serializable {
        public enum Type {
            FOREVER("forever"), LIMITED("limited");
            @Getter @Setter
            private String value;

            Type(String value) { this.value = value; }
        }

        private Type type = Type.FOREVER;
        private Timestamp datetime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Timestamp ctime;
    @org.hibernate.annotations.Type(type = "json")
    @Column(columnDefinition = "json")
    private Expire expire;
    private Short status;

    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH}, optional=false)
    @JoinColumn(name="gid")
    private LinksGroup group;

    @OneToMany(mappedBy = "id", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<LinkLog> linkLogs;
}
