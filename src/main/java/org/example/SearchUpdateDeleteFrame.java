package org.example;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.Vector;

class SearchUpdateDeleteFrame extends JInternalFrame implements ActionListener, ListSelectionListener
{
    private JSplitPane split_pane;
    private JPanel panel1,panel2;
    private JLabel lbl,lbl_message;
    private JTextField txt_search, txt[];
    private JList <String> emp_list;
    private JScrollPane jsp;
    private  JButton b[];
    private JDateChooser dateChooser;
    public static Connection con;

    public SearchUpdateDeleteFrame()
    {

        super("Search Update Delete",true,true,true,true);

        //Panel - 1
        panel1 = new JPanel();
        panel1.setLayout(null);

        lbl = new JLabel("Enter Employee Name to Search");
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
        panel2.setLayout(new GridLayout(8,2));

        String str[] = {"    Employee Id", "    Employee Name","    City","    Salary", "    Position", "    Joining Date",};

        txt = new JTextField[6];

        for(int i=0; i<str.length-1;i++)
        {
            lbl = new JLabel(str[i]);
            panel2.add(lbl);

            txt[i]= new JTextField();
            panel2.add(txt[i]);
        }
        txt[0].setEditable(false);

        lbl = new JLabel(str[txt.length-1]);
        panel2.add(lbl);

        dateChooser = new JDateChooser();
        dateChooser.setMaxSelectableDate(java.sql.Date.valueOf(LocalDate.now()));

        panel2.add(dateChooser);



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

    public void actionPerformed(ActionEvent e)
    {
        try
        {
            Object obj = e.getSource();
            if(obj == b[0])
            {
                //Update of searchupdate delete
                String id = txt[0].getText().toUpperCase();
                String ename = txt[1].getText().toUpperCase();
                String city = txt[2].getText().toUpperCase();
                String salary = txt[3].getText();
                java.sql.Date joiningDate = new java.sql.Date(dateChooser.getDate().getTime());
                String position = txt[4].getText().toUpperCase();


                String sql = "update employee set name = ?,city = ?, salary = ?, joiningDate= ?, position=? where id = ?";
                con = Employee.connect();

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(6,id);
                ps.setString(1,ename);
                ps.setString(2,city);
                ps.setString(3,salary);
                ps.setDate(4,joiningDate);
                ps.setString(5,position);


                int n = ps.executeUpdate();
                ps.close();
                Employee.disconnect(con);

                if(n==1)
                {
                    getTableData();
                    lbl_message.setText("Record Updated...");
                    JOptionPane.showMessageDialog(this,"Record Updated Successfully");
                }
                else
                {
                    lbl_message.setText("Record Not Updated...");
                    JOptionPane.showMessageDialog(this,"Record Not Updated");
                }
                getTableData();
            }
            else
            if(obj==b[1])
            {
                String id= txt[0].getText();

                String sql = "delete from employee where id = ?";
                con = Employee.connect();

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1,id);

                int n =ps.executeUpdate();

                if(n==1)
                {
                    getTableData();
                    JOptionPane.showMessageDialog(this,"Record deleted");
                    lbl_message.setText("Record deleted...");
                    for(int i =0;i<txt.length-1; i++)
                        txt[i].setText("");
                    lbl_message.setText("");

                    dateChooser.setDate(null);
                }
                else {
                    JOptionPane.showMessageDialog(this, "Record not deleted");
                    lbl_message.setText("Record not deleted...");
                }
                Employee.disconnect(con);
            }
            else
            if(obj==txt_search)
            {
                String search_text = txt_search.getText().toUpperCase();
                String sql = "select name from employee  where name like '"+search_text+"%' order by name";
                con = Employee.connect();

                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs =ps.executeQuery();

                Vector<String> v = new Vector <String> ();
                while(rs.next())
                {
                    String ename = rs.getString("name");
                    v.add(ename);
                }
                rs.close();
                ps.close();
                Employee.disconnect(con);
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
            String sql = "select * from employee where name = ? ";

            con = Employee.connect();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,sname);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                String temp;
                String id = rs.getString("id");
                String name = rs.getString("name");
                String city = rs.getString("city");
                String salary = rs.getString("salary");
                Date joiningDate = rs.getDate("joiningDate");
                String position = rs.getString("position");

                txt[0].setText(id);
                txt[1].setText(name);
                txt[2].setText(city);
                txt[3].setText(salary);
                dateChooser.setDate(joiningDate);
                txt[4].setText(position);
            }
            rs.close();
            ps.close();
            Employee.disconnect(con);
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
            String sql = "select name from employee order by name";
            con = Employee.connect();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Vector <String> v = new Vector<String>();
            while(rs.next())
            {
                String ename =rs.getString("name");
                v.add(ename);
            }
            rs.close();
            ps.close();
            Employee.disconnect(con);
            emp_list.setListData(v);
        }
        catch(Exception ex)
        {
//            lbl_message.setText(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
