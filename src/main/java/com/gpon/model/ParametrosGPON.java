package com.gpon.model;

import java.util.ArrayList;
import java.util.List;

public class ParametrosGPON {
    
    // Usamos Double (wrapper) para permitir valores nulos (campos não preenchidos pelo usuário)
    private Double txPower; // dBm
    private Double sensitivity; // dBm
    private Double distance; // km
    private Double fiberAttenuation; // dB/km
    private Double splitterLoss; // dB
    private Double connectorLoss; // dB
    private Double spliceLoss; // dB
    private Double margin; // dB

    public ParametrosGPON() {}

    public Double getTxPower() { return txPower; }
    public void setTxPower(Double txPower) { this.txPower = txPower; }

    public Double getSensitivity() { return sensitivity; }
    public void setSensitivity(Double sensitivity) { this.sensitivity = sensitivity; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public Double getFiberAttenuation() { return fiberAttenuation; }
    public void setFiberAttenuation(Double fiberAttenuation) { this.fiberAttenuation = fiberAttenuation; }

    public Double getSplitterLoss() { return splitterLoss; }
    public void setSplitterLoss(Double splitterLoss) { this.splitterLoss = splitterLoss; }

    public Double getConnectorLoss() { return connectorLoss; }
    public void setConnectorLoss(Double connectorLoss) { this.connectorLoss = connectorLoss; }

    public Double getSpliceLoss() { return spliceLoss; }
    public void setSpliceLoss(Double spliceLoss) { this.spliceLoss = spliceLoss; }

    public Double getMargin() { return margin; }
    public void setMargin(Double margin) { this.margin = margin; }

    /**
     * Retorna o número de parâmetros que não foram preenchidos (estão nulos).
     */
    public int getNullCount() {
        int count = 0;
        if (txPower == null) count++;
        if (sensitivity == null) count++;
        if (distance == null) count++;
        if (fiberAttenuation == null) count++;
        if (splitterLoss == null) count++;
        if (connectorLoss == null) count++;
        if (spliceLoss == null) count++;
        if (margin == null) count++;
        return count;
    }

    /**
     * Retorna o nome da variável que está nula (para fins de isolamento matemático).
     * Retorna nulo se houver mais de uma ou nenhuma variável nula.
     */
    public String getVariavelFaltante() {
        if (getNullCount() != 1) {
            return null;
        }
        if (txPower == null) return "txPower";
        if (sensitivity == null) return "sensitivity";
        if (distance == null) return "distance";
        if (fiberAttenuation == null) return "fiberAttenuation";
        if (splitterLoss == null) return "splitterLoss";
        if (connectorLoss == null) return "connectorLoss";
        if (spliceLoss == null) return "spliceLoss";
        if (margin == null) return "margin";
        return null;
    }
}
