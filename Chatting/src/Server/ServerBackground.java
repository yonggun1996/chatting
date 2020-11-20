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
	
	//����ڵ��� ������ �����ϴ� ��
	private Map<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();
	
	public void setGui(ServerGUI gui) {
		this.gui = gui;
	}

	public void setting() {
		try {
			Collections.synchronizedMap(clientmap);//Ŭ���̾�Ʈ�� ������ ����
			serversocket = new ServerSocket(7777);//7777��Ʈ�� ������ ����д�
			
			while(true) {
				//����ڰ� ������ �����ϸ� ����� ���´�.
				//�湮�ڸ� ��� �޾� ������ ���ù��� ��� ����
				System.out.println("�����...");
				socket = serversocket.accept();//Ŭ���̾�Ʈ�� �����Բ� ������ ���ִ� ������ ����д�.
				System.out.println(socket.getInetAddress() + "���� �����߽��ϴ�.");
				
				//���ο� ����ڸ� �޴� �ڵ�
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
		sendMessage(nick + "���� �����߽��ϴ�." + "\n");
		clientmap.put(nick, out);
	}
	
	public void removeClient(String nick) {
		sendMessage(nick + "���� �������ϴ�." + "\n");
		clientmap.remove(nick);
	}
	
	//�޼��� ������ �����ϴ� �޼ҵ�
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
	
	//----------------���ο� Ŭ���̾�Ʈ�� �޴� ���ù� ����--------------//
	
	class Receiver extends Thread{
		//Ŭ���̾�Ʈ�� ������ �� ���� ���ù� �����尡 �����ȴ�.
		private DataInputStream in;
		private DataOutputStream out;
		private String nick;
		
		public Receiver(Socket socket) {
			//���ù��� ����
			//����ؼ� ��Ʈ��ũ ������ �޾� ��� ��û�ϴ� ��
			try {
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				nick = in.readUTF();
				addClient(nick, out);
				//Ŭ���̾�Ʈ�� �޾ƿ��� �ڵ�
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void run() {
			try {
				while(in != null) {
					msg = in.readUTF();//Ŭ���̾�Ʈ�� ���� ������ �о�鿩 msg�� �ʱ�ȭ�Ѵ�.
					sendMessage(msg);
					gui.appendMsg(msg);
				}
			} catch (IOException e) {
				//������� ����� ���⼭ ���ܰ� �߻��ϴµ�
				//���ܰ� �߻��Ѵٴ°� ����ڰ� �������� �õ��� �߱� ����
				//�ᱹ�� ����ڰ� �����Եȴ�.
				removeClient(nick);
			}//try - catch
			
		}
		
	}
	
}
