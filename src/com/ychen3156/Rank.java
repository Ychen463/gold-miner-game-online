package com.ychen3156;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;


public class Rank extends JFrame{
    private JTable jTable_rankTable;
    private JButton jButton_CANCEL;
    private JLabel label;
    private JPanel contentPane;
    public Rank() throws SQLException, PrinterException {
        setTitle("Ranking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//<-- destroy only this frame
        setBounds(300, 190, 600, 400);
        setResizable(false);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setLocationByPlatform( true );
//
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Current Ranking (Top 10)", TitledBorder.LEFT, TitledBorder.TOP));

        ArrayList data = new ArrayList();
        ArrayList<String> columnNames = new ArrayList<String>();

        try {
            String query = "SELECT `acct`,`gameDateTime`,`points` FROM `GameLogs` order by `points`,`gameDateTime` DESC LIMIT 10 ;";
            ResultSet rs = ConnectionDB.getConnection().prepareStatement(query).executeQuery();
            {
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();

                //  Get column names
                for (int i = 1; i <= columns; i++) {
                    columnNames.add(md.getColumnName(i));
                }
                //  Get row data
                while(rs.next())  {
                    ArrayList row = new ArrayList(columns);
                    for (int i = 1; i <= columns; i++){
                        row.add( rs.getObject(i) );
                    }
                    data.add( row );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Vector columnNamesVector = new Vector();
        Vector dataVector = new Vector();
        for (int i = 0; i < data.size(); i++) {
            ArrayList subArray = (ArrayList) data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++) {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }

        for (int i = 0; i < columnNames.size(); i++)
            columnNamesVector.add(columnNames.get(i));

        //  Create table with database data

        jTable_rankTable = new JTable(dataVector, columnNamesVector);
        jTable_rankTable.setPreferredScrollableViewportSize(jTable_rankTable.getPreferredSize());
        setJTableColumnsWidth(jTable_rankTable, 800, 20, 40, 40);
        JScrollPane scrollpane = new JScrollPane(jTable_rankTable);
        scrollpane.setPreferredSize(new Dimension(480, 300));
        panel.add(scrollpane);
        add(panel);

        jButton_CANCEL = new JButton("Cancel");
        jButton_CANCEL.setBounds(410, 160, 100, 20);
        jButton_CANCEL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        contentPane.add(jButton_CANCEL);
        setVisible( true );

    }

    public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
                                             double... percentages) {
        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int)
                    (tablePreferredWidth * (percentages[i] / total)));
        }
    }
};
