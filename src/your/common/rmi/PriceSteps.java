package your.common.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import your.common.rmi.exceptions.BillingServerException;

public class PriceSteps implements Serializable {

	private static final long serialVersionUID = -5381878532998684196L;
	
	private List<PriceStep> priceSteps;
	
	public PriceSteps() {
		priceSteps = Collections.synchronizedList(new ArrayList<PriceStep>());
	}
	
	public synchronized void createPriceStep(double startPrice, double endPrice,	double fixedPrice, double variablePricePercent) throws BillingServerException {
		PriceStep newStep = new PriceStep(startPrice, endPrice, fixedPrice, variablePricePercent);
		
		newStep.checkValues();		
		checkOverlap(newStep);
		
		priceSteps.add(newStep);
	}

	private void checkOverlap(PriceStep newStep) throws BillingServerException {
		for (PriceStep step : priceSteps) {
			if (step.checkOverlap(newStep)) {
				throw new BillingServerException("there is an start/end-price-overlap");
			}
		}
	}
	
	public synchronized void deletePriceStep(double startPrice, double endPrice)	throws RemoteException {
		PriceStep toDelete = new PriceStep(startPrice, endPrice);
		
		if (!priceSteps.contains(toDelete)) {
			throw new BillingServerException("price step ["+startPrice+" "+endPrice+"]does not exist");
		}
		
		priceSteps.remove(toDelete);
	}
	
	public synchronized double getFeeFixed(double price) {
		PriceStep step = getPriceStep(price);
		
		return step == null ? 0 : step.fixedPrice;
	}

	public synchronized double getFeeVariable(double price) {
		PriceStep step = getPriceStep(price);
		
		return step == null ? 0 : step.variablePricePercent;
	}
	
	@Override
	public synchronized String toString() {
		String str = "\nMin_price Max_price Fee_Fixed Fee_Variable\n";
		
		for(PriceStep step : priceSteps) {
			str += step.toString() + "\n";
		}
		
		return str;
	}
	
	private PriceStep getPriceStep(double price) {
		for (PriceStep step : priceSteps) {
			if (step.contains(price)) {
				return step;
			}
		}
		
		return null;
	}
	
	private class PriceStep implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private double startPrice;
		private double endPrice;
		private double fixedPrice;
		private double variablePricePercent;
		
		public PriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) {
			this(startPrice, endPrice);
			
			this.fixedPrice = fixedPrice;
			this.variablePricePercent = variablePricePercent;
		}
		
		public PriceStep(double startPrice, double endPrice) {
			this.startPrice = startPrice;
			this.endPrice = endPrice == 0d ? Double.MAX_VALUE : endPrice;
		}

		private void checkValues() throws BillingServerException {
			if (startPrice < 0d || endPrice < 0d || fixedPrice < 0d || variablePricePercent < 0d) {
				throw new BillingServerException("no negative values!");
			}
			
			if (startPrice >= endPrice) {
				throw new BillingServerException("startPrice < endPrice");
			}
		}
		
		private boolean contains(double price) {
			return startPrice < price && price <= endPrice;
		}
		
		private boolean checkOverlap(PriceStep other) {
			return contains(other.endPrice) || contains(other.startPrice+1) || //other intervall inside of intervall
					(other.startPrice < startPrice && other.endPrice >= endPrice); //this intervall inside of other intervall
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PriceStep other = (PriceStep) obj;
			if (Double.doubleToLongBits(endPrice) != Double
					.doubleToLongBits(other.endPrice))
				return false;
			if (Double.doubleToLongBits(startPrice) != Double
					.doubleToLongBits(other.startPrice))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			String startPriceStr = String.format("%-10s", startPrice);
			String endPriceStr = String.format("%-10s", endPrice == Double.MAX_VALUE ? 0d : endPrice);
			String fixedPriceStr = String.format("%-10s", fixedPrice);
			String variablePricePercentStr = String.format("%-11s", variablePricePercent);
			return startPriceStr + endPriceStr + fixedPriceStr + variablePricePercentStr;
		}
	}
}
