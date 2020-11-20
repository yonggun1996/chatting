package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerBackground {

	private ServerSocket serversocket;
	private Socket socket;
	private ServerGUI gui;
	private String msg;
	
	//사용자들의 정보를 저장하는 맵
	private Map<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();
	
	public void setGui(ServerGUI gui) {
		this.gui = gui;
	}

	public void setting() {
		try {
			Collections.synchronizedMap(clientmap);//클라이언트의 정보를 저장
			serversocket = new ServerSocket(7777);//7777포트로 서버를 열어둔다
			
			while(true) {
				//사용자가 서버에 접속하면 여기로 들어온다.
				//방문자를 계속 받아 스레드 리시버를 계속 생성
				System.out.println("대기중...");
				socket = serversocket.accept();//클라이언트가 들어오게끔 리스닝 해주는 공간을 열어둔다.
				System.out.println(socket.getInetAddress() + "에서 접속했습니다.");
				
				//새로운 사용자를 받는 코드
				Receiver receiver = new Receiver(socket);
				receiver.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}//try-catch
	}
	
	public static void main(String[] args) {
		ServerBackground serverbackground = new ServerBackground();
		serverbackground.setting();
	}
	
	public void addClient(String nick, DataOutputStream out) throws IOException{
		sendMessage(nick + "님이 접속했습니다." + "\n");
		clientmap.put(nick, out);
	}
	
	public void removeClient(String nick) {
		sendMessage(nick + "님이 나갔습니다." + "\n");
		clientmap.remove(nick);
	}
	
	//메세지 내용을 전파하는 메소드
	public void sendMessage(String msg) {
		Iterator<String> it = clientmap.keySet().iterator();
		String key = "";
		while(it.hasNext()) {
			key = it.next();
			try {
				clientmap.get(key).writeUTF(msg);
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//----------------새로운 클라이언트를 받는 리시버 구간--------------//
	
	class Receiver extends Thread{
		//클라이언트가 생성될 때 마다 리시버 스레드가 생성된다.
		private DataInputStream in;
		private DataOutputStream out;
		private String nick;
		
		public Receiver(Socket socket) {
			//리시버를 생성
			//계속해서 네트워크 소켓을 받아 듣고 요청하는 일
			try {
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				nick = in.readUTF();
				addClient(nick, out);
				//클라이언트를 받아오는 코드
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void run() {
			try {
				while(in != null) {
					msg = in.readUTF();//클라이언트가 보낸 문장을 읽어들여 msg에 초기화한다.
					sendMessage(msg);
					gui.appendMsg(msg);
				}
			} catch (IOException e) {
				//사용접속 종료시 여기서 예외가 발생하는데
				//예외가 발생한다는건 사용자가 나가려는 시도를 했기 때문
				//결국엔 사용자가 나가게된다.
				removeClient(nick);
			}//try - catch
			
		}
		
	}
	
}
