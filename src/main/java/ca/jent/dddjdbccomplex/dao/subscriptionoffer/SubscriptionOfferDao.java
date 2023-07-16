package ca.jent.dddjdbccomplex.dao.subscriptionoffer;

import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge.RatePlanChargeHardware;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.SubscriptionOffer;
import ca.jent.dddjdbccomplex.config.Config;
import ca.jent.dddjdbccomplex.types.OfferType;
import ca.jent.dddjdbccomplex.types.Price;
import ca.jent.dddjdbccomplex.types.ProductType;
import ca.jent.dddjdbccomplex.types.Sku;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan.*;
import static ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge.*;


@Component
@AllArgsConstructor
public class SubscriptionOfferDao {

    private final SubscriptionOfferRepository subscriptionOfferRepository;

    public SubscriptionOffer save(SubscriptionOffer subscriptionOffer) {
        SubscriptionOfferEntity entity = SubscriptionOfferEntity.map(subscriptionOffer);
        SubscriptionOfferEntity savedEntity = subscriptionOfferRepository.save(entity);
        return SubscriptionOfferEntity.map(savedEntity);
    }

    public Optional<SubscriptionOffer> findById(Long id) {
        return subscriptionOfferRepository.findById(id).map(SubscriptionOfferEntity::map);
    }

    @Table("subscriptionoffer")
    record SubscriptionOfferEntity(
            @Id Long id,
            String sku,
            Integer department,
            Set<RatePlanEntity> rateplans
    ){
        static SubscriptionOfferEntity map(SubscriptionOffer subscriptionOffer) {
            return new SubscriptionOfferEntity(
                    subscriptionOffer.getId(),
                    subscriptionOffer.getSku().value(),
                    subscriptionOffer.getDepartment(),
                    Set.of(
                            RatePlanEntity.map(subscriptionOffer.getRatePlanHardware()),
                            RatePlanEntity.map(subscriptionOffer.getRatePlanHardwareWithMbProtection()),
                            RatePlanEntity.map(subscriptionOffer.getRatePlanHardwareWithNonMbProtection())
                    )
            );
        }

        static SubscriptionOffer map(SubscriptionOfferEntity entity) {
            Map<OfferType, RatePlanEntity> rateplans = entity.rateplans.stream().collect(Collectors.toMap(
                    RatePlanEntity::getOfferType,
                    Function.identity()
            ));
            return SubscriptionOffer.of(
                    entity.id,
                    Sku.of(entity.sku),
                    entity.department,
                    (RatePlanHardware) RatePlanEntity.map(rateplans.get(OfferType.HARDWARE_ONLY)),
                    (RatePlanHardwareWithMbProtection) RatePlanEntity.map(rateplans.get(OfferType.HARDWARE_WITH_MB_PROTECTION)),
                    (RatePlanHardwareWithNonMbProtection) RatePlanEntity.map(rateplans.get(OfferType.HARDWARE_WITH_NONMB_PROTECTION))
            );
        }
    }

