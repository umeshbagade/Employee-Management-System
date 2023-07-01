package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MyFrame extends JFrame implements ActionListener
{
    private JDesktopPane jdp;
    private JToolBar tbar;
    private JButton b[];
    private InsertFrame insert_frame;
    private AllEmployeesFrame all_employee_frame;
    private SearchUpdateDeleteFrame search_update_delete_frame;

    public MyFrame()
    {
        super("Employee Management Sysytem");

        tbar = new JToolBar();
        this.add(tbar, BorderLayout.NORTH);
        tbar.setFloatable(false);
        tbar.setLayout(new GridLayout(1,3));

        String arr[] = {"Add New Employee","Update Delete Search","All Employees"};
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
            //Add new Employee

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
                if(all_employee_frame ==arr[i])
                {
                    c++;
                    break;
                }
            }
            if(c==0)
                all_employee_frame=null;

            if(all_employee_frame == null)
            {
                all_employee_frame = new AllEmployeesFrame();
                all_employee_frame.setBounds(10,270,795,220);
                jdp.add(all_employee_frame);
            }
            else
                JOptionPane.showMessageDialog(this,"All Employee Frame is already opened ");
        }
    }
}