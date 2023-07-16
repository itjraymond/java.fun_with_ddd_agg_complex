package ca.jent.dddjdbccomplex.aggregates.subscriptionoffer;

import ca.jent.dddjdbccomplex.types.Price;
import ca.jent.dddjdbccomplex.types.ProductType;
import ca.jent.dddjdbccomplex.types.Sku;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge.*;

public sealed interface RatePlanCharge permits RatePlanChargeHardware, RatePlanChargeMbProtection, RatePlanChargeNonMbProtection  {

    ProductType getProductType();

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    final public class RatePlanChargeHardware implements RatePlanCharge {
        Sku sku;
        static ProductType productType = ProductType.HARDWARE;
        String name;
        Price price;

        @Override
        public ProductType getProductType() {
            return productType;
        }

        public static RatePlanCharge of(Sku sku, String name, Price price) {
            return new RatePlanChargeHardware(sku,name,price);
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    final public class RatePlanChargeMbProtection implements RatePlanCharge {
        Sku sku;
        static ProductType productType = ProductType.MB_PROTECTION;
        String name;
        Price price;

        @Override
        public ProductType getProductType() {
            return productType;
        }

        public static RatePlanCharge of(Sku sku, String name, Price price) {
            return new RatePlanChargeMbProtection(sku,name,price);
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    final public class RatePlanChargeNonMbProtection implements RatePlanCharge {
        Sku sku;
        static ProductType productType = ProductType.NONMB_PROTECTION;
        String name;
        Price price;

        @Override
        public ProductType getProductType() {
            return productType;
        }

        public static RatePlanCharge of(Sku sku, String name, Price price) {
            return new RatePlanChargeNonMbProtection(sku,name,price);
        }
    }
}
