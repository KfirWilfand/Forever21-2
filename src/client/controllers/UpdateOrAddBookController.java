package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UpdateOrAddBookController {

    @FXML
    private TextField tfCatalogNumber;

    @FXML
    private TextField tfBookName;

    @FXML
    private TextField tfAuthorName;

    @FXML
    private TextField tfEditionNumber;

    @FXML
    private TextField tfLocationOnShelf;

    @FXML
    private TextArea txteDescription;

    @FXML
    private DatePicker dpPrintingDate;

    @FXML
    private DatePicker dpPurchaseDate;

    @FXML
    private TextField tfCopiesNumber;

    @FXML
    private CheckBox cbIsPopular;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnUploadTableOfContent;

    @FXML
    private Button btnAddBook;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnAddCopy;

    @FXML
    void onClickAddBook(ActionEvent event) {

    }

    @FXML
    void onClickAddCopy(ActionEvent event) {

    }

    @FXML
    void onClickBack(ActionEvent event) {

    }

    @FXML
    void onClickUpdate(ActionEvent event) {

    }

}
