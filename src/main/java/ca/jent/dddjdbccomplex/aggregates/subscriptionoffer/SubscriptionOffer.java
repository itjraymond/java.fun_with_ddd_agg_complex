package ca.jent.dddjdbccomplex.aggregates.subscriptionoffer;

import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan.RatePlanHardware;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan.RatePlanHardwareWithMbProtection;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan.RatePlanHardwareWithNonMbProtection;
import ca.jent.dddjdbccomplex.types.Sku;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionOffer {
    Long id;
    Sku sku;
    Integer department;
    RatePlanHardware ratePlanHardware;
    RatePlanHardwareWithMbProtection ratePlanHardwareWithMbProtection;
    RatePlanHardwareWithNonMbProtection ratePlanHardwareWithNonMbProtection;

    public static SubscriptionOffer of(Long id, Sku sku, Integer department, RatePlanHardware ratePlanHardware, RatePlanHardwareWithMbProtection ratePlanHardwareWithMbProtection, RatePlanHardwareWithNonMbProtection ratePlanHardwareWithNonMbProtection) {
        return new SubscriptionOffer(id,sku,department,ratePlanHardware,ratePlanHardwareWithMbProtection,ratePlanHardwareWithNonMbProtection);
    }
}
