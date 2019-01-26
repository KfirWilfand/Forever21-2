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
import common.entity.Statistic;

public class StatisticController {
	private static StatisticController instance;
	private List<Integer> popularBorrowBookDuration;
	private List<Integer> regularBorrowBookDuration;
	private List<Integer> borrowBookLates;
	private boolean isGivenDateSnapshot;

	public static StatisticController getInstance() {
		if (instance == null) {
			instance = new StatisticController();
		}
		return instance;
	}

	public Message getStatstic(Object msg) {
		try {
			// init
			this.popularBorrowBookDuration = getBorrowBooksByPopularity(Boolean.TRUE);
			this.regularBorrowBookDuration = getBorrowBooksByPopularity(Boolean.FALSE);
			this.borrowBookLates = getBorrowBooksLates();

			float popAverage = getAverage(popularBorrowBookDuration);
			int popMedian = getMedian(popularBorrowBookDuration);
			Map<Integer, List<Integer>> popDistribution = getDistribution(popularBorrowBookDuration);

			float regAverage = getAverage(regularBorrowBookDuration);
			int regMedian = getMedian(regularBorrowBookDuration);
			Map<Integer, List<Integer>> regDistribution = getDistribution(regularBorrowBookDuration);

			float lateAverage = getAverage(borrowBookLates);
			int lateMedian = getMedian(borrowBookLates);
			Map<Integer, List<Integer>> lateDistribution = getDistribution(borrowBookLates);

			isGivenDateSnapshot = true;
			ActiviySnapshot activiySnapshot = readActiviySnapshotByDate((Date) ((Message) msg).getObj());

			Date firstSnapshot = getFirstSnapshotDate();
			Date lastSnapshot = getLastSnapshotDate();

			Statistic statistic = new Statistic(popAverage, popMedian, popDistribution, regAverage, regMedian,
					regDistribution, lateAverage, lateMedian, lateDistribution, activiySnapshot, firstSnapshot,
					lastSnapshot);

			if (isGivenDateSnapshot) {
				return new Message(OperationType.GetStatstic, statistic, ReturnMessageType.Successful);
			}

			return new Message(OperationType.GetStatstic, statistic, ReturnMessageType.SuccessfulWithLastSnapshotDate);

		} catch (Exception e) {
			e.printStackTrace();
			return new Message(OperationType.GetStatstic, null, ReturnMessageType.Unsuccessful);

		}
	}

	private Date getLastSnapshotDate() throws Exception {
		String lastSnapshotQuary = "SELECT Max(aDate) FROM obl.activity_statistic;";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet lastSnapshotQuary_res = dbControllerObj.query(lastSnapshotQuary);

		if (!lastSnapshotQuary_res.next()) {
			throw new Exception("Can't Find last Snapshot date");
		}

		return lastSnapshotQuary_res.getDate(1);

	}

	private Date getFirstSnapshotDate() throws Exception {
		String firstSnapshotQuary = "SELECT Min(aDate) FROM obl.activity_statistic;";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet firstSnapshotQuary_res = dbControllerObj.query(firstSnapshotQuary);

		if (!firstSnapshotQuary_res.next()) {
			throw new Exception("Can't Find last Snapshot date");
		}

		return firstSnapshotQuary_res.getDate(1);
	}

	public ActiviySnapshot readActiviySnapshotByDate(Date date) throws Exception {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String activiySnapshotByDateQuary = "SELECT `aDate`, `aLockSub`, `aHoldSub`, `aActiveSub`, `aCopies`, `aLates` "
				+ "FROM obl.activity_statistic Where aDate = '" + dateFormat.format(date) + "';";

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet activiySnapshotByDateQuary_res = dbControllerObj.query(activiySnapshotByDateQuary);

		boolean res;
		ActiviySnapshot activiySnapshot;
		while (res = activiySnapshotByDateQuary_res.next()) {
			activiySnapshot = new ActiviySnapshot(activiySnapshotByDateQuary_res.getDate("aDate"),
					activiySnapshotByDateQuary_res.getInt("aLockSub"),
					activiySnapshotByDateQuary_res.getInt("aHoldSub"),
					activiySnapshotByDateQuary_res.getInt("aActiveSub"),
					activiySnapshotByDateQuary_res.getInt("aCopies"), activiySnapshotByDateQuary_res.getInt("aLates"));

			return activiySnapshot;
		}

		if (!res) {
			isGivenDateSnapshot = false;
			return readActiviySnapshotByDate(getLastSnapshotDate());
		}

		return null;
	}

	public void insertStatisticActiviySnapshot() throws Exception {
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

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate borrowDate = LocalDate.now();

		String quaryInsertNewActivity = "INSERT INTO `obl`.`activity_statistic` (`aDate`, `aLockSub`, `aHoldSub`, `aActiveSub`, `aCopies`, `aLates`) "
				+ "VALUES ('" + Date.valueOf(borrowDate) + "', '" + aLockSubQuary_res.getInt(1) + "', '"
				+ aHoldSubQuary_res.getInt(1) + "', '" + aActiveSubQuary_res.getInt(1) + "', '"
				+ aCopiesQuary_res.getInt(1) + "', '" + aLatesQuary_res.getInt(1) + "');";

		dbControllerObj.insert(quaryInsertNewActivity);
	}

	public Map<Integer, List<Integer>> getDistribution(List<Integer> borrowBookDuration) throws Exception {

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

	public float getAverage(List<Integer> borrowBookDuration) {
		int sum = 0;

		for (Integer duration : borrowBookDuration) {
			sum += duration;
		}

		return ((float) sum) / borrowBookDuration.size();
	}

	public int getMedian(List<Integer> borrowBookDuration) {
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

	public List<Integer> getBorrowBooksByPopularity(Boolean isPopular) throws SQLException {
		List<List<Date>> borrowDates = new ArrayList<List<Date>>();

		String borrowBookQuaryByPopularity = "SELECT borrowDate,returnDueDate "
				+ "FROM obl.borrows as a join obl.copeis as b on a.copyID=b.copyID join obl.books as c on b.bCatalogNum=c.bCatalogNum "
				+ "where c.bIsPopular = " + isPopular + ";";

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

	public List<Integer> getBorrowBooksLates() throws SQLException {
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

	private List<Integer> calcBorrowDuration(List<List<Date>> borrowDates) {
		
		List<Integer> borrowDuration = new ArrayList<Integer>();

		for (List<Date> borrowBook : borrowDates) {
			Long diff = borrowBook.get(1).getTime() - borrowBook.get(0).getTime();

			borrowDuration.add(Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
		}
		
		return borrowDuration;
	}

}
