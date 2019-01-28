package client.controllers.adapters;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import client.ViewStarter;
import common.entity.Book;
import javafx.scene.control.Alert.AlertType;
import server.controllers.ReaderController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import sandbox.WrapToTest;

public class PDFGenerator {

	/** instance is a singleton of the class */
	private static PDFGenerator instance;

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

	public void createPdf(String dest, String title, List<Book> bookList) {
		try {

			if (bookList.isEmpty()) {
				ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Empty List Error",
						"Error, can't save empty book list");
				return;
			}
			
			File file = new File(dest);
			file.getParentFile().mkdirs();

			Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
			Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

			Image img = Image
					.getInstance(System.getProperty("user.dir") + "/src/client/boundery/resources/logo-small.png");
			Paragraph para = new Paragraph(new Phrase(title, titleFont));
			para.setAlignment(Element.ALIGN_CENTER);

			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(dest));
			document.open();

			document.add(img);
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
		}
	}
}
