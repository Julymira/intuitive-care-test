package br.com.julyana.parser;

import br.com.julyana.model.LancamentoContabil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvProcessor {

    public List<LancamentoContabil> processar(InputStream inputStream) throws Exception {
        
        List<LancamentoContabil> despesas = new ArrayList<>();

        // ... O resto do código

        return despesas; 
    }
}