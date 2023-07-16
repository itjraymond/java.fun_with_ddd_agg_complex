# java Fun with DDD / Aggregate Root with some complexity.

Demo using sealed class and spring data jdbc to persist aggregate root.

## Description

I was working with an online retail store and while working for this company, there was a good business domain 
that I could use to demo Spring Data JDBC, Aggregate Root concept (DDD) and SUM type (sealed class) that our
team implemented but did not leverage advanced concepts such as aggregate root and sealed class.

In this demo, I will implement the following business concept: SubscriptionOffers

A subscription offer is composed of a product (the one that we want a customer to subscript for $) and three
rate plan choices: 

1. Just the product (we will be calling this: HARDWARE_ONLY - because for our project we will assume laptop subscriptions)
2. The product with a member protection price (we will call this HARDWARE_WITH_MB_PROTECTION)
3. The product with a non-member protection price (we will call this HARDWARE_WITH_NONMB_PROTECTION)

So essentially, a customer would decide if he wants to subscribe to just the laptop (HARDWARE_ONLY), or if he
is a member, subscribe to the laptop and a small cost for protection (i.e. free repairs) or if he is not a
member, subscript to the laptop and a small cost for protect (usually higher than member).

But a customer will have this choice.

We notice that for a given hardware laptop, there will always be three Rate Plan choices:

1. HARDWARE_ONLY
2. HARDWARE_WITH_MB_PROTECTION
3. HARDWARE_WITH_NONMB_PROTECTION

Each of these 3 choice will be composed of their appropriate charges (cost).  So we will end up with:

1. RatePlan = HARDWARE_ONLY 
   - `RatePlanCharge Hardware` (e.g. $1599.98) 
2. RatePlan = HARDWARE_WITH_MB_PROTECTION 
   - `RatePlanCharge Hardware` (e.g. $1599.98)
   - `RatePlanCharge Mb Protection` (e.g. $10.52)
3. RatePlan = HARDWARE_WITH_NONMB_PROTECTION
    - `RatePlanCharge Hardware` (e.g. $1599.98)
    - `RatePlanCharge NonMb Protection` (e.g. $16.23)

So we can see the data structure is going to be very specific but at the same time we want to treat 
those as RatePlan in general for some situation and very specifically in other situation.  To accomplish
that we will leverage the sealed interface RatePlan with three specific implementation.  We will also
use sealed interface for RatePlanCharge: HARDWARE, MB_PROTECTION, NONMB_PROTECTION.  Those 3 rate plan charges are
actually considered "product".  For instance, a "Non-member protection" is a product that can be purchased for a cost.

## Some observation

The Rate Plan with Rate Plan Charges are very strict structure.  For example, a Rate Plan HARDWARE_WITH_MB_PROTECTION
is composed of one HARDWARE rate plan charge and one MB_PROTECTION rate plan; not a NONMB_PROTECTION. This is different
than something like Address.  An Address can be CANADIAN_ADDRESS or US_ADDRESS or INTERNATIONAL_ADDRESS (a SUM Type like 
Rate Plan) but unlike a subscription with 3 rate plan, an address is just it: an address with no other constraints.
The subscription offer has to have 3 rate plan and each rate plan has to have their specific rate plan charges.

## Aggregate Root

That is why the Subscription Offer makes a good aggregate root.  A Subscription will be composed of some subscription
information, 3 Rate Plan and each Rate Plan composed of Rate Plan Charges.  The aggregate root will expose operations
to be performed on a "subscription offer".

## Some type alias

We will create few type alias (actually type alias is not possible in Java but we will try to come close).

We will need a Sku (Stock-Keeping unit) which is made of 6 to 10 characters (String).  But we will make a type Sku and
java record will be nice for that.

We will also need Price to represent a cost or amount.  Hence we will create a Price type that extends BigDecimal.

We will have an enum OfferType which will represent which kind of Rate Plan we have. 

    - HARDWARE_ONLY
    - HARDWARE_WITH_MB_PROTECTION
    - HARDWARE_WITH_NONMB_PROTECTION

We will have a ProductType to represent if the product is a Laptop (hardware) or a protection (member or non-member).

    - HARDWARE
    - MB_PROTECTION
    - NONMB_PROTECTION

## Database

Since we are going to use an aggregate root (subscription offer), we will use spring data jdbc (not jpa). 
This will show case what kind of "work" we need to do in order to CRUD the aggregate in a postgresql. This will
turn out to be very neat and very simple. We will see how we store three different RatePlan in a single table
and how to read the data back into the appropriate type.

## Serialization / Deserialization

We will also make sure we can serialize and deserialize our aggregate root.

## java packages

This a part where I will experiment.  I would like to leverage information hidding with package level class. 
This mean that I will create one package for the aggregate root and only the aggregate root will be public.
All the containing Entities or Value Objects will have package level visibility.

All the types such as Sku, Price, OfferType, ProductType and more will be in a 'types' package.