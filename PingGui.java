import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.util.Date;
import java.util.Scanner;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PingGui {

	private JFrame frmPingtest;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws IOException {
		new PingGui();
} 

	public PingGui() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() throws IOException{

		frmPingtest = new JFrame();
		frmPingtest.setTitle("NetAnalyser V1.0");
		frmPingtest.setBounds(100, 100, 1100, 300);
		frmPingtest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPingtest.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(368, 10, 372, 241);
		textArea.setEditable(false);
		textArea.setText("Your output will appear hear");
		JTextAreaOutputStream out = new JTextAreaOutputStream (textArea);
    	System.setOut (new PrintStream (out));
		frmPingtest.getContentPane().add(textArea);

		JTextArea textArea2 = new JTextArea();
		textArea2.setBackground(SystemColor.control);
		textArea2.setBounds(760, 69, 214, 143);
		textArea2.setEditable(false);
		textArea2.setText("Your output will appear hear");
		frmPingtest.getContentPane().add(textArea2);

		textField = new JTextField();
		textField.setBounds(114, 72, 214, 19);
		frmPingtest.getContentPane().add(textField);
		textField.setColumns(10);



		JSpinner Probespinner = new JSpinner();
		Probespinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
		Probespinner.setBounds(200, 120, 30, 22);
		frmPingtest.getContentPane().add(Probespinner);
		
		JButton btnProcess = new JButton("Process");
		btnProcess.setFont(new Font("Arial", Font.BOLD, 12));
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				System.setOut(new PrintStream (out));
				String UrlString = textField.getText();
				int Prob_num = (int)Probespinner.getValue();
				ProcessBuilder pb = new ProcessBuilder("cmd","/c","ping "+"/n "+Prob_num+" "+UrlString);
				try{
					Process process = pb.start();
					Scanner scanner = new Scanner(process.getInputStream());
				while(scanner.hasNextLine()) {
					System.out.println(scanner.nextLine());
				}
				/* 文件输出测试 */
				Date dNow = new Date( );
      			SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd-hh-mm-ss");
				File f = new File(UrlString+"-"+ft.format(dNow)+".txt");
				f.createNewFile();
				FileOutputStream file1 = new FileOutputStream(f);
				PrintStream print2 = new PrintStream(file1);
				System.setOut(print2);
				System.out.println(UrlString+"-"+ft.format(dNow)+".txt");
				System.out.println();
				System.out.println("RTT(ms) histogram"); 

				scanner.close();
			} catch(IOException E){
				E.printStackTrace();
			}
		}
		}
		);
		
		btnProcess.setBounds(137, 192, 93, 23);
		frmPingtest.getContentPane().add(btnProcess);
		
		
		JLabel UrlLabel = new JLabel("Test URL");
		UrlLabel.setFont(new Font("Arial", Font.BOLD, 12));
		UrlLabel.setBounds(42, 74, 62, 15);
		frmPingtest.getContentPane().add(UrlLabel);
		
		JLabel ProbeLabel = new JLabel("No. of Probes");
		ProbeLabel.setFont(new Font("Arial", Font.BOLD, 12));
		ProbeLabel.setBounds(102, 123, 84, 15);
		frmPingtest.getContentPane().add(ProbeLabel);
		
		JLabel TitleLabel = new JLabel("Enter Test URL & no. of Probes and click on Process");
		TitleLabel.setFont(new Font("Arial", Font.BOLD, 12));
		TitleLabel.setBounds(0, 27, 315, 15);
		frmPingtest.getContentPane().add(TitleLabel);

		JLabel HisLabel = new JLabel("Histogram");
		HisLabel.setFont(new Font("Arial", Font.BOLD, 12));
		HisLabel.setBounds(779, 15, 62, 15);
		frmPingtest.getContentPane().add(HisLabel);

		frmPingtest.setVisible(true);

	}
}

class JTextAreaOutputStream extends OutputStream
{
    private final JTextArea destination;
 
 
    public JTextAreaOutputStream (JTextArea destination)
    {
        if (destination == null)
            throw new IllegalArgumentException ("Destination is null");
 
 
        this.destination = destination;
    }
    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException
    {
        final String text = new String (buffer, offset, length);
        SwingUtilities.invokeLater(new Runnable ()
            {
                @Override
                public void run() 
                {
                    destination.append (text);
                }
            });
    }
    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
}