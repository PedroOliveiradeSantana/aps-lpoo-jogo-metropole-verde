package controller;

import model.City;
import model.Policy;
import view.GameWindow;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameEngine implements ActionListener {

    private final City model;
    private GameWindow view;
    private final Map<String, Policy> availablePolicies;
    private static final double TAX_INCREASE_REVENUE = 120_000.00;
    private static final double POLLUTING_COMPANY_REVENUE = 200_000.00;
    private static final double EFFICIENCY_BONUS_REVENUE = 50_000.00;
    private final Random randomGenerator;
    private boolean gameEnded = false;

    public GameEngine(City model) {
        this.model = model;
        this.availablePolicies = new HashMap<>();
        this.randomGenerator = new Random();
        initializePolicies();
    }

    private void initializePolicies() {
        availablePolicies.put("BUILD_PARK", new Policy("Construir Novo Parque", 100000, 7, 0, 10));
        availablePolicies.put("INVEST_RECYCLING", new Policy("Investir em Reciclagem", 150000, 0, 10, 15));
        availablePolicies.put("RAISE_TAXES", new Policy("Aumentar Impostos", 0, 0, 0, -10));
        availablePolicies.put("AWARENESS_CAMPAIGN", new Policy("Campanha de Conscientização", 50000, 0, 3, 2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameEnded || view == null || !view.isDisplayable()) {
           return;
        }

        String command = e.getActionCommand();
        if ("NEXT_TURN".equals(command)) {
            handleNextTurn();
        } else {
            Policy selectedPolicy = availablePolicies.get(command);
            if (selectedPolicy != null) {
                handlePolicySelection(selectedPolicy, command);
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            if (!gameEnded) {
                 updateAndCheckGameState();
            }
        });
    }

    private void handlePolicySelection(Policy policy, String command) {
        if ("RAISE_TAXES".equals(command)) {
            int choice = JOptionPane.showConfirmDialog(view,
                    "Aumentar impostos gera R$ " + String.format("%,.2f", TAX_INCREASE_REVENUE) + " mas reduz a Felicidade. Confirmar?",
                    "Confirmar Aumento de Impostos", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                model.addRevenue(TAX_INCREASE_REVENUE);
                model.applyHappinessEffect(policy.getHappinessEffect());
                String logMsg = String.format("Turno %d: Impostos aumentados. Orçamento +R$%,.2f, Felicidade %+d.",
                        model.getCurrentTurn(), TAX_INCREASE_REVENUE, policy.getHappinessEffect());
                view.logMessage(logMsg);
                handleNextTurn(); // <-- LINHA ADICIONADA
            }
        } else {
            if (model.getBudget() >= policy.getCost()) {
                int choice = JOptionPane.showConfirmDialog(view,
                        "Confirmar a implementação de '" + policy.getName() + "' por R$ " + String.format("%,.2f", policy.getCost()) + "?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    model.applyPolicyEffects(policy);
                    String logMsg = String.format("Turno %d: Política '%s' implementada. Orçamento -R$%,.2f, Ar %+d, Resíduos %+d, Felicidade %+d.",
                            model.getCurrentTurn(), policy.getName(), policy.getCost(),
                            policy.getAirQualityEffect(), policy.getWasteManagementEffect(), policy.getHappinessEffect());
                    view.logMessage(logMsg);
                    handleNextTurn(); // <-- LINHA ADICIONADA
                }
            } else {
                JOptionPane.showMessageDialog(view, "Orçamento insuficiente para implementar esta política!", "Erro", JOptionPane.ERROR_MESSAGE);
                if (view != null && view.isDisplayable()) view.updateView(model);
            }
        }
    }

    private void handleNextTurn() {
        boolean wasBankrupt = model.getBudget() < 0;

        if(model.getCurrentTurn() < City.MAX_TURNS) {
            model.nextTurn();
            if (view != null && view.isDisplayable()) {
                 view.logMessage("--- Avançando para o Turno " + model.getCurrentTurn() + " ---");
                 handleRandomEvent();
             }
        } else {
             if (view != null && view.isDisplayable()) {
                 view.logMessage("--- Mandato encerrado (Turno " + model.getCurrentTurn() + ") ---");
              }
        }

        if (wasBankrupt && model.getBudget() < 0) {
            endGame("Game Over! A cidade faliu (orçamento negativo por dois turnos consecutivos).");
        }
        
    }

    private void handleRandomEvent() {
    	
        int chance = randomGenerator.nextInt(100);

        if (chance < 15) {
            int happinessPenalty = -8;
            model.applyHappinessEffect(happinessPenalty);
            String eventMessage = "Evento Negativo: Greve surpresa dos transportes! Felicidade " + happinessPenalty + ".";
            if (view != null && view.isDisplayable()) {
                view.logMessage(eventMessage);
                JOptionPane.showMessageDialog(view, eventMessage, "Evento Aleatório", JOptionPane.WARNING_MESSAGE);
            }
        } else if (chance < 25) {
             model.addRevenue(EFFICIENCY_BONUS_REVENUE);
             String eventMessage = "Evento Positivo: Bônus por Eficiência Energética! Orçamento +" + String.format("%,.2f", EFFICIENCY_BONUS_REVENUE) + ".";
             if (view != null && view.isDisplayable()) {
                 view.logMessage(eventMessage);
                 JOptionPane.showMessageDialog(view, eventMessage, "Evento Aleatório", JOptionPane.INFORMATION_MESSAGE);
              }
        } else if (chance < 30) {
             handleDilemmaEvent();
        }
    }

    private void handleDilemmaEvent() {
         String message = "Evento de Dilema: Uma grande empresa quer se instalar na cidade!\n"
                        + "Oferecem R$" + String.format("%,.2f", POLLUTING_COMPANY_REVENUE) + " em investimentos imediatos.\n"
                        + "Contudo, suas operações reduzirão drasticamente a Qualidade do Ar (-20).\n"
                        + "Você aceita a proposta?";
         
         int choice = -1;
         if (view != null && view.isDisplayable()){
            choice = JOptionPane.showConfirmDialog(view, message, "Decisão Importante", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
         } else {
             return;
         }

         if (choice == JOptionPane.YES_OPTION) {
             model.addRevenue(POLLUTING_COMPANY_REVENUE);
             model.applyAirQualityEffect(-20);
             String logMsg = String.format("Turno %d: Empresa poluente aceita! Orçamento +R$%,.2f, Qualidade do Ar -20.",
            		 model.getCurrentTurn(), POLLUTING_COMPANY_REVENUE);
             if (view != null) view.logMessage(logMsg);
         } else if (choice == JOptionPane.NO_OPTION) {
             String logMsg = String.format("Turno %d: Proposta da empresa poluente recusada.", model.getCurrentTurn());
             if (view != null) view.logMessage(logMsg);
         }        
    }

    private void updateAndCheckGameState() {
        if (!gameEnded && view != null && view.isDisplayable()) {
            view.updateView(model);
            checkSimpleEndGameConditions();
        }
    }

    private void checkSimpleEndGameConditions() {
        if (gameEnded || view == null || !view.isDisplayable()) {
           return;
        }

        boolean shouldEnd = false;
        String endMessage = "";

        if (model.getAirQuality() <= 0 || model.getWasteManagement() <= 0 || model.getHappiness() <= 0) {
           shouldEnd = true;
           endMessage = "Game Over! A cidade entrou em colapso (um indicador chegou a zero).";
        }
       
        	
        else if (model.getCurrentTurn() >= City.MAX_TURNS) {
        	                
        	if(!"NEXT_TURN".equals(currentCommand)) {
                if (model.getAirQuality() >= City.AIR_QUALITY_GOAL &&
                    model.getWasteManagement() >= City.WASTE_MANAGEMENT_GOAL &&
                    model.getHappiness() >= City.HAPPINESS_GOAL &&
                    model.getBudget() >= 0) {
                    shouldEnd = true;
                    endMessage = "Parabéns! Você transformou a cidade em uma Metrópole Verde e venceu o jogo!";
                }
                
                else {
                    shouldEnd = true;
                    if(model.getBudget() < 0) {
                         endMessage = "Fim de jogo. Você chegou ao final do mandato, mas com o orçamento negativo.";
                    } else {
                         endMessage = "Fim de jogo. Você chegou ao final do mandato, mas não atingiu todas as metas de sustentabilidade.";
                    }
                }
            }

        }

        if (shouldEnd) {
            endGame(endMessage);
        }
    }
    
    private String currentCommand = "";

    private void endGame(String message) {
        if (!gameEnded && view != null && view.isDisplayable()) {
            gameEnded = true;
            SwingUtilities.invokeLater(() -> {
                  if (view != null) {
                     JOptionPane.showMessageDialog(view, message, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                     view.dispose();
                     view = null;
                  }
             });
        }
    }

    public void startGame() {
        if (view != null) {
            gameEnded = false;
            view.setVisible(true);
            view.logMessage("Início do seu mandato. Boa sorte, Prefeito(a)!");
            view.updateView(model);
        }
    }

    public void setView(GameWindow view) {
        this.view = view;
    }
}