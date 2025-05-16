package com.empik.empiktask.coupon;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.error.TaskAppException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class CouponNormalizingRepository implements CouponRepository {

    private final CouponJpaRepository repository;
    private final CouponUserJpaRepository couponUserRepository;

    @Override
    public boolean couponWithCodeExists(String code) {
        return repository.existsCouponByCodeIgnoreCase(code);
    }

    @Override
    public Coupon createNew(Coupon newCoupon) {
        CouponEntity entity = new CouponEntity();
        entity.setCoupon(newCoupon);
        return repository.save(entity).getCoupon();
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        Optional<CouponEntity> found = repository.findByCodeIgnoreCase(code);
        if (found.isPresent()) {
            return found.map(CouponEntity::getCoupon);
        }
        return Optional.empty();
    }

    @Override
    public boolean couponCodeUsedByUser(String code, String userId) {
        return couponUserRepository.existsByCouponCodeIgnoreCaseAndUserId(code, userId);
    }

    @Override
    public void registerUserUsage(String code, String userId) {
        couponUserRepository.save(CouponUsedByUserEntity.from(code, userId));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
        couponUserRepository.deleteAll();
    }

    @Override
    public Coupon updateUsage(Coupon coupon) {
        CouponEntity entity = repository.findByCodeIgnoreCase(coupon.getCode().formattedCode())
            .orElseThrow(() -> new TaskAppException("Coupon not found"));
        entity.updateCouponUsage(coupon);
        return repository.save(entity).getCoupon();
    }

    @Repository
    interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {

        boolean existsCouponByCodeIgnoreCase(String code);

        Optional<CouponEntity> findByCodeIgnoreCase(String code);
    }

    @Repository
    interface CouponUserJpaRepository extends JpaRepository<CouponUsedByUserEntity, Long> {

        boolean existsByCouponCodeIgnoreCaseAndUserId(String couponCode, String userId);
    }

    @Data
    @Entity
    @Table(name = "t_coupon")
    @NoArgsConstructor
    static class CouponEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "c_id")
        private Long id;

        @Column(name = "c_coupon_id")
        private UUID couponId;

        @Column(name = "c_code")
        private String code;

        @Column(name = "c_created_at")
        private LocalDate createdAt;

        @Column(name = "c_max_usage")
        private Integer maxUsage;

        @Column(name = "c_actual_usage")
        private Integer actualUsage;

        @Column(name = "c_country")
        @Enumerated(EnumType.STRING)
        private CountryCode countryCode;

        @Version
        @Column(name = "c_version")
        private Long version;

        public void setCoupon(Coupon newCoupon) {
            this.couponId = newCoupon.getCouponId();
            this.code = newCoupon.getCode().formattedCode();
            this.createdAt = newCoupon.getCreatedAt();
            this.maxUsage = newCoupon.getMaxUsage();
            this.actualUsage = newCoupon.getActualUsage();
            this.countryCode = newCoupon.getCountry();
            this.version = newCoupon.getVersion();
        }

        public void updateCouponUsage(Coupon coupon) {
            this.actualUsage = coupon.getActualUsage();
            this.version = coupon.getVersion();
        }

        public Coupon getCoupon() {
            return new Coupon(
                this.couponId,
                new CouponCode(this.code),
                this.createdAt,
                this.maxUsage,
                this.actualUsage,
                this.countryCode,
                this.version
            );
        }
    }

    @Data
    @Entity
    @Table(name = "t_coupon_user")
    @NoArgsConstructor
    static class CouponUsedByUserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "cu_id")
        private Long id;

        @Column(name = "cu_user_id")
        private String userId;

        @Column(name = "cu_coupon_code")
        private String couponCode;

        @CreationTimestamp
        @Column(name = "cu_used_at")
        private LocalDateTime usedAt;

        static CouponUsedByUserEntity from(String code, String userId) {
            CouponUsedByUserEntity result = new CouponUsedByUserEntity();
            result.setCouponCode(code);
            result.setUserId(userId);
            return result;
        }
    }
}
