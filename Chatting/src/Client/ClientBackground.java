package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientBackground {
	
	private Socket socket;
	//데이터를 서로 주고 받기 위한 통로(스트림)생성
	private DataInputStream in;
	private DataOutputStream out;
	private ClientGUI gui;
	private String msg;
	private String nickname;
	
	public void setGui(ClientGUI gui) {
		this.gui = gui;
	}

	public void connet() {
		try {
			socket = new Socket("localhost", 7777);
			System.out.println("서버 연결 성공");
			
			//소켓에서 받아온 데이터를 서로 주고받기 위한 스트림
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
			out.writeUTF(nickname);//서버단으로 닉네임 전송 후 서버가 맵에 저장한다
			System.out.println("메세지 전송 완료");//24행의 문장을 실행하면 출력한다
			while(in != null) {
				msg = in.readUTF();
				gui.appendMessage(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ClientBackground clientbackground = new ClientBackground();
		clientbackground.connet();
	}

	public void sendMessage(String msg2) {
		try {
			out.writeUTF(msg2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
