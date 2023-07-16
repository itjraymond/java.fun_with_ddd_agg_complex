package ca.jent.dddjdbccomplex.dao.subscriptionoffer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import static ca.jent.dddjdbccomplex.dao.subscriptionoffer.SubscriptionOfferDao.*;

@Repository
public interface SubscriptionOfferRepository extends CrudRepository<SubscriptionOfferEntity, Long> {
}
