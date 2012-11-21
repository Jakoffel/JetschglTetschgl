package your.billingserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import your.common.rmi.Bill;
import your.common.rmi.BillingServerSecure;
import your.common.rmi.PriceSteps;
import your.common.rmi.exceptions.BillingServerException;

public class BillingServerSecureImpl implements BillingServerSecure {

	private PriceSteps priceSteps;
	private List<Auction> auctions;
	
	public BillingServerSecureImpl() {
		priceSteps = new PriceSteps();
		auctions = Collections.synchronizedList(new ArrayList<Auction>());
	}
	
	@Override
	public synchronized PriceSteps getPriceSteps() throws RemoteException {
		return priceSteps;
	}

	@Override
	public synchronized void createPriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws RemoteException {
		priceSteps.createPriceStep(startPrice, endPrice, fixedPrice, variablePricePercent);
	}

	@Override
	public synchronized void deletePriceStep(double startPrice, double endPrice) throws RemoteException {
		priceSteps.deletePriceStep(startPrice, endPrice);
	}

	@Override
	public synchronized void billAuction(String user, long auctionId, double price) throws RemoteException {
		if (user.length() == 0 || auctionId < 0 || price < 0) {
			throw new BillingServerException("invalid bill-item-values");
		}
		
		auctions.add(new Auction(user, auctionId, price));
	}

	@Override
	public synchronized Bill getBill(String user) throws RemoteException {
		Bill bill = new Bill(user);

		for (Auction auction : auctions) {
			if (auction.user.equals(user)) {
				double feeFixed = priceSteps.getFeeFixed(auction.price);
				double feeVariable = priceSteps.getFeeVariable(auction.price);
				
				bill.add(auction.auctionId, auction.price, feeFixed, feeVariable);
			}
		}
		
		return bill;
	}
	
	private class Auction {
		private String user;
		private long auctionId;
		private double price;
		
		public Auction(String user, long auctionId, double price) {
			super();
			this.user = user;
			this.auctionId = auctionId;
			this.price = price;
		}
	}
}
