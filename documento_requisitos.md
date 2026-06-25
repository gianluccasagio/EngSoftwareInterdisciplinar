# Documento de Requisitos

Este documento contém os artefatos de requisitos solicitados para o sistema **Calculadora de Link Budget GPON**, atualizados de acordo com a implementação final do código.

## Lista de Requisitos

### Requisitos Funcionais (RF)
*   **RF1:** O sistema deve calcular qualquer variável da fórmula de Link Budget desde que as demais sejam fornecidas.
    *   *Status:* **Atendido**. A classe `LinkBudgetCalculadora` consegue isolar matematicamente e resolver a equação para qualquer um dos 8 parâmetros possíveis quando exatamente 1 é deixado em branco.
*   **RF2:** O sistema deve validar entradas com base em padrões de normas técnicas (ex: ITU-T G.984).
    *   *Status:* **Atendido**. A classe `ValidadorLimites` emite alertas se a atenuação, potência Tx, sensibilidade, distância ou margem de segurança estiverem fora dos padrões comerciais do padrão GPON (ex: Tx fora de +1.5 a +7 dBm, atenuação fora de 0.2 a 0.4 dB/km).

### Requisitos Não Funcionais (RNF)
*   **RNF1:** O diagrama de classes deve separar a interface de usuário da lógica matemática de propagação.
    *   *Status:* **Atendido**. O sistema utiliza o padrão MVC (Model-View-Controller). A view (`InterfaceGrafica`) e a lógica matemática (`LinkBudgetCalculadora`) são completamente isoladas, comunicando-se apenas através do `CalculadoraController`.

---

## 1. Diagrama de Caso de Uso

O diagrama de caso de uso descreve as interações entre o usuário (Ator) e o sistema.

```mermaid
flowchart LR
    User([Usuário])
    
    subgraph Calculadora Link Budget GPON
        UC1(Inserir parâmetros do Link Budget)
        UC2(Calcular variável faltante)
        UC3(Visualizar resultados calculados)
        UC4(Visualizar alertas de projeto)
        UC5(Limpar campos)
    end
    
    User --> UC1
    User --> UC2
    User --> UC5
    
    UC2 -. "<<include>>\n(se cálculo bem sucedido)" .-> UC3
    UC2 -. "<<include>>\n(validação de limites)" .-> UC4
```

### Descrição dos Casos de Uso:
*   **Inserir parâmetros do Link Budget:** O usuário preenche os campos com os valores conhecidos (Potência Tx, Sensibilidade Rx, Distância, Atenuação da Fibra, Perdas de Splitters, Conectores, Fusões e Margem de Segurança), deixando exatamente um campo em branco (que será calculado).
*   **Calcular variável faltante:** O usuário solicita o cálculo. O sistema identifica o campo vazio e aplica a fórmula correta do Link Budget.
*   **Visualizar resultados calculados:** O sistema preenche o campo que estava em branco com o resultado do cálculo.
*   **Visualizar alertas de projeto:** O sistema valida os valores (calculados ou inseridos) contra padrões típicos de GPON (ex: atenuação, distância) e exibe alertas caso algo esteja fora do comum ou represente um risco.
*   **Limpar campos:** O usuário solicita a limpeza de todos os campos de entrada e saída.

---

## 2. Diagrama de Classe

O diagrama de classe reflete a estrutura exata do código Java atual, seguindo a arquitetura MVC (Model-View-Controller) implementada.

```mermaid
classDiagram
    class ParametrosGPON {
        -Double txPower
        -Double sensitivity
        -Double distance
        -Double fiberAttenuation
        -Double splitterLoss
        -Double connectorLoss
        -Double spliceLoss
        -Double margin
        +getTxPower() Double
        +setTxPower(Double)
        +getSensitivity() Double
        +setSensitivity(Double)
        +getDistance() Double
        +setDistance(Double)
        +getFiberAttenuation() Double
        +setFiberAttenuation(Double)
        +getSplitterLoss() Double
        +setSplitterLoss(Double)
        +getConnectorLoss() Double
        +setConnectorLoss(Double)
        +getSpliceLoss() Double
        +setSpliceLoss(Double)
        +getMargin() Double
        +setMargin(Double)
        +getNullCount() int
        +getVariavelFaltante() String
    }

    class LinkBudgetCalculadora {
        +calcular(params: ParametrosGPON) ParametrosGPON
    }

    class ValidadorLimites {
        +validar(params: ParametrosGPON) List~String~
    }

    class CalculadoraController {
        -InterfaceGrafica view
        -LinkBudgetCalculadora calculadora
        -ValidadorLimites validador
        +CalculadoraController(view: InterfaceGrafica)
        +processarCalculo() void
    }

    class InterfaceGrafica {
        -JTextField txtTxPower
        -JTextField txtSensitivity
        -JTextField txtDistance
        -JTextField txtFiberAttenuation
        -JTextField txtSplitterLoss
        -JTextField txtConnectorLoss
        -JTextField txtSpliceLoss
        -JTextField txtMargin
        -JTextArea txtAlertas
        -CalculadoraController controller
        +InterfaceGrafica()
        -configurarInterface() void
        -adicionarCampo(painel: JPanel, label: String) JTextField
        -parseDoubleOrNull(field: JTextField) Double
        +getTxPower() Double
        +getSensitivity() Double
        +getDistance() Double
        +getFiberAttenuation() Double
        +getSplitterLoss() Double
        +getConnectorLoss() Double
        +getSpliceLoss() Double
        +getMargin() Double
        +atualizarResultados(params: ParametrosGPON) void
        +mostrarAlertas(alertas: List~String~) void
        +mostrarErro(titulo: String, mensagem: String) void
        +limparCampos() void
        +main(args: String[]) void
    }

    %% Relações do Controller
    CalculadoraController --> LinkBudgetCalculadora : "1. usa"
    CalculadoraController --> ValidadorLimites : "2. usa"
    CalculadoraController --> InterfaceGrafica : "4. atualiza"
    InterfaceGrafica --> CalculadoraController : "3. aciona"
    
    %% Uso do Model (ParametrosGPON) nas demais classes
    LinkBudgetCalculadora ..> ParametrosGPON : "recebe/retorna"
    ValidadorLimites ..> ParametrosGPON : "recebe"
    CalculadoraController ..> ParametrosGPON : "instancia/manipula"
    InterfaceGrafica ..> ParametrosGPON : "exibe"
```

### Arquitetura:
*   **Model (`com.gpon.model`)**: Contém as classes `ParametrosGPON` (DTO contendo as variáveis), `LinkBudgetCalculadora` (regras de negócio do cálculo) e `ValidadorLimites` (validação de regras de engenharia óptica).
*   **Controller (`com.gpon.controller`)**: O `CalculadoraController` orquestra a comunicação entre a View e os Models.
*   **View (`com.gpon.view`)**: A `InterfaceGrafica` cuida exclusivamente dos componentes Swing (Java) para interação visual com o usuário, sem conter lógica de negócios.