    @Table("rateplan")
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class RatePlanEntity {
        @Id Long id;
        OfferType offerType;
        String sku;
        String name;
        Instant effectiveStartDate;
        Instant effectiveEndDate;
        Set<RatePlanChargeEntity> ratePlanCharges;

        // factory method
        static RatePlanEntity of(Long id, OfferType offerType, Sku sku, String name, ZonedDateTime effectiveStartDate, ZonedDateTime effectiveEndDate, RatePlanChargeEntity hardware, RatePlanChargeEntity protection) {
            Set<RatePlanChargeEntity> set = protection == null ? Set.of(hardware) : Set.of(hardware, protection);
            return new RatePlanEntity(id, offerType, sku.value(), name, effectiveStartDate.toInstant(), effectiveEndDate.toInstant(), set);
        }

        // mappers
        static RatePlanEntity map(RatePlan ratePlan) {
            return switch (ratePlan) {
                case RatePlanHardware rp -> RatePlanEntity.of(
                        rp.getId(),
                        rp.getOfferType(),
                        rp.getSku(),
                        rp.getName(),
                        rp.getEffectiveStartDate(),
                        rp.getEffectiveEndDate(),
                        RatePlanChargeEntity.map(rp.getRatePlanChargeHardware()),
                        null
                );
                case RatePlanHardwareWithMbProtection rp -> RatePlanEntity.of(
                        rp.getId(),
                        rp.getOfferType(),
                        rp.getSku(),
                        rp.getName(),
                        rp.getEffectiveStartDate(),
                        rp.getEffectiveEndDate(),
                        RatePlanChargeEntity.map(rp.getRatePlanChargeHardware()),
                        RatePlanChargeEntity.map(rp.getRatePlanChargeMbProtection())
                );
                case RatePlanHardwareWithNonMbProtection rp -> RatePlanEntity.of(
                        rp.getId(),
                        rp.getOfferType(),
                        rp.getSku(),
                        rp.getName(),
                        rp.getEffectiveStartDate(),
                        rp.getEffectiveEndDate(),
                        RatePlanChargeEntity.map(rp.getRatePlanChargeHardware()),
                        RatePlanChargeEntity.map(rp.getRatePlanChargeNonMbProtection())
                );
            };
        }

        static RatePlan map(RatePlanEntity entity) {
            Map<ProductType, RatePlanChargeEntity> ratePlanCharges = entity.ratePlanCharges.stream().collect(Collectors.toMap(
                    rpc -> rpc.productType,
                    Function.identity()
            ));
            return switch (entity.offerType) {
                case HARDWARE_ONLY -> RatePlanHardware.of(
                        entity.id,
                        Sku.of(entity.sku),
                        entity.name,
                        ZonedDateTime.ofInstant(entity.effectiveStartDate, Config.zoneId),
                        ZonedDateTime.ofInstant(entity.effectiveEndDate, Config.zoneId),
                        (RatePlanChargeHardware) RatePlanChargeEntity.map(ratePlanCharges.get(ProductType.HARDWARE))
                );
                case HARDWARE_WITH_MB_PROTECTION -> RatePlanHardwareWithMbProtection.of(
                        entity.id,
                        Sku.of(entity.sku),
                        entity.name,
                        ZonedDateTime.ofInstant(entity.effectiveStartDate, Config.zoneId),
                        ZonedDateTime.ofInstant(entity.effectiveEndDate, Config.zoneId),
                        (RatePlanChargeHardware) RatePlanChargeEntity.map(ratePlanCharges.get(ProductType.HARDWARE)),
                        (RatePlanChargeMbProtection) RatePlanChargeEntity.map(ratePlanCharges.get(ProductType.MB_PROTECTION))
                );
                case HARDWARE_WITH_NONMB_PROTECTION -> RatePlanHardwareWithNonMbProtection.of(
                        entity.id,
                        Sku.of(entity.sku),
                        entity.name,
                        ZonedDateTime.ofInstant(entity.effectiveStartDate, Config.zoneId),
                        ZonedDateTime.ofInstant(entity.effectiveEndDate, Config.zoneId),
                        (RatePlanChargeHardware) RatePlanChargeEntity.map(ratePlanCharges.get(ProductType.HARDWARE)),
                        (RatePlanChargeNonMbProtection) RatePlanChargeEntity.map(ratePlanCharges.get(ProductType.NONMB_PROTECTION))
                );
            };
        }
    }

    @Table("rateplancharge")
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class RatePlanChargeEntity {
        String sku;
        ProductType productType;
        String name;
        BigDecimal price;

        // factory method
        static RatePlanChargeEntity of(Sku sku, ProductType productType, String name, Price price) {
            return new RatePlanChargeEntity(sku.value(), productType, name, price);
        }

        // mappers
        static RatePlanChargeEntity map(RatePlanCharge ratePlanCharge) {
            return switch (ratePlanCharge) {
                case RatePlanChargeHardware rpc -> RatePlanChargeEntity.of(rpc.getSku(), rpc.getProductType(), rpc.getName(), rpc.getPrice());
                case RatePlanChargeMbProtection rpc -> RatePlanChargeEntity.of(rpc.getSku(), rpc.getProductType(), rpc.getName(), rpc.getPrice());
                case RatePlanChargeNonMbProtection rpc -> RatePlanChargeEntity.of(rpc.getSku(), rpc.getProductType(), rpc.getName(), rpc.getPrice());
            };
        }

        static RatePlanCharge map(RatePlanChargeEntity ratePlanChargeEntity) {
            return switch (ratePlanChargeEntity.productType) {
                case HARDWARE -> RatePlanChargeHardware.of(Sku.of(ratePlanChargeEntity.sku), ratePlanChargeEntity.name, Price.of(ratePlanChargeEntity.price));
                case MB_PROTECTION -> RatePlanChargeMbProtection.of(Sku.of(ratePlanChargeEntity.sku), ratePlanChargeEntity.name, Price.of(ratePlanChargeEntity.price));
                case NONMB_PROTECTION -> RatePlanChargeNonMbProtection.of(Sku.of(ratePlanChargeEntity.sku), ratePlanChargeEntity.name, Price.of(ratePlanChargeEntity.price));
            };
        }
    }
}
