package ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import static ca.jent.dddjdbccomplex.aggregates.subscriptionoffer.dao.SubscriptionOfferDao.*;

@Repository
public interface SubscriptionOfferRepository extends CrudRepository<SubscriptionOfferEntity, Long> {
}
