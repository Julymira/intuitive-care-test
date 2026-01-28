package br.com.julyana.parser;

import br.com.julyana.model.LancamentoContabil;
import br.com.julyana.model.Operadora;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvProcessor {

    public List<LancamentoContabil> processar(InputStream inputStream) throws Exception {
        
        List<LancamentoContabil> despesas = new ArrayList<>();

        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setDelimiter(';') 
                .build();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
             CSVParser parser = new CSVParser(reader, format)) {

            for (CSVRecord record : parser) {
                LancamentoContabil lancamento = new LancamentoContabil();

                lancamento.setRegAns(record.get("REG_ANS"));
                lancamento.setCdContaContabil(record.get("CD_CONTA_CONTABIL"));
                lancamento.setDescricao(record.get("DESCRICAO"));

                String dataStr = record.get("DATA");
                if (dataStr != null && !dataStr.isEmpty()) {
                    lancamento.setData(LocalDate.parse(dataStr));
                }

                String valorStr = record.get("VL_SALDO_FINAL").replace(",", ".");
                lancamento.setSaldoFinal(new BigDecimal(valorStr));

                if (lancamento.isDespesaEvento()) {
                    despesas.add(lancamento);
                }
            }
        }

        return despesas; 
    }

    public Map<String, Operadora> carregarOperadoras(InputStream input) throws Exception {
        Map<String, Operadora> registros = new HashMap<>();

        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setDelimiter(';') 
                .build();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, "ISO-8859-1"));
             CSVParser parser = new CSVParser(reader, format)) {

            for (CSVRecord record : parser) {
                Operadora operadora = new Operadora();
                
                operadora.setRegAns(record.get("REGISTRO_OPERADORA"));
                operadora.setCnpj(record.get("CNPJ"));
                operadora.setRazaoSocial(record.get("Razao_Social"));

                registros.put(operadora.getRegAns(), operadora);
            }
        }
        
        return registros;
    }
}
