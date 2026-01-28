# Teste Técnico - Integração ANS (Parte 1)
**Candidata:** Julyana Mira Medeiros

## 🚀 Como Executar o Projeto

1. **Pré-requisitos:** Ter o Java 21 e o Maven instalados.
2. **Configuração:** O arquivo `pom.xml` já contém as dependências necessárias (Jsoup, Apache Commons CSV/IO, Apache POI).
3. **Execução:** Execute a classe `br.com.julyana.App`.
4. **O que o código fará:**
   - Criará a pasta `dados_ans/` na raiz do projeto.
   - Realizará o download do cadastro de operadoras (CADOP) e das Demonstrações Contábeis dos últimos 3 trimestres de 2025 diretamente do portal de Dados Abertos da ANS.
   - Extrairá os arquivos ZIP automaticamente.
   - Processará os dados, consolidará as despesas e gerará o arquivo `consolidado_despesas.zip` na raiz do projeto.

## 🛠 Decisões Técnicas e Trade-offs

### Processamento Incremental (Item 1.2)
**Escolha:** Processamento incremental via streams e leitura de arquivos físicos (`FileInputStream`).
**Justificativa:** Considerando o volume de dados das demonstrações contábeis (arquivos que somam dezenas de megabytes), o carregamento simultâneo em memória RAM poderia resultar em `OutOfMemoryError`. A abordagem escolhida lê cada arquivo individualmente, acumula os valores necessários em um mapa e libera os recursos, garantindo alta performance e baixo consumo de memória.

### Análise de Inconsistências (Item 1.3)
Durante a consolidação, foram aplicadas as seguintes tratativas:
- **CNPJs duplicados:** O sistema utiliza o `Registro_ANS` como identificador único para o mapeamento. Isso assegura que as somas sejam atribuídas corretamente à operadora correta, mesmo que ocorram variações na Razão Social ou duplicidade de CNPJs entre os períodos.
- **Valores zerados/negativos:** Realiza-se a soma algébrica dos saldos finais. Valores negativos são processados para refletir estornos ou ajustes contábeis presentes na fonte original.
- **Formatos de Data e Normalização:** O código extrai e normaliza as informações de período (Ano/Trimestre) para garantir a consistência do relatório final. Para os cálculos financeiros, utiliza-se `BigDecimal` para evitar a perda de precisão comum em tipos de ponto flutuante (`Double/Float`).
- **Saída:** O arquivo final foi gerado em formato CSV (delimitado por `;`) e compactado em ZIP conforme a especificação.

## 📂 Estrutura do Projeto
- `service`: Orquestração do download automático, extração de ZIPs e inteligência de negócio.
- `parser`: Lógica de leitura, filtragem de tipos de conta (Despesas com Eventos) e normalização de arquivos.
- `model`: Classes de representação de dados (Operadora e LançamentoContabil).