package org.choongang.file.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.BaseMember;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name="idx_fInfo_gid", columnList = "gid"),
        @Index(name="idx_fInfo_gid_loc", columnList = "gid,location")
})
public class FileInfo extends BaseMember {

    @Id @GeneratedValue
    private Long seq;

    @Column(length = 65, nullable = false)
    private String gid= UUID.randomUUID().toString();

    @Column(length = 65)
    private String location;

    @Column(length = 80)
    private String fileName;

    @Column(length = 30)
    private String extension;

    @Column(length = 65)
    private String fileType;

    @Transient
    private String filePath;

    @Transient
    private String fileUrl;

    @Transient
    private List<String> thumbsPath;

    @Transient
    private List<String> thumbsUrl;

    private boolean done;


}
