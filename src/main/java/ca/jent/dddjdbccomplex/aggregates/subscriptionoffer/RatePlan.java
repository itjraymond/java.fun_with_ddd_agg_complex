package ca.jent.dddjdbccomplex.aggregates.subscriptionoffer;

import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge.RatePlanChargeHardware;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge.RatePlanChargeMbProtection;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge.RatePlanChargeNonMbProtection;
import ca.jent.dddjdbccomplex.types.OfferType;
import ca.jent.dddjdbccomplex.types.Sku;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.ZonedDateTime;

import static ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan.*;

public sealed interface RatePlan permits RatePlanHardware, RatePlanHardwareWithMbProtection, RatePlanHardwareWithNonMbProtection {

    OfferType getOfferType();

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    final class RatePlanHardware implements RatePlan {
        Long id;
        static OfferType offerType = OfferType.HARDWARE_ONLY;
        Sku sku;
        String name;
        ZonedDateTime effectiveStartDate;
        ZonedDateTime effectiveEndDate;
        RatePlanChargeHardware ratePlanChargeHardware;

        @Override
        public OfferType getOfferType() {
            return offerType;
        }

        // factory method
        public static RatePlan of(
                Long id,
                Sku sku,
                String name,
                ZonedDateTime effectiveStartDate,
                ZonedDateTime effectiveEndDate,
                RatePlanChargeHardware ratePlanChargeHardware
        ) {
            return new RatePlanHardware(
                    id,
                    sku,
                    name,
                    effectiveStartDate,
                    effectiveEndDate,
                    ratePlanChargeHardware
            );
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    final class RatePlanHardwareWithMbProtection implements RatePlan {
        Long id;
        static OfferType offerType = OfferType.HARDWARE_WITH_MB_PROTECTION;
        Sku sku;
        String name;
        ZonedDateTime effectiveStartDate;
        ZonedDateTime effectiveEndDate;
        RatePlanChargeHardware ratePlanChargeHardware;
        RatePlanChargeMbProtection ratePlanChargeMbProtection;

        @Override
        public OfferType getOfferType() {
            return offerType;
        }

        public static RatePlan of(
                Long id,
                Sku sku,
                String name,
                ZonedDateTime effectiveStartDate,
                ZonedDateTime effectiveEndDate,
                RatePlanChargeHardware ratePlanChargeHardware,
                RatePlanChargeMbProtection ratePlanChargeMbProtection
        ) {
            return new RatePlanHardwareWithMbProtection(
                    id,
                    sku,
                    name,
                    effectiveStartDate,
                    effectiveEndDate,
                    ratePlanChargeHardware,
                    ratePlanChargeMbProtection
            );
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    final class RatePlanHardwareWithNonMbProtection implements RatePlan {

        Long id;
        static OfferType offerType = OfferType.HARDWARE_WITH_NONMB_PROTECTION;
        Sku sku;
        String name;
        ZonedDateTime effectiveStartDate;
        ZonedDateTime effectiveEndDate;
        RatePlanChargeHardware ratePlanChargeHardware;
        RatePlanChargeNonMbProtection ratePlanChargeNonMbProtection;

        @Override
        public OfferType getOfferType() {
            return offerType;
        }

        public static RatePlan of(
                Long id,
                Sku sku,
                String name,
                ZonedDateTime effectiveStartDate,
                ZonedDateTime effectiveEndDate,
                RatePlanChargeHardware ratePlanChargeHardware,
                RatePlanChargeNonMbProtection ratePlanChargeNonMbProtection
        ) {
            return new RatePlanHardwareWithNonMbProtection(
                    id,
                    sku,
                    name,
                    effectiveStartDate,
                    effectiveEndDate,
                    ratePlanChargeHardware,
                    ratePlanChargeNonMbProtection
            );
        }
    }
}
