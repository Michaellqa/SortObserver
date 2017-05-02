import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class xFrame extends Application
{
    private int lastArraySize = 100;
    private ObservableList<SortingResults> statList;
    private TableView<SortingResults> sortingResultsTable;
    private TableView<StepResults> singleDemoTable;
    private TextField sizeValueField, fromValueField, toValueField, memoryVolumeField;


    public static void main(String arg[])
    {
        System.out.println("JFX is launched");
        launch(arg);
    }

    @Override
    public  void  start(Stage primaryStage) throws Exception
    {
        Stage window = primaryStage;
        window.setTitle("Sorting");

        statList = FXCollections.observableArrayList();
        sortingResultsTable = new TableView<>();
        singleDemoTable = new TableView<>();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(getStatisticTab(), getStepTab());
        updateStepTable();

        Scene scene = new Scene(tabPane,  752, 460);
        window.setScene(scene);
        window.show();
    }

    // ######### Statics ##########

    private void refreshStatisticsTab()
    {
        lastArraySize = Integer.parseInt(sizeValueField.getText());

        statList.clear();
        statList.addAll(Sort.getFullRangeStats(lastArraySize));
    }

    private void addCustomStatistics()
    {
        lastArraySize = Integer.parseInt(sizeValueField.getText());
        int memoryValue = Integer.parseInt(memoryVolumeField.getText());

        if (memoryValue >= 1 && memoryValue <= 10) {
            statList.addAll(Sort.getStatList(lastArraySize, memoryValue));
        }
        else {
            memoryVolumeField.clear();
            memoryVolumeField.setPromptText("1..10 %");
        }
    }

    private Tab getStatisticTab()
    {
        // Labels
        Label sizeLabel = new Label("Количество элементов");
        Label memoryLabel = new Label("Внутренняя память");

        VBox infoBox = new VBox();
        infoBox.setSpacing(10);
        infoBox.getChildren().addAll(sizeLabel, memoryLabel);

        // Array Properties
        sizeValueField = restrictedNumeric("100");
        sizeValueField.setMaxSize(150, 30);

        memoryVolumeField = restrictedNumeric("5");
        memoryVolumeField.setMaxSize(70, 30);

        VBox propBox = new VBox();
        propBox.getChildren().addAll(sizeValueField, memoryVolumeField);

        // Inspector input
        fromValueField = restrictedNumeric("1");
        fromValueField.setMaxSize(150, 30);
        fromValueField.setPromptText("Выберете диапазон");

        toValueField = restrictedNumeric("10");
        toValueField.setMaxSize(150, 30);
        toValueField.setPromptText("макс. 15 элементов");

        VBox rangeBox = new VBox();
        rangeBox.setPadding(new Insets(0,0,0,80));
        rangeBox.getChildren().addAll(fromValueField, toValueField);

        // Buttons
        Button refreshButton = new Button("Обзор");
        refreshButton.setOnAction(e -> refreshStatisticsTab() );

        Button showCharactsButton = new Button("+");
        showCharactsButton.setOnAction(e -> addCustomStatistics() );

        Button inspectButton = new Button("Выборка");
        inspectButton.setOnAction(e -> showInspectorView() );

        VBox refreshVBox = new VBox();
        refreshVBox.setSpacing(2);
        refreshVBox.getChildren().addAll(refreshButton, showCharactsButton);
        // table
        sortingResultsTable.setItems(statList);
        refreshStatisticsTab();
        addColumns();

        // bottom instrument panel
        HBox toolsBox = new HBox();
        toolsBox.setPadding(new Insets(10,10,10,10));
        toolsBox.setSpacing(10);
        toolsBox.getChildren().setAll(infoBox, propBox, refreshVBox, rangeBox, inspectButton);
        // scene
        VBox sceneBox = new VBox();
        sceneBox.getChildren().setAll(sortingResultsTable, toolsBox);

        Tab statTab = new Tab();
        statTab.setText("Характеристики");
        statTab.setContent(sceneBox);

        return statTab;
    }

    private TextField restrictedNumeric(String title)
    {
        TextField textField = new TextField(title);

        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue.intValue() > oldValue.intValue()){
                char ch = textField.getText().charAt(oldValue.intValue());
                if(!(ch >= '0' && ch <= '9' )){

                    textField.setText(textField.getText().substring(0, textField.getText().length()-1));
                }
            }
        });
        return textField;
    }

    private void addColumns()
    {
        TableColumn<SortingResults, String> titleColumn = new TableColumn<>("Название");
        titleColumn.setMinWidth(100);
        titleColumn.setCellValueFactory( new PropertyValueFactory<>("title"));

        TableColumn<SortingResults, Integer> compColumn = new TableColumn<>("Сравнения");
        compColumn.setMinWidth(100);
        compColumn.setCellValueFactory( new PropertyValueFactory<>("comparisons"));

        TableColumn<SortingResults, Integer> wColumn = new TableColumn<>("Записи");
        wColumn.setMinWidth(100);
        wColumn.setCellValueFactory( new PropertyValueFactory<>("writings"));

        TableColumn<SortingResults, Integer> rColumn = new TableColumn<>("Чтения");
        rColumn.setMinWidth(100);
        rColumn.setCellValueFactory( new PropertyValueFactory<>("readings"));

        TableColumn<SortingResults, Integer> timeColumn = new TableColumn<>("Время");
        timeColumn.setMinWidth(100);
        timeColumn.setCellValueFactory( new PropertyValueFactory<>("time"));

        sortingResultsTable.getColumns().setAll(titleColumn, compColumn, rColumn, wColumn, timeColumn);
    }

    // ########## Steps ##########

    private void updateStepTable()
    {
        Sort.updateUnsortedArray(true, 15);

        ObservableList<StepResults> singleStepList = FXCollections.observableArrayList(Sort.getSingleStepList());
        singleDemoTable.setItems(singleStepList);
    }

    private Tab getStepTab()
    {
        Tab stepTab = new Tab();

        TableView<StepResults> table;
        table = singleDemoTable;
        stepTab.setText("Демо");

        Button refreshButton = new Button("Обновить");
        refreshButton.setOnAction(event -> updateStepTable() );

        for (int i = 0; i < Sort.demoArraySize; i++)
        {
            TableColumn<StepResults, String> column = new TableColumn<>(Integer.toString(i+1));
            column.setMinWidth(50);
            column.setMaxWidth(50);
            column.setCellValueFactory( new PropertyValueFactory<>("el" + Integer.toString(i+1)));
            table.getColumns().add(i, column);
        }
        VBox vbox = new VBox();
        vbox.getChildren().setAll(table, refreshButton);

        stepTab.setContent(vbox);

        return stepTab;
    }

    // ######### Inspector ##########

    private void showInspectorView()
    {
            int from = Integer.parseInt(fromValueField.getText());
            int to = Integer.parseInt(toValueField.getText());

            if (to < 0 || from < 0 || to >= lastArraySize || from >= lastArraySize)
            {
                fromValueField.setText("");
                toValueField.setText("");
                fromValueField.setPromptText("Выберете диапазон");
                toValueField.setPromptText("от 0 до " + (lastArraySize - 1));
                return;
            }
            if (to - from < 0)
            {
                int temp = from;
                from = to;
                to = temp;
            }
            if (to - from < 15) inspectElements(from, to );
            else
            {
                fromValueField.setText("");
                toValueField.setText("");
                fromValueField.setPromptText("Выберете диапазон");
                toValueField.setPromptText("макс. 15 элементов");
            }
    }

    private void inspectElements(int fromValue, int toValue)
    {
        int numberOfElements = toValue - fromValue + 1;
        int preferedWidth = 152 + numberOfElements * 50;

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(preferedWidth + 2);
        stage.setTitle("Inspector");

        ObservableList<StepResults> stepResultsList = FXCollections.observableArrayList();
        stepResultsList.add(new StepResults(Sort.getSubArray(fromValue, toValue, Sort.unsortedFile)).setName("Несортированный"));

        for (int i = 1; i <= 10; i++) {
            stepResultsList.add(new StepResults(Sort.getSubArray(fromValue, toValue, "H"+i)).setName("H"+i));
        }

        TableColumn<StepResults, String> col = new TableColumn<>("Название");
        col.setMinWidth(150);
        col.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableView<StepResults> table = new TableView<>();
        table.getColumns().add(0, col);
        table.setItems(stepResultsList);

        for (int i = 0; i < numberOfElements; i++)
        {
            TableColumn<StepResults, String> column = new TableColumn<>(Integer.toString(fromValue + i));
            column.setMinWidth(50);
            column.setCellValueFactory(new PropertyValueFactory<>("el" + Integer.toString(i + 1)));
            table.getColumns().add(i+1, column);
        }
        VBox root = new VBox();
        root.getChildren().add(table);

        stage.setScene(new Scene(root, preferedWidth, 350));
        stage.showAndWait();
    }
}
