package application;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

public class BackUART extends Service<String>{

    private StringProperty puerto = new SimpleStringProperty();
	private TextArea ta;
		
	public BackUART(TextArea textArea)
	{
		super();
		ta = textArea;
	}
	@Override
	protected Task<String> createTask() {
		SerialPort sp = null;
		try {
			   sp = SerialPort.getCommPort(puerto.get());
			   sp.openPort();
		}catch(SerialPortInvalidPortException pi)
		{
				pi.printStackTrace();
		}
		
		return new Hilo(sp,ta);
	}
	
    public final void setPuerto(String puerto) {
        this.puerto.set(puerto);
    }

    public final String getPuerto() {
        return puerto.get();
    }

    public final StringProperty urlProperty() {
       return puerto;
    }
	
}

class Hilo extends Task<String>{

	SerialPort sp;
	TextArea area;
	public Hilo(SerialPort sp,TextArea area)
	{
		this.sp = sp;
		this.area = area;
	}
	
	@Override
	protected String call() throws Exception {
		if(sp.isOpen())
		{
			System.out.println("Puerto abierto!");
			sp.addDataListener(new SerialPortDataListener()
			{
				@Override
				public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
				@Override
				public void serialEvent(SerialPortEvent event)
				{
					if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
						return;
					byte[] newData = new byte[sp.bytesAvailable()];
					int numRead = sp.readBytes(newData, newData.length);
					String recibida = new String(newData);
					area.appendText(recibida);
				}
			});
			while(true)
			{
				if (isCancelled()) {
					sp.closePort();
					System.out.println("Tarea cancelada!");
		            break;
		        }
			}
		}else{
			area.setText("Puerto cerrado!");
			System.out.println("Puerto cerrado!");
		}
		return null;
	}

    @Override protected void succeeded() {
        super.succeeded();
        updateMessage("Terminó!");
    }

    @Override protected void cancelled() {
        super.cancelled();
        updateMessage("Hilo cancelado!");
    }

    @Override protected void failed() {
    super.failed();
    updateMessage("Falló!");
    }
}