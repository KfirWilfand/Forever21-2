package client.controllers.adapters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.List;
import javax.imageio.ImageIO;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import client.ViewStarter;
import client.controllers.StatisticController;
import common.entity.ActiviySnapshot;
import common.entity.Book;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import java.io.FileOutputStream;

public class PDFGenerator {

	/** instance is a singleton of the class */
	private static PDFGenerator instance;

	Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
	Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
	Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
	Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

	private PDFGenerator() {
	}

	/**
	 * getInstance is creating the singleton object of the class
	 */
	public static PDFGenerator getInstance() {
		if (instance == null) {
			instance = new PDFGenerator();
		}
		return instance;
	}

	/**
	 * Generate a PDF of books
	 * 
	 * @param dest: path to save the file
	 * @param title: PDF title
	 * @param bookList: List of book to print
	 */
	public File createPdf(String dest, String title, List<Book> bookList) {
		File file = new File(dest);
		try {

			if (bookList.isEmpty()) {
				ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Empty List Error",
						"Error, can't save empty book list");
				return null;
			}

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();

			Paragraph para = new Paragraph(new Phrase(title, titleFont));
			para.setAlignment(Element.ALIGN_CENTER);

			document.add(para);

			for (Book book : bookList) {

				PdfPTable table = new PdfPTable(4);

				// Row1: Book name and Author
				PdfPCell cell1 = new PdfPCell(new Phrase(book.getBookName() + " " + book.getAuthor(), boldFont));
				cell1.setColspan(4);
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setBorder(2);
				table.addCell(cell1);

				// Row2:
				table.addCell(new Phrase("Description: ", boldFont));
				PdfPCell cell2 = new PdfPCell(new Phrase(book.getDescription(), normalFont));
				cell2.setColspan(3);
				cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell2);

				// Row3:
				table.addCell("Genre: ");
				PdfPCell cell = new PdfPCell(new Phrase(book.getGenre().toString(), normalFont));
				cell.setColspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);

				// Row4:
				table.addCell(new Phrase("Catalog Number:", boldFont));
				table.addCell(new Phrase(Integer.toString(book.getCatalogNum()), normalFont));
				table.addCell(new Phrase("Shelf Location: ", boldFont));
				table.addCell(new Phrase(book.getShelfLocation(), normalFont));

				// Row5:
				table.addCell(new Phrase("Available Copies: ", boldFont));
				table.addCell(new Phrase(Integer.toString(book.getAvilableCopiesNum()), normalFont));
				table.addCell(new Phrase("Copies: ", boldFont));
				table.addCell(new Phrase(Integer.toString(book.getCopiesNum()), normalFont));

				// Row6:
				table.addCell(new Phrase("Purchase Date: ", boldFont));
				table.addCell(new Phrase(book.getPurchaseDate().toString(), normalFont));
				table.addCell(new Phrase("Print Date: ", boldFont));
				table.addCell(new Phrase(book.getPrintDate().toString(), normalFont));

				// Row7:
				table.addCell(new Phrase("Edition: ", boldFont));
				table.addCell(new Phrase(book.getEdition(), normalFont));
				table.addCell(new Phrase(new Phrase("Classification: ", boldFont)));
				table.addCell(new Phrase(book.isPopular() ? "Popular" : "Regular", normalFont));

