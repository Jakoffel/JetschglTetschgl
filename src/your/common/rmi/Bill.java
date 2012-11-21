package your.common.rmi;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Serializable {

	private static final long serialVersionUID = -2473476414475621920L;

	private ArrayList<BillItem> items;
	private String user;
	
	public Bill(String user) {
		this.user = user;
	}
	
	public void add(long auctionId, double price, double feeFixed, double feeVariableInPercent) {
		items.add(new BillItem(auctionId, price, feeFixed, feeVariableInPercent));
	}
	
	@Override
	public String toString() {
		String str = "";
		
		for (BillItem item : items) {
			str += item + "\n";
		}
		
		return str;
	}
	
	public class BillItem {
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
			return auctionId + " " + price + " " + feeFixed + " " + feeVariableInPercent + calculateFeeTotal();
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
