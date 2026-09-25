package BaristaDailyPayCalculator.javaGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BaristaPayGUI extends JFrame {

    // UI Components
    private JTextField hoursField;
    private JLabel regularPayLabel;
    private JLabel otPayLabel;
    private JLabel grossPayLabel;
    private JLabel taxLabel;
    private JLabel finalPayLabel;

    // Backend Constants (Galing sa logic mo!)
    private static final double RATE = 80.0;
    private static final double TAX_RATE = 0.05;
    private static final double OT_MULTIPLIER = 1.5;

    public BaristaPayGUI() {
        // Frame Setup
        setTitle("Barista Daily Pay Calculator");
        setSize(450, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));

        // Color Palette (Espresso / Dark Coffee Theme)
        Color darkBg = new Color(30, 27, 24);
        Color cardBg = new Color(44, 40, 36);
        Color accentColor = new Color(217, 119, 6);
        Color textColor = new Color(245, 245, 240);
        Color subTextColor = new Color(160, 155, 150);

        getContentPane().setBackground(darkBg);

        // 1. HEADER PANEL
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(darkBg);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JLabel titleLabel = new JLabel("☕ BARISTA PAY CALCULATOR");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Daily Shift Earnings & Overtime Breakdown");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(subTextColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);

        // 2. INPUT & CALCULATION PANEL
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(cardBg);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(new Color(60, 55, 50), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input Label & Field
        JLabel inputLabel = new JLabel("Hours Worked:");
        inputLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        inputLabel.setForeground(textColor);

        hoursField = new JTextField(10);
        hoursField.setFont(new Font("SansSerif", Font.BOLD, 14));
        hoursField.setBackground(new Color(20, 18, 16));
        hoursField.setForeground(textColor);
        hoursField.setCaretColor(textColor);
        hoursField.setHorizontalAlignment(JTextField.CENTER);

        JButton calcButton = new JButton("Compute Pay");
        calcButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        calcButton.setBackground(accentColor);
        calcButton.setForeground(Color.WHITE);
        calcButton.setFocusPainted(false);

        // Labels for Breakdown Output
        regularPayLabel = createValueLabel("₱0.00", textColor);
        otPayLabel = createValueLabel("₱0.00", textColor);
        grossPayLabel = createValueLabel("₱0.00", textColor);
        taxLabel = createValueLabel("₱0.00", new Color(239, 68, 68)); // Red for tax
        finalPayLabel = createValueLabel("₱0.00", accentColor);
        finalPayLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Add to Panel Layout
        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(inputLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; centerPanel.add(hoursField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        centerPanel.add(calcButton, gbc);

        gbc.gridwidth = 1;
        addResultRow(centerPanel, gbc, 2, "Regular Pay (Max 8h):", regularPayLabel, subTextColor);
        addResultRow(centerPanel, gbc, 3, "Overtime Pay (1.5x):", otPayLabel, subTextColor);
        addResultRow(centerPanel, gbc, 4, "Gross Earnings:", grossPayLabel, subTextColor);
        addResultRow(centerPanel, gbc, 5, "Tax Deduction (5% > ₱1k):", taxLabel, subTextColor);
        addResultRow(centerPanel, gbc, 6, "Net Take-Home Pay:", finalPayLabel, subTextColor);

        // 3. FOOTER / WATERMARK PANEL (Explicit Credits)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(darkBg);
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));

        JLabel watermark1 = new JLabel("🧠 Backend Logic & Mathematical Modeling: Chocoacocoa");
        watermark1.setFont(new Font("SansSerif", Font.BOLD, 11));
        watermark1.setForeground(new Color(59, 130, 246)); // Blue
        watermark1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel watermark2 = new JLabel("🎨 Swing GUI Layout & Visual Design: Gemini AI");
        watermark2.setFont(new Font("SansSerif", Font.ITALIC, 11));
        watermark2.setForeground(subTextColor);
        watermark2.setAlignmentX(Component.CENTER_ALIGNMENT);

        footerPanel.add(watermark1);
        footerPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        footerPanel.add(watermark2);

        // Add Panels to Window
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Button Action Event (Connects GUI to your backend calculation)
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePayroll();
            }
        });

        // Enter Key Action inside input field
        hoursField.addActionListener(e -> calculatePayroll());
    }

    // Helper method to add result rows
    private void addResultRow(JPanel panel, GridBagConstraints gbc, int row, String text, JLabel valueLabel, Color labelColor) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(labelColor);
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row;
        panel.add(valueLabel, gbc);
    }

    private JLabel createValueLabel(String initialText, Color color) {
        JLabel label = new JLabel(initialText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    // === YOUR BACKEND LOGIC INTEGRATION ===
    private void calculatePayroll() {
        try {
            int hour = Integer.parseInt(hoursField.getText().trim());

            if (hour < 0) {
                JOptionPane.showMessageDialog(this, "Hours worked cannot be negative!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double regularPay = 0;
            double otPay = 0;
            double grossPay = 0;
            double tax = 0;
            double finalPay = 0;

            // Eksaktong formula at condition na binuo mo:
            if (hour > 8) {
                regularPay = 8 * RATE;
                otPay = (hour - 8) * (RATE * OT_MULTIPLIER);
                grossPay = regularPay + otPay; // (8*80) + ((hour-8)*(80*1.5))
            } else {
                regularPay = hour * RATE;
                grossPay = regularPay;
            }

            finalPay = grossPay;

            // Tax calculation
            if (grossPay > 1000) {
                tax = grossPay * TAX_RATE;
                finalPay = grossPay - tax;
            }

            // Update GUI Displays
            regularPayLabel.setText(String.format("₱%.2f", regularPay));
            otPayLabel.setText(String.format("₱%.2f", otPay));
            grossPayLabel.setText(String.format("₱%.2f", grossPay));
            taxLabel.setText(String.format("-₱%.2f", tax));
            finalPayLabel.setText(String.format("₱%.2f", finalPay));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for hours!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Set Look and Feel to System Default for native rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new BaristaPayGUI().setVisible(true);
        });
    }
}