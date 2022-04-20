package com.acme.mytrader.strategy;

import com.acme.mytrader.price.PriceSource;
import com.acme.mytrader.execution.ExecutionService;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Matchers.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    TradingStrategy tradingStrategy;
    @Mock PriceSource priceSource;
    @Mock ExecutionService broker;
    
    @Before
    public void init() {
        tradingStrategy = new TradingStrategy(broker, 100);
        tradingStrategy.setBuyThreshold("IBM", 55.0);
        tradingStrategy.setSellThreshold("IBM", 550.0);
    }
    
    @Test
    public void testBuyRationale() {
        // this will check Updated the price ABOVE buy-threshold.
        tradingStrategy.priceUpdate("IBM", 56.0);

        // it will check there is no buy oders should be placed.
        Mockito.verify(broker, Mockito.times(0)).buy(anyString(), anyDouble(), anyInt());
        tradingStrategy.priceUpdate("IBM", 54.0);

        // this will trigger when IBM price dropped below threshold. than Exactly one buy order must be placed.
        Mockito.verify(broker, Mockito.times(1)).buy("IBM", 54.0, 100);
    }
}