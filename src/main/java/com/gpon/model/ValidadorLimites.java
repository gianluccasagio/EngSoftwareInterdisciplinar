package com.gpon.model;

import java.util.ArrayList;
import java.util.List;

public class ValidadorLimites {

    /**
     * Analisa os parâmetros e retorna uma lista de alertas se os valores estiverem
     * fora dos padrões práticos/comerciais (ex: ITU-T G.984).
     *
     * @param params Objeto contendo os parâmetros calculados.
     * @return Lista de strings contendo alertas de projeto.
     */
    public List<String> validar(ParametrosGPON params) {
        List<String> alertas = new ArrayList<>();

        if (params.getFiberAttenuation() != null) {
            if (params.getFiberAttenuation() < 0.2 || params.getFiberAttenuation() > 0.4) {
                alertas.add("Aviso: Atenuação da fibra fora do padrão comum (0.2 a 0.4 dB/km).");
            }
        }

        if (params.getDistance() != null) {
            if (params.getDistance() > 20.0) {
                alertas.add("Aviso: Distância excede 20 km. Verifique se a classe óptica suporta o enlace físico projetado.");
            }
        }

        if (params.getTxPower() != null) {
            if (params.getTxPower() < 1.0 || params.getTxPower() > 7.0) {
                alertas.add("Aviso: Potência de transmissão (Tx) fora da classe típica B+ ou C+ (+1.5 a +7 dBm).");
            }
        }

        if (params.getSensitivity() != null) {
            if (params.getSensitivity() < -32.0 || params.getSensitivity() > -25.0) {
                alertas.add("Aviso: Sensibilidade fora do padrão comum de equipamentos GPON (-25 a -32 dBm).");
            }
        }

        if (params.getMargin() != null) {
            if (params.getMargin() < 3.0) {
                alertas.add("Aviso: Margem de segurança inferior a 3 dB é considerada de risco para o projeto.");
            }
        }

        return alertas;
    }
}
