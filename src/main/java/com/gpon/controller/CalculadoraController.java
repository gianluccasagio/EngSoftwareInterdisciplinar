package com.gpon.controller;

import com.gpon.model.LinkBudgetCalculadora;
import com.gpon.model.ParametrosGPON;
import com.gpon.model.ValidadorLimites;
import com.gpon.view.InterfaceGrafica;

import java.util.List;

public class CalculadoraController {

    private InterfaceGrafica view;
    private LinkBudgetCalculadora calculadora;
    private ValidadorLimites validador;

    public CalculadoraController(InterfaceGrafica view) {
        this.view = view;
        this.calculadora = new LinkBudgetCalculadora();
        this.validador = new ValidadorLimites();
    }

    public void processarCalculo() {
        try {
            // 1. Obter os dados da interface e popular o Model
            ParametrosGPON params = new ParametrosGPON();
            
            Double txPower = view.getTxPower();
            Double sensitivity = view.getSensitivity();
            Double distance = view.getDistance();
            Double fiberAttenuation = view.getFiberAttenuation();
            Double splitterLoss = view.getSplitterLoss();
            Double connectorLoss = view.getConnectorLoss();
            Double spliceLoss = view.getSpliceLoss();
            Double margin = view.getMargin();

            params.setTxPower(txPower);
            params.setSensitivity(sensitivity);
            params.setDistance(distance);
            params.setFiberAttenuation(fiberAttenuation);
            params.setSplitterLoss(splitterLoss);
            params.setConnectorLoss(connectorLoss);
            params.setSpliceLoss(spliceLoss);
            params.setMargin(margin);

            // 2. Chama a calculadora para realizar o link budget
            ParametrosGPON paramsCalculados = calculadora.calcular(params);

            // 3. Atualizar a View com o novo valor
            view.atualizarResultados(paramsCalculados);

            // 4. Validar as regras de negócio e alertar na View se necessário
            List<String> alertas = validador.validar(paramsCalculados);
            view.mostrarAlertas(alertas);

        } catch (IllegalArgumentException | ArithmeticException e) {
            view.mostrarErro("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            view.mostrarErro("Erro Inesperado", "Ocorreu um erro ao processar o cálculo: " + e.getMessage());
        }
    }
}
