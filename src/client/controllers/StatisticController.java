package client.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.DocumentException;
import client.ViewStarter;
import client.controllers.adapters.PDFGenerator;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.ActiviySnapshot;
import common.entity.BookStatistic;
import common.entity.enums.BookStatsticType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class StatisticController {
	/** Node component in statistic controller */
	@FXML
	private TabPane tabPaneStatistic;
	/** Node component in statistic controller */
	@FXML
	private Tab tabActivity;
	/** Node component in statistic controller */
	@FXML
	private PieChart pcSubscriberStatus;
	/** Node component in statistic controller */
	@FXML
	private DatePicker dpActivityStatistic;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticSubLatesNumCopies;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticNumCopies;
	/** Node component in statistic controller */
	@FXML
	private Tab tabBorrow;
	/** Node component in statistic controller */
	@FXML
	private AnchorPane apBorrow;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticAveragePopularBooks;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticMedianRegularBooks;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticAverageRegularBooks;
	/** Node component in statistic controller */
	@FXML
	private CategoryAxis xAxisReg;
	/** Node component in statistic controller */
	@FXML
	private NumberAxis yAxisReg;
	/** Node component in statistic controller */
	@FXML
	private BarChart<String, Integer> bcStatisticRegularBooks;
	/** Node component in statistic controller */
	@FXML
	private BarChart<String, Integer> bcStatisticPopularBooks;
	/** Node component in statistic controller */
	@FXML
	private BarChart<String, Integer> bcStatisticReturnLates;
	/** Node component in statistic controller */
	@FXML
	private BarChart<String, Integer> bcStatisticReturnLatesSingle;
	/** Node component in statistic controller */
	@FXML
	private CategoryAxis xAxisPop;
	/** Node component in statistic controller */
	@FXML
	private NumberAxis yAxisPop;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticMedianPopularBooks;
	/** Node component in statistic controller */
	@FXML
	private Tab tabReturnLates;
	/** Node component in statistic controller */
	@FXML
	private AnchorPane apReturnLates;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticAverageReturnLates;
	/** Node component in statistic controller */
	@FXML
	private CategoryAxis xAxisLate;
	/** Node component in statistic controller */
	@FXML
	private NumberAxis yAxisLate;
	/** Node component in statistic controller */
	@FXML
	private TextField tfStatisticSingleBookReturnLates;
	/** Node component in statistic controller */
	@FXML
	private Button btnStatisticSearchSingleBookReturnLates;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticSingleBookAverageReturnLates;
	/** Node component in statistic controller */
	@FXML
	private CategoryAxis xAxisLate2;
	/** Node component in statistic controller */
	@FXML
	private NumberAxis yAxisLate2;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticMedianReturnLates;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticSingleBookMedianReturnLates;
	/** Node component in statistic controller */
	@FXML
	private Tab tabGenerateReport;
	/** Node component in statistic controller */
	@FXML
	private Label lblStatisticAverageReturnLates1;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbActReportCopies;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbActReportLates;
	/** Node component in statistic controller */
	@FXML
	private DatePicker dpActReportStartDate;
	/** Node component in statistic controller */
	@FXML
	private DatePicker dpActReportEndDate;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbBorrRegReportDecDist;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbBorrRegReportAvg;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbBorrRegReportMed;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbBorrPopReportDecDist;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbBorrPopReportAvg;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbBorrPopReportMed;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbAllLateRegReportDecDist;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbAllLateRegReportAvg;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbAllLatesRegReportMed;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbLateSingleBooksAvg;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbLateSingleBooksMed;
	/** Node component in statistic controller */
	@FXML
	private CheckBox cbLateSingleBooksDist;
	/**
	 * mActiviySnapshot is list of ActiviySnapshot, this list is for temporary uses.
	 */
	private List<ActiviySnapshot> mActiviySnapshot;
	protected boolean isTabReturnLatesPressed = false;
	protected boolean isTabBorrowPressed = false;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
	/** PDF_PATH is the path for saved stock result file */
	private static String PDF_PATH = "output/report_" + LocalDateTime.now().format(formatter) + ".pdf";

	/**
	 * initialize the librarian controller
	 */
	@FXML
	public void initialize() {
		ViewStarter.client.statisticClientControllerObj = this;
		mActiviySnapshot = new ArrayList<ActiviySnapshot>();
	}

	/**
	 * initializeDetailsOnStatisticClick all statistic details on startup.
	 */
	public void initializeDetailsOnStatisticClick() {

		// initialize on start
		ViewStarter.client
				.handleMessageFromClientUI(new Message(OperationType.GetLastActiviySnapshotRecord, "initialize")); // sending
		ViewStarter.client
				.handleMessageFromClientUI(new Message(OperationType.GetBookStatstic, BookStatsticType.Popular));
		ViewStarter.client
				.handleMessageFromClientUI(new Message(OperationType.GetBookStatstic, BookStatsticType.Regular));
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.GetBookStatstic, BookStatsticType.Late));

		// bad practice bad work!
		tfStatisticSingleBookReturnLates.setText("4");
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.GetAllLatesReturnBySingleBookCatId,
				tfStatisticSingleBookReturnLates.getText()));


		// initialize on click tab
		tabPaneStatistic.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
				if (newTab.equals(tabActivity)) {
					ViewStarter.client.handleMessageFromClientUI(
							new Message(OperationType.GetLastActiviySnapshotRecord, "initialize")); // sending
				}

				if (newTab.equals(tabReturnLates)) {
					ViewStarter.client.handleMessageFromClientUI(
							new Message(OperationType.GetBookStatstic, BookStatsticType.Late));
					isTabReturnLatesPressed = true;
				}

				if (newTab.equals(tabBorrow)) {
					ViewStarter.client.handleMessageFromClientUI(
							new Message(OperationType.GetBookStatstic, BookStatsticType.Popular));
					ViewStarter.client.handleMessageFromClientUI(
							new Message(OperationType.GetBookStatstic, BookStatsticType.Regular));
					isTabBorrowPressed = true;
				}

				if (newTab.equals(tabGenerateReport)) {
					if (!isTabReturnLatesPressed || !isTabBorrowPressed) {
						ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR,
								"Statstic Generate Report",
								"You have to explore \"Return Late\" and \"Borrow\" tab before you try to generate report.");
						tabPaneStatistic.getSelectionModel().select(oldTab);
					}
				}

			}
		});

		// initialize activity statistic on pick a new date
		dpActivityStatistic.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {

				ViewStarter.client.handleMessageFromClientUI(
						new Message(OperationType.GetActiviySnapshotByDate, Date.valueOf(newValue))); // sending

			}
		});

		ChangeListener<LocalDate> changeListener = new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {

				if (dpActReportStartDate.getValue() == null || dpActReportEndDate.getValue() == null) {
					cbActReportLates.setDisable(true);
					cbActReportCopies.setDisable(true);
					return;
				} else if (dpActReportStartDate.getValue().isAfter(dpActReportEndDate.getValue())) {
					ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Activiy Snapshot",
							"Activiy start date must be grather then end date");
					dpActReportEndDate.setValue(null);
					cbActReportLates.setDisable(true);
					cbActReportCopies.setDisable(true);
					return;
				}

				Date[] dates = new Date[2];
				dates[0] = Date.valueOf(dpActReportStartDate.getValue());
				dates[1] = Date.valueOf(dpActReportEndDate.getValue());

				ViewStarter.client
						.handleMessageFromClientUI(new Message(OperationType.GetActiviySnapshotsByPeriod, dates)); // sending
			}

		};

		dpActReportStartDate.valueProperty().addListener(changeListener);
		dpActReportEndDate.valueProperty().addListener(changeListener);
	}

	/**
	 * on Return Activity Snapshot By Period from server in generate report tab, the
	 * method update the local activity snapshot list, and change disability of
	 * relate check boxes.
	 */
	public void onReturnActivitySnapshotByPeriod(List<ActiviySnapshot> activiySnapshotList) {
		mActiviySnapshot.clear();
		mActiviySnapshot.addAll(activiySnapshotList);
		cbActReportLates.setDisable(false);
		cbActReportCopies.setDisable(false);
	}

	/**
	 * on Unsuccessful Return Activity Snapshot By Period from server in generate
	 * report tab, the method update the local activity snapshot list, and change
	 * disability of relate check boxes.
	 */
	public void onReturnActivitySnapshotByPeriodUnsuccessful() {
		mActiviySnapshot.clear();
		cbActReportLates.setDisable(true);
		cbActReportCopies.setDisable(true);
		dpActReportEndDate.setValue(null);
	}

	/**
	 * on return activity snapshot by date, update Activity UI tab
	 */
	public void onReturnActivitySnapshotByDate(ActiviySnapshot activiySnapshot) {
		dpActivityStatistic.setValue(LocalDate.parse(activiySnapshot.getaDate().toString()));
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Active " + activiySnapshot.getActive(), activiySnapshot.getActive()),
				new PieChart.Data("Hold " + activiySnapshot.getHold(), activiySnapshot.getHold()),
				new PieChart.Data("Lock " + activiySnapshot.getLock(), activiySnapshot.getLock()));

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pcSubscriberStatus.setData(pieChartData);
				lblStatisticNumCopies.setText(Integer.toString(activiySnapshot.getCopies()));
				lblStatisticSubLatesNumCopies.setText(Integer.toString(activiySnapshot.getLates()));
			}
		});
	}

	/**
	 * Generate custom PDF file
	 * 
	 * @param ActionEvent event
	 */
	@FXML
	void onClickSaveReportPDF(ActionEvent event) {
		if (dpActReportStartDate.getValue() == null && dpActReportEndDate.getValue() != null) {
			ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Generate Report",
					"You must insert end date, to add activity snapshot");
			return;
		}

		if (dpActReportStartDate.getValue() != null && dpActReportEndDate.getValue() == null) {
			ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Generate Report",
					"You must insert start date, to add activity snapshot");
			return;
		}

		if (this.cbLateSingleBooksAvg.isSelected() || this.cbLateSingleBooksMed.isSelected()
				|| this.cbLateSingleBooksDist.isSelected()) {
			if (tfStatisticSingleBookReturnLates.getText().equals("")) {
				ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Generate Report",
						"You can't add single book lates statstic without search book in advance");
				cbLateSingleBooksAvg.setSelected(false);
				cbLateSingleBooksMed.setSelected(false);
				cbLateSingleBooksDist.setSelected(false);
				return;
			}
		}

		try {
			File file = PDFGenerator.getInstance().createPdf(PDF_PATH, "Statatic Report", this);
			Desktop.getDesktop().open(file);
			ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.INFORMATION, "Generate Report",
					"you'r file saved in " + file.getPath());
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}

	}

	/**
	 * On click search button, send new Message to server to get all lates return by
	 * single book by catalog number.
	 * 
	 * @param ActionEvent event
	 */
	@FXML
	void onSearchSingleBook(ActionEvent event) {
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.GetAllLatesReturnBySingleBookCatId,
				tfStatisticSingleBookReturnLates.getText()));
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.GetAllLatesReturnBySingleBookCatId,
				tfStatisticSingleBookReturnLates.getText()));
	}

	/**
	 * updateBookStatsticUI is updating the statistic book elements.
	 * 
	 * @param bookStatistic
	 */
	public void updateBookStatsticUI(BookStatistic bookStatistic) {
		
		if(bookStatistic.getDistribution().isEmpty()) {
			return;
		}
		
		String[] distributionRange = getDistraibutionRanges(bookStatistic.getDistribution());

		Series<String, Integer> distributionDataByDecimalRange = getDistributionDataByDecimalRange(
				bookStatistic.getDistribution(), distributionRange);

		String median = Integer.toString(bookStatistic.getMedian());
		String average = new DecimalFormat("#.##").format(bookStatistic.getAverage());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch (bookStatistic.getType()) {
				case Popular:
					bcStatisticPopularBooks.getData().clear();
					bcStatisticPopularBooks.getData().add(distributionDataByDecimalRange);
					bcStatisticPopularBooks.setLegendVisible(false);
					lblStatisticMedianPopularBooks.setText(median);
					lblStatisticAveragePopularBooks.setText(average);
					break;
				case Regular:
					bcStatisticRegularBooks.getData().clear();
					bcStatisticRegularBooks.getData().add(distributionDataByDecimalRange);
					bcStatisticRegularBooks.setLegendVisible(false);
					lblStatisticMedianRegularBooks.setText(median);
					lblStatisticAverageRegularBooks.setText(average);
					break;
				case Late:
					bcStatisticReturnLates.getData().clear();
					bcStatisticReturnLates.getData().add(distributionDataByDecimalRange);
					bcStatisticReturnLates.setLegendVisible(false);
					lblStatisticMedianReturnLates.setText(median);
					lblStatisticAverageReturnLates.setText(average);
					break;
				case SingleLate:
					bcStatisticReturnLatesSingle.getData().clear();
					bcStatisticReturnLatesSingle.getData().add(distributionDataByDecimalRange);
					bcStatisticReturnLatesSingle.setLegendVisible(false);
					lblStatisticSingleBookAverageReturnLates.setText(median);
					lblStatisticSingleBookMedianReturnLates.setText(average);
					break;
				}
			}
		});
	}


	/**
	 * getDistributionDataByDecimalRange help to generate the chart series to
	 * display in the BarCharts.
	 * 
	 * @param distribution      is the data to display
	 * @param distributionRange is a list of string that contain decimal calculated
	 *                          range.
	 */
	private XYChart.Series<String, Integer> getDistributionDataByDecimalRange(Map<Integer, List<Integer>> distribution,
			String[] distributionRange) {

		XYChart.Series<String, Integer> series = new XYChart.Series<>();

		for (int i = 0; i < distributionRange.length; i++) {
			series.getData().add(new XYChart.Data<>(distributionRange[i], distribution.get(i).size()));
		}
		return series;
	}

	/**
	 * getDistraibutionRanges decimal calculated range distribution
	 * 
	 * @param distribution      is the data to display
	 * @param distributionRange is a list of string that contain decimal calculated
	 *                          range.
	 * @return list of string that contain decimal calculated ranges.
	 */
	private String[] getDistraibutionRanges(Map<Integer, List<Integer>> distribution) {

		String[] ranges = new String[10];

		if (distribution.isEmpty()) {
			ranges[0] = "No data to display";
			return ranges;
		}

		Integer max = Collections.max(distribution.get(9));
		float range = ((float) max / 10);

		float min = 0;
		float tempRange = range;

		for (int i = 0; i < 10; i++) {
			ranges[i] = new DecimalFormat("#.##").format(min) + "-" + new DecimalFormat("#.##").format(tempRange);

			min += range;
			tempRange += range;
		}

		return ranges;
	}

	/**
	 * getDpActivityStatistic
	 * 
	 * @return DatePicker
	 */
	public DatePicker getDpActivityStatistic() {
		return dpActivityStatistic;
	}

	/**
	 * getDpActReportStartDate
	 * 
	 * @return DatePicker
	 */
	public DatePicker getDpActReportStartDate() {
		return dpActReportStartDate;
	}

	/**
	 * getDpActReportEndDate
	 * 
	 * @return DatePicker
	 */
	public DatePicker getDpActReportEndDate() {
		return dpActReportEndDate;
	}

	/**
	 * getActiviySnapshot
	 * 
	 * @return mActiviySnapshot
	 */
	public List<ActiviySnapshot> getActiviySnapshot() {
		return mActiviySnapshot;
	}

	/**
	 * getBcStatisticRegularBooks
	 * 
	 * @return bcStatisticRegularBooks
	 */
	public BarChart<String, Integer> getBcStatisticRegularBooks() {
		return bcStatisticRegularBooks;
	}

	/**
	 * getBcStatisticPopularBooks
	 * 
	 * @return bcStatisticPopularBooks
	 */
	public BarChart<String, Integer> getBcStatisticPopularBooks() {
		return bcStatisticPopularBooks;
	}

	/**
	 * getBcStatisticReturnLates
	 * 
	 * @return bcStatisticReturnLates
	 */
	public BarChart<String, Integer> getBcStatisticReturnLates() {
		return bcStatisticReturnLates;
	}

	/**
	 * @return getBcStatisticReturnLatesSingle
	 */
	public BarChart<String, Integer> getBcStatisticReturnLatesSingle() {
		return bcStatisticReturnLatesSingle;
	}

	/**
	 * getCbActReportLates
	 * 
	 * @return ChecbActReportLatesckBox
	 */
	public CheckBox getCbActReportLates() {
		return cbActReportLates;
	}

	/**
	 * getCbBorrRegReportDecDist
	 * 
	 * @return cbBorrRegReportDecDist
	 */
	public CheckBox getCbBorrRegReportDecDist() {
		return cbBorrRegReportDecDist;
	}

	/**
	 * getCbBorrRegReportAvg
	 * 
	 * @return cbBorrRegReportAvg
	 */
	public CheckBox getCbBorrRegReportAvg() {
		return cbBorrRegReportAvg;
	}

	/**
	 * getCbBorrRegReportMed
	 * 
	 * @return cbBorrRegReportMed
	 */
	public CheckBox getCbBorrRegReportMed() {
		return cbBorrRegReportMed;
	}

	/**
	 * getCbBorrPopReportDecDist
	 * 
	 * @return cbBorrPopReportDecDist
	 */
	public CheckBox getCbBorrPopReportDecDist() {
		return cbBorrPopReportDecDist;
	}

	/**
	 * getCbBorrPopReportAvg
	 * 
	 * @return cbBorrPopReportAvg
	 */
	public CheckBox getCbBorrPopReportAvg() {
		return cbBorrPopReportAvg;
	}

	/**
	 * getCbBorrPopReportMed
	 * 
	 * @return cbBorrPopReportMed
	 */
	public CheckBox getCbBorrPopReportMed() {
		return cbBorrPopReportMed;
	}

	/**
	 * getCbAllLateRegReportDecDist
	 * 
	 * @return cbAllLateRegReportDecDist
	 */
	public CheckBox getCbAllLateRegReportDecDist() {
		return cbAllLateRegReportDecDist;
	}

	/**
	 * getCbAllLateRegReportAvg
	 * 
	 * @return cbAllLateRegReportAvg
	 */
	public CheckBox getCbAllLateRegReportAvg() {
		return cbAllLateRegReportAvg;
	}

	/**
	 * getDpActivityStatistic
	 * 
	 * @return cbAllLatesRegReportMed
	 */
	public CheckBox getCbAllLatesRegReportMed() {
		return cbAllLatesRegReportMed;
	}

	/**
	 * getDpActivityStatistic
	 * 
	 * @return cbLateSingleBooksAvg
	 */
	public CheckBox getCbLateSingleBooksAvg() {
		return cbLateSingleBooksAvg;
	}

	/**
	 * getDpActivityStatistic
	 * 
	 * @return cbLateSingleBooksMed
	 */
	public CheckBox getCbLateSingleBooksMed() {
		return cbLateSingleBooksMed;
	}

	/**
	 * getDpActivityStatistic
	 * 
	 * @return cbLateSingleBooksDist
	 */
	public CheckBox getCbLateSingleBooksDist() {
		return cbLateSingleBooksDist;
	}

	/**
	 * getDpActivityStatistic
	 * 
	 * @return cbActReportCopies
	 */
	public CheckBox getCbActReportCopies() {
		return cbActReportCopies;
	}

	/**
	 * getDpActivityStatistic
	 * 
	 * @return lblStatisticSubLatesNumCopies
	 */
	public Label getLblStatisticSubLatesNumCopies() {
		return lblStatisticSubLatesNumCopies;
	}

	/**
	 * getLblStatisticNumCopies
	 * 
	 * @return lblStatisticNumCopies
	 */
	public Label getLblStatisticNumCopies() {
		return lblStatisticNumCopies;
	}

	/**
	 * getLblStatisticAveragePopularBooks
	 * 
	 * @return lblStatisticAveragePopularBooks
	 */
	public Label getLblStatisticAveragePopularBooks() {
		return lblStatisticAveragePopularBooks;
	}

	/**
	 * getLblStatisticMedianRegularBooks
	 * 
	 * @return lblStatisticMedianRegularBooks
	 */
	public Label getLblStatisticMedianRegularBooks() {
		return lblStatisticMedianRegularBooks;
	}

	/**
	 * getLblStatisticAverageRegularBooks
	 * 
	 * @return lblStatisticAverageRegularBooks
	 */
	public Label getLblStatisticAverageRegularBooks() {
		return lblStatisticAverageRegularBooks;
	}

	/**
	 * getLblStatisticMedianPopularBooks
	 * 
	 * @return lblStatisticMedianPopularBooks
	 */
	public Label getLblStatisticMedianPopularBooks() {
		return lblStatisticMedianPopularBooks;
	}

	/**
	 * getLblStatisticAverageReturnLates
	 * 
	 * @return lblStatisticAverageReturnLates
	 */
	public Label getLblStatisticAverageReturnLates() {
		return lblStatisticAverageReturnLates;
	}

	/**
	 * getLblStatisticSingleBookAverageReturnLates
	 * 
	 * @return lblStatisticSingleBookAverageReturnLates
	 */
	public Label getLblStatisticSingleBookAverageReturnLates() {
		return lblStatisticSingleBookAverageReturnLates;
	}

	/**
	 * getLblStatisticMedianReturnLates
	 * 
	 * @return lblStatisticMedianReturnLates
	 */
	public Label getLblStatisticMedianReturnLates() {
		return lblStatisticMedianReturnLates;
	}

	/**
	 * getLblStatisticSingleBookMedianReturnLates
	 * 
	 * @return lblStatisticSingleBookMedianReturnLates
	 */
	public Label getLblStatisticSingleBookMedianReturnLates() {
		return lblStatisticSingleBookMedianReturnLates;
	}

	/**
	 * getLblStatisticAverageReturnLates1
	 * 
	 * @return lblStatisticAverageReturnLates1
	 */
	public Label getLblStatisticAverageReturnLates1() {
		return lblStatisticAverageReturnLates1;
	}

	/**
	 * getTfStatisticSingleBookReturnLates
	 * 
	 * @return tfStatisticSingleBookReturnLates
	 */
	public TextField getTfStatisticSingleBookReturnLates() {
		return tfStatisticSingleBookReturnLates;
	}

}
