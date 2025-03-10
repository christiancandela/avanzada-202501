package com.example.demo.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Document("reportes")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Report {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String title;

    private String description;

    private LocalDateTime date;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    @DBRef
    private List<Category> categories;

    private ObjectId idUser;

    private ReportStatus status;

    private int ratingsImportant; // Número de veces que el reporte ha sido marcado como importante
}