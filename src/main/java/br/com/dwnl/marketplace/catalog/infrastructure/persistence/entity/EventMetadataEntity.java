package br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@RequiredArgsConstructor
public class EventMetadataEntity {

    @Id
    private String id;

    @NotNull
    private UUID eventId;

    private String eventDescription;

    private Map<String, Object> technicalRequirements;

    private List<Sector> sectors;

    private List<Seat> seats;

    @CreatedDate
    private Instant createdOn;

    @LastModifiedDate
    private Instant updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sector {
        private String name;
        private BigDecimal price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Seat{
        private String code;
        private String sectorName;
    }
}
