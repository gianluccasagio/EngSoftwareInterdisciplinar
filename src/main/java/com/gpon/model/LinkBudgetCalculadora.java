package com.gpon.model;

public class LinkBudgetCalculadora {

    /**
     * Calcula a variável faltante com base na equação de Link Budget GPON.
     * Equação base: Tx - Sensibilidade - (Distância * Atenuação) - Splitters - Conectores - Fusões - Margem = 0
     * 
     * @param params Objeto contendo os parâmetros. Exatamente um deve ser nulo.
     * @return O mesmo objeto com o valor faltante preenchido.
     * @throws IllegalArgumentException se não houver exatamente 1 variável faltando.
     * @throws ArithmeticException se houver divisão por zero (ex: distância ou atenuação zero).
     */
    public ParametrosGPON calcular(ParametrosGPON params) throws IllegalArgumentException {
        String faltante = params.getVariavelFaltante();
        
        if (faltante == null) {
            throw new IllegalArgumentException("Para o cálculo, é necessário deixar exatamente UMA variável em branco.");
        }

        switch (faltante) {
            case "txPower":
                params.setTxPower(params.getSensitivity() + 
                                  (params.getDistance() * params.getFiberAttenuation()) + 
                                  params.getSplitterLoss() + 
                                  params.getConnectorLoss() + 
                                  params.getSpliceLoss() + 
                                  params.getMargin());
                break;
                
            case "sensitivity":
                params.setSensitivity(params.getTxPower() - 
                                      (params.getDistance() * params.getFiberAttenuation()) - 
                                      params.getSplitterLoss() - 
                                      params.getConnectorLoss() - 
                                      params.getSpliceLoss() - 
                                      params.getMargin());
                break;
                
            case "distance":
                if (params.getFiberAttenuation() == 0) {
                    throw new ArithmeticException("Atenuação da fibra não pode ser zero ao calcular a distância.");
                }
                params.setDistance((params.getTxPower() - params.getSensitivity() - 
                                    params.getSplitterLoss() - params.getConnectorLoss() - 
                                    params.getSpliceLoss() - params.getMargin()) / params.getFiberAttenuation());
                break;
                
            case "fiberAttenuation":
                if (params.getDistance() == 0) {
                    throw new ArithmeticException("Distância não pode ser zero ao calcular a atenuação da fibra.");
                }
                params.setFiberAttenuation((params.getTxPower() - params.getSensitivity() - 
                                            params.getSplitterLoss() - params.getConnectorLoss() - 
                                            params.getSpliceLoss() - params.getMargin()) / params.getDistance());
                break;
                
            case "splitterLoss":
                params.setSplitterLoss(params.getTxPower() - params.getSensitivity() - 
                                       (params.getDistance() * params.getFiberAttenuation()) - 
                                       params.getConnectorLoss() - 
                                       params.getSpliceLoss() - 
                                       params.getMargin());
                break;
                
            case "connectorLoss":
                params.setConnectorLoss(params.getTxPower() - params.getSensitivity() - 
                                        (params.getDistance() * params.getFiberAttenuation()) - 
                                        params.getSplitterLoss() - 
                                        params.getSpliceLoss() - 
                                        params.getMargin());
                break;
                
            case "spliceLoss":
                params.setSpliceLoss(params.getTxPower() - params.getSensitivity() - 
                                     (params.getDistance() * params.getFiberAttenuation()) - 
                                     params.getSplitterLoss() - 
                                     params.getConnectorLoss() - 
                                     params.getMargin());
                break;
                
            case "margin":
                params.setMargin(params.getTxPower() - params.getSensitivity() - 
                                 (params.getDistance() * params.getFiberAttenuation()) - 
                                 params.getSplitterLoss() - 
                                 params.getConnectorLoss() - 
                                 params.getSpliceLoss());
                break;
        }
        
        return params;
    }
}
