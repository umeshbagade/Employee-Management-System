package org.example;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

class AllEmployeesFrame extends JInternalFrame
{
    private JTable tbl;
    private static Connection con;
    public AllEmployeesFrame()
    {
        super("ALL Employees",true,true,true,true);

        Vector<String> col_names = new Vector <String>();
        String cols[] = {"Sr No.","Employee Id", "Employee Name","City","Salary", "Joining Date", "Position"};

        for(int i=0; i<cols.length; i++)
            col_names.add(cols[i]);

        Vector <Vector> rows = new Vector <Vector>();

        try
        {
            con = Employee.connect();
            String sql = "select id, name, city, salary, joiningDate, position from employee order by id";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int c=0;

            while(rs.next())
            {
                int rn = ++c;//rs.getInt(1);
                String eid = rs.getString(1);
                String ename = rs.getString(2);
                String city = rs.getString(3);
                String salary = rs.getString(4);
                String joiningDate = rs.getString(5);
                String position = rs.getString(6);

                Vector <String> single_row = new Vector <String>();

                single_row.add(rn+"");
                single_row.add(eid);
                single_row.add(ename);
                single_row.add(city);
                single_row.add(salary);
                single_row.add(joiningDate);
                single_row.add(position);
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