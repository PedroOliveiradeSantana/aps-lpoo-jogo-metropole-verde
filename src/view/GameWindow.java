package view;

import controller.GameEngine;
import model.City;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private JProgressBar airQualityBar, wasteManagementBar, happinessBar;
    private JLabel budgetValueLabel, turnValueLabel;

    private JButton buildParkButton, investRecyclingButton, raiseTaxesButton, awarenessCampaignButton, nextTurnButton;

    private JTextArea logTextArea;

    public GameWindow(GameEngine controller) {
        setTitle("Metrópole Verde");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        add(createStatusPanel(), BorderLayout.NORTH);
        add(createActionsPanel(controller), BorderLayout.CENTER);
        add(createLogPanel(), BorderLayout.SOUTH);
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new GridBagLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);

        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font valueFont = new Font("Arial", Font.PLAIN, 12);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.EAST;
        JLabel airQualityTitleLabel = new JLabel("Qual. Ar (Meta: " + City.AIR_QUALITY_GOAL + "):");
        airQualityTitleLabel.setFont(labelFont);
        statusPanel.add(airQualityTitleLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.35; gbc.anchor = GridBagConstraints.WEST;
        airQualityBar = new JProgressBar(0, 100);
        airQualityBar.setStringPainted(true);
        airQualityBar.setFont(valueFont);
        statusPanel.add(airQualityBar, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.EAST;
        JLabel wasteManagementTitleLabel = new JLabel("Resíduos (Meta: " + City.WASTE_MANAGEMENT_GOAL + "):");
        wasteManagementTitleLabel.setFont(labelFont);
        statusPanel.add(wasteManagementTitleLabel, gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.35; gbc.anchor = GridBagConstraints.WEST;
        wasteManagementBar = new JProgressBar(0, 100);
        wasteManagementBar.setStringPainted(true);
        wasteManagementBar.setFont(valueFont);
        statusPanel.add(wasteManagementBar, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.EAST;
        JLabel happinessTitleLabel = new JLabel("Felicidade (Meta: " + City.HAPPINESS_GOAL + "):");
        happinessTitleLabel.setFont(labelFont);
        statusPanel.add(happinessTitleLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.35; gbc.anchor = GridBagConstraints.WEST;
        happinessBar = new JProgressBar(0, 100);
        happinessBar.setStringPainted(true);
        happinessBar.setFont(valueFont);
        statusPanel.add(happinessBar, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.EAST;
        JLabel budgetTitleLabel = new JLabel("Orçamento:");
        budgetTitleLabel.setFont(labelFont);
        statusPanel.add(budgetTitleLabel, gbc);

        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.35; gbc.anchor = GridBagConstraints.WEST;
        budgetValueLabel = new JLabel("R$ 0,00");
        budgetValueLabel.setFont(valueFont);
        statusPanel.add(budgetValueLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.EAST;
        JLabel turnTitleLabel = new JLabel("Turno:");
        turnTitleLabel.setFont(labelFont);
        statusPanel.add(turnTitleLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.35; gbc.anchor = GridBagConstraints.WEST;
        turnValueLabel = new JLabel("0 / " + City.MAX_TURNS);
        turnValueLabel.setFont(valueFont);
        statusPanel.add(turnValueLabel, gbc);

        gbc.gridx = 2; gbc.gridy = 2; statusPanel.add(new JLabel(""), gbc);
        gbc.gridx = 3; gbc.gridy = 2; statusPanel.add(new JLabel(""), gbc);

        return statusPanel;
    }

    private JPanel createActionsPanel(GameEngine controller) {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buildParkButton = new JButton("Construir Parque (-$100k / +10 Fel)");
        buildParkButton.setToolTipText("Custo: $100.000, Ar +7, Felicidade +10");
        buildParkButton.setActionCommand("BUILD_PARK");
        buildParkButton.addActionListener(controller);

        investRecyclingButton = new JButton("Investir Reciclagem (-$150k / +15 Fel)");
        investRecyclingButton.setToolTipText("Custo: $150.000, Resíduos +10, Felicidade +15");
        investRecyclingButton.setActionCommand("INVEST_RECYCLING");
        investRecyclingButton.addActionListener(controller);
        
        awarenessCampaignButton = new JButton("Campanha Conscientização (-$50k / +2 Fel)");
        awarenessCampaignButton.setToolTipText("Custo: $50.000, Resíduos +3, Felicidade +2");
        awarenessCampaignButton.setActionCommand("AWARENESS_CAMPAIGN");
        awarenessCampaignButton.addActionListener(controller);
        
        raiseTaxesButton = new JButton("Aumentar Impostos (+R$120k / -10 Fel)");
        raiseTaxesButton.setToolTipText("Ganha: $120.000, Felicidade -10");
        raiseTaxesButton.setActionCommand("RAISE_TAXES");
        raiseTaxesButton.addActionListener(controller);
        
        nextTurnButton = new JButton("Avançar Turno");
        nextTurnButton.setToolTipText("Passa para o próximo trimestre e coleta impostos.");
        nextTurnButton.setActionCommand("NEXT_TURN");
        nextTurnButton.addActionListener(controller);
        
        actionsPanel.add(buildParkButton);
        actionsPanel.add(investRecyclingButton);
        actionsPanel.add(awarenessCampaignButton);
        actionsPanel.add(raiseTaxesButton);
        actionsPanel.add(nextTurnButton);
        return actionsPanel;
    }

    private JScrollPane createLogPanel() {
        logTextArea = new JTextArea(10, 70);
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Eventos"));
        return scrollPane;
    }

    public void updateView(City city) {
        SwingUtilities.invokeLater(() -> {
            airQualityBar.setValue(city.getAirQuality());
            airQualityBar.setString(city.getAirQuality() + "/100");
            
            wasteManagementBar.setValue(city.getWasteManagement());
            wasteManagementBar.setString(city.getWasteManagement() + "/100");
            
            happinessBar.setValue(city.getHappiness());
            happinessBar.setString(city.getHappiness() + "/100");
            
            budgetValueLabel.setText(String.format("R$ %,.2f", city.getBudget()));
            turnValueLabel.setText(city.getCurrentTurn() + " / " + City.MAX_TURNS);
            if (city.getBudget() < 0) {
                 budgetValueLabel.setForeground(Color.RED);
            } else {
                 budgetValueLabel.setForeground(Color.BLACK);
            }
        });
    }

    public void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logTextArea.append(message + "\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        });
    }
}