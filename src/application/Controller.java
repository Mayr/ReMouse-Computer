package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Controller implements Initializable {
	
	private static boolean isRunning = false;
	private InetAddress ipAddr;
	private Server server;
	
	@FXML private Label alert;
    @FXML private ImageView info;
    @FXML private Label status;
    @FXML private Label ip;
    @FXML private Button start;
    @FXML private Text url;

    public void setIP(InetAddress i) {
    	ipAddr = i;
		ip.setText(ipAddr.toString().substring(1, ipAddr.toString().length()));

    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			InetAddress myHost = InetAddress.getLocalHost();
			server = new Server();
			
			ip.setText("-nil-");
			notify("Software started succesfully.");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void handleServerControl(ActionEvent event) {
		isRunning=!isRunning;
		if(isRunning) {
			
			server.startServer(this);
			status.setText("Running");
			status.setTextFill(Color.rgb(19, 202, 206));
			
			start.setText("Stop Server");
			start.setStyle("-fx-background-color: #EC3531;");
			
		}else {
			if(server.stopServer()) {	
				status.setText("Offline");
				status.setTextFill(Color.rgb(236,53,49));
				ip.setText("-nil-");
				
				start.setText("Start Server");
				start.setStyle("-fx-background-color: #5882dd;");
				
				notify(" Server stopped.");
			}else {
				isRunning = true;
			}
		}
	}
	
	public void serverStopped() {
		status.setText("Offline");
		status.setTextFill(Color.rgb(236,53,49));
		
		start.setText("Start Server");
		start.setStyle("-fx-background-color: #5882dd;");
		isRunning = false;

		notify(" Server stopped.");
	}
	
	public void notify(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_TIME;

		alert.setText(java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS).format(dtf) +" "+message);
		
	}

}
