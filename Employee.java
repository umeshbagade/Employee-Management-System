import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.event.*;

//*************************** 1st INSERT FRAME ******************************************
class InsertFrame extends JInternalFrame implements ActionListener
{
	private JLabel lbl, lbl_message;
	private JTextField txt[];
	private JButton b;

	public InsertFrame()
	{
		super("Add New Student",true,true,true,true);
		this.setLayout(new GridLayout(7,2));

		String str[] = {"ID Code", "Student Name","Town Name","DOB (dd-mmm-yyyy)"};
		txt = new JTextField[4];

		for(int i=0 ; i<txt.length; i++)
		{
			lbl = new JLabel(str[i]);
			this.add(lbl);

		    txt[i] = new JTextField();
		    this.add(txt[i]);
		} 

		String arr[] = {"Insert","Clear"};
		for(int i=0; i<arr.length; i++)
		{
			b = new JButton(arr[i]);
			this.add(b);
			b.addActionListener(this);
		}

		lbl_message = new JLabel("...");
		this.add(lbl_message);

		this.setVisible(true);
		this.setSize(200,250);
	}
	public void actionPerformed(ActionEvent e)
	{
		String cap = e.getActionCommand();
		if(cap.equalsIgnoreCase("Insert"))
		{
			try
			{
				
				String id = txt[0].getText().toUpperCase();
				String sname = txt[1].getText().toUpperCase();
				String tn = txt[2].getText().toUpperCase();
				String dob = txt[3].getText();

				String sql = "insert into students values(?,?,?,?)";

				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","system","Kartik1842");
				PreparedStatement ps = con.prepareStatement(sql);

				
				ps.setString(1,id);
				ps.setString(2,sname);
				ps.setString(3,tn);
				ps.setString(4,dob);

				int n = ps.executeUpdate();
				ps.close();
				con.close();

				if(n==1)
				{
					lbl_message.setText("Record Inserted Successfully");
					lbl_message.setForeground(Color.BLUE);
				}
				else
				{
					lbl_message.setText("Record Not Inserted");
					lbl_message.setForeground(Color.RED);
				}
			}
			catch(Exception ex)
			{
				lbl_message.setText(ex.getMessage());
				ex.printStackTrace();
			}
		}
		else
			if(cap.equalsIgnoreCase("Clear"))
			{
				for(int i=0;i<txt.length; i++)
				txt[i].setText("");

				lbl_message.setText("");

			}
	}
}
//*************** 2ndFRAME SEARCH UPDATE DELETE ***********************************************************
class SearchUpdateDeleteFrame extends JInternalFrame implements ActionListener, ListSelectionListener
{
	private JSplitPane split_pane;
	private JPanel panel1,panel2;
	private JLabel lbl,lbl_message;
	private JTextField txt_search, txt[];
	private JList <String> emp_list;
	private JScrollPane jsp;
	private  JButton b[];
	public static Connection con;

	public SearchUpdateDeleteFrame()
	{
		
		super("Search Update Delete",true,true,true,true);

		//Panel - 1
		panel1 = new JPanel();
		panel1.setLayout(null);

		lbl = new JLabel("Enter Student Name to Search");
		lbl.setBounds(10,10,200,30);
		panel1.add(lbl);

		txt_search = new JTextField(20);
		txt_search.setBounds(10,40,200,30);
		panel1.add(txt_search);
		txt_search.addActionListener(this);

		emp_list = new JList <String>();
		jsp = new JScrollPane(emp_list);
		jsp.setBounds(10,80,200,150);
		panel1.add(jsp);
		getTableData();

		emp_list.addListSelectionListener(this);

		//Panel 2
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(6,2));

		String str[] = {"    ID Code", "    Student Name","    Town Name","    Date of Birth"};
		txt = new JTextField[4];

		for(int i=0; i<4;i++)
		{
			lbl = new JLabel(str[i]);
			panel2.add(lbl);

			txt[i]= new JTextField();
			panel2.add(txt[i]);
		}
		txt[0].setEditable(false);

		String arr[] = {"Update","Delete"};
		b = new JButton[2];
		for(int i=0;i<arr.length;i++)
		{
			b[i] = new JButton(arr[i]);
			panel2.add(b[i]);
			b[i].addActionListener(this);
		}

		lbl_message = new JLabel("---");
		panel2.add(lbl_message);

