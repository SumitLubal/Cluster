package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JLabel;

import process.DBManager;
import rmi.HTTPServerEcho;
import rmi.HTTPServerForNodes;

public class ManagerGUI extends JFrame {

	private JPanel contentPane;
	public static boolean serverState;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerGUI frame = new ManagerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	HTTPServerEcho Comunicator;
	public JLabel lblLastNodeAccess;
	public JLabel lblLastClientIp;
	HTTPServerForNodes nodeCommunicator;
	private JLabel lblCurrentLabel;

	public ManagerGUI() {
		Comunicator = new HTTPServerEcho(this);
		Comunicator.start();

		nodeCommunicator = new HTTPServerForNodes(this);
		nodeCommunicator.start();
		serverState = true;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 466, 336);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnAddNode = new JButton("Add Node");
		btnAddNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNode();
			}

			private void addNode() {
				// TODO Auto-generated method stub
				String ipAddress = JOptionPane.showInputDialog(this,
						"Enter IP Address");
				boolean isReachable = false;
				try {
					System.out.println("Trying to connect to system");
					isReachable = InetAddress.getByName(ipAddress).isReachable(
							1000);
					System.out.println("System is connected");
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// DBManager.createIPTable();
				String querry = "INSERT INTO `cluster`.`node` (`ip`, `loads`, `dates`) VALUES ('"
						+ ipAddress
						+ "', '0', '"
						+ new java.sql.Date(System.currentTimeMillis()) + "');";
				System.out.println(isReachable);
				// this logic is created for ensuring that server is alive
				// temporarily disabled
				isReachable = true;
				if (isReachable) {
					System.out.println("Firing querry");
					if (DBManager.fireQuerry(querry)) {

						System.out.println(querry);
						JOptionPane.showMessageDialog(null, "New node added : "
								+ ipAddress + " OK!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Failed see log");
				}
			}
		});
		btnAddNode.setBounds(10, 32, 99, 23);
		contentPane.add(btnAddNode);

		JButton btnDeleteNode = new JButton("Delete Node");
		btnDeleteNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ipAddress = JOptionPane.showInputDialog(this,
						"Enter IP Address");
				String querry = "DELETE FROM `cluster`.`node` WHERE `node`.`ip` = \'"
						+ ipAddress + "\'";
				if (DBManager.fireQuerry(querry)) {
					JOptionPane.showConfirmDialog(null,
							"Do you really want to delet node");
				} else {
					JOptionPane.showMessageDialog(null,
							"Could not find node with given ip address");
				}
			}
		});
		btnDeleteNode.setBounds(10, 66, 99, 23);
		contentPane.add(btnDeleteNode);

		lblCurrentLabel = new JLabel("Current Stat:");
		lblCurrentLabel.setBounds(143, 36, 281, 14);
		contentPane.add(lblCurrentLabel);

		JLabel lblNewLabel = new JLabel("Total Data Transfer:");
		lblNewLabel.setBounds(143, 70, 281, 14);
		contentPane.add(lblNewLabel);

		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setBounds(143, 104, 281, 14);
		contentPane.add(lblSpeed);

		JButton btnNewButton_3 = new JButton("Auto Delete");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						ResultSet rs = DBManager.readFromTable("node");
						String deletedIPs = "Deleted IPs: ";
						boolean isDeleted = false;
						try {
							while (rs.next()) {
								String a;
								if (!checkForServerAlive(
										(a = rs.getString("ip")), 48101)) {
									lblCurrentLabel
											.setText("Current status:scanning for "
													+ a);
									String querry = "DELETE FROM `cluster`.`node` WHERE `node`.`ip` = \'"
											+ a + "\'";
									DBManager.fireQuerry(querry);
									deletedIPs += ", " + a;
									isDeleted = true;
								}
							}
							if (isDeleted) {
								JOptionPane.showMessageDialog(null, deletedIPs);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}

			private boolean checkForServerAlive(String SERVER_ADDRESS,
					int TCP_SERVER_PORT) {
				// TODO Auto-generated method stub
				try (Socket s = new Socket(SERVER_ADDRESS, TCP_SERVER_PORT)) {
					return true;
				} catch (IOException ex) {
					/* ignore */
				}
				return false;

			}
		});
		btnNewButton_3.setBounds(10, 203, 99, 23);
		contentPane.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("Stop");
		btnNewButton_4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JButton b = (JButton) arg0.getSource();
				if (b.getText().equals("Start")) { // if already started
					// start the server
					serverState = true;
					JOptionPane.showMessageDialog(null, "Server started");
					b.setText("Stop");
				} else {
					serverState = false;
					// stop the server
					JOptionPane.showMessageDialog(null, "Server stopped");
					b.setText("Start");
				}
			}
		});
		btnNewButton_4.setBounds(10, 237, 99, 23);
		contentPane.add(btnNewButton_4);

		lblLastNodeAccess = new JLabel("Last Node Access:");
		lblLastNodeAccess.setBounds(143, 138, 281, 14);
		contentPane.add(lblLastNodeAccess);

		lblLastClientIp = new JLabel("Last Client IP Address:");
		lblLastClientIp.setBounds(143, 173, 297, 14);
		contentPane.add(lblLastClientIp);
	}
}
