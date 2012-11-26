package your.common.rmi;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Serializable {

	private static final long serialVersionUID = -2473476414475621920L;

	private ArrayList<BillItem> items;
	private String user;
	
	public Bill(String user) {
		items = new ArrayList<BillItem>();
		this.user = user;
	}
	
	public void add(long auctionId, double price, double feeFixed, double feeVariableInPercent) {
		items.add(new BillItem(auctionId, price, feeFixed, feeVariableInPercent));
	}
	
	@Override
	public String toString() {
		String str = "auction-ID strike_price fee_fixed fee_variable fee_total\n";
		
		for (BillItem item : items) {
			str += item + "\n";
		}
		
		return str;
	}
	
	public class BillItem implements Serializable {

		private static final long serialVersionUID = 1L;
		private long auctionId;
		private double price;
		
		private double feeFixed;
		private double feeVariableInPercent;

		public BillItem(long auctionId, double price, double feeFixed, double feeVariableInPercent) {
			super();
			this.auctionId = auctionId;
			this.price = price;
			this.feeFixed = feeFixed;
			this.feeVariableInPercent = feeVariableInPercent;
		}

		@Override
		public String toString() {
			String auctionIdStr = String.format("%-11s", auctionId);
			String priceStr = String.format("%-13s", price);
			String feeFixedStr = String.format("%-10s", feeFixed);
			String feeVariableInPercentStr = String.format("%-13s", feeVariableInPercent);
			String total = String.format("%-9s", calculateFeeTotal());
			return auctionIdStr + priceStr + feeFixedStr + feeVariableInPercentStr + total;
		}

		private double calculateFeeTotal() {
			double total = feeFixed;
			total += price * feeVariableInPercent / 100;
			
			return total;
		}

		public String getUser() {
			return user;
		}

		public long getAuctionId() {
			return auctionId;
		}

		public double getPrice() {
			return price;
		}

		public double getFeeFixed() {
			return feeFixed;
		}

		public double getFeeVariableInPercent() {
			return feeVariableInPercent;
		}
	}
}
