
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private JPanel MainPanel;
    private JButton megtek;
    private JPanel TablePanel;
    private JPanel GombPanel;
    private JButton search;
    private JButton valt;
    private JTextField searchField;
    private JLabel hibaText;
    private JPanel FRAME;
    private JPanel Notific;
    private JPanel BefPanel;
    private JPanel Gomb2Panel;
    private JPanel BefTablePanel;
    private JPanel DataBaseAddPanel;
    private JPanel GombPanel3;
    private JButton visszaButton;
    private JButton DataBaseAddBut;
    private JPanel twoInPanel;
    private JPanel twoInLeftPanel;
    private JLabel hozLab;
    private JPanel twoInMidPanel;
    private JPanel twoInRightPanel;
    private JTable table;
    private JTable befTable;
    private List<Data> data;
    private List<Data> bajcsa;
    private List<Data> keresztur;
    private JList list;
    private String valforS = null;
    private int valasztottB;
    private int numberOfFound;
    private String baseDataPath;
    private HibaShow hibaShow;
    private DoubleClickListener dcl = new DoubleClickListener();

    // TODO: Normálisra megcsinálni az adatbázis hozzáadást [OPEN]

    private GUI() {
        $$$setupUI$$$();
        valasztottB = 0;
        numberOfFound = 0;
        bajcsa = new ArrayList<>();
        keresztur = new ArrayList<>();
        data = new ArrayList<>();
        hibaShow = new HibaShow(hibaText);

        TablePanel.setVisible(false);
        table = new JTable();
        table.setName("MainTable");
        Object[] dataa = {"Cikkszám", "Cikk név", "Min. rendelhető"};
        String[] boltok = {"Bajcsa", "Múrakeresztúr"};
        JComboBox combo = new JComboBox(boltok);
        JComboBox comboo = new JComboBox(boltok);
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Cikkszám");
        model.addColumn("Cikk név");
        model.addColumn("Min. rendelhető");
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        TablePanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
        TablePanel.add(table, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.addMouseListener(new PopClickListener(table.getName()));
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                super.keyPressed(keyEvent);
                if (keyEvent.getExtendedKeyCode() == 10 && numberOfFound == 1) {
                    int selected = (int) table.getModel().getValueAt(0, 0);
                    if (valasztottB == 0) {
                        boolean inAlready = false;
                        for (Data d : bajcsa) {
                            if (data.get(selected).getCS().equalsIgnoreCase(d.getCS())) {
                                inAlready = true;
                            }
                        }
                        if (!inAlready) {
                            bajcsa.add(new Data(data.get(selected).getCC(), data.get(selected).getCS(), data.get(selected).getCN(), data.get(selected).getMin(), data.get(selected).getME(), data.get(selected).getAfa(), data.get(selected).getMA()));
                            hibaShow.show(Color.green, "Hozzáadva ide: Bajcsa", true);
                            DefaultTableModel modell = (DefaultTableModel) table.getModel();
                            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                                modell.removeRow(i);
                            }
                            for (Data d : data) {
                                modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                            }
                        } else {
                            hibaShow.show(Color.red, "Ezt már hozzáadtad!", true);
                        }
                    } else if (valasztottB == 1) {
                        boolean inAlready = false;
                        for (Data d : keresztur) {
                            if (data.get(selected).getCS().equalsIgnoreCase(d.getCS())) {
                                inAlready = true;
                            }
                        }
                        if (!inAlready) {
                            keresztur.add(new Data(data.get(selected).getCC(), data.get(selected).getCS(), data.get(selected).getCN(), data.get(selected).getMin(), data.get(selected).getME(), data.get(selected).getAfa(), data.get(selected).getMA()));
                            hibaShow.show(Color.green, "Hozzáadva ide: Múrakeresztúr", true);
                            DefaultTableModel modell = (DefaultTableModel) table.getModel();
                            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                                modell.removeRow(i);
                            }
                            for (Data d : data) {
                                modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                            }
                        } else {
                            hibaShow.show(Color.red, "Ezt már hozzáadtad!", true);
                        }
                    }
                }
                searchField.requestFocus();
            }
        });
        TablePanel.add(scrollPane);
        megtek.addActionListener(actionEvent -> {
            CardLayout c = (CardLayout) FRAME.getLayout();
            c.show(FRAME, "DataBaseAddPanel");
            twoInPanel.add(Notific, BorderLayout.SOUTH);
        });
        list = new JList(dataa);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setSelectedIndex(0);
        valforS = (String) dataa[0];
        list.addListSelectionListener(listSelectionEvent -> valforS = (String) list.getSelectedValue());
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(200, 40));
        GombPanel.add(listScroller);
        combo.setSelectedIndex(0);
        combo.addActionListener(actionEvent -> valasztottB = combo.getSelectedIndex());
        GombPanel.add(new JLabel("Választott bolt: "));
        GombPanel.add(combo);
        valt = new JButton("Befejezés");
        valt.addActionListener(actionEvent -> {
            CardLayout c = (CardLayout) FRAME.getLayout();
            c.show(FRAME, "BefPanel");
            comboo.setSelectedIndex(valasztottB);
            BefTablePanel.add(Notific, BorderLayout.SOUTH);
        });
        GombPanel.add(valt);
        search.addActionListener(actionEvent -> search());
        JButton back = new JButton("<<Vissza<<");
        back.addActionListener(actionEvent -> {
            CardLayout c = (CardLayout) FRAME.getLayout();
            c.show(FRAME, "MainPanel");
            valasztottB = 0;
            combo.setSelectedIndex(0);
            TablePanel.add(Notific, BorderLayout.SOUTH);
        });
        befTable = new JTable();
        befTable.setName("BefTable");
        DefaultTableModel modell = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 2 && dcl.isDoubleClicked()) {
                    befTable.setValueAt("", row, col);
                }
                return col == 2;
            }
        };
        modell.addColumn("Cikkszám");
        modell.addColumn("Cikk név");
        modell.addColumn("Min. rendelhető");
        befTable.setModel(modell);
        befTable.addMouseListener(new PopClickListener(befTable.getName()));
        BefTablePanel.add(befTable.getTableHeader(), BorderLayout.PAGE_START);
        BefTablePanel.add(befTable, BorderLayout.CENTER);
        JScrollPane scrollPanel = new JScrollPane(befTable);
        befTable.setFillsViewportHeight(true);
        BefTablePanel.add(scrollPanel);
        comboo.addActionListener(actionEvent -> {
            DefaultTableModel mode = (DefaultTableModel) befTable.getModel();
            valasztottB = comboo.getSelectedIndex();
            for (int i = befTable.getRowCount() - 1; i >= 0; i--) {
                mode.removeRow(i);
            }
            if (valasztottB == 0) {
                for (Data d : bajcsa) {
                    mode.addRow(new Object[]{d.getCS(), d.getCN(), (int) d.getMin()});
                }
            } else if (valasztottB == 1) {
                for (Data d : keresztur) {
                    mode.addRow(new Object[]{d.getCS(), d.getCN(), (int) d.getMin()});
                }
            }
        });
        JCheckBox excel = new JCheckBox();
        excel.setText("Excel");
        excel.setSelected(true);
        JCheckBox ods = new JCheckBox();
        ods.setText("OpenS");
        JButton createTable = new JButton("Rendelés készítése");
        createTable.addActionListener(actionEvent -> {
            if (valasztottB == 0) {
                try {
                    List<Double> inTableNum = new ArrayList<>();
                    for (int i = 0; i < befTable.getRowCount(); i++) {
                        inTableNum.add(Double.parseDouble(befTable.getValueAt(i, 2).toString()));
                    }
                    for (int i = 0; i < inTableNum.size(); i++) {
                        bajcsa.get(i).setMin(inTableNum.get(i));
                    }
                    Thread ren = new Thread(() -> {
                        hibaShow.show(Color.green, "Rendelés készítése", true);
                        try {
                            if (excel.isSelected()) {
                                ExcelInterface.SaveAsXls(boltok[0], bajcsa);
                            }
                            if (ods.isSelected()) {
                                ExcelInterface.saveAsOds(boltok[0], bajcsa);
                            }
                            hibaShow.show(Color.green, "Rendelés elkészült!", true);
                        } catch (IOException e) {
                            hibaShow.show(Color.red, "Hiba a rendelés készíteskor! (Lehet meg van nyitva!)", true);
                        }
                    });
                    ren.start();
                } catch (NumberFormatException e) {
                    hibaShow.show(Color.red, "Csak számokat írhatsz!", true);
                }
            } else if (valasztottB == 1) {
                try {
                    List<Double> inTableNum = new ArrayList<>();
                    for (int i = 0; i < befTable.getRowCount(); i++) {
                        inTableNum.add(Double.parseDouble(befTable.getValueAt(i, 2).toString()));
                    }
                    for (int i = 0; i < inTableNum.size(); i++) {
                        keresztur.get(i).setMin(inTableNum.get(i));
                    }
                    Thread ren = new Thread(() -> {
                        hibaShow.show(Color.green, "Rendelés készítése", true);
                        try {
                            if (excel.isSelected()) {
                                ExcelInterface.SaveAsXls(boltok[1], keresztur);
                            }
                            if (ods.isSelected()) {
                                ExcelInterface.saveAsOds(boltok[1], keresztur);
                            }
                            hibaShow.show(Color.green, "Rendelés elkészült!", true);
                        } catch (IOException e) {
                            hibaShow.show(Color.red, "Hiba a rendelés készíteskor! (Lehet meg van nyitva!)", false);
                        }
                    });
                    ren.start();
                } catch (NumberFormatException e) {
                    hibaShow.show(Color.red, "Csak számokat írhatsz!", true);
                }
            }
        });
        Gomb2Panel.add(back);
        Gomb2Panel.add(new JLabel("  Választott rendelés: "));
        Gomb2Panel.add(comboo);
        Gomb2Panel.add(new JLabel(" Választott formátum: "));
        Gomb2Panel.add(excel);
        Gomb2Panel.add(ods);
        Gomb2Panel.add(createTable);
        final JTextField DataBasePathField = new JTextField();
        DataBasePathField.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> dropped = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : dropped) {
                        DataBasePathField.setText(file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        twoInMidPanel.add(DataBasePathField, BorderLayout.NORTH);
        visszaButton.addActionListener(actionEvent -> {
            CardLayout c = (CardLayout) FRAME.getLayout();
            c.show(FRAME, "MainPanel");
            TablePanel.add(Notific, BorderLayout.SOUTH);
            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) table.getModel()).removeRow(i);
            }
            DefaultTableModel modelll = (DefaultTableModel) table.getModel();

            for (Data d : data) {
                modelll.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
            }
        });
        DataBaseAddBut.addActionListener(actionEvent -> {
            String path = DataBasePathField.getText();
            if (path.isEmpty()) {
                hibaShow.show(Color.red, "Nem írtál be semmit!", true);
            }
            String[] tmp = path.split("[.]");
            final boolean[] good = {false};
            final boolean[] runs = {false};
            Thread be;
            if (tmp[tmp.length - 1].startsWith("ods")) {
                be = new Thread(() -> {
                    try {
                        runs[0] = true;
                        hibaShow.show(Color.green, "Beolvasás...", false);
                        DataBaseAddBut.setEnabled(false);
                        visszaButton.setEnabled(false);
                        data = ExcelInterface.getDataFromOds(path);
                        DataBaseAddBut.setEnabled(true);
                        visszaButton.setEnabled(true);
                        hibaShow.show(Color.green, "Beolvasás kész!", true);
                        good[0] = true;
                        runs[0] = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        hibaShow.show(Color.red, "Nem Megendett fájl!", true);
                        DataBaseAddBut.setEnabled(true);
                        visszaButton.setEnabled(true);
                    }
                });
                be.start();
            } else if (tmp[tmp.length - 1].startsWith("xls")) {
                be = new Thread(() -> {
                    try {
                        runs[0] = true;
                        hibaShow.show(Color.green, "Beolvasás...", false);
                        DataBaseAddBut.setEnabled(false);
                        visszaButton.setEnabled(false);
                        data = ExcelInterface.getDatafromXls(path);
                        DataBaseAddBut.setEnabled(true);
                        visszaButton.setEnabled(true);
                        hibaShow.show(Color.green, "Beolvasás kész!", true);
                        good[0] = true;
                        runs[0] = false;
                    } catch (IOException | InvalidFormatException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        hibaShow.show(Color.red, "Nem Megendett fájl!", true);
                        DataBaseAddBut.setEnabled(true);
                        visszaButton.setEnabled(true);
                    }
                });
                be.start();
            } else {
                hibaShow.show(Color.red, "Nem Megendett fájl!", true);
            }
            Thread ment = new Thread(() -> {
                while (runs[0]) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!runs[0]) break;
                }
                if (good[0]) {
                    try {
                        FileOutputStream saveFile = new FileOutputStream("Save.sav");
                        ObjectOutputStream save = new ObjectOutputStream(saveFile);
                        save.writeObject(path);
                        save.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            ment.start();
            DataBasePathField.setText("");
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                super.keyPressed(keyEvent);
                if (keyEvent.getExtendedKeyCode() == 10) {
                    search();
                    table.requestFocus();
                }
            }
        });
        searchField.setName("searchField");
        searchField.addMouseListener(new PopClickListener(searchField.getName()));
        checkSave();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().FRAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 500));
        frame.pack();
        frame.setTitle("Rendelő");
        frame.setVisible(true);
    }


    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    private void search() {
        String mit = searchField.getText();
        boolean found = false;
        DefaultTableModel modell = (DefaultTableModel) table.getModel();
        int ind = 0;
        numberOfFound = 0;
        if (mit.isEmpty()) {
            hibaShow.show(Color.red, "Nem írtál be semmit!", true);
        } else if (valforS != null) {
            switch (valforS) {
                case "Cikkszám":
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        modell.removeRow(i);
                    }
                    for (Data d : data) {
                        if (d.getCS().startsWith(mit)) {
                            ind++;
                            found = true;
                            modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                        }
                    }
                    break;
                case "Cikk név":
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        modell.removeRow(i);
                    }
                    for (Data d : data) {
                        if (d.getCN().startsWith(mit.toUpperCase())) {
                            ind++;
                            found = true;
                            modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                        }
                    }
                    break;
                case "Min. rendelhető":
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        modell.removeRow(i);
                    }
                    for (Data d : data) {
                        if (String.valueOf(d.getMin()).startsWith(mit)) {
                            ind++;
                            found = true;
                            modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                        }
                    }
                    break;
            }
            if (!found) {
                hibaShow.show(Color.red, "Nincs találat!", true);
                for (Data d : data) {
                    modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                }
            } else {
                numberOfFound = ind;
                hibaShow.show(Color.green, "Találat!", true);
            }
        } else {
            hibaShow.show(Color.red, "Nincsenek keresési feltételek!", true);
        }
        searchField.setText("");
    }

    private void checkSave() {
        boolean vanSave = true;
        try {
            FileInputStream saveFileIn = new FileInputStream("Save.sav");
            ObjectInputStream restore = new ObjectInputStream(saveFileIn);
            baseDataPath = (String) restore.readObject();
            if (baseDataPath.isEmpty()) {
                vanSave = false;
            }
        } catch (FileNotFoundException | EOFException e) {
            vanSave = false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (vanSave) {
            CardLayout c = (CardLayout) FRAME.getLayout();
            c.show(FRAME, "MainPanel");
            Thread t = new Thread(() -> {
                try {
                    TablePanel.setVisible(true);
                    table.setVisible(false);
                    megtek.setEnabled(false);
                    search.setEnabled(false);
                    valt.setEnabled(false);
                    searchField.setEnabled(false);
                    hibaShow.show(Color.green, "Betöltés...", false);
                    String[] tmp = baseDataPath.split("[.]");
                    if (tmp[tmp.length - 1].startsWith("xls")) {
                        data = ExcelInterface.getDatafromXls(baseDataPath);
                    } else if (tmp[tmp.length - 1].startsWith("ods")) {
                        data = ExcelInterface.getDataFromOds(baseDataPath);
                    }
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        ((DefaultTableModel) table.getModel()).removeRow(i);
                    }
                    DefaultTableModel modell = (DefaultTableModel) table.getModel();

                    for (Data d : data) {
                        modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                    }
                    hibaShow.show(Color.green, "Betöltés kész!", true);
                    megtek.setEnabled(true);
                    search.setEnabled(true);
                    valt.setEnabled(true);
                    searchField.setEnabled(true);
                    table.setVisible(true);
                } catch (IOException | InvalidFormatException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        } else {
            CardLayout c = (CardLayout) FRAME.getLayout();
            c.show(FRAME, "DataBaseAddPanel");
            twoInPanel.add(Notific, BorderLayout.SOUTH);
            hibaShow.show(Color.red, "Nincs hozzá adva megfelelő adatbázis!", true);
            megtek.setEnabled(true);
            search.setEnabled(true);
            valt.setEnabled(true);
            searchField.setEnabled(true);
            table.setVisible(true);
            TablePanel.setVisible(true);
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        FRAME = new JPanel();
        FRAME.setLayout(new CardLayout(0, 0));
        MainPanel = new JPanel();
        MainPanel.setLayout(new BorderLayout(0, 0));
        FRAME.add(MainPanel, "MainPanel");
        GombPanel = new JPanel();
        GombPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        MainPanel.add(GombPanel, BorderLayout.NORTH);
        megtek = new JButton();
        megtek.setText("Adatbázis modosítás");
        GombPanel.add(megtek);
        search = new JButton();
        search.setText("Keresés");
        GombPanel.add(search);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(100, 20));
        GombPanel.add(searchField);
        TablePanel = new JPanel();
        TablePanel.setLayout(new BorderLayout(0, 0));
        MainPanel.add(TablePanel, BorderLayout.CENTER);
        TablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10), null));
        Notific = new JPanel();
        Notific.setLayout(new BorderLayout(0, 0));
        Notific.setBackground(new Color(-5855578));
        TablePanel.add(Notific, BorderLayout.SOUTH);
        hibaText = new JLabel();
        hibaText.setBackground(new Color(-6447715));
        hibaText.setEnabled(true);
        Font hibaTextFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 20, hibaText.getFont());
        if (hibaTextFont != null) hibaText.setFont(hibaTextFont);
        hibaText.setForeground(new Color(-65536));
        hibaText.setHorizontalAlignment(0);
        hibaText.setHorizontalTextPosition(4);
        hibaText.setText("Label");
        Notific.add(hibaText, BorderLayout.CENTER);
        BefPanel = new JPanel();
        BefPanel.setLayout(new BorderLayout(5, 5));
        FRAME.add(BefPanel, "BefPanel");
        Gomb2Panel = new JPanel();
        Gomb2Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        BefPanel.add(Gomb2Panel, BorderLayout.NORTH);
        Gomb2Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        BefTablePanel = new JPanel();
        BefTablePanel.setLayout(new BorderLayout(0, 0));
        BefPanel.add(BefTablePanel, BorderLayout.CENTER);
        BefTablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10), null));
        DataBaseAddPanel = new JPanel();
        DataBaseAddPanel.setLayout(new BorderLayout(5, 5));
        FRAME.add(DataBaseAddPanel, "DataBaseAddPanel");
        GombPanel3 = new JPanel();
        GombPanel3.setLayout(new BorderLayout(5, 5));
        DataBaseAddPanel.add(GombPanel3, BorderLayout.NORTH);
        GombPanel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), null));
        visszaButton = new JButton();
        visszaButton.setText("<<Vissza<<");
        GombPanel3.add(visszaButton, BorderLayout.WEST);
        twoInPanel = new JPanel();
        twoInPanel.setLayout(new BorderLayout(0, 0));
        DataBaseAddPanel.add(twoInPanel, BorderLayout.CENTER);
        twoInPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 5, 0, 10), null));
        twoInLeftPanel = new JPanel();
        twoInLeftPanel.setLayout(new BorderLayout(0, 0));
        twoInPanel.add(twoInLeftPanel, BorderLayout.WEST);
        twoInLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        hozLab = new JLabel();
        hozLab.setText("Hozzáadandó fájl:");
        twoInLeftPanel.add(hozLab, BorderLayout.NORTH);
        twoInMidPanel = new JPanel();
        twoInMidPanel.setLayout(new BorderLayout(0, 0));
        twoInPanel.add(twoInMidPanel, BorderLayout.CENTER);
        twoInMidPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5), null));
        twoInRightPanel = new JPanel();
        twoInRightPanel.setLayout(new BorderLayout(0, 0));
        twoInPanel.add(twoInRightPanel, BorderLayout.EAST);
        twoInRightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5), null));
        DataBaseAddBut = new JButton();
        DataBaseAddBut.setText("Hozzáadás");
        twoInRightPanel.add(DataBaseAddBut, BorderLayout.NORTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return FRAME;
    }

    class PopUpDemo extends JPopupMenu {
        JMenuItem add;
        JMenuItem delete;
        JMenuItem copy;
        JMenuItem paste;
        String where;
        Point p;

        PopUpDemo(String name, Point p) {
            this.where = name;
            this.p = p;
            copy = new JMenuItem("Cella másolása");
            copy.addActionListener(actionEvent -> {
                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);
                StringSelection entry = new StringSelection(table.getValueAt(table.convertRowIndexToView(row), table.convertColumnIndexToView(col)).toString());
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                c.setContents(entry, null);
            });
            paste = new JMenuItem("Beillesztés");
            paste.addActionListener(actionEvent -> {
                try {
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    searchField.setText(data);
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            });
            add = new JMenuItem("Hozzáad");
            add.addActionListener(actionEvent -> {
                try {
                    if ((int) table.getModel().getValueAt(table.getSelectedRow(), 0) != -1) {
                        int selected = (int) table.getModel().getValueAt(table.getSelectedRow(), 0);
                        if (valasztottB == 0) {
                            boolean inAlready = false;
                            for (Data d : bajcsa) {
                                if (data.get(selected).getCS().equalsIgnoreCase(d.getCS())) {
                                    inAlready = true;
                                }
                            }
                            if (!inAlready) {
                                bajcsa.add(new Data(data.get(selected).getCC(), data.get(selected).getCS(), data.get(selected).getCN(), data.get(selected).getMin(), data.get(selected).getME(), data.get(selected).getAfa(), data.get(selected).getMA()));
                                hibaShow.show(Color.green, "Hozzáadva ide: Bajcsa", true);
                                DefaultTableModel modell = (DefaultTableModel) table.getModel();
                                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                                    modell.removeRow(i);
                                }
                                for (Data d : data) {
                                    modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                                }
                            } else {
                                hibaShow.show(Color.red, "Ezt már hozzáadtad!", true);
                            }
                        } else if (valasztottB == 1) {
                            boolean inAlready = false;
                            for (Data d : keresztur) {
                                if (data.get(selected).getCS().equalsIgnoreCase(d.getCS())) {
                                    inAlready = true;
                                }
                            }
                            if (!inAlready) {
                                keresztur.add(new Data(data.get(selected).getCC(), data.get(selected).getCS(), data.get(selected).getCN(), data.get(selected).getMin(), data.get(selected).getME(), data.get(selected).getAfa(), data.get(selected).getMA()));
                                hibaShow.show(Color.green, "Hozzáadva ide: Múrakeresztúr", true);
                                DefaultTableModel modell = (DefaultTableModel) table.getModel();
                                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                                    modell.removeRow(i);
                                }
                                for (Data d : data) {
                                    modell.addRow(new Object[]{d.getIndex(), d.getCS(), d.getCN(), (int) d.getMin()});
                                }
                            } else {
                                hibaShow.show(Color.red, "Ezt már hozzáadtad!", true);
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    hibaShow.show(Color.red, "Nem választottál ki semmit!", true);
                }
            });
            delete = new JMenuItem("Törlés");
            delete.addActionListener(actionEvent -> {
                int selected = befTable.getSelectedRow();
                DefaultTableModel mode = (DefaultTableModel) befTable.getModel();
                if (selected != -1) {
                    if (valasztottB == 0) {
                        bajcsa.remove(selected);
                        for (int i = befTable.getRowCount() - 1; i >= 0; i--) {
                            mode.removeRow(i);
                        }
                        for (Data d : bajcsa) {
                            mode.addRow(new Object[]{d.getCS(), d.getCN(), (int) d.getMin()});
                        }
                        hibaShow.show(Color.green, "Törölve innen: Bajcsa", true);
                    } else if (valasztottB == 1) {
                        keresztur.remove(selected);
                        for (int i = befTable.getRowCount() - 1; i >= 0; i--) {
                            mode.removeRow(i);
                        }
                        for (Data d : keresztur) {
                            mode.addRow(new Object[]{d.getCS(), d.getCN(), (int) d.getMin()});
                        }
                        hibaShow.show(Color.green, "Törölve innen: Múrakeresztúr", true);
                    }
                }
            });
            if (where.equalsIgnoreCase("MainTable")) {
                add(add);
                add(copy);
            } else if (where.equalsIgnoreCase("BefTable")) {
                add(delete);
            } else if (where.equalsIgnoreCase("searchField")) {
                add(paste);
            }
        }
    }


    class PopClickListener extends MouseAdapter {

        String where;

        PopClickListener(String name) {
            this.where = name;
        }

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPop(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        void doPop(MouseEvent e) {
            PopUpDemo menu = new PopUpDemo(where, e.getPoint());
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

/*    class DoubleClickListener {

        private int numOfClicks;
        private long clickTime;

        DoubleClickListener() {
            this.numOfClicks = 0;
            this.clickTime = 0;
        }

        public boolean isDoubleClicked() {
            numOfClicks++;
            if (System.currentTimeMillis() - clickTime > 1000) {
                numOfClicks = 0;
            }
            if (numOfClicks >= 1) {
                return true;
            }
            clickTime = System.currentTimeMillis();
            return false;
        }

    }*/
}
