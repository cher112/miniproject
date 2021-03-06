/*
 * @Author: Zhihao Chen
 * @Date: 2020-05-15 17:22:55
 * @LastEditTime: 2020-05-21 01:20:07
 * @LastEditors: sueRimn
 * @Description: In User Settings Edit
 * @FilePath: \ping\NetAnalyser.java
 */ 
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetAnalyser {

	private JFrame frmPingtest;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String arg[]) throws IOException {
		int set_Probe = Integer.parseInt(arg[0]);
		new NetAnalyser(set_Probe);
} 

	public NetAnalyser(int a) throws IOException {
		initialize(a);
	}

 /**
  * This i s a method of initialize the NetAnalyser.
  * Using the JFrame and add TextArea which is for showing the
  *output of"Ping -n +URL", TextField Jspinner for set the URL and Probe_Num.
  * Build the action for Process-bottom.Frist, set the output stream to TextArea,clean all remain text by setText()
  * Read the value from textField and Probespinner,get the URL and Probes number
  * use regular expression to match both the right output like "Reply from IP bytes
  * =32 times = 17 TTL = 243" and ignore the "Time out" by match the reg_err(extra credit)
  * set all numbers next to "times=" in  from second line  to 1+Probesnumere line.
  * if the output has "Time out", fixed the Probe_num by minus the Probe_fix.
  * Finally use if and for to write histgram.
  * @name: initialize
  * @Descripttion: Initialize the contents of the jframe.
  * @param null 
  * @return: null
  */ 
	public void initialize(int a) throws IOException{

		
		if(a>20){
			System.out.println("The probes_num you set is too big(>20), we set that to default_max 20");
			a=20;
		}else if(a<0)
		{
			System.out.println("The probes_num you set is too small(>20), we set that to default_min 10");
			a=10;
		}else{}

  /**
   * @description: 
   * @param {type} 
   * @return: 
   */  
  frmPingtest = new JFrame();
		frmPingtest.setTitle("NetAnalyser V1.0");
		frmPingtest.setBounds(100, 100, 1100, 300);
		frmPingtest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPingtest.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setEnabled(false);
		scrollPane_1.setBounds(730, 113, 350, 130);
		scrollPane_1.setBorder(null);
		frmPingtest.getContentPane().add(scrollPane_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(350, 22, 350, 220);
		frmPingtest.getContentPane().add(scrollPane);


		JTextArea textArea = new JTextArea();
		JScrollPane jsp1 = new JScrollPane(textArea);
		jsp1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textArea.setEditable(false);
		textArea.setText("Your output will appear hear");
		JTextAreaOutputStream out = new JTextAreaOutputStream (textArea);
		PrintStream old = System.out;
		System.setOut (new PrintStream (out));
		scrollPane.setViewportView(textArea);


		JTextArea textArea2 = new JTextArea();
		textArea2.setBackground(SystemColor.control);
		textArea2.setEditable(false);
		JTextAreaOutputStream out2 = new JTextAreaOutputStream (textArea2);
		scrollPane_1.setViewportView(textArea2);

		textField = new JTextField();
		textField.setBounds(114, 72, 214, 19);
		frmPingtest.getContentPane().add(textField);
		textField.setColumns(10);



		JSpinner Probespinner = new JSpinner();
		Probespinner.setModel(new SpinnerNumberModel(1, 1, a, 1));
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
					String regex = "(?<=\\=)\\d+";
					String reg_err = ".*[\\.��]$";
					Pattern p = Pattern.compile(regex);
					Pattern p_err = Pattern.compile(reg_err);
					ArrayList<String> a1 = new ArrayList<>();
					int count = 0;
					int Probe_fix=0;
				while(scanner.hasNextLine()) {
					String output = scanner.nextLine();
					System.out.println(output);
					Matcher m = p.matcher(output);
					Matcher error = p_err.matcher(output);
					if(error.find()){
						Probe_fix++;
					} else {
					if(count<=(Prob_num+1) && count>0){
						
						while (m.find()) { 
							a1.add(m.group(0));
						}
					}
				}	
					count++;
				
			}
				/*regex text*/
				ArrayList<Integer> list = new ArrayList<>();
				ArrayList<Integer> list_2 = new ArrayList<>();
				ArrayList<Integer> list_hist = new ArrayList<>();
				int Prob_num_2 = Prob_num-Probe_fix;
				
				if(Prob_num_2==1){
					list.add(Integer.parseInt(a1.get(1)));
					list_2.add(Integer.parseInt(a1.get(1)));
				}else{
				for(int i=1;i<(Prob_num_2*3-1);i=i+3){
					list.add(Integer.parseInt(a1.get(i)));
					list_2.add(Integer.parseInt(a1.get(i)));
				}
				
				System.out.println(list);
			}
				if(list.size()==0){
					System.out.println("empty in bins,try it again.");
				}else{
				Collections.sort(list,Collections.reverseOrder());
				Collections.sort(list_2,Collections.reverseOrder());
				double judge = (list.get(0)-list.get(list.size()-1))%3;
				double bin_size1 = (list.get(0)-list.get(list.size()-1))/3;
				int bin_size;
				if(judge!=0){
					bin_size = ((int)Math.ceil(bin_size1));
				}else{
					bin_size = ((int)Math.ceil(bin_size1));
				}
				if(bin_size==0){bin_size++;}
				if(list.get(0)>=(list.get(list.size()-1)+3*(bin_size))){bin_size++;}
				for(int j=0;j<3;j++){
					int tem_count = 0;
					for (int i = (list_2.size()-1); i >= 0; i--) {
						if(list_2.get(i) < (list.get(list.size()-1)+(j+1)*(bin_size)) && list_2.get(i) >= (list.get(list.size()-1)+j*(bin_size))){
							tem_count++;
						}
					}
					System.setOut(new PrintStream(out2));

					System.out.print((list.get(list.size()-1)+j*(bin_size))+"<=RTT<"+(list.get(list.size()-1)+(j+1)*(bin_size))+" ");
					list_hist.add(tem_count);
					
					if(tem_count!=0){
					for(int l=0;l<tem_count;l++){
						System.out.print("*        ");
					}
					System.out.println();
				}else{System.out.println();}
					}
					
					textArea2.setText("");
					
					/* txt text */
					LocalDateTime dNow = LocalDateTime.now();
					DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
					UrlString = UrlString.replaceAll("\\.","-");
					File f = new File(UrlString+"-"+ft.format(dNow)+".txt");
					f.createNewFile();
					FileOutputStream file1 = new FileOutputStream(f);
					PrintStream print2 = new PrintStream(file1);
					System.setOut(print2);
					System.out.println(UrlString+"-"+ft.format(dNow)+".txt");
					System.out.println();
					System.out.println("RTT(ms) histogram");
					for(int i = 0;i<bin_size;i++){
					  System.out.print(list.get(list.size()-1)+3*i+"-"+(list.get(list.size()-1)+3*(i+1))+":"+list_hist.get(i));
					  System.out.println();
					  } 

					  scanner.close();
					}
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
		HisLabel.setBounds(736, 74, 62, 15);
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