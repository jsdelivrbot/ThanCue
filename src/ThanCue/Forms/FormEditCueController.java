package ThanCue.Forms;

import ThanCue.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

import static ThanCue.CueType.UNSET;

/**
 * Created by mike on 04/06/16.
 */
public class FormEditCueController {
    // todo the rest of this class and stuff

    private FormMainController parentController;
    private Cue editingCue;
    private boolean cueIsToAdd;

    //Container panes
    @FXML
    private AnchorPane anchor_pane;
    @FXML
    private GridPane grid_pane;
    @FXML
    private HBox container_file_chooser;

    //Labels
    @FXML
    private Label lblCueInfo;
    @FXML
    private Label lblCueName;
    @FXML
    private Label lblCueType;
    @FXML
    private Label lblCueBehaviour;
    @FXML
    private Label lblFilePath;

    //Selectors
    @FXML
    private ComboBox cmbCueType;
    @FXML
    private ComboBox cmbCueBehaviour;

    //Text areas
    @FXML
    private TextField txtCueName;

    //Form buttons
    @FXML
    private Button btnSaveChanges;
    @FXML
    private Button btnCancelChanges;
    @FXML
    private Button btnChooseFile;

    @FXML
    public void initialize() {
        setActions();
        setSizes();

        cmbCueType.getItems().setAll(CueType.values());
        cmbCueType.getSelectionModel().select(UNSET);
        cmbCueBehaviour.getItems().setAll(CueBehaviour.values());
        cmbCueBehaviour.getSelectionModel().select(CueBehaviour.PLAY_ON_GO);
    }

    private void setActions() {
        //buttons
        btnCancelChanges.setOnAction(event -> closeEditCueWindow());
        btnSaveChanges.setOnAction(event -> closeAfterReturningCue());
        btnChooseFile.setOnAction(event -> chooseFile());

        //fields
        cmbCueType.setOnAction(event -> changeCueType());
        txtCueName.textProperty().addListener((observableValue, s, t1) -> changeCueName()); //text changed
        cmbCueBehaviour.setOnAction(event -> changeCueBehaviour());
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file for cue");
        fileChooser.setInitialDirectory(((FileCue)editingCue).getFilePath().getParent().toFile());
        File file = fileChooser.showOpenDialog(btnChooseFile.getScene().getWindow());
        if(file != null){
            changeFilePath(file);
        }
    }

    private void changeFilePath(File file) {
        ((FileCue)editingCue).setCueFilePath(file.getAbsolutePath());
        updateFieldEntries(true);
    }

    private void changeCueBehaviour() {
        editingCue.setCueBehaviour((CueBehaviour)cmbCueBehaviour.getSelectionModel().getSelectedItem());
    }

    //todo reorder methods to be more organised

    private void changeCueName() {
        editingCue.setCueName(txtCueName.getText());
    }

    private void closeEditCueWindow() {
        Stage thisStage = (Stage)btnCancelChanges.getScene().getWindow();
        thisStage.close();
    }

    private void closeAfterReturningCue() {
        if(cmbCueType.getSelectionModel().getSelectedItem() == CueType.UNSET){
            showDialogInvalidSelection();
            return;
        }

        if(cueIsToAdd){
            parentController.addNewCue(editingCue);
        }else {
            parentController.setEditedCue(editingCue);
        }
        closeEditCueWindow();
    }

    private void setSizes() {
        //allow growing
        btnSaveChanges.setMaxWidth(Double.MAX_VALUE);
        btnCancelChanges.setMaxWidth(Double.MAX_VALUE);
        lblCueInfo.setMaxWidth(Double.MAX_VALUE);
        lblCueType.setMaxWidth(Double.MAX_VALUE);
        lblCueName.setMaxWidth(Double.MAX_VALUE);
        lblCueBehaviour.setMaxWidth(Double.MAX_VALUE);
        container_file_chooser.setMaxWidth(Double.MAX_VALUE);
        lblFilePath.setMaxWidth(Double.MAX_VALUE);

        //ensure grid pane always fill width of parent
        AnchorPane.setTopAnchor(grid_pane, .0);
        AnchorPane.setBottomAnchor(grid_pane, .0);
        AnchorPane.setLeftAnchor(grid_pane, .0);
        AnchorPane.setRightAnchor(grid_pane, .0);

        //make grid columns wide as parent
        ColumnConstraints columnConstraintForHalfWidth = new ColumnConstraints();
        columnConstraintForHalfWidth.setPercentWidth(50);
        grid_pane.getColumnConstraints().addAll(columnConstraintForHalfWidth, columnConstraintForHalfWidth);

        //set HGrow
        HBox.setHgrow(lblFilePath, Priority.ALWAYS);

        //update layouts
        anchor_pane.layout();
        grid_pane.layout();
    }

    public void setParentController(FormMainController c){
        this.parentController = c;
    }

    public void setEditObject(Cue c) {
        if (c != null) {
            editingCue = c;
        } else {
            editingCue = new UnknownCue();
        }
        updateFieldEntries(true);
    }

    public void setCueIsToAdd(boolean b){
        cueIsToAdd = b;
    }

    private void updateFieldEntries(boolean changeCueTypeComboBox) {
        lblCueInfo.setText(editingCue.toString());
        txtCueName.setText(editingCue.getCueName());
        if (changeCueTypeComboBox) {
            // DO NOT CHANGE COMBOBOX WHEN THIS METHOD IS CALLED DUE TO A CHANGE IN THE COMBO BOX
            cmbCueType.getSelectionModel().select(editingCue.cueTypeEnum);
        }
        cmbCueBehaviour.getSelectionModel().select(editingCue.cueBehaviourEnum);
        boolean cueTypeUsesFile = editingCue instanceof FileCue;
        if(cueTypeUsesFile){
            lblFilePath.setText(((FileCue)editingCue).getFilePath().toAbsolutePath().toString());
        }else{
            lblFilePath.setText("No file");
        }
        container_file_chooser.setDisable(!cueTypeUsesFile);
    }

    private void changeCueType() {
        switch ((CueType) cmbCueType.getSelectionModel().getSelectedItem()) {
            case UNSET:
                showDialogInvalidSelection(); // todo maybe remove UNSET entirely and use unknown as the default starting cue?
                                                // it would make things a LOT easier in my opinion (I know it was my idea initially) - mike
                return;
            case UNKNOWN:
                editingCue = new UnknownCue();
                break;
            case SOUND:
                editingCue = new SoundCue();
                break;
            case LIGHT:
                //todo make light cue a thing
                throw new NotImplementedException();
                //break;
            case VIDEO:
                editingCue = new VideoCue();
                break;
        }
        editingCue.setCueName(txtCueName.getText());
        editingCue.setCueBehaviour((CueBehaviour)cmbCueBehaviour.getSelectionModel().getSelectedItem());
        updateFieldEntries(false);
    }

    private void showDialogInvalidSelection() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You cannot create an unset cue! Use unknown instead!", ButtonType.OK);
        alert.setHeaderText("Selection issue");
        alert.showAndWait();
    }
}
