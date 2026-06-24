package com.gpon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkBudgetCalculadoraTest {

    private LinkBudgetCalculadora calculadora;

    @BeforeEach
    public void setup() {
        calculadora = new LinkBudgetCalculadora();
    }

    @Test
    public void testeCalculoTxPower() {
        ParametrosGPON p = new ParametrosGPON();
        p.setSensitivity(-28.0);
        p.setDistance(20.0);
        p.setFiberAttenuation(0.25);
        p.setSplitterLoss(18.0); // ex: 1:64
        p.setConnectorLoss(1.0);
        p.setSpliceLoss(0.5);
        p.setMargin(3.0);
        // Tx nulo

        ParametrosGPON resultado = calculadora.calcular(p);
        // Tx = -28 + 5 + 18 + 1 + 0.5 + 3 = -0.5
        assertEquals(-0.5, resultado.getTxPower(), 0.001);
    }

    @Test
    public void testeCalculoDistancia() {
        ParametrosGPON p = new ParametrosGPON();
        p.setTxPower(2.5);
        p.setSensitivity(-28.0);
        p.setFiberAttenuation(0.25);
        p.setSplitterLoss(18.0);
        p.setConnectorLoss(1.0);
        p.setSpliceLoss(0.5);
        p.setMargin(3.0);
        // Distancia nula

        ParametrosGPON resultado = calculadora.calcular(p);
        // dist = (2.5 - (-28) - 18 - 1 - 0.5 - 3) / 0.25 = (30.5 - 22.5) / 0.25 = 8 / 0.25 = 32 km
        assertEquals(32.0, resultado.getDistance(), 0.001);
    }

    @Test
    public void testeFalhaAoNaoInformarDadosSuficientes() {
        ParametrosGPON p = new ParametrosGPON();
        p.setTxPower(2.5);
        // Vários nulos

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadora.calcular(p);
        });
        
        assertTrue(exception.getMessage().contains("exatamente UMA variável"));
    }

    @Test
    public void testeFalhaTodosDadosPreenchidos() {
        ParametrosGPON p = new ParametrosGPON();
        p.setTxPower(2.5);
        p.setSensitivity(-28.0);
        p.setDistance(20.0);
        p.setFiberAttenuation(0.25);
        p.setSplitterLoss(18.0);
        p.setConnectorLoss(1.0);
        p.setSpliceLoss(0.5);
        p.setMargin(3.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadora.calcular(p);
        });
        
        assertTrue(exception.getMessage().contains("exatamente UMA variável"));
    }
}
