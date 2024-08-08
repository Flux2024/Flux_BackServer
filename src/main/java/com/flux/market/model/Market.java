package com.flux.market.model;

import com.flux.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "market")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id", nullable = false)
    private Long marketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "market_title", nullable = false)
    private String marketTitle;

    @ElementCollection
    @CollectionTable(name = "market_images", joinColumns = @JoinColumn(name = "market_id"))
    @Column(name = "image_url")
    private List<String> marketImgs;

    @Column(name = "market_price", nullable = false)
    private double marketPrice;

    @Column(name = "market_max_price")
    private double marketMaxPrice;

    @Column(name = "market_category", nullable = false)
    private String marketCategory;

    @Column(name = "market_contents", nullable = false)
    private String marketContents;

    @Column(name = "market_orderable_status", nullable = false)
    private boolean marketOrderableStatus;

    @Column(name = "user_mail")
    private String userMail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "market_period")
    private String marketPeriod;

    @CreationTimestamp
    @Column(name = "market_create_at", nullable = false, updatable = false)
    private OffsetDateTime marketCreateAt;

    @UpdateTimestamp
    @Column(name = "market_update_at")
    private OffsetDateTime marketUpdateAt;

    @Column(name = "market_sell_date")
    private OffsetDateTime marketSellDate;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private OffsetDateTime endDate;

    @Column(name = "market_view", nullable = false)
    private int marketView;

    @Override
    public String toString() {
        return "Market{" +
                "marketId=" + marketId +
                ", user=" + user +
                ", marketTitle='" + marketTitle + '\'' +
                ", marketImgs=" + marketImgs +
                ", marketPrice=" + marketPrice +
                ", marketMaxPrice=" + marketMaxPrice +
                ", marketCategory='" + marketCategory + '\'' +
                ", marketContents='" + marketContents + '\'' +
                ", marketOrderableStatus=" + marketOrderableStatus +
                ", userMail='" + userMail + '\'' +
                ", userName='" + userName + '\'' +
                ", marketPeriod='" + marketPeriod + '\'' +
                ", marketCreateAt=" + marketCreateAt +
                ", marketUpdateAt=" + marketUpdateAt +
                ", marketSellDate=" + marketSellDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", marketView=" + marketView +
                '}';
    }
}
