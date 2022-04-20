package com.acme.mytrader.strategy;

import java.util.HashMap;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy implements PriceListener {

	private Integer lot;
	private ExecutionService myBroker;
	private HashMap<String, Double> buyThresholds;
	private HashMap<String, Double> sellThresholds;
	private static Integer defaultLot = 100;

	public TradingStrategy(ExecutionService broker, Integer lot) {
		this.myBroker = broker;
		this.buyThresholds = new HashMap<String, Double>();
		this.sellThresholds = new HashMap<String, Double>();
		this.lot = lot;
	}

	public TradingStrategy(ExecutionService broker) {
		this(broker, defaultLot);
	}

	// implementation of PriceListener interface
	public void priceUpdate(String security, double price) {
		meanReverseionStrategy(security, price);
	}

	/**
	 * Core method of the task which uses the Mean Reversion strategy for triggering trading ordres. 
	 * as per given exercise, with some Assumption the price will bounce back or fall back from current price, 
	 * So buy or sell at low or high prices.
	 * 
	 * @param is used for security 	: The name of the security (instrument)
	 * @param is used for price 	: The current market price of the instrument.
	 */
	private void meanReverseionStrategy(String security, Double price) {
		
		Double buyThreshold = buyThresholds.get(security);
		Double sellThreshold = sellThresholds.get(security);
		
		if (buyThreshold != null && price < buyThreshold) {
			myBroker.buy(security, price, getLot());
		}
		if (sellThreshold != null && price > sellThreshold) {
			myBroker.sell(security, price, getLot());
		}
	}

	/**
	 * to Sets the BUY threshold for the given security.
	 * @param is used for security : The name of the security (instrument)
	 * @param is used for threshold: The threshold, below which a buy order should be placed.
	 */
	public void setBuyThreshold(String security, Double buyThreshold) {
		// IDEA: Check if the buy threshold is lower than an existing BUY threshold.
		// this case is undefined in the user story.
		buyThresholds.put(security, buyThreshold);
	}

	/**
	 * to Sets the SELL threshold for the given security.
	 * @param is used for security : The name of the security (instrument)
	 * @param is used for threshold: The threshold, above which a buy order should be placed.
	 */
	public void setSellThreshold(String security, Double sellThreshold) {
		// IDEA: Check if the buy threshold is lower than an existing SELL threshold.
		// this case is undefined in the user story.
		sellThresholds.put(security, sellThreshold);
	}

	// to clears all buy/sell price thresholds.
	 
	public void clearTresholds() {
		sellThresholds.clear();
		buyThresholds.clear();
	}

	// setter and getter for lot

	public void setLot(Integer lot) {
		this.lot = lot;
	}

	public Integer getLot() {
		return lot;
	}
}
