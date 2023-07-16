package ca.jent.dddjdbccomplex.dao.subscriptionoffer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionOfferRepository extends CrudRepository<SubscriptionOfferDao.SubscriptionOfferEntity, Long> {
}