		split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel1,panel2);
		split_pane.setDividerLocation(211);
		split_pane.setDividerSize(1);
		this.add(split_pane);

		this.setSize(400,300);
		this.setVisible(true);

	}
	public void connect() throws Exception
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","system","Kartik1842");
	}
	public void disconnect() throws Exception
	{
		if(!con.isClosed())
			con.close();
	}
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			Object obj = e.getSource();
			if(obj == b[0])
			{
				//Update of searchupdate delete
				String id = txt[0].getText().toUpperCase();
				String sname = txt[1].getText().toUpperCase();
				String tn = txt[2].getText().toUpperCase();
				String dob = txt[3].getText().toUpperCase();

				String sql = "update students set stud_name = ?,town_name = ?, dob = ? where id_code = ?";
				connect();

				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1,sname);
				ps.setString(2,tn);
				ps.setString(3,dob);
				ps.setString(4,id);

				int n = ps.executeUpdate();
				ps.close();
				disconnect();

				if(n==1)
				{
					getTableData();
					JOptionPane.showMessageDialog(this,"Record Updated Successfully");
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Record Not Updated");
				}
				getTableData();
			}
			else
			if(obj==b[1])
			{
				String id= txt[0].getText();

				String sql = "delete from students where id_code = ?";
				connect();

				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1,id);

				int n =ps.executeUpdate();

				if(n==1)
				{
					getTableData();
					JOptionPane.showMessageDialog(this,"Record deleted");

					for(int i =0;i<txt.length; i++)
					txt[i].setText("");
					lbl_message.setText("");
				}
				else
					JOptionPane.showMessageDialog(this,"Record not deleted");
			}
			else
			if(obj==txt_search)
			{
				String search_text = txt_search.getText().toUpperCase();
				String sql = "select stud_name from students  where stud_name like '"+search_text+"%' order by stud_name";
				connect();

				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs =ps.executeQuery();

				Vector <String> v = new Vector <String> ();
				while(rs.next())
				{
					String sname = rs.getString("stud_name");
					v.add(sname);
				}
				rs.close();
				ps.close();
				disconnect();
				emp_list.setListData(v);
			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this,ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		try
		{
			String sname = (String) emp_list.getSelectedValue();
			String sql="select * from students where stud_name = ? ";
	
			connect();

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1,sname); 
			ResultSet rs = ps.executeQuery();

			if(rs.next())
			{
				String temp;
				String id = rs.getString("id_code");
				String tn = rs.getString("town_name");
				String dob = rs.getString("dob");

				 temp = dob;
				String s=temp.substring(0,10);
		
				String year = s.substring(0, 4);
				String month = s.substring(5, 7);
				String day = s.substring(8, 10);
				String newm = "";
				
				switch(month)
				{
					case "01": newm = "JAN"; break;
					case "02": newm = "FEB"; break;
					case "03": newm = "MAR"; break;
					case "04": newm = "APR"; break;
					case "05": newm = "MAY"; break;
					case "06": newm = "JUN"; break;
					case "07": newm = "JUL"; break;
					case "08": newm = "AUG"; break;
					case "09": newm = "SEP"; break;
					case "10": newm = "OCT"; break;
					case "11": newm = "NOV"; break;
					case "12": newm = "DEC"; break;
				}
		
				dob = day+"-"+newm+"-"+year;

				txt[0].setText(id);
				txt[1].setText(sname);
				txt[2].setText(tn);
				txt[3].setText(dob);
			}
			rs.close();
			ps.close();
			disconnect();
		}
		catch(Exception ex)
		{
			lbl_message.setText(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void getTableData()
	{
		try
		{
			String sql = "select stud_name from students order by stud_name";
			connect();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			Vector <String> v = new Vector<String>(); 
			while(rs.next())
			{
				String sname =rs.getString("stud_name");
				v.add(sname);
			}
			rs.close();
			ps.close();
			disconnect();
			emp_list.setListData(v);
		}
		catch(Exception ex)
		{
			lbl_message.setText(ex.getMessage());
			ex.printStackTrace();
		}
	}	
}
//************************** 3rd FRAME ALL STUDENTS FRAME ****************************************************
class AllStudentsFrame extends JInternalFrame 
{
	private JTable tbl;

	public AllStudentsFrame()
	{
		super("ALL Students",true,true,true,true);

		Vector <String> col_names = new Vector <String>();
		String cols[] = {"Sr No.","ID Code", "Student Name","City","Date of Birth"};

		for(int i=0; i<cols.length; i++)
		col_names.add(cols[i]);

		Vector <Vector> rows = new Vector <Vector>();

		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","system","Kartik1842");
			String sql = "select id_code,stud_name,town_name,to_Char(dob,'dd-MON-yyyy') from students order by id_code";

			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			int c=0;

			while(rs.next())
			{
				int rn = ++c;//rs.getInt(1);
				String id = rs.getString(1);
				String sname = rs.getString(2);
				String tn = rs.getString(3);
				String dob = rs.getString(4);

				Vector <String> single_row = new Vector <String>();

				single_row.add(rn+"");
				single_row.add(id);
				single_row.add(sname);
				single_row.add(tn);
				single_row.add(dob);
				rows.add(single_row);
			}
			rs.close();
			ps.close();
			con.close();

			tbl = new JTable(rows,col_names);
			JTableHeader th = tbl.getTableHeader();
			th.setBackground(Color.PINK);
			th.setForeground(Color.BLUE);
			JScrollPane jsp = JTable.createScrollPaneForTable(tbl);
			this.add(jsp, BorderLayout.CENTER);

		}
		catch(Exception ex)
		{
			JOptionPane.showInputDialog(this,ex.getMessage());
			ex.printStackTrace();
		}

		this.setVisible(true);
		this.setSize(650,300);
	}
}
//******************** MAIN FRAME ***********************************************************************************
class MyFrame extends JFrame implements ActionListener
{
	private JDesktopPane jdp;
	private JToolBar tbar;
	private JButton b[];
	private InsertFrame insert_frame;
	private AllStudentsFrame all_student_frame;
	private SearchUpdateDeleteFrame search_update_delete_frame;

