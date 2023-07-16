
create TABLE subscriptionoffer (
  id                        bigserial primary key ,
  sku                       VARCHAR(10),
  department                INT
);

create TABLE rateplan (
    id          bigserial primary key ,
    offer_type              VARCHAR(50),
    sku                     VARCHAR(10),
    name                    VARCHAR(100),
    effective_start_date    TIMESTAMP WITH TIME ZONE,
    effective_end_date      TIMESTAMP WITH TIME ZONE,
    subscriptionoffer bigint,
    FOREIGN KEY (subscriptionoffer) references subscriptionoffer(id)
);

create TABLE rateplancharge (
    sku                     VARCHAR(10),
    product_type            VARCHAR(25),
    name                    VARCHAR(100),
    price                   NUMERIC,
    rateplan                bigint,
    FOREIGN KEY (rateplan) references rateplan(id)
);