				document.add(table);
			}

			document.close();

			ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.INFORMATION, "Successful",
					"Success! your result saved in " + dest);

		} catch (DocumentException e) {
			e.printStackTrace();
			ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Document Error",
					"Error, can't save your book list result");
		} catch (IOException e) {
			e.printStackTrace();
			ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "File Error",
					"Error, file save path not found or in use.");
		} catch (Exception e) {
			e.printStackTrace();
			ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Search List is Empty",
					"Error, Search List is Empty.");
		}
		return file;
	}

	/**
	 * Generate a PDF file with statistic report, all data comes from 
	 * statistic controller, include what to insert to file.
	 * @param dest  path to save the file
	 * @param title PDF title
	 * @param statis StatisticController
	 */
	public File createPdf(String dest, String title, StatisticController statis)
			throws DocumentException, MalformedURLException, IOException {

		File file = new File(dest);
		file.getParentFile().mkdirs();

		Paragraph para = new Paragraph(new Phrase(title, titleFont));
		para.setAlignment(Element.ALIGN_CENTER);

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();

		document.add(para);

		List<ActiviySnapshot> mSnapshot = statis.getActiviySnapshot();

		if (!mSnapshot.isEmpty()) {
			PdfPTable table = new PdfPTable(4);

			// Row1: Period
			PdfPCell cell1 = new PdfPCell(new Phrase("Activity Period: " + statis.getDpActReportStartDate().getValue()
					+ " to " + statis.getDpActReportEndDate().getValue(), subTitleFont));
			cell1.setColspan(4);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBorder(2);
			table.addCell(cell1);

			document.add(table);

			for (ActiviySnapshot snapshot : mSnapshot) {

				PdfPTable table2 = new PdfPTable(4);

				// Row1: Period
				PdfPCell cell2 = new PdfPCell(new Phrase("Date: " + snapshot.getaDate().toString(), boldFont));
				cell2.setColspan(4);
				cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell2.setBorder(2);
				table2.addCell(cell2);

				// Row2: aLockSub | aHoldSub
				table2.addCell(new Phrase("Lock: ", boldFont));
				table2.addCell(new PdfPCell(new Phrase(Integer.toString(snapshot.getLock()), normalFont)));
				table2.addCell(new Phrase("Hold: ", boldFont));
				table2.addCell(new Phrase(Integer.toString(snapshot.getHold()), normalFont));

				// Row3: Active | aCopies
				table2.addCell(new Phrase("Active: ", boldFont));
				table2.addCell(new Phrase(Integer.toString(snapshot.getActive()), normalFont));
				if (statis.getCbActReportCopies().isSelected()) {
					table2.addCell(new Phrase("Copies: ", boldFont));
					table2.addCell(new Phrase(Integer.toString(snapshot.getCopies()), normalFont));
				} else {
					table2.addCell(new Phrase(" ", boldFont));
					table2.addCell(new Phrase(" ", boldFont));
				}

				// Row4: aLates | aLates
				if (statis.getCbActReportLates().isSelected()) {
					table2.addCell(new Phrase("Lates: ", boldFont));
					table2.addCell(new Phrase(Integer.toString(snapshot.getLates()), normalFont));
					table2.addCell(new Phrase(" ", boldFont));
					table2.addCell(new Phrase(" ", boldFont));
				}

				document.add(table2);
			}

		}

		// Regular Book
		if (statis.getCbBorrRegReportDecDist().isSelected() || statis.getCbBorrRegReportAvg().isSelected()
				|| statis.getCbBorrRegReportMed().isSelected()) {

			PdfPTable table = new PdfPTable(4);

			// Row1: Period
			PdfPCell cell1 = new PdfPCell(new Phrase("Borrow Regular Books", subTitleFont));
			cell1.setColspan(4);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBorder(2);
			table.addCell(cell1);

			if (statis.getCbBorrRegReportDecDist().isSelected()) {
				Image iTextImage = getImageByNode(statis.getBcStatisticRegularBooks());
				if (iTextImage != null) {
					PdfPCell cell2 = new PdfPCell(iTextImage, true);
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell1.setBorder(0);
					cell2.setColspan(4);
					table.addCell(cell2);
				}
			}

			if (statis.getCbBorrRegReportAvg().isSelected()) {
				table.addCell(new Phrase("Average: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticAverageRegularBooks().getText(), normalFont));
				if (!statis.getCbBorrRegReportMed().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}
			}

			if (statis.getCbBorrRegReportMed().isSelected()) {
				table.addCell(new Phrase("Median: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticMedianRegularBooks().getText(), normalFont));
				if (!statis.getCbBorrRegReportAvg().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}
			}

			document.add(table);

		}

		// Popular Book
		if (statis.getCbBorrPopReportDecDist().isSelected() || statis.getCbBorrPopReportAvg().isSelected()
				|| statis.getCbBorrPopReportMed().isSelected()) {

			PdfPTable table = new PdfPTable(4);
			// Row1: Period
			PdfPCell cell1 = new PdfPCell(new Phrase("Borrow Popular Books", subTitleFont));
			cell1.setColspan(4);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBorder(2);
			table.addCell(cell1);

			if (statis.getCbBorrPopReportDecDist().isSelected()) {
				Image iTextImage = getImageByNode(statis.getBcStatisticPopularBooks());
				if (iTextImage != null) {
					PdfPCell cell2 = new PdfPCell(iTextImage, true);
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell1.setBorder(0);
					cell2.setColspan(4);
					table.addCell(cell2);
				}
			}

			if (statis.getCbBorrPopReportAvg().isSelected()) {
				table.addCell(new Phrase("Average: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticAveragePopularBooks().getText(), normalFont));
				if (!statis.getCbBorrPopReportMed().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}
			}

			if (statis.getCbBorrPopReportMed().isSelected()) {
				table.addCell(new Phrase("Median: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticMedianPopularBooks().getText(), normalFont));
				if (!statis.getCbBorrPopReportAvg().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}
			}

			document.add(table);
		}

		// All Late
		if (statis.getCbAllLateRegReportDecDist().isSelected() || statis.getCbAllLateRegReportAvg().isSelected()
				|| statis.getCbAllLatesRegReportMed().isSelected()) {

			PdfPTable table = new PdfPTable(4);

			// Row1: Period
			PdfPCell cell1 = new PdfPCell(new Phrase("All Lates Books", subTitleFont));
			cell1.setColspan(4);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBorder(2);
			table.addCell(cell1);

			if (statis.getCbAllLateRegReportDecDist().isSelected()) {
				Image iTextImage = getImageByNode(statis.getBcStatisticRegularBooks());
				if (iTextImage != null) {
					PdfPCell cell2 = new PdfPCell(iTextImage, true);
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell1.setBorder(0);
					cell2.setColspan(4);
					table.addCell(cell2);
				}
			}

			if (statis.getCbAllLateRegReportAvg().isSelected()) {
				table.addCell(new Phrase("Average: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticAverageReturnLates().getText(), normalFont));
				if (!statis.getCbAllLatesRegReportMed().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}

			}

			if (statis.getCbAllLatesRegReportMed().isSelected()) {
				table.addCell(new Phrase("Median: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticMedianReturnLates().getText(), normalFont));
				if (!statis.getCbAllLateRegReportAvg().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}
			}

			document.add(table);
		}

		// Lates By Single Book
		if (statis.getCbLateSingleBooksDist().isSelected() || statis.getCbLateSingleBooksAvg().isSelected()
				|| statis.getCbLateSingleBooksMed().isSelected()) {

			PdfPTable table = new PdfPTable(4);

			// Row1: Period
			PdfPCell cell1 = new PdfPCell(new Phrase("Lates By Single Book [Catalog Number:"
					+ statis.getTfStatisticSingleBookReturnLates().getText() + "]", subTitleFont));
			cell1.setColspan(4);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setBorder(2);
			table.addCell(cell1);

			if (statis.getCbLateSingleBooksDist().isSelected()) {
				Image iTextImage = getImageByNode(statis.getBcStatisticReturnLatesSingle());
				if (iTextImage != null) {
					PdfPCell cell2 = new PdfPCell(iTextImage, true);
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell1.setBorder(0);
					cell2.setColspan(4);
					table.addCell(cell2);
				}
			}

			if (statis.getCbLateSingleBooksAvg().isSelected()) {
				table.addCell(new Phrase("Average: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticSingleBookAverageReturnLates().getText(), normalFont));
				if (!statis.getCbLateSingleBooksMed().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}
			}

			if (statis.getCbLateSingleBooksMed().isSelected()) {
				table.addCell(new Phrase("Median: ", boldFont));
				table.addCell(new Phrase(statis.getLblStatisticSingleBookMedianReturnLates().getText(), normalFont));
				if (!statis.getCbLateSingleBooksAvg().isSelected()) {
					table.addCell(new Phrase(" ", boldFont));
					table.addCell(new Phrase(" ", boldFont));
				}
			}

			document.add(table);
		}

		document.close();
		return file;
	}

	/**
	 * getImageByNode
	 * 
	 * @param Node
	 * @return Image
	 */
	private Image getImageByNode(Node node) {
		try {
			WritableImage writableImage = node.snapshot(new SnapshotParameters(), null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", baos);
			return Image.getInstance(baos.toByteArray());
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
