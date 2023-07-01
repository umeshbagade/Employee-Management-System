package org.example;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

class InsertFrame extends JInternalFrame implements ActionListener
{
    private JLabel lbl, lbl_message;
    private JTextField txt[];
    private JButton b;
    private JDateChooser dateChooser;
    private static Connection con;
    public InsertFrame()
    {
        super("Add New Employee",true,true,true,true);
        this.setLayout(new GridLayout(9,2));

        String str[] = {"Employee Id", "Employee Name","City","Salary",  "Position", "Joining Date"};
        txt = new JTextField[str.length];

        for(int i=0 ; i<txt.length-1; i++)
        {
            lbl = new JLabel(str[i]);
            this.add(lbl);

            txt[i] = new JTextField();
            this.add(txt[i]);
        }
        
        lbl = new JLabel(str[txt.length-1]);
        this.add(lbl);

//        txt[txt.length-1] = new JTextField();
//        this.add(txt[txt.length-1]);
        dateChooser = new JDateChooser();
        dateChooser.setMaxSelectableDate(java.sql.Date.valueOf(LocalDate.now()));

        this.add(dateChooser);
        
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

                String eid = txt[0].getText().toUpperCase();
                String ename = txt[1].getText().toUpperCase();
                String city = txt[2].getText().toUpperCase();
                Double salary = Double.parseDouble(txt[3].getText());
                Date date = new java.sql.Date(dateChooser.getDate().getTime());
                String position = txt[4].getText().toUpperCase();

                String sql = "insert into employee values(?,?,?,?,?,?)";

                con = Employee.connect();
                PreparedStatement ps = con.prepareStatement(sql);


                ps.setString(1,eid);
                ps.setString(2,ename);
                ps.setString(3,city);
                ps.setDouble(4,salary);
                ps.setDate(5, date);
                ps.setString(6, position);

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

        }else{

        }
    }
}
