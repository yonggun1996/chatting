package Server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerGUI extends JFrame implements ActionListener{
	
	private JTextArea jta = new JTextArea(40, 25);
	private JTextField jtf = new JTextField(25);
	//서버를 실행시키기 위해 백그라운드 객체를 생성, 연동하는 부분
	private ServerBackground server = new ServerBackground();

	public ServerGUI() {
		add(jta, BorderLayout.CENTER);
		add(jtf, BorderLayout.SOUTH);
		jtf.addActionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setBounds(200, 100, 400, 600);
		setTitle("서버");
		
		server.setGui(this);
		server.setting();
	}
	
	public static void main(String[] args) {
		new ServerGUI();//error
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String msg = "서버 : " + jtf.getText() + "\n";
		//jta.append(msg);
		System.out.println(msg);
		server.sendMessage(msg);
		jtf.setText("");
	}

	public void appendMsg(String msg) {
		jta.append(msg);
		//System.out.print("날라온 메세지 : " + msg);
	}
	
}