	public MyFrame()
	{
		super("Student Management Sysytem");

		tbar = new JToolBar();
		this.add(tbar, BorderLayout.NORTH);
		tbar.setFloatable(false);
		tbar.setLayout(new GridLayout(1,3));

		String arr[] = {"Add New Student","Update Delete Search","All Students"};
		b = new JButton[3];

		for(int i=0;i<arr.length; i++)
		{
			b[i]= new JButton(arr[i]);
			tbar.add(b[i]);
			b[i].addActionListener(this);
		}

		jdp = new JDesktopPane();
		this.add(jdp);

		this.setVisible(true);
		this.setSize(835,600);
	}
	public void actionPerformed(ActionEvent e)
	{

		Object obj = e.getSource();
		if(obj == b[0])
		{
			//Add new Student

			JInternalFrame arr[] = jdp.getAllFrames();
			int c =0;
			for(int i=0;i<arr.length; i++)
			{
				if(insert_frame == arr[i])
				{
					c++;
					break;
				}
			}
			if(c==0)
				insert_frame = null;
			else
				JOptionPane.showMessageDialog(this,"Insert frame is already opened");

			if(insert_frame == null)
			{
				insert_frame = new InsertFrame();
				jdp.add(insert_frame);
				insert_frame.setBounds(10,10,300,250);
			}			
		}
		else 
		if(obj == b[1])
		{
			JInternalFrame arr[] = jdp.getAllFrames();
			int c =0;
			for(int i=0;i<arr.length; i++)
			{
				if(search_update_delete_frame == arr[i])
				{
					c++;
					break;
				}
			}
			if(c==0)
				search_update_delete_frame = null;
			else
				JOptionPane.showMessageDialog(this,"Search Update Delete frame is already opened");

			if(search_update_delete_frame == null)
			{
				search_update_delete_frame = new SearchUpdateDeleteFrame();
				jdp.add(search_update_delete_frame);
				search_update_delete_frame.setBounds(320,10,485,250);
			}				
		}
		if(obj == b[2])
		{
			JInternalFrame arr[] = 	jdp.getAllFrames();
			int c=0;
			for(int i=0;i<arr.length; i++)
			{
				if(all_student_frame ==arr[i])
				{
					c++;
					break;
				}
			}
			if(c==0)
				all_student_frame=null;

			if(all_student_frame == null)
			{
				all_student_frame = new AllStudentsFrame();
				all_student_frame.setBounds(10,270,795,220);
				jdp.add(all_student_frame);
			}
			else
				JOptionPane.showMessageDialog(this,"All Student Frame is already opened ");
		}
	}
}
//****************************************************************************************
class Employee
{
	public static void main(String[] args) 
	{
		MyFrame f = new MyFrame();	
	}
}
