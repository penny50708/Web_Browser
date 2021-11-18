package javafxwebbrowser;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class JavaFxWebBrowser extends Application {
	private final Tab historyTab = new Tab("History");
	private final TabPane tabPane = new TabPane();
	private SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
	private final Button newTabbutton = new Button();
	private boolean isHistoryDisplay = false;
	private ArrayList<TabContainer> allTabs = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Landing tab
		Tab landingTab = new Tab("Home");

		TabContainer container = new TabContainer("http://www.google.com", landingTab);
		Background browserBackground = new Background(createImage("kiwibird.gif"));
		container.setBackground(browserBackground);
		allTabs.add(container);
		landingTab.setContent(container);

		// Fotmat the add new tab button
		Tab plusTab = new Tab();
		ImageView view = new ImageView(new Image(new FileInputStream("plus.png")));
		view.setFitWidth(10);
		view.setFitHeight(10);
		newTabbutton.setStyle("-fx-background-color: transparent");
		newTabbutton.setGraphic(view);
		plusTab.setGraphic(newTabbutton);
		plusTab.setDisable(true);
		plusTab.setClosable(false);

		Border bor=new Border(new BorderStroke(Paint.valueOf("#458B00"),BorderStrokeStyle.DASHED,new CornerRadii(10),new BorderWidths(5)));
		tabPane.setBorder(bor);
	    tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		tabPane.getTabs().addAll(landingTab, plusTab);
		tabPane.setPrefWidth(1000);
		tabPane.setPrefHeight(600);

		newTabbutton.setOnMouseClicked(action -> {
			Tab newTab = new Tab("New Page");
			TabContainer newTabContainer = new TabContainer(null, newTab);
			newTab.setContent(newTabContainer);
			tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab);
			selectionModel.select(newTab);
			allTabs.add(newTabContainer);
		});

		Scene scene = new Scene(tabPane, 1180, 600);
		primaryStage.setTitle("KIWI Browser-Time for Us to Fly");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("kiwi4.png")));
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();

	}

	private static BackgroundImage createImage(String url) {
		try {
			return new BackgroundImage(new Image(new FileInputStream(url)), BackgroundRepeat.REPEAT,
					BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public class TabContainer extends VBox {
		private VBox vbox = this;
		private TextField textField = new TextField();
		private WebView tabWebView = new WebView();
		private WebEngine engine = tabWebView.getEngine();
		private double webZoom = 1;
		private WebHistory history;
		private HBox hBox;
		private Tab tab;

		public TabContainer(String homeURL, Tab tab) {
			this.hBox = addHBox();
			this.tab = tab;

			this.getChildren().addAll(hBox);
			textField.setFont(Font.font("Arial", FontWeight.BOLD, 15));
			textField.setPrefWidth(500);
			textField.setMaxWidth(500);
			textField.setAlignment(Pos.BASELINE_LEFT);

			VBox.setVgrow(tabWebView, Priority.ALWAYS);
			HBox.setHgrow(textField, Priority.ALWAYS);

			/*
			 * if(homeURL != null && homeURL.equals("http://www.google.com")) { homePage();
			 * }
			 */

			textField.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					String validURL = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
					String enteredText = textField.getText();
					String url = "";
					if (enteredText.matches(validURL)) {
						
						if (enteredText.startsWith("https://") || enteredText.startsWith("http://")) {
							url = enteredText;
						} else {
							url = "https://" + enteredText;
						}
					} else {
						url = "http://google.com/search?q=" + enteredText;
					}

					loadURL(url);

				}

			});
		}

		public HBox addHBox() {

			HBox hbox = new HBox();

			hbox.setPadding(new Insets(3, 6, 3, 6));

			hbox.setSpacing(3);

			Button launch = new Button();
			ImageView launchImage = new ImageView(getClass().getResource("launch.png").toExternalForm());
			launchImage.setFitHeight(20);
			launchImage.setFitWidth(20);
			launch.setStyle("-fx-background-color: transparent");
			launch.setGraphic(launchImage);
			launch.setText("Launch");
			launch.setPrefSize(80, 20);
            launch.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            this.launch();
			});
			launch.setCursor(Cursor.HAND);

			Button refresh = new Button();
			ImageView refreshImage = new ImageView(getClass().getResource("refresh.png").toExternalForm());
			refresh.setPrefSize(80, 20);
			refreshImage.setFitHeight(20);
			refreshImage.setFitWidth(20);
			refresh.setStyle("-fx-background-color: transparent");
			refresh.setGraphic(refreshImage);
			refreshImage.setPreserveRatio(true);
			refresh.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> refreshPage());
			refresh.setCursor(Cursor.HAND);

			Button zoomIn = new Button();
			ImageView zoomInImage = new ImageView(getClass().getResource("zoomin.png ").toExternalForm());
			zoomInImage.setFitHeight(20);
			zoomInImage.setFitWidth(20);
			zoomIn.setPrefSize(70, 20);
			zoomIn.setStyle("-fx-background-color: transparent");
			zoomIn.setGraphic(zoomInImage);
			zoomIn.setText("+");

			zoomIn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> zoomIn());
			zoomIn.setCursor(Cursor.HAND);

			Button zoomOut = new Button();
			ImageView zoomOutImage = new ImageView(getClass().getResource("zoomout.png ").toExternalForm());
			zoomOutImage.setFitHeight(20);
			zoomOutImage.setFitWidth(20);
			zoomOut.setPrefSize(70, 20);
			zoomOut.setStyle("-fx-background-color: transparent");
			zoomOut.setGraphic(zoomOutImage);
			zoomOut.setText("-");
			zoomOut.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> zoomOut());
			zoomOut.setCursor(Cursor.HAND);
			
			Button displayHistory = new Button();
			ImageView historyImage = new ImageView(getClass().getResource("history.png").toExternalForm());
			historyImage.setFitHeight(20);
			historyImage.setFitWidth(20);
			displayHistory.setStyle("-fx-background-color: transparent");
			displayHistory.setGraphic(historyImage);
			displayHistory.setText("History");
			displayHistory.setPrefSize(90, 20);
			displayHistory.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> displayHistory());
			displayHistory.setCursor(Cursor.HAND);

			Button back = new Button();
			ImageView backImage = new ImageView(getClass().getResource("back.png").toExternalForm());
			backImage.setFitHeight(20);
			backImage.setFitWidth(20);
			back.setStyle("-fx-background-color: transparent");
			back.setGraphic(backImage);
			back.setText("Back");
			back.setPrefSize(80, 20);
			back.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> back());
			back.setCursor(Cursor.HAND);

			Button forward = new Button();
			ImageView forwardImage = new ImageView(getClass().getResource("forward.png").toExternalForm());
			forwardImage.setFitHeight(20);
			forwardImage.setFitWidth(20);
			forward.setStyle("-fx-background-color: transparent");
            forward.setGraphic(forwardImage);
			forward.setText("Forward");
			forward.setPrefSize(90, 20);
			forward.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> forward());
			forward.setCursor(Cursor.HAND);

			Button homePage = new Button();
			ImageView homeImage = new ImageView(getClass().getResource("homepage.png").toExternalForm());
			homeImage.setFitHeight(20);
			homeImage.setFitWidth(20);
			homePage.setGraphic(homeImage);
			homePage.setStyle("-fx-background-color: transparent");
			homePage.setText("Home");
			homePage.setPrefSize(90, 20);
			homePage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> homePage());
			homePage.setCursor(Cursor.HAND);

			hbox.getChildren().addAll(back, forward, refresh, textField, launch, homePage, zoomIn, zoomOut,
					displayHistory);
			return hbox;

		}

		// Node to get the background from the home button's parent
		// Node is the parent of all GUI objects in JavaFX
		public void homePage() {
			tabWebView.getEngine().load("http://www.google.com");
			if (!this.getChildren().contains(tabWebView)) {
				this.getChildren().add(tabWebView);
			} else {
				tabWebView.getEngine().load("http://www.google.com");
			}

			if (!this.getChildren().contains(tabWebView)) {
				vbox.setBackground(null);
				vbox.getChildren().add(tabWebView);
			}
		}

		public void launch() {
			String validURL = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
			String enteredText = textField.getText();
			String url = "";
			if (enteredText.matches(validURL)) {
				
				if (enteredText.startsWith("https://") || enteredText.startsWith("http://")) {
					url = enteredText;
				} else {
					url = "https://" + enteredText;
				}
			} else {
				url = "http://google.com/search?q=" + enteredText;
			}
			loadURL(url);
		}

		private void loadURL(String url) {
			engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
				public void changed(ObservableValue ov, State oldState, State newState) {
					if (newState == State.SUCCEEDED) {
						tab.setText(engine.getTitle());
					}
				}
			});
			engine.load(url);

			if (!this.getChildren().contains(tabWebView)) {
				vbox.setBackground(null);
				vbox.getChildren().add(tabWebView);
			}
		}

		public void refreshPage() {
			engine.reload();
		}

		public void zoomIn() {
			webZoom += 0.25;
			tabWebView.setZoom(webZoom);
		}

		public void zoomOut() {
			webZoom -= 0.25;
			tabWebView.setZoom(webZoom);
		}

		public void displayHistory() {
			if (!isHistoryDisplay) {
				isHistoryDisplay = true;

				TableView<HistoryItem> tableView = new TableView<>();
				TableColumn<HistoryItem, String> column1 = new TableColumn<>("Title");
				TableColumn<HistoryItem, Hyperlink> column2 = new TableColumn<>("URL");
				TableColumn<HistoryItem, String> column3 = new TableColumn<>("Date");
				column1.setCellValueFactory(new PropertyValueFactory<>("title"));
				column2.setCellValueFactory(new PropertyValueFactory<>("url"));
				column3.setCellValueFactory(new PropertyValueFactory<>("date"));
				tableView.getColumns().addAll(column1, column2, column3);

				for (TabContainer container : allTabs) {
					for (WebHistory.Entry entry : container.getWebEngine().getHistory().getEntries()) {
						tableView.getItems().add(new HistoryItem(entry.getTitle(), new Hyperlink(entry.getUrl()),
								entry.getLastVisitedDate().toString()));
					}

				}
				historyTab.setContent(tableView);

				tabPane.getTabs().add(tabPane.getTabs().size() - 1, historyTab);
				// Select the history tab
				selectionModel.select(historyTab);
				historyTab.setOnCloseRequest(new EventHandler<Event>() {
					@Override
					public void handle(Event arg0) {
						isHistoryDisplay = false;
					}
				});
			} else {
				selectionModel.select(historyTab);
			}

		}

		public void back() {
			history = engine.getHistory();
			ObservableList<WebHistory.Entry> entries = history.getEntries();
			if (history.getCurrentIndex() >= 1) {
				textField.setText(entries.get(history.getCurrentIndex() - 1).getUrl());
				history.go(-1);
			}
		}

		public void forward() {
			history = engine.getHistory();
			ObservableList<WebHistory.Entry> entries = history.getEntries();

			if (history.getCurrentIndex() < entries.size() - 1) {
				textField.setText(entries.get(history.getCurrentIndex() + 1).getUrl());
				history.go(1);
			}
		}

		public WebEngine getWebEngine() {
			return engine;
		}

	}

	public static void main(String[] args) {
		Application.launch(args);

	}

}