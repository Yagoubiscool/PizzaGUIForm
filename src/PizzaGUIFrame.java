import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PizzaGUIFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel crustPanel;
    private JPanel sizePanel;
    private JPanel toppingPanel;
    private JPanel textPanel;
    private JPanel buttonPanel;

    private JRadioButton thinButton;
    private JRadioButton regularButton;
    private JRadioButton deepDishButton;
    private ButtonGroup crustGroup;

    private JComboBox<String> sizeComboBox;

    private JCheckBox pepperoniBox;
    private JCheckBox mushroomBox;
    private JCheckBox onionBox;
    private JCheckBox sausageBox;
    private JCheckBox blackOliveBox;
    private JCheckBox jalapenoBox;

    private JTextArea receiptTextArea;
    private JScrollPane scrollPane;

    private JButton orderButton;
    private JButton clearButton;
    private JButton quitButton;

    private DecimalFormat df = new DecimalFormat("$#,##0.00");

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        createCrustPanel();
        createSizePanel();
        createToppingPanel();

        topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);

        JPanel middleTopPanel = new JPanel(new BorderLayout(10, 10));
        middleTopPanel.add(topPanel, BorderLayout.NORTH);
        middleTopPanel.add(toppingPanel, BorderLayout.CENTER);

        createTextPanel();
        createButtonPanel();

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(textPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(middleTopPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void createCrustPanel() {
        crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Type of Crust"));
        thinButton = new JRadioButton("Thin");
        regularButton = new JRadioButton("Regular");
        deepDishButton = new JRadioButton("Deep-dish");

        crustGroup = new ButtonGroup();
        crustGroup.add(thinButton);
        crustGroup.add(regularButton);
        crustGroup.add(deepDishButton);

        crustPanel.setLayout(new BoxLayout(crustPanel, BoxLayout.Y_AXIS));
        crustPanel.add(thinButton);
        crustPanel.add(regularButton);
        crustPanel.add(deepDishButton);
    }

    private void createSizePanel() {
        sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Pizza Size"));
        String[] sizes = {"Small ($8.00)", "Medium ($12.00)", "Large ($16.00)", "Super ($20.00)"};
        sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.setSelectedIndex(-1);
        sizePanel.add(sizeComboBox);
    }

    private void createToppingPanel() {
        toppingPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        toppingPanel.setBorder(BorderFactory.createTitledBorder("Toppings ($1.00 each)"));

        pepperoniBox = new JCheckBox("Pepperoni");
        mushroomBox = new JCheckBox("Mushrooms");
        onionBox = new JCheckBox("Onions");
        sausageBox = new JCheckBox("Sausage");
        blackOliveBox = new JCheckBox("Black Olives");
        jalapenoBox = new JCheckBox("Jalapeños");

        toppingPanel.add(pepperoniBox);
        toppingPanel.add(mushroomBox);
        toppingPanel.add(onionBox);
        toppingPanel.add(sausageBox);
        toppingPanel.add(blackOliveBox);
        toppingPanel.add(jalapenoBox);
    }

    private void createTextPanel() {
        textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createTitledBorder("Order Receipt"));
        receiptTextArea = new JTextArea(10, 40);
        receiptTextArea.setEditable(false);
        receiptTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane = new JScrollPane(receiptTextArea);
        textPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        orderButton.addActionListener(e -> processOrder());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    PizzaGUIFrame.this,
                    "Are you sure you want to quit?",
                    "Quit Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void processOrder() {
        String crust = "";
        if (thinButton.isSelected()) crust = "Thin";
        else if (regularButton.isSelected()) crust = "Regular";
        else if (deepDishButton.isSelected()) crust = "Deep-dish";

        if (crust.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a type of crust.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int sizeIndex = sizeComboBox.getSelectedIndex();
        if (sizeIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a pizza size.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean hasTopping = pepperoniBox.isSelected() || mushroomBox.isSelected() ||
                onionBox.isSelected() || sausageBox.isSelected() ||
                blackOliveBox.isSelected() || jalapenoBox.isSelected();

        if (!hasTopping) {
            JOptionPane.showMessageDialog(this, "Please select at least one ingredient.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double basePrice = 0.0;
        String sizeName = "";
        switch (sizeIndex) {
            case 0:
                basePrice = 8.00;
                sizeName = "Small";
                break;
            case 1:
                basePrice = 12.00;
                sizeName = "Medium";
                break;
            case 2:
                basePrice = 16.00;
                sizeName = "Large";
                break;
            case 3:
                basePrice = 20.00;
                sizeName = "Super";
                break;
        }

        double toppingsTotal = 0.0;
        StringBuilder toppingsList = new StringBuilder();

        JCheckBox[] checkboxes = {pepperoniBox, mushroomBox, onionBox, sausageBox, blackOliveBox, jalapenoBox};
        String[] toppingNames = {"Pepperoni", "Mushrooms", "Onions", "Sausage", "Black Olives", "Jalapeños"};

        for (int i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].isSelected()) {
                toppingsList.append(String.format("%-25s %s\n", toppingNames[i], df.format(1.00)));
                toppingsTotal += 1.00;
            }
        }

        double subTotal = basePrice + toppingsTotal;
        double tax = subTotal * 0.07;
        double total = subTotal + tax;

        StringBuilder receipt = new StringBuilder();
        receipt.append("=========================================\n");
        receipt.append(String.format("%-25s %s\n", crust + " Crust (" + sizeName + ")", df.format(basePrice)));
        receipt.append(toppingsList.toString());
        receipt.append(String.format("%-25s %s\n", "Sub-total:", df.format(subTotal)));
        receipt.append(String.format("%-25s %s\n", "Tax:", df.format(tax)));
        receipt.append("---------------------------------------------------------------------\n");
        receipt.append(String.format("%-25s %s\n", "Total:", df.format(total)));
        receipt.append("=========================================\n");

        receiptTextArea.setText(receipt.toString());
    }

    private void clearForm() {
        crustGroup.clearSelection();
        sizeComboBox.setSelectedIndex(-1);
        pepperoniBox.setSelected(false);
        mushroomBox.setSelected(false);
        onionBox.setSelected(false);
        sausageBox.setSelected(false);
        blackOliveBox.setSelected(false);
        jalapenoBox.setSelected(false);
        receiptTextArea.setText("");
    }
}