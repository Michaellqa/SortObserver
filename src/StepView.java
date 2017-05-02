import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;


public class StepView {

    static Scene scene;
    static  String[][] results;

    public static void showSteps(String title) {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(1002);
        stage.setTitle(title);

        Button refresh = new Button("refresh");
        refresh.setOnAction(event -> {
            showSteps(title);
            stage.close();

        });

        VBox root = new VBox();

        ObservableList<String[]> data = FXCollections.observableArrayList();
        sortMode(title);
        data.addAll(Arrays.asList(results));
        data.remove(0); //remove titles from data

        TableView<String[]> table = new TableView<>();
        for (int i = 0; i < results[0].length; i++) {
            TableColumn tc = new TableColumn(results[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(50);
            table.getColumns().add(tc);
        }
        table.setItems(data);
        root.getChildren().addAll(table, refresh);

        stage.setScene(new Scene(root, 1000, 400));
        stage.show();
//        stage.showAndWait();
    }

    private static void sortMode(String mode){

        switch (mode) {
            case "Bubble":
                results = StepSort.doBubbleSort();
                break;
            case "Insertion":
                results = StepSort.doInsertionSort();
                break;
            case "Selection":
                results = StepSort.doSelectionSort();
                break;
            case "Shell":
                results = StepSort.doShellSort();
                break;
            case "Lined":
                results = StepSort.doLinedSort();
                break;
            }
        }

}
