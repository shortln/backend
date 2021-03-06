package org.shortln.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Link {
    @Data
    @NoArgsConstructor
    public static class Expire implements Serializable {
        public enum Type {
            FOREVER("forever"), LIMITED("limited");
            @Setter
            private String value;

            Type(String value) { this.value = value; }

            @JsonValue
            public String getValue() {
                return value;
            }
        }

        private Type type = Type.FOREVER;
        private Timestamp datetime;
    }
    public static enum Status {
        USE((short) 0), BAN((short) 1);
        @Getter @Setter
        private short value;

        Status(short value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String jumpUrl;
    private Timestamp ctime;
    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private Expire expire;
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH}, optional=false)
    @JoinColumn(name="gid")
    private LinksGroup group;

    @JsonIgnore
    @OneToMany(mappedBy = "link", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<LinkLog> linkLogs;

    @PrePersist
    void preInsert() {
        if (ctime == null)
            ctime = new Timestamp(new Date().getTime());
    }
}
