package com.example.resource_management_api.asset;

import com.example.resource_management_api.facility.Facility;
import com.example.resource_management_api.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Asset extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus currentStatus;

    public void changeStatus(AssetStatus status) {
        this.currentStatus = status;
    }
}