package com.example.resource_management_api.asset.history;

import com.example.resource_management_api.asset.Asset;
import com.example.resource_management_api.asset.AssetStatus;
import com.example.resource_management_api.global.common.BaseEntity;
import com.example.resource_management_api.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "asset_status_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AssetStatusHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus toStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Enumerated(EnumType.STRING)
    private StatusChangeReason reason;
}