import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class MySudoku extends JFrame{
	private Sudoku m_Sudoku;

	private JPanel m_Input;
	private Vector<JTextField> m_PointVector;

	private JMenuBar m_MenuBar;
	private JMenu m_Menu_File;
	private JMenuItem m_Item_SaveToFile;
	private JMenuItem m_Item_ReadFromFile;

	private JToolBar m_Buttons;
	private JButton m_Button_Solve;
	private JButton m_Button_NextAnswer;
	private JButton m_Button_PreviousAnswer;
	private JButton m_Button_Clear;

	private JLabel m_Label_Status;

	private int m_NumberOfAnswers;
	private Font m_TextFont;

	private void clearData(){
		for (int i = 0; i<81; i++){
			m_PointVector.get(i).setText("");
			m_PointVector.get(i).setForeground(Color.BLUE);
		}
		m_NumberOfAnswers = 0;
		m_Sudoku.clear();
		m_Label_Status.setText("Ready");
		m_Button_NextAnswer.setEnabled(false);
		m_Button_PreviousAnswer.setEnabled(false);
	}

	public MySudoku(){
		super("MySudoku");
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		m_Sudoku = new Sudoku();

		m_Input = new JPanel();
		m_Input.setLayout(new GridLayout(9,9));


		m_TextFont = new Font("Arial", Font.BOLD, 30);
		m_PointVector = new Vector<JTextField>(81);

		JTextField Data;
		for (int i = 0; i < 81; i++){
			Data = new JTextField("",1);
			Data.setHorizontalAlignment(JTextField.CENTER);
			Data.setFont(m_TextFont);

			Data.setBackground(Color.WHITE);
			Data.setForeground(Color.BLUE);

			Data.addKeyListener(new TextChange());
			m_PointVector.addElement(Data);
			m_Input.add(Data);
		}

//the buttons 
		m_Buttons = new JToolBar();
		m_Buttons.setFloatable(false);

		m_Button_Solve = new JButton("Solve");
		m_Button_Solve.addActionListener(new ButtonClick());
		m_Buttons.add(m_Button_Solve);

		m_Button_Clear = new JButton("Clear");
		m_Button_Clear.addActionListener(new ButtonClick());
		m_Buttons.add(m_Button_Clear);

		m_Buttons.addSeparator();

		m_Button_NextAnswer = new JButton("Next Ans");
		m_Button_NextAnswer.addActionListener(new ButtonClick());
		m_Button_NextAnswer.setEnabled(false);
		m_Buttons.add(m_Button_NextAnswer);

		m_Button_PreviousAnswer = new JButton("Pre Ans");
		m_Button_PreviousAnswer.addActionListener(new ButtonClick());
		m_Button_PreviousAnswer.setEnabled(false);
		m_Buttons.add(m_Button_PreviousAnswer);

//the status label
		m_Label_Status = new JLabel("Ready");	

//the menu bar
		m_MenuBar = new JMenuBar();
		m_Menu_File = new JMenu("File");

		m_Item_SaveToFile = new JMenuItem("Save the result");
		m_Item_SaveToFile.addActionListener(new MenuClick());
		m_Item_ReadFromFile = new JMenuItem("Import files");
		m_Item_ReadFromFile.addActionListener(new MenuClick());

		m_Menu_File.add(m_Item_SaveToFile);
		m_Menu_File.add(m_Item_ReadFromFile);
		m_MenuBar.add(m_Menu_File);

		add(m_Input,BorderLayout.CENTER);
		add(m_Label_Status,BorderLayout.SOUTH);
		add(m_Buttons,BorderLayout.NORTH);
		setJMenuBar(m_MenuBar);

		setSize(300,400);
		setResizable(false);
		//pack();
		setVisible(true);
	};

//the listener
//the menu listener
	private class MenuClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			if (cmd.equals("Save the result")){
		//save the answers
				String FileName = new String("Ans_Sudoku_BY_ZeRo.txt");
				try{
					BufferedWriter fout = new BufferedWriter(new FileWriter(FileName));
					for (int k = 0; k< m_Sudoku.getNumberOfAnswers(); k++){
						fout.write("Ans No." + Integer.toString(k+1) + ":");
						fout.newLine();
						for (int i=0; i<9; i++){
							for (int j=0; j<9; j++)
								fout.write(Integer.toString(m_Sudoku.getPoint(k,i,j))+' ');
							fout.newLine();
						}
						fout.newLine();							
					}
					fout.close();
				}
				catch(IOException iox){
					System.out.println("Something goes wrong when writing " + FileName);
				}
			}else if (cmd.equals("Import files")){
		//open the file
				JFileChooser fc = new JFileChooser();
				if (fc.showOpenDialog(MySudoku.this) == JFileChooser.APPROVE_OPTION ){

					String FileName = fc.getSelectedFile().getName();
					String line;
					int num = 0;
					char value;

					try{
						clearData();
						BufferedReader fin = new BufferedReader(new FileReader(FileName));
						line = fin.readLine();
						while (line != null && num < 81){
							for (int i = 0; i<line.length(); i++){
								value = line.charAt(i);
								if (Character.isDigit(value)){
									if (Character.compare(value,'0') != 0){
										m_PointVector.get(num).setText(Character.toString(value));
										m_PointVector.get(num).setForeground(Color.BLACK);
									}
									num++;
								}
							}
							line = fin.readLine();
						}
						fin.close();
					}
					catch(IOException iox){
						System.out.println("Can't Read from" + FileName);
					}
				}
			};
		}
	}
	private class ButtonClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			if (cmd.equals("Solve")){

				m_NumberOfAnswers = 0;
				m_Sudoku.clear();
				m_Label_Status.setText("Start Searching...");

				for (int i = 0; i<9; i++)
					for (int j = 0; j<9; j++)
						if (m_PointVector.get(i*9+j).getText().equals(""))
							m_Sudoku.addPoint(i,j,0);
						else
							m_Sudoku.addPoint(i,j,Integer.decode(m_PointVector.get(i*9+j).getText()));

				m_Sudoku.solve(10);

				if (m_Sudoku.getNumberOfAnswers() != 0){
					for (int i = 0; i<9; i++)
						for (int j = 0; j<9; j++)
							if (m_PointVector.get(i*9+j).getText().equals(""))
								m_PointVector.get(i*9+j).setText(Integer.toString(m_Sudoku.getPoint(m_NumberOfAnswers,i,j)));
	
					m_Label_Status.setText(Integer.toString(m_Sudoku.getNumberOfAnswers()) + " Answers have been found!");
				}else m_Label_Status.setText("No Answers have been found!");

				if (m_NumberOfAnswers == m_Sudoku.getNumberOfAnswers() - 1)
					m_Button_NextAnswer.setEnabled(false);
				else m_Button_NextAnswer.setEnabled(true);
				if (m_NumberOfAnswers == 0)
					m_Button_PreviousAnswer.setEnabled(false);
				else m_Button_PreviousAnswer.setEnabled(true);

			}else if (cmd.equals("Next Ans")){
				m_Button_PreviousAnswer.setEnabled(true);
				if (m_NumberOfAnswers < m_Sudoku.getNumberOfAnswers()-1){
					m_NumberOfAnswers++;
					m_Label_Status.setText("Answer No." + Integer.toString(m_NumberOfAnswers+1) + " | " + Integer.toString(m_Sudoku.getNumberOfAnswers()) + " Answers in all");
					for (int i = 0; i<9; i++)
						for (int j = 0; j<9; j++)
							m_PointVector.get(i*9+j).setText(Integer.toString(m_Sudoku.getPoint(m_NumberOfAnswers,i,j)));
					if (m_NumberOfAnswers == m_Sudoku.getNumberOfAnswers() - 1) m_Button_NextAnswer.setEnabled(false);
				}

			}else if (cmd.equals("Pre Ans")){
				m_Button_NextAnswer.setEnabled(true);
				if (m_NumberOfAnswers > 0){
					m_NumberOfAnswers--;
					m_Label_Status.setText("Answer No." + Integer.toString(m_NumberOfAnswers+1) + " | " + Integer.toString(m_Sudoku.getNumberOfAnswers()) + " Answers in all");
					for (int i = 0; i<9; i++)
						for (int j = 0; j<9; j++)
							m_PointVector.get(i*9+j).setText(Integer.toString(m_Sudoku.getPoint(m_NumberOfAnswers,i,j)));
					if (m_NumberOfAnswers == 0) m_Button_PreviousAnswer.setEnabled(false);
				}
			}else if (cmd.equals("Clear")){
				clearData();
			};
		};
	};

	private class TextChange extends KeyAdapter{
		public void keyTyped(KeyEvent e){

			JTextField text = (JTextField)e.getSource();
			int index 	= m_PointVector.indexOf(text);
			char KeyChar 	= e.getKeyChar();
			boolean next 	= false;

			//System.out.println(e.getKeyChar());

			if (Character.isDigit(KeyChar) && Character.getNumericValue(KeyChar) > 0){
				text.setText("");
				text.setForeground(Color.BLACK);
				next = true;
			}else{
				if (Character.compare(KeyChar,'0') == 0 || Character.compare(KeyChar,' ') == 0 )
					next = true;
				e.setKeyChar('\0');
				text.setText("");
				text.setForeground(Color.BLUE);
			};

			if (next && index < 80)
				m_PointVector.get(index+1).grabFocus();
		};

		public void keyPressed(KeyEvent e){
			int index = m_PointVector.indexOf((JTextField)e.getSource());

			//System.out.println(e.getKeyCode());

			switch(e.getKeyCode()){
				case KeyEvent.VK_UP:
					if (index > 8 ) m_PointVector.get(index-9).grabFocus(); 
					break;
				case KeyEvent.VK_DOWN:
					if (index < 72) m_PointVector.get(index+9).grabFocus(); 
					break;
				case KeyEvent.VK_LEFT:
					if (index > 0 ) m_PointVector.get(index-1).grabFocus(); 
					break;
				case KeyEvent.VK_RIGHT:
					if (index < 80) m_PointVector.get(index+1).grabFocus(); 
					break;
			}
		};
	};

//the main
	public static void main(String args[]){
		MySudoku m_Sudoku = new MySudoku();
	};
};
