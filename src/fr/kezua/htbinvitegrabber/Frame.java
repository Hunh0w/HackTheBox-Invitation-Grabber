package fr.kezua.htbinvitegrabber;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings({ "deprecation", "serial" })
public class Frame extends JFrame {
	
	public static Frame instance;
	public final String url = "https://www.hackthebox.eu/api/invite/generate";
	
	public ImageIcon background;
	public Image icon;

	private JPanel contentPane;
	private JTextField txt;
	private JButton button;
	
	public static Frame getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void loadValues() {
		background = new ImageIcon(this.getClass().getClassLoader().getResource("assets/fond.gif"));
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("assets/icon.jpeg")).getImage();
	}
	
	public Frame() {
		instance = this;
		loadValues();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(icon);
		setTitle("HackTheBox Invite Code Generator v1.0");
		setBounds(100, 100, 490, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("HackTheBox Invite Generator By Kezua'SH");
		lblNewLabel.setForeground(new Color(105, 105, 105));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 464, 53);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		
		txt = new JTextField();
		txt.setForeground(new Color(0, 100, 0));
		txt.setEditable(false);
		txt.setBounds(129, 75, 236, 20);
		contentPane.add(txt);
		txt.setColumns(10);
		
		button = new JButton("GET INVITE CODE");
		button.setFont(new Font("Tahoma", Font.BOLD, 15));
		button.setForeground(new Color(105, 105, 105));
		button.setBackground(new Color(0, 0, 0));
		button.setBounds(129, 214, 219, 46);
		contentPane.add(button);
		
		JLabel label = new JLabel("");
		label.setIcon(background);
		label.setBounds(0, 0, 484, 271);
		contentPane.add(label);
		setupListeners();
	}
	
	@SuppressWarnings("resource")
	public void request() throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0"));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse response = httpclient.execute(httppost);
		BufferedReader BR = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String inputLine;
		String lines = "";
		while ((inputLine = BR.readLine()) != null) {
			lines = lines+" "+inputLine;
		}
		try{
			decode(lines);
		}catch(IOException ex) {
			txt.setText("Generating Failed");
		}
	}
	
	public void decode(String chars) throws IOException {
		String[] strs = chars.split("\"");
		String code = strs[7];
		byte[] bytesar = Base64.decodeBase64(code.getBytes());
		String decoded = new String(bytesar);
		txt.setText(decoded);
	}
	
	public void start() {
		button.setEnabled(false);
		try {
			request();
		} catch (IOException e) {
			e.printStackTrace();
		}
		button.setEnabled(true);
		
	}
	
	public void setupListeners() {
		this.button.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
            	Frame.getInstance().start();
            }
		});
	}
}
