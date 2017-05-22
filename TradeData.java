package trade.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class TradeData {

	private static final String Sunday = "Sunday";
	private static final String Saturday = "Saturday";
	private static final String Friday = "Friday";
	static String newSettlementDate;
	static Date oldDate;
	static String newDateStr, retainDate;
	static String newDateForTable;
	static SimpleDateFormat sdf;
	static Calendar calendar;
	static String sendStr;
	static String day;
	static String curr;
	static double buyAmount, sellAmount;
	static String entityValue;
	static String settledate;
	static String buysell;
	static boolean flag;
	static double totalAmountBuySell;

	/* Method to increment Date by One day based on currency */

	public static String incrementDateByOneDay() {
		try {
			calendar.setTime(sdf.parse(newDateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DATE, 1); // number of days to add
		newDateForTable = sdf.format(calendar.getTime());
		return newDateForTable;
	}

	/* Method to increment Date by Two days based on currency */

	public static String incrementDateByTwoDay() {
		try {
			calendar.setTime(sdf.parse(newDateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DATE, 2); // number of days to add
		newDateForTable = sdf.format(calendar.getTime());
		return newDateForTable;
	}

	public static String setDate(String date, String currency) {
		curr = currency;
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		calendar = Calendar.getInstance();

		SimpleDateFormat sdfOut = new SimpleDateFormat("EEEE");// format to
																// extract the
																// day from the
																// date
		try {
			oldDate = sdf.parse(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		day = sdfOut.format(oldDate);
		retainDate = sdf.format(oldDate);// to retain this date if date in not a
											// weekend day
		newDateStr = sdf.format(oldDate);

		/* Conditions to check for weekend day baesed on currency */

		switch (day) {
		case Friday:
			if (curr == "AED" || curr == "SAR") {
				sendStr = incrementDateByTwoDay();

			} else
				sendStr = retainDate;
			break;

		case Saturday:
			if (curr == "AED" || curr == "SAR") {
				sendStr = incrementDateByOneDay();
			} else
				sendStr = incrementDateByTwoDay();
			break;

		case Sunday:
			if (!(curr == "AED" || curr == "SAR")) {
				sendStr = incrementDateByOneDay();
				break;
			}
		default:
			sendStr = retainDate;
		}
		return sendStr;

	}

	public static void main(String[] args) {
		/* Hard coded list of data to test the test scenario */

		List<Trade> tradeList = new ArrayList<Trade>();
		tradeList.add(new Trade("ABASUS", "B", 2.0, "KRW", "01/01/2017", "03/02/2017", 1, 10.0, 0.0, false));
		tradeList.add(new Trade("ABASUS", "B", 1.0, "SAR", "01/01/2017", "03/02/2017", 2, 10.0, 0.0, false));
		tradeList.add(new Trade("ABASUS", "S", 1.0, "JPY", "01/01/2017", "02/02/2017", 3, 10.0, 0.0, false));
		tradeList.add(new Trade("DIGECT", "B", 1.0, "AED", "01/02/2017", "11/02/2017", 4, 20.0, 0.0, false));
		tradeList.add(new Trade("TECHNO", "B", 2.0, "GBP", "01/02/2017", "11/02/2017", 1, 110.0, 0.0, false));
		tradeList.add(new Trade("ALPHA", "B", 1.0, "SAA", "01/02/2017", "08/03/2017", 2, 30.0, 0.0, false));
		tradeList.add(new Trade("FOO", "S", 1.0, "CAD", "01/02/2017", "09/02/2017", 3, 40.0, 0.0, false));
		tradeList.add(new Trade("COPE", "B", 1.0, "CHF", "01/02/2017", "09/02/2017", 4, 50.0, 0.0, false));
		tradeList.add(new Trade("BETA", "B", 2.0, "SAR", "01/02/2017", "04/02/2017", 1, 60.0, 0.0, false));
		tradeList.add(new Trade("GAMMA", "B", 1.0, "JPY", "01/02/2017", "10/02/2017", 2, 70.0, 0.0, false));
		tradeList.add(new Trade("HUSH", "S", 1.0, "SAA", "01/02/2017", "18/02/2017", 3, 80.0, 0.0, false));
		tradeList.add(new Trade("BATA", "S", 1.0, "AED", "01/02/2017", "18/02/2017", 4, 90.0, 0.0, false));
		
		/* Edit dates based on currency***/
		for (int i = 0; i < tradeList.size(); i++) {

			String date = tradeList.get(i).getSettlementDate();
			String currency = tradeList.get(i).getCurrency();
			String newDate = setDate(date, currency);
			tradeList.get(i).setSettlementDate(newDate);

		}		
		


		/* for Lopp to calculate USD total amount of each entry */
		double USDamount;

		for (int i = 0; i < tradeList.size(); i++) {
			USDamount = (tradeList.get(i).getAgreedFx()) * (tradeList.get(i).getUnits())
					* (tradeList.get(i).getPricePerUnit());
			tradeList.get(i).setUSDSettlementAmout(USDamount);

		}

		/* New arrayList to extract data from main arrayList */

		List<Trade> amountList = new ArrayList<Trade>();

		for (int i = 0; i < tradeList.size(); i++) {
			amountList.add(new Trade((tradeList.get(i).getEntity()), (tradeList.get(i).getBuySell()),
					(tradeList.get(i).getSettlementDate()), (tradeList.get(i).getUSDSettlementAmout()),
					(tradeList.get(i).getProcessFieldFlag())));

		}

		List<Trade> amountTotalList = new ArrayList<Trade>();// New list to get
																// report based
																// on incoming
																// and outgoing
																// amount

		for (int i = 0; i < amountList.size(); i++) {
			int indexValue = 0;
			entityValue = amountList.get(i).getEntity();
			settledate = amountList.get(i).getSettlementDate();
			buysell = amountList.get(i).getBuySell();
			flag = amountList.get(i).getProcessFieldFlag();
			
			/* Condition to calculate total sell or buy amount based on entity and settlement date */
			
			if (buysell == "B") {
				buyAmount = amountList.get(i).getUSDSettlementAmout();
				sellAmount = 0.0;
			} else {
				sellAmount = amountList.get(i).getUSDSettlementAmout();
				buyAmount = 0.0;
			}
			
			/*If flag is true record is processed*/
			if (flag == false) {

				for (int j = i + 1; j < amountList.size() - 1; j++) {
					if ((entityValue.equals(amountList.get(j).getEntity()))
							&& (settledate.equals(amountList.get(j).getSettlementDate()))) {

						if ((amountList.get(j).getBuySell()) == "B") {
							amountList.get(i).setProcessFieldFlag(true);
							amountList.get(j).setProcessFieldFlag(true);

							buyAmount = buyAmount + amountList.get(j).getUSDSettlementAmout();
						} else if ((amountList.get(j).getBuySell()) == "S") {
							amountList.get(i).setProcessFieldFlag(true);

							amountList.get(j).setProcessFieldFlag(true);
							sellAmount = sellAmount + amountList.get(j).getUSDSettlementAmout();
						}
					}
				}
               /* Create new arraylist to store the total buy or sell amount on */
				if (buyAmount != 0.0) {
					amountTotalList.add(indexValue, new Trade(entityValue, buysell, settledate, buyAmount));
					indexValue++;
				} else if (sellAmount != 0.0) {
					amountTotalList.add(indexValue, new Trade(entityValue, buysell, settledate, sellAmount));
					indexValue++;
				}
			}
		}
		amountTotalList
				.sort(Comparator.comparing(Trade::getSettlementDate).thenComparing(Trade::getTotalSettlementAmount));
		
		
		/* reverse the order to get the hightest amount on top row of produced record */

		Collections.reverse(amountTotalList);
		System.out.println("\n");
		System.out.println("Entity"+"\t"+"BuySell"+"\t"+"SettlementDate"+"\t"+"TotalUSEDAMOUNT"+"\t");
		for (int i = 0; i < amountTotalList.size(); i++) {
	
			System.out.println(  amountTotalList.get(i).getEntity() + "\t" 
					+ amountTotalList.get(i).getBuySell() + "\t"  
					+ amountTotalList.get(i).getSettlementDate() + "\t" + 
					+ amountTotalList.get(i).getTotalSettlementAmount());
		}
		}
	}



/* Class to define all required fields for the  Trade data */

class Trade {

	String Entity;
	String BuySell;
	double AgreedFx;
	String Currency;
	String InstructionDate;
	String SettlementDate;
	int Units;
	double PricePerUnit;
	double USDSettlementAmout;
	boolean ProcessFieldFlag;
	double TotalSettlementAmount;
	

	Trade(String entity, String buySell, double agreedFx, String currency, String instructionDate,
			String settlementDate, int units, double pricePerUnit, double usdSettlementAmount,boolean processFieldFlag) {

		this.Entity = entity;
		this.BuySell = buySell;
		this.AgreedFx = agreedFx;
		this.Currency = currency;
		this.InstructionDate = instructionDate;
		this.SettlementDate = settlementDate;
		this.Units = units;
		this.PricePerUnit = pricePerUnit;
		this.USDSettlementAmout = usdSettlementAmount;
		this.ProcessFieldFlag = processFieldFlag;
	}

	Trade(String entity, String buySell, String settlementDate, double usdSettlementAmount,boolean processField) {

		this.Entity = entity;
		this.BuySell = buySell;
		this.SettlementDate = settlementDate;
		this.USDSettlementAmout = usdSettlementAmount;
		this.ProcessFieldFlag = processField;
	}
	
	Trade(String entity, String buySell, String settlementDate, double totalSettlementAmount) {

		this.Entity = entity;
		this.BuySell = buySell;
		this.SettlementDate = settlementDate;
		this.TotalSettlementAmount = totalSettlementAmount;

	}
	
	Trade( String buySell, String settlementDate, double totalSettlementAmount) {

		this.BuySell = buySell;
		this.SettlementDate = settlementDate;
		this.TotalSettlementAmount = totalSettlementAmount;

	}
	
	
	
	
	
	public boolean getProcessFieldFlag() {
		return ProcessFieldFlag;
	}

	public void setProcessFieldFlag(boolean processField) {
		this.ProcessFieldFlag = processField;
		;
	}

	public String getEntity() {
		return Entity;
	}

	public void setEntity(String entity) {
		this.Entity = entity;
		;
	}

	public String getBuySell() {
		return BuySell;
	}

	public void setBuySell(String buySell) {
		this.BuySell = buySell;
	}

	public Double getAgreedFx() {
		return AgreedFx;
	}

	public void setAgreedFx(double agreedFx) {
		this.AgreedFx = agreedFx;
	}

	public String getCurrency() {
		return Currency;
	}

	public void Currency(String curr) {
		this.Currency = curr;
	}

	public String getInstructionDate() {
		return InstructionDate;
	}

	public void setInstructionDate(String instructiondate) {
		this.InstructionDate = instructiondate;
	}

	public String getSettlementDate() {
		return SettlementDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.SettlementDate = settlementDate;
	}

	public int getUnits() {
		return Units;
	}

	public void setUnits(int unit) {
		this.Units = unit;
	}

	public Double getPricePerUnit() {
		return PricePerUnit;
	}

	public void setPricePerUnit(double unitPrice) {
		this.PricePerUnit = unitPrice;
	}

	public Double getTotalSettlementAmount() {
		return TotalSettlementAmount;
	}

	public void setTotalSettlementAmount(double totalAmount) {
		this.TotalSettlementAmount = totalAmount;
	}
	
	public Double getUSDSettlementAmout() {
		return USDSettlementAmout;
	}

	public void setUSDSettlementAmout(double amount) {
		this.USDSettlementAmout = amount;
	}

}




