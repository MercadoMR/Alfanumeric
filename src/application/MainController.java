package application;


import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainController {


	@FXML
	ToggleButton tb;
	
	@FXML
	TextArea txtArea;
	
	@FXML
	TextField tf;

	private BackUART back;
	
	/* This method is automatically called after 
	 * the fxml file is loaded
	 *
	 */ 
	@FXML
	private void initialize()
	{
		back = new BackUART(txtArea);
	}
	
	
	@FXML
	public void toggle(ActionEvent ae)
	{
		if(tb.isSelected()) //Encendido
		{
			tb.setText("Apagar");
			txtArea.setText("-------- Recepción encendida en el puerto "+tf.getText()+" --------\n");
			//txtArea.appendText("\n-------- Recepción encendida en el puerto "+tf.getText()+" --------\n");
			back.setPuerto(tf.getText());
			back.restart();
		}else //Apagado
		{
			tb.setText("Encender");
			txtArea.appendText("\n-------- Recepción apagada en puerto "+tf.getText()+" --------\n");
			back.cancel();
		}
		
		//progressBar.progressProperty().bind(tarea.progressProperty());
		//new Thread(tarea).start();

	}
}
