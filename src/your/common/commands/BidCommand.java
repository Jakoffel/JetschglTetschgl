package your.common.commands;

import java.math.BigDecimal;

public class BidCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -304362476184384818L;
	private int auctionId;
	private BigDecimal amount;
	
	public BidCommand(String userName, int auctionId, BigDecimal amount) {
		super(userName);
	
		this.auctionId = auctionId;
		this.amount = amount;
	}

	public int getAuctionId() {
		return auctionId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}
