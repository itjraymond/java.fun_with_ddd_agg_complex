package ca.jent.dddjdbccomplex.dao.subscriptionoffer;

import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge;
import ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.SubscriptionOffer;
import ca.jent.dddjdbccomplex.config.Config;
import ca.jent.dddjdbccomplex.types.Price;
import ca.jent.dddjdbccomplex.types.Sku;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlan.*;
import static ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.RatePlanCharge.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureJson
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SubscriptionOfferDao.class)
@ActiveProfiles("test")
class SubscriptionOfferDaoTest {

    @Autowired
    SubscriptionOfferDao subscriptionOfferDao;

    @Autowired
    ObjectMapper mapper;

    @Test
    void save() throws JsonProcessingException {
        Sku sku = Sku.of("10000000");
        Sku protectionSkuMb = Sku.of("20000000");
        Sku protectionSkuNonMb = Sku.of("30000000");

        RatePlanCharge rpch = RatePlanChargeHardware.of(sku, "rate plan charge hardware", Price.of("1598.79"));
        RatePlanCharge rpcm = RatePlanChargeMbProtection.of(protectionSkuMb, "rate plan charge mb protection", Price.of("10.68"));
        RatePlanCharge rpcn = RatePlanChargeNonMbProtection.of(protectionSkuNonMb, "rate plan charge non member protection", Price.of("15.87"));

        RatePlan rph = RatePlanHardware.of(
                null,
                sku,
                "rate plan hardware",
                LocalDateTime.parse("2023-07-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME).atZone(Config.zoneId),
                LocalDateTime.parse("2023-07-09T00:00:00", DateTimeFormatter.ISO_DATE_TIME).atZone(Config.zoneId),
                (RatePlanChargeHardware) rpch
        );

        RatePlan rphm = RatePlanHardwareWithMbProtection.of(
                null,
                sku,
                "rate plan hardware with member protection",
                LocalDateTime.parse("2023-07-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME).atZone(Config.zoneId),
                LocalDateTime.parse("2023-07-09T00:00:00", DateTimeFormatter.ISO_DATE_TIME).atZone(Config.zoneId),
                (RatePlanChargeHardware) rpch,
                (RatePlanChargeMbProtection) rpcm
        );

        RatePlan rphn = RatePlanHardwareWithNonMbProtection.of(
                null,
                sku,
                "rate plan hardware with non-member protection",
                LocalDateTime.parse("2023-07-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME).atZone(Config.zoneId),
                LocalDateTime.parse("2023-07-09T00:00:00", DateTimeFormatter.ISO_DATE_TIME).atZone(Config.zoneId),
                (RatePlanChargeHardware) rpch,
                (RatePlanChargeNonMbProtection) rpcn
        );

        SubscriptionOffer subscriptionOffer = SubscriptionOffer.of(
                null,
                sku,
                15,
                (RatePlanHardware) rph,
                (RatePlanHardwareWithMbProtection) rphm,
                (RatePlanHardwareWithNonMbProtection) rphn
        );

        SubscriptionOffer savedSubscriptionOffer = subscriptionOfferDao.save(subscriptionOffer);

        assertNotNull(savedSubscriptionOffer.getId());
        assertNotNull(savedSubscriptionOffer.getRatePlanHardware().getId());

        Optional<SubscriptionOffer> subscriptionOfferWithIds = subscriptionOfferDao.findById(savedSubscriptionOffer.getId());

        assertTrue(subscriptionOfferWithIds.isPresent());

        String jsonSubscriptionPackage = mapper.writeValueAsString(subscriptionOfferWithIds);

        SubscriptionOffer subPackage = mapper.readValue(jsonSubscriptionPackage, SubscriptionOffer.class);

        assertNotNull(subPackage.getId());
        assertNotNull(subPackage.getRatePlanHardwareWithMbProtection().getId());
    }

    @Test
    void findById() {
    }
}