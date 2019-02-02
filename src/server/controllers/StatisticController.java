package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.ActiviySnapshot;
import common.entity.BookSelected;
import common.entity.BookStatistic;
import common.entity.Statistic;
import common.entity.enums.BookStatsticType;

/**
 * The StatisticController class represent the statistic controller on the
 * server's side
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class StatisticController {
	/** instance is a singleton of the class */
	private static StatisticController instance;

	private StatisticController() {
	}

	/**
	 * getInstance is creating the singleton object of the class
	 */
	public static StatisticController getInstance() {
		if (instance == null) {
			instance = new StatisticController();
		}
		return instance;
	}

	/**
	 * getBookStatstic is getting the general system Book statistic details
	 * 
	 * @param msg contains the message from the client
	 * @return Message
	 */
	public Message getBookStatstic(Object msg) {

		try {
			List<Integer> duration = null;

			switch ((BookStatsticType) ((Message) msg).getObj()) {
			case Popular:
				duration = getBorrowBooksByPopularity(Boolean.TRUE);
				break;
			case Regular:
				duration = getBorrowBooksByPopularity(Boolean.FALSE);
				break;
			case Late:
				duration = getBorrowBooksLates();
				break;
			}

			if (duration == null)
				return new Message(OperationType.GetBookStatstic, null, ReturnMessageType.Unsuccessful);

			float average = getAverage(duration);
			int median = getMedian(duration);
			Map<Integer, List<Integer>> distribution = getDistribution(duration);

			BookStatistic bookStatistic = new BookStatistic(distribution, average, median,
					(BookStatsticType) ((Message) msg).getObj());

			return new Message(OperationType.GetBookStatstic, bookStatistic, ReturnMessageType.Successful);
		} catch (Exception e) {
			e.printStackTrace();
			return new Message(OperationType.GetBookStatstic, null, ReturnMessageType.Unsuccessful);
		}
	}

	/**
	 * getLateBookStatsticByCatId is get Book Statistic obj by catalog num
	 * 
	 * @param msg contains the message from the client
	 * @return Message with BookStatistic
	 */
	public Message getLateBookStatsticByCatId(Object msg) {

		try {
			List<Integer> duration = null;
			duration = getLateBooksByCatId((String) ((Message) msg).getObj());

			if (duration == null || duration.isEmpty())
				return new Message(OperationType.GetAllLatesReturnBySingleBookCatId, null,
						ReturnMessageType.Unsuccessful);

			float average = getAverage(duration);
			int median = getMedian(duration);
			Map<Integer, List<Integer>> distribution = getDistribution(duration);

			BookStatistic bookStatistic = new BookStatistic(distribution, average, median, BookStatsticType.SingleLate);

			return new Message(OperationType.GetAllLatesReturnBySingleBookCatId, bookStatistic,
					ReturnMessageType.Successful);
		} catch (Exception e) {
			e.printStackTrace();
			return new Message(OperationType.GetAllLatesReturnBySingleBookCatId, null, ReturnMessageType.Unsuccessful);
		}
	}

	/**
	 * insertStatisticActiviySnapshot use primarily to insert new statistic activity
	 * snapshot automatic functions.
	 * @throws SQLException when occurs
	 */
	public static void insertStatisticActiviySnapshot() throws Exception {
		String aLockSubQuary = "SELECT COUNT(subStatus) FROM obl.subscribers as a Where a.subStatus='Lock';";
		String aHoldSubQuary = "SELECT COUNT(subStatus) FROM obl.subscribers as a Where a.subStatus='Hold';";
		String aActiveSubQuary = "SELECT COUNT(subStatus) FROM obl.subscribers as a Where a.subStatus='Avtive';";
		String aCopiesQuary = "SELECT COUNT(copyId) FROM obl.copeis";
		String aLatesQuary = "SELECT COUNT(copyId) FROM obl.borrows where returnDueDate <= actualReturnDate;";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet aLockSubQuary_res = dbControllerObj.query(aLockSubQuary);
		ResultSet aHoldSubQuary_res = dbControllerObj.query(aHoldSubQuary);
		ResultSet aActiveSubQuary_res = dbControllerObj.query(aActiveSubQuary);
		ResultSet aCopiesQuary_res = dbControllerObj.query(aCopiesQuary);
		ResultSet aLatesQuary_res = dbControllerObj.query(aLatesQuary);

		if (!(aLockSubQuary_res.next() && aHoldSubQuary_res.next() && aActiveSubQuary_res.next()
				&& aCopiesQuary_res.next() && aLatesQuary_res.next())) {
			throw new Exception("Some of statistic parmer is missing! Can't insert new Activity to DB!");
		}

		LocalDate borrowDate = LocalDate.now();

		String quaryInsertNewActivity = "INSERT INTO `obl`.`activity_statistic` (`aDate`, `aLockSub`, `aHoldSub`, `aActiveSub`, `aCopies`, `aLates`) "
				+ "VALUES ('" + Date.valueOf(borrowDate) + "', '" + aLockSubQuary_res.getInt(1) + "', '"
				+ aHoldSubQuary_res.getInt(1) + "', '" + aActiveSubQuary_res.getInt(1) + "', '"
				+ aCopiesQuary_res.getInt(1) + "', '" + aLatesQuary_res.getInt(1) + "');";

		dbControllerObj.insert(quaryInsertNewActivity);
	}

	/**
	 * getLastSnapshotDate
	 * 
	 * @throws SQLException when occurs
	 * @return last snapshot activity date
	 */
	private Date getLastSnapshotDate() throws Exception {
		String lastSnapshotQuary = "SELECT Max(aDate) FROM obl.activity_statistic;";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet lastSnapshotQuary_res = dbControllerObj.query(lastSnapshotQuary);

		if (!lastSnapshotQuary_res.next()) {
			throw new Exception("Can't Find last Snapshot date");
		}

		return lastSnapshotQuary_res.getDate(1);

	}

	/**
	 * getFirstSnapshotDate
	 * 
	 * @throws SQLException when occurs
	 * @return first snapshot activity date
	 */
	private Date getFirstSnapshotDate() throws Exception {
		String firstSnapshotQuary = "SELECT Min(aDate) FROM obl.activity_statistic;";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet firstSnapshotQuary_res = dbControllerObj.query(firstSnapshotQuary);

		if (!firstSnapshotQuary_res.next()) {
			throw new Exception("Can't Find last Snapshot date");
		}

		return firstSnapshotQuary_res.getDate(1);
	}

	/**
	 * getActivitySnapshotByDate
	 * 
	 * @param date of activity snapshot
	 * @return ActiviySnapshot
	 * @throws SQLException
	 */
	public ActiviySnapshot getActivitySnapshotByDate(Date date) throws SQLException {
		ActiviySnapshot activiySnapshot = null;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String activiySnapshotByDateQuary = "SELECT `aDate`, `aLockSub`, `aHoldSub`, `aActiveSub`, `aCopies`, `aLates` "
				+ "FROM obl.activity_statistic Where aDate = '" + dateFormat.format(date) + "';";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet activiySnapshotByDateQuary_res = dbControllerObj.query(activiySnapshotByDateQuary);

		while (activiySnapshotByDateQuary_res.next()) {
			activiySnapshot = new ActiviySnapshot(activiySnapshotByDateQuary_res.getDate("aDate"),
					activiySnapshotByDateQuary_res.getInt("aLockSub"),
					activiySnapshotByDateQuary_res.getInt("aHoldSub"),
					activiySnapshotByDateQuary_res.getInt("aActiveSub"),
					activiySnapshotByDateQuary_res.getInt("aCopies"), activiySnapshotByDateQuary_res.getInt("aLates"));
		}

		return activiySnapshot;
	}

	/**
	 * getDistribution return map of list, split by decimal distribution
	 * 
	 * @param borrowBookDuration
	 * @throws Exception when occurs
	 * @return distribution
	 */
	private Map<Integer, List<Integer>> getDistribution(List<Integer> borrowBookDuration) throws Exception {

		Map<Integer, List<Integer>> distribution = new HashMap<Integer, List<Integer>>();
		if (borrowBookDuration.isEmpty())
			return distribution;
		Integer max = Collections.max(borrowBookDuration);
		float range = ((float) max / 10);

		// init map
		for (int i = 0; i < 10; i++) {
			distribution.put(i, new ArrayList<Integer>());
		}

		for (Integer duration : borrowBookDuration) {
			float prevSector = 0;
			float nextSector = range;

			for (int i = 0; i < 10; i++) {

				// fix edge case for max value
				if (duration == max) {
					distribution.get(9).add(duration);
					break;
				}

				// find duration distribution
				// prevSector < duration < nextSector
				if (prevSector < duration && duration <= nextSector) {
					distribution.get(i).add(duration);
					break;
				}

				// increasing the range to next sector in distribution
				prevSector += range;
				nextSector += range;
			}
		}

		return distribution;
	}

	/**
	 * getAverage return average of single list distribution
	 * 
	 * @param borrowBookDuration
	 * @throws SQLException when occurs
	 * @return average
	 */
	private float getAverage(List<Integer> borrowBookDuration) {
		int sum = 0;

		for (Integer duration : borrowBookDuration) {
			sum += duration;
		}

		return ((float) sum) / borrowBookDuration.size();
	}

	/**
	 * getMedian return median of single list distribution
	 * 
	 * @param borrowBookDuration
	 * @throws SQLException when occurs
	 * @return average
	 */
	private int getMedian(List<Integer> borrowBookDuration) {
		int median = 0;
		Collections.sort(borrowBookDuration);
		if (borrowBookDuration.size() == 0)
			return 0;

		if (borrowBookDuration.size() % 2 == 0) {
			median = borrowBookDuration.get((borrowBookDuration.size() - 1) / 2);
		} else {
			median = (borrowBookDuration.get((borrowBookDuration.size() - 1) / 2)
					+ borrowBookDuration.get(borrowBookDuration.size() / 2)) / 2;
		}

		return median;
	}

	/**
	 * getBorrowBooksByPopularity return list of calculate duration borrow books.
	 * 
	 * @param isPopular
	 * @throws SQLException when occurs
	 * @return list of calculate duration
	 */
	private List<Integer> getBorrowBooksByPopularity(Boolean isPopular) throws SQLException {
		List<List<Date>> borrowDates = new ArrayList<List<Date>>();
		String borrowBookQuaryByPopularity = "SELECT borrowDate,returnDueDate "
				+ "FROM obl.borrows as a join obl.copeis as b on a.copyID=b.copyID join obl.books as c on b.bCatalogNum=c.bCatalogNum "
				+ "where c.bIsPopular = " + isPopular.toString() + ";";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet borrowBook_res = dbControllerObj.query(borrowBookQuaryByPopularity);

		while (borrowBook_res.next()) {
			List<Date> date = new ArrayList<Date>();

			date.add(borrowBook_res.getDate("borrowDate"));
			date.add(borrowBook_res.getDate("returnDueDate"));

			borrowDates.add(date);
		}

		return calcBorrowDuration(borrowDates);
	}

	/**
	 * getBorrowBooksByPopularity return list of calculate duration borrow books.
	 * 
	 * @param isPopular
	 * @throws SQLException when occurs
	 * @return list of calculate duration
	 */
	private List<Integer> getLateBooksByCatId(String bCatalogNum) throws SQLException {
		List<List<Date>> borrowDates = new ArrayList<List<Date>>();

		String borrowBookQuaryLate = "SELECT c.bCatalogNum ,c.bName ,a.borrowDate, a.returnDueDate, a.actualReturnDate"
				+ " FROM obl.borrows as a join obl.copeis as b on a.copyID=b.copyID join obl.books as c on b.bCatalogNum=c.bCatalogNum"
				+ " where a.actualReturnDate and a.returnDueDate < a.actualReturnDate AND c.bCatalogNum = '"
				+ bCatalogNum + "';";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet borrowBook_res = dbControllerObj.query(borrowBookQuaryLate);

		while (borrowBook_res.next()) {
			List<Date> date = new ArrayList<Date>();

			date.add(borrowBook_res.getDate("borrowDate"));
			date.add(borrowBook_res.getDate("returnDueDate"));

			borrowDates.add(date);
		}

		return calcBorrowDuration(borrowDates);
	}

	/**
	 * getBorrowBooksLates return list of calculate duration late borrow books.
	 * 
	 * @throws SQLException when occurs
	 * @return list of calculate duration
	 */
	private List<Integer> getBorrowBooksLates() throws SQLException {
		List<List<Date>> borrowDates = new ArrayList<List<Date>>();

		String borrowBookQuaryLates = "SELECT returnDueDate ,actualReturnDate "
				+ "FROM obl.borrows as a join obl.copeis as b on a.copyID=b.copyID join obl.books as c on b.bCatalogNum=c.bCatalogNum "
				+ "where a.actualReturnDate and a.returnDueDate < a.actualReturnDate;";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet borrowBook_res = dbControllerObj.query(borrowBookQuaryLates);

		while (borrowBook_res.next()) {
			List<Date> date = new ArrayList<Date>();

			date.add(borrowBook_res.getDate("returnDueDate"));
			date.add(borrowBook_res.getDate("actualReturnDate"));

			borrowDates.add(date);
		}

		return calcBorrowDuration(borrowDates);
	}

	/**
	 * calcBorrowDuration return list of calculate duration of borrow books.
	 * 
	 * @param borrowDates
	 * @return list of calculate duration
	 */
	private List<Integer> calcBorrowDuration(List<List<Date>> borrowDates) {

		List<Integer> borrowDuration = new ArrayList<Integer>();

		for (List<Date> borrowBook : borrowDates) {
			Long diff = borrowBook.get(1).getTime() - borrowBook.get(0).getTime();

			borrowDuration.add(Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
		}

		return borrowDuration;
	}
	/**
	 * getAllLatesReturnBySingleBookCatId return Single Book of calculate duration of borrow books.
	 * 
	 * @param msg
	 * @return list of calculate duration
	 */
	public Message getAllLatesReturnBySingleBookCatId(Message msg) {
		try {
			String bCatalogNum = (String) msg.getObj();
			List<List<Date>> borrowDates = new ArrayList<List<Date>>();
			String borrowBookQuaryLates = "SELECT c.bCatalogNum ,c.bName ,a.borrowDate, a.returnDueDate, a.actualReturnDate"
					+ " FROM obl.borrows as a join obl.copeis as b on a.copyID=b.copyID join obl.books as c on b.bCatalogNum=c.bCatalogNum"
					+ " where a.actualReturnDate and a.returnDueDate < a.actualReturnDate AND c.bCatalogNum = '"
					+ bCatalogNum + "';";

			DBcontroller dbControllerObj = DBcontroller.getInstance();
			ResultSet borrowBook_res = dbControllerObj.query(borrowBookQuaryLates);

			while (borrowBook_res.next()) {
				List<Date> date = new ArrayList<Date>();

				date.add(borrowBook_res.getDate("returnDueDate"));
				date.add(borrowBook_res.getDate("actualReturnDate"));

				borrowDates.add(date);
			}

			BookSelected bookSelected = new BookSelected(borrowBook_res.getInt("bCatalogNum"),
					borrowBook_res.getString("bName"), borrowBook_res.getDate("borrowDate"),
					borrowBook_res.getDate("returnDueDate"), borrowBook_res.getDate("actualReturnDate"),
					getAverage(calcBorrowDuration(borrowDates)), getMedian(calcBorrowDuration(borrowDates)),
					getDistribution(calcBorrowDuration(borrowDates)));

			return new Message(OperationType.GetAllLatesReturnBySingleBookCatId, bookSelected,
					ReturnMessageType.Successful);
		} catch (Exception e) {
			e.printStackTrace();
			return new Message(OperationType.GetAllLatesReturnBySingleBookCatId, null, ReturnMessageType.Unsuccessful);
		}

	}
	/**
	 * getSingleActiviySnapshotByPeriod return List of ActiviySnapshot between given period dates
	 * 
	 * @param msg
	 * @return Message           with list of calculate duration
	 * @exception SQLException      
	 */
	public Message getSingleActiviySnapshotByPeriod(Object msg) throws SQLException {

		Date[] snapshotDates = (Date[]) ((Message) msg).getObj();

		String borrowBookQuaryLates = "SELECT `aDate`, `aLockSub`, `aHoldSub`, `aActiveSub`, `aCopies`, `aLates`"
				+ " FROM obl.activity_statistic as a" + " WHERE  a.aDate >= '" + snapshotDates[0] + "' AND a.aDate <= '"
				+ snapshotDates[1] + "';";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet activiySnapshot_res = dbControllerObj.query(borrowBookQuaryLates);

		List<ActiviySnapshot> activiySnapshots = new ArrayList<ActiviySnapshot>();

		while (activiySnapshot_res.next()) {
			activiySnapshots.add(
					new ActiviySnapshot(activiySnapshot_res.getDate("aDate"), activiySnapshot_res.getInt("aLockSub"),
							activiySnapshot_res.getInt("aHoldSub"), activiySnapshot_res.getInt("aActiveSub"),
							activiySnapshot_res.getInt("aCopies"), activiySnapshot_res.getInt("aLates")));
		}

		if (activiySnapshots.isEmpty())
			return new Message(OperationType.GetActiviySnapshotsByPeriod, null, ReturnMessageType.Unsuccessful);

		return new Message(OperationType.GetActiviySnapshotsByPeriod, activiySnapshots, ReturnMessageType.Successful);
	}
	/**
	 * getSingleActiviySnapshotByDate return Single Activity Snapshot by date
	 * if the Activity do not exist return the last activity record
	 * 
	 * @param msg       Message with dates
	 * @return Message with list of calculate duration
	 */
	public Message getSingleActiviySnapshotByDate(Message msg) {
		boolean isGivenDateSnapshot =true;
		
		try {
			ActiviySnapshot activitySnapshot = getActivitySnapshotByDate((Date) msg.getObj());
			if (activitySnapshot == null) {
				isGivenDateSnapshot = false;
				activitySnapshot = getActivitySnapshotByDate(getLastSnapshotDate());
			}

			if (isGivenDateSnapshot) {
				return new Message(OperationType.GetActiviySnapshotByDate, activitySnapshot,
						ReturnMessageType.Successful);
			} else {
				return new Message(OperationType.GetActiviySnapshotByDate, activitySnapshot,
						ReturnMessageType.SuccessfulWithLastSnapshotDate);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new Message(OperationType.GetActiviySnapshotByDate, null, ReturnMessageType.Unsuccessful);

		}
	}
	/**
	 * getLastActiviySnapshotRecord return the last activity record 
	 * 
	 * @param msg with empty object
	 * @return Message with list of calculate duration
	 */
	public Message getLastActiviySnapshotRecord(Message msg) {
		try {
			ActiviySnapshot activitySnapshot = getActivitySnapshotByDate(getLastSnapshotDate());

			return new Message(OperationType.GetLastActiviySnapshotRecord, activitySnapshot,
					ReturnMessageType.Successful);

		} catch (Exception e) {
			e.printStackTrace();
			return new Message(OperationType.GetActiviySnapshotByDate, null, ReturnMessageType.Unsuccessful);
		}

	}
}
