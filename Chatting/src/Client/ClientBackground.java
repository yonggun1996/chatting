package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientBackground {
	
	private Socket socket;
	//�����͸� ���� �ְ� �ޱ� ���� ���(��Ʈ��)����
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
			System.out.println("���� ���� ����");
			
			//���Ͽ��� �޾ƿ� �����͸� ���� �ְ�ޱ� ���� ��Ʈ��
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
			out.writeUTF(nickname);//���������� �г��� ���� �� ������ �ʿ� �����Ѵ�
			System.out.println("�޼��� ���� �Ϸ�");//24���� ������ �����ϸ� ����Ѵ�
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
