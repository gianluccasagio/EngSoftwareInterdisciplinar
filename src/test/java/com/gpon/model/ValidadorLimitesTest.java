package com.gpon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ValidadorLimitesTest {

    private ValidadorLimites validador;

    @BeforeEach
    public void setup() {
        validador = new ValidadorLimites();
    }

    @Test
    public void testeSemAlertas() {
        ParametrosGPON p = new ParametrosGPON();
        p.setFiberAttenuation(0.35);
        p.setDistance(15.0);
        p.setTxPower(3.0);
        p.setSensitivity(-28.0);
        p.setMargin(5.0);

        List<String> alertas = validador.validar(p);
        assertTrue(alertas.isEmpty(), "Não deve haver alertas para parâmetros padrão.");
    }

    @Test
    public void testeAlertasDistanciaEMargem() {
        ParametrosGPON p = new ParametrosGPON();
        p.setDistance(25.0); // Alerta
        p.setMargin(2.0); // Alerta
        
        List<String> alertas = validador.validar(p);
        assertEquals(2, alertas.size());
        assertTrue(alertas.get(0).contains("excede 20 km"));
        assertTrue(alertas.get(1).contains("inferior a 3 dB"));
    }
}
