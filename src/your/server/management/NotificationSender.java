package your.server.management;

import java.util.List;

import your.server.objects.Notification;


public class NotificationSender {


	public void sendNotification(Notification notification) {
		/*try {
			User user = Main.getUserManagement().getUser(notification.getOwnerName());
			byte[] message = notification.printMessage().getBytes();

			DatagramPacket packet = new DatagramPacket(message, message.length, user.getClientHost(), user.getUdpPort());
			DatagramSocket dsocket = new DatagramSocket();

			dsocket.send(packet);
			dsocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

	}

	public void sendNotifications(List<Notification> notifications) {
		/*for (Notification notification : notifications) {
			sendNotification(notification);
		}*/
	}	
}
