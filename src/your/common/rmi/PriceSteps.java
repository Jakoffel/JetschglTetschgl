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
		
		newStep.checkNegativeValues();
		
		for (PriceStep step : priceSteps) {
			step.checkOverlapWith(newStep);
		}
		
		priceSteps.add(newStep);
	}
	
	public synchronized void deletePriceStep(double startPrice, double endPrice)	throws RemoteException {
		PriceStep toDelete = new PriceStep(startPrice, endPrice);
		
		if (!priceSteps.contains(toDelete)) {
			throw new BillingServerException("price step does not exist");
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
			this.startPrice = startPrice;
			this.endPrice = endPrice;
			this.fixedPrice = fixedPrice;
			this.variablePricePercent = variablePricePercent;
		}
		
		public PriceStep(double startPrice, double endPrice) {
			this.startPrice = startPrice;
			this.endPrice = endPrice;
		}

		public void checkNegativeValues() throws BillingServerException {
			if (startPrice < 0 || endPrice < 0 || fixedPrice < 0 || variablePricePercent < 0) {
				throw new BillingServerException("no negative values!");
			}
		}

		public void checkOverlapWith(PriceStep other) throws BillingServerException {
			//TODO was besseres einfallen lassen, sacrebleu!!!
			if ((startPrice <= other.startPrice && (other.startPrice < endPrice || endPrice == 0)) ||
				(startPrice <= other.endPrice && (other.endPrice < endPrice || endPrice == 0))) {
				throw new BillingServerException("there is an start/end-price-overlap");
			}
		}
		
		public boolean contains(double price) {
			return startPrice <= price && (price < endPrice || endPrice == 0);
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
	}
}
