package br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "seat_locks", timeToLive = 30)
public class SeatLockEntity {
    @Id
    private String id;

    @Indexed
    private String customerId;

    private Instant createdAt;
}
