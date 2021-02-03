import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SendMail extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField nameTextField;
	private JTextField addressTextField;
    // メールの送信先はYahooメール。送信元もYahooメール
	private String myID = "自分のYahooID";
	private String myPassword = "自分のYahooパスワード";
	private String myAddress = "自分のYahooメールアドレス";
	private String myName = "自分の名前";
	private String destinationAddress;
	private String destinationName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendMail frame = new SendMail();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// フレームの作成
	public SendMail() {
		setTitle( "Yahooメール定型文の送信" );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 340, 172);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel nameLabel = new JLabel("相手の名前");
		nameLabel.setBounds(12, 10, 300, 13);
		contentPane.add(nameLabel);

		JLabel addressLabel = new JLabel("相手のYahooメールアドレス(@以下除く)");
		addressLabel.setBounds(12, 57, 300, 13);
		contentPane.add(addressLabel);

		JButton sendButton = new JButton("送信");
		sendButton.setBounds(12, 103, 300, 21);
		contentPane.add(sendButton);
		sendButton.addActionListener(this);

		nameTextField = new JTextField();
		nameTextField.setBounds(12, 26, 300, 21);
		contentPane.add(nameTextField);
		nameTextField.setColumns(10);

		addressTextField = new JTextField();
		addressTextField.setBounds(12, 72, 300, 21);
		contentPane.add(addressTextField);
		addressTextField.setColumns(10);
	}

	// メール送信の処理
	public void sendProcess() {
	    try {
	        // メール送信のプロパティ設定
	        Properties props = new Properties();
	        props.put("mail.smtp.host", "smtp.mail.yahoo.co.jp");
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.ssl.trust", "*");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.connectiontimeout", "10000");
	        props.put("mail.smtp.timeout", "10000");

	        // セッションを作成する
	        Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                @Override
					protected
	                    PasswordAuthentication
	                    getPasswordAuthentication() {
	                    return new
	                        PasswordAuthentication(myID, myPassword);
	                }
	            });

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(
	        		myAddress, myName));
	        message.setReplyTo(new Address[]{
	            new InternetAddress(destinationAddress)});
	        message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(destinationAddress));
	        message.setSubject("挨拶");
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setText(destinationName + "さん、おはようございます。");

	        // メールのメタ情報を作成
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);
	        message.setHeader(
	            "Content-Transfer-Encoding", "base64");

	        // メールを送信
	        message.setContent(multipart);
	        Transport.send(message);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    }
	}

		// 送信ボタンを押した後の処理
		public void actionPerformed(ActionEvent e) {
			destinationAddress = addressTextField.getText() + "@yahoo.co.jp";
			destinationName = nameTextField.getText();
		    if (destinationName.length() == 0) {
			    JOptionPane.showMessageDialog(this, "相手の名前が入力されていません");
		    } else if (destinationAddress.length() == 12) {
			    JOptionPane.showMessageDialog(this, "相手のYahooメールアドレスが入力されていません");
		    } else {
				sendProcess();
			    JOptionPane.showMessageDialog(this, destinationName + "さんに送信しました");
		    };
		    nameTextField.setText("");
		    addressTextField.setText("");
		}
}
