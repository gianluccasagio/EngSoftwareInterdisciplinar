package com.gpon.view;

import com.gpon.controller.CalculadoraController;
import com.gpon.model.ParametrosGPON;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfaceGrafica extends JFrame {

    private JTextField txtTxPower;
    private JTextField txtSensitivity;
    private JTextField txtDistance;
    private JTextField txtFiberAttenuation;
    private JTextField txtSplitterLoss;
    private JTextField txtConnectorLoss;
    private JTextField txtSpliceLoss;
    private JTextField txtMargin;
    
    private JTextArea txtAlertas;
    private CalculadoraController controller;

    public InterfaceGrafica() {
        super("Calculadora de Link Budget GPON");
        this.controller = new CalculadoraController(this);
        
        configurarInterface();
    }

    private void configurarInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLayout(new BorderLayout(10, 10));
        
        JPanel painelFormulario = new JPanel(new GridLayout(8, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtTxPower = adicionarCampo(painelFormulario, "Potência Tx (dBm):");
        txtSensitivity = adicionarCampo(painelFormulario, "Sensibilidade Rx (dBm):");
        txtDistance = adicionarCampo(painelFormulario, "Distância (km):");
        txtFiberAttenuation = adicionarCampo(painelFormulario, "Atenuação Fibra (dB/km):");
        txtSplitterLoss = adicionarCampo(painelFormulario, "Perda Splitters (dB):");
        txtConnectorLoss = adicionarCampo(painelFormulario, "Perda Conectores (dB):");
        txtSpliceLoss = adicionarCampo(painelFormulario, "Perda Fusões (dB):");
        txtMargin = adicionarCampo(painelFormulario, "Margem Segurança (dB):");

        add(painelFormulario, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();
        JButton btnCalcular = new JButton("Calcular Variável Faltante");
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.processarCalculo();
            }
        });
        
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());

        painelBotoes.add(btnCalcular);
        painelBotoes.add(btnLimpar);
        add(painelBotoes, BorderLayout.CENTER);

        JPanel painelAlertas = new JPanel(new BorderLayout());
        painelAlertas.setBorder(BorderFactory.createTitledBorder("Alertas de Projeto"));
        txtAlertas = new JTextArea(8, 30);
        txtAlertas.setEditable(false);
        painelAlertas.add(new JScrollPane(txtAlertas), BorderLayout.CENTER);
        add(painelAlertas, BorderLayout.SOUTH);
    }

    private JTextField adicionarCampo(JPanel painel, String label) {
        painel.add(new JLabel(label));
        JTextField textField = new JTextField();
        painel.add(textField);
        return textField;
    }

    private Double parseDoubleOrNull(JTextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(text.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O valor '" + text + "' não é um número válido.");
        }
    }

    public Double getTxPower() { return parseDoubleOrNull(txtTxPower); }
    public Double getSensitivity() { return parseDoubleOrNull(txtSensitivity); }
    public Double getDistance() { return parseDoubleOrNull(txtDistance); }
    public Double getFiberAttenuation() { return parseDoubleOrNull(txtFiberAttenuation); }
    public Double getSplitterLoss() { return parseDoubleOrNull(txtSplitterLoss); }
    public Double getConnectorLoss() { return parseDoubleOrNull(txtConnectorLoss); }
    public Double getSpliceLoss() { return parseDoubleOrNull(txtSpliceLoss); }
    public Double getMargin() { return parseDoubleOrNull(txtMargin); }

    public void atualizarResultados(ParametrosGPON params) {
        txtTxPower.setText(params.getTxPower() != null ? String.format("%.2f", params.getTxPower()) : "");
        txtSensitivity.setText(params.getSensitivity() != null ? String.format("%.2f", params.getSensitivity()) : "");
        txtDistance.setText(params.getDistance() != null ? String.format("%.2f", params.getDistance()) : "");
        txtFiberAttenuation.setText(params.getFiberAttenuation() != null ? String.format("%.2f", params.getFiberAttenuation()) : "");
        txtSplitterLoss.setText(params.getSplitterLoss() != null ? String.format("%.2f", params.getSplitterLoss()) : "");
        txtConnectorLoss.setText(params.getConnectorLoss() != null ? String.format("%.2f", params.getConnectorLoss()) : "");
        txtSpliceLoss.setText(params.getSpliceLoss() != null ? String.format("%.2f", params.getSpliceLoss()) : "");
        txtMargin.setText(params.getMargin() != null ? String.format("%.2f", params.getMargin()) : "");
        txtAlertas.setText("Cálculo realizado com sucesso!\n");
    }

    public void mostrarAlertas(List<String> alertas) {
        if (alertas == null || alertas.isEmpty()) {
            txtAlertas.append("Projeto dentro dos padrões.\n");
            return;
        }
        for (String alerta : alertas) {
            txtAlertas.append(alerta + "\n");
        }
    }

    public void mostrarErro(String titulo, String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public void limparCampos() {
        txtTxPower.setText("");
        txtSensitivity.setText("");
        txtDistance.setText("");
        txtFiberAttenuation.setText("");
        txtSplitterLoss.setText("");
        txtConnectorLoss.setText("");
        txtSpliceLoss.setText("");
        txtMargin.setText("");
        txtAlertas.setText("");
    }

    public static void main(String[] args) {
        // Altera o look and feel para o do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new InterfaceGrafica().setVisible(true);
        });
    }
}
