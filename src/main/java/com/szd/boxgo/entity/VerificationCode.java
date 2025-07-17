package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "verification_codes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "code")
    String code;

    @Column(name = "email")
    String email;

    @Column(name = "purpose")
    String purpose;

    @Column(name = "is_used")
    Boolean isUsed;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @EqualsAndHashCode.Exclude
    OffsetDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    @EqualsAndHashCode.Exclude
    OffsetDateTime expiresAt;
}